'use client';

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import axios from "axios";
import { format, parseISO } from "date-fns";
import { esSoloNumeros, validarCUIT, esObligatorio } from "../validaciones/validaciones";

interface FacturaDTO {
  id: number;
  fecha: string;
  total: number;
  tipo: string;
  estado: string;
  responsablepago?: {
    tipo: string;
    dni?: string;
    tipoDocumento?: string;
    cuit?: string;
  };
}

interface NotaCreditoDTO {
  id?: number;
  importe: number;
  facturas?: FacturaDTO[];
  responsablepago?: {
    tipo: string;
    dni?: string;
    tipoDocumento?: string;
    cuit?: string;
  };
}

export default function IngresarNotaCredito() {
  const router = useRouter();
  
  // Estados del formulario
  const [cuit, setCuit] = useState("");
  const [tipoDocumento, setTipoDocumento] = useState("");
  const [numeroDocumento, setNumeroDocumento] = useState("");
  
  // Estados de errores
  const [errorCuit, setErrorCuit] = useState(false);
  const [errorNumeroDoc, setErrorNumeroDoc] = useState(false);
  const [errorBusqueda, setErrorBusqueda] = useState("");
  
  // Estados de facturas
  const [facturas, setFacturas] = useState<FacturaDTO[]>([]);
  const [facturasSeleccionadas, setFacturasSeleccionadas] = useState<number[]>([]);
  const [buscando, setBuscando] = useState(false);
  
  // Estados de nota de crédito
  const [notaCreditoGenerada, setNotaCreditoGenerada] = useState<NotaCreditoDTO | null>(null);
  const [procesando, setProcesando] = useState(false);
  const [mostrarDetalle, setMostrarDetalle] = useState(false);
  
  // Validaciones en tiempo real
  useEffect(() => {
    if (cuit !== "") {
      setErrorCuit(!validarCUIT(cuit));
    } else {
      setErrorCuit(false);
    }
  }, [cuit]);
  
  useEffect(() => {
    if (numeroDocumento !== "") {
      setErrorNumeroDoc(!esSoloNumeros(numeroDocumento));
    } else {
      setErrorNumeroDoc(false);
    }
  }, [numeroDocumento]);
  
  // Buscar facturas
  const buscarFacturas = async (e: React.FormEvent) => {
    e.preventDefault();
    
    // Validar que se complete al menos CUIT o Tipo y Nro Documento
    const tieneCuit = esObligatorio(cuit);
    const tieneTipoYNumero = esObligatorio(tipoDocumento) && esObligatorio(numeroDocumento);
    
    if (!tieneCuit && !tieneTipoYNumero) {
      setErrorBusqueda("Debe completar el CUIT o Tipo y Nro de Documento");
      return;
    }
    
    // Validar formato de CUIT si está completo
    if (tieneCuit && !validarCUIT(cuit)) {
      setErrorCuit(true);
      setErrorBusqueda("El CUIT ingresado no es válido");
      return;
    }
    
    // Validar formato de número de documento si está completo
    if (tieneTipoYNumero && !esSoloNumeros(numeroDocumento)) {
      setErrorNumeroDoc(true);
      setErrorBusqueda("El número de documento debe contener solo números");
      return;
    }
    
    setBuscando(true);
    setErrorBusqueda("");
    
    try {
      // Construir parámetros para filtrar facturas
      const params: any = {};
      if (tieneCuit) {
        params.cuit = cuit.trim();
      }
      if (tieneTipoYNumero) {
        params.tipoDocumento = tipoDocumento.trim();
        params.numeroDocumento = numeroDocumento.trim();
      }
      
      // Buscar facturas - asumiendo que hay un endpoint que acepta estos parámetros
      // Si no existe, podría ser necesario obtener todas y filtrar en el frontend
      const response = await axios.get("http://localhost:8080/facturas", {
        params
      });
      
      const facturasData: FacturaDTO[] = response.data || [];
      
      // Filtrar solo facturas pendientes
      const facturasPendientes = facturasData.filter((f: FacturaDTO) => 
        f.estado?.toUpperCase() === "PENDIENTE"
      );
      
      setFacturas(facturasPendientes);
      setFacturasSeleccionadas([]);
      
      if (facturasPendientes.length === 0) {
        setErrorBusqueda("No hay facturas pendientes de pago a nombre de la persona ingresada");
      }
    } catch (error: any) {
      console.error("Error al buscar facturas:", error);
      setErrorBusqueda("Error al buscar facturas. Verifique los datos ingresados.");
      setFacturas([]);
    } finally {
      setBuscando(false);
    }
  };
  
  // Toggle selección de factura
  const toggleSeleccionFactura = (idFactura: number) => {
    setFacturasSeleccionadas((prev) => {
      if (prev.includes(idFactura)) {
        return prev.filter((id) => id !== idFactura);
      } else {
        return [...prev, idFactura];
      }
    });
  };
  
  // Calcular suma de importes seleccionados
  const calcularSumaImportes = () => {
    return facturas
      .filter((f) => facturasSeleccionadas.includes(f.id))
      .reduce((suma, f) => suma + f.total, 0);
  };
  
  // Generar nota de crédito
  const generarNotaCredito = async () => {
    if (facturasSeleccionadas.length === 0) {
      setErrorBusqueda("Debe seleccionar al menos una factura");
      return;
    }
    
    setProcesando(true);
    setErrorBusqueda("");
    
    try {
      const sumaImportes = calcularSumaImportes();
      
      const notaCreditoDTO = {
        importe: sumaImportes,
        facturas: facturasSeleccionadas.map((id) => ({ id }))
      };
      
      const response = await axios.post("http://localhost:8080/api/notas-crédito", notaCreditoDTO, {
        headers: {
          "Content-Type": "application/json"
        }
      });
      
      if (response.status === 200 || response.status === 201) {
        // Obtener la nota de crédito generada con todos sus datos
        const notaCredito = response.data;
        setNotaCreditoGenerada({
          id: notaCredito.id,
          importe: sumaImportes,
          facturas: facturas.filter((f) => facturasSeleccionadas.includes(f.id)),
          responsablepago: facturas[0]?.responsablepago
        });
        setMostrarDetalle(true);
      }
    } catch (error: any) {
      console.error("Error al generar nota de crédito:", error);
      setErrorBusqueda(error.response?.data?.message || "Error al generar la nota de crédito");
    } finally {
      setProcesando(false);
    }
  };
  
  // Manejar Shift+Tab
  const handleKeyDownInput = (e: React.KeyboardEvent<HTMLInputElement | HTMLSelectElement>) => {
    if (e.key === "Tab" && e.shiftKey) {
      e.preventDefault();
      const form = e.currentTarget.form;
      if (form) {
        const inputs = Array.from(form.querySelectorAll("input, select"));
        const index = inputs.indexOf(e.currentTarget);
        if (index > 0) {
          (inputs[index - 1] as HTMLElement).focus();
        }
      }
    }
  };
  
  // Calcular IVA (21% si es factura tipo A)
  const calcularIVA = (factura: FacturaDTO) => {
    if (factura.tipo?.toUpperCase() === "A") {
      // IVA del 21% sobre el importe neto
      // Si el total incluye IVA: neto = total / 1.21, IVA = total - neto
      const neto = factura.total / 1.21;
      return factura.total - neto;
    }
    return 0;
  };
  
  // Calcular importe neto
  const calcularImporteNeto = (factura: FacturaDTO) => {
    if (factura.tipo?.toUpperCase() === "A") {
      return factura.total / 1.21;
    }
    return factura.total;
  };
  
  // Obtener nombre del responsable
  const obtenerNombreResponsable = (factura: FacturaDTO) => {
    if (!factura.responsablepago) {
      return "Sin responsable";
    }
    
    if (factura.responsablepago.tipo === "FISICA") {
      return `${factura.responsablepago.dni || ""} - ${factura.responsablepago.tipoDocumento || ""}`;
    } else {
      return factura.responsablepago.cuit || "Persona Jurídica";
    }
  };
  
  return (
    <main className="flex gap-8 px-8 py-8 items-start bg-gradient-to-br from-gray-50 to-gray-100 min-h-screen">
      {/* FORMULARIO DE BÚSQUEDA */}
      {!mostrarDetalle && (
        <div className="bg-white rounded-xl shadow-lg p-6 w-80 h-fit sticky top-4">
          <h2 className="text-2xl font-bold text-indigo-950 mb-6 pb-3 border-b-2 border-indigo-200">
            Ingresar Nota de Crédito
          </h2>
          <form onSubmit={buscarFacturas} className="flex flex-col space-y-4">
            <div>
              <label className="text-indigo-950 font-semibold mb-2 block text-sm">CUIT:</label>
              <input
                type="text"
                value={cuit}
                onChange={(e) => {
                  const valor = e.target.value.toUpperCase();
                  setCuit(valor);
                  setErrorCuit(false);
                  setErrorBusqueda("");
                }}
                onKeyDown={handleKeyDownInput}
                className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                  errorCuit
                    ? "border-red-500 bg-red-50"
                    : "border-gray-300 hover:border-indigo-400"
                }`}
                placeholder="XX-XXXXXXXX-X"
              />
              {errorCuit && (
                <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                  <span>⚠</span> CUIT inválido. Formato: XX-XXXXXXXX-X
                </p>
              )}
            </div>
            
            <div>
              <label className="text-indigo-950 font-semibold mb-2 block text-sm">Tipo Documento:</label>
              <select
                value={tipoDocumento}
                onChange={(e) => {
                  setTipoDocumento(e.target.value.toUpperCase());
                  setErrorBusqueda("");
                }}
                onKeyDown={handleKeyDownInput}
                className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              >
                <option value="">Seleccionar tipo</option>
                <option value="DNI">DNI</option>
                <option value="PASAPORTE">PASAPORTE</option>
                <option value="LE">LE</option>
                <option value="LC">LC</option>
              </select>
            </div>
            
            <div>
              <label className="text-indigo-950 font-semibold mb-2 block text-sm">Nro Documento:</label>
              <input
                type="text"
                value={numeroDocumento}
                onChange={(e) => {
                  const valor = e.target.value.toUpperCase();
                  setNumeroDocumento(valor);
                  setErrorNumeroDoc(false);
                  setErrorBusqueda("");
                }}
                onKeyDown={handleKeyDownInput}
                className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                  errorNumeroDoc
                    ? "border-red-500 bg-red-50"
                    : "border-gray-300 hover:border-indigo-400"
                }`}
                placeholder="Ingrese el número de documento"
              />
              {errorNumeroDoc && (
                <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                  <span>⚠</span> Ingrese solo números.
                </p>
              )}
            </div>
            
            {errorBusqueda && (
              <div className="bg-red-50 border-l-4 border-red-500 text-red-700 p-3 rounded-lg">
                <p className="text-sm font-semibold">{errorBusqueda}</p>
              </div>
            )}
            
            <button
              type="submit"
              disabled={buscando}
              className={`w-full px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105 ${
                buscando ? "opacity-50 cursor-not-allowed" : ""
              }`}
            >
              {buscando ? "Buscando..." : "BUSCAR"}
            </button>
          </form>
        </div>
      )}
      
      {/* CONTENIDO PRINCIPAL */}
      <section className="flex-1">
        {!mostrarDetalle && (
          <>
            {/* LISTA DE FACTURAS */}
            {facturas.length > 0 && (
              <div className="bg-white rounded-xl shadow-xl overflow-hidden">
                <div className="p-6 border-b-2 border-indigo-200">
                  <h2 className="text-2xl font-bold text-indigo-950">
                    Facturas Pendientes
                  </h2>
                  <p className="text-sm text-gray-600 mt-1">
                    {facturas.length} factura(s) pendiente(s) encontrada(s)
                  </p>
                </div>
                
                <div className="max-h-[600px] overflow-y-auto">
                  <table className="w-full border-collapse">
                    <thead className="bg-gradient-to-r from-indigo-950 to-indigo-900 text-white sticky top-0 z-10 shadow-md">
                      <tr>
                        <th className="p-4 font-semibold text-left">Seleccionar</th>
                        <th className="p-4 font-semibold text-left">Nro Factura</th>
                        <th className="p-4 font-semibold text-left">Fecha</th>
                        <th className="p-4 font-semibold text-left">Importe Neto</th>
                        <th className="p-4 font-semibold text-left">IVA</th>
                        <th className="p-4 font-semibold text-left">Importe Total</th>
                      </tr>
                    </thead>
                    <tbody>
                      {facturas.map((f: FacturaDTO) => {
                        const esSeleccionada = facturasSeleccionadas.includes(f.id);
                        const importeNeto = calcularImporteNeto(f);
                        const iva = calcularIVA(f);
                        
                        return (
                          <tr
                            key={f.id}
                            className={`hover:bg-indigo-50 transition-colors cursor-pointer ${
                              esSeleccionada ? "bg-indigo-100" : "bg-white"
                            }`}
                            onClick={() => toggleSeleccionFactura(f.id)}
                          >
                            <td className="p-4 border-b border-gray-200 text-center">
                              <input
                                type="checkbox"
                                checked={esSeleccionada}
                                onChange={() => toggleSeleccionFactura(f.id)}
                                className="w-4 h-4 text-indigo-950 focus:ring-indigo-500"
                              />
                            </td>
                            <td className="p-4 border-b border-gray-200 font-medium text-indigo-950">
                              {f.id}
                            </td>
                            <td className="p-4 border-b border-gray-200 text-gray-700">
                              {format(parseISO(f.fecha), "dd/MM/yyyy")}
                            </td>
                            <td className="p-4 border-b border-gray-200 text-gray-700">
                              ${importeNeto.toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                            </td>
                            <td className="p-4 border-b border-gray-200 text-gray-700">
                              {f.tipo?.toUpperCase() === "A" 
                                ? `$${iva.toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
                                : "-"
                              }
                            </td>
                            <td className="p-4 border-b border-gray-200 text-gray-700 font-semibold">
                              ${f.total.toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
                
                {/* SUMA DE IMPORTES SELECCIONADOS */}
                {facturasSeleccionadas.length > 0 && (
                  <div className="p-6 bg-indigo-50 border-t-2 border-indigo-200">
                    <div className="flex justify-between items-center">
                      <div>
                        <p className="text-sm text-gray-600">Facturas seleccionadas:</p>
                        <p className="text-lg font-semibold text-indigo-950">
                          {facturasSeleccionadas.length} factura(s)
                        </p>
                      </div>
                      <div className="text-right">
                        <p className="text-sm text-gray-600">Suma de importes:</p>
                        <p className="text-2xl font-bold text-indigo-950">
                          ${calcularSumaImportes().toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                        </p>
                      </div>
                    </div>
                  </div>
                )}
              </div>
            )}
            
            {/* BOTONES */}
            <div className="flex justify-center gap-4 mt-6">
              <Link href="/menu">
                <button className="px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold">
                  Cancelar
                </button>
              </Link>
              
              <button
                onClick={generarNotaCredito}
                disabled={facturasSeleccionadas.length === 0 || procesando}
                className={`px-6 py-3 bg-gradient-to-r from-green-600 to-green-700 text-white rounded-lg hover:from-green-700 hover:to-green-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105 ${
                  facturasSeleccionadas.length === 0 || procesando ? "opacity-50 cursor-not-allowed" : ""
                }`}
              >
                {procesando ? "Procesando..." : "ACEPTAR"}
              </button>
            </div>
          </>
        )}
        
        {/* DETALLE DE NOTA DE CRÉDITO GENERADA */}
        {mostrarDetalle && notaCreditoGenerada && (
          <div className="bg-white rounded-xl shadow-xl p-6">
            <div className="mb-6 pb-4 border-b-2 border-indigo-200">
              <h2 className="text-2xl font-bold text-indigo-950">
                Nota de Crédito Generada
              </h2>
            </div>
            
            <div className="space-y-4 mb-6">
              <div className="p-4 bg-indigo-50 rounded-lg">
                <p className="text-sm text-gray-600">Nro. de Nota de Crédito:</p>
                <p className="text-xl font-bold text-indigo-950">{notaCreditoGenerada.id}</p>
              </div>
              
              <div className="p-4 bg-indigo-50 rounded-lg">
                <p className="text-sm text-gray-600">Responsable de Pago:</p>
                <p className="text-lg font-semibold text-indigo-950">
                  {notaCreditoGenerada.responsablepago?.tipo === "FISICA"
                    ? `${notaCreditoGenerada.responsablepago.dni || ""} - ${notaCreditoGenerada.responsablepago.tipoDocumento || ""}`
                    : notaCreditoGenerada.responsablepago?.cuit || "Sin responsable"}
                </p>
              </div>
              
              <div className="p-4 bg-indigo-50 rounded-lg">
                <p className="text-sm text-gray-600">Importe Neto:</p>
                <p className="text-xl font-semibold text-indigo-950">
                  ${(notaCreditoGenerada.importe / 1.21).toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                </p>
              </div>
              
              <div className="p-4 bg-indigo-50 rounded-lg">
                <p className="text-sm text-gray-600">IVA:</p>
                <p className="text-xl font-semibold text-indigo-950">
                  ${(notaCreditoGenerada.importe - (notaCreditoGenerada.importe / 1.21)).toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                </p>
              </div>
              
              <div className="p-4 bg-indigo-100 rounded-lg border-2 border-indigo-300">
                <p className="text-sm text-gray-600">Importe Total:</p>
                <p className="text-3xl font-bold text-indigo-950">
                  ${notaCreditoGenerada.importe.toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                </p>
              </div>
            </div>
            
            <div className="flex justify-center mt-6">
              <button
                onClick={() => {
                  setMostrarDetalle(false);
                  setNotaCreditoGenerada(null);
                  setFacturas([]);
                  setFacturasSeleccionadas([]);
                  setCuit("");
                  setTipoDocumento("");
                  setNumeroDocumento("");
                  setErrorBusqueda("");
                }}
                className="px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105"
              >
                ACEPTAR
              </button>
            </div>
          </div>
        )}
      </section>
    </main>
  );
}

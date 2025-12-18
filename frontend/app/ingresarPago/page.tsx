'use client';

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import axios from "../lib/axios";
import { format, parseISO } from "date-fns";
import { esSoloNumeros, esObligatorio } from "../validaciones/validaciones";

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

interface MedioPagoSeleccionado {
  tipo: string;
  monto: number;
  fecha: string;
  // Moneda
  tipoMoneda?: string;
  cotizacion?: number;
  montoConvertido?: number; // Monto en ARS para cálculos internos
  // Tarjeta crédito
  banco?: string;
  cuotas?: number;
  // Tarjeta débito
  dniTitular?: string;
  // Cheque
  numeroCheque?: number;
  plazo?: string;
}

export default function IngresarPago() {
  const router = useRouter();
  
  // Estados del formulario
  const [nroHabitacion, setNroHabitacion] = useState("");
  const [errorNroHabitacion, setErrorNroHabitacion] = useState(false);
  const [errorNroHabitacionObligatorio, setErrorNroHabitacionObligatorio] = useState(false);
  const [mensajeError, setMensajeError] = useState("");
  
  // Estados de facturas
  const [facturas, setFacturas] = useState<FacturaDTO[]>([]);
  const [facturaSeleccionada, setFacturaSeleccionada] = useState<FacturaDTO | null>(null);
  const [buscando, setBuscando] = useState(false);
  
  // Estados de pago
  const [medioPagoSeleccionado, setMedioPagoSeleccionado] = useState<string>("");
  const [mediosPago, setMediosPago] = useState<MedioPagoSeleccionado[]>([]);
  const [medioPagoActual, setMedioPagoActual] = useState<MedioPagoSeleccionado>({
    tipo: "",
    monto: 0,
    fecha: new Date().toISOString().split('T')[0]
  });
  
  // Estados de cálculo
  const [totalAcumulado, setTotalAcumulado] = useState(0);
  const [vuelto, setVuelto] = useState(0);
  const [totalAPagar, setTotalAPagar] = useState(0);
  
  // Estados de UI
  const [mostrarCartelSaldada, setMostrarCartelSaldada] = useState(false);
  const [procesando, setProcesando] = useState(false);
  
  // Validar número de habitación
  const validarNroHabitacion = () => {
    const vacio = !esObligatorio(nroHabitacion);
    const invalido = nroHabitacion !== "" && !esSoloNumeros(nroHabitacion);
    
    setErrorNroHabitacionObligatorio(vacio);
    setErrorNroHabitacion(invalido);
    
    if (vacio) {
      setMensajeError("Número de habitación faltante");
      return false;
    }
    if (invalido) {
      setMensajeError("Número de habitación incorrecto");
      return false;
    }
    return true;
  };
  
  // Buscar facturas pendientes
  const buscarFacturas = async () => {
    if (!validarNroHabitacion()) {
      return;
    }
    
    setBuscando(true);
    setMensajeError("");
    
    try {
      const response = await axios.get(`http://localhost:8080/facturas/habitacion/${nroHabitacion}`);
      const facturasData: FacturaDTO[] = response.data || [];
      console.log("Facturas recibidas:", facturasData);
      // Filtrar solo facturas pendientes
      const facturasPendientes = facturasData.filter((f: FacturaDTO) => 
        f.estado?.toUpperCase() === "PENDIENTE"
      );
      
      setFacturas(facturasPendientes);
      
      if (facturasPendientes.length === 0) {
        setMensajeError("No existen facturas pendientes de pago");
      }
    } catch (error: any) {
      console.error("Error al buscar facturas:", error);
      setMensajeError("Error al buscar facturas. Verifique el número de habitación.");
      setFacturas([]);
    } finally {
      setBuscando(false);
    }
  };
  
  // Seleccionar factura
  const seleccionarFactura = (factura: FacturaDTO) => {
    setFacturaSeleccionada(factura);
    setTotalAPagar(factura.total);
    setTotalAcumulado(0);
    setVuelto(0);
    setMediosPago([]);
    setMedioPagoSeleccionado("");
    setMedioPagoActual({
      tipo: "",
      monto: 0,
      fecha: new Date().toISOString().split('T')[0]
    });
  };
  
  // Manejar cambio de medio de pago
  const handleMedioPagoChange = (tipo: string) => {
    setMedioPagoSeleccionado(tipo);
    setMedioPagoActual({
      tipo: tipo,
      monto: 0,
      fecha: new Date().toISOString().split('T')[0],
      tipoMoneda: tipo === "MONEDA_LOCAL" ? "ARS" : undefined,
      cotizacion: undefined,
      banco: undefined,
      cuotas: undefined,
      dniTitular: undefined,
      numeroCheque: undefined,
      plazo: undefined
    });
  };
  
  // Agregar medio de pago
  const agregarMedioPago = () => {
    if (!medioPagoSeleccionado) {
      setMensajeError("Debe seleccionar un medio de pago");
      return;
    }
    
    // Validar campos según el tipo de medio
    const errores: string[] = [];
    
    if (medioPagoActual.monto <= 0) {
      errores.push("Importe");
    }
    
    if (!medioPagoActual.fecha) {
      errores.push("Fecha");
    }
    
    if (medioPagoSeleccionado === "MONEDA_LOCAL" || medioPagoSeleccionado === "MONEDA_EXTRANJERA") {
      if (!medioPagoActual.tipoMoneda) {
        errores.push("Moneda");
      }
    }
    
    if (medioPagoSeleccionado === "MONEDA_EXTRANJERA") {
      if (!medioPagoActual.cotizacion || medioPagoActual.cotizacion <= 0) {
        errores.push("Cotización");
      }
    }
    
    if (medioPagoSeleccionado === "TARJETA_CREDITO") {
      if (!medioPagoActual.banco) {
        errores.push("Banco");
      }
      if (!medioPagoActual.cuotas || medioPagoActual.cuotas <= 0) {
        errores.push("Cuotas");
      }
    }
    
    if (medioPagoSeleccionado === "TARJETA_DEBITO") {
      if (!medioPagoActual.banco) {
        errores.push("Banco");
      }
      if (!medioPagoActual.dniTitular) {
        errores.push("DNI Titular");
      }
    }
    
    if (medioPagoSeleccionado === "CHEQUE") {
      if (!medioPagoActual.numeroCheque || medioPagoActual.numeroCheque <= 0) {
        errores.push("Número de cheque");
      }
      if (!medioPagoActual.banco) {
        errores.push("Banco");
      }
      if (!medioPagoActual.plazo) {
        errores.push("Fecha de cobro");
      }
    }
    
    if (errores.length > 0) {
      setMensajeError(`Faltan los siguientes datos: ${errores.join(", ")}`);
      return;
    }
    
    // Calcular monto en ARS (si es moneda extranjera, multiplicar por cotización)
    let montoEnARS = medioPagoActual.monto;
    if (medioPagoSeleccionado === "MONEDA_EXTRANJERA" && medioPagoActual.cotizacion) {
      montoEnARS = medioPagoActual.monto * medioPagoActual.cotizacion;
    }
    
    // Agregar el medio de pago (guardar el monto original, pero usar el convertido para cálculos)
    const medioPagoAGuardar = {
      ...medioPagoActual,
      montoConvertido: montoEnARS // Guardar el monto convertido para cálculos
    };
    setMediosPago([...mediosPago, medioPagoAGuardar]);
    
    // Actualizar total acumulado usando el monto convertido
    const nuevoTotal = totalAcumulado + montoEnARS;
    setTotalAcumulado(nuevoTotal);
    
    // Calcular vuelto y actualizar total a pagar
    const deudaOriginal = facturaSeleccionada!.total;
    if (nuevoTotal >= deudaOriginal) {
      setVuelto(nuevoTotal - deudaOriginal);
      setTotalAPagar(0);
    } else {
      setVuelto(0);
      setTotalAPagar(deudaOriginal - nuevoTotal);
    }
    
    // Limpiar formulario
    setMedioPagoActual({
      tipo: "",
      monto: 0,
      fecha: new Date().toISOString().split('T')[0]
    });
    setMedioPagoSeleccionado("");
  };
  
  // Eliminar medio de pago
  const eliminarMedioPago = (index: number) => {
    const medio = mediosPago[index];
    const nuevosMedios = mediosPago.filter((_, i) => i !== index);
    setMediosPago(nuevosMedios);
    
    // Recalcular totales (usar monto convertido si existe, sino el monto original)
    const montoAEliminar = (medio as any).montoConvertido || medio.monto;
    const nuevoTotal = totalAcumulado - montoAEliminar;
    setTotalAcumulado(nuevoTotal);
    
    const deudaOriginal = facturaSeleccionada!.total;
    if (nuevoTotal >= deudaOriginal) {
      setVuelto(nuevoTotal - deudaOriginal);
      setTotalAPagar(0);
    } else {
      setVuelto(0);
      setTotalAPagar(deudaOriginal - nuevoTotal);
    }
  };
  
  // Procesar pago
  const procesarPago = async () => {
    if (!facturaSeleccionada) {
      return;
    }
    
    if (totalAcumulado < facturaSeleccionada.total) {
      // Según el caso de uso, si el monto es menor, se actualiza el total a pagar y vuelve al punto 6
      // Esto ya está manejado automáticamente en agregarMedioPago
      setMensajeError("El monto acumulado es menor a la deuda. Debe agregar más medios de pago.");
      return;
    }
    
    if (mediosPago.length === 0) {
      setMensajeError("Debe agregar al menos un medio de pago.");
      return;
    }
    
    setProcesando(true);
    
    try {
      // Construir DTO según el formato esperado
      const pagoDTO = {
        monto: totalAcumulado,
        fecha: new Date().toISOString().split('T')[0],
        factura: {
          id: facturaSeleccionada.id
        },
        medios: mediosPago.map(medio => {
          const medioDTO: any = {
            tipo: medio.tipo,
            monto: medio.monto,
            fecha: medio.fecha
          };
          
          if (medio.tipo === "MONEDA_LOCAL" || medio.tipo === "MONEDA_EXTRANJERA") {
            medioDTO.tipoMoneda = medio.tipoMoneda;
          }
          
          if (medio.tipo === "MONEDA_EXTRANJERA") {
            medioDTO.cotizacion = medio.cotizacion;
          }
          
          if (medio.tipo === "TARJETA_CREDITO") {
            medioDTO.banco = medio.banco;
            medioDTO.cuotas = medio.cuotas;
          }
          
          if (medio.tipo === "TARJETA_DEBITO") {
            medioDTO.banco = medio.banco;
            medioDTO.dniTitular = medio.dniTitular;
          }
          
          if (medio.tipo === "CHEQUE") {
            medioDTO.numeroCheque = medio.numeroCheque;
            medioDTO.banco = medio.banco;
            medioDTO.plazo = medio.plazo; // Fecha de cobro del cheque
          }
          
          return medioDTO;
        })
      };
      
      const response = await axios.post("http://localhost:8080/pagos", pagoDTO, {
        headers: {
          "Content-Type": "application/json"
        }
      });
      
      if (response.status === 200) {
        setMostrarCartelSaldada(true);
      }
    } catch (error: any) {
      console.error("Error al procesar pago:", error);
      setMensajeError(error.response?.data?.message || "Error al procesar el pago");
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
      <div className="bg-white rounded-xl shadow-lg p-6 w-80 h-fit sticky top-4">
        <h2 className="text-2xl font-bold text-indigo-950 mb-6 pb-3 border-b-2 border-indigo-200">
          Ingresar Pago
        </h2>
        <form onSubmit={(e) => { e.preventDefault(); buscarFacturas(); }} className="flex flex-col space-y-4">
          <div>
            <label className="text-indigo-950 font-semibold mb-2 block text-sm">Número de habitación:</label>
            <input
              type="text"
              value={nroHabitacion}
              onChange={(e) => {
                const valor = e.target.value.toUpperCase();
                setNroHabitacion(valor);
                setErrorNroHabitacionObligatorio(false);
                setErrorNroHabitacion(false);
                setMensajeError("");
              }}
              onKeyDown={handleKeyDownInput}
              className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                errorNroHabitacion || errorNroHabitacionObligatorio
                  ? "border-red-500 bg-red-50"
                  : "border-gray-300 hover:border-indigo-400"
              }`}
              placeholder="Ingrese el número de habitación"
            />
            {errorNroHabitacionObligatorio && (
              <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                <span>⚠</span> {mensajeError || "El campo número de habitación no puede estar vacío"}
              </p>
            )}
            {errorNroHabitacion && !errorNroHabitacionObligatorio && (
              <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                <span>⚠</span> {mensajeError || "Número de habitación incorrecto"}
              </p>
            )}
          </div>
          
          <button
            type="submit"
            disabled={buscando}
            className={`w-full px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105 ${
              buscando ? "opacity-50 cursor-not-allowed" : ""
            }`}
          >
            {buscando ? "Buscando..." : "Buscar Facturas"}
          </button>
        </form>
      </div>
      
      {/* CONTENIDO PRINCIPAL */}
      <section className="flex-1">
        {mensajeError && !facturaSeleccionada && (
          <div className="bg-red-50 border-l-4 border-red-500 text-red-700 p-4 rounded-lg mb-4 shadow-md">
            <p className="font-semibold">{mensajeError}</p>
          </div>
        )}
        
        {/* LISTA DE FACTURAS */}
        {facturas.length > 0 && !facturaSeleccionada && (
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
                    <th className="p-4 font-semibold text-left">ID Factura</th>
                    <th className="p-4 font-semibold text-left">Fecha</th>
                    <th className="p-4 font-semibold text-left">Total</th>
                    <th className="p-4 font-semibold text-left">Tipo</th>
                    <th className="p-4 font-semibold text-left">Responsable</th>
                  </tr>
                </thead>
                <tbody>
                  {facturas.map((f: FacturaDTO) => {
                    const facturaId = f.id;
                    const esSeleccionada = facturaSeleccionada?.id === facturaId;
                    return (
                    <tr
                      key={facturaId}
                      className="bg-white hover:bg-indigo-50 transition-colors cursor-pointer"
                      onClick={() => seleccionarFactura(f)}
                    >
                      <td className="p-4 border-b border-gray-200 text-center">
                        <input
                          type="radio"
                          name="factura"
                          checked={esSeleccionada}
                          onChange={() => seleccionarFactura(f)}
                          className="w-4 h-4 text-indigo-950 focus:ring-indigo-500"
                        />
                      </td>
                      <td className="p-4 border-b border-gray-200 font-medium text-indigo-950">
                        {facturaId}
                      </td>
                      <td className="p-4 border-b border-gray-200 text-gray-700">
                        {format(parseISO(f.fecha), "dd/MM/yyyy")}
                      </td>
                      <td className="p-4 border-b border-gray-200 text-gray-700 font-semibold">
                        ${f.total.toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                      </td>
                      <td className="p-4 border-b border-gray-200 text-gray-700">
                        {f.tipo}
                      </td>
                      <td className="p-4 border-b border-gray-200 text-gray-700">
                        {obtenerNombreResponsable(f)}
                      </td>
                    </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        )}
        
        {/* FORMULARIO DE PAGO */}
        {facturaSeleccionada && (
          <div className="bg-white rounded-xl shadow-xl p-6">
            <div className="mb-6 pb-4 border-b-2 border-indigo-200">
              <h2 className="text-2xl font-bold text-indigo-950">
                Procesar Pago
              </h2>
            </div>
            
            {/* INFORMACIÓN DE LA FACTURA */}
            <div className="mb-6 p-4 bg-indigo-50 rounded-lg">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-sm text-gray-600">Número de Factura:</p>
                  <p className="text-lg font-semibold text-indigo-950">{facturaSeleccionada.id}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600">Responsable:</p>
                  <p className="text-lg font-semibold text-indigo-950">{obtenerNombreResponsable(facturaSeleccionada)}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600">Total a Pagar:</p>
                  <p className="text-2xl font-bold text-indigo-950">
                    ${totalAPagar.toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </p>
                </div>
                <div>
                  <p className="text-sm text-gray-600">Vuelto:</p>
                  <p className="text-2xl font-bold text-green-600">
                    ${vuelto.toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </p>
                </div>
              </div>
            </div>
            
            {/* MEDIOS DE PAGO AGREGADOS */}
            {mediosPago.length > 0 && (
              <div className="mb-6">
                <h3 className="text-lg font-semibold text-indigo-950 mb-3">Medios de Pago Utilizados:</h3>
                <div className="space-y-2">
                  {mediosPago.map((medio, index) => {
                    const montoMostrar = medio.tipo === "MONEDA_EXTRANJERA" && medio.montoConvertido
                      ? medio.montoConvertido
                      : medio.monto;
                    const montoOriginal = medio.tipo === "MONEDA_EXTRANJERA" ? medio.monto : null;
                    
                    return (
                    <div key={index} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                      <div className="flex-1">
                        <p className="font-semibold text-indigo-950">
                          {medio.tipo}
                          {medio.tipo === "MONEDA_EXTRANJERA" && medio.tipoMoneda && ` (${medio.tipoMoneda})`}
                        </p>
                        {medio.tipo === "MONEDA_EXTRANJERA" && montoOriginal && (
                          <p className="text-xs text-gray-500">
                            {montoOriginal.toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })} {medio.tipoMoneda} × {medio.cotizacion} = 
                          </p>
                        )}
                        <p className="text-sm text-gray-600">
                          ${montoMostrar.toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })} ARS
                        </p>
                      </div>
                      <button
                        onClick={() => eliminarMedioPago(index)}
                        className="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600 transition text-sm"
                      >
                        Eliminar
                      </button>
                    </div>
                    );
                  })}
                </div>
                <div className="mt-4 p-3 bg-indigo-100 rounded-lg">
                  <p className="text-sm text-gray-600">Total Acumulado:</p>
                  <p className="text-xl font-bold text-indigo-950">
                    ${totalAcumulado.toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </p>
                </div>
              </div>
            )}
            
            {/* FORMULARIO DE MEDIO DE PAGO */}
            <div className="mb-6 p-4 border-2 border-indigo-200 rounded-lg">
              <h3 className="text-lg font-semibold text-indigo-950 mb-4">Agregar Medio de Pago</h3>
              
              <div className="mb-4">
                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Medio de Pago:</label>
                <select
                  value={medioPagoSeleccionado}
                  onChange={(e) => handleMedioPagoChange(e.target.value)}
                  className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">Seleccionar medio de pago</option>
                  <option value="MONEDA_LOCAL">Moneda</option>
                  <option value="CHEQUE">Cheques</option>
                  <option value="TARJETA_CREDITO">Tarjetas de crédito</option>
                  <option value="TARJETA_DEBITO">Tarjetas de débito</option>
                  <option value="MONEDA_EXTRANJERA">Moneda Extranjera</option>
                </select>
              </div>
              
              {medioPagoSeleccionado && (
                <div className="space-y-4">
                  <div>
                    <label className="text-indigo-950 font-semibold mb-2 block text-sm">Importe:</label>
                    <input
                      type="number"
                      step="0.01"
                      min="0"
                      value={medioPagoActual.monto || ""}
                      onChange={(e) => setMedioPagoActual({ ...medioPagoActual, monto: parseFloat(e.target.value) || 0 })}
                      onKeyDown={handleKeyDownInput}
                      className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      placeholder="0.00"
                    />
                  </div>
                  
                  <div>
                    <label className="text-indigo-950 font-semibold mb-2 block text-sm">Fecha:</label>
                    <input
                      type="date"
                      value={medioPagoActual.fecha}
                      onChange={(e) => setMedioPagoActual({ ...medioPagoActual, fecha: e.target.value })}
                      onKeyDown={handleKeyDownInput}
                      className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                  </div>
                  
                  {/* Campos específicos según medio de pago */}
                  {medioPagoSeleccionado === "MONEDA_LOCAL" && (
                    <div>
                      <label className="text-indigo-950 font-semibold mb-2 block text-sm">Moneda:</label>
                      <select
                        value={medioPagoActual.tipoMoneda || ""}
                        onChange={(e) => setMedioPagoActual({ ...medioPagoActual, tipoMoneda: e.target.value })}
                        onKeyDown={handleKeyDownInput}
                        className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      >
                        <option value="">Seleccionar moneda</option>
                        <option value="ARS">ARS (Pesos Argentinos)</option>
                      </select>
                    </div>
                  )}
                  
                  {medioPagoSeleccionado === "MONEDA_EXTRANJERA" && (
                    <div>
                      <label className="text-indigo-950 font-semibold mb-2 block text-sm">Moneda:</label>
                      <select
                        value={medioPagoActual.tipoMoneda || ""}
                        onChange={(e) => setMedioPagoActual({ ...medioPagoActual, tipoMoneda: e.target.value })}
                        onKeyDown={handleKeyDownInput}
                        className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      >
                        <option value="">Seleccionar moneda</option>
                        <option value="USD">USD (Dólares)</option>
                        <option value="EUR">EUR (Euros)</option>
                        <option value="BRL">BRL (Reales)</option>
                      </select>
                    </div>
                  )}
                  
                  {medioPagoSeleccionado === "MONEDA_EXTRANJERA" && (
                    <div>
                      <label className="text-indigo-950 font-semibold mb-2 block text-sm">Cotización:</label>
                      <input
                        type="number"
                        step="0.01"
                        min="0"
                        value={medioPagoActual.cotizacion || ""}
                        onChange={(e) => setMedioPagoActual({ ...medioPagoActual, cotizacion: parseFloat(e.target.value) || 0 })}
                        onKeyDown={handleKeyDownInput}
                        className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        placeholder="Ingrese la cotización"
                      />
                      <p className="text-xs text-gray-500 mt-1">
                        Cotización de {medioPagoActual.tipoMoneda || "la moneda"} respecto a ARS
                      </p>
                    </div>
                  )}
                  
                  {medioPagoSeleccionado === "TARJETA_CREDITO" && (
                    <>
                      <div>
                        <label className="text-indigo-950 font-semibold mb-2 block text-sm">Banco:</label>
                        <select
                          value={medioPagoActual.banco || ""}
                          onChange={(e) => setMedioPagoActual({ ...medioPagoActual, banco: e.target.value })}
                          onKeyDown={handleKeyDownInput}
                          className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        >
                          <option value="">Seleccionar banco</option>
                          <option value="Galicia">Galicia</option>
                          <option value="Santander">Santander</option>
                          <option value="BBVA">BBVA</option>
                          <option value="Nación">Nación</option>
                          <option value="Provincia">Provincia</option>
                        </select>
                      </div>
                      <div>
                        <label className="text-indigo-950 font-semibold mb-2 block text-sm">Cuotas:</label>
                        <input
                          type="number"
                          min="1"
                          max="12"
                          value={medioPagoActual.cuotas || ""}
                          onChange={(e) => setMedioPagoActual({ ...medioPagoActual, cuotas: parseInt(e.target.value) || 0 })}
                          onKeyDown={handleKeyDownInput}
                          className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                          placeholder="1"
                        />
                      </div>
                    </>
                  )}
                  
                  {medioPagoSeleccionado === "TARJETA_DEBITO" && (
                    <>
                      <div>
                        <label className="text-indigo-950 font-semibold mb-2 block text-sm">Banco:</label>
                        <select
                          value={medioPagoActual.banco || ""}
                          onChange={(e) => setMedioPagoActual({ ...medioPagoActual, banco: e.target.value })}
                          onKeyDown={handleKeyDownInput}
                          className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        >
                          <option value="">Seleccionar banco</option>
                          <option value="Galicia">Galicia</option>
                          <option value="Santander">Santander</option>
                          <option value="BBVA">BBVA</option>
                          <option value="Nación">Nación</option>
                          <option value="Provincia">Provincia</option>
                        </select>
                      </div>
                      <div>
                        <label className="text-indigo-950 font-semibold mb-2 block text-sm">DNI Titular:</label>
                        <input
                          type="text"
                          value={medioPagoActual.dniTitular || ""}
                          onChange={(e) => setMedioPagoActual({ ...medioPagoActual, dniTitular: e.target.value.toUpperCase() })}
                          onKeyDown={handleKeyDownInput}
                          className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                          placeholder="Ingrese DNI"
                        />
                      </div>
                    </>
                  )}
                  
                  {medioPagoSeleccionado === "CHEQUE" && (
                    <>
                      <div>
                        <label className="text-indigo-950 font-semibold mb-2 block text-sm">Nro. de Cheque:</label>
                        <input
                          type="number"
                          min="1"
                          value={medioPagoActual.numeroCheque || ""}
                          onChange={(e) => setMedioPagoActual({ ...medioPagoActual, numeroCheque: parseInt(e.target.value) || 0 })}
                          onKeyDown={handleKeyDownInput}
                          className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                          placeholder="Número de cheque"
                        />
                      </div>
                      <div>
                        <label className="text-indigo-950 font-semibold mb-2 block text-sm">Banco:</label>
                        <select
                          value={medioPagoActual.banco || ""}
                          onChange={(e) => setMedioPagoActual({ ...medioPagoActual, banco: e.target.value })}
                          onKeyDown={handleKeyDownInput}
                          className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        >
                          <option value="">Seleccionar banco</option>
                          <option value="Galicia">Galicia</option>
                          <option value="Santander">Santander</option>
                          <option value="BBVA">BBVA</option>
                          <option value="Nación">Nación</option>
                          <option value="Provincia">Provincia</option>
                        </select>
                      </div>
                      <div>
                        <label className="text-indigo-950 font-semibold mb-2 block text-sm">Fecha de Cobro:</label>
                        <input
                          type="date"
                          value={medioPagoActual.plazo || ""}
                          onChange={(e) => setMedioPagoActual({ ...medioPagoActual, plazo: e.target.value })}
                          onKeyDown={handleKeyDownInput}
                          className="w-full p-3 border-2 rounded-lg text-indigo-950 border-gray-300 hover:border-indigo-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        />
                      </div>
                    </>
                  )}
                  
                  <button
                    type="button"
                    onClick={agregarMedioPago}
                    className="w-full px-6 py-3 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-all shadow-md hover:shadow-lg font-semibold"
                  >
                    Agregar Medio de Pago
                  </button>
                </div>
              )}
            </div>
            
            {/* BOTONES */}
            <div className="flex justify-center gap-4 mt-6">
              <Link href="/menu">
                <button className="px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold">
                  Cancelar
                </button>
              </Link>
              
              <button
                onClick={procesarPago}
                disabled={totalAcumulado < facturaSeleccionada.total || procesando}
                className={`px-6 py-3 bg-gradient-to-r from-green-600 to-green-700 text-white rounded-lg hover:from-green-700 hover:to-green-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105 ${
                  totalAcumulado < facturaSeleccionada.total || procesando ? "opacity-50 cursor-not-allowed" : ""
                }`}
              >
                {procesando ? "Procesando..." : "Procesar Pago"}
              </button>
            </div>
            
            {mensajeError && (
              <div className="mt-4 bg-red-50 border-l-4 border-red-500 text-red-700 p-4 rounded-lg shadow-md">
                <p className="font-semibold">{mensajeError}</p>
              </div>
            )}
          </div>
        )}
      </section>
      
      {/* CARTEL DE FACTURA SALDADA */}
      {mostrarCartelSaldada && (
        <FacturaSaldadaCartel
          onClose={() => {
            setMostrarCartelSaldada(false);
            setFacturaSeleccionada(null);
            setFacturas([]);
            setNroHabitacion("");
            setMediosPago([]);
            setTotalAcumulado(0);
            setVuelto(0);
            setTotalAPagar(0);
            setMensajeError("");
          }}
        />
      )}
    </main>
  );
}

// Componente del cartel de factura saldada
function FacturaSaldadaCartel({ onClose }: { onClose: () => void }) {
  useEffect(() => {
    const handleKeyPress = () => {
      onClose();
    };

    window.addEventListener('keydown', handleKeyPress);
    return () => {
      window.removeEventListener('keydown', handleKeyPress);
    };
  }, [onClose]);

  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-[9999]">
      <div className="relative bg-white rounded-xl shadow-xl p-8 w-auto min-w-[500px]">
        <img
          src="/imagenInformacion.png"
          alt="Info"
          className="absolute top-4 left-4 w-12 h-12"
        />
        <button
          type="button"
          onClick={onClose}
          className="absolute top-4 right-4 text-gray-700 hover:text-black text-xl font-bold"
        >
          ✕
        </button>
        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950 mt-8">
          Factura saldada. TOQUE UNA TECLA PARA CONTINUAR…
        </h2>
      </div>
    </div>
  );
}

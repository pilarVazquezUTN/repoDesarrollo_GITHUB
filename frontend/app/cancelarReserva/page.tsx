'use client';

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import axios from "axios";
import Link from "next/link";
import { format, parseISO } from "date-fns";
import { esSoloLetras, esObligatorio } from "../validaciones/validaciones";
import ReservaCancelada from "../carteles/reservaCancelada";

interface ReservaDTO {
  id_reserva: number;
  fecha_desde: string;
  fecha_hasta: string;
  estado: string;
  nombre: string;
  apellido: string;
  telefono: string;
  habitacion: {
    numero: number;
    estado: string;
    precio: number;
    cantidadPersonas: number;
    tipohabitacion: string;
  };
}

export default function CancelarReserva() {
  const router = useRouter();
  
  // Estados del formulario
  const [apellido, setApellido] = useState("");
  const [nombre, setNombre] = useState("");
  
  // Estados de resultados
  const [reservas, setReservas] = useState<ReservaDTO[]>([]);
  const [reservasSeleccionadas, setReservasSeleccionadas] = useState<number[]>([]);
  const [mostrarCartel, setMostrarCartel] = useState(false);
  const [mostrarModalConfirmacion, setMostrarModalConfirmacion] = useState(false);
  const [mostrarCartelCancelacion, setMostrarCartelCancelacion] = useState(false);
  const [mostrarCartelError, setMostrarCartelError] = useState(false);
  const [mensajeError, setMensajeError] = useState("");
  const [cantidadCanceladas, setCantidadCanceladas] = useState(1);
  const [buscando, setBuscando] = useState(false);
  const [cancelando, setCancelando] = useState(false);
  
  // Estados de errores
  const [errorNombre, setErrorNombre] = useState(false);
  const [errorApellido, setErrorApellido] = useState(false);
  const [errorApellidoObligatorio, setErrorApellidoObligatorio] = useState(false);

  // Validaciones en tiempo real
  useEffect(() => {
    setErrorNombre(nombre !== "" && !esSoloLetras(nombre));
  }, [nombre]);

  useEffect(() => {
    setErrorApellido(apellido !== "" && !esSoloLetras(apellido));
  }, [apellido]);

  useEffect(() => {
    setErrorApellidoObligatorio(false);
  }, [apellido]);

  // Función de búsqueda
  const buscarReservas = async (e: React.FormEvent) => {
    e.preventDefault();

    // Validar apellido obligatorio
    const apellidoVacio = !esObligatorio(apellido);
    setErrorApellidoObligatorio(apellidoVacio);
    
    if (apellidoVacio) {
      const inputApellido = document.querySelector('input[placeholder*="apellido"]') as HTMLInputElement;
      if (inputApellido) {
        inputApellido.focus();
      }
      return;
    }

    // Validaciones de formato
    const nombreInvalido = nombre !== "" && !esSoloLetras(nombre);
    const apellidoInvalido = !esSoloLetras(apellido);

    setErrorNombre(nombreInvalido);
    setErrorApellido(apellidoInvalido);

    if (nombreInvalido || apellidoInvalido) {
      return;
    }

    setBuscando(true);
    try {
      const params: any = {
        apellido: apellido || "",
        nombre: nombre || ""
      };

      const response = await axios.get("http://localhost:8080/reservas/buscar", {
        params
      });

      const reservasData = response.data || [];
      
      // Filtrar solo reservas en estado PENDIENTE (puede venir como "Pendiente" o "PENDIENTE")
      const reservasPendientes = reservasData.filter((r: ReservaDTO) => 
        r.estado?.toUpperCase() === "PENDIENTE"
      );
      
      setReservas(reservasPendientes);
      setReservasSeleccionadas([]);

      if (reservasPendientes.length === 0) {
        setMostrarCartel(true);
      }
    } catch (error) {
      console.error("Error al buscar reservas:", error);
      setReservas([]);
      setMostrarCartel(true);
    } finally {
      setBuscando(false);
    }
  };

  // Toggle selección de reserva
  const toggleSeleccionReserva = (idReserva: number) => {
    setReservasSeleccionadas((prev) => {
      if (prev.includes(idReserva)) {
        return prev.filter((id) => id !== idReserva);
      } else {
        return [...prev, idReserva];
      }
    });
  };

  // Función para mostrar modal de confirmación
  const mostrarConfirmacionCancelacion = () => {
    if (reservasSeleccionadas.length === 0) {
      return;
    }
    setMostrarModalConfirmacion(true);
  };

  // Función para cancelar reservas
  const cancelarReservas = async () => {
    if (reservasSeleccionadas.length === 0) {
      return;
    }

    setMostrarModalConfirmacion(false);
    setCancelando(true);
    
    try {
      // Cancelar todas las reservas seleccionadas una por una para capturar errores específicos
      const reservasACancelar = [...reservasSeleccionadas];
      const reservasCanceladasExitosas: number[] = [];
      
      for (const idReserva of reservasACancelar) {
        try {
          const dto = {
            id_reserva: idReserva
          };
          
          await axios.put("http://localhost:8080/reservas/cancelar", dto, {
            headers: {
              "Content-Type": "application/json"
            }
          });
          
          reservasCanceladasExitosas.push(idReserva);
        } catch (error: any) {
          // Si es un error 400, extraer el mensaje del backend
          if (error.response && error.response.status === 400) {
            let mensajeError = "";
            
            // Intentar extraer el mensaje de diferentes formatos posibles
            if (typeof error.response.data === 'string') {
              mensajeError = error.response.data;
            } else if (error.response.data?.message) {
              mensajeError = error.response.data.message;
            } else if (error.response.data?.error) {
              mensajeError = error.response.data.error;
            } else {
              // Si no hay mensaje, construir uno con el estado de la reserva
              const reservaFallida = reservas.find(r => r.id_reserva === idReserva);
              const estado = reservaFallida?.estado || "DESCONOCIDO";
              mensajeError = `Solo se pueden cancelar reservas en estado PENDIENTE. La reserva actual está en estado: ${estado}`;
            }
            
            // Si el mensaje no menciona el estado, agregarlo
            if (!mensajeError.includes("estado") && !mensajeError.includes("PENDIENTE")) {
              const reservaFallida = reservas.find(r => r.id_reserva === idReserva);
              const estado = reservaFallida?.estado || "DESCONOCIDO";
              mensajeError = `Solo se pueden cancelar reservas en estado PENDIENTE. La reserva actual está en estado: ${estado}`;
            }
            
            setMensajeError(mensajeError);
            setMostrarCartelError(true);
            setCancelando(false);
            return;
          }
          
          // Si es otro tipo de error, lanzarlo para que se capture en el catch externo
          throw error;
        }
      }

      // Si todas las cancelaciones fueron exitosas
      if (reservasCanceladasExitosas.length > 0) {
        const cantidad = reservasCanceladasExitosas.length;
        setCantidadCanceladas(cantidad);

        // Eliminar las reservas canceladas de la lista
        setReservas((prev) => prev.filter((r) => !reservasCanceladasExitosas.includes(r.id_reserva)));
        setReservasSeleccionadas([]);
        setMostrarCartelCancelacion(true);
      }
    } catch (error: any) {
      console.error("Error al cancelar reservas:", error);
      
      // Si es un error 400, ya fue manejado arriba
      if (error.response && error.response.status === 400) {
        return;
      }
      
      // Para otros errores, mostrar mensaje genérico
      setMensajeError("Hubo un error al cancelar las reservas.");
      setMostrarCartelError(true);
    } finally {
      setCancelando(false);
    }
  };

  // Formatear fecha
  const formatearFecha = (fecha: string) => {
    try {
      return format(parseISO(fecha), "dd/MM/yyyy");
    } catch {
      return fecha;
    }
  };

  // Manejar Enter en botones
  const handleKeyDown = (e: React.KeyboardEvent, action: () => void) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      action();
    }
  };

  // Manejar Shift+Tab para retroceder
  const handleKeyDownInput = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Tab" && e.shiftKey) {
      e.preventDefault();
      const form = e.currentTarget.form;
      if (form) {
        const inputs = Array.from(form.querySelectorAll("input"));
        const index = inputs.indexOf(e.currentTarget);
        if (index > 0) {
          (inputs[index - 1] as HTMLElement).focus();
        }
      }
    }
  };

  return (
    <main className="flex gap-8 px-8 py-8 items-start bg-gradient-to-br from-gray-50 to-gray-100 min-h-screen">
      {/* FORMULARIO DE BÚSQUEDA */}
      <div className="bg-white rounded-xl shadow-lg p-6 w-80 h-fit sticky top-4">
        <h2 className="text-2xl font-bold text-indigo-950 mb-6 pb-3 border-b-2 border-indigo-200">
          Criterios de Búsqueda
        </h2>
        <form onSubmit={buscarReservas} className="flex flex-col space-y-4">
          <div>
            <label className="text-indigo-950 font-semibold mb-2 block text-sm">Apellido:</label>
            <input
              type="text"
              value={apellido}
              onChange={(e) => {
                const valor = e.target.value.toUpperCase();
                setApellido(valor);
                setErrorApellidoObligatorio(false);
              }}
              onKeyDown={handleKeyDownInput}
              className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                errorApellido || errorApellidoObligatorio
                  ? "border-red-500 bg-red-50"
                  : "border-gray-300 hover:border-indigo-400"
              }`}
              placeholder="Ingrese el apellido"
            />
            {errorApellido && (
              <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                <span>⚠</span> Ingrese solo letras.
              </p>
            )}
            {errorApellidoObligatorio && (
              <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                <span>⚠</span> El campo apellido no puede estar vacío.
              </p>
            )}
          </div>

          <div>
            <label className="text-indigo-950 font-semibold mb-2 block text-sm">Nombre:</label>
            <input
              type="text"
              value={nombre}
              onChange={(e) => setNombre(e.target.value.toUpperCase())}
              onKeyDown={handleKeyDownInput}
              className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                errorNombre
                  ? "border-red-500 bg-red-50"
                  : "border-gray-300 hover:border-indigo-400"
              }`}
              placeholder="Ingrese el nombre"
            />
            {errorNombre && (
              <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                <span>⚠</span> Ingrese solo letras.
              </p>
            )}
          </div>

          <button
            type="submit"
            disabled={buscando}
            onKeyDown={(e) => handleKeyDown(e, buscarReservas)}
            className={`w-full px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105 ${
              buscando ? "opacity-50 cursor-not-allowed" : ""
            }`}
          >
            {buscando ? "Buscando..." : "Buscar"}
          </button>
        </form>
      </div>

      {/* TABLA DE RESULTADOS */}
      <section className="flex-1">
        <div className="bg-white rounded-xl shadow-xl overflow-hidden">
          <div className="p-6 border-b-2 border-indigo-200">
            <h2 className="text-2xl font-bold text-indigo-950">
              Reservas Pendientes
            </h2>
            <p className="text-sm text-gray-600 mt-1">
              {reservas.length > 0 
                ? `${reservas.length} reserva(s) pendiente(s) encontrada(s)` 
                : "No hay resultados para mostrar"}
            </p>
            <p className="text-xs text-gray-500 mt-1">
              Solo se pueden cancelar reservas en estado PENDIENTE
            </p>
          </div>

          {reservas.length > 0 ? (
            <div className="max-h-[600px] overflow-y-auto overflow-x-auto">
              <table className="w-full border-collapse">
                <thead className="bg-gradient-to-r from-indigo-950 to-indigo-900 text-white sticky top-0 z-10 shadow-md">
                  <tr>
                    <th className="p-4 font-semibold text-left">Seleccionar</th>
                    <th className="p-4 font-semibold text-left">ID Reserva</th>
                    <th className="p-4 font-semibold text-left">Apellido</th>
                    <th className="p-4 font-semibold text-left">Nombre</th>
                    <th className="p-4 font-semibold text-left">Teléfono</th>
                    <th className="p-4 font-semibold text-left">Habitación</th>
                    <th className="p-4 font-semibold text-left">Tipo</th>
                    <th className="p-4 font-semibold text-left">Desde</th>
                    <th className="p-4 font-semibold text-left">Hasta</th>
                    <th className="p-4 font-semibold text-left">Estado</th>
                  </tr>
                </thead>
                <tbody>
                  {reservas.map((r) => {
                    const isSelected = reservasSeleccionadas.includes(r.id_reserva);
                    return (
                      <tr
                        key={r.id_reserva}
                        className={`hover:bg-indigo-50 transition-colors ${
                          isSelected ? "bg-indigo-100" : "bg-white"
                        }`}
                      >
                        <td className="p-4 border-b border-gray-200 text-center">
                          <input
                            type="checkbox"
                            checked={isSelected}
                            onChange={() => toggleSeleccionReserva(r.id_reserva)}
                            className="w-4 h-4 text-indigo-950 focus:ring-indigo-500"
                          />
                        </td>
                        <td className="p-4 border-b border-gray-200 font-medium text-indigo-950">
                          {r.id_reserva}
                        </td>
                        <td className="p-4 border-b border-gray-200 text-gray-700">
                          {r.apellido}
                        </td>
                        <td className="p-4 border-b border-gray-200 text-gray-700">
                          {r.nombre}
                        </td>
                        <td className="p-4 border-b border-gray-200 text-gray-700">
                          {r.telefono}
                        </td>
                        <td className="p-4 border-b border-gray-200 text-gray-700 font-semibold">
                          {r.habitacion?.numero || "N/A"}
                        </td>
                        <td className="p-4 border-b border-gray-200 text-gray-700">
                          {r.habitacion?.tipohabitacion || "N/A"}
                        </td>
                        <td className="p-4 border-b border-gray-200 text-gray-700">
                          {formatearFecha(r.fecha_desde)}
                        </td>
                        <td className="p-4 border-b border-gray-200 text-gray-700">
                          {formatearFecha(r.fecha_hasta)}
                        </td>
                        <td className="p-4 border-b border-gray-200">
                          <span className="px-2 py-1 rounded-full text-xs font-semibold bg-yellow-100 text-yellow-800">
                            {r.estado}
                          </span>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          ) : (
            <div className="p-12 text-center">
              <div className="text-gray-400 text-lg mb-2">
                🔍 Complete los criterios de búsqueda y presione "Buscar"
              </div>
              <p className="text-sm text-gray-500">
                Ingrese el apellido para buscar reservas pendientes
              </p>
            </div>
          )}
        </div>

        {/* BOTONES DE ACCIÓN */}
        <div className="flex justify-center gap-4 mt-6">
          <Link href="/menu">
            <button
              onKeyDown={(e) => handleKeyDown(e, () => router.push("/menu"))}
              className="px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold"
            >
              Cancelar
            </button>
          </Link>

          <button
            onClick={mostrarConfirmacionCancelacion}
            disabled={reservasSeleccionadas.length === 0 || cancelando}
            onKeyDown={(e) => handleKeyDown(e, mostrarConfirmacionCancelacion)}
            className={`px-6 py-3 bg-gradient-to-r from-red-600 to-red-700 text-white rounded-lg hover:from-red-700 hover:to-red-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105 ${
              reservasSeleccionadas.length === 0 || cancelando ? "opacity-50 cursor-not-allowed" : ""
            }`}
          >
            {cancelando ? "Cancelando..." : "Aceptar"}
          </button>
        </div>
      </section>

      {/* CARTEL DE NO ENCONTRADO */}
      {mostrarCartel && (
        <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-[9999]">
          <div className="relative bg-white rounded-xl shadow-xl p-8 w-auto min-w-[400px]">
            <img
              src="/imagenError.png"
              alt="Error"
              className="absolute top-4 left-4 w-12 h-12"
            />
            <button
              className="absolute top-4 right-4 text-gray-700 hover:text-black text-xl font-bold"
              onClick={() => setMostrarCartel(false)}
            >
              ✕
            </button>
            <h2 className="text-xl font-bold text-red-700 text-center mb-4 mt-8">
              No existen reservas para los criterios de búsqueda
            </h2>
            <div className="flex justify-center mt-6">
              <button
                onClick={() => {
                  setMostrarCartel(false);
                  const inputApellido = document.querySelector('input[placeholder*="apellido"]') as HTMLInputElement;
                  if (inputApellido) {
                    inputApellido.focus();
                  }
                }}
                className="px-6 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
              >
                Aceptar
              </button>
            </div>
          </div>
        </div>
      )}

      {/* MODAL DE CONFIRMACIÓN */}
      {mostrarModalConfirmacion && (
        <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-[9999]">
          <div className="relative bg-white rounded-xl shadow-xl p-8 w-auto min-w-[400px]">
            <img
              src="/imagenInformacion.png"
              alt="Info"
              className="absolute top-4 left-4 w-12 h-12"
            />
            <h2 className="text-xl font-bold text-indigo-950 text-center mb-4 mt-8">
              Confirmar Cancelación
            </h2>
            <p className="text-gray-700 text-center mb-6">
              ¿Está seguro que desea cancelar {reservasSeleccionadas.length} reserva(s) seleccionada(s)?
            </p>
            <div className="flex justify-center gap-4 mt-6">
              <button
                onClick={() => {
                  setMostrarModalConfirmacion(false);
                }}
                className="px-6 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition"
              >
                CANCELAR
              </button>
              <button
                onClick={cancelarReservas}
                className="px-6 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
              >
                ACEPTAR
              </button>
            </div>
          </div>
        </div>
      )}

      {/* CARTEL DE ERROR DE CANCELACIÓN */}
      {mostrarCartelError && (
        <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-[9999]">
          <div className="relative bg-white rounded-xl shadow-xl p-8 w-auto min-w-[500px] max-w-[600px]">
            <img
              src="/imagenError.png"
              alt="Error"
              className="absolute top-4 left-4 w-12 h-12"
            />
            <button
              className="absolute top-4 right-4 text-gray-700 hover:text-black text-xl font-bold"
              onClick={() => setMostrarCartelError(false)}
            >
              ✕
            </button>
            <h2 className="text-xl font-bold text-red-700 text-center mb-4 mt-8">
              Error al cancelar reserva
            </h2>
            <p className="text-gray-700 text-center mb-6 whitespace-pre-line">
              {mensajeError}
            </p>
            <div className="flex justify-center mt-6">
              <button
                onClick={() => {
                  setMostrarCartelError(false);
                  setMensajeError("");
                }}
                className="px-6 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
              >
                Aceptar
              </button>
            </div>
          </div>
        </div>
      )}

      {/* CARTEL DE RESERVA CANCELADA */}
      {mostrarCartelCancelacion && (
        <ReservaCancelada 
          cantidad={cantidadCanceladas}
          onClose={() => {
            setMostrarCartelCancelacion(false);
          }} 
        />
      )}
    </main>
  );
}

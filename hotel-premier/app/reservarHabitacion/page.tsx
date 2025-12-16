'use client';

import React from "react";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { format, parseISO, eachDayOfInterval, isAfter } from "date-fns";
import { usePathname } from "next/navigation";
import OcuparHabitacionIgualmente from "../carteles/ocuparHabitacionIgualmente";
import CartelHabitacionNoDisponible from "../carteles/CartelHabitacionNoDisponible";
import habitacionesSeleccionadas from "../carteles/habitacionesSeleccionadas"
import axios from "axios";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrashCan  } from "@fortawesome/free-solid-svg-icons";
import CartelListaHabitaciones from "../carteles/cartelListaHabitaciones";
import Link from "next/link";
import { validarFechasReserva, esSoloLetras, esSoloNumeros, esObligatorio, telefonoValido } from "@/app/validaciones/validaciones";
import ReservaConfirmada from "../carteles/reservaConfirmada";


// =========================
// TIPOS E INTERFACES (COINCIDEN CON  JSON)
// =========================
type TipoHabitacion = "IndividualEstandar" | "DobleEstandar" | "Suite" | "DobleSuperior" | "SuperiorFamilyPlan";

interface ReservaDTO {
  id_reserva?: number;
  fecha_desde: string;
  fecha_hasta: string;
  estado: string;
  nombre?: string;
  apellido?: string;
  telefono?: string;
  habitacion?: {
    numero: number;
    estado: string;
    precio: number;
    cantidadPersonas: number;
    tipohabitacion: string;
  };
}

interface EstadiaDTO {
  checkIn: string;
  checkOut: string;
}

interface HabitacionDTO {
  numero: number;
  estado: string; 
  precio: number;
  cantidadPersonas: number;
  tipohabitacion: string;
  listareservas?: ReservaDTO[]; 
  listaestadias?: EstadiaDTO[];
}

interface Props {
    ocultarTabla?: boolean;
}

export default function ReservarHabitacion({ ocultarTabla = false }: Props) {

  // =========================
  // ESTADOS
  // =========================
  const [desdeFecha, setDesdeFecha] = useState("");
  const [hastaFecha, setHastaFecha] = useState("");
  const [tipoSeleccionado, setTipoSeleccionado] = useState<"" | TipoHabitacion>("");
  const [erroresFecha, setErroresFecha] = useState({ desdeInvalido: false, hastaInvalido: false, ordenInvalido: false });
  const [fechasValidas, setFechasValidas] = useState(false);
  const [errorSeleccion, setErrorSeleccion] = useState("");
  
  const [seleccionados, setSeleccionados] = useState<string[]>([]);
  const [mostrarCartelOH, setMostrarCartelOH] = useState(false);
  const [mostrarCartel, setMostrarCartel] = useState(false);
  
  const [habitaciones, setHabitaciones] = useState<HabitacionDTO[] | []>([]);
  const [rangos, setRangos] = useState<{ numero: number; desde: string; hasta: string }[]>([]);

  const [mostrarCartelLista, setMostrarCartelLista] = useState(false);
  const [listaBackend, setListaBackend] = useState<any[]>([]);
  const router = useRouter();
  //erne
  const [ultimoClick, setUltimoClick] = useState<string | null>(null);
  const [reservas, setReservas] = useState<ReservaDTO[]>([]);
  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [telefono, setTelefono] = useState("");
  const [errorNombre, setErrorNombre] = useState(false);
  const [errorApellido, setErrorApellido] = useState(false);
  const [errorTelefono, setErrorTelefono] = useState(false);
  const [errorNombreObligatorio, setErrorNombreObligatorio] = useState(false);
  const [errorApellidoObligatorio, setErrorApellidoObligatorio] = useState(false);
  const [errorTelefonoObligatorio, setErrorTelefonoObligatorio] = useState(false);
  const [mostrarCartelConfirmacion, setMostrarCartelConfirmacion] = useState(false);

  // =========================
  // CONFIGURACIÓN HABITACIONES
  // =========================
  const TipoHabitacion = {
    IndividualEstandar: "IndividualEstandar" ,
    DobleEstandar: "DobleEstandar",
    SuperiorFamilyPlan: "SuperiorFamilyPlan",
    Suite: "Suite",
    DobleSuperior: "DobleSuperior"
  }

  // =========================
  // VALIDAR FECHAS
  // =========================
  function validarDesde(fecha: string) {
    const result = validarFechasReserva(fecha, hastaFecha);
    setErroresFecha(result);
    setFechasValidas(result.valido);
  }

  function validarHasta(fecha: string) {
    const result = validarFechasReserva(desdeFecha, fecha);
    setErroresFecha(result);
    setFechasValidas(result.valido);
  }


  // =========================
  // FETCH DE DATOS
  // =========================
  // Validaciones de formulario
  useEffect(() => {
    setErrorNombre(nombre !== "" && !esSoloLetras(nombre));
  }, [nombre]);

  useEffect(() => {
    setErrorApellido(apellido !== "" && !esSoloLetras(apellido));
  }, [apellido]);

  useEffect(() => {
    setErrorTelefono(telefono !== "" && (!esSoloNumeros(telefono) || !telefonoValido(telefono)));
  }, [telefono]);

  useEffect(() => {
    const fetchHabitacionesYReservas = async () => {
      if (fechasValidas && tipoSeleccionado) {
        try {
          // Obtener habitaciones por tipo
          const responseHabitaciones = await axios.get(`http://localhost:8080/habitaciones`, {
            params: { 
              tipo: tipoSeleccionado
            },
          });

          // Obtener reservas en el rango de fechas
          const responseReservas = await axios.get(`http://localhost:8080/reservas`, {
            params: {
              desde: desdeFecha,
              hasta: hastaFecha
            },
          });

          const habitacionesData = responseHabitaciones.data || [];
          const reservasData = responseReservas.data || [];

          console.log("Habitaciones recibidas:", habitacionesData);
          console.log("Reservas recibidas:", reservasData);
          
          // Ver estructura completa de una reserva para debug
          if (reservasData.length > 0) {
            console.log("Estructura completa primera reserva:", JSON.stringify(reservasData[0], null, 2));
            console.log("Todos los campos de la reserva:", Object.keys(reservasData[0]));
            console.log("Valor de habitacion en reserva:", reservasData[0].habitacion);
            console.log("Valor de nro_habitacion en reserva:", reservasData[0].nro_habitacion);
          }

          // Mapear reservas a cada habitación
          // El endpoint puede devolver reservas con habitacion.numero, nro_habitacion, o habitacion como objeto
          const habitacionesConReservas = habitacionesData.map((h: any) => {
            const reservasDeEstaHabitacion = reservasData.filter((r: any) => {
              // Verificar todas las posibles formas en que puede venir el número de habitación
              let numeroReserva = null;
              
              // Caso 1: r.habitacion.numero (objeto habitacion con numero)
              if (r.habitacion && typeof r.habitacion === 'object' && r.habitacion.numero) {
                numeroReserva = r.habitacion.numero;
              }
              // Caso 2: r.nro_habitacion (campo directo)
              else if (r.nro_habitacion !== undefined && r.nro_habitacion !== null) {
                numeroReserva = r.nro_habitacion;
              }
              // Caso 3: r.habitacion es un número directamente
              else if (typeof r.habitacion === 'number') {
                numeroReserva = r.habitacion;
              }
              
              const coincide = numeroReserva === h.numero;
              if (coincide) {
                console.log(`✓ Reserva ${r.id_reserva} (${r.fecha_desde} a ${r.fecha_hasta}, estado: ${r.estado}) asignada a habitación ${h.numero}`);
              }
              return coincide;
            });
            
            if (reservasDeEstaHabitacion.length > 0) {
              console.log(`Habitación ${h.numero} tiene ${reservasDeEstaHabitacion.length} reserva(s)`);
            }

            return {
              ...h,
              listareservas: reservasDeEstaHabitacion,
              listaestadias: [] // No se usa en este caso de uso
            };
          });

          console.log("Habitaciones con reservas mapeadas:", habitacionesConReservas);
          setHabitaciones(habitacionesConReservas);
          setReservas(reservasData);
        } catch(err) {
          console.error("Error al cargar habitaciones y reservas:", err);
          setHabitaciones([]);
          setReservas([]);
        }
      }
    };

    fetchHabitacionesYReservas();
  }, [fechasValidas, tipoSeleccionado, desdeFecha, hastaFecha]);

  // =========================
  // LÓGICA DE ESTADOS Y SELECCIÓN
  // =========================
  const fechasIntervalo = (desdeFecha && hastaFecha && fechasValidas)
    ? eachDayOfInterval({ start: parseISO(desdeFecha), end: parseISO(hastaFecha) })
    : [];

  const fechaEnRango = (fechaTarget: string, inicio: string, fin: string) => {
    return fechaTarget >= inicio && fechaTarget <= fin;
  };

  const toggleSeleccion = (fechaDate: Date, habitacion: HabitacionDTO, event: React.MouseEvent<HTMLTableCellElement>) => {
    const fechaString = format(fechaDate, "yyyy-MM-dd");
    const keyActual = `${fechaString}|${habitacion.numero}`;

    // VALIDACIONES: No permitir seleccionar si está fuera de servicio
    // El estado puede venir como "FUERADESERVICIO" o "FUERA_DE_SERVICIO"
    if (habitacion.estado === "FUERADESERVICIO" || habitacion.estado === "FUERA_DE_SERVICIO") {
      setMostrarCartel(true);
      setTimeout(() => setMostrarCartel(false), 2500);
      return;
    }

    // Validar si hay reserva en estado "FINALIZADA" (ocupada)
    const tieneReservaFinalizada = habitacion.listareservas?.some((r: ReservaDTO) => {
      const enRango = fechaEnRango(fechaString, r.fecha_desde, r.fecha_hasta);
      return enRango && r.estado === "FINALIZADA";
    });
    if (tieneReservaFinalizada) {
      setMostrarCartel(true);
      setTimeout(() => setMostrarCartel(false), 2500);
      return;
    }

    // Validar si hay reserva en estado "ENCURSO" o "PENDIENTE" (reservada)
    const tieneReservaEnCurso = habitacion.listareservas?.some((r: ReservaDTO) => {
      const enRango = fechaEnRango(fechaString, r.fecha_desde, r.fecha_hasta);
      return enRango && (r.estado === "ENCURSO" || r.estado === "PENDIENTE");
    });
    if (tieneReservaEnCurso) {
      setMostrarCartel(true);
      setTimeout(() => setMostrarCartel(false), 2500);
      return;
    }

    // =============================================
    // SHIFT + CLICK — seleccionar rango de fechas
    // =============================================
    if (event?.shiftKey && ultimoClick) {
      const [fechaPrev, numeroPrev] = ultimoClick.split("|");

      // Solo rango si es la misma habitación
      if (Number(numeroPrev) === habitacion.numero) {
        const inicio = parseISO(fechaPrev);
        const fin = parseISO(fechaString);

        const intervalo = eachDayOfInterval({
          start: inicio < fin ? inicio : fin,
          end: inicio < fin ? fin : inicio,
        });

        const claves = intervalo.map((f: Date) =>
          `${format(f, "yyyy-MM-dd")}|${habitacion.numero}`
        );

        setSeleccionados((prev: string[]) => [...new Set([...prev, ...claves])]);
        return;
      }
    }

    // Selección simple
    setSeleccionados((prev: string[]) =>
      prev.includes(keyActual)
        ? prev.filter((item: string) => item !== keyActual)
        : [...prev, keyActual]
    );

    // guardar último click
    setUltimoClick(keyActual);
  };

  const eliminarSeleccionados = () => setSeleccionados([]);

  const deleteSeleccionado = (ran:{ numero: number; desde: string; hasta: string }) => {
    const fechasABorrar = eachDayOfInterval({
      start: parseISO(ran.desde),
      end: parseISO(ran.hasta)
    }).map((f: Date) => `${format(f,"yyyy-MM-dd")}|${ran.numero}`);

    setSeleccionados((prev: string[]) =>
      prev.filter((sel: string) => !fechasABorrar.includes(sel))
    );
  };

    const construirHabitacionesDTO = (): HabitacionDTO[] => {
      const lista: HabitacionDTO[] = seleccionados.map((sel: string) => {
        const [fecha, numero] = sel.split("|");
        const habitacion = (Array.isArray(habitaciones) ? habitaciones : []).find(h => h.numero === Number(numero));

        return {
          numero: Number(numero),
          estado: habitacion?.estado || "DISPONIBLE",
          precio: habitacion?.precio ?? 0,
          cantidadPersonas: habitacion?.cantidadPersonas ?? 1,
          tipohabitacion: habitacion?.tipohabitacion ?? (tipoSeleccionado as string),
          listareservas: [], 
          listaestadias: []
        };
      });
      return lista;
    };

const listaHabitacionUnica = (): HabitacionDTO[] => {
  const listaUnica: Record<number, HabitacionDTO> = {};

  seleccionados.forEach((sel: string) => {
    const [fecha, numero] = sel.split("|");
    const num = Number(numero);

    if (listaUnica[num]) return;

    const habitacion = (Array.isArray(habitaciones) ? habitaciones : [])
      .find(h => h.numero === num);

    listaUnica[num] = {
      numero: num,
      estado: habitacion?.estado || "Disponible",
      precio: habitacion?.precio ?? 0,
      cantidadPersonas: habitacion?.cantidadPersonas ?? 1,
      tipohabitacion: habitacion?.tipohabitacion ?? (tipoSeleccionado as string),

      // Estas claves tienen que coincidir exactamente como las usa el backend
      listareservas: [],
      listaestadias: []
    };
  });

  return Object.values(listaUnica);
};

  function generarRangos(seleccionados: string[]) {
    const datos = seleccionados.map(sel => {
      const [fecha, numero] = sel.split("|");
      return {
        fecha: fecha,
        fechaDate: parseISO(fecha),
        numero: Number(numero)
      };
    });

    datos.sort((a, b) => {
      if (a.numero !== b.numero) return a.numero - b.numero;
      return a.fechaDate.getTime() - b.fechaDate.getTime();
    });

    const resultado: any[] = [];
    let actual = null;

    for (let item of datos) {
      if (!actual) {
        actual = { numero: item.numero, desde: item.fecha, hasta: item.fecha };
        continue;
      }

      const esMismaHab = item.numero === actual.numero;
      const diffDias =
        (item.fechaDate.getTime() - parseISO(actual.hasta).getTime()) /
        (1000 * 60 * 60 * 24);

      if (esMismaHab && diffDias === 1) {
        actual.hasta = item.fecha;
      } else {
        resultado.push(actual);
        actual = { numero: item.numero, desde: item.fecha, hasta: item.fecha };
      }
    }
    if (actual) resultado.push(actual);
    return resultado;
  }

  useEffect(() => {
    const nuevosRangos = generarRangos(seleccionados);
    setRangos(nuevosRangos);
  }, [seleccionados]);



// Cálculo de fechas reales según selección
const fechaDesdeSeleccion = rangos.length
  ? rangos.reduce((min: string, r: { numero: number; desde: string; hasta: string }) => r.desde < min ? r.desde : min, rangos[0].desde)
  : desdeFecha;

const fechaHastaSeleccion = rangos.length
  ? rangos.reduce((max: string, r: { numero: number; desde: string; hasta: string }) => r.hasta > max ? r.hasta : max, rangos[0].hasta)
  : hastaFecha;

  // =========================
  // RENDER
  // =========================
  return (
    <main className="flex gap-8 px-8 py-8 items-start bg-gradient-to-br from-gray-50 to-gray-100 min-h-screen">

      {/* FORMULARIO DE FECHAS Y TIPO */}
      <div className="bg-white rounded-xl shadow-lg p-6 w-80 h-fit sticky top-4">
        <h2 className="text-2xl font-bold text-indigo-950 mb-6 pb-3 border-b-2 border-indigo-200">Filtros de Búsqueda</h2>
        <form className="flex flex-col space-y-4">
          <div>
            <label className="text-indigo-950 font-semibold mb-2 block text-sm">Desde Fecha:</label>
            <input
              type="date"
              value={desdeFecha}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => setDesdeFecha(e.target.value)}
              onBlur={(e: React.FocusEvent<HTMLInputElement>) => validarDesde(e.target.value)}
              className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent
                ${(erroresFecha.desdeInvalido || erroresFecha.ordenInvalido || (!desdeFecha && tipoSeleccionado)) 
                  ? "border-red-500 bg-red-50" 
                  : "border-gray-300 hover:border-indigo-400"}`}
            />
            {erroresFecha.desdeInvalido && <span className="text-red-600 text-xs mt-1 block">La fecha no puede ser menor a hoy</span>}
            {erroresFecha.ordenInvalido && <span className="text-red-600 text-xs mt-1 block">La fecha "Desde" no puede ser posterior a "Hasta"</span>}
          </div>

          <div>
            <label className="text-indigo-950 font-semibold mb-2 block text-sm">Hasta Fecha:</label>
            <input
              type="date"
              value={hastaFecha}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => setHastaFecha(e.target.value)}
              onBlur={(e: React.FocusEvent<HTMLInputElement>) => validarHasta(e.target.value)}
              className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent
                ${(erroresFecha.hastaInvalido || erroresFecha.ordenInvalido || (!hastaFecha && tipoSeleccionado)) 
                  ? "border-red-500 bg-red-50" 
                  : "border-gray-300 hover:border-indigo-400"}`}
            />
            {erroresFecha.hastaInvalido && <span className="text-red-600 text-xs mt-1 block">La fecha no puede ser menor a hoy</span>}
            {erroresFecha.ordenInvalido && <span className="text-red-600 text-xs mt-1 block">La fecha "Hasta" no puede ser anterior a "Desde"</span>}
          </div>

          <div>
            <label className="text-indigo-950 font-semibold mb-2 block text-sm">Tipo de Habitación:</label>
            <select
              value={tipoSeleccionado}
              onChange={(e: React.ChangeEvent<HTMLSelectElement>) => setTipoSeleccionado(e.target.value as TipoHabitacion)}
              onFocus={() => {
                setErroresFecha(prev => ({
                  desdeInvalido: prev.desdeInvalido || !desdeFecha,
                  hastaInvalido: prev.hastaInvalido || !hastaFecha,
                  ordenInvalido: prev.ordenInvalido
                }));
              }}
              className={`w-full p-3 border-2 rounded-lg transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent
                ${!fechasValidas 
                  ? "bg-gray-100 cursor-not-allowed text-gray-400 border-gray-300" 
                  : "text-indigo-950 border-gray-300 hover:border-indigo-400 bg-white"}`}
            >
              <option value="" disabled>Seleccionar tipo</option>
              <option value={TipoHabitacion.IndividualEstandar}>Individual Estandar</option>
              <option value={TipoHabitacion.DobleEstandar}>Doble Estandar</option>
              <option value={TipoHabitacion.DobleSuperior}>Doble Superior</option>
              <option value={TipoHabitacion.SuperiorFamilyPlan}>Superior Family Plan</option>
              <option value={TipoHabitacion.Suite}>Suite</option>
            </select>
          </div>
        </form>
      </div>

      {mostrarCartel && (
        <CartelHabitacionNoDisponible
          mensaje="La habitación seleccionada no se encuentra disponible"
          onClose={() => setMostrarCartel(false)}
        />
      )}

      {mostrarCartelLista && (
           <CartelListaHabitaciones
             lista={listaBackend}
             seleccionados={seleccionados}
             desdeFecha={desdeFecha}
             hastaFecha={hastaFecha}
             rangos={rangos}
             onClose={() => {
               setMostrarCartelLista(false);
               setSeleccionados([]);
               setRangos([]);
             }}
             onAceptar={() => {
               setMostrarCartelLista(false);
               setMostrarFormulario(true);
             }}
           />
       )}

      {mostrarCartelConfirmacion && (
        <ReservaConfirmada
          onClose={() => {
            setMostrarCartelConfirmacion(false);
          }}
        />
      )}

      {/* TABLA ESTADO HABITACIÓN */}
      <section className="flex-1 max-h-[800px] overflow-auto">
        {tipoSeleccionado && fechasIntervalo.length > 0 ? (
          
          <>
            <div className="bg-white rounded-xl shadow-xl overflow-hidden">
            <table className="w-full border-collapse">
              <thead className="bg-gradient-to-r from-indigo-950 to-indigo-900 text-white sticky top-0 z-10 shadow-md">
                <tr>
                  <th className="p-4 font-semibold text-left">Fecha</th>
                  {tipoSeleccionado && (
                    <th colSpan={habitaciones.length} className="p-4 font-semibold text-center">Habitaciones</th>
                  )}
                </tr>
              </thead>

              <tbody>
                <tr className="bg-indigo-50">
                  <td className="p-3 border-b-2 border-indigo-200 font-semibold text-indigo-950"></td>
                  {tipoSeleccionado && habitaciones.map(hab => (
                    <td key={hab?.numero} className="p-3 border-b-2 border-indigo-200 text-center font-bold text-indigo-950">{hab?.numero}</td>
                  ))}
                </tr>

                {fechasIntervalo.map((fechaDate: Date) => {
                  const fechaString = format(fechaDate,"yyyy-MM-dd");
                  const fechaDisplay = format(fechaDate,"dd/MM/yyyy");

                  return (
                    <tr key={fechaString} className="hover:bg-indigo-50/50 transition-colors">
                      <td className="p-3 border-b border-gray-200 text-center font-semibold text-indigo-950 bg-white sticky left-0 z-5">{fechaDisplay}</td>
                      {tipoSeleccionado && habitaciones.map((hab: HabitacionDTO) => {
                        
                        const key = `${fechaString}|${hab?.numero}`;
                        const esSeleccionado = seleccionados.includes(key);

                        // 1. CHEQUEO FUERA DE SERVICIO
                        // El estado puede venir como "FUERADESERVICIO" o "FUERA_DE_SERVICIO"
                        const esFueraServicio = hab.estado === "FUERADESERVICIO" || hab.estado === "FUERA_DE_SERVICIO";

                        // 2. CHEQUEO RESERVA FINALIZADA (OCUPADA - ROJA)
                        const esOcupada = !esFueraServicio && hab.listareservas?.some((reserva: ReservaDTO) => {
                          const enRango = fechaEnRango(fechaString, reserva.fecha_desde, reserva.fecha_hasta);
                          return enRango && reserva.estado === "FINALIZADA";
                        });

                        // 3. CHEQUEO RESERVA EN CURSO/PENDIENTE (RESERVADA - AMARILLO CLARO)
                        const esReservada = !esFueraServicio && !esOcupada && hab.listareservas?.some((reserva: ReservaDTO) => {
                          const enRango = fechaEnRango(fechaString, reserva.fecha_desde, reserva.fecha_hasta);
                          return enRango && (reserva.estado === "ENCURSO" || reserva.estado === "PENDIENTE");
                        });
                        
                        // Debug: solo para la primera celda de la primera habitación
                        if (hab.numero === habitaciones[0]?.numero && fechaString === fechasIntervalo[0]?.toISOString().split('T')[0]) {
                          console.log(`Debug celda - Hab: ${hab.numero}, Fecha: ${fechaString}`);
                          console.log(`  Estado habitación: ${hab.estado}`);
                          console.log(`  Reservas de esta habitación:`, hab.listareservas);
                          console.log(`  esFueraServicio: ${esFueraServicio}`);
                          console.log(`  esOcupada: ${esOcupada}`);
                          console.log(`  esReservada: ${esReservada}`);
                          console.log(`  esSeleccionado: ${esSeleccionado}`);
                        }
                        
                         let bgClass = ""; // Color claro para disponible (se aplica por CSS de la tabla) 
                         if (esFueraServicio) {
                             bgClass = "bg-gray-700"; // Gris para fuera de servicio
                         } else if (esOcupada) {
                             bgClass = "bg-red-500"; // Roja para ocupada (Finalizada)
                         } else if (esReservada) {
                             bgClass = "bg-yellow-200"; // Amarillo claro para reservada de BD (EN CURSO/Pendiente)
                         } else if (esSeleccionado) {
                             bgClass = "bg-yellow-500"; // Amarillo oscuro cuando el usuario selecciona
                         }

                        return (
                          <td
                            key={hab.numero}
                            className={`p-4 border-b border-gray-200 cursor-pointer transition-all hover:scale-105 hover:shadow-md ${bgClass}`}
                            onClick={(e: React.MouseEvent<HTMLTableCellElement>) => toggleSeleccion(fechaDate, hab, e)}
                            title={esFueraServicio ? "Fuera de servicio" : esOcupada ? "Ocupada" : esReservada ? "Reservada" : esSeleccionado ? "Seleccionada" : "Disponible"}
                          ></td>
                        );
                      })}
                    </tr>
                  );
                })}
              </tbody>
            </table>
            </div>

             {/* LEYENDA */}
             <div className="bg-white rounded-xl shadow-lg p-4 mt-6">
               <h3 className="text-lg font-semibold text-indigo-950 mb-3 text-center">Leyenda</h3>
               <ul className="flex items-center gap-4 flex-wrap justify-center">
                 <li className="flex items-center gap-2">
                   <span className="w-5 h-5 rounded-full bg-yellow-200 border-2 border-yellow-300 shadow-sm"></span>
                   <span className="text-sm font-medium text-gray-700">RESERVADA</span>
                 </li>
                 <li className="flex items-center gap-2">
                   <span className="w-5 h-5 rounded-full bg-yellow-500 border-2 border-yellow-600 shadow-sm"></span>
                   <span className="text-sm font-medium text-gray-700">SELECCIONADA</span>
                 </li>
                 <li className="flex items-center gap-2">
                   <span className="w-5 h-5 rounded-full border-2 border-gray-300 shadow-sm" style={{backgroundColor: 'aliceblue'}}></span>
                   <span className="text-sm font-medium text-gray-700">DISPONIBLE</span>
                 </li>
                 <li className="flex items-center gap-2">
                   <span className="w-5 h-5 rounded-full bg-gray-700 border-2 border-gray-800 shadow-sm"></span>
                   <span className="text-sm font-medium text-gray-700">FUERA DE SERVICIO</span>
                 </li>
                 <li className="flex items-center gap-2">
                   <span className="w-5 h-5 rounded-full bg-red-500 border-2 border-red-600 shadow-sm"></span>
                   <span className="text-sm font-medium text-gray-700">OCUPADA</span>
                 </li>
               </ul>
             </div>

             {errorSeleccion && (
                 <div className="bg-red-50 border-l-4 border-red-500 text-red-700 p-4 rounded-lg mt-4 shadow-md">
                   <p className="font-semibold">{errorSeleccion}</p>
                 </div>
               )}
         
             <div className="flex justify-center gap-4 mt-6">
             <Link href="/menu">
               <button className="px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold">
                 Cancelar
               </button>
             </Link>

             <button
               className="px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105"
                onClick={async () => {
                if (seleccionados.length === 0) {
                  setErrorSeleccion("Debes seleccionar al menos una habitación.");
                  setTimeout(() => setErrorSeleccion(""), 2000);
                  return;
                }

                // Generar lista con formato del caso de uso
                const listaHab = listaHabitacionUnica();
                const listaFormateada = rangos.map((rango: { numero: number; desde: string; hasta: string }) => {
                  const hab = listaHab.find(h => h.numero === rango.numero);
                  return {
                    numero: rango.numero,
                    tipo: hab?.tipohabitacion || tipoSeleccionado,
                    desde: rango.desde,
                    hasta: rango.hasta
                  };
                });

                setListaBackend(listaFormateada);
                setMostrarCartelLista(true);
              }}
              > Aceptar
            </button>
            
          </div>

            {mostrarCartelOH && <OcuparHabitacionIgualmente onClose={() => setMostrarCartelOH(false)} />}
          </>
        ): (
            <div className="bg-white rounded-xl shadow-lg p-12 text-center">
              <div className="text-gray-400 text-lg mb-2">
                📅 Seleccione un rango de fechas y tipo para ver la disponibilidad
              </div>
              <p className="text-sm text-gray-500">Complete los filtros en el panel lateral para comenzar</p>
            </div>
        )}

        {/* FORMULARIO DE DATOS DEL HUÉSPED */}
        {mostrarFormulario && (
          <div className="mt-8 p-8 border-2 border-indigo-200 rounded-xl bg-white shadow-2xl">
            <div className="mb-6 pb-4 border-b-2 border-indigo-200">
              <h3 className="text-2xl font-bold text-indigo-950">
                Reserva a nombre de:
              </h3>
              <p className="text-sm text-gray-600 mt-1">Complete los datos del huésped para confirmar la reserva</p>
            </div>
            <form onSubmit={async (e) => {
              e.preventDefault();

              // Validar campos obligatorios
              setErrorNombreObligatorio(!esObligatorio(nombre));
              setErrorApellidoObligatorio(!esObligatorio(apellido));
              setErrorTelefonoObligatorio(!esObligatorio(telefono));

              if (!esObligatorio(nombre) || !esObligatorio(apellido) || !esObligatorio(telefono)) {
                return;
              }

              if (errorNombre || errorApellido || errorTelefono) {
                return;
              }

              // Crear lista de reservas DTO según formato esperado por el backend
              const listaReservasDTO = rangos.map((rango: { numero: number; desde: string; hasta: string }) => ({
                fecha_desde: rango.desde,
                fecha_hasta: rango.hasta,
                nombre: nombre.toUpperCase(),
                apellido: apellido.toUpperCase(),
                telefono: telefono,
                habitacion: {
                  numero: rango.numero
                }
              }));

              try {
                const response = await axios.post("http://localhost:8080/reservas", listaReservasDTO, {
                  headers: {
                    "Content-Type": "application/json",
                  },
                });

                if (response.status === 200 || response.status === 201) {
                  setMostrarFormulario(false);
                  setMostrarCartelConfirmacion(true);
                  setSeleccionados([]);
                  setRangos([]);
                  setNombre("");
                  setApellido("");
                  setTelefono("");
                  // Recargar datos
                  const fetchHabitacionesYReservas = async () => {
                    if (fechasValidas && tipoSeleccionado) {
                      try {
                        const responseHabitaciones = await axios.get(`http://localhost:8080/habitaciones`, {
                          params: { tipo: tipoSeleccionado },
                        });
                        const responseReservas = await axios.get(`http://localhost:8080/reservas`, {
                          params: { desde: desdeFecha, hasta: hastaFecha },
                        });
                        const habitacionesData = responseHabitaciones.data || [];
                        const reservasData = responseReservas.data || [];
                        const habitacionesConReservas = habitacionesData.map((h: any) => {
                          const reservasDeEstaHabitacion = reservasData.filter((r: ReservaDTO) => 
                            r.habitacion?.numero === h.numero
                          );
                          return {
                            ...h,
                            listareservas: reservasDeEstaHabitacion,
                            listaestadias: []
                          };
                        });
                        setHabitaciones(habitacionesConReservas);
                        setReservas(reservasData);
                      } catch(err) {
                        console.error("Error al recargar datos:", err);
                      }
                    }
                  };
                  fetchHabitacionesYReservas();
                }
              } catch (err) {
                console.error("Error al registrar reservas:", err);
                alert("Hubo un error al registrar las reservas.");
              }
            }}>
              <div className="space-y-5">
                <div>
                  <label className="text-indigo-950 font-semibold mb-2 block text-sm">Apellido:</label>
                  <input
                    type="text"
                    value={apellido}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                      const valor = e.target.value.toUpperCase();
                      setApellido(valor);
                      setErrorApellidoObligatorio(false);
                    }}
                    onKeyDown={(e: React.KeyboardEvent<HTMLInputElement>) => {
                      if (e.key === "Tab" && e.shiftKey) {
                        e.preventDefault();
                        const inputs = e.currentTarget.form?.querySelectorAll("input");
                        if (inputs) {
                          const index = Array.from(inputs).indexOf(e.currentTarget as HTMLInputElement);
                          if (index > 0) {
                            (inputs[index - 1] as HTMLInputElement).focus();
                          }
                        }
                      }
                    }}
                    className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                      errorApellido || errorApellidoObligatorio 
                        ? "border-red-500 bg-red-50" 
                        : "border-gray-300 hover:border-indigo-400"
                    }`}
                    autoFocus
                    placeholder="Ingrese el apellido"
                  />
                  {errorApellido && (
                    <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                      <span>⚠</span> Ingrese solo letras.
                    </p>
                  )}
                  {errorApellidoObligatorio && (
                    <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                      <span>⚠</span> El apellido es obligatorio.
                    </p>
                  )}
                </div>

                <div>
                  <label className="text-indigo-950 font-semibold mb-2 block text-sm">Nombre:</label>
                  <input
                    type="text"
                    value={nombre}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                      const valor = e.target.value.toUpperCase();
                      setNombre(valor);
                      setErrorNombreObligatorio(false);
                    }}
                    onKeyDown={(e: React.KeyboardEvent<HTMLInputElement>) => {
                      if (e.key === "Tab" && e.shiftKey) {
                        e.preventDefault();
                        const inputs = e.currentTarget.form?.querySelectorAll("input");
                        if (inputs) {
                          const index = Array.from(inputs).indexOf(e.currentTarget as HTMLInputElement);
                          if (index > 0) {
                            (inputs[index - 1] as HTMLInputElement).focus();
                          }
                        }
                      }
                    }}
                    className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                      errorNombre || errorNombreObligatorio 
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
                  {errorNombreObligatorio && (
                    <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                      <span>⚠</span> El nombre es obligatorio.
                    </p>
                  )}
                </div>

                <div>
                  <label className="text-indigo-950 font-semibold mb-2 block text-sm">Teléfono:</label>
                  <input
                    type="text"
                    value={telefono}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                      const valor = e.target.value.toUpperCase();
                      setTelefono(valor);
                      setErrorTelefonoObligatorio(false);
                    }}
                    onKeyDown={(e: React.KeyboardEvent<HTMLInputElement>) => {
                      if (e.key === "Tab" && e.shiftKey) {
                        e.preventDefault();
                        const inputs = e.currentTarget.form?.querySelectorAll("input");
                        if (inputs) {
                          const index = Array.from(inputs).indexOf(e.currentTarget as HTMLInputElement);
                          if (index > 0) {
                            (inputs[index - 1] as HTMLInputElement).focus();
                          }
                        }
                      }
                    }}
                    className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                      errorTelefono || errorTelefonoObligatorio 
                        ? "border-red-500 bg-red-50" 
                        : "border-gray-300 hover:border-indigo-400"
                    }`}
                    placeholder="Ingrese el teléfono"
                  />
                  {errorTelefono && (
                    <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                      <span>⚠</span> Ingrese un teléfono válido.
                    </p>
                  )}
                  {errorTelefonoObligatorio && (
                    <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                      <span>⚠</span> El teléfono es obligatorio.
                    </p>
                  )}
                </div>

                <div className="flex justify-center gap-4 mt-8 pt-4 border-t border-gray-200">
                  <button
                    type="button"
                    onClick={() => {
                      setMostrarFormulario(false);
                      setSeleccionados([]);
                      setRangos([]);
                    }}
                    className="px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold"
                  >
                    Cancelar
                  </button>
                  <button
                    type="submit"
                    className="px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105"
                  >
                    Confirmar Reserva
                  </button>
                </div>
              </div>
            </form>
          </div>
        )}
      </section>
      

      {/* TABLA DERECHA */}
      {!ocultarTabla && seleccionados.length > 0 && (
        <section className="flex-1 bg-white rounded-xl shadow-xl p-6 h-fit sticky top-4">
          <div className="mb-4 pb-3 border-b-2 border-indigo-200">
            <h2 className="text-xl font-bold text-indigo-950">
              Habitaciones Seleccionadas
            </h2>
            <p className="text-sm text-gray-600 mt-1">{seleccionados.length} día(s) seleccionado(s)</p>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full border-collapse">
              <thead className="bg-gradient-to-r from-indigo-950 to-indigo-900 text-white">
                <tr>
                  <th className="p-3 text-left font-semibold">Eliminar</th>
                  <th className="p-3 text-center font-semibold">Habitación</th>
                  <th className="p-3 text-center font-semibold">Fecha Desde</th>
                  <th className="p-3 text-center font-semibold">Fecha Hasta</th>
                </tr>
              </thead>
              <tbody>
                {rangos.map((ran: { numero: number; desde: string; hasta: string }, index: number) => {
                  const fechaDesde = format(parseISO(ran.desde),"dd/MM/yyyy");
                  const fechaHasta = format(parseISO(ran.hasta),"dd/MM/yyyy");
                  return (
                    <tr key={index} className="bg-white hover:bg-indigo-50 transition-colors border-b border-gray-200">
                      <td className="p-3 text-center">
                        <button
                          onClick={() => deleteSeleccionado(ran)} 
                          className="p-2 rounded-lg hover:bg-red-100 transition-all text-red-600 hover:text-red-700"
                          title="Eliminar esta selección"
                        >
                          <FontAwesomeIcon icon={faTrashCan} />
                        </button>
                      </td>
                      <td className="p-3 text-center font-semibold text-indigo-950">{ran.numero}</td>
                      <td className="p-3 text-center text-gray-700">{fechaDesde}</td>
                      <td className="p-3 text-center text-gray-700">{fechaHasta}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>

          <button
            onClick={eliminarSeleccionados}
            className="w-full mt-6 px-4 py-3 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-all shadow-md hover:shadow-lg font-semibold"
          >
            Eliminar Todas
          </button>
        </section>
      )}

    </main>
  );
}
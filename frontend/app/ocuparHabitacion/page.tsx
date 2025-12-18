'use client';

import React, { useState, useEffect } from "react";
import { format, parseISO, eachDayOfInterval } from "date-fns";
import axios from "../lib/axios";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrashCan } from "@fortawesome/free-solid-svg-icons";

import HabitacionReservadaOcuparIgual from "../carteles/habitacionReservadaOcuparIgual";
import CartelHabitacionNoDisponible from "../carteles/CartelHabitacionNoDisponible";
import CartelNoSeleccionoHuesped from "../carteles/CartelNoSeleccionoHuesped";
import SeguirOcuparHabitacion from "../carteles/SeguirOcuparHabitacion";
import CartelInfoReserva from "../carteles/CartelInfoReserva";
import Link from "next/link";

import { validarFechasReserva, esSoloLetras, esSoloNumeros, validarDNI } from "@/app/validaciones/validaciones";
import { TipoHuesped } from "../tabla/page";

// =========================
// TIPOS E INTERFACES
// =========================
type TipoHabitacion = "IndividualEstandar" | "DobleEstandar" | "Suite" | "DobleSuperior" | "SuperiorFamilyPlan";

interface ReservaDTO {
  id_reserva?: number;
  nro_habitacion?: number;
  fecha_desde: string;
  fecha_hasta: string;
  estado: string;
  nombre?: string;
  apellido?: string;
  telefono?: string;
  habitacion?: {
    numero: number;
  };
}

interface EstadiaDTO {
  checkin: string;
  checkout: string;
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

interface RangoSeleccionado {
  numero: number;
  desde: string;
  hasta: string;
  tieneReserva: boolean;
  reservaId?: number;
  nombreReserva?: string;
  apellidoReserva?: string;
}

type SortKey = keyof TipoHuesped | "dni" | "tipoDocumento";

export default function OcuparHabitacion() {

  // =========================
  // ESTADOS - FASE 1: SELECCIÓN HABITACIONES
  // =========================
  const [fase, setFase] = useState<'habitaciones' | 'huespedes'>('habitaciones');
  
  const [desdeFecha, setDesdeFecha] = useState("");
  const [hastaFecha, setHastaFecha] = useState("");
  const [tipoSeleccionado, setTipoSeleccionado] = useState<"" | TipoHabitacion>("");
  const [erroresFecha, setErroresFecha] = useState({ desdeInvalido: false, hastaInvalido: false, ordenInvalido: false });
  const [fechasValidas, setFechasValidas] = useState(false);

  const [habitaciones, setHabitaciones] = useState<HabitacionDTO[]>([]);
  const [seleccionados, setSeleccionados] = useState<string[]>([]);
  const [habitacionesSeleccionadasInfo, setHabitacionesSeleccionadasInfo] = useState<Record<number, HabitacionDTO>>({});
  const [rangos, setRangos] = useState<RangoSeleccionado[]>([]);
  const [ultimoClick, setUltimoClick] = useState<string | null>(null);

  // Estados carteles fase 1
  const [mostrarCartelNoDisponible, setMostrarCartelNoDisponible] = useState(false);
  const [mostrarCartelReservada, setMostrarCartelReservada] = useState(false);
  const [mostrarCartelInfoReserva, setMostrarCartelInfoReserva] = useState(false);
  const [mensajeReserva, setMensajeReserva] = useState("");
  const [conflictosActuales, setConflictosActuales] = useState<any[]>([]);
  const [errorSeleccion, setErrorSeleccion] = useState("");

  // =========================
  // ESTADOS - FASE 2: BÚSQUEDA HUÉSPEDES
  // =========================
  const [apellido, setApellido] = useState("");
  const [nombre, setNombre] = useState("");
  const [tipoDoc, setTipoDoc] = useState("");
  const [numeroDoc, setNumeroDoc] = useState("");
  
  const [huespedes, setHuespedes] = useState<TipoHuesped[]>([]);
  const [huespedesSeleccionados, setHuespedesSeleccionados] = useState<TipoHuesped[]>([]);
  
  // Estado para manejar huéspedes por cada habitación
  const [indiceHabitacionActual, setIndiceHabitacionActual] = useState(0);
  const [huespedesPorHabitacion, setHuespedesPorHabitacion] = useState<Record<number, TipoHuesped[]>>({});
  
  const [mostrarCartelNoEncontrado, setMostrarCartelNoEncontrado] = useState(false);
  const [mostrarCartelNoSelecciono, setMostrarCartelNoSelecciono] = useState(false);
  const [mostrarSeguir, setMostrarSeguir] = useState(false);

  // Estados de errores búsqueda
  const [errorNombre, setErrorNombre] = useState(false);
  const [errorApellido, setErrorApellido] = useState(false);
  const [errorNumeroDoc, setErrorNumeroDoc] = useState(false);

  // Estado de ordenamiento tabla huéspedes
  const [sortConfig, setSortConfig] = useState<{ key: SortKey | null; direction: 'asc' | 'desc' }>({
    key: "apellido",
    direction: 'asc'
  });

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
  // FETCH DE DATOS - HABITACIONES Y RESERVAS (igual que reservarHabitacion)
  // =========================
  useEffect(() => {
    const fetchHabitacionesYReservas = async () => {
      if (fechasValidas && tipoSeleccionado) {
        try {
          // Obtener habitaciones por tipo
          const responseHabitaciones = await axios.get(`http://localhost:8080/habitaciones`, {
            params: { tipo: tipoSeleccionado },
          });

          // Obtener reservas en el rango de fechas
          const responseReservas = await axios.get(`http://localhost:8080/reservas`, {
            params: { desde: desdeFecha, hasta: hastaFecha },
          });

          const habitacionesData = responseHabitaciones.data || [];
          const reservasData = responseReservas.data || [];

          console.log("Habitaciones recibidas:", habitacionesData);
          console.log("Reservas recibidas:", reservasData);

          // Mapear reservas a cada habitación (igual que en reservarHabitacion)
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
              listaestadias: [] // No se usa GET de estadías
            };
          });

          console.log("Habitaciones con reservas mapeadas:", habitacionesConReservas);
          
          // Ordenar habitaciones por número
          const habitacionesOrdenadas = habitacionesConReservas.sort((a: HabitacionDTO, b: HabitacionDTO) => a.numero - b.numero);
          setHabitaciones(habitacionesOrdenadas);
        } catch(err) {
          console.error("Error al cargar habitaciones y reservas:", err);
          setHabitaciones([]);
        }
      }
    };

    fetchHabitacionesYReservas();
  }, [fechasValidas, tipoSeleccionado, desdeFecha, hastaFecha]);

  // Validaciones en tiempo real para búsqueda de huéspedes
  useEffect(() => {
    setErrorNombre(nombre !== "" && !esSoloLetras(nombre));
  }, [nombre]);

  useEffect(() => {
    setErrorApellido(apellido !== "" && !esSoloLetras(apellido));
  }, [apellido]);

  useEffect(() => {
    setErrorNumeroDoc(numeroDoc !== "" && (!esSoloNumeros(numeroDoc) || !validarDNI(numeroDoc)));
  }, [numeroDoc]);

  // =========================
  // LÓGICA DE SELECCIÓN HABITACIONES
  // =========================
  const fechasIntervalo = (desdeFecha && hastaFecha && fechasValidas)
    ? eachDayOfInterval({ start: parseISO(desdeFecha), end: parseISO(hastaFecha) })
    : [];

  const normalizar = (f?: string) => {
    if (!f) return "";
    return f.includes("T") ? f.split("T")[0] : f;
  };

  const fechaEnRango = (fechaTarget: string, inicio?: string, fin?: string) => {
    const i = normalizar(inicio);
    const f = normalizar(fin);
    if (!i || !f) return false;
    return fechaTarget >= i && fechaTarget <= f;
  };

  // Obtener info de reserva para una fecha/habitación específica
  const obtenerReservaEnFecha = (habitacion: HabitacionDTO, fechaString: string): ReservaDTO | null => {
    if (!habitacion.listareservas) return null;
    return habitacion.listareservas.find(r => 
      fechaEnRango(fechaString, r.fecha_desde, r.fecha_hasta) && 
      (r.estado === "ENCURSO" || r.estado === "PENDIENTE")
    ) || null;
  };

  const toggleSeleccion = (fechaDate: Date, habitacion: HabitacionDTO, event: React.MouseEvent<HTMLTableCellElement>) => {
    const fechaString = format(fechaDate, "yyyy-MM-dd");
    const keyActual = `${fechaString}|${habitacion.numero}`;

    // VALIDACIONES: No permitir seleccionar si está fuera de servicio
    if (habitacion.estado === "FUERADESERVICIO" || habitacion.estado === "FUERA_DE_SERVICIO" || habitacion.estado === "FueraDeServicio") {
      setMostrarCartelNoDisponible(true);
      setTimeout(() => setMostrarCartelNoDisponible(false), 2500);
      return;
    }

    // Validar si hay reserva FINALIZADA (ocupada) - igual que en reservarHabitacion
    const tieneReservaFinalizada = habitacion.listareservas?.some((r: ReservaDTO) => {
      const enRango = fechaEnRango(fechaString, r.fecha_desde, r.fecha_hasta);
      return enRango && r.estado === "FINALIZADA";
    });
    if (tieneReservaFinalizada) {
      setMostrarCartelNoDisponible(true);
      setTimeout(() => setMostrarCartelNoDisponible(false), 2500);
      return;
    }

    // Verificar si la celda clickeada pertenece a una RESERVA (ENCURSO o PENDIENTE)
    const reservaEnFecha = obtenerReservaEnFecha(habitacion, fechaString);
    
    if (reservaEnFecha) {
      // Si clickea sobre una reserva, seleccionar automáticamente el rango EXACTO de la reserva
      const fechaDesdeReserva = normalizar(reservaEnFecha.fecha_desde);
      const fechaHastaReserva = normalizar(reservaEnFecha.fecha_hasta);
      
      // Generar todas las claves del rango de la reserva
      const intervaloReserva = eachDayOfInterval({
        start: parseISO(fechaDesdeReserva),
        end: parseISO(fechaHastaReserva)
      });
      
      const clavesReserva = intervaloReserva.map((f: Date) =>
        `${format(f, "yyyy-MM-dd")}|${habitacion.numero}`
      );
      
      // Verificar si ya está seleccionada la reserva completa (para hacer toggle)
      const todasSeleccionadas = clavesReserva.every(k => seleccionados.includes(k));
      
      if (todasSeleccionadas) {
        // Deseleccionar toda la reserva
        setSeleccionados((prev: string[]) => 
          prev.filter((sel: string) => !clavesReserva.includes(sel))
        );
        
        // Limpiar info si no quedan más selecciones de esta habitación
        const quedanOtras = seleccionados.some((sel: string) => {
          const [_, numero] = sel.split("|");
          return Number(numero) === habitacion.numero && !clavesReserva.includes(sel);
        });
        
        if (!quedanOtras) {
          setHabitacionesSeleccionadasInfo((prevInfo) => {
            const nuevaInfo = { ...prevInfo };
            delete nuevaInfo[habitacion.numero];
            return nuevaInfo;
          });
        }
      } else {
        // Seleccionar toda la reserva
        setSeleccionados((prev: string[]) => [...new Set([...prev, ...clavesReserva])]);
        
        setHabitacionesSeleccionadasInfo((prevInfo) => ({
          ...prevInfo,
          [habitacion.numero]: habitacion
        }));
        
        // Mostrar mensaje con info de la reserva
        const msg = `Reserva seleccionada:\n\nHabitación ${habitacion.numero}\nDesde: ${format(parseISO(fechaDesdeReserva), "dd/MM/yyyy")}\nHasta: ${format(parseISO(fechaHastaReserva), "dd/MM/yyyy")}\nA nombre de: ${reservaEnFecha.nombre || "Sin nombre"} ${reservaEnFecha.apellido || ""}`;
        setMensajeReserva(msg);
        setMostrarCartelInfoReserva(true);
      }
      
      setUltimoClick(keyActual);
      return;
    }

    // SHIFT + CLICK — seleccionar rango de fechas (solo para celdas DISPONIBLES)
    if (event?.shiftKey && ultimoClick) {
      const [fechaPrev, numeroPrev] = ultimoClick.split("|");

      if (Number(numeroPrev) === habitacion.numero) {
        const inicio = parseISO(fechaPrev);
        const fin = parseISO(fechaString);

        const intervalo = eachDayOfInterval({
          start: inicio < fin ? inicio : fin,
          end: inicio < fin ? fin : inicio,
        });

        // Verificar si alguna fecha del intervalo está ocupada (reserva FINALIZADA)
        const hayOcupada = intervalo.some((f: Date) => {
          const fStr = format(f, "yyyy-MM-dd");
          return habitacion.listareservas?.some((r: ReservaDTO) => 
            fechaEnRango(fStr, r.fecha_desde, r.fecha_hasta) && r.estado === "FINALIZADA"
          );
        });

        if (hayOcupada) {
          setMostrarCartelNoDisponible(true);
          setTimeout(() => setMostrarCartelNoDisponible(false), 2500);
          return;
        }

        // Verificar si alguna celda del rango pertenece a una reserva
        const primeraReservaEnRango = intervalo
          .map((f: Date) => obtenerReservaEnFecha(habitacion, format(f, "yyyy-MM-dd")))
          .find(r => r !== null);

        if (primeraReservaEnRango) {
          // Si hay una reserva en el rango, mostrar advertencia y no permitir selección parcial
          const msg = `No se puede seleccionar un rango parcial sobre una reserva.\n\nLa reserva va desde: ${format(parseISO(normalizar(primeraReservaEnRango.fecha_desde)), "dd/MM/yyyy")}\nHasta: ${format(parseISO(normalizar(primeraReservaEnRango.fecha_hasta)), "dd/MM/yyyy")}\nA nombre de: ${primeraReservaEnRango.nombre || "Sin nombre"} ${primeraReservaEnRango.apellido || ""}\n\nDebe seleccionar el rango exacto de la reserva clickeando sobre ella.`;
          setMensajeReserva(msg);
          setMostrarCartelInfoReserva(true);
          return;
        }

        const claves = intervalo.map((f: Date) =>
          `${format(f, "yyyy-MM-dd")}|${habitacion.numero}`
        );

        setSeleccionados((prev: string[]) => [...new Set([...prev, ...claves])]);
        
        setHabitacionesSeleccionadasInfo((prevInfo) => ({
          ...prevInfo,
          [habitacion.numero]: habitacion
        }));
        
        setUltimoClick(keyActual);
        return;
      }
    }

    // Selección simple (toggle) - solo para celdas DISPONIBLES
    setSeleccionados((prev: string[]) => {
      const estaSeleccionado = prev.includes(keyActual);
      const nuevaSeleccion = estaSeleccionado
        ? prev.filter((item: string) => item !== keyActual)
        : [...prev, keyActual];
      
      if (!estaSeleccionado) {
        setHabitacionesSeleccionadasInfo((prevInfo) => ({
          ...prevInfo,
          [habitacion.numero]: habitacion
        }));
      } else {
        const tieneOtrasSelecciones = nuevaSeleccion.some((sel: string) => {
          const [_, numero] = sel.split("|");
          return Number(numero) === habitacion.numero;
        });
        
        if (!tieneOtrasSelecciones) {
          setHabitacionesSeleccionadasInfo((prevInfo) => {
            const nuevaInfo = { ...prevInfo };
            delete nuevaInfo[habitacion.numero];
            return nuevaInfo;
          });
        }
      }
      
      return nuevaSeleccion;
    });

    setUltimoClick(keyActual);
  };

  const eliminarSeleccionados = () => {
    setSeleccionados([]);
    setHabitacionesSeleccionadasInfo({});
  };

  const deleteSeleccionado = (ran: RangoSeleccionado) => {
    const fechasABorrar = eachDayOfInterval({
      start: parseISO(ran.desde),
      end: parseISO(ran.hasta)
    }).map((f: Date) => `${format(f,"yyyy-MM-dd")}|${ran.numero}`);

    setSeleccionados((prev: string[]) => {
      const nuevasSelecciones = prev.filter((sel: string) => !fechasABorrar.includes(sel));
      
      const tieneSelecciones = nuevasSelecciones.some((sel: string) => {
        const [_, numero] = sel.split("|");
        return Number(numero) === ran.numero;
      });
      
      if (!tieneSelecciones) {
        setHabitacionesSeleccionadasInfo((prevInfo) => {
          const nuevaInfo = { ...prevInfo };
          delete nuevaInfo[ran.numero];
          return nuevaInfo;
        });
      }
      
      return nuevasSelecciones;
    });
  };

  // Generar rangos con info de reserva
  function generarRangos(seleccionados: string[]): RangoSeleccionado[] {
    const datos = seleccionados.map(sel => {
      const [fecha, numero] = sel.split("|");
      const hab = habitacionesSeleccionadasInfo[Number(numero)] || habitaciones.find(h => h.numero === Number(numero));
      const reserva = hab ? obtenerReservaEnFecha(hab, fecha) : null;
      
      return {
        fecha: fecha,
        fechaDate: parseISO(fecha),
        numero: Number(numero),
        tieneReserva: !!reserva,
        reservaId: reserva?.id_reserva,
        nombreReserva: reserva?.nombre,
        apellidoReserva: reserva?.apellido
      };
    });

    datos.sort((a, b) => {
      if (a.numero !== b.numero) return a.numero - b.numero;
      return a.fechaDate.getTime() - b.fechaDate.getTime();
    });

    const resultado: RangoSeleccionado[] = [];
    let actual: RangoSeleccionado | null = null;

    for (let item of datos) {
      if (!actual) {
        actual = { 
          numero: item.numero, 
          desde: item.fecha, 
          hasta: item.fecha,
          tieneReserva: item.tieneReserva,
          reservaId: item.reservaId,
          nombreReserva: item.nombreReserva,
          apellidoReserva: item.apellidoReserva
        };
        continue;
      }

      const esMismaHab = item.numero === actual.numero;
      const diffDias = (item.fechaDate.getTime() - parseISO(actual.hasta).getTime()) / (1000 * 60 * 60 * 24);

      if (esMismaHab && diffDias === 1) {
        actual.hasta = item.fecha;
        // Si alguna fecha tiene reserva, el rango tiene reserva
        if (item.tieneReserva) {
          actual.tieneReserva = true;
          actual.reservaId = item.reservaId;
          actual.nombreReserva = item.nombreReserva;
          actual.apellidoReserva = item.apellidoReserva;
        }
      } else {
        resultado.push(actual);
        actual = { 
          numero: item.numero, 
          desde: item.fecha, 
          hasta: item.fecha,
          tieneReserva: item.tieneReserva,
          reservaId: item.reservaId,
          nombreReserva: item.nombreReserva,
          apellidoReserva: item.apellidoReserva
        };
      }
    }
    if (actual) resultado.push(actual);
    return resultado;
  }

  useEffect(() => {
    const nuevosRangos = generarRangos(seleccionados);
    setRangos(nuevosRangos);
  }, [seleccionados, habitacionesSeleccionadasInfo]);

  // =========================
  // LÓGICA BOTÓN ACEPTAR - FASE 1
  // =========================
  const handleAceptarHabitaciones = () => {
    if (seleccionados.length === 0) {
      setErrorSeleccion("Debes seleccionar al menos una habitación.");
      setTimeout(() => setErrorSeleccion(""), 2000);
      return;
    }

    // Verificar si hay conflictos con reservas
    const conflictos: any[] = [];

    for (const sel of seleccionados) {
      const [fechaStr, habStr] = sel.split("|");
      const numero = Number(habStr);

      const habitacion = habitacionesSeleccionadasInfo[numero] || habitaciones.find(h => h.numero === numero);
      if (!habitacion || !habitacion.listareservas) continue;

      const reservaSolapada = habitacion.listareservas.find(r =>
        fechaEnRango(fechaStr, r.fecha_desde, r.fecha_hasta) &&
        (r.estado === "ENCURSO" || r.estado === "PENDIENTE")
      );

      if (reservaSolapada && !conflictos.find(c => c.reservaId === reservaSolapada.id_reserva)) {
        conflictos.push({
          numero,
          desde: reservaSolapada.fecha_desde,
          hasta: reservaSolapada.fecha_hasta,
          nombre: reservaSolapada.nombre || "Sin nombre",
          apellido: reservaSolapada.apellido || "",
          fecha: fechaStr,
          reservaId: reservaSolapada.id_reserva
        });
      }
    }

    // Si hay conflictos, mostrar cartel de "Ocupar Igual"
    if (conflictos.length > 0) {
      let msg = "";
      conflictos.forEach(c => {
        msg += `Habitación ${c.numero}\n`;
        msg += `Reservada desde: ${format(parseISO(c.desde), "dd/MM/yyyy")}\n`;
        msg += `Reservada hasta: ${format(parseISO(c.hasta), "dd/MM/yyyy")}\n`;
        msg += `A nombre de: ${c.nombre} ${c.apellido}\n\n`;
      });

      setMensajeReserva(msg);
      setConflictosActuales(conflictos);
      setMostrarCartelReservada(true);
      return;
    }

    // Si no hay conflictos, pasar directamente a fase de huéspedes
    setIndiceHabitacionActual(0);
    setHuespedesPorHabitacion({});
    setFase('huespedes');
  };

  // =========================
  // BÚSQUEDA DE HUÉSPEDES - FASE 2
  // =========================
  const buscarHuesped = async (e: React.FormEvent) => {
    e.preventDefault();

    const nombreInvalido = nombre !== "" && !esSoloLetras(nombre);
    const apellidoInvalido = apellido !== "" && !esSoloLetras(apellido);
    const numeroDocInvalido = numeroDoc !== "" && (!esSoloNumeros(numeroDoc) || !validarDNI(numeroDoc));

    setErrorNombre(nombreInvalido);
    setErrorApellido(apellidoInvalido);
    setErrorNumeroDoc(numeroDocInvalido);

    if (nombreInvalido || apellidoInvalido || numeroDocInvalido) {
      return;
    }

    try {
      const params: any = {
        apellido: apellido || "",
        nombre: nombre || "",
        tipoDocumento: tipoDoc || "",
        dni: numeroDoc || ""
      };

      const response = await axios.get("http://localhost:8080/huespedes", { params });

      setHuespedes(response.data || []);

      if (!response.data || response.data.length === 0) {
        setMostrarCartelNoEncontrado(true);
      }
    } catch (error) {
      console.error("Error al buscar huéspedes:", error);
      setHuespedes([]);
      setMostrarCartelNoEncontrado(true);
    }
  };

  // Toggle selección de huésped (un click selecciona, otro deselecciona)
  const toggleSeleccionHuesped = (huesped: TipoHuesped) => {
    setHuespedesSeleccionados(prev => {
      const existe = prev.some(h => 
        h.huespedID.dni === huesped.huespedID.dni && 
        h.huespedID.tipoDocumento === huesped.huespedID.tipoDocumento
      );
      if (existe) {
        return prev.filter(h => 
          !(h.huespedID.dni === huesped.huespedID.dni && 
            h.huespedID.tipoDocumento === huesped.huespedID.tipoDocumento)
        );
      } else {
        return [...prev, huesped];
      }
    });
  };

  // Función de ordenamiento
  const handleSort = (key: SortKey) => {
    let direction: 'asc' | 'desc' = 'asc';
    if (sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });
  };

  // Huéspedes ordenados
  const sortedHuespedes = [...huespedes].sort((a, b) => {
    if (!sortConfig.key) return 0;

    const key = sortConfig.key;
    let aValue: any;
    let bValue: any;

    if (key === "dni") {
      aValue = a.huespedID.dni;
      bValue = b.huespedID.dni;
    } else if (key === "tipoDocumento") {
      aValue = a.huespedID.tipoDocumento;
      bValue = b.huespedID.tipoDocumento;
    } else {
      aValue = a[key];
      bValue = b[key];
    }

    if (aValue < bValue) return sortConfig.direction === "asc" ? -1 : 1;
    if (aValue > bValue) return sortConfig.direction === "asc" ? 1 : -1;
    return 0;
  });

  // =========================
  // ACEPTAR HUÉSPEDES (sin enviar estadía todavía)
  // =========================
  const handleAceptarHuespedes = () => {
    if (huespedesSeleccionados.length === 0) {
      setMostrarCartelNoSelecciono(true);
      return;
    }

    // Guardar huéspedes de la habitación actual
    const nuevosHuespedesPorHabitacion = {
      ...huespedesPorHabitacion,
      [indiceHabitacionActual]: [...huespedesSeleccionados]
    };
    setHuespedesPorHabitacion(nuevosHuespedesPorHabitacion);

    // Si hay más habitaciones, pasar a la siguiente
    if (indiceHabitacionActual < rangos.length - 1) {
      setIndiceHabitacionActual(indiceHabitacionActual + 1);
      // Limpiar selección de huéspedes para la nueva habitación
      setHuespedesSeleccionados([]);
      setHuespedes([]);
      setApellido("");
      setNombre("");
      setTipoDoc("");
      setNumeroDoc("");
      return;
    }

    // Si es la última habitación, mostrar cartel de opciones (SIN enviar todavía)
    setMostrarSeguir(true);
  };

  // =========================
  // ENVIAR ESTADÍAS AL BACKEND
  // =========================
  const enviarEstadias = async (): Promise<boolean> => {
    try {
      for (let i = 0; i < rangos.length; i++) {
        const rango = rangos[i];
        const huespedesDeEstaHabitacion = huespedesPorHabitacion[i] || [];
        
        const listaHuespedes = huespedesDeEstaHabitacion.map(h => ({
          huespedID: {
            tipoDocumento: h.huespedID.tipoDocumento,
            dni: h.huespedID.dni
          }
        }));

        let estadiaDTO: any;

        if (rango.tieneReserva && rango.reservaId) {
          // CON RESERVA: incluir id_reserva, NO incluir checkout
          estadiaDTO = {
            checkin: rango.desde,
            habitacion: {
              numero: rango.numero
            },
            listahuesped: listaHuespedes,
            reserva: {
              id_reserva: rango.reservaId
            }
          };
        } else {
          // SIN RESERVA: incluir checkout, NO incluir reserva
          estadiaDTO = {
            checkin: rango.desde,
            checkout: rango.hasta,
            habitacion: {
              numero: rango.numero
            },
            listahuesped: listaHuespedes
          };
        }

        console.log(`=== DTO HABITACIÓN ${rango.numero} ===`);
        console.log(JSON.stringify(estadiaDTO, null, 2));
        console.log("======================================");

        await axios.post("http://localhost:8080/estadias", estadiaDTO, {
          headers: {
            "Content-Type": "application/json",
          },
        });
      }
      return true;
    } catch (error) {
      console.error("Error al registrar estadía:", error);
      alert("Hubo un error al registrar la estadía.");
      return false;
    }
  };

  // =========================
  // FUNCIONES DE NAVEGACIÓN
  // =========================
  const handleSeguirCargando = () => {
    // Volver a la fase de huéspedes para seguir agregando (NO envía estadías)
    setIndiceHabitacionActual(0);
    setHuespedesSeleccionados([]);
    setHuespedes([]);
    setApellido("");
    setNombre("");
    setTipoDoc("");
    setNumeroDoc("");
    setMostrarSeguir(false);
  };

  const handleCargarOtraHabitacion = async () => {
    // Primero enviar las estadías
    const exito = await enviarEstadias();
    if (exito) {
      // Luego hacer reload de la página
      window.location.reload();
    }
  };

  const handleSalir = async () => {
    // Primero enviar las estadías
    const exito = await enviarEstadias();
    if (exito) {
      // Luego ir al menú (el Link ya maneja la navegación)
    }
  };

  // Manejar Shift+Tab para retroceder
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

  // =========================
  // RENDER
  // =========================
  return (
    <main className="flex gap-8 px-8 py-8 items-start bg-gradient-to-br from-gray-50 to-gray-100 min-h-screen">

      {/* ========================= */}
      {/* FASE 1: SELECCIÓN DE HABITACIONES */}
      {/* ========================= */}
      {fase === 'habitaciones' && (
        <>
          {/* FORMULARIO DE FECHAS Y TIPO */}
          <div className="bg-white rounded-xl shadow-lg p-6 w-80 h-fit sticky top-4">
            <h2 className="text-2xl font-bold text-indigo-950 mb-6 pb-3 border-b-2 border-indigo-200">Filtros de Búsqueda</h2>
            <form className="flex flex-col space-y-4">
              <div>
                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Desde Fecha:</label>
                <input
                  type="date"
                  value={desdeFecha}
                  onChange={(e) => setDesdeFecha(e.target.value)}
                  onBlur={(e) => validarDesde(e.target.value)}
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
                  onChange={(e) => setHastaFecha(e.target.value)}
                  onBlur={(e) => validarHasta(e.target.value)}
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
                  onChange={(e) => setTipoSeleccionado(e.target.value as TipoHabitacion)}
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
                  <option value="IndividualEstandar">Individual Estandar</option>
                  <option value="DobleEstandar">Doble Estandar</option>
                  <option value="DobleSuperior">Doble Superior</option>
                  <option value="SuperiorFamilyPlan">Superior Family Plan</option>
                  <option value="Suite">Suite</option>
                </select>
              </div>
            </form>
          </div>

          {/* TABLA ESTADO HABITACIÓN */}
          <section className="flex-1">
            {tipoSeleccionado && fechasIntervalo.length > 0 ? (
              <>
                <div className="bg-white rounded-xl shadow-xl overflow-hidden">
                  <div className="max-h-[600px] overflow-y-auto overflow-x-auto">
                    <table className="w-full border-collapse">
                      <thead className="bg-gradient-to-r from-indigo-950 to-indigo-900 text-white sticky top-0 z-10 shadow-md">
                        <tr>
                          <th className="p-4 font-semibold text-left">Fecha</th>
                          <th colSpan={habitaciones.length} className="p-4 font-semibold text-center">Habitaciones</th>
                        </tr>
                      </thead>

                      <tbody>
                        <tr className="bg-indigo-50">
                          <td className="p-3 border-b-2 border-indigo-200 font-semibold text-indigo-950"></td>
                          {habitaciones.map(hab => (
                            <td key={hab.numero} className="p-3 border-b-2 border-indigo-200 text-center font-bold text-indigo-950">{hab.numero}</td>
                          ))}
                        </tr>

                        {fechasIntervalo.map((fechaDate: Date) => {
                          const fechaString = format(fechaDate, "yyyy-MM-dd");
                          const fechaDisplay = format(fechaDate, "dd/MM/yyyy");

                          return (
                            <tr key={fechaString} className="hover:bg-indigo-50/50 transition-colors">
                              <td className="p-3 border-b border-gray-200 text-center font-semibold text-indigo-950 bg-white sticky left-0 z-5">{fechaDisplay}</td>
                              {habitaciones.map((hab: HabitacionDTO) => {
                                const key = `${fechaString}|${hab.numero}`;
                                const esSeleccionado = seleccionados.includes(key);

                                // 1. CHEQUEO FUERA DE SERVICIO
                                const esFueraServicio = hab.estado === "FUERADESERVICIO" || hab.estado === "FUERA_DE_SERVICIO" || hab.estado === "FueraDeServicio";

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

                                let bgClass = "";
                                if (esFueraServicio) {
                                  bgClass = "bg-gray-700"; // Gris para fuera de servicio
                                } else if (esOcupada) {
                                  bgClass = "bg-red-500"; // Roja para ocupada (Finalizada)
                                } else if (esSeleccionado) {
                                  bgClass = "bg-yellow-500"; // Amarillo oscuro cuando el usuario selecciona (tiene prioridad sobre reservada)
                                } else if (esReservada) {
                                  bgClass = "bg-yellow-200"; // Amarillo claro para reservada (EN CURSO/Pendiente)
                                }

                                return (
                                  <td
                                    key={hab.numero}
                                    className={`p-4 border-b border-gray-200 cursor-pointer transition-all hover:scale-105 hover:shadow-md ${bgClass}`}
                                    onClick={(e) => toggleSeleccion(fechaDate, hab, e)}
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
                    onClick={handleAceptarHabitaciones}
                  >
                    Aceptar
                  </button>
                </div>
              </>
            ) : (
              <div className="bg-white rounded-xl shadow-lg p-12 text-center">
                <div className="text-gray-400 text-lg mb-2">
                  📅 Seleccione un rango de fechas y tipo para ver la disponibilidad
                </div>
                <p className="text-sm text-gray-500">Complete los filtros en el panel lateral para comenzar</p>
              </div>
            )}
          </section>

          {/* TABLA DERECHA - HABITACIONES SELECCIONADAS */}
          {seleccionados.length > 0 && (
            <section className="flex-1 bg-white rounded-xl shadow-xl p-6 h-fit sticky top-4 max-w-md">
              <div className="mb-4 pb-3 border-b-2 border-indigo-200">
                <h2 className="text-xl font-bold text-indigo-950">
                  Habitaciones Seleccionadas
                </h2>
                <p className="text-sm text-gray-600 mt-1">{seleccionados.length} día(s) seleccionado(s)</p>
              </div>
              <div className="overflow-x-auto max-h-[400px] overflow-y-auto">
                <table className="w-full border-collapse">
                  <thead className="bg-gradient-to-r from-indigo-950 to-indigo-900 text-white sticky top-0">
                    <tr>
                      <th className="p-3 text-left font-semibold">Eliminar</th>
                      <th className="p-3 text-center font-semibold">Hab.</th>
                      <th className="p-3 text-center font-semibold">Desde</th>
                      <th className="p-3 text-center font-semibold">Hasta</th>
                      <th className="p-3 text-center font-semibold">Reserva</th>
                    </tr>
                  </thead>
                  <tbody>
                    {rangos.map((ran, index) => {
                      const fechaDesde = format(parseISO(ran.desde), "dd/MM/yyyy");
                      const fechaHasta = format(parseISO(ran.hasta), "dd/MM/yyyy");
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
                          <td className="p-3 text-center text-gray-700 text-sm">{fechaDesde}</td>
                          <td className="p-3 text-center text-gray-700 text-sm">{fechaHasta}</td>
                          <td className="p-3 text-center">
                            {ran.tieneReserva ? (
                              <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
                                Sí
                              </span>
                            ) : (
                              <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800">
                                No
                              </span>
                            )}
                          </td>
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
        </>
      )}

      {/* ========================= */}
      {/* FASE 2: BÚSQUEDA Y SELECCIÓN DE HUÉSPEDES */}
      {/* ========================= */}
      {fase === 'huespedes' && (
        <>
          {/* FORMULARIO DE BÚSQUEDA DE HUÉSPEDES */}
          <div className="bg-white rounded-xl shadow-lg p-6 w-80 h-fit sticky top-4">
            {/* Indicador de habitación actual */}
            <div className="bg-indigo-100 rounded-lg p-3 mb-4 text-center">
              <p className="text-indigo-950 font-bold text-lg">
                Habitación {rangos[indiceHabitacionActual]?.numero}
              </p>
              <p className="text-indigo-700 text-sm">
                ({indiceHabitacionActual + 1} de {rangos.length})
              </p>
              <p className="text-indigo-600 text-xs mt-1">
                {rangos[indiceHabitacionActual]?.desde && format(parseISO(rangos[indiceHabitacionActual].desde), "dd/MM/yyyy")} - {rangos[indiceHabitacionActual]?.hasta && format(parseISO(rangos[indiceHabitacionActual].hasta), "dd/MM/yyyy")}
              </p>
            </div>
            
            <h2 className="text-2xl font-bold text-indigo-950 mb-6 pb-3 border-b-2 border-indigo-200">
              Criterios de Búsqueda
            </h2>
            <form onSubmit={buscarHuesped} className="flex flex-col space-y-4">
              <div>
                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Apellido:</label>
                <input
                  type="text"
                  value={apellido}
                  onChange={(e) => setApellido(e.target.value.toUpperCase())}
                  onKeyDown={handleKeyDownInput}
                  className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                    errorApellido ? "border-red-500 bg-red-50" : "border-gray-300 hover:border-indigo-400"
                  }`}
                  placeholder="Ingrese el apellido"
                />
                {errorApellido && (
                  <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                    <span>⚠</span> Ingrese solo letras.
                  </p>
                )}
              </div>

              <div>
                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Nombres:</label>
                <input
                  type="text"
                  value={nombre}
                  onChange={(e) => setNombre(e.target.value.toUpperCase())}
                  onKeyDown={handleKeyDownInput}
                  className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                    errorNombre ? "border-red-500 bg-red-50" : "border-gray-300 hover:border-indigo-400"
                  }`}
                  placeholder="Ingrese el nombre"
                />
                {errorNombre && (
                  <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                    <span>⚠</span> Ingrese solo letras.
                  </p>
                )}
              </div>

              <div>
                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Tipo de Documento:</label>
                <select
                  value={tipoDoc}
                  onChange={(e) => setTipoDoc(e.target.value)}
                  onKeyDown={handleKeyDownInput}
                  className="w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent border-gray-300 hover:border-indigo-400 bg-white"
                >
                  <option value="">Seleccionar tipo</option>
                  <option value="DNI">DNI</option>
                  <option value="LE">LE</option>
                  <option value="LC">LC</option>
                  <option value="PASAPORTE">Pasaporte</option>
                  <option value="OTRO">Otro</option>
                </select>
              </div>

              <div>
                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Número de Documento:</label>
                <input
                  type="text"
                  value={numeroDoc}
                  onChange={(e) => setNumeroDoc(e.target.value)}
                  onKeyDown={handleKeyDownInput}
                  className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                    errorNumeroDoc ? "border-red-500 bg-red-50" : "border-gray-300 hover:border-indigo-400"
                  }`}
                  placeholder="Ingrese el número"
                />
                {errorNumeroDoc && (
                  <p className="text-red-600 text-xs mt-1 flex items-center gap-1">
                    <span>⚠</span> Ingrese un DNI válido.
                  </p>
                )}
              </div>

              <button
                type="submit"
                className="w-full px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105"
              >
                Buscar
              </button>
            </form>

            {/* Resumen de habitaciones seleccionadas */}
            <div className="mt-6 pt-4 border-t-2 border-indigo-200">
              <h3 className="text-sm font-semibold text-indigo-950 mb-2">Habitaciones a ocupar:</h3>
              <div className="flex flex-wrap gap-2">
                {rangos.map((r, i) => (
                  <span key={i} className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800">
                    Hab. {r.numero}
                  </span>
                ))}
              </div>
            </div>
          </div>

          {/* TABLA DE RESULTADOS */}
          <section className="flex-1">
            <div className="bg-white rounded-xl shadow-xl overflow-hidden">
              <div className="p-6 border-b-2 border-indigo-200">
                <h2 className="text-2xl font-bold text-indigo-950">
                  Selección de Huéspedes
                </h2>
                <p className="text-sm text-gray-600 mt-1">
                  {huespedes.length > 0 
                    ? `${huespedes.length} huésped(es) encontrado(s) - ${huespedesSeleccionados.length} seleccionado(s)` 
                    : "Busque huéspedes para agregarlos a la estadía"}
                </p>
              </div>

              {huespedes.length > 0 ? (
                <div className="max-h-[500px] overflow-y-auto overflow-x-auto">
                  <table className="w-full border-collapse">
                    <thead className="bg-gradient-to-r from-indigo-950 to-indigo-900 text-white sticky top-0 z-10 shadow-md">
                      <tr>
                        <th className="p-4 font-semibold text-center">Seleccionar</th>
                        <th 
                          className="p-4 font-semibold text-left cursor-pointer hover:bg-indigo-800 transition-colors"
                          onClick={() => handleSort("apellido")}
                        >
                          Apellido {sortConfig.key === "apellido" ? (sortConfig.direction === "asc" ? "↑" : "↓") : "↑↓"}
                        </th>
                        <th 
                          className="p-4 font-semibold text-left cursor-pointer hover:bg-indigo-800 transition-colors"
                          onClick={() => handleSort("nombre")}
                        >
                          Nombre {sortConfig.key === "nombre" ? (sortConfig.direction === "asc" ? "↑" : "↓") : "↑↓"}
                        </th>
                        <th 
                          className="p-4 font-semibold text-left cursor-pointer hover:bg-indigo-800 transition-colors"
                          onClick={() => handleSort("tipoDocumento")}
                        >
                          Tipo Doc. {sortConfig.key === "tipoDocumento" ? (sortConfig.direction === "asc" ? "↑" : "↓") : "↑↓"}
                        </th>
                        <th 
                          className="p-4 font-semibold text-left cursor-pointer hover:bg-indigo-800 transition-colors"
                          onClick={() => handleSort("dni")}
                        >
                          Nro. Doc. {sortConfig.key === "dni" ? (sortConfig.direction === "asc" ? "↑" : "↓") : "↑↓"}
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {sortedHuespedes.map((h) => {
                        const isSelected = huespedesSeleccionados.some(
                          hs => hs.huespedID.dni === h.huespedID.dni && hs.huespedID.tipoDocumento === h.huespedID.tipoDocumento
                        );
                        return (
                          <tr
                            key={`${h.huespedID.tipoDocumento}-${h.huespedID.dni}`}
                            className={`hover:bg-indigo-50 transition-colors ${
                              isSelected ? "bg-green-50" : "bg-white"
                            }`}
                          >
                            <td className="p-4 border-b border-gray-200 text-center">
                              <input
                                type="checkbox"
                                checked={isSelected}
                                onChange={() => toggleSeleccionHuesped(h)}
                                className="w-5 h-5 text-green-600 focus:ring-green-500 rounded"
                              />
                            </td>
                            <td className="p-4 border-b border-gray-200 font-medium text-indigo-950">
                              {h.apellido}
                            </td>
                            <td className="p-4 border-b border-gray-200 text-gray-700">
                              {h.nombre}
                            </td>
                            <td className="p-4 border-b border-gray-200 text-gray-700">
                              {h.huespedID.tipoDocumento}
                            </td>
                            <td className="p-4 border-b border-gray-200 text-gray-700">
                              {h.huespedID.dni}
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
                    Puede ingresar ninguno, uno o varios criterios de búsqueda
                  </p>
                </div>
              )}
            </div>

            {/* HUÉSPEDES SELECCIONADOS */}
            {huespedesSeleccionados.length > 0 && (
              <div className="bg-white rounded-xl shadow-lg p-4 mt-6">
                <h3 className="text-lg font-semibold text-indigo-950 mb-3">Huéspedes seleccionados para la estadía:</h3>
                <div className="flex flex-wrap gap-2">
                  {huespedesSeleccionados.map((h, i) => (
                    <span 
                      key={i} 
                      className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-green-100 text-green-800"
                    >
                      {h.apellido}, {h.nombre}
                    </span>
                  ))}
                </div>
              </div>
            )}

            {/* BOTONES DE ACCIÓN */}
            <div className="flex justify-center gap-4 mt-6">
              <button
                onClick={() => setFase('habitaciones')}
                className="px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold"
              >
                Volver
              </button>

              <Link href="/menu">
                <button className="px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold">
                  Cancelar
                </button>
              </Link>

              <button
                onClick={handleAceptarHuespedes}
                className="px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105"
              >
                Aceptar
              </button>
            </div>
          </section>
        </>
      )}

      {/* ========================= */}
      {/* CARTELES */}
      {/* ========================= */}
      
      {mostrarCartelNoDisponible && (
        <CartelHabitacionNoDisponible
          mensaje="La habitación seleccionada no se encuentra disponible"
          onClose={() => setMostrarCartelNoDisponible(false)}
        />
      )}

      {mostrarCartelReservada && (
        <HabitacionReservadaOcuparIgual
          mensajeExtra={mensajeReserva}
          onClose={() => setMostrarCartelReservada(false)}
          onConfirm={() => {
            setMostrarCartelReservada(false);
            setIndiceHabitacionActual(0);
            setHuespedesPorHabitacion({});
            setFase('huespedes');
          }}
        />
      )}

      {mostrarCartelInfoReserva && (
        <CartelInfoReserva
          mensaje={mensajeReserva}
          onClose={() => setMostrarCartelInfoReserva(false)}
        />
      )}

      {mostrarCartelNoEncontrado && (
        <div className="fixed inset-0 flex items-center justify-center z-[9999]">
          <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>
          <div className="relative bg-white rounded-lg shadow-lg p-6 w-96 text-center">
            <img src="/imagenInformacion.png" alt="Info" className="absolute top-3 left-3 w-10 h-10" />
            <h2 className="text-xl font-bold text-indigo-950 mb-4">No se encontraron huéspedes</h2>
            <p className="text-gray-700 mb-6">No hay huéspedes que coincidan con los criterios de búsqueda.</p>
            <button
              onClick={() => setMostrarCartelNoEncontrado(false)}
              className="px-6 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
            >
              Aceptar
            </button>
          </div>
        </div>
      )}

      {mostrarCartelNoSelecciono && (
        <CartelNoSeleccionoHuesped
          onAceptar={() => setMostrarCartelNoSelecciono(false)}
        />
      )}

      {mostrarSeguir && (
        <SeguirOcuparHabitacion
          onSeguirCargando={handleSeguirCargando}
          onCargarMasHabitaciones={handleCargarOtraHabitacion}
          onSalir={handleSalir}
        />
      )}

    </main>
  );
}

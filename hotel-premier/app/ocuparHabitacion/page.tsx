'use client';

import React, { useState, useEffect } from "react";
import { format, parseISO, eachDayOfInterval, isAfter } from "date-fns";
import axios from "axios";

import HabitacionReservadaOcuparIgual from "../carteles/habitacionReservadaOcuparIgual";
import CartelHabitacionNoDisponible from "../carteles/CartelHabitacionNoDisponible";
import CartelHabitacionesOcupadas from "../carteles/cartelHabitacionesOcupadas";
import CartelPresioneTecla from "../carteles/CartelPresioneTecla";
import Link from "next/link";

import { validarFechasReserva } from "@/app/validaciones/validaciones";


// =========================
// TIPOS E INTERFACES
// =========================
type TipoHabitacion = "IndividualEstandar" | "DobleEstandar" | "Suite" | "DobleSuperior" | "SuperiorFamilyPlan";

interface ReservaDTO {
  nro_habitacion: number;
  fecha_desde: string;
  fecha_hasta: string;
  estado: string;
  nombre: string;
  apellido: string;
  telefono?: number;
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
  listareservas: ReservaDTO[];
  listaestadias: EstadiaDTO[];
}

interface Props {
  ocultarTabla?: boolean;
}



export default function OcuparHabitacion({ ocultarTabla = false }: Props) {

  // =========================
  // ESTADOS
  // =========================
  const [desdeFecha, setDesdeFecha] = useState("");
  const [hastaFecha, setHastaFecha] = useState("");
  const [tipoSeleccionado, setTipoSeleccionado] = useState<"" | TipoHabitacion>("");
  const [erroresFecha, setErroresFecha] = useState({ desdeInvalido: false, hastaInvalido: false, ordenInvalido: false });
  const [fechasValidas, setFechasValidas] = useState(false);

  // Estados para mostrar carteles
  const [mostrarCartelNoDisponible, setMostrarCartelNoDisponible] = useState(false);
  const [mostrarCartelReservada, setMostrarCartelReservada] = useState(false);
  const [mostrarCartelLista, setMostrarCartelLista] = useState(false);
  const [mostrarCartelPresioneTecla, setMostrarCartelPresioneTecla] = useState(false);
  

  const [habitaciones, setHabitaciones] = useState<HabitacionDTO[] | []>([]);
  const [seleccionados, setSeleccionados] = useState<string[]>([]);


  const [mensajeReserva, setMensajeReserva] = useState("");



  //const [mostrarCartelReservada, setMostrarCartelReservada] = useState(false);

  
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
  useEffect(() => {
    const fetchDetalleHabitaciones = async () => {
      if (fechasValidas && tipoSeleccionado) {
        try {
          const response = await axios.get(`http://localhost:8080/detalleHabitaciones`, {
            params: { 
              tipo: tipoSeleccionado,
              fechaDesde: desdeFecha, 
              fechaHasta: hastaFecha 
            },
          });
          setHabitaciones(response.data);
        } catch(err) {
          console.error("Error al cargar detalle habitaciones:", err);
          // MOCK DATA en caso de error para visualización
          const mockHabitaciones: HabitacionDTO[] = Array.from({length: 5}, (_, i) => ({
             numero: 101 + i,
             estado: i === 2 ? "FueraDeServicio" : "DISPONIBLE",
             precio: 100,
             cantidadPersonas: 2,
             tipohabitacion: tipoSeleccionado || "DobleEstandar",
             listareservas: i === 1 ? [{ nro_habitacion: 102, fecha_desde: desdeFecha, fecha_hasta: hastaFecha, estado: "RESERVADA", nombre: "MockNombre", apellido: "MockApellido" }] : [],
             listaestadias: i === 3 ? [{ checkin: desdeFecha, checkout: hastaFecha }] : []
          }));
          setHabitaciones(mockHabitaciones);
        }
      }
    };
    fetchDetalleHabitaciones();
  }, [fechasValidas, tipoSeleccionado, desdeFecha, hastaFecha]);

  // =========================
  // LÓGICA DE SELECCIÓN
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
    if (!i || !f) return false;         // si faltan fechas, no hay rango válido
    return fechaTarget >= i && fechaTarget <= f;
  };


  const ejecutarSeleccion = (fechaString: string, habitacion: HabitacionDTO) => {
    const key = `${fechaString}|${habitacion.numero}`;
    setSeleccionados(prev =>
      prev.includes(key) ? prev.filter(item => item!==key) : [...prev, key]
    );
  };

  const handleClickCelda = (fechaDate: Date, hab: HabitacionDTO) => {
    const fechaString = format(fechaDate, "yyyy-MM-dd");

    const esFueraServicio = hab.estado === "FueraDeServicio";

    const esOcupada = hab.listaestadias?.some(e =>
      fechaEnRango(fechaString, e.checkin, e.checkout)
    );

    // ❌ NO SE PUEDE TOCAR: OCUPADA / FUERA DE SERVICIO
    if (esFueraServicio || esOcupada) {
      setMostrarCartelNoDisponible(true);
      return;
    }

    // ✅ DISPONIBLE O RESERVADA
    // Se selecciona directamente. El color verde lo dará el render al detectar que está en "seleccionados"
    ejecutarSeleccion(fechaString, hab);
  };


    const confirmarOcupacion = () => {
        setHabitaciones(prev =>
            prev.map(h => {
            // 🔎 Fechas seleccionadas SOLO de esta habitación
            const fechasDeEsta = seleccionados
                .filter(s => s.split("|")[1] === String(h.numero))
                .map(s => s.split("|")[0])
                .sort(); // ordenarlas

            if (fechasDeEsta.length === 0) return h;

            // Convertimos fechas sueltas en rangos contiguos
            const nuevosRangos: { checkin: string; checkout: string }[] = [];

            let inicio = fechasDeEsta[0];
            let fin = fechasDeEsta[0];

            for (let i = 1; i < fechasDeEsta.length; i++) {
                const fechaActual = fechasDeEsta[i];
                const fechaAnterior = fechasDeEsta[i - 1];

                const dActual = parseISO(fechaActual);
                const dAnterior = parseISO(fechaAnterior);

                // Si es el día siguiente → continúa el rango
                const diferenciaDias = (dActual.getTime() - dAnterior.getTime()) / (1000 * 60 * 60 * 24);

                if (diferenciaDias === 1) {
                fin = fechaActual;
                } else {
                // Cerrar el rango anterior
                nuevosRangos.push({ checkin: inicio, checkout: fin });
                inicio = fechaActual;
                fin = fechaActual;
                }
            }

            // Cerrar último rango
            nuevosRangos.push({ checkin: inicio, checkout: fin });

            return {
                ...h,
                listaestadias: [...(h.listaestadias || []), ...nuevosRangos]
            };
            })
        );

        // Limpiar selección
        setSeleccionados([]);
        setMostrarCartelLista(false);

        setMostrarCartelPresioneTecla(true);

        };


   
              const confirmarOcupacionEstadia = async () => {
          // Primero generamos la nueva lista ACTUALIZADA
          const nuevasHabitaciones = habitaciones.map(h => {
              const fechasDeEsta = seleccionados
                  .filter(s => s.split("|")[1] === String(h.numero))
                  .map(s => s.split("|")[0])
                  .sort();

        if (fechasDeEsta.length === 0) return h;

        const nuevosRangos: { checkin: string; checkout: string }[] = [];

        let inicio = fechasDeEsta[0];
        let fin = fechasDeEsta[0];

        for (let i = 1; i < fechasDeEsta.length; i++) {
            const fechaActual = fechasDeEsta[i];
            const fechaAnterior = fechasDeEsta[i - 1];

            const dActual = parseISO(fechaActual);
            const dAnterior = parseISO(fechaAnterior);
            const diferenciaDias =
                (dActual.getTime() - dAnterior.getTime()) / (1000 * 60 * 60 * 24);

            if (diferenciaDias === 1) {
                fin = fechaActual;
            } else {
                nuevosRangos.push({ checkin: inicio, checkout: fin });
                inicio = fechaActual;
                fin = fechaActual;
            }
        }

        nuevosRangos.push({ checkin: inicio, checkout: fin });

        return {
            ...h,
            listaestadias: [...(h.listaestadias || []), ...nuevosRangos]
        };
    });

    // Ahora sí actualizamos el estado
    setHabitaciones(nuevasHabitaciones);

    // 📨 ENVIAR AL BACK DESDE nuevasHabitaciones (NO desde habitaciones)
    try {
        for (const h of nuevasHabitaciones) {
            const fechasDeEsta = seleccionados.filter(s => s.split("|")[1] === String(h.numero));
            if (fechasDeEsta.length === 0) continue;

                    const fechasOrdenadas = fechasDeEsta.map(s => s.split("|")[0]).sort();

                    const rangos: { checkin: string; checkout: string }[] = [];
                    let ini = fechasOrdenadas[0];
                    let fin = fechasOrdenadas[0];

                    for (let i = 1; i < fechasOrdenadas.length; i++) {
                        const actual = fechasOrdenadas[i];
                        const anterior = fechasOrdenadas[i - 1];

                        const dA = parseISO(actual);
                        const dB = parseISO(anterior);
                        const dif =
                            (dA.getTime() - dB.getTime()) / (1000 * 60 * 60 * 24);

                        if (dif === 1) {
                            fin = actual;
                        } else {
                            rangos.push({ checkin: ini, checkout: fin });
                            ini = actual;
                            fin = actual;
                        }
                    }

                    rangos.push({ checkin: ini, checkout: fin });

                    // Enviar cada rango
                    for (const r of rangos) {
                        const estadiaDTO = {
                            checkin: r.checkin,
                            checkout: r.checkout,
                            habitacion: h   // ← OBJETO COMPLETO, NO STRING
                        };

                        await fetch("http://localhost:8080/estadias", {
                            method: "POST",
                            headers: { "Content-Type": "application/json" },
                            body: JSON.stringify(estadiaDTO),
                        });
                    }
                }
            } catch (error) {
                console.error("❌ Error enviando estadías:", error);
            }

            // Limpiar selección y carteles
            setSeleccionados([]);
            setMostrarCartelLista(false);
            setMostrarCartelPresioneTecla(true);
        };





  // =========================
  // LÓGICA BOTÓN ACEPTAR
  // =========================
  const handleAceptar = () => {
    const conflictos: {
        numero: number;
        desde: string;
        hasta: string;
        nombre: string;
        apellido: string;
        fecha: string;
    }[] = [];

    for (const sel of seleccionados) {
        const [fechaStr, habStr] = sel.split("|");
        const numero = Number(habStr);

        const habitacion = habitaciones.find(h => h.numero === numero);
        if (!habitacion || !habitacion.listareservas) continue;

        const reservaSolapada = habitacion.listareservas.find(r =>
        fechaEnRango(fechaStr, r.fecha_desde, r.fecha_hasta)
        );

        if (reservaSolapada) {
        conflictos.push({
            numero,
            desde: reservaSolapada.fecha_desde,
            hasta: reservaSolapada.fecha_hasta,
            nombre: reservaSolapada.nombre,
            apellido: reservaSolapada.apellido,
            fecha: fechaStr
        });
        }
    }

    // Si hay uno o varios conflictos → armar mensaje múltiple
    if (conflictos.length > 0) {
        let msg = "Conflictos encontrados:\n\n";

        conflictos.forEach(c => {
        msg +=
            `Habitación ${c.numero}\n` +
            `Fecha seleccionada: ${ format(parseISO(c.fecha), "dd/MM/yyyy") }\n` +
            `Reservada desde: ${ format(parseISO(c.desde), "dd/MM/yyyy") }\n` +
            `Reservada Hasta: ${ format(parseISO(c.hasta), "dd/MM/yyyy") }\n` +
            `A nombre de: ${c.nombre} ${c.apellido}\n\n`;
        });

        setMensajeReserva(msg);
        setMostrarCartelReservada(true);
        return;
    }

    // Si no hay conflictos
    setMostrarCartelLista(true);
    };



  // =========================
  // RENDER
  // =========================
  return (
    <main className="flex gap-8 px-6 py-6 items-start">

      {/* =========================
          CARTELES
      ========================== */}
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
            setMostrarCartelLista(true); // Abre lista después de "Ocupar igual"
            }}
        />
        )}

        {mostrarCartelLista && (
          <CartelHabitacionesOcupadas
            lista={seleccionados}
            desdeFecha={desdeFecha}
            hastaFecha={hastaFecha}
            onClose={() => setMostrarCartelLista(false)}
            onConfirm={confirmarOcupacionEstadia}   // 👈 ACÁ SE EJECUTA TODO
          />
        )}


        {mostrarCartelPresioneTecla && (
        <CartelPresioneTecla onClose={() => setMostrarCartelPresioneTecla(false)} />
        )}




      {/* FORMULARIO LATERAL */}
      <form className="flex flex-col justify-center">
        <label className="text-indigo-950 font-medium mb-1">Desde Fecha:</label>
        <input
          type="date"
          value={desdeFecha}
          onChange={(e) => setDesdeFecha(e.target.value)}
          onBlur={(e) => validarDesde(e.target.value)}
          className={`p-2 border rounded mb-1 text-indigo-950
            ${(erroresFecha.desdeInvalido || erroresFecha.ordenInvalido || (!desdeFecha && tipoSeleccionado)) 
              ? "border-red-500 bg-red-100" 
              : ""}`}
        />
        {erroresFecha.desdeInvalido && <span className="text-red-600 text-sm mb-2 block">La fecha no puede ser menor a hoy</span>}
        {erroresFecha.ordenInvalido && <span className="text-red-600 text-sm mb-2 block">La fecha "Desde" no puede ser posterior a "Hasta"</span>}

        <label className="text-indigo-950 font-medium mb-1">Hasta Fecha:</label>
        <input
          type="date"
          value={hastaFecha}
          onChange={(e) => setHastaFecha(e.target.value)}
          onBlur={(e) => validarHasta(e.target.value)}
          className={`p-2 border rounded mb-1 text-indigo-950
            ${(erroresFecha.hastaInvalido || erroresFecha.ordenInvalido || (!hastaFecha && tipoSeleccionado)) 
              ? "border-red-500 bg-red-100" 
              : ""}`}
        />
        {erroresFecha.hastaInvalido && <span className="text-red-600 text-sm mb-2 block">La fecha no puede ser menor a hoy</span>}
        {erroresFecha.ordenInvalido && <span className="text-red-600 text-sm mb-2 block">La fecha "Hasta" no puede ser anterior a "Desde"</span>}

        <label className="text-indigo-950 font-medium mb-1">Tipo de Habitación:</label>
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
          className={`p-2 border rounded mb-4 text-indigo-950 ${!fechasValidas ? "bg-gray-200 cursor-not-allowed" : ""}`}
        >
          <option value="" disabled>Seleccionar tipo</option>
          <option value="IndividualEstandar">Individual Estandar</option>
          <option value="DobleEstandar">Doble Estandar</option>
          <option value="DobleSuperior">Doble Superior</option>
          <option value="SuperiorFamilyPlan">Superior Family Plan</option>
          <option value="Suite">Suite</option>
        </select>
      </form>

      {/* TABLA Y CONTENIDO DERECHO */}
      <section className="flex-2 max-h-[800px]">
        {tipoSeleccionado && fechasIntervalo.length > 0 ? (
          <>
            <table className="w-full border-collapse border shadow-lg">
              <thead className="bg-indigo-950 text-white sticky top-0 z-10">
                <tr>
                  <th className="p-2">Fecha</th>
                  <th colSpan={habitaciones.length} className="p-2">Habitaciones</th>
                </tr>
                {/* Fila de números de habitación */}
                <tr className="text-indigo-950 bg-white">
                   <td className="p-2 border"></td>
                   {habitaciones.map(h => (
                     <td key={h.numero} className="p-2 border text-center">{h.numero}</td>
                   ))}
                </tr>
              </thead>

              <tbody>
                {fechasIntervalo.map(fechaDate => {
                  const fechaString = format(fechaDate, "yyyy-MM-dd");
                  const fechaDisplay = format(fechaDate, "dd/MM/yyyy");

                  return (
                    <tr key={fechaString}>
                      <td className="p-2 border text-center font-medium bg-white">
                        {fechaDisplay}
                      </td>

                      {habitaciones.map(h => {
                        const esFueraServicio = h.estado === "FueraDeServicio";
                        const esOcupada = h.listaestadias?.some(e => fechaEnRango(fechaString, e.checkin, e.checkout));
                        const esReservada = h.listareservas?.some(r => fechaEnRango(fechaString, r.fecha_desde, r.fecha_hasta));
                        
                        const key = `${fechaString}|${h.numero}`;
                        const esSeleccionada = seleccionados.includes(key);

                        let bg = "bg-white";
                        if (esFueraServicio) bg = "bg-gray-700";
                        else if (esOcupada) bg = "bg-blue-900";
                        else if (esSeleccionada) bg = "bg-green-500"; 
                        else if (esReservada) bg = "bg-red-500";

                        return (
                          <td
                            key={h.numero}
                            className={`p-4 border cursor-pointer ${bg}`}
                            onClick={() => handleClickCelda(fechaDate, h)}
                          ></td>
                        );
                      })}
                    </tr>
                  );
                })}
              </tbody>
            </table>

            {/* LEYENDA */}
            <ul className="flex items-center gap-2 mt-4 flex-wrap justify-center list-none p-0">
               <li className="flex items-center gap-2 mt-4 flex-wrap justify-center">
                 <span className="w-4 h-4 rounded-full bg-red-500"></span><span>RESERVADA</span>
                 <span className="w-4 h-4 rounded-full bg-white border"></span><span>DISPONIBLE</span>
                 <span className="w-4 h-4 rounded-full bg-gray-700"></span><span>FUERA DE SERVICIO</span>
                 <span className="w-4 h-4 rounded-full bg-blue-900"></span><span>OCUPADA</span>
                 <span className="w-4 h-4 rounded-full bg-green-500"></span><span>SELECCIONADA</span>
               </li>
            </ul>

            {/* BOTONES DE ACCIÓN */}
            <div className="flex justify-center gap-4 mt-6">
                <Link href="/menu"> 
                <button className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800">
                  Cancelar
                </button>
                </Link>
                

                <button
                  className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800"
                  onClick={handleAceptar}
                  disabled={seleccionados.length === 0}
                >
                  Aceptar
                </button>
            </div>
          </>
        ) : (
            <div className="text-gray-400 text-center mt-20">
                Seleccione un rango de fechas y tipo para ver la disponibilidad.
            </div>
        )}
      </section>
    </main>
  );
}
'use client';

import { useState, useEffect } from "react";
import { format, parseISO, eachDayOfInterval, isAfter } from "date-fns";
import { usePathname } from "next/navigation";
import OcuparHabitacionIgualmente from "../carteles/ocuparHabitacionIgualmente";
import CartelHabitacionNoDisponible from "../carteles/CartelHabitacionNoDisponible";
import axios from "axios";

// Tipos
type TipoHabitacion = "IndividualEstandar" | "DobleEstandar" | "Suite" | "DobleSuperior" | "SuperiorFamilyPlan";

interface ReservaDTO {
  id: number;
  numeroHabitacion: number;
  fechaDesde: string;
  fechaHasta: string;
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
  const [erroresFecha, setErroresFecha] = useState({
    desdeInvalido: false, hastaInvalido: false, ordenInvalido: false
  });
  const [fechasValidas, setFechasValidas] = useState(false);
  const [reservas, setReservas] = useState<ReservaDTO[]>([]);
  const [seleccionados, setSeleccionados] = useState<string[]>([]);
  const [mostrarCartelOH, setMostrarCartelOH] = useState(false);
  const [mostrarCartel, setMostrarCartel] = useState(false);

  const pathname = usePathname();

  // =========================
  // CONFIGURACIÓN HABITACIONES
  // =========================
  const habitacionesPorTipo: Record<TipoHabitacion, number[]> = {
    IndividualEstandar: [1,2,3,4,5,6,7,8,9,10],
    DobleEstandar: [11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28],
    DobleSuperior: [29,30,31,32,33,34,35,36],
    SuperiorFamilyPlan: [37,38,39,40,41,42,43,44,45,46],
    Suite: [47,48],
  };

  // =========================
  // VALIDAR FECHAS
  // =========================
  function validarFechas(desde: string, hasta: string) {
    const hoy = new Date();
    hoy.setHours(0,0,0,0);

    const d = desde ? parseISO(desde) : null;
    const h = hasta ? parseISO(hasta) : null;

    const nuevosErrores = { desdeInvalido: false, hastaInvalido: false, ordenInvalido: false };

    if (d && d < hoy) nuevosErrores.desdeInvalido = true;
    if (h && h < hoy) nuevosErrores.hastaInvalido = true;
    if (d && h && isAfter(d, h)) nuevosErrores.ordenInvalido = true;

    setErroresFecha(nuevosErrores);

    const valido =
      !!desde &&
      !!hasta &&
      !nuevosErrores.desdeInvalido &&
      !nuevosErrores.hastaInvalido &&
      !nuevosErrores.ordenInvalido;

    setFechasValidas(valido);

    if (!valido) { setReservas([]); setSeleccionados([]); }

    return valido;
  }

  // =========================
  // FETCH RESERVAS
  // =========================
  useEffect(() => {
    const fetchReservas = async () => {
      if (fechasValidas && tipoSeleccionado) {
        try {
          const response = await axios.get("http://localhost:8080/reservas", {
            params: { fechaDesde: desdeFecha, fechaHasta: hastaFecha },
          });
          setReservas(response.data);
        } catch (err) {
          console.error("Error al cargar reservas:", err);
        }
      }
    };
    fetchReservas();
  }, [fechasValidas, tipoSeleccionado, desdeFecha, hastaFecha]);

  // =========================
  // TABLA Y SELECCIÓN
  // =========================
  const fechasIntervalo =
    desdeFecha && hastaFecha && fechasValidas
      ? eachDayOfInterval({ start: parseISO(desdeFecha), end: parseISO(hastaFecha) })
      : [];

  const estaReservada = (fechaDate: Date, numeroHab: number) =>
    reservas.some(r => {
      const fechaFilaString = format(fechaDate, "yyyy-MM-dd");
      return (
        r.numeroHabitacion === numeroHab &&
        fechaFilaString >= r.fechaDesde &&
        fechaFilaString <= r.fechaHasta
      );
    });

  const toggleSeleccion = (fechaDate: Date, numeroHab: number) => {
    const fechaString = format(fechaDate, "yyyy-MM-dd");
    const key = `${fechaString}|${numeroHab}`;

    if (estaReservada(fechaDate, numeroHab)) {
      setMostrarCartel(true);
      setTimeout(() => setMostrarCartel(false), 3000);





      return;
    }

// =========================
    // TABLA "HABITACIONES DISPONIBLES"
    // =========================
    const toggleCheck = (id: number) => {
        setFilas(prev =>
            prev.map(f => f.id === id ? { ...f, checked: !f.checked } : f)
        );
    };

    const eliminarSeleccionados = () => {
        setFilas(prev => prev.filter(f => !f.checked));
    };




    setSeleccionados((prev) =>
      prev.includes(key) ? prev.filter((item) => item !== key) : [...prev, key]
    );
  };

  const eliminarSeleccionados = () => setSeleccionados([]);

  // =========================
  // AGRUPAR SELECCIONES EN RANGOS
  // =========================
  function agruparPorRangos(seleccionados: string[]) {
    const items = seleccionados
      .map(sel => {
        const [fecha, num] = sel.split("|");
        return { fecha, hab: Number(num) };
      })
      .sort((a, b) => a.hab - b.hab || a.fecha.localeCompare(b.fecha));

    const grupos: { hab: number; desde: string; hasta: string }[] = [];

    items.forEach(item => {
      const ultimo = grupos[grupos.length - 1];

      if (!ultimo || ultimo.hab !== item.hab) {
        grupos.push({ hab: item.hab, desde: item.fecha, hasta: item.fecha });
      } else {
        const siguienteEsperada = format(
          new Date(ultimo.hasta + "T00:00:00").getTime() + 24 * 60 * 60 * 1000,
          "yyyy-MM-dd"
        );

        if (item.fecha === siguienteEsperada) {
          ultimo.hasta = item.fecha;
        } else {
          grupos.push({ hab: item.hab, desde: item.fecha, hasta: item.fecha });
        }
      }
    });

    return grupos;
  }

  // =========================
  // RENDER
  // =========================
  return (
    <main className="flex gap-8 px-6 py-6 items-start">

      {/* FORMULARIO */}
      <form className="flex flex-col justify-center">
        <label className="text-indigo-950 font-medium mb-1">Desde Fecha:</label>
        <input
          type="date"
          value={desdeFecha}
          onChange={(e) => {
            setDesdeFecha(e.target.value);
            validarFechas(e.target.value, hastaFecha);
          }}
          className="p-2 border rounded mb-1 text-indigo-950"
        />

        <label className="text-indigo-950 font-medium mb-1">Hasta Fecha:</label>
        <input
          type="date"
          value={hastaFecha}
          onChange={(e) => {
            setHastaFecha(e.target.value);
            validarFechas(desdeFecha, e.target.value);
          }}
          className="p-2 border rounded mb-4 text-indigo-950"
        />

        <label className="text-indigo-950 font-medium mb-1">Tipo de Habitación:</label>
        <select
          value={tipoSeleccionado}
          onChange={(e) => setTipoSeleccionado(e.target.value as TipoHabitacion)}
          className="p-2 border rounded mb-4"
        >
          <option value="" disabled>Seleccionar tipo</option>
          <option value="IndividualEstandar">Individual Estandar</option>
          <option value="DobleEstandar">Doble Estandar</option>
          <option value="DobleSuperior">Doble Superior</option>
          <option value="SuperiorFamilyPlan">Superior Family Plan</option>
          <option value="Suite">Suite</option>
        </select>
      </form>

      {/* CARTEL HABITACIÓN NO DISPONIBLE */}
      {mostrarCartel && (
        <CartelHabitacionNoDisponible
          mensaje="La habitación seleccionada no se encuentra disponible"
          onClose={() => setMostrarCartel(false)}
        />
      )}

      {/* TABLA CENTRAL */}
      <section className="flex-2 max-h-[800px]">
        {tipoSeleccionado && fechasIntervalo.length > 0 && (
          <>
            <table className="w-full border-collapse border shadow-lg">
              <thead className="bg-indigo-950 text-white sticky top-0 z-10">
                <tr>
                  <th className="p-2">Fecha</th>
                  <th className="p-2" colSpan={habitacionesPorTipo[tipoSeleccionado].length}>
                    Habitaciones
                  </th>
                </tr>
              </thead>

              <tbody>
                <tr>
                  <td className="p-2 border"></td>
                  {habitacionesPorTipo[tipoSeleccionado].map(num => (
                    <td key={num} className="p-2 border text-center">{num}</td>
                  ))}
                </tr>

                {fechasIntervalo.map((fechaDate) => {
                  const fecha = format(fechaDate, "dd/MM/yyyy");
                  return (
                    <tr key={fecha}>
                      <td className="p-2 border text-center">{fecha}</td>

                      {habitacionesPorTipo[tipoSeleccionado].map((num) => {
                        const key = `${format(fechaDate, "yyyy-MM-dd")}|${num}`;
                        const seleccionado = seleccionados.includes(key);
                        const reservada = estaReservada(fechaDate, num);

                        return (
                          <td
                            key={num}
                            className={`p-4 border cursor-pointer
                              ${reservada ? "bg-red-500" : seleccionado ? "bg-green-500" : "bg-white"}
                            `}
                            onClick={() => toggleSeleccion(fechaDate, num)}
                          ></td>
                        );
                      })}
                    </tr>
                  );
                })}
              </tbody>
            </table>

            {/* LEYENDA */}
            <li className="flex items-center gap-2 mt-4 flex-wrap justify-center">
              <span className="w-4 h-4 rounded-full bg-red-500"></span>RESERVADA
              <span className="w-4 h-4 rounded-full bg-white border"></span>DISPONIBLE
              <span className="w-4 h-4 rounded-full bg-gray-700"></span>FUERA DE SERVICIO
              <span className="w-4 h-4 rounded-full bg-blue-900"></span>OCUPADA
              <span className="w-4 h-4 rounded-full bg-green-500"></span>SELECCIONADA
            </li>

            <button
              className="block mx-auto mt-6 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800"
              onClick={() => pathname === "/ocuparHabitacion" && setMostrarCartelOH(true)}
            >
              Aceptar
            </button>

            {mostrarCartelOH && <OcuparHabitacionIgualmente onClose={() => setMostrarCartelOH(false)} />}
          </>
        )}
      </section>

      {/* TABLA DERECHA - AGRUPADA */}
      {!ocultarTabla && seleccionados.length > 0 && (
        <section className="flex-1">
          <h2 className="bg-indigo-950 text-white font-bold text-center mb-0">
            Habitaciones Seleccionadas:
          </h2>

          <table className="w-full border-collapse border shadow-lg">
            <thead className="bg-indigo-950 text-white">
              <tr>
                <th className="p-3 border">Eliminar</th>
                <th className="p-3 border">N° Habitación</th>
                <th className="p-3 border">Fecha Desde</th>
                <th className="p-3 border">Fecha Hasta</th>
              </tr>
            </thead>

            <tbody>
              {agruparPorRangos(seleccionados).map(grupo => (


                <tr key={`${grupo.hab}-${grupo.desde}`} className="bg-white hover:bg-indigo-100">

                    <td className="p-3 border text-center">  <input type="checkbox" name="seleccion"  /> </td>



                  <td className="p-3 border text-center">{grupo.hab}</td>
                  <td className="p-3 border text-center">
                    {format(parseISO(grupo.desde), "dd/MM/yyyy")}
                  </td>
                  <td className="p-3 border text-center">
                    {format(parseISO(grupo.hasta), "dd/MM/yyyy")}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <button
            onClick={eliminarTodos}
            className="block mx-auto mt-6 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800"
          >
            Eliminar Todas
          </button>

          <button
             onClick={eliminarSeleccionados}
             className="block mx-auto mt-6 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800"
          >
              Eliminar
          </button>

        </section>
      )}

    </main>
  );
}

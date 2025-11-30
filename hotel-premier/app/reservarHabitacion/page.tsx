'use client';

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
//erne
import CartelListaHabitaciones from "../carteles/cartelListaHabitaciones";
import Link from "next/link";

// Tipos
type TipoHabitacion = "IndividualEstandar" | "DobleEstandar" | "Suite" | "DobleSuperior" | "SuperiorFamilyPlan";

interface ReservaDTO {
  id: number;
  nro_habitacion: number;
  fecha_desde: string;
  fecha_hasta: string;
}

interface HabitacionDTO {
  numero:number;
  estado:string;
  precio:number;
  cantidadPersonas: number;
  tipoHab: TipoHabitacion;
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
  const [reservas, setReservas] = useState<ReservaDTO[]>([]);
  const [seleccionados, setSeleccionados] = useState<string[]>([]);
  const [mostrarCartelOH, setMostrarCartelOH] = useState(false);
  const [mostrarCartel, setMostrarCartel] = useState(false);
  const [habitaciones, setHabitaciones] = useState<HabitacionDTO[] | []>([]);

  //agreggo erne
  const [mostrarCartelLista, setMostrarCartelLista] = useState(false);
  const [listaBackend, setListaBackend] = useState<any[]>([]);
  const [paso, setPaso] = useState<1 | 2>(1);
  const router = useRouter();



  const pathname = usePathname();

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
  function validarFechas(desde: string, hasta: string) {
    const hoy = new Date();
    hoy.setHours(0,0,0,0);

    const d = desde ? parseISO(desde) : null;
    const h = hasta ? parseISO(hasta) : null;

    const nuevosErrores = { desdeInvalido: false, hastaInvalido: false, ordenInvalido: false };

    if (d && d < hoy) nuevosErrores.desdeInvalido = true;
    if (h && h < hoy) nuevosErrores.hastaInvalido = true;
    if (d && h && isAfter(d,h)) nuevosErrores.ordenInvalido = true;

    setErroresFecha(nuevosErrores);

    const valido = !!desde && !!hasta && !nuevosErrores.desdeInvalido && !nuevosErrores.hastaInvalido && !nuevosErrores.ordenInvalido;
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
          console.log(desdeFecha);
          console.log(hastaFecha);
          const response = await axios.get(`http://localhost:8080/reservas`, {
            params: { fechaDesde: desdeFecha, fechaHasta: hastaFecha },
          });
          setReservas(response.data);
          console.log(response.data);
        } catch(err) {
          console.error("Error al cargar reservas:", err);
        }
      }
    };

    const fetchHabitaciones = async () =>{
      try{
        const res = await axios.get(`http://localhost:8080/habitaciones`, {
            params: { tipo:tipoSeleccionado },
          });
          setHabitaciones(res.data);
          console.log("habitacion: " + setHabitaciones);

      }catch(err) {
          console.error("Error al cargar reservas:", err);
        }
    }
    fetchReservas();
    fetchHabitaciones();
  }, [fechasValidas, tipoSeleccionado, desdeFecha, hastaFecha]);

  // =========================
  // TABLA Y SELECCIÓN
  // =========================
  const fechasIntervalo = (desdeFecha && hastaFecha && fechasValidas)
    ? eachDayOfInterval({ start: parseISO(desdeFecha), end: parseISO(hastaFecha) })
    : [];

  const estaReservada = (fechaDate: Date, numeroHab: number) =>
    reservas.some(r => {
      const fechaFilaString = format(fechaDate,"yyyy-MM-dd");
      console.log("fecha fila string: " + fechaFilaString);
      console.log("fecha Desde: " + r.fecha_desde);
      console.log("fecha Hasta: " + r.fecha_hasta);
      console.log("numero habitacion r:" + r.nro_habitacion);
      console.log("numero habitacion: " + numeroHab);
      return r.nro_habitacion===numeroHab && fechaFilaString >= r.fecha_desde && fechaFilaString <= r.fecha_hasta;
    });

  const toggleSeleccion = (fechaDate: Date, numeroHab: number) => {
    const fechaString = format(fechaDate,"yyyy-MM-dd");
    const key = `${fechaString}|${numeroHab}`;

    if (estaReservada(fechaDate, numeroHab)) {
      setMostrarCartel(true);
      setTimeout(() => setMostrarCartel(false), 3000);
      return;
    }

    setSeleccionados(prev =>
      prev.includes(key) ? prev.filter(item => item!==key) : [...prev, key]
    );
  };

  const eliminarSeleccionados = () => setSeleccionados([]);

  const deleteSeleccionado = (sel: string) => {
    const nuevosSeleccionados = seleccionados.filter((item) => item !== sel);
    setSeleccionados(nuevosSeleccionados);
  };

  //erne
    // Construye lista de HabitacionDTO a partir de `seleccionados`
    const construirHabitacionesDTO = (): HabitacionDTO[] => {
      const lista: HabitacionDTO[] = seleccionados.map(sel => {
        const [fecha, numero] = sel.split("|");
        const habitacion = (Array.isArray(habitaciones) ? habitaciones : []).find(h => h.numero === Number(numero));

        return {
          numero: Number(numero),
          estado: habitacion?.estado || "DISPONIBLE",
          precio: habitacion?.precio ?? 0,
          cantidadPersonas: habitacion?.cantidadPersonas ?? 1,
          tipoHab: habitacion?.tipoHab ?? (tipoSeleccionado as TipoHabitacion)
        };
      });

      console.log("LISTA DTO A ENVIAR:", lista);
      return lista;
    };

    // Envía la lista al backend (query params con fechas + body con HabitacionDTO[])
    const enviarReservasAlBack = async () => {
      try {
        if (!desdeFecha || !hastaFecha) {
          alert("Debe seleccionar Fecha Desde y Fecha Hasta antes de confirmar.");
          return;
        }

        if (seleccionados.length === 0) {
          alert("No seleccionaste ninguna habitación.");
          return;
        }

        const listaDTO = construirHabitacionesDTO();

        const res = await axios.post(
          `http://localhost:8080/listados?fechaDesde=${desdeFecha}&fechaHasta=${hastaFecha}`,
          listaDTO
        );

        // guardo respuesta del backend para mostrar en cartel
        setListaBackend(res.data || []);
        setMostrarCartelLista(true);

      } catch (err) {
        console.error("Error enviando reservas:", err);
        alert("Error enviando reservas. Ver consola.");
      }
    };


  // =========================
  // RENDER
  // =========================
  return (
    <main className="flex gap-8 px-6 py-6 items-start">

      {/* FORMULARIO DE FECHAS Y TIPO */}
      <form className="flex flex-col justify-center">
        <label className="text-indigo-950 font-medium mb-1">Desde Fecha:</label>
        <input
          type="date"
          value={desdeFecha}
          onChange={(e) => { setDesdeFecha(e.target.value); validarFechas(e.target.value, hastaFecha); }}
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
          onChange={(e) => { setHastaFecha(e.target.value); validarFechas(desdeFecha, e.target.value); }}
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
            // marcar campos vacíos en rojo si el usuario toca el select
            setErroresFecha(prev => ({
              desdeInvalido: prev.desdeInvalido || !desdeFecha,
              hastaInvalido: prev.hastaInvalido || !hastaFecha,
              ordenInvalido: prev.ordenInvalido
            }));
          }}
          className={`p-2 border rounded mb-4 ${!fechasValidas ? "bg-gray-200 cursor-not-allowed text-gray-400" : "text-indigo-950"}`}
        >
          <option value="" disabled>Seleccionar tipo</option>
          <option value={TipoHabitacion.IndividualEstandar}>Individual Estandar</option>
          <option value={TipoHabitacion.DobleEstandar}>Doble Estandar</option>
          <option value={TipoHabitacion.DobleSuperior}>Doble Superior</option>
          <option value={TipoHabitacion.SuperiorFamilyPlan}>Superior Family Plan</option>
          <option value={TipoHabitacion.Suite}>Suite</option>
        </select>
      </form>

      {/* CARTEL DE HABITACIÓN NO DISPONIBLE */}
      {mostrarCartel && (
        <CartelHabitacionNoDisponible
          mensaje="La habitación seleccionada no se encuentra disponible"
          onClose={() => setMostrarCartel(false)}
        />
      )}
      {/* Cartel con la lista devuelta por el backend */}

         {mostrarCartelLista && (  //erne
           <CartelListaHabitaciones
             lista={listaBackend}
             seleccionados={seleccionados}
             desdeFecha={desdeFecha}
             hastaFecha={hastaFecha}
             onClose={() => setMostrarCartelLista(false)}
           />

       )}


      {/* TABLA ESTADO HABITACIÓN */}
      <section className="flex-2 max-h-[800px]">
        {tipoSeleccionado && fechasIntervalo.length > 0 && (
          <>
            <table className="w-full border-collapse border shadow-lg">
              <thead className="bg-indigo-950 text-white sticky top-0 z-10">
                <tr>
                  <th className="p-2">Fecha</th>
                  {tipoSeleccionado && (
                    <th colSpan={habitaciones.length} className="p-2">Habitaciones</th>
                  )}
                </tr>
              </thead>

              <tbody>
                <tr>
                  <td className="p-2 border"></td>
                  {tipoSeleccionado && habitaciones.map(hab => (
                    <td key={hab?.numero} className="p-2 border text-center">{hab?.numero}</td>
                  ))}
                </tr>

                {fechasIntervalo.map(fechaDate => {
                  const fecha = format(fechaDate,"dd/MM/yyyy");
                  return (
                    <tr key={fecha}>
                      <td className="p-2 border text-center font-medium">{fecha}</td>
                      {tipoSeleccionado && habitaciones.map(hab => {
                        const key = `${format(fechaDate,"yyyy-MM-dd")}|${hab?.numero}`;
                        const seleccionado = seleccionados.includes(key);
                        const reservada = estaReservada(fechaDate,hab?.numero);
                        console.log("reservada:" + reservada)
                        console.log(reservas)

                        return (
                          <td
                            key={hab.numero}
                            className={`p-4 border cursor-pointer
                              ${reservada ? "bg-red-500" : seleccionado ? "bg-green-500" : "bg-white"}
                            `}
                            onClick={() => toggleSeleccion(fechaDate,hab?.numero)}
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
              <span className="w-4 h-4 rounded-full bg-red-500"></span><span>RESERVADA</span>
              <span className="w-4 h-4 rounded-full bg-white border"></span><span>DISPONIBLE</span>
              <span className="w-4 h-4 rounded-full bg-gray-700"></span><span>FUERA DE SERVICIO</span>
              <span className="w-4 h-4 rounded-full bg-blue-900"></span><span>OCUPADA</span>
              <span className="w-4 h-4 rounded-full bg-green-500"></span><span>SELECCIONADA</span>
            </li>
        
            <div className="flex justify-center gap-4 mt-6">
            <Link href="/menu">
              <button className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800">
                Cancelar
              </button>
            </Link>

            <button
              className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800"
             onClick={() => {
               const seleccionEncoded = encodeURIComponent(JSON.stringify(seleccionados));
                 const listaDTO = construirHabitacionesDTO();
                   setListaBackend(listaDTO);
                   setMostrarCartelLista(true);  //  Muestra cartel antes del push
             }}
             > Aceptar
            </button>
          </div>


            {mostrarCartelOH && <OcuparHabitacionIgualmente onClose={() => setMostrarCartelOH(false)} />}
          </>
        )}
      </section>

      {/* TABLA DERECHA OPCIONAL */}
      {!ocultarTabla && seleccionados.length > 0 && (
        <section className="flex-1">
          <h2 className="bg-indigo-950 text-white font-bold text-center mb-0">
            Habitaciones Seleccionadas:
          </h2>
          <table className="w-full border-collapse border shadow-lg">
            <thead className="bg-indigo-950 text-white">
              <tr>
                <th className="p-3 border">Eliminar</th>
                <th className="p-3 border">Habitación</th>
                <th className="p-3 border">Fecha</th>
              </tr>
            </thead>
            <tbody>
              {seleccionados.map(sel => {
                const [fecha,num] = sel.split("|");
                const fechaMostrar = format(parseISO(fecha),"dd/MM/yyyy");
                return (
                  <tr key={sel} className="bg-white hover:bg-indigo-100">
                    <td className="p-3 border text-center"><button
                        onClick={() => deleteSeleccionado(sel)}   // <-- tu función
                        className=" text-white p-1 rounded hover:bg-red-700"
                    >
                        <FontAwesomeIcon icon={faTrashCan} className="text-black" />
                      </button>
                    </td>
                    <td className="p-3 border text-center">{num}</td>
                    <td className="p-3 border text-center">{fechaMostrar}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>

          <button
            onClick={eliminarSeleccionados}
            className="block mx-auto mt-6 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800"
          >
            Eliminar Todas
          </button>
        </section>
      )}
  {/* ============================ */}
  {/* PASO 2 — FORMULARIO DATOS   */}
  {/* ============================ */}

    </main>
  );
}
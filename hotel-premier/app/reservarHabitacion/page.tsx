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
import CartelListaHabitaciones from "../carteles/cartelListaHabitaciones";
import Link from "next/link";
import { validarFechasReserva } from "@/app/validaciones/validaciones";


// =========================
// TIPOS E INTERFACES (COINCIDEN CON TU JSON)
// =========================
type TipoHabitacion = "IndividualEstandar" | "DobleEstandar" | "Suite" | "DobleSuperior" | "SuperiorFamilyPlan";

interface ReservaDTO {
  id: number;
  nro_habitacion: number;
  fecha_desde: string;
  fecha_hasta: string;
  estado: string;
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
  listaReservas: ReservaDTO[]; 
  listaestadias: EstadiaDTO[];
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
  
  const [seleccionados, setSeleccionados] = useState<string[]>([]);
  const [mostrarCartelOH, setMostrarCartelOH] = useState(false);
  const [mostrarCartel, setMostrarCartel] = useState(false);
  
  const [habitaciones, setHabitaciones] = useState<HabitacionDTO[] | []>([]);
  const [rangos, setRangos] = useState<{ numero: number; desde: string; hasta: string }[]>([]);

  const [mostrarCartelLista, setMostrarCartelLista] = useState(false);
  const [listaBackend, setListaBackend] = useState<any[]>([]);
  const router = useRouter();

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
          setHabitaciones([]);
        }
      }
    };

    fetchDetalleHabitaciones();
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

  const toggleSeleccion = (fechaDate: Date, habitacion: HabitacionDTO) => {
    const fechaString = format(fechaDate,"yyyy-MM-dd");
    
    // 1. CHEQUEO FUERA DE SERVICIO (MODIFICADO)
    // Ahora muestra el cartel en lugar de hacer un return silencioso
    if (habitacion.estado === "FueraDeServicio") {
        setMostrarCartel(true); 
        setTimeout(() => setMostrarCartel(false), 3000);
        return;
    }

    // 2. CHEQUEO ESTADÍA (Ocupada)
    const tieneEstadia = habitacion.listaestadias?.some(e => 
        fechaEnRango(fechaString, e.checkIn, e.checkOut)
    );
    if(tieneEstadia) {
        setMostrarCartel(true); 
        setTimeout(() => setMostrarCartel(false), 3000);
        return;
    }

    // 3. CHEQUEO RESERVA
    const tieneReserva = habitacion.listaReservas?.some(r => 
        fechaEnRango(fechaString, r.fecha_desde, r.fecha_hasta)
    );

    if (tieneReserva) {
      setMostrarCartel(true);
      setTimeout(() => setMostrarCartel(false), 3000);
      return;
    }

    // Si está libre, permite seleccionar
    const key = `${fechaString}|${habitacion.numero}`;
    setSeleccionados(prev =>
      prev.includes(key) ? prev.filter(item => item!==key) : [...prev, key]
    );
  };

  const eliminarSeleccionados = () => setSeleccionados([]);

  const deleteSeleccionado = (ran:{ numero: number; desde: string; hasta: string }) => {
    const fechasABorrar = eachDayOfInterval({
      start: parseISO(ran.desde),
      end: parseISO(ran.hasta)
    }).map(f => `${format(f,"yyyy-MM-dd")}|${ran.numero}`);

    setSeleccionados(prev =>
      prev.filter(sel => !fechasABorrar.includes(sel))
    );
  };

    const construirHabitacionesDTO = (): HabitacionDTO[] => {
      const lista: HabitacionDTO[] = seleccionados.map(sel => {
        const [fecha, numero] = sel.split("|");
        const habitacion = (Array.isArray(habitaciones) ? habitaciones : []).find(h => h.numero === Number(numero));

        return {
          numero: Number(numero),
          estado: habitacion?.estado || "DISPONIBLE",
          precio: habitacion?.precio ?? 0,
          cantidadPersonas: habitacion?.cantidadPersonas ?? 1,
          tipohabitacion: habitacion?.tipohabitacion ?? (tipoSeleccionado as string),
          listaReservas: [], 
          listaestadias: []
        };
      });
      return lista;
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
                  const fechaString = format(fechaDate,"yyyy-MM-dd");
                  const fechaDisplay = format(fechaDate,"dd/MM/yyyy");

                  return (
                    <tr key={fechaString}>
                      <td className="p-2 border text-center font-medium">{fechaDisplay}</td>
                      {tipoSeleccionado && habitaciones.map(hab => {
                        
                        const key = `${fechaString}|${hab?.numero}`;
                        const esSeleccionado = seleccionados.includes(key);

                        // 1. CHEQUEO FUERA DE SERVICIO
                        const esFueraServicio = hab.estado === "FueraDeServicio";

                        // 2. CHEQUEO ESTADÍA (OCUPADA)
                        const esOcupada = !esFueraServicio && hab.listaestadias?.some(estadia => 
                            fechaEnRango(fechaString, estadia.checkIn, estadia.checkOut)
                        );

                        // 3. CHEQUEO RESERVA
                        const esReservada = !esFueraServicio && !esOcupada && hab.listaReservas?.some(reserva => 
                            fechaEnRango(fechaString, reserva.fecha_desde, reserva.fecha_hasta)
                        );
                        
                        let bgClass = "bg-white"; 
                        if (esFueraServicio) {
                            bgClass = "bg-gray-700"; 
                        } else if (esOcupada) {
                            bgClass = "bg-blue-900"; 
                        } else if (esReservada) {
                            bgClass = "bg-red-500";  
                        } else if (esSeleccionado) {
                            bgClass = "bg-green-500"; 
                        }

                        return (
                          <td
                            key={hab.numero}
                            className={`p-4 border cursor-pointer ${bgClass}`}
                            onClick={() => toggleSeleccion(fechaDate, hab)}
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
                 const listaDTO = construirHabitacionesDTO();
                 setListaBackend(listaDTO);
                 setMostrarCartelLista(true);
              }}
              > Aceptar
            </button>
          </div>

            {mostrarCartelOH && <OcuparHabitacionIgualmente onClose={() => setMostrarCartelOH(false)} />}
          </>
        )}
      </section>

      {/* TABLA DERECHA */}
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
                <th className="p-3 border">Fecha Desde</th>
                <th className="p-3 border">Fecha Hasta</th>
              </tr>
            </thead>
            <tbody>
              {rangos.map((ran,index) => {
                const fechaDesde = format(parseISO(ran.desde),"dd/MM/yyyy");
                const fechaHasta = format(parseISO(ran.hasta),"dd/MM/yyyy");
                return (
                  <tr key={index} className="bg-white hover:bg-indigo-100">
                    <td className="p-3 border text-center"><button
                        onClick={() => deleteSeleccionado(ran)} 
                        className=" text-white p-1 rounded hover:bg-red-700"
                    >
                        <FontAwesomeIcon icon={faTrashCan} className="text-black" />
                      </button>
                    </td>
                    <td className="p-3 border text-center">{ran.numero}</td>
                    <td className="p-3 border text-center">{fechaDesde}</td>
                    <td className="p-3 border text-center">{fechaHasta}</td>
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

    </main>
  );
}
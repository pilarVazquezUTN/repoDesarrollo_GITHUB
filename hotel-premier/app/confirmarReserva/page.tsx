"use client";
import { useState, useEffect } from "react";
import { useSearchParams } from "next/navigation";
import { useMemo } from "react";
import { parseISO, format } from "date-fns";
import { esSoloLetras, esSoloNumeros} from "../validaciones/validaciones";


interface HabitacionDTO {
  numero: number;
  estado: string; 
  precio: number;
  cantidadPersonas: number;
  tipohabitacion: string;
  listaReservas: ReservaDTO[]; 
  listaestadias: EstadiaDTO[];
}

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

export default function DatosHuesped() {

  const params = useSearchParams();
  const raw = params.get("seleccion") || "[]";

  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [telefono, setTelefono] = useState("");

  const [rangos, setRangos] = useState<{ numero: number; desde: string; hasta: string }[]>([]);


  // Estados para errores
  const [errorNombre, setErrorNombre] = useState(false);
  const [errorApellido, setErrorApellido] = useState(false);
  const [errorTelefono, setErrorTelefono] = useState(false);

  useEffect(() => {
    setErrorNombre(nombre !== "" && !esSoloLetras(nombre));
  }, [nombre]);

  useEffect(() => {
    setErrorApellido(apellido !== "" && !esSoloLetras(apellido));
  }, [apellido]);

  useEffect(() => {
    setErrorTelefono(telefono !== "" && !esSoloNumeros(telefono));
  }, [telefono]);

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    //cuando los datos son incorrectos
    if (errorNombre || errorApellido || errorTelefono) {
      //ya estan los useeffect que hacen que se pongan en rojo a penas se escriben mal
      return;
    }

    //CUANDO LOS DATOS SON CORRECTOS QUE TIENE QUE PASAR?
    //aqui iria la logica para confirmar la reserva, como llamar a una api o algo
  };


  const seleccion = useMemo(() => {
    try {
      return JSON.parse(raw);
    } catch {
      return [];
    }
  }, [raw]);
  
  
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
      const nuevosRangos = generarRangos(seleccion);
      setRangos(nuevosRangos);
    }, [seleccion]);
  
  

  return (
    <main className="p-10">

      <h2 className="text-2xl font-bold text-indigo-950 mb-6">
        Datos del Huésped
      </h2>

      {/* CONTENEDOR A DOS COLUMNAS */}
      <div className="flex gap-10">

        {/* IZQUIERDA - FORMULARIO */}
        <form onSubmit={handleSubmit} className="flex flex-col justify-center flex-1 max-w-md">
          
          
            <label className="text-indigo-950 font-medium mb-1">Nombre:</label>
            <input 
                type="text"
                placeholder="Nombre"
                value={nombre}
                onChange={(e) => setNombre(e.target.value.toUpperCase())}
                className={`p-2 border rounded mb-1 placeholder-gray-400 text-indigo-950 
                    ${errorNombre ? "border-red-500" : ""}`}
            />
            {errorNombre && (
                <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>
            )}

            
            <label className="text-indigo-950 font-medium mb-1">Apellido:</label>
            <input 
                type="text"
                placeholder="Apellido"
                value={apellido}
                onChange={(e) => setApellido(e.target.value.toUpperCase())}
                className={`p-2 border rounded mb-1 placeholder-gray-400 text-indigo-950 
                    ${errorApellido ? "border-red-500" : ""}`}
            />
            {errorApellido && (
                <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>
            )}

            <label className="text-indigo-950 font-medium mb-1">Teléfono:</label>
            <input 
                type="text"
                placeholder="Teléfono"
                value={telefono}
                onChange={(e) => setTelefono(e.target.value.toUpperCase())}
                className={`p-2 border rounded mb-1 placeholder-gray-400 text-indigo-950 
                    ${errorTelefono ? "border-red-500" : ""}`}
            />
            {errorTelefono && (
                <p className="text-red-500 text-sm mb-3">Ingrese solo números.</p>
            )}

         {/* BOTÓN CENTRADO */}
           <div className="w-full flex justify-center">
             <button
               className="mt-6 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800"
               type="submit"
             >
               Confirmar Reserva
             </button>
           </div>
        </form>

        {/* DERECHA - TABLA */}
        <div className="flex-1">
          <h3 className="text-xl font-bold mb-4 text-indigo-950">
            Habitaciones Seleccionadas
          </h3>

          <table className="w-full border-collapse border shadow-lg">
            <thead className="bg-indigo-950 text-white">
              <tr>
                <th className="p-3 border">Habitación</th>
                <th className="p-3 border">Fecha Desde</th>
                <th className="p-3 border">Fecha Hasta</th>
              </tr>
            </thead>

            <tbody>
              {rangos.map((ran,index) => {

                return (
                  <tr key={index}>
                    <td className="p-3 border text-center">{ran.numero}</td>
                    <td className="p-3 border text-center">
                      {format(parseISO(ran.desde), "dd/MM/yyyy")}
                    </td>
                    <td className="p-3 border text-center">
                      {format(parseISO(ran.hasta), "dd/MM/yyyy")}
                    </td>

                  </tr>
                );
              })}
            </tbody>
          </table>



        </div>

      </div>

    </main>
  );
}

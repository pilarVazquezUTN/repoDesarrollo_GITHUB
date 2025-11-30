"use client";

import { useSearchParams } from "next/navigation";
import { useMemo } from "react";
import { parseISO, format } from "date-fns";

export default function DatosHuesped() {

  const params = useSearchParams();
  const raw = params.get("seleccion") || "[]";

  const seleccion = useMemo(() => {
    try {
      return JSON.parse(raw);
    } catch {
      return [];
    }
  }, [raw]);

  return (
    <main className="p-10">

      <h2 className="text-2xl font-bold text-indigo-950 mb-6">
        Datos del Huésped
      </h2>

      {/* CONTENEDOR A DOS COLUMNAS */}
      <div className="flex gap-10">

        {/* IZQUIERDA - FORMULARIO */}
        <div className="w-[350px]">
          <div className="grid grid-cols-1 gap-4 mb-10">
            <input type="text" placeholder="Nombre" className="p-2 border rounded" />
            <input type="text" placeholder="Apellido" className="p-2 border rounded" />
            <input type="text" placeholder="Teléfono" className="p-2 border rounded" />
          </div>

         {/* BOTÓN CENTRADO */}
           <div className="w-full flex justify-center">
             <button
               className="mt-6 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800"
               onClick={() => {}}
             >
               Confirmar Reserva
             </button>
           </div>

        </div>

        {/* DERECHA - TABLA */}
        <div className="flex-1">
          <h3 className="text-xl font-bold mb-4 text-indigo-950">
            Habitaciones Seleccionadas
          </h3>

          <table className="w-full border-collapse border shadow-lg">
            <thead className="bg-indigo-950 text-white">
              <tr>
                <th className="p-3 border">Habitación</th>
                <th className="p-3 border">Fecha</th>
              </tr>
            </thead>

            <tbody>
              {seleccion.map(sel => {
                const [fecha, nro] = sel.split("|");

                return (
                  <tr key={sel}>
                    <td className="p-3 border text-center">{nro}</td>
                    <td className="p-3 border text-center">
                      {format(parseISO(fecha), "dd/MM/yyyy")}
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

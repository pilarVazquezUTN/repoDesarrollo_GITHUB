'use client';

import { useState } from "react";

export default function EstadoHabitacion() {
    type TipoHabitacion = "Individual" | "DobleEstandar" | "SuiteDoble" | "DobleSuperior" | "SuperiorFamilyPlan";

    const habitacionesPorTipo: Record<TipoHabitacion, number[]> = {
    Individual: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
    DobleEstandar: [11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28],
    DobleSuperior: [29, 30, 31, 32, 33, 34, 35, 36],
    SuperiorFamilyPlan: [37, 38, 39, 40, 41, 42, 43, 44, 45, 46],
    SuiteDoble: [47, 48],
  };

  const [tipoSeleccionado, setTipoSeleccionado] = useState<"" | TipoHabitacion>("");
  
 {/* esta constante despues se remplaza por la base de datos */}

    const [desdeFecha, setDesdeFecha] = useState("");
    const [hastaFecha, setHastaFecha] = useState("");

    function generarFechas(desde: string, hasta: string): string[] {
        const fechas: string[] = [];
        const inicio = new Date(desde);
        const fin = new Date(hasta);

        while (inicio <= fin) {
            const dia = inicio.getDate().toString().padStart(2, "0");
            const mes = (inicio.getMonth() + 1).toString().padStart(2, "0");
            const año = inicio.getFullYear();
            fechas.push(`${dia}/${mes}/${año}`);
            inicio.setDate(inicio.getDate() + 1);
        }

        return fechas;
    }
    const fechasMostradas = desdeFecha && hastaFecha ? generarFechas(desdeFecha, hastaFecha) : [];

    return (
        <main className="flex gap-8 px-6 py-6 items-start">
            {/*  Formulario a la izquierda */}
            <form className="flex flex-col justify-center">
                <label className="text-indigo-950 font-medium mb-1">Desde Fecha:</label>
                <input type="date" pattern="\d{2}/\d{2}/\d{4}" placeholder="Desde Fecha" onChange={(e) => setDesdeFecha(e.target.value)} className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Hasta Fecha:</label>
                <input type="date" pattern="\d{2}/\d{2}/\d{4}" placeholder="Hasta Fecha" onChange={(e) => setHastaFecha(e.target.value)} className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Tipo de Habitación:</label>
                <select name="tipoHabitacion" value={tipoSeleccionado} onChange={(e: React.ChangeEvent<HTMLSelectElement>) => setTipoSeleccionado(e.target.value as TipoHabitacion)} className="p-2 border rounded mb-4 text-gray-400 focus:text-indigo-950">
                    <option value="" disabled className="text-gray-400">Seleccionar tipo</option>
                    <option value="Individual">Individual</option>
                    <option value="DobleEstandar">Doble Estandar</option>
                    <option value="DobleSuperior">Doble Superior</option>
                    <option value="SuperiorFamilyPlan">Superior Family Plan</option>
                    <option value="SuiteDoble">Suite Doble</option>
                    
                </select>
            </form>
            {/*  Tabla a la derecha */}
            <section className="flex-2 max-h-[800px]">
                {tipoSeleccionado && (
                    <><table className="w-full border-collapse border shadow-lg">
                        <thead className="bg-indigo-950 text-white sticky top-0 z-10">
                            <tr>
                                <th className="p-2 text-white sticky top-0">Fecha</th>
                                <th colSpan={habitacionesPorTipo[tipoSeleccionado]?.length} className="border p-2 sticky top-0">Habitaciones</th>
                            </tr>
                        </thead>
                        <tbody>

                            <tr>
                                <td className="p-2 border text-center font-semibold"></td>
                                {habitacionesPorTipo[tipoSeleccionado]?.map((num) => (
                                    <td key={num} className="p-2 border text-center">{num}</td>
                                ))}
                            </tr>

                            {fechasMostradas.map((fecha, i) => (
                                <tr key={fecha}>
                                    <td className="p-2 border text-center font-medium">{fecha}</td>
                                    {habitacionesPorTipo[tipoSeleccionado]?.map((num) => (
                                        <td key={num} className="p-4 border bg-white"></td>
                                    ))}
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    
                    <li className="flex items-center gap-2 mt-4 flex-wrap justify-center">
                        <span className="w-4 h-4 rounded-full bg-red-500"></span>
                        <span className="text-sm text-indigo-950">RESERVADA</span>

                        <span className="w-4 h-4 rounded-full bg-white border border-gray-400"></span>
                        <span className="text-sm text-indigo-950">DISPONIBLE</span>

                        <span className="w-4 h-4 rounded-full bg-gray-700"></span>
                        <span className="text-sm text-indigo-950">FUERA DE SERVICIO</span>

                        <span className="w-4 h-4 rounded-full bg-blue-900"></span>
                        <span className="text-sm text-indigo-950">OCUPADA</span>

                        <span className="w-4 h-4 rounded-full bg-green-500"></span>
                        <span className="text-sm text-indigo-950">SELECCIONADA</span>

                    </li><button className="block mx-auto mt-6 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">Aceptar</button></>
            
                )}
                
            </section>
        </main>
    );
}
'use client';

import { useState } from "react";
import { format, parseISO, eachDayOfInterval, isAfter } from "date-fns";
import { usePathname } from "next/navigation";
import OcuparHabitacionIgualmente from "../carteles/ocuparHabitacionIgualmente";

export default function ReservarHabitacion({ ocultarTabla = false }) {

    const [filas, setFilas] = useState([
        { id: 1, numero: 3, desde: "02/12/2024", hasta: "02/12/2024", checked: false },
        { id: 2, numero: 3, desde: "03/12/2024", hasta: "03/12/2024", checked: false },
        { id: 3, numero: 5, desde: "03/12/2024", hasta: "05/12/2024", checked: false },
        { id: 4, numero: 8, desde: "01/12/2024", hasta: "01/12/2024", checked: false },
    ]);

    // =========================
    // VALIDACIONES DE FECHA
    // =========================
    const [desdeFecha, setDesdeFecha] = useState("");
    const [hastaFecha, setHastaFecha] = useState("");

    const [erroresFecha, setErroresFecha] = useState({
        desdeInvalido: false,
        hastaInvalido: false,
        ordenInvalido: false,
    });

    const [fechasValidas, setFechasValidas] = useState(false);

    function validarFechas(desde: string, hasta: string) {
        const hoy = new Date();
        hoy.setHours(0, 0, 0, 0);

        const d = desde ? new Date(desde) : null;
        const h = hasta ? new Date(hasta) : null;

        const nuevosErrores = {
            desdeInvalido: false,
            hastaInvalido: false,
            ordenInvalido: false,
        };

        if (d && d < hoy) nuevosErrores.desdeInvalido = true;
        if (h && h < hoy) nuevosErrores.hastaInvalido = true;
        if (d && h && d > h) nuevosErrores.ordenInvalido = true;

        setErroresFecha(nuevosErrores);

        const valido = !(nuevosErrores.desdeInvalido || nuevosErrores.hastaInvalido || nuevosErrores.ordenInvalido);
        setFechasValidas(valido);
        return valido;
    }

    // =========================
    // GENERAR FECHAS PARA LA TABLA
    // =========================
    function generarFechas(desde: string, hasta: string): string[] {
        const inicio = parseISO(desde);
        const fin = parseISO(hasta);

        if (isAfter(inicio, fin)) return [];

        const intervalo = eachDayOfInterval({ start: inicio, end: fin });
        return intervalo.map((fecha) => format(fecha, "dd/MM/yyyy"));
    }

    const fechasMostradas =
        desdeFecha && hastaFecha && fechasValidas
            ? generarFechas(desdeFecha, hastaFecha)
            : [];

    // =========================
    // TIPO DE HABITACIÓN
    // =========================
    type TipoHabitacion = "Individual" | "DobleEstandar" | "SuiteDoble" | "DobleSuperior" | "SuperiorFamilyPlan";

    const habitacionesPorTipo: Record<TipoHabitacion, number[]> = {
        Individual: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
        DobleEstandar: [11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28],
        DobleSuperior: [29, 30, 31, 32, 33, 34, 35, 36],
        SuperiorFamilyPlan: [37, 38, 39, 40, 41, 42, 43, 44, 45, 46],
        SuiteDoble: [47, 48],
    };

    const [tipoSeleccionado, setTipoSeleccionado] = useState<"" | TipoHabitacion>("");

    // =========================
    // TABLA SELECCIÓN
    // =========================
    const pathname = usePathname();
    const [mostrarCartelOH, setMostrarCartelOH] = useState(false);
    const [seleccionados, setSeleccionados] = useState<string[]>([]);

    const toggleSeleccion = (fecha: string, habitacion: number) => {
        const key = `${fecha}|${habitacion}`;
        setSeleccionados(prev =>
            prev.includes(key)
                ? prev.filter(item => item !== key)
                : [...prev, key]
        );
    };

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

    // =========================
    // RENDER
    // =========================
    return (
        <main className="flex gap-8 px-6 py-6 items-start">

            {/* FORMULARIO DE FECHAS Y TIPO */}
            <form className="flex flex-col justify-center">
                {/* DESDE FECHA */}
                <label className="text-indigo-950 font-medium mb-1">Desde Fecha:</label>
                <input
                    type="date"
                    value={desdeFecha}
                    onChange={(e) => {
                        setDesdeFecha(e.target.value);
                        validarFechas(e.target.value, hastaFecha);
                    }}
                    className={`p-2 border rounded mb-1 text-indigo-950
                        ${(erroresFecha.desdeInvalido || erroresFecha.ordenInvalido) ? "border-red-500 bg-red-100" : ""}
                    `}
                />
                {erroresFecha.desdeInvalido && (
                    <span className="text-red-600 text-sm mb-2 block">
                        La fecha no puede ser menor a hoy
                    </span>
                )}
                {erroresFecha.ordenInvalido && (
                    <span className="text-red-600 text-sm mb-2 block">
                        La fecha "Desde" no puede ser posterior a "Hasta"
                    </span>
                )}

                {/* HASTA FECHA */}
                <label className="text-indigo-950 font-medium mb-1">Hasta Fecha:</label>
                <input
                    type="date"
                    value={hastaFecha}
                    onChange={(e) => {
                        setHastaFecha(e.target.value);
                        validarFechas(desdeFecha, e.target.value);
                    }}
                    className={`p-2 border rounded mb-1 text-indigo-950
                        ${(erroresFecha.hastaInvalido || erroresFecha.ordenInvalido) ? "border-red-500 bg-red-100" : ""}
                    `}
                />
                {erroresFecha.hastaInvalido && (
                    <span className="text-red-600 text-sm mb-2 block">
                        La fecha no puede ser menor a hoy
                    </span>
                )}
                {erroresFecha.ordenInvalido && (
                    <span className="text-red-600 text-sm mb-2 block">
                        La fecha "Hasta" no puede ser anterior a "Desde"
                    </span>
                )}

                {/* TIPO DE HABITACIÓN */}
                <label className="text-indigo-950 font-medium mb-1">Tipo de Habitación:</label>
                <select
                    disabled={!fechasValidas}
                    value={tipoSeleccionado}
                    onChange={(e) => setTipoSeleccionado(e.target.value as TipoHabitacion)}
                    className={`p-2 border rounded mb-4 
                        ${!fechasValidas ? "bg-gray-200 cursor-not-allowed text-gray-400" : "text-indigo-950"}
                    `}
                >
                    <option value="" disabled>Seleccionar tipo</option>
                    <option value="Individual">Individual</option>
                    <option value="DobleEstandar">Doble Estandar</option>
                    <option value="DobleSuperior">Doble Superior</option>
                    <option value="SuperiorFamilyPlan">Superior Family Plan</option>
                    <option value="SuiteDoble">Suite Doble</option>
                </select>
            </form>

            {/* TABLA ESTADO HABITACIÓN */}
            <section className="flex-2 max-h-[800px]">

                {tipoSeleccionado && fechasMostradas.length > 0 && (
                    <>
                        <table className="w-full border-collapse border shadow-lg">
                            <thead className="bg-indigo-950 text-white sticky top-0 z-10">
                                <tr>
                                    <th className="p-2">Fecha</th>
                                    <th colSpan={habitacionesPorTipo[tipoSeleccionado].length} className="p-2">
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

                                {fechasMostradas.map(fecha => (
                                    <tr key={fecha}>
                                        <td className="p-2 border text-center font-medium">{fecha}</td>

                                        {habitacionesPorTipo[tipoSeleccionado].map(num => {
                                            const key = `${fecha}|${num}`;
                                            const seleccionado = seleccionados.includes(key);

                                            return (
                                                <td
                                                    key={num}
                                                    className={`p-4 border cursor-pointer 
                                                        ${seleccionado ? "bg-green-500" : "bg-white"}
                                                    `}
                                                    onClick={() => toggleSeleccion(fecha, num)}
                                                ></td>
                                            );
                                        })}
                                    </tr>
                                ))}
                            </tbody>
                        </table>

                        {/* LEYENDA */}
                        <li className="flex items-center gap-2 mt-4 flex-wrap justify-center">
                            <span className="w-4 h-4 rounded-full bg-red-500"></span>
                            <span>RESERVADA</span>

                            <span className="w-4 h-4 rounded-full bg-white border"></span>
                            <span>DISPONIBLE</span>

                            <span className="w-4 h-4 rounded-full bg-gray-700"></span>
                            <span>FUERA DE SERVICIO</span>

                            <span className="w-4 h-4 rounded-full bg-blue-900"></span>
                            <span>OCUPADA</span>

                            <span className="w-4 h-4 rounded-full bg-green-500"></span>
                            <span>SELECCIONADA</span>
                        </li>

                        <button
                            className="block mx-auto mt-6 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800"
                            onClick={() => {
                                if (pathname === "/ocuparHabitacion") {
                                    setMostrarCartelOH(true);
                                }
                            }}
                        >
                            Aceptar
                        </button>

                        {mostrarCartelOH && (
                            <OcuparHabitacionIgualmente onClose={() => setMostrarCartelOH(false)} />
                        )}
                    </>
                )}

            </section>

            {/* TABLA DERECHA */}
            {!ocultarTabla && (
                <section className="flex-1">
                    <h2 className="bg-indigo-950 text-white font-bold text-center mb-0">
                        Habitaciones Disponibles:
                    </h2>

                    <table className="w-full">
                        <thead className="bg-indigo-950 text-white">
                            <tr>
                                <th className="p-3 border">Eliminar</th>
                                <th className="p-3 border">Número</th>
                                <th className="p-3 border">Desde</th>
                                <th className="p-3 border">Hasta</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filas.map(f => (
                                <tr key={f.id} className="bg-white hover:bg-indigo-100">
                                    <td className="p-3 border text-center">
                                        <input
                                            type="checkbox"
                                            checked={f.checked}
                                            onChange={() => toggleCheck(f.id)}
                                        />
                                    </td>
                                    <td className="p-3 border text-center">{f.numero}</td>
                                    <td className="p-3 border text-center">{f.desde}</td>
                                    <td className="p-3 border text-center">{f.hasta}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

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

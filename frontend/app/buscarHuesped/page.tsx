'use client';

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import axios from "axios";
import Link from "next/link";
import CartelNoEncontrado from "../carteles/huespedNoEncontrado";
import { esSoloLetras, esSoloNumeros, validarDNI } from "../validaciones/validaciones";
import { TipoHuesped } from "../tabla/page";

type SortKey = keyof TipoHuesped | "dni" | "tipoDocumento";

export default function BuscarHuesped() {
    const router = useRouter();
    
    // Estados del formulario
    const [apellido, setApellido] = useState("");
    const [nombre, setNombre] = useState("");
    const [tipoDoc, setTipoDoc] = useState("");
    const [numeroDoc, setNumeroDoc] = useState("");
    
    // Estados de resultados
    const [huespedes, setHuespedes] = useState<TipoHuesped[]>([]);
    const [huespedSeleccionado, setHuespedSeleccionado] = useState<TipoHuesped | null>(null);
    const [mostrarCartel, setMostrarCartel] = useState(false);
    
    // Estados de errores
    const [errorNombre, setErrorNombre] = useState(false);
    const [errorApellido, setErrorApellido] = useState(false);
    const [errorNumeroDoc, setErrorNumeroDoc] = useState(false);
    
    // Estado de ordenamiento
    const [sortConfig, setSortConfig] = useState<{ key: SortKey | null; direction: 'asc' | 'desc' }>({
        key: "apellido",
        direction: 'asc'
    });

    // Validaciones en tiempo real
    React.useEffect(() => {
        setErrorNombre(nombre !== "" && !esSoloLetras(nombre));
    }, [nombre]);

    React.useEffect(() => {
        setErrorApellido(apellido !== "" && !esSoloLetras(apellido));
    }, [apellido]);

    React.useEffect(() => {
        setErrorNumeroDoc(numeroDoc !== "" && (!esSoloNumeros(numeroDoc) || !validarDNI(numeroDoc)));
    }, [numeroDoc]);

    // Función de búsqueda
    const buscarHuesped = async (e: React.FormEvent) => {
        e.preventDefault();

        // Validaciones
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
            // Construir parámetros - siempre enviar todos, incluso si están vacíos
            // Ejemplo: http://localhost:8080/huespedes?nombre=&apellido=M&tipoDocumento&dni=
            const params: any = {
                apellido: apellido || "",
                nombre: nombre || "",
                tipoDocumento: tipoDoc || "",
                dni: numeroDoc || ""
            };

            const response = await axios.get("http://localhost:8080/huespedes", {
                params
            });

            setHuespedes(response.data || []);
            setHuespedSeleccionado(null);

            if (!response.data || response.data.length === 0) {
                setMostrarCartel(true);
            }
        } catch (error) {
            console.error("Error al buscar huéspedes:", error);
            setHuespedes([]);
            setMostrarCartel(true);
        }
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

    // Seleccionar huésped
    const setSeleccionado = (huesped: TipoHuesped) => {
        setHuespedSeleccionado(huesped);
    };

    // Navegación según el caso de uso
    const handleSiguiente = () => {
        // Si no hay selección o no hay resultados, ir a Dar Alta (CU11)
        if (huespedSeleccionado === null || huespedes.length === 0) {
            router.push("/darAltaHuesped");
            return;
        }

        // Si hay selección, guardar en sessionStorage y ir a Modificar (CU10)
        if (huespedSeleccionado) {
            sessionStorage.setItem('huespedSeleccionado', JSON.stringify(huespedSeleccionado));
            router.push("/modificarHuesped");
        }
    };

    // Manejar Enter en botones
    const handleKeyDown = (e: React.KeyboardEvent, action: () => void) => {
        if (e.key === 'Enter') {
            e.preventDefault();
            action();
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

    return (
        <main className="flex gap-8 px-8 py-8 items-start bg-gradient-to-br from-gray-50 to-gray-100 min-h-screen">
            {/* FORMULARIO DE BÚSQUEDA */}
            <div className="bg-white rounded-xl shadow-lg p-6 w-80 h-fit sticky top-4">
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
                                errorApellido
                                    ? "border-red-500 bg-red-50"
                                    : "border-gray-300 hover:border-indigo-400"
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
                                errorNombre
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
                                errorNumeroDoc
                                    ? "border-red-500 bg-red-50"
                                    : "border-gray-300 hover:border-indigo-400"
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
                        onKeyDown={(e) => handleKeyDown(e, buscarHuesped)}
                        className="w-full px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105"
                    >
                        Buscar
                    </button>
                </form>
            </div>

            {/* TABLA DE RESULTADOS */}
            <section className="flex-1">
                <div className="bg-white rounded-xl shadow-xl overflow-hidden">
                    <div className="p-6 border-b-2 border-indigo-200">
                        <h2 className="text-2xl font-bold text-indigo-950">
                            Resultados de Búsqueda
                        </h2>
                        <p className="text-sm text-gray-600 mt-1">
                            {huespedes.length > 0 
                                ? `${huespedes.length} huésped(es) encontrado(s)` 
                                : "No hay resultados para mostrar"}
                        </p>
                    </div>

                    {huespedes.length > 0 ? (
                        <div className="max-h-[600px] overflow-y-auto overflow-x-auto">
                            <table className="w-full border-collapse">
                                <thead className="bg-gradient-to-r from-indigo-950 to-indigo-900 text-white sticky top-0 z-10 shadow-md">
                                    <tr>
                                        <th className="p-4 font-semibold text-left">Seleccionar</th>
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
                                            Tipo Documento {sortConfig.key === "tipoDocumento" ? (sortConfig.direction === "asc" ? "↑" : "↓") : "↑↓"}
                                        </th>
                                        <th 
                                            className="p-4 font-semibold text-left cursor-pointer hover:bg-indigo-800 transition-colors"
                                            onClick={() => handleSort("dni")}
                                        >
                                            Número Documento {sortConfig.key === "dni" ? (sortConfig.direction === "asc" ? "↑" : "↓") : "↑↓"}
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {sortedHuespedes.map((h) => {
                                        const isSelected = huespedSeleccionado?.huespedID.dni === h.huespedID.dni && 
                                                          huespedSeleccionado?.huespedID.tipoDocumento === h.huespedID.tipoDocumento;
                                        return (
                                            <tr
                                                key={`${h.huespedID.tipoDocumento}-${h.huespedID.dni}`}
                                                className={`hover:bg-indigo-50 transition-colors ${
                                                    isSelected ? "bg-indigo-100" : "bg-white"
                                                }`}
                                            >
                                                <td className="p-4 border-b border-gray-200 text-center">
                                                    <input
                                                        type="radio"
                                                        name="seleccion"
                                                        checked={isSelected}
                                                        onChange={() => setSeleccionado(h)}
                                                        className="w-4 h-4 text-indigo-950 focus:ring-indigo-500"
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

                {/* BOTONES DE ACCIÓN */}
                <div className="flex justify-center gap-4 mt-6">
                    <Link href="/menu">
                        <button
                            onKeyDown={(e) => handleKeyDown(e, () => router.push("/menu"))}
                            className="px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold"
                        >
                            Cancelar
                        </button>
                    </Link>

                    <button
                        onClick={handleSiguiente}
                        onKeyDown={(e) => handleKeyDown(e, handleSiguiente)}
                        className="px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105"
                    >
                        Siguiente
                    </button>
                </div>
            </section>

            {/* CARTEL DE NO ENCONTRADO */}
            {mostrarCartel && (
                <CartelNoEncontrado onClose={() => setMostrarCartel(false)} />
            )}
        </main>
    );
}

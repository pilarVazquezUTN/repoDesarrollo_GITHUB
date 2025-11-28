"use client"
import { useEffect, useState } from "react";
import Tabla from "../tabla/page";
import axios from "axios";
import { TipoHuesped } from "../tabla/page";
import CartelNoEncontrado from "../carteles/huespedNoEncontrado";
import Link from "next/link";

export default function BuscarHuesped() { 

    const [dni, setDni] = useState("");
    const [nombre, setNombre] = useState("");
    const [apellido, setApellido] = useState("");
    const [tipoDoc, setTipoDoc] = useState("");
    const [huespedes, setHuespedes] = useState<TipoHuesped[]>([]);
    const [mostrarCartel, setMostrarCartel] = useState(false);

    // Estados para errores (todos boolean)
    const [errorNombre, setErrorNombre] = useState(false);
    const [errorApellido, setErrorApellido] = useState(false);
    const [errorDni, setErrorDni] = useState(false);

    const buscarHuesped = async (e: React.FormEvent) => {
        e.preventDefault();

        // VALIDACIONES --------------------------
        const soloLetras = /^[A-ZÁÉÍÓÚÑ\s]*$/;
        const soloNumeros = /^[0-9]*$/;

        const nombreInvalido = nombre !== "" && !soloLetras.test(nombre);
        const apellidoInvalido = apellido !== "" && !soloLetras.test(apellido);
        const dniInvalido = dni !== "" && !soloNumeros.test(dni);

        setErrorNombre(nombreInvalido);
        setErrorApellido(apellidoInvalido);
        setErrorDni(dniInvalido);

        // Si hay errores → no consultar API
        if (nombreInvalido || apellidoInvalido || dniInvalido) {
            return;
        }

        try {
            const response = await axios.get("http://localhost:8080/huespedes", {
                params: {
                    dni: dni || null,
                    nombre: nombre || null,
                    apellido: apellido || null,
                    tipoDocumento: tipoDoc || null
                }
            });

            setHuespedes(response.data);

            if (response.data.length === 0) {
                setMostrarCartel(true);
            }

        } catch (error) {
            console.log("error al cargar huespedes:", error);
        }
    };

    return (
    <main className="flex gap-8 px-6 py-6 items-start">

        {/* FORMULARIO */}
        <form onSubmit={buscarHuesped} className="flex flex-col justify-center">

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

            <label className="text-indigo-950 font-medium mb-1">Tipo de Documento:</label>
            <select 
                name="tipoDocumento"
                value={tipoDoc}
                onChange={(e) => setTipoDoc(e.target.value.toUpperCase())}
                className="p-2 border rounded mb-4 text-gray-400 focus:text-indigo-950"
            >
                <option value="" className="text-gray-400">Seleccionar tipo</option>
                <option value="DNI">DNI</option>
                <option value="PASAPORTE">PASAPORTE</option>
                <option value="CUIT">LE</option>
                <option value="CUIT">LC</option>
            </select>

            <label className="text-indigo-950 font-medium mb-1">Número de Documento:</label>
            <input 
                type="text"
                placeholder="Número de Documento"
                value={dni}
                onChange={(e) => setDni(e.target.value)}
                className={`p-2 border rounded mb-1 placeholder-gray-400 text-indigo-950
                    ${errorDni ? "border-red-500" : ""}`}
            />
            {errorDni && (
                <p className="text-red-500 text-sm mb-3">Ingrese solo números.</p>
            )}

            <button className="self-center px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">
                Buscar
            </button>

        </form>

        {/* TABLA */}
        <section className="flex-1 flex flex-col">
            <Tabla huespedes={huespedes}/>

            <div className="mt-6 justify-center sticky bottom-0 flex gap-4">
                <Link href="/menu">
                    <button className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition"> Cancelar </button>
                </Link>
                
                <Link href="/modificarHuesped">
                    <button className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition"> Siguiente </button>
                </Link>
            </div>
        </section>

        {/* CARTEL */}
        {mostrarCartel && (
            <CartelNoEncontrado onClose={() => setMostrarCartel(false)} />
        )}

    </main>
    );
}

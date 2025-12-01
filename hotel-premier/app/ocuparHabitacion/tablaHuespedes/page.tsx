"use client"
import { useEffect, useState } from "react";
import Tabla from "../tabla/page";
import axios from "axios";
import { TipoHuesped } from "../../tabla/page";
import CartelNoEncontrado from "../../carteles/huespedNoEncontrado";
import Link from "next/link";
import { useRouter } from "next/navigation";
import CartelNoSeleccionoHuesped from "../../carteles/CartelNoSeleccionoHuesped";
import SeleccionarRepresentantes from "../../carteles/SeleccionarRepresentantes";
import SeguirOcuparHabitacion from "../../carteles/SeguirOcuparHabitacion";

export default function BuscarHuespedOcuparHabitacion() { 

    const [dni, setDni] = useState("");
    const [nombre, setNombre] = useState("");
    const [apellido, setApellido] = useState("");
    const [tipoDoc, setTipoDoc] = useState("");
    const [huespedes, setHuespedes] = useState<TipoHuesped[]>([]);
    const [mostrarCartel, setMostrarCartel] = useState(false);

    // ✔️ AGREGAR ESTO — huésped único seleccionado
    const [huespedSeleccionado, setHuespedSeleccionado] = useState<TipoHuesped | null>(null);

    // ✔️ Selecciones múltiples
    const [huespedesSeleccionados, setHuespedesSeleccionados] = useState<TipoHuesped[]>([]);

    const [mostrarCartelNoSelecciono, setMostrarCartelNoSelecciono] = useState(false);
    const [mostrarCartelRepresentantes, setMostrarCartelRepresentantes] = useState(false);
    const [mostrarSeguir, setMostrarSeguir] = useState(false);

    const router = useRouter();

    // Estados para errores
    const [errorNombre, setErrorNombre] = useState(false);
    const [errorApellido, setErrorApellido] = useState(false);
    const [errorDni, setErrorDni] = useState(false);

    const buscarHuesped = async (e: React.FormEvent) => {
        e.preventDefault();

        const soloLetras = /^[A-ZÁÉÍÓÚÑ\s]*$/;
        const soloNumeros = /^[0-9]*$/;

        const nombreInvalido = nombre !== "" && !soloLetras.test(nombre);
        const apellidoInvalido = apellido !== "" && !soloLetras.test(apellido);
        const dniInvalido = dni !== "" && !soloNumeros.test(dni);

        setErrorNombre(nombreInvalido);
        setErrorApellido(apellidoInvalido);
        setErrorDni(dniInvalido);

        if (nombreInvalido || apellidoInvalido || dniInvalido) {
            return;
        }

        try {
            const response = await axios.get("http://localhost:8080/huespedes", {
                params: {
                    dni: dni || null,
                    tipoDocumento: tipoDoc || null,
                    nombre: nombre ? `${nombre}%` : null,
                    apellido: apellido ? `${apellido}%` : null
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

    const toggleSeleccion = (huesped: TipoHuesped) => {
        setHuespedesSeleccionados(prev => {
            const existe = prev.some(h => h.huespedID.dni === huesped.huespedID.dni);
            if (existe) {
                return prev.filter(h => h.huespedID.dni !== huesped.huespedID.dni);
            } else {
                return [...prev, huesped];
            }
        });
    };

    // ✔️ función que se llama al hacer clic en un único huésped
    const setSeleccionado = (huesped: TipoHuesped) => {
        setHuespedSeleccionado(huesped);
        console.log("Huésped seleccionado:", huesped);
    };

    const irADarAltaHuesped = () => {
        if (huespedesSeleccionados.length === 0) {
            setMostrarCartelNoSelecciono(true);
            return;
        }

        setMostrarCartelRepresentantes(true);
    };


    const recibirRepresentantes = (representante: TipoHuesped | null) => {
        if (representante) {
            console.log("Representante seleccionado:", representante);
            // Hacés lo que necesites con el representante
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
                {errorApellido && <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>}

                <label className="text-indigo-950 font-medium mb-1">Nombre:</label>
                <input 
                    type="text"
                    placeholder="Nombre"
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value.toUpperCase())}
                    className={`p-2 border rounded mb-1 placeholder-gray-400 text-indigo-950
                        ${errorNombre ? "border-red-500" : ""}`}
                />
                {errorNombre && <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>}

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
                    <option value="LE">LE</option>
                    <option value="LC">LC</option>
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
                {errorDni && <p className="text-red-500 text-sm mb-3">Ingrese solo números.</p>}

                <button className="self-center px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">
                    Buscar
                </button>
            </form>

            {/* TABLA */}
            <section className="flex-1 flex flex-col">
                <Tabla 
                    huespedes={huespedes}
                    toggleSeleccion={toggleSeleccion}
                    seleccionados={huespedesSeleccionados}
                    setSeleccionado={setSeleccionado}   // ← AGREGAR ESTO
                />

                <div className="mt-6 justify-center sticky bottom-0 flex gap-4">
                    <Link className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition" href="/menu">
                        Cancelar
                    </Link>
                    
                    <button onClick={irADarAltaHuesped} className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">
                        Siguiente
                    </button>
                </div>
            </section>

            {mostrarCartel && (
                <CartelNoEncontrado onClose={() => setMostrarCartel(false)} />
            )}

            {mostrarCartelNoSelecciono && (
                <CartelNoSeleccionoHuesped
                    onAceptar={() => setMostrarCartelNoSelecciono(false)}  
                />
                )}

            {mostrarCartelRepresentantes && (
                <SeleccionarRepresentantes
                    huespedes={huespedesSeleccionados}
                    onCerrar={() => setMostrarCartelRepresentantes(false)}
                    onAceptar={(rep) => {
                    recibirRepresentantes(rep);
                    setMostrarCartelRepresentantes(false); // cerrar cartel
                    }}
                    onSeguir={() => setMostrarSeguir(true)} // abrir el nuevo cartel
                />
            )}

            {mostrarSeguir && (
                <SeguirOcuparHabitacion
                    onSeguirCargando={() => {
                    console.log("Seguir Cargando Huespedes");
                    setMostrarSeguir(false);
                    }}
                    onCargarMasHabitaciones={() => {
                    console.log("Cargar más Habitaciones");
                    setMostrarSeguir(false);
                    }}
                    onSalir={() => {
                    console.log("Salir");
                    setMostrarSeguir(false);
                    }}
                />
            )}



        </main>
    );
}

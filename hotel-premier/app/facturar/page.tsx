"use client";
import Tabla from "../tabla/page";
import { useState } from "react";
import FacturarUnTerceroModal from "../carteles/facturarUnTercero";
import axios from "axios";

export default function Facturar() {
    const [open, setOpen] = useState(false); {/*hace que el cartel de facturar a un tercero se muestre o no*/}
    const [mostrar, setMostrar] = useState(false);
    const [nroHabitacion, setNroHabitacion] = useState("");
    const [horaSalida, setHoraSalida] = useState("");
    const [huespedes, setHuespedes] = useState([]); //

    const buscarHuespedes = async () => {
        if (!nroHabitacion.trim()) return;

        try {
            const response = await axios.get(`http://localhost:8080/habitaciones/${nroHabitacion}/huespedes`);
            setHuespedes(response.data); // guardo los huespedes en el state
            setMostrar(true); // muestro la tabla
        } catch (error: any) {
            console.error(error);
            alert("No se pudieron cargar los huespedes.");
        }
    };

    return (
        <main className="flex flex-col gap-8 px-6 py-6 items-center">
            <form className="flex flex-row "> {/*  hago que esten uno al lado del otro los ingresos de datos */}

                <div className="flex flex-col"> {/*hago que el label este arriba de su input*/}
                    <label className="text-indigo-950 ">numero de la Habitacion:</label>
                    <input onChange={(e) => setNroHabitacion(e.target.value)} type="text" placeholder="numero de la Habitacion" className="mr-10 p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />
                </div>
                <div className="flex flex-col"> {/*hago que el label este arriba de su input*/}
                    <label className="text-indigo-950">hora de salida:</label>
                    <input type="text" placeholder="hora de salida" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />
                </div>
            </form>
            <button onClick={buscarHuespedes} className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">Buscar</button> 
            {mostrar && (
                <>
                    <p className="text-indigo-950 font-bold ">seleccione una persona como responsable de pago</p>
                    <Tabla huespedes={huespedes} key={nroHabitacion} />
                    <div className=" mt-6 justify-center sticky bottom-0 flex gap-4 ">
                        {/* NO VA LINK SOLO QUE QUERIA PROBAR COMPLETAR FACTURA 
                        <Link href="/facturar/completarFactura"> 
                            
                        </Link>
                        */}
                        
                        {/* BOTON PARA ABRIR EL CARTEL DE FACTURAR A UN TERCERO */}
                        <button  className="px-4 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
                        onClick={() => 
                            setOpen(true)
                        } 
                        >
                            Facturar a un tercero
                        </button>
                        {/* MOSTRAR CARTEL SOLO SI open === true */}
                        {open && (
                            <FacturarUnTerceroModal onClose={() => setOpen(false)} />
                        )}
                    </div>
                </>
                )}

        </main>
    );
}
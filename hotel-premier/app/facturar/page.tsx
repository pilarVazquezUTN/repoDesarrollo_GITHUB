"use client";
import Tabla from "../tabla/page";
import { useState } from "react";
import Link from "next/link";
import FacturarUnTerceroModal from "../carteles/facturarUnTercero"; 

export default function Facturar() {
    const [open, setOpen] = useState(false); {/*hace que el cartel de facturar a un tercero se muestre o no*/}
    const [mostrar, setMostrar] = useState(false);
    return (
        <main className="flex flex-col gap-8 px-6 py-6 items-center">
            <form className="flex flex-row "> {/*  hago que esten uno al lado del otro los ingresos de datos */}

                <div className="flex flex-col"> {/*hago que el label este arriba de su input*/}
                    <label className="text-indigo-950 ">numero de la Habitacion:</label>
                    <input type="text" placeholder="numero de la Habitacion" className="mr-10 p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />
                </div>
                <div className="flex flex-col"> {/*hago que el label este arriba de su input*/}
                    <label className="text-indigo-950">hora de salida:</label>
                    <input type="text" placeholder="hora de salida" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />
                </div>
            </form>
            <button onClick={() => setMostrar(true)} className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">Buscar</button> 
            {mostrar && (
                <>
                    <p className="text-indigo-950 font-bold ">seleccione una persona como responsable de pago</p>
                    <Tabla />
                    <div className=" mt-6 justify-center sticky bottom-0 flex gap-4 ">
                        {/* NO VA LINK SOLO QUE QUERIA PROBAR COMPLETAR FACTURA */}
                        <Link href="/facturar/completarFactura"> 
                            
                        </Link>
                        
                        
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
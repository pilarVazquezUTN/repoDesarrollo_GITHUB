"use client";
import { useState } from "react";
import EliminarHuesped from "../carteles/eliminarHuesped";
import NoSePuedeEliminarHuesped from "../carteles/noSePuedeEliminarHUesped";

export default function DarBajaHuesped(){
    const [openAceptar, setOpenAceptar] = useState(false);
    return(
        <main className="flex gap-80 px-6 py-6 items-start justify-center"> 

            <form className="flex flex-col">

                <label className="text-indigo-950 font-medium mb-1">Apellido*:</label>
                <input type="text" placeholder="Apellido" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Nombre*:</label>
                <input type="text" placeholder="Nombre" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Tipo de Documento*:</label>
                <select name="tipoDocumento" defaultValue="" className="p-2 border rounded mb-4 text-gray-400 focus:text-indigo-950">
                    <option value="" className="text-gray-400">Seleccionar tipo</option>
                    <option value="DNI">DNI</option>
                    <option value="Pasaporte">Pasaporte</option>
                    <option value="CUIT">CUIT</option>
                </select>

                <label className="text-indigo-950 font-medium mb-1">Número de Documento*:</label>
                <input type="text" placeholder="Número de Documento" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">CUIT*:</label>
                <input type="text" placeholder="XX-XXXXXXXX-X" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />
                
                <label className="text-indigo-950 font-medium mb-1">Posicion frente al IVA*:</label>
                <input type="text" placeholder="Número de Documento" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />
                <p className="mt-3">campos a completar obligatorio*</p>
            </form>

            <form className="flex flex-col">
                <label className="text-indigo-950 font-medium mb-1">Calle*:</label>
                <input type="text" placeholder="calle" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Numero*:</label>
                <input type="text" placeholder="numero" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Departamento:</label>
                <input type="text" placeholder="departamento" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Piso</label>
                <input type="text" placeholder="piso" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Codigo Postal*:</label>
                <input type="text" placeholder="codigo postal" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />
                
                <label className="text-indigo-950 font-medium mb-1">Localidad*:</label>
                <input type="text" placeholder="localidad" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Provincia*:</label>
                <input type="text" placeholder="provincia" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />
                
                <label className="text-indigo-950 font-medium mb-1">Pais*:</label>
                <input type="text" placeholder="pais" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />
            </form>
            
            <form className="flex flex-col">

                <label className="text-indigo-950 font-medium mb-1">Fecha de nacimiento*:</label>
                <input type="date"  placeholder="fecha de nacimiento" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Telefono*:</label>
                <input type="text" placeholder="telefono" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Email*:</label>
                <input type="text" placeholder="email" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Ocupacion:</label>
                <input type="text" placeholder="ocupacion" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Nacionalidad:</label>
                <input type="text" placeholder="nacionalidad" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />
                
                <div className="flex flex-row gap-3 items-center">   
                    
                {/* BOTON PARA ABRIR EL CARTEL DE FACTURAR A UN TERCERO */}
                    <button type="button" className="px-4 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition">Cancelar</button>
            

                    <button type="button" className="px-4 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
                        onClick={() => 
                            setOpenAceptar(true)
                        } 
                        >
                            Eliminar
                        </button>
                        {/* MOSTRAR CARTEL SOLO SI open === true */}
                        {openAceptar && (
                            /*<EliminarHuesped onClose={() => setOpenAceptar(false)} />*/
                            <NoSePuedeEliminarHuesped onClose={() => setOpenAceptar(false)} />
                        )}


                </div>
            </form>

        </main>
    );
}
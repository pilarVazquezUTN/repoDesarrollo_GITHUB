'use client';

import { useState } from "react";
import { format, parseISO, eachDayOfInterval, isAfter } from "date-fns";
import { usePathname } from "next/navigation";

export default function ReservarHabitacion() {

    const [filas, setFilas] = useState([
        { id: 1, numero: 3, desde: "02/12/2024", hasta: "02/12/2024", checked: false },
        { id: 2, numero: 3, desde: "03/12/2024", hasta: "03/12/2024", checked: false },
        { id: 3, numero: 5, desde: "03/12/2024", hasta: "05/12/2024", checked: false },
        { id: 4, numero: 8, desde: "01/12/2024", hasta: "01/12/2024", checked: false },
    ]);
    //FUNCION PARA SABER QUE FUNCIONES ESTAN TILDADAS PARA ELIMINAR
    const toggleCheck = (id:number) => { //ACTUALIZO EL ESTADO DE MI CHECKBOX EN MI CODIGO PARA QUE SE SEPA QUE ESTA SELECCIONADA (PASA DE {... checked: false} A {... checked: true} )
        setFilas(prev => //ESTADO DE LAS FILAS
            prev.map(f => //RECORRE LA FILA Y LA ACTUALIZA DEPENDIENDO SI F (FILA CON DATOS) CUMPLE O NO CON LO SIGUIENTE
                f.id === id ? { ...f, checked: !f.checked } : f // LA F EN LA QUE YO ESTOY PARADA TIENE EL MISMO ID QUE ESTOY BUSCANDO -> SI ENTONCES COPIO LOS DATOS DE F Y CAMBIO SU CHECK A TRUE {... checked: false}=>{... checked: true}
            )
        );
    };

    const eliminarSeleccionados = () => {
        setFilas(prev => prev.filter(f => !f.checked)); //FILTER CREA UN NUEVO ARRAY CON LAS FILAS QUE TENGAN CHECKBOX FALSE (ES DECIR QUE QUITA LAS SELECCIONADAS)
    };

    //estadoHabitacion
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
            const inicio = parseISO(desde);
            const fin = parseISO(hasta);
    
            // Validación: si inicio es después de fin, devolvemos array vacío
            if (isAfter(inicio, fin)) return [];
    
            const intervalo = eachDayOfInterval({ start: inicio, end: fin });
            return intervalo.map((fecha) => format(fecha, "dd/MM/yyyy"));
        }
        const fechasMostradas = desdeFecha && hastaFecha ? generarFechas(desdeFecha, hastaFecha) : [];
        //estadoHabitacion

        const pathname = usePathname();//para poder negarle la tabla de habitaciones disponibles a estadoHabitacion

        const [seleccionados, setSeleccionados] = useState<string[]>([]);//seleccion dentro de la tabla1
        
    const toggleSeleccion = (fecha: string, habitacion: number) => {
        const key = `${fecha}|${habitacion}`;

        setSeleccionados(prev =>
            prev.includes(key)
            ? prev.filter(item => item !== key) // si ya estaba → lo saco
            : [...prev, key]                    // si no estaba → lo agrego
        );
    };

    return (
        <main className="flex gap-8 px-6 py-6 items-start">

            {/* ESTO ES MI ESTADO HABITACION */}
            <form className="flex flex-col justify-center">
                <label className="text-indigo-950 font-medium mb-1">Desde Fecha:</label>
                <input type="date"  placeholder="Desde Fecha" onChange={(e) => setDesdeFecha(e.target.value)} className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

                <label className="text-indigo-950 font-medium mb-1">Hasta Fecha:</label>
                <input type="date"  placeholder="Hasta Fecha" onChange={(e) => setHastaFecha(e.target.value)} className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

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
                                    {habitacionesPorTipo[tipoSeleccionado]?.map((num) => {
                                        const key = `${fecha}|${num}`;
                                        const seleccionado = seleccionados.includes(key);

                                        return (
                                            <td
                                                key={num}
                                                className={`p-4 border cursor-pointer ${
                                                    seleccionado ? "bg-green-500" : "bg-white"
                                                }`}
                                                onClick={() => toggleSeleccion(fecha, num)}
                                            ></td>
                                        );
                                    })}
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
            {/*TERMINA MI ESTADO HABITACION */}
            {/*tabla para ir viendo las habitaciones que se seleccionaron y poder trabajar con ella TENER EN CUENTA QUE AMBAS TABLAS TRABAJAN EN CONJUNTO, ES DECIR, SI SE ELIMINA DE ESTA TABLA TIENE QUE DESELECCIONARSE DE LA OTRA*/}
            {pathname !== "/estadoHabitacion" &&(
                <section className="flex-1  max-h-[50px]">
                    <h2 className="bg-indigo-950 text-white font-bold sticky top-0 text-center mb-0">Habitaciones Disponibles:</h2>
                    <table className="w-full ">
                        <thead className="bg-indigo-950 text-white sticky top-0 ">
                            <tr>
                                <th className="p-3 border W-full sticky top-1 ">eliminar:</th>
                                <th className="p-3 border W-full sticky top-1 ">numero Habitacion:</th>
                                <th className="p-3 border W-full sticky top-1">Desde:</th>
                                <th className="p-3 border W-full sticky top-1">Hasta:</th>

                            </tr>
                        </thead>
                        <tbody>
                            {/* Repetí esta fila para cada resultado SON EJEMPLOS EN REALIDAD LA TABLA ES DINAMICA  EN BASE A LA BDD*/}
                            {/* el boton de eliminar cuando se haga la implementacion se puede mejorar*/}
                            {filas.map(f => (
                                <tr key={f.id} className="bg-white hover:bg-indigo-100">
                                    <td className="p-3 border text-center">
                                        <input type="checkbox" checked={f.checked} onChange={() => toggleCheck(f.id)}/>
                                    </td>
                                    <td className="p-3 border text-center">{f.numero}</td>
                                    <td className="p-3 border text-center">{f.desde}</td>
                                    <td className="p-3 border text-center">{f.hasta}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    {/* se podria buscar la forma de que apretando la casilla de eliminar automaticamente se eliminen sin necesidad del boton eliminar*/ }
                    <button onClick={eliminarSeleccionados} className="block mx-auto mt-6 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">Eliminar</button>
                </section>
            )}
        </main>
    );
}
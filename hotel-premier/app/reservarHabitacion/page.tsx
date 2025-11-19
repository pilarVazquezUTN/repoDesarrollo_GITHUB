'use client';

import { useState } from "react";
import EstadoHabitacion from "../estadoHabitacion/page";

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
    return (
        <main className="flex gap-8 px-6 py-6 items-start">
            <section className="flex-2 ">
            <EstadoHabitacion />
            </section>
            {/*tabla para ir viendo las habitaciones que se seleccionaron y poder trabajar con ella TENER EN CUENTA QUE AMBAS TABLAS TRABAJAN EN CONJUNTO, ES DECIR, SI SE ELIMINA DE ESTA TABLA TIENE QUE DESELECCIONARSE DE LA OTRA*/}
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
        </main>
    );
}
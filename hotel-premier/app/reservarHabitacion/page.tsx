'use client';

import { useState } from "react";
import EstadoHabitacion from "../estadoHabitacion/page";

export default function BuscarHuesped() {

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
                        <tr className="bg-white hover:bg-indigo-100">
                            <td className="p-3 border text-center"><input type="checkbox" name="seleccion" /></td>
                            <td className="p-2 border text-center">3</td>
                            <td className="p-2 border text-center">02/12/2024</td>
                            <td className="p-2 border text-center">02/12/2024</td>
                        </tr>
                        <tr className="bg-white hover:bg-indigo-100">
                            <td className="p-3 border text-center"><input type="checkbox" name="seleccion" /></td>
                            <td className="p-3 border text-center">3</td>
                            <td className="p-3 border text-center">03/12/2024</td>
                            <td className="p-3 border text-center">03/12/2024</td>
                        </tr>
                        <tr className="bg-white hover:bg-indigo-100">
                            <td className="p-3 border text-center"><input type="checkbox" name="seleccion" /></td>
                            <td className="p-2 border text-center">5</td>
                            <td className="p-2 border text-center">03/12/2024</td>
                            <td className="p-2 border text-center">05/12/2024</td>
                        </tr>
                        <tr className="bg-white hover:bg-indigo-100">
                            <td className="p-3 border text-center"><input type="checkbox" name="seleccion" /></td>
                            <td className="p-3 border text-center">8</td>
                            <td className="p-3 border text-center">01/12/2024</td>
                            <td className="p-3 border text-center">01/12/2024</td>
                        </tr>
                    </tbody>
                </table>
                {/* se podria buscar la forma de que apretando la casilla de eliminar automaticamente se eliminen sin necesidad del boton eliminar*/ }
                <button className="block mx-auto mt-6 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">Eliminar</button>
            </section>
        </main>
    );
}
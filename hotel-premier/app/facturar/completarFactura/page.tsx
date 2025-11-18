"use client";
import { it } from "node:test";
import { useState } from "react";

export default function CompletarFactura() {
    const items = [
        { label: 'Valor de la Estadía', price: 10000 },
        { label: 'Consumos de la Habitación', price: 500 },
        { label: 'Lavado y Planchado', price: 500 },
        { label: 'Sauna', price: 10000 },
        { label: 'Bar', price: 0 },
    ];
    const [seleccionarItems, itemsSeleccionados] = useState<number[]>([]);
    const CuentaTotal = (index: number) => {
            itemsSeleccionados((prev) => {
            if (prev.includes(index)) {
                // Si ya estaba seleccionado → lo saco
                return prev.filter(i => i !== index);
            } else {
                // Si no estaba → lo agrego
                return [...prev, index];
            }
        });
    };
      // Calcular total dinámico
    const total = seleccionarItems.reduce((acc, index) => acc + items[index].price, 0);
    return (
        <main className=" flex flex-col px-6 py-6">
            <section className="text-left flex flex-col gap-4">
                <strong className=" ">Razon social: se completa con los datos de la BDD</strong>
                <strong className=" ">Tipo de factura: Supongo que el tipo de factura esta en la base de datos?</strong>
            </section>

            <section className="text-center mt-4">
                <strong className=" ">Selecciona los items a facturar:</strong>
                <div className="flex flex-col items-center mt-4">
                    {items.map((item, index) => (
                        <label key={index} className="flex justify-between items-center mb-2">
                            <div className="flex text-center gap-2">
                                <input type="checkbox" className="form-checkbox" checked={seleccionarItems.includes(index)} onChange={() => CuentaTotal(index)}/>
                                <span className="text-center">{item.label}</span>
                            </div>
                            <span className="font-medium">${item.price}</span>
                        </label>
                    ))}
                </div>
                <div className="mt-4 text-xl font-bold">
                    total del monto a pagar: ${total}
                </div>
                <button className="block mx-auto mt-6 px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">Aceptar</button>
            </section>
        </main>
    );
}
"use client";
import Link from "next/link";
import React from "react";

interface Props {
  onSeguirCargando: () => void;
  onCargarMasHabitaciones: () => void;
  onSalir: () => void;
}

export default function SeguirOcuparHabitacion({
  onSeguirCargando,
  onCargarMasHabitaciones,
  onSalir,
}: Props) {
  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center">

      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL CARTEL */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140 text-center">

        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img
          src="/imagenInformacion.png"
          alt="Informacion"
          className="absolute top-3 left-3 w-10 h-10"
        />

        {/* TITULO */}
        <div className="pl-10">
          <h2 className="text-indigo-950 font-bold text-center mb-4">
            ¿Qué querés hacer ahora?
          </h2>
          <h3 className="text-gray-700 text-sm mb-3">
            Elegí una de las siguientes opciones:
          </h3>
        </div>

        {/* BOTONES */}
        <div className="mt-6 flex flex-col gap-3">
          
          <Link href="/ocuparHabitacion/tablaHuespedes"> 
                <button className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition" onClick={onSeguirCargando}> Seguir Cargando Huespedes</button>
          </Link>
          

            <Link href="/ocuparHabitacion">
                <button className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition" onClick={onCargarMasHabitaciones}> Cargar más Habitaciones</button>
            </Link>

            <Link href="/menu"> 
                <button className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition" onClick={onSalir}> Salir </button>
            </Link>

        </div>

      </div>
    </div>
  );
}

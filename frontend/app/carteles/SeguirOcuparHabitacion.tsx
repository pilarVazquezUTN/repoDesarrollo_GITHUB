"use client";
import { useRouter } from "next/navigation";
import React, { useState } from "react";

interface Props {
  onSeguirCargando: () => void;
  onCargarMasHabitaciones: () => Promise<void>;
  onSalir: () => Promise<void>;
}

export default function SeguirOcuparHabitacion({
  onSeguirCargando,
  onCargarMasHabitaciones,
  onSalir,
}: Props) {
  const router = useRouter();
  const [cargando, setCargando] = useState(false);

  const handleSalir = async () => {
    setCargando(true);
    await onSalir();
    router.push("/menu");
  };

  const handleCargarOtra = async () => {
    setCargando(true);
    await onCargarMasHabitaciones();
  };

  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center">

      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL CARTEL */}
      <div className="relative bg-white rounded-xl shadow-2xl p-8 w-[500px] text-center">

        {/* ICONO DE INFORMACIÓN */}
        <div className="flex justify-center mb-4">
          <div className="w-16 h-16 rounded-full bg-indigo-100 flex items-center justify-center">
            <svg className="w-10 h-10 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
          </div>
        </div>

        {/* TITULO */}
        <h2 className="text-2xl font-bold text-indigo-950 mb-2">
          Huéspedes cargados
        </h2>
        
        <p className="text-gray-600 mb-6">
          Se han asignado los huéspedes a las habitaciones seleccionadas.
        </p>

        <h3 className="text-indigo-950 font-semibold mb-4">
          ¿Qué desea hacer ahora?
        </h3>

        {/* BOTONES */}
        <div className="flex flex-col gap-3">
          
          <button 
            onClick={onSeguirCargando}
            disabled={cargando}
            className="w-full px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold disabled:opacity-50"
          >
            Seguir Cargando Huéspedes
          </button>

          <button 
            onClick={handleCargarOtra}
            disabled={cargando}
            className="w-full px-6 py-3 bg-gradient-to-r from-indigo-700 to-indigo-600 text-white rounded-lg hover:from-indigo-600 hover:to-indigo-500 transition-all shadow-md hover:shadow-lg font-semibold disabled:opacity-50"
          >
            {cargando ? "Enviando estadías..." : "Cargar Otra Habitación"}
          </button>

          <button 
            onClick={handleSalir}
            disabled={cargando}
            className="w-full px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold disabled:opacity-50"
          >
            {cargando ? "Enviando estadías..." : "Salir"}
          </button>

        </div>

      </div>
    </div>
  );
}

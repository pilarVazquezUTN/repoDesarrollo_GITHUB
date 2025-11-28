"use client";

import Link from "next/link";

type Props = {
  onClose: () => void;
  onNuevo: () => void;  // 👈 NUEVO
  nombre: string;
  apellido: string;
};

export default function CargarOtroHuesped({ onClose, onNuevo, nombre, apellido }: Props) {
  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-140 text-center">
        
        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img src="/imagenInformacion.png"  alt="Informacion" className="absolute top-3 left-3 w-10 h-10" />

        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950">
          El huesped <strong>{nombre} {apellido}</strong> ha sido cargado satisfactoriamente
        </h2>

        <strong>¿Desea cargar otro huesped?</strong>

        <div className="mt-6 justify-center sticky bottom-0 flex gap-4">
          
          {/* SI → limpiar campos sin salir de la página */}
          <button 
            type="button" 
            className="px-7 py-1 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
            onClick={() => {
              onNuevo();   // 👉 Limpia todo
              onClose();   // 👉 Cierra cartel
            }}
          >
            SI
          </button>

          {/* NO → volver al menú */}
          <Link href="/menu">
            <button 
              type="button" 
              className="px-7 py-1 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
              onClick={onClose}
            >
              NO
            </button>
          </Link>

        </div>
      </div>
    </div>
  );
}

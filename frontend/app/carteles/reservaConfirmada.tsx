"use client";

import Link from "next/link";

type Props = {
  onClose: () => void;
};

export default function ReservaConfirmada({ onClose }: Props) {
  return (



    <div className="fixed inset-0 flex items-center justify-center z-50">



      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL MODAL */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140">

        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img
                 src="/imagenInformacion.png"
                 alt="Info"
                 className="absolute top-4 left-4 w-12 h-12"
               />

        {/* Botón de cerrar */}
                <button
                  type="button"
                  onClick={onClose}
                  className="absolute top-2 right-2 text-black font-bold text-lg hover:text-gray-800"
                >
                  ✕
                </button>

        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950">
          Reservas creadas exitosamente
        </h2>


      </div>
    </div>
  );
}

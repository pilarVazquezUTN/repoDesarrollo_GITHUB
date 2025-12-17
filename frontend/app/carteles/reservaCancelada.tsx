"use client";

import { useEffect } from "react";

type Props = {
  cantidad?: number;
  onClose: () => void;
};

export default function ReservaCancelada({ cantidad = 1, onClose }: Props) {
  useEffect(() => {
    const handleKeyPress = () => {
      onClose();
    };

    window.addEventListener('keydown', handleKeyPress);
    return () => {
      window.removeEventListener('keydown', handleKeyPress);
    };
  }, [onClose]);

  const mensaje = cantidad === 1 
    ? "Reserva cancelada" 
    : "Reservas canceladas";

  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-[9999]">
      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL MODAL */}
      <div className="relative bg-white rounded-xl shadow-xl p-8 w-auto min-w-[500px]">
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
          className="absolute top-4 right-4 text-gray-700 hover:text-black text-xl font-bold"
        >
          ✕
        </button>

        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950 mt-8">
          {mensaje}
        </h2>
        <p className="text-center text-gray-700 mb-6">
          PRESIONE UNA TECLA PARA CONTINUAR…
        </p>
      </div>
    </div>
  );
}

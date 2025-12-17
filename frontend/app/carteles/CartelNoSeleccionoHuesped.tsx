"use client";
import React from "react";

interface Props {
  onAceptar: () => void;
}

export default function CartelNoSeleccionoHuesped({ onAceptar }: Props) {
  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center">

      {/* FONDO OSCURECIDO */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140 text-center">

        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img
          src="/imagenError.png"
          alt="Error"
          className="absolute top-3 left-3 w-10 h-10"
        />

        {/* TEXTO CENTRADO CON PADDING */}
        <div className="pl-10">
          <h2 className="text-red-700 font-bold text-center mb-4">
            No se seleccionó ningún huésped
          </h2>

          <h3 className="text-indigo-950">
            Debe seleccionar un huésped antes de continuar.
          </h3>
        </div>

        {/* BOTÓN */}
        <div className="mt-6 flex justify-center">
          <button
            type="button"
            className="px-6 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition"
            onClick={onAceptar}
          >
            Aceptar
          </button>
        </div>

      </div>
    </div>
  );
}

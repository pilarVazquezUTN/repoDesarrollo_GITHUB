import React from "react";

interface Props {
  mensaje: string;
  onCorregir: () => void;
  onAceptar: () => void;
}

export default function ErrorDniExistente({ mensaje, onCorregir, onAceptar }: Props) {
  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center">

      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL CARTEL */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140 text-center">

        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img
          src="/imagenError.png"
          alt="Error"
          className="absolute top-3 left-3 w-10 h-10"
        />

        {/* CONTENEDOR DEL TEXTO - con padding para centrarlo visualmente */}
        <div className="pl-10"> 
          <h2 className="text-red-700 font-bold text-center mb-4">
            {mensaje}
          </h2>
          <h3> El tipo y número de documento ya existen en el sistema </h3>
        </div>

        {/* BOTONES */}
        <div className="mt-6 flex justify-center gap-4">
          <button
            type="button"
            className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition"
            onClick={onCorregir}
          >
            Corregir
          </button>

          <button
            type="button"
            className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition"
            onClick={onAceptar}
          >
            Aceptar Igualmente
          </button>
        </div>
      </div>
    </div>
  );
}

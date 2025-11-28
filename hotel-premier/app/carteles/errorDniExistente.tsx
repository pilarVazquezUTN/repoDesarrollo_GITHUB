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

      {/* CONTENIDO DEL CARTEL - VISUAL DEL CARTEL BLANCO */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140 text-center">

        {/* TÍTULO (mantengo contenido pero con estilo blanco) */}
        <h2 className="text-red-700 font-bold text-center mb-4">
          {mensaje}
        </h2>

        {/* BOTONES MÁS JUNTOS COMO EL CARTEL BLANCO */}
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

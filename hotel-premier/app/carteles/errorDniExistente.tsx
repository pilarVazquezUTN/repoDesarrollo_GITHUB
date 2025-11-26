import React from "react";

interface Props {
  mensaje: string;
  onCorregir: () => void;
  onAceptar: () => void;
}

export default function ErrorDniExistente({ mensaje, onCorregir, onAceptar }: Props) {
  return (
    <div
      className="fixed inset-0 flex items-center justify-center z-[9999]"
      style={{ background: "transparent" }}
    >
      <div className="bg-yellow-300 border-2 border-yellow-600 rounded-xl shadow-xl p-6 max-w-sm text-center">
        <p className="mb-4 text-lg font-semibold">{mensaje}</p>

        <div className="flex justify-center gap-4">
          <button
            type="button"
            className="px-4 py-2 rounded-lg border border-black bg-white hover:bg-gray-100"
            onClick={onCorregir}
          >
            Corregir
          </button>

          <button
            type="button"
            className="px-4 py-2 rounded-lg border border-black bg-white hover:bg-gray-100"
            onClick={onAceptar}
          >
            Aceptar Igualmente
          </button>
        </div>
      </div>
    </div>
  );
}

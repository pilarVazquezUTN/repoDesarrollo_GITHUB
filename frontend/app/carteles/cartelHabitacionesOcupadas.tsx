"use client";

import React from "react";

interface Props {
  lista: string[];
  seleccionados?: string[];
  desdeFecha: string;
  hastaFecha: string;
  onClose: () => void;
  onConfirm?: () => void;
}

export default function CartelHabitacionesOcupadas({
  lista,
  desdeFecha,
  hastaFecha,
  onClose,
  onConfirm
}: Props) {

  const habitacionesUnicas = Array.from(
    new Set(lista.map((item) => item.split("|")[1]))
  ).sort((a, b) => Number(a) - Number(b));

  const handleConfirmar = () => {
    if (onConfirm) {
      onConfirm();
    }
    onClose();
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      
      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL MODAL */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140">

        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img
          src="/imagenInformacion.png"
          alt="Informacion"
          className="absolute top-3 left-3 w-10 h-10"
        />

        {/* TITULO */}
        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950">
          Confirmar selección de habitaciones
        </h2>

        {/* CONTENIDO DEL MENSAJE */}
        <div className="text-gray-700 text-center space-y-4">

          {/* Fechas seleccionadas */}
          <div className="grid grid-cols-2 gap-4 bg-gray-50 p-3 rounded-md text-sm">
            <div>
              <span className="block text-xs text-gray-500 uppercase font-semibold">
                Desde
              </span>
              <span className="font-medium text-indigo-900">{desdeFecha}</span>
            </div>
            <div>
              <span className="block text-xs text-gray-500 uppercase font-semibold">
                Hasta
              </span>
              <span className="font-medium text-indigo-900">{hastaFecha}</span>
            </div>
          </div>

          {/* Info */}
          <p>
            Se han seleccionado{" "}
            <span className="font-bold">{lista.length}</span> bloques de ocupación.
          </p>

          {/* Habitaciones afectadas */}
          {habitacionesUnicas.length > 0 && (
            <div>
              <p className="text-xs text-gray-500 uppercase font-semibold mb-1">
                Habitaciones afectadas:
              </p>

              <div className="flex flex-wrap justify-center gap-2">
                {habitacionesUnicas.map((num) => (
                  <span
                    key={num}
                    className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800"
                  >
                    Hab. {num}
                  </span>
                ))}
              </div>
            </div>
          )}
        </div>

        {/* BOTONES */}
        <div className="mt-6 justify-center flex gap-4">
          <button
            type="button"
            className="px-7 py-1 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
            onClick={onClose}
          >
            Volver
          </button>

          <button
            type="button"
            className="px-7 py-1 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
            onClick={handleConfirmar}
          >
            Confirmar
          </button>
        </div>

      </div>
    </div>
  );
}

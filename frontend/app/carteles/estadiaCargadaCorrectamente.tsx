"use client";
import React from "react";

interface Props {
  onClose: () => void;
  habitaciones: number[];
  checkin: string;
  checkout?: string;
}

export default function EstadiaCargadaCorrectamente({
  onClose,
  habitaciones,
  checkin,
  checkout
}: Props) {
  return (
    <div className="fixed inset-0 flex items-center justify-center z-[9999]">
      
      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO */}
      <div className="relative bg-white rounded-xl shadow-2xl p-8 w-[450px]">

        {/* ICONO DE ÉXITO */}
        <div className="flex justify-center mb-4">
          <div className="w-16 h-16 rounded-full bg-green-100 flex items-center justify-center">
            <svg className="w-10 h-10 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
            </svg>
          </div>
        </div>

        {/* TITULO */}
        <h2 className="text-2xl font-bold text-center mb-4 text-indigo-950">
          ¡Estadía registrada correctamente!
        </h2>

        {/* INFORMACIÓN */}
        <div className="bg-gray-50 rounded-lg p-4 mb-6">
          <div className="grid grid-cols-2 gap-4 text-sm">
            <div>
              <span className="block text-xs text-gray-500 uppercase font-semibold">Check-in</span>
              <span className="font-medium text-indigo-900">{checkin}</span>
            </div>
            {checkout && (
              <div>
                <span className="block text-xs text-gray-500 uppercase font-semibold">Check-out</span>
                <span className="font-medium text-indigo-900">{checkout}</span>
              </div>
            )}
          </div>
          
          <div className="mt-4">
            <span className="block text-xs text-gray-500 uppercase font-semibold mb-2">Habitaciones</span>
            <div className="flex flex-wrap gap-2">
              {habitaciones.map((num) => (
                <span
                  key={num}
                  className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800"
                >
                  Hab. {num}
                </span>
              ))}
            </div>
          </div>
        </div>

        {/* BOTÓN */}
        <div className="flex justify-center">
          <button
            type="button"
            className="px-8 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold"
            onClick={onClose}
          >
            Aceptar
          </button>
        </div>
      </div>
    </div>
  );
}


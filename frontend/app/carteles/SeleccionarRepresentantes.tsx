"use client";
import React from "react";
import { TipoHuesped } from "@/app/tabla/page";

interface Props {
  huespedes: TipoHuesped[];
  onCerrar: () => void;
  onAceptar: (representante: TipoHuesped | null) => void;
  onSeguir?: () => void; // ← nuevo callback opcional
}


export default function SeleccionarRepresentantes({
  huespedes,
  onCerrar,
  onAceptar,
  onSeguir,
}: Props) {
  const [representante, setRepresentante] = React.useState<TipoHuesped | null>(null);
  const [error, setError] = React.useState(false); // ← estado para mostrar error

    const handleAceptar = () => {
        if (!representante) {
            setError(true);
            return;
        }

        onAceptar(representante);

        if (onSeguir) {
            onSeguir(); // Abrir el siguiente cartel
        }
    };


  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center">

      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL CARTEL */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140 text-center">

        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img
          src="/imagenInformacion.png"
          alt="Informacion"
          className="absolute top-3 left-3 w-10 h-10"
        />

        {/* TITULO */}
        <div className="pl-10">
          <h2 className="text-indigo-950 font-bold text-center mb-4">
            Seleccionar Representante
          </h2>
          <h3 className={`text-sm mb-3 ${error ? "text-red-500 font-bold" : "text-gray-700"}`}>
            Marcá un huésped como representante.
          </h3>
        </div>

        {/* LISTA DE HUESPEDES */}
        <div className="max-h-[250px] overflow-y-auto border p-3 rounded text-left mt-2">
          {huespedes.map((h) => (
            <label
              key={h.huespedID.dni}
              className="flex items-center gap-2 mb-2 cursor-pointer"
            >
              <input
                type="radio"
                name="representante"
                checked={representante?.huespedID.dni === h.huespedID.dni}
                onChange={() => {
                  setRepresentante(h);
                  setError(false); // ← si selecciona algo, se quita el error
                }}
              />
              {h.apellido}, {h.nombre} —{" "}
              {h.huespedID.tipoDocumento} {h.huespedID.dni}
            </label>
          ))}
        </div>

        {/* BOTONES */}
        <div className="mt-6 flex justify-center gap-4">
          <button
            type="button"
            className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition"
            onClick={onCerrar}
          >
            Cancelar
          </button>

          <button
            type="button"
            className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition"
            onClick={handleAceptar}
          >
            Aceptar
          </button>
        </div>

      </div>
    </div>
  );
}

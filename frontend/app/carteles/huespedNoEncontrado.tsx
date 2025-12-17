"use client";
import Link from "next/link";

interface Props {
  onClose: () => void;
}

export default function huespedNoEncontrado({ onClose }: Props) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">

      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL CARTEL */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140">

        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img src="/imagenError.png"  alt="Error" className="absolute top-3 left-3 w-10 h-10" />

        <h2 className="text-red-700 font-bold text-center mb-4">
          No se encontraron huéspedes con esos datos.<br />
        </h2>

        <p className="text-gray-700 text-center mb-6">
          Presione <b>Dar Alta Huésped</b> si desea cargarlo.
        </p>

        {/* BOTONES MÁS JUNTOS */}
        <div className="mt-6 flex justify-center gap-4">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-gray-500 transition"
          >
            Cancelar
          </button>

          <Link
            href="/darAltaHuesped"
            className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition"
          >
            Dar Alta Huésped
          </Link>
        </div>
      </div>
    </div>
  );
}

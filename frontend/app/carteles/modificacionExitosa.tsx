"use client";
import Link from "next/link";

type Props = {
  onClose: () => void;
};

export default function ModificacionExitosa({ onClose }: Props) {
  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center">
      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL CARTEL */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140 text-center">
        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img src="/imagenInformacion.png" alt="Información" className="absolute top-3 left-3 w-10 h-10" />

        <div className="pl-10">
          <h2 className="text-xl font-bold text-center mb-4 text-indigo-950">
            La operación ha culminado con éxito
          </h2>
        </div>

        {/* BOTÓN */}
        <div className="mt-6 flex justify-center">
          <Link href="/menu">
            <button
              type="button"
              className="px-6 py-2 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold"
              onClick={onClose}
            >
              Aceptar
            </button>
          </Link>
        </div>
      </div>
    </div>
  );
}

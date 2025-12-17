"use client";
import Link from "next/link";

type Props = {
  onSi: () => void;
  onNo: () => void;
};

export default function CancelarModificacion({ onSi, onNo }: Props) {
  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center">
      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL CARTEL */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140 text-center">
        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img src="/imagenAdvertencia.png" alt="Advertencia" className="absolute top-3 left-3 w-10 h-10" />

        <div className="pl-10">
          <h2 className="text-xl font-bold text-center mb-4 text-indigo-950">
            ¿Desea cancelar la modificación del huésped?
          </h2>
        </div>

        {/* BOTONES SI / NO */}
        <div className="mt-6 flex justify-center gap-4">
          <Link href="/menu">
            <button
              type="button"
              className="px-6 py-2 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold"
              onClick={onSi}
            >
              Si
            </button>
          </Link>

          <button
            type="button"
            className="px-6 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold"
            onClick={onNo}
          >
            No
          </button>
        </div>
      </div>
    </div>
  );
}

"use client";

type Props = {
  onSi: () => void;
  onNo: () => void;
};

export default function CancelarAltaHuesped({ onSi, onNo }: Props) {
  return (
    <div className="fixed inset-0 flex items-center justify-center z-[9999]">

      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL MODAL */}
      <div className="relative bg-white rounded-xl shadow-2xl p-8 w-[450px]">

        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img src="/imagenAdvertencia.png" alt="Advertencia" className="absolute top-4 left-4 w-12 h-12" />

        <h2 className="text-2xl font-bold text-center mb-6 text-indigo-950 mt-4">
          ¿Desea cancelar el alta del huésped?
        </h2>

        <p className="text-center text-gray-600 mb-6">
          Los datos ingresados no serán guardados.
        </p>

        {/* Botones SI / NO */}
        <div className="flex justify-center gap-4">
          <button
            type="button"
            className="px-8 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold"
            onClick={onSi}
          >
            SI
          </button>

          <button
            type="button"
            className="px-8 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold"
            onClick={onNo}
          >
            NO
          </button>
        </div>
      </div>
    </div>
  );
}

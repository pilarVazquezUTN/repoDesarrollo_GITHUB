"use client";

type Props = {
  onClose: () => void;
};

export default function MenorEdad({ onClose }: Props) {
  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center">
      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL CARTEL */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140 text-center">
        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img src="/imagenError.png" alt="Error" className="absolute top-3 left-3 w-10 h-10" />

        <div className="pl-10">
          <h2 className="text-xl font-bold text-center mb-4 text-red-700">
            La persona seleccionada es menor de edad. Por favor elija otra
          </h2>
        </div>

        {/* BOTÓN */}
        <div className="mt-6 flex justify-center">
          <button
            type="button"
            className="px-6 py-2 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold"
            onClick={onClose}
          >
            Aceptar
          </button>
        </div>
      </div>
    </div>
  );
}


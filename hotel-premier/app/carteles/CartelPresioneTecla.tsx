"use client";

type Props = {
  onClose: () => void;
};

export default function CartelPresioneTecla({ onClose }: Props) {
  return (
    <div className="fixed inset-0 flex items-center justify-center z-[9999]">

      {/* CONTENIDO */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-120">

        {/* ICONO INFORMACIÓN */}
        <img
          src="/imagenInformacion.png"
          alt="Info"
          className="absolute top-3 left-3 w-10 h-10"
        />

        {/* TÍTULO */}
        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950">
          Las habitaciones seleccionadas fueron marcadas como ocupadas.
        </h2>

        {/* TEXTO / INDICACIÓN */}
        <p className="text-center text-gray-700 mb-6">
          Presione aceptar para continuar…
        </p>

        {/* BOTÓN */}
        <div className="flex justify-center">
          <button
            type="button"
            onClick={onClose}
            className="px-6 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
          >
            Aceptar
          </button>
        </div>
      </div>
    </div>
  );
}

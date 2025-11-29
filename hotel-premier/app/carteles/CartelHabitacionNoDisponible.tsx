// components/carteles/CartelAviso.tsx
"use client";

interface Props {
  mensaje: string;
  onClose: () => void;
}

export default function CartelAviso({ mensaje, onClose }: Props) {
  return (
    <div className="fixed inset-0 flex items-center justify-center z-[9999]">

      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL MODAL */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140 text-center">

        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img
          src="/imagenAdvertencia.png"
          alt="Advertencia"
          className="absolute top-3 left-3 w-10 h-10"
        />

        {/* Botón de cerrar */}
        <button
          type="button"
          onClick={onClose}
          className="absolute top-2 right-2 text-black font-bold text-lg hover:text-gray-700"
        >
          ✕
        </button>

        {/* TEXTO DEL MENSAJE */}
        <p className="text-xl font-semibold text-indigo-950 mt-4">
          {mensaje}
        </p>
      </div>
    </div>
  );
}

// components/carteles/CartelAviso.tsx
'use client';

interface Props {
  mensaje: string;
  onClose: () => void;
}

export default function CartelAviso({ mensaje, onClose }: Props) {
  return (
    <div
      className="fixed inset-0 flex items-center justify-center z-[9999]"
      style={{ background: "transparent" }}
    >
      <div className="bg-yellow-300 border-2 border-yellow-600 rounded-xl shadow-xl p-6 max-w-sm text-center relative">

        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img src="/imagenInformacion.png"  alt="Informacion" className="absolute top-3 left-3 w-10 h-10" />

        {/* Botón de cerrar */}
        <button
          type="button"
          onClick={onClose}
          className="absolute top-2 right-2 text-black font-bold text-lg hover:text-gray-800"
        >
          ✕
        </button>

        <p className="text-lg font-semibold">{"Habitaciones seleccionadas. Texto de prueba"}</p>
      </div>
    </div>
  );
}

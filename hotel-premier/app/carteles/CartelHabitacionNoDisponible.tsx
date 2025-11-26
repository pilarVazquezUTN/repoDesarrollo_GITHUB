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
        {/* Botón de cerrar */}
        <button
          type="button"
          onClick={onClose}
          className="absolute top-2 right-2 text-black font-bold text-lg hover:text-gray-800"
        >
          ✕
        </button>

        <p className="text-lg font-semibold">{mensaje}</p>
      </div>
    </div>
  );
}

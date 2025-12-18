"use client";

interface Props {
  mensaje: string;
  onClose: () => void;
}

export default function CartelInfoReserva({ mensaje, onClose }: Props) {
  return (
    <div className="fixed inset-0 flex items-center justify-center z-[9999]">
      
      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO */}
      <div className="relative bg-white rounded-xl shadow-2xl p-8 w-[450px]">

        {/* ICONO */}
        <img
          src="/imagenInformacion.png"
          alt="Información"
          className="absolute top-4 left-4 w-12 h-12"
        />

        {/* TITULO */}
        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950 mt-4">
          Información de Reserva
        </h2>

        {/* MENSAJE DINÁMICO */}
        <p className="text-center text-indigo-950 whitespace-pre-line mb-6">
          {mensaje}
        </p>

        {/* BOTÓN */}
        <div className="flex justify-center">
          <button
            type="button"
            className="px-8 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold"
            onClick={onClose}
          >
            Aceptar
          </button>
        </div>
      </div>
    </div>
  );
}


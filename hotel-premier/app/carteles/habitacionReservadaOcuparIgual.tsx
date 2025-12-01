"use client";

interface Props {
  onClose: () => void;
  onConfirm: () => void;
  mensajeExtra: string;
}

export default function HabitacionReservadaOcuparIgual({
  onClose,
  onConfirm,
  mensajeExtra
}: Props) {
  return (
    <div className="fixed inset-0 flex items-center justify-center z-[9999]">
      
      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140">

        {/* ICONO */}
        <img
          src="/imagenAdvertencia.png"
          alt="Advertencia"
          className="absolute top-3 left-3 w-10 h-10"
        />

        {/* TITULO */}
        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950 mt-8">
          Habitación Reservada
        </h2>

        {/* TEXTO PRINCIPAL */}
        <p className="text-center text-indigo-950 mb-4">
          La habitación seleccionada ya posee una reserva en el rango indicado.
        </p>

        {/* MENSAJE DINÁMICO */}
        <p className="text-center font-semibold text-indigo-950 whitespace-pre-line mb-6">
          {mensajeExtra}
        </p>

        {/* BOTONES */}
        <div className="mt-6 justify-center flex gap-4">
          <button
            type="button"
            className="px-7 py-1 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
            onClick={onClose}
          >
            Volver
          </button>

          <button
            type="button"
            className="px-7 py-1 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
            onClick={onConfirm}
          >
            Ocupar igual
          </button>
        </div>
      </div>
    </div>
  );
}

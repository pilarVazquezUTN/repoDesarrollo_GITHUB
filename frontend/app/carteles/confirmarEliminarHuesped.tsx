"use client";

type Props = {
  nombre: string;
  apellido: string;
  tipoDocumento: string;
  dni: string;
  onEliminar: () => void;
  onCancelar: () => void;
};

export default function ConfirmarEliminarHuesped({ nombre, apellido, tipoDocumento, dni, onEliminar, onCancelar }: Props) {
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
            Los datos del huésped <strong>{nombre} {apellido}</strong>, {tipoDocumento} {dni} serán eliminados del sistema
          </h2>
        </div>

        {/* BOTONES */}
        <div className="mt-6 flex justify-center gap-4">
          <button
            type="button"
            className="px-6 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold"
            onClick={onCancelar}
          >
            Cancelar
          </button>

          <button
            type="button"
            className="px-6 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-all shadow-md hover:shadow-lg font-semibold"
            onClick={onEliminar}
          >
            Eliminar
          </button>
        </div>
      </div>
    </div>
  );
}

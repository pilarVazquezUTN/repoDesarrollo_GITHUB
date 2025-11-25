"use client";

type Props = {
  onClose: () => void;
  mensaje: string; // Recibimos el mensaje con la lista de campos faltantes
};

export default function ErrorValidacionCartel({ onClose, mensaje }: Props) {
  return (
    <div className="fixed inset-0 flex items-center justify-center z-50 bg-black/50"> {/* Fondo semitransparente para destacar el cartel */}
      <div className="bg-white rounded-lg shadow-lg p-6 w-140 max-w-md mx-4"> {/* Estilos idénticos a CancelarAltaHuepsed */}
        
        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950">
          Campos Incompletos
        </h2>
        
        {/* Mostramos el mensaje. whitespace-pre-line permite que los \n se vean como saltos de línea */}
        <p className="text-gray-700 text-center mb-6 whitespace-pre-line font-medium">
          {mensaje}
        </p>

        {/* Botón idéntico al de tu otro cartel */}
        <div className="mt-6 flex justify-center sticky bottom-0">
            <button 
                type="button" 
                className="px-7 py-1 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition font-bold" 
                onClick={onClose}
            >
                ENTENDIDO
            </button>
        </div>

      </div>
    </div>
  );
}
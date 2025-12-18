"use client";

type Props = {
  onClose: () => void;
};

export default function CartelPresioneTecla({ onClose }: Props) {
  
  // Detectar cualquier tecla presionada
  const handleKeyDown = (e: React.KeyboardEvent) => {
    onClose();
  };

  return (
    <div 
      className="fixed inset-0 flex items-center justify-center z-[9999]"
      onKeyDown={handleKeyDown}
      tabIndex={0}
      autoFocus
    >
      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO */}
      <div className="relative bg-white rounded-xl shadow-2xl p-8 w-[500px]">

        {/* ICONO INFORMACIÓN */}
        <img
          src="/imagenInformacion.png"
          alt="Info"
          className="absolute top-4 left-4 w-12 h-12"
        />

        {/* TÍTULO */}
        <h2 className="text-2xl font-bold text-center mb-4 text-indigo-950 mt-4">
          Habitaciones marcadas como ocupadas
        </h2>

        {/* TEXTO / INDICACIÓN */}
        <p className="text-center text-gray-700 mb-2">
          Las habitaciones seleccionadas fueron marcadas con el color de "Ocupada".
        </p>
        
        <div className="flex justify-center my-4">
          <span className="w-8 h-8 rounded-full bg-yellow-500 border-2 border-yellow-600 shadow-md"></span>
        </div>

        <p className="text-center text-gray-600 text-sm mb-6">
          Presione el botón para continuar con la carga de huéspedes y acompañantes.
        </p>

        {/* BOTÓN */}
        <div className="flex justify-center">
          <button 
            type="button" 
            onClick={onClose} 
            className="px-8 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105"
            autoFocus
          > 
            PRESIONE PARA CONTINUAR
          </button>
        </div>
      </div>
    </div>
  );
}

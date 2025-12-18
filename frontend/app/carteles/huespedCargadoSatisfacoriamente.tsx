"use client";

type Props = {
  onSi: () => void;
  onNo: () => void;
  nombre: string;
  apellido: string;
};

export default function HuespedCargadoSatisfactoriamente({ onSi, onNo, nombre, apellido }: Props) {
  return (
    <div className="fixed inset-0 flex items-center justify-center z-[9999]">
      
      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL MODAL */}
      <div className="relative bg-white rounded-xl shadow-2xl p-8 w-[500px] text-center">
        
        {/* ICONO DE ÉXITO */}
        <div className="flex justify-center mb-4">
          <div className="w-16 h-16 rounded-full bg-green-100 flex items-center justify-center">
            <svg className="w-10 h-10 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
            </svg>
          </div>
        </div>

        <h2 className="text-2xl font-bold text-indigo-950 mb-4">
          ¡Huésped registrado!
        </h2>

        <p className="text-gray-700 mb-2">
          El huésped <strong className="text-indigo-950">{nombre} {apellido}</strong> ha sido satisfactoriamente cargado al sistema.
        </p>

        <p className="text-indigo-950 font-semibold mb-6 mt-4">
          ¿Desea cargar otro huésped?
        </p>

        <div className="flex justify-center gap-4">
          
          {/* SI → limpiar campos sin salir de la página */}
          <button 
            type="button" 
            className="px-8 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold"
            onClick={onSi}
          >
            SI
          </button>

          {/* NO → volver al menú */}
          <button 
            type="button" 
            className="px-8 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold"
            onClick={onNo}
          >
            NO
          </button>

        </div>
      </div>
    </div>
  );
}

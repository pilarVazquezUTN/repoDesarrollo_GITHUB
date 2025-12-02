'use client';

import { useRouter } from "next/navigation";
interface Props {
  lista: any[];
  seleccionados: string[];
  desdeFecha: string;
  hastaFecha: string;
  onClose: () => void;
}

export default function CartelListaHabitaciones({
                                                  lista,
                                                  seleccionados,
                                                  desdeFecha,
                                                  hastaFecha,
                                                  onClose
                                                }: Props)  {
    const router = useRouter(); // HORA  EXISTE
  return (
    <div className="fixed inset-0  bg-opacity-30 flex items-center justify-center z-[9999]">
      <div className="relative bg-white w-[600px] max-h-[80vh] overflow-auto rounded-xl p-8 shadow-xl border">

        {/* Ícono de información */}
        <img
          src="/imagenInformacion.png"
          alt="Info"
          className="absolute top-4 left-4 w-12 h-12"
        />

        {/* Botón cerrar */}
        <button
          className="absolute top-4 right-4 text-gray-700 hover:text-black text-xl font-bold"
          onClick={onClose}
        >
          ✕
        </button>

        {/* Título */}
        <h2 className="text-center text-2xl font-bold text-[#1D4F73] mb-6">
          HABITACIONES SELECCIONADAS:
        </h2>

        {/* Lista del backend */}
        <div className="space-y-6">
          {lista.map((item: any, index: number) => (
            <div key={index} className="text-[#1D4F73] text-lg leading-relaxed">
              <p>• HABITACIÓN {index + 1}: NÚMERO {item.numero}</p>
              <p>• TIPO: {item.tipo}</p>
              <p>• DESDE: {item.fechaDesde} - HASTA: {item.fechaHasta}</p>
            </div>
          ))}
        </div>

        {/* Botones */}
        <div className="flex justify-center gap-6 mt-8">
          <button className="px-4 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition">
            RECHAZAR
          </button>
         <button
           className="px-4 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
           onClick={() => {
             const seleccionEncoded = encodeURIComponent(
               JSON.stringify(seleccionados)
             );
             router.push(
               `/confirmarReserva?seleccion=${seleccionEncoded}&desde=${desdeFecha}&hasta=${hastaFecha}`
             );
           }}
         >
           ACEPTAR
         </button>



        </div>
      </div>
    </div>
  );
}

'use client';

import { useRouter } from "next/navigation";
import { format, parseISO } from "date-fns";
import { es } from "date-fns/locale";

interface Props {
  lista: any[];
  seleccionados: string[];
  rangos: { numero: number; desde: string; hasta: string }[];
  desdeFecha: string;
  hastaFecha: string;
  onClose: () => void;
  onAceptar: () => void;
}

export default function CartelListaHabitaciones({
  lista,
  rangos,
  seleccionados,
  desdeFecha,
  hastaFecha,
  onClose,
  onAceptar
}: Props) {
  const router = useRouter();

  const formatearFecha = (fecha: string) => {
    try {
      const fechaDate = parseISO(fecha);
      const nombreDia = format(fechaDate, "EEEE", { locale: es });
      const fechaFormateada = format(fechaDate, "dd/MM/yyyy");
      return { nombreDia: nombreDia.charAt(0).toUpperCase() + nombreDia.slice(1), fechaFormateada };
    } catch {
      return { nombreDia: "", fechaFormateada: fecha };
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-[9999]">
      <div className="relative bg-white w-[700px] max-h-[80vh] overflow-auto rounded-xl p-8 shadow-xl border">

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

        {/* Lista con formato del caso de uso */}
        <div className="space-y-6">
          {lista.map((item: any, index: number) => {
            const rango = rangos.find(r => r.numero === item.numero);
            if (!rango) return null;

            const fechaDesde = formatearFecha(rango.desde);
            const fechaHasta = formatearFecha(rango.hasta);

            return (
              <div key={index} className="text-[#1D4F73] text-lg leading-relaxed border-b pb-4">
                <p className="font-semibold">• Tipo de Habitación: {item.tipo}</p>
                <p>• Ingreso: {fechaDesde.nombreDia}, {fechaDesde.fechaFormateada}, 12:00hs.</p>
                <p>• Egreso: {fechaHasta.nombreDia}, {fechaHasta.fechaFormateada}, 10:00hs</p>
              </div>
            );
          })}
        </div>

        {/* Botones ACEPTAR y RECHAZAR */}
        <div className="flex justify-center gap-6 mt-8">
          <button 
            className="px-6 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 transition"
            onClick={() => {
              onClose();
            }}
          >
            RECHAZAR
          </button>

          <button
            className="px-6 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
            onClick={onAceptar}
          >
            ACEPTAR
          </button>
        </div>
      </div>
    </div>
  );
}

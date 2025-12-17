"use client";
import { useEffect } from "react";
import Link from "next/link";

type Props = {
  nombre: string;
  apellido: string;
  tipoDocumento: string;
  dni: string;
  onClose: () => void;
};

export default function HuespedEliminado({ nombre, apellido, tipoDocumento, dni, onClose }: Props) {
  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      event.preventDefault();
      onClose();
    };

    window.addEventListener("keydown", handleKeyDown);
    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, [onClose]);

  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center">
      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL CARTEL */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140 text-center">
        {/* IMAGEN ESQUINA SUPERIOR IZQUIERDA */}
        <img src="/imagenInformacion.png" alt="Información" className="absolute top-3 left-3 w-10 h-10" />

        <div className="pl-10">
          <h2 className="text-xl font-bold text-center mb-4 text-indigo-950">
            Los datos del huésped <strong>{nombre} {apellido}</strong>, {tipoDocumento} {dni} han sido eliminados del sistema.
          </h2>
          <p className="text-gray-700 mb-6">Presione cualquier tecla para continuar…</p>
        </div>
      </div>
    </div>
  );
}

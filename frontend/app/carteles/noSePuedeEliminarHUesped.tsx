"use client";
import { useEffect } from "react";

type Props = {
  onClose: () => void;
};

export default function NoSePuedeEliminarHuesped({ onClose }: Props) {
    useEffect(() => {
        // Función que se ejecuta cuando se aprieta una tecla
        const handleKeyDown = (event: KeyboardEvent) => {
            event.preventDefault();  // evita que el ENTER sea capturado por formularios
            onClose();
        };

        // Agregar listener
        window.addEventListener("keydown", handleKeyDown);

        // Sacar listener cuando el modal se destruye
        return () => {
        window.removeEventListener("keydown", handleKeyDown);
        };
    }, [onClose]);

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/50 z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-140 text-center">
        <h2 className="text-xl font-bold mb-4 text-indigo-950">EL HUESPED NO PUEDE SER ELIMINADO DEL SISTEMA</h2>
        <p className="text-indigo-950 text-sm">Presione cualquier tecla para continuar...</p>
      </div>
    </div>
  );
}
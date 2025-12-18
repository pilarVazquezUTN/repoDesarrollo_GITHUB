"use client";

import { useState, ChangeEvent } from "react";
import axios from "../lib/axios";

type Props = {
  onClose: () => void;
  onAceptar: (cuit: string, razonSocial: string) => void;
};

export default function FacturarUnTerceroModal({ onClose, onAceptar }: Props) {
  const [cuit, setCuit] = useState("");
  const [razonSocial, setRazonSocial] = useState("");
  const [error, setError] = useState("");
  const [buscando, setBuscando] = useState(false);

  const buscarRazonSocial = async () => {
    if (!cuit.trim()) {
      setError("El CUIT no puede estar vacío");
      return;
    }

    setBuscando(true);
    setError("");

    try {
      // Buscar responsable de pago por CUIT
      const response = await axios.get(`http://localhost:8080/responsablesPago`, {
        params: { cuit: cuit.trim() }
      });

      if (response.data && response.data.length > 0) {
        setRazonSocial(response.data[0].razonSocial || "");
      } else {
        setError("No se encontró un responsable de pago con ese CUIT");
        setRazonSocial("");
      }
    } catch (error) {
      console.error("Error al buscar CUIT:", error);
      setError("Error al buscar el CUIT. Verifique que sea correcto.");
      setRazonSocial("");
    } finally {
      setBuscando(false);
    }
  };

  const handleAceptar = () => {
    if (!cuit.trim()) {
      setError("El CUIT no puede estar vacío");
      return;
    }

    if (!razonSocial) {
      setError("Debe buscar la razón social primero");
      return;
    }

    onAceptar(cuit, razonSocial);
  };

  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center">
      {/* FONDO TENUE */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* CONTENIDO DEL CARTEL */}
      <div className="relative bg-white rounded-lg shadow-lg p-6 w-140">
        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950">Facturar a un Tercero</h2>
        
        {/* Campo para ingresar CUIT */}
        <div className="mb-4">
          <label className="text-indigo-950 font-semibold mb-2 block text-sm">CUIT:</label>
          <div className="flex gap-2">
            <input
              type="text"
              value={cuit}
              onChange={(e) => {
                setCuit(e.target.value.toUpperCase());
                setRazonSocial("");
                setError("");
              }}
              placeholder="XX-XXXXXXXX-X"
              className="flex-1 p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent border-gray-300 hover:border-indigo-400"
              onKeyDown={(e) => {
                if (e.key === 'Enter') {
                  e.preventDefault();
                  buscarRazonSocial();
                }
              }}
            />
            <button
              type="button"
              onClick={buscarRazonSocial}
              disabled={buscando}
              className="px-4 py-2 bg-indigo-950 text-white rounded-lg hover:bg-indigo-800 transition-all disabled:bg-gray-400 disabled:cursor-not-allowed"
            >
              {buscando ? "Buscando..." : "Buscar"}
            </button>
          </div>
          {error && <p className="text-red-600 text-xs mt-1">{error}</p>}
        </div>

        {/* Mostrar Razón Social */}
        {razonSocial && (
          <div className="mb-4">
            <label className="text-indigo-950 font-semibold mb-2 block text-sm">Razón Social:</label>
            <p className="p-3 bg-gray-50 border-2 border-gray-300 rounded-lg text-indigo-950 font-medium">
              {razonSocial}
            </p>
          </div>
        )}

        {/* Botones */}
        <div className="mt-6 flex justify-center gap-4">
          <button
            onClick={onClose}
            className="px-6 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold"
          >
            Cancelar
          </button>
          <button
            onClick={handleAceptar}
            disabled={!razonSocial}
            className="px-6 py-2 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold disabled:bg-gray-400 disabled:cursor-not-allowed"
          >
            Aceptar
          </button>
        </div>
      </div>
    </div>
  );
}

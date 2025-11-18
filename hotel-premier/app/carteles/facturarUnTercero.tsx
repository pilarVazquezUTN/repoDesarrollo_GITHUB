"use client";

import { useState, ChangeEvent } from "react";

type Props = {
  onClose: () => void;
};

export default function FacturarUnTerceroModal({ onClose }: Props) {
  const [cuit, setCuit] = useState("");

  const guardarCuit = (e: ChangeEvent<HTMLInputElement>) => {
    setCuit(e.target.value);
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-140">
        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950">Facturar a un Tercero</h2>
        {/*  Campo para ingresar CUIT y mostrar Razón Social  */}
        <div className="flex gap-5">
          <form className=" flex flex-col flex-1">
            <label className="text-indigo-950 flex-1 ">CUIT:</label>
            <input type="text" value={cuit} onChange={guardarCuit} placeholder="CUIT" className="p-1 border rounded mb-3 placeholder-gray-400 text-indigo-950" />
          </form>
          <strong className="text-indigo-950 flex-1">Razón Social: se completa con los datos que se saquen de la BDD</strong>
        </div>
        {/*/  Botones de Aceptar y Cancelar  */}
        <div className=" mt-6 justify-center sticky bottom-0 flex gap-4 ">
          <button onClick={onClose} className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">Cancelar</button>
          <button className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition"
            onClick={() => {
              console.log("ENVIANDO:", { cuit});
              onClose();
            }}
          >
            Aceptar
          </button>
        </div>
      </div>
    </div>
  );
}

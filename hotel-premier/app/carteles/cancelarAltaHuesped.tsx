"use client";

import Link from "next/link";

type Props = {
  onClose: () => void;
};

export default function CancelarAltaHuepsed({ onClose }: Props) {

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-140">
        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950">¿Desea cancelar el alta del huesped?</h2>
        {/*/  Botones de Aceptar y Cancelar  */}
        <div className=" mt-6 justify-center sticky bottom-0 flex gap-4 ">
            <nav> 
              <Link href="/menu"> 
                <button type="button" className="px-7 py-1 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition" onClick={onClose}>SI</button>
              </Link>
            </nav>
            <button type="button" className="px-7 py-1 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition" onClick={onClose}>NO</button>
        </div>
      </div>
    </div>
  );
}
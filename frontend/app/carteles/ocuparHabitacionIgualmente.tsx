"use client";

type Props = {
  onClose: () => void;
};

export default function CargarOtroHuesped({ onClose }: Props) {

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-140 text-center">
        <h2 className="text-xl font-bold text-center mb-4 text-indigo-950">Los dias (DIAS DESDE Y HASTA) se encuentran reservados</h2>
        <strong >Estan reservados por: NOMBRE Y APELLIDO HUESPED</strong>
        {/*/  Botones de Aceptar y Cancelar  */}
        <div className=" mt-6 justify-center sticky bottom-0 flex gap-4 ">
            <button type="button" className="px-7 py-1 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition" onClick={onClose}>Volver</button>
            <button type="button" className="px-7 py-1 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition" onClick={onClose}>Ocupar Igual</button>
        </div>
      </div>
    </div>
  );
}
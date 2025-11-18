import Tabla from "./tabla/page";

export default function BuscarHuesped() {
    return (
        <main className="flex flex-col gap-8 px-6 py-6 items-center">
            <form className="flex flex-row ">
                <label className="text-indigo-950 ">numero de la Habitacion:</label>
                <input type="text" placeholder="numero de la Habitacion" className="mr-10 p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />
                <label className="text-indigo-950">hora de salida:</label>
                <input type="text" placeholder="hora de salida" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />
            </form>
            <p className="text-indigo-950 font-bold ">seleccione una persona como responsable de pago</p>
            <Tabla />

            <div className=" mt-6 justify-center sticky bottom-0 flex gap-4 ">
                <button className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">Buscar</button>

                <button className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">Facturar a nombre de un tercero</button>
            </div>

        </main>
    );
}
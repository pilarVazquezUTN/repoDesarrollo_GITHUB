import Tabla from "../tabla/page";

export default function BuscarHuesped() {
    return (
    <main className="flex gap-8 px-6 py-6 items-start">
        {/*  Formulario a la izquierda */}
        <form className="flex flex-col  justify-center">

            <label className="text-indigo-950 font-medium mb-1">Apellido:</label>
            <input type="text" placeholder="Apellido" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

            <label className="text-indigo-950 font-medium mb-1">Nombre:</label>
            <input type="text" placeholder="Nombre" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

            <label className="text-indigo-950 font-medium mb-1">Tipo de Documento:</label>
            <select name="tipoDocumento" defaultValue="" className="p-2 border rounded mb-4 text-gray-400 focus:text-indigo-950">
                <option value="" className="text-gray-400">Seleccionar tipo</option>
                <option value="DNI">DNI</option>
                <option value="Pasaporte">Pasaporte</option>
                <option value="CUIT">CUIT</option>
            </select>

            <label className="text-indigo-950 font-medium mb-1">Número de Documento:</label>
            <input type="text" placeholder="Número de Documento" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950" />

            <button className="self-center px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">Buscar</button>

        </form>
        <section className="flex-1 flex flex-col">
            <Tabla />
            {/*  Tabla a la derecha */}
            <div className=" mt-6 justify-center sticky bottom-0 flex gap-4 ">
                <button className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">Cancelar</button>

                <button className="px-4 py-2 bg-indigo-950 text-white rounded hover:bg-indigo-800 transition">Siguiente</button>
            </div>
        </section>

    </main>
   
    );
}
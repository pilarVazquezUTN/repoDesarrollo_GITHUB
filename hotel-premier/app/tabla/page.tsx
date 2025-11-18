export default function Tabla() {
    return (
        <section className="flex-1 overflow-y-auto max-h-[400px]">
            <table className="w-full border-collapse border shadow-lg">
                <thead className="bg-indigo-950 text-white sticky top-0 ">
                    <tr>
                        <th className="p-3 border ">Seleccionar</th>
                        <th className="p-3 border">Apellido</th>
                        <th className="p-3 border">Nombre</th>
                        <th className="p-3 border">Tipo de Documento</th>
                        <th className="p-3 border">Número de Documento</th>
                    </tr>
                </thead>
                <tbody>
                    {/* Repetí esta fila para cada resultado SON EJEMPLOS EN REALIDAD LA TABLA ES DINAMICA  EN BASE A LA BDD*/}
                    <tr className="bg-white hover:bg-indigo-100">
                        <td className="p-2 border text-center"><input type="radio" name="seleccion" /></td>
                        <td className="p-2 border">Gómez</td>
                        <td className="p-2 border">Lucía</td>
                        <td className="p-2 border">DNI</td>
                        <td className="p-2 border">12345678</td>
                    </tr>
                    <tr className="bg-white hover:bg-indigo-100">
                        <td className="p-3 border text-center"><input type="radio" name="seleccion" /></td>
                        <td className="p-3 border">Gómez</td>
                        <td className="p-3 border">Lucía</td>
                        <td className="p-3 border">DNI</td>
                        <td className="p-3 border">12445678</td>
                    </tr>
                </tbody>
            </table>
        </section>
    );
}
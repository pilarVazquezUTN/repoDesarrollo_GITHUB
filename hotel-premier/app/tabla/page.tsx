export type TipoHuesped = {
    id: number;
    nombre: string;
    apellido: string;
    dni: string;
    tipoDocumento: string;
    fechaNacimiento: string;
    nacionalidad: string;
    ocupacion: string;
    email: string;
    telefono: string;

    direccionHuesped: {
        calle: string;
        numero: number;
        piso: number | null;
        departamento: string | null;
        codigoPostal: number;
        localidad: string;
        provincia: string;
        pais: string;
    };

    cuit: string;
    posicionIva: string;
    huespedID: number | null;
};

type TablaProps = {
    huespedes: TipoHuesped[];
};

export default function Tabla({huespedes}: TablaProps) {
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
                    {huespedes.map((h) => (
                        <tr key={h.huespedID?.dni} className="bg-white hover:bg-indigo-100">
                            <td className="p-2 border text-center">
                                <input type="radio" name="seleccion" />
                            </td>
                            <td className="p-2 border">{h.apellido}</td>
                            <td className="p-2 border">{h.nombre}</td>

                            {/* CAMPOS ANIDADOS CORRECTOS */}
                            <td className="p-2 border">{h.huespedID?.tipoDocumento}</td>
                            <td className="p-2 border">{h.huespedID?.dni}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </section>
    );
}

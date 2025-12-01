import { useState } from "react";

export type TipoHuesped = {
    id: number;
    nombre: string;
    apellido: string;
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
    huespedID: {
        tipoDocumento: string;
        dni: string;
    };
};

type TablaProps = {
    huespedes: TipoHuesped[];
    toggleSeleccion: (h: TipoHuesped) => void;  
    seleccionados: TipoHuesped[];               
    setSeleccionado: (h: TipoHuesped) => void;
};

type SortKey = keyof TipoHuesped | "dni" | "tipoDocumento";

export default function Tabla({ huespedes, toggleSeleccion, seleccionados }: TablaProps) {

    const [sortConfig, setSortConfig] = useState<{ key: SortKey | null; direction: 'asc' | 'desc' }>({
        key: "apellido",
        direction: 'asc'
    });

    const sortedHuespedes = [...huespedes].sort((a, b) => {
        if (!sortConfig.key) return 0;

        const key = sortConfig.key;

        let aValue: any = a;
        let bValue: any = b;

        if (key === "dni") {
            aValue = a.huespedID.dni;
            bValue = b.huespedID.dni;
        } else if (key === "tipoDocumento") {
            aValue = a.huespedID.tipoDocumento;
            bValue = b.huespedID.tipoDocumento;
        } else {
            aValue = a[key];
            bValue = b[key];
        }

        if (aValue < bValue) return sortConfig.direction === "asc" ? -1 : 1;
        if (aValue > bValue) return sortConfig.direction === "asc" ? 1 : -1;
        return 0;
    });

    const handleSort = (key: SortKey) => {
        let direction: 'asc' | 'desc' = 'asc';

        if (sortConfig.key === key && sortConfig.direction === 'asc') {
            direction = 'desc';
        }

        setSortConfig({ key, direction });
    };

    return (
        <section className="flex-1 overflow-y-auto max-h-[400px]">
            <table className="w-full border-collapse border shadow-lg">
                <thead className="bg-indigo-950 text-white sticky top-0">
                    <tr>
                        <th className="p-3 border">Seleccionar</th>
                        <th className="p-3 border" onClick={() => handleSort("apellido")}>Apellido ↑↓</th>
                        <th className="p-3 border" onClick={() => handleSort("nombre")}>Nombre ↑↓</th>
                        <th className="p-3 border" onClick={() => handleSort("tipoDocumento")}>Tipo Doc ↑↓</th>
                        <th className="p-3 border" onClick={() => handleSort("dni")}>DNI ↑↓</th>
                    </tr>
                </thead>

                <tbody>
                    {sortedHuespedes.map((h) => {
                        const seleccionado = seleccionados.some(
                            (sel) =>
                                sel.huespedID.dni === h.huespedID.dni &&
                                sel.huespedID.tipoDocumento === h.huespedID.tipoDocumento
                        );

                        return (
                            <tr key={h.huespedID.tipoDocumento + h.huespedID.dni} className="bg-white hover:bg-indigo-100">
                                <td className="p-2 border text-center">
                                    <input
                                        type="checkbox"
                                        checked={seleccionados.some(
                                            (s) =>
                                                s.huespedID.dni === h.huespedID.dni &&
                                                s.huespedID.tipoDocumento === h.huespedID.tipoDocumento
                                        )}
                                        onChange={() => toggleSeleccion(h)}
                                    />
                                </td>
                                <td className="p-2 border">{h.apellido}</td>
                                <td className="p-2 border">{h.nombre}</td>
                                <td className="p-2 border">{h.huespedID.tipoDocumento}</td>
                                <td className="p-2 border">{h.huespedID.dni}</td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </section>
    );
}

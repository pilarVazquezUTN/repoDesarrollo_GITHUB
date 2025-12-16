'use client';

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import axios from "axios";
import { TipoHuesped } from "../tabla/page";
import FacturarUnTerceroModal from "../carteles/facturarUnTercero";
import MenorEdad from "../carteles/menorEdad";
import { esSoloNumeros, validarCUIT } from "../validaciones/validaciones";

interface Consumo {
    id: number;
    descripcion: string;
    precio: number;
    fecha: string;
}

interface Estadia {
    id_estadia: number;
    checkin: string;
    checkout: string;
    precioTotal: number;
}

interface ItemFacturar {
    id: number;
    tipo: 'estadia' | 'consumo';
    descripcion: string;
    precio: number;
    estadiaId?: number;
}

export default function Facturar() {
    const router = useRouter();
    
    // Estados del formulario
    const [nroHabitacion, setNroHabitacion] = useState("");
    const [horaSalida, setHoraSalida] = useState("");
    
    // Estados de errores
    const [errores, setErrores] = useState<string[]>([]);
    const [errorNroHabitacion, setErrorNroHabitacion] = useState(false);
    const [errorHoraSalida, setErrorHoraSalida] = useState(false);
    
    // Estados de datos
    const [huespedes, setHuespedes] = useState<TipoHuesped[]>([]);
    const [huespedSeleccionado, setHuespedSeleccionado] = useState<TipoHuesped | null>(null);
    const [estadia, setEstadia] = useState<Estadia | null>(null);
    const [consumos, setConsumos] = useState<Consumo[]>([]);
    const [itemsFacturar, setItemsFacturar] = useState<ItemFacturar[]>([]);
    const [itemsSeleccionados, setItemsSeleccionados] = useState<number[]>([]);
    
    // Estados de UI
    const [mostrarTabla, setMostrarTabla] = useState(false);
    const [mostrarItems, setMostrarItems] = useState(false);
    const [mostrarTercero, setMostrarTercero] = useState(false);
    const [mostrarMenorEdad, setMostrarMenorEdad] = useState(false);
    
    // Estado del responsable de pago
    const [responsablePago, setResponsablePago] = useState<{
        id: number;
        nombre: string;
        tipoFactura: 'A' | 'B';
        esTercero: boolean;
    } | null>(null);

    // Validar número de habitación
    const validarNroHabitacion = (valor: string) => {
        if (!valor.trim()) {
            setErrorNroHabitacion(true);
            return false;
        }
        if (!esSoloNumeros(valor)) {
            setErrorNroHabitacion(true);
            return false;
        }
        setErrorNroHabitacion(false);
        return true;
    };

    // Validar hora de salida
    const validarHoraSalida = (valor: string) => {
        if (!valor.trim()) {
            setErrorHoraSalida(true);
            return false;
        }
        // Validar formato de hora (HH:MM)
        const horaRegex = /^([0-1][0-9]|2[0-3]):[0-5][0-9]$/;
        if (!horaRegex.test(valor)) {
            setErrorHoraSalida(true);
            return false;
        }
        setErrorHoraSalida(false);
        return true;
    };

    // Calcular edad
    const calcularEdad = (fechaNacimiento: string): number => {
        const hoy = new Date();
        const nacimiento = new Date(fechaNacimiento);
        let edad = hoy.getFullYear() - nacimiento.getFullYear();
        const mes = hoy.getMonth() - nacimiento.getMonth();
        if (mes < 0 || (mes === 0 && hoy.getDate() < nacimiento.getDate())) {
            edad--;
        }
        return edad;
    };

    // Buscar ocupantes de la habitación
    const buscarHuespedes = async (e: React.FormEvent) => {
        e.preventDefault();
        
        const erroresLista: string[] = [];
        
        // Validar campos
        if (!validarNroHabitacion(nroHabitacion)) {
            erroresLista.push("Número de habitación faltante o incorrecto");
        }
        if (!validarHoraSalida(horaSalida)) {
            erroresLista.push("Hora de salida faltante o incorrecta");
        }
        
        if (erroresLista.length > 0) {
            setErrores(erroresLista);
            // Enfocar en el primer campo con error
            if (!nroHabitacion.trim() || !esSoloNumeros(nroHabitacion)) {
                document.querySelector<HTMLInputElement>('input[name="nroHabitacion"]')?.focus();
            } else {
                document.querySelector<HTMLInputElement>('input[name="horaSalida"]')?.focus();
            }
            return;
        }

        setErrores([]);

        try {
            // Buscar estadía en curso de la habitación (incluye huéspedes y consumos)
            const responseEstadia = await axios.get(`http://localhost:8080/estadias/enCurso/${nroHabitacion}`);
            
            if (!responseEstadia.data) {
                setErrores(["La habitación no está ocupada"]);
                return;
            }

            const estadiaData = responseEstadia.data;
            
            // Console log: datos recibidos del backend
            console.log("=== DATOS RECIBIDOS DEL BACKEND ===");
            console.log("Estadía completa:", JSON.stringify(estadiaData, null, 2));
            console.log("=====================================");

            // Extraer huéspedes de la estadía
            const huespedesData = estadiaData.listahuesped || [];
            if (huespedesData.length === 0) {
                setErrores(["La habitación no tiene huéspedes asignados"]);
                return;
            }

            // Mapear huéspedes al formato TipoHuesped
            const huespedesMapeados: TipoHuesped[] = huespedesData.map((h: any) => ({
                id: h.id || 0,
                nombre: h.nombre || "",
                apellido: h.apellido || "",
                fechaNacimiento: h.fechaNacimiento || "",
                nacionalidad: h.nacionalidad || "",
                ocupacion: h.ocupacion || "",
                email: h.email || "",
                telefono: h.telefono || "",
                direccionHuesped: h.direccion || {},
                cuit: h.cuit || "",
                posicionIva: h.posicionIva || "CF",
                huespedID: {
                    tipoDocumento: h.huespedID?.tipoDocumento || h.tipoDocumento || "",
                    dni: h.huespedID?.dni || h.dni || ""
                }
            }));

            setHuespedes(huespedesMapeados);
            setMostrarTabla(true);
            setMostrarItems(false);
            setHuespedSeleccionado(null);
            setResponsablePago(null);

            // Guardar estadía
            // El precio puede venir en diferentes campos o calcularse después
            const precioEstadia = estadiaData.precioTotal || estadiaData.precio || 0;
            setEstadia({
                id_estadia: estadiaData.id_estadia || estadiaData.id,
                checkin: estadiaData.checkin,
                checkout: estadiaData.checkout || null,
                precioTotal: precioEstadia
            });

            // Extraer consumos de la estadía
            const consumosData = estadiaData.listaconsumos || [];
            const consumosMapeados: Consumo[] = consumosData.map((c: any, index: number) => ({
                id: c.id || index + 1,
                descripcion: c.descripcion || `Consumo ${index + 1}`,
                precio: c.price || c.precio || 0,
                fecha: c.fecha || new Date().toISOString().split('T')[0]
            }));

            setConsumos(consumosMapeados);
            
            // Console log: datos procesados
            console.log("=== DATOS PROCESADOS ===");
            console.log("Huéspedes mapeados:", huespedesMapeados.length);
            console.log("Consumos mapeados:", consumosMapeados.length);
            console.log("Estadía guardada:", {
                id_estadia: estadiaData.id_estadia || estadiaData.id,
                checkin: estadiaData.checkin,
                checkout: estadiaData.checkout,
                precioTotal: precioEstadia
            });
            console.log("========================");

        } catch (error: any) {
            console.error("Error al buscar estadía:", error);
            if (error.response?.status === 404) {
                setErrores(["La habitación no está ocupada"]);
            } else {
                setErrores(["Error al cargar los datos de la habitación"]);
            }
        }
    };

    // Seleccionar responsable de pago
    const seleccionarResponsable = (huesped: TipoHuesped) => {
        // Validar edad primero
        if (huesped.fechaNacimiento) {
            const edad = calcularEdad(huesped.fechaNacimiento);
            if (edad < 18) {
                setMostrarMenorEdad(true);
                return;
            }
        }
        
        setHuespedSeleccionado(huesped);

        // Determinar tipo de factura según posición IVA
        const tipoFactura = huesped.posicionIva === "RESPONSABLE INSCRIPTO" ? 'A' : 'B';
        
        setResponsablePago({
            id: huesped.id,
            nombre: `${huesped.nombre} ${huesped.apellido}`,
            tipoFactura,
            esTercero: false
        });

        // Preparar items para facturar
        prepararItems();
    };

    // Preparar items a facturar
    const prepararItems = () => {
        const items: ItemFacturar[] = [];
        
        // Agregar estadía si existe
        if (estadia && estadia.id_estadia) {
            const checkoutDisplay = estadia.checkout || "En curso";
            items.push({
                id: 1,
                tipo: 'estadia',
                descripcion: `Estadía (${estadia.checkin} - ${checkoutDisplay})`,
                precio: estadia.precioTotal || 0, // Si no hay precio, se puede calcular o mostrar 0
                estadiaId: estadia.id_estadia
            });
        }

        // Agregar consumos pendientes (solo los que no estén facturados)
        consumos.forEach((consumo, index) => {
            // Asumimos que todos los consumos están pendientes
            // Si el backend marca cuáles están facturados, filtrar aquí
            items.push({
                id: items.length + 1,
                tipo: 'consumo',
                descripcion: consumo.descripcion || `Consumo ${index + 1}`,
                precio: consumo.precio || 0,
            });
        });

        setItemsFacturar(items);
        setItemsSeleccionados(items.map(item => item.id)); // Seleccionar todos por defecto
        setMostrarItems(true);
    };

    // Manejar facturación a tercero
    const handleFacturarTercero = (cuit: string, razonSocial: string) => {
        setMostrarTercero(false);
        
        // Buscar o crear responsable de pago
        axios.get(`http://localhost:8080/responsablesPago`, {
            params: { cuit: cuit.trim() }
        }).then(response => {
            if (response.data && response.data.length > 0) {
                const responsable = response.data[0];
                setResponsablePago({
                    id: responsable.id,
                    nombre: razonSocial,
                    tipoFactura: 'A', // Terceros siempre factura A
                    esTercero: true
                });
                prepararItems();
            } else {
                // Si no existe, redirigir a dar alta
                router.push('/darAltaResponsablePago');
            }
        }).catch(error => {
            console.error("Error al buscar responsable:", error);
            // Si no existe, redirigir a dar alta
            router.push('/darAltaResponsablePago');
        });
    };

    // Toggle selección de item
    const toggleItem = (itemId: number) => {
        setItemsSeleccionados(prev => 
            prev.includes(itemId) 
                ? prev.filter(id => id !== itemId)
                : [...prev, itemId]
        );
    };

    // Calcular total
    const calcularTotal = (): number => {
        return itemsSeleccionados.reduce((total, itemId) => {
            const item = itemsFacturar.find(i => i.id === itemId);
            return total + (item?.precio || 0);
        }, 0);
    };

    // Calcular IVA
    const calcularIVA = (): number => {
        if (!responsablePago || responsablePago.tipoFactura === 'B') {
            return 0;
        }
        return calcularTotal() * 0.21; // 21% IVA
    };

    // Generar factura
    const generarFactura = async () => {
        if (itemsSeleccionados.length === 0) {
            setErrores(["Debe seleccionar al menos un ítem para facturar"]);
            return;
        }

        if (!responsablePago || !estadia) {
            setErrores(["Faltan datos para generar la factura"]);
            return;
        }

        // Obtener IDs de consumos seleccionados
        const consumosIds: number[] = [];
        itemsSeleccionados.forEach(id => {
            const item = itemsFacturar.find(i => i.id === id);
            if (item?.tipo === 'consumo') {
                // Buscar el consumo original por descripción/precio
                const consumo = consumos.find(c => 
                    (c.descripcion === item.descripcion || item.descripcion.includes(c.descripcion)) && 
                    Math.abs(c.precio - item.precio) < 0.01
                );
                if (consumo && consumo.id) {
                    consumosIds.push(consumo.id);
                }
            }
        });

        // Construir fecha/hora de checkout
        const fechaCheckout = new Date().toISOString().split('T')[0];
        const fechaHoraCheckout = `${fechaCheckout}T${horaSalida}:00`;

        const facturaDTO = {
            tipo: responsablePago.tipoFactura,
            fechaHoraCheckoutReal: fechaHoraCheckout,
            consumosIds: consumosIds,
            totalEstimado: calcularTotal() + calcularIVA(),
            estadia: {
                id_estadia: estadia.id_estadia
            },
            responsablepago: {
                id: responsablePago.id
            }
        };

        // Console log: datos que se envían al backend
        console.log("=== DATOS ENVIADOS AL BACKEND ===");
        console.log("Factura DTO:", JSON.stringify(facturaDTO, null, 2));
        console.log("Estadía actual:", JSON.stringify(estadia, null, 2));
        console.log("Consumos seleccionados IDs:", consumosIds);
        console.log("Items seleccionados:", itemsSeleccionados);
        console.log("Items facturar:", JSON.stringify(itemsFacturar, null, 2));
        console.log("Consumos disponibles:", JSON.stringify(consumos, null, 2));
        console.log("=================================");

        try {
            // Si la estadía no tiene checkout, actualizarla primero
            if (estadia && !estadia.checkout) {
                console.log("=== ACTUALIZANDO ESTADÍA CON CHECKOUT ===");
                const fechaCheckoutUpdate = new Date().toISOString().split('T')[0];
                try {
                    await axios.put(`http://localhost:8080/estadias/${estadia.id_estadia}`, {
                        checkout: fechaCheckoutUpdate
                    });
                    console.log("Estadía actualizada con checkout:", fechaCheckoutUpdate);
                } catch (updateError: any) {
                    console.error("Error al actualizar estadía:", updateError);
                    // Continuar de todas formas, el backend puede manejarlo
                }
            }

            const response = await axios.post("http://localhost:8080/facturas", facturaDTO);
            
            if (response.status === 200 || response.status === 201) {
                // Éxito - redirigir o mostrar mensaje
                alert("Factura generada exitosamente");
                // Limpiar formulario
                setNroHabitacion("");
                setHoraSalida("");
                setHuespedes([]);
                setMostrarTabla(false);
                setMostrarItems(false);
                setResponsablePago(null);
                setErrores([]);
            }
        } catch (error: any) {
            console.error("Error al generar factura:", error);
            if (error.response?.data) {
                console.error("Detalles del error del backend:", error.response.data);
                setErrores([error.response.data || "Error al generar la factura"]);
            } else {
                setErrores(["Error al generar la factura"]);
            }
        }
    };

    // Manejar Shift+Tab
    const handleKeyDownInput = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === "Tab" && e.shiftKey) {
            e.preventDefault();
            const form = e.currentTarget.form;
            if (form) {
                const inputs = Array.from(form.querySelectorAll("input"));
                const index = inputs.indexOf(e.currentTarget);
                if (index > 0) {
                    (inputs[index - 1] as HTMLElement).focus();
                }
            }
        }
    };

    // Manejar Enter en botones
    const handleKeyDown = (e: React.KeyboardEvent, action: () => void) => {
        if (e.key === 'Enter') {
            e.preventDefault();
            action();
        }
    };

    return (
        <main className="flex gap-8 px-8 py-8 items-start bg-gradient-to-br from-gray-50 to-gray-100 min-h-screen">
            <div className="w-full max-w-7xl mx-auto">
                {/* TÍTULO */}
                <div className="bg-white rounded-xl shadow-lg p-6 mb-6">
                    <h1 className="text-3xl font-bold text-indigo-950 mb-2">Facturar</h1>
                    <p className="text-gray-600">Complete los datos para generar la factura de una habitación</p>
                </div>

                {/* FORMULARIO DE BÚSQUEDA */}
                <div className="bg-white rounded-xl shadow-lg p-6 mb-6">
                    <h2 className="text-xl font-bold text-indigo-950 mb-4 pb-3 border-b-2 border-indigo-200">
                        Datos de Búsqueda
                    </h2>
                    <form onSubmit={buscarHuespedes} className="flex flex-col space-y-4">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">
                                    Número de Habitación*:
                                </label>
                                <input
                                    name="nroHabitacion"
                                    type="text"
                                    value={nroHabitacion}
                                    onChange={(e) => {
                                        setNroHabitacion(e.target.value);
                                        setErrorNroHabitacion(false);
                                    }}
                                    onBlur={(e) => validarNroHabitacion(e.target.value)}
                                    onKeyDown={handleKeyDownInput}
                                    className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                                        errorNroHabitacion
                                            ? "border-red-500 bg-red-50"
                                            : "border-gray-300 hover:border-indigo-400"
                                    }`}
                                    placeholder="Número de habitación"
                                />
                                {errorNroHabitacion && (
                                    <p className="text-red-600 text-xs mt-1">Número de habitación inválido</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">
                                    Hora de Salida*:
                                </label>
                                <input
                                    name="horaSalida"
                                    type="time"
                                    value={horaSalida}
                                    onChange={(e) => {
                                        setHoraSalida(e.target.value);
                                        setErrorHoraSalida(false);
                                    }}
                                    onBlur={(e) => validarHoraSalida(e.target.value)}
                                    onKeyDown={handleKeyDownInput}
                                    className={`w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                                        errorHoraSalida
                                            ? "border-red-500 bg-red-50"
                                            : "border-gray-300 hover:border-indigo-400"
                                    }`}
                                />
                                {errorHoraSalida && (
                                    <p className="text-red-600 text-xs mt-1">Hora de salida inválida</p>
                                )}
                            </div>
                        </div>

                        {/* MENSAJES DE ERROR */}
                        {errores.length > 0 && (
                            <div className="bg-red-50 border-l-4 border-red-500 text-red-700 p-4 rounded-lg">
                                <p className="font-semibold mb-2">Errores encontrados:</p>
                                <ul className="list-disc list-inside">
                                    {errores.map((error, index) => (
                                        <li key={index} className="text-sm">{error}</li>
                                    ))}
                                </ul>
                            </div>
                        )}

                        <button
                            type="submit"
                            onKeyDown={(e) => handleKeyDown(e, () => buscarHuespedes(e as any))}
                            className="w-full md:w-auto px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105"
                        >
                            Buscar
                        </button>
                    </form>
                </div>

                {/* TABLA DE HUÉSPEDES */}
                {mostrarTabla && (
                    <div className="bg-white rounded-xl shadow-lg p-6 mb-6">
                        <h2 className="text-xl font-bold text-indigo-950 mb-4 pb-3 border-b-2 border-indigo-200">
                            Seleccione una persona como responsable de pago
                        </h2>
                        <div className="max-h-[400px] overflow-y-auto">
                            <table className="w-full border-collapse">
                                <thead className="bg-gradient-to-r from-indigo-950 to-indigo-900 text-white sticky top-0 z-10 shadow-md">
                                    <tr>
                                        <th className="p-4 font-semibold text-left">Seleccionar</th>
                                        <th className="p-4 font-semibold text-left">Apellido</th>
                                        <th className="p-4 font-semibold text-left">Nombre</th>
                                        <th className="p-4 font-semibold text-left">Tipo Documento</th>
                                        <th className="p-4 font-semibold text-left">Número Documento</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {huespedes.map((h) => {
                                        const isSelected = huespedSeleccionado?.id === h.id;
                                        return (
                                            <tr
                                                key={`${h.huespedID.tipoDocumento}-${h.huespedID.dni}`}
                                                className={`hover:bg-indigo-50 transition-colors ${
                                                    isSelected ? "bg-indigo-100" : "bg-white"
                                                }`}
                                            >
                                                <td className="p-4 border-b border-gray-200 text-center">
                                                    <input
                                                        type="radio"
                                                        name="seleccion"
                                                        checked={isSelected}
                                                        onChange={() => seleccionarResponsable(h)}
                                                        className="w-4 h-4 text-indigo-950 focus:ring-indigo-500"
                                                    />
                                                </td>
                                                <td className="p-4 border-b border-gray-200 font-medium text-indigo-950">
                                                    {h.apellido}
                                                </td>
                                                <td className="p-4 border-b border-gray-200 text-gray-700">
                                                    {h.nombre}
                                                </td>
                                                <td className="p-4 border-b border-gray-200 text-gray-700">
                                                    {h.huespedID.tipoDocumento}
                                                </td>
                                                <td className="p-4 border-b border-gray-200 text-gray-700">
                                                    {h.huespedID.dni}
                                                </td>
                                            </tr>
                                        );
                                    })}
                                </tbody>
                            </table>
                        </div>

                        <div className="mt-6 flex justify-center gap-4">
                            <button
                                type="button"
                                onClick={() => setMostrarTercero(true)}
                                className="px-6 py-3 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-all shadow-md hover:shadow-lg font-semibold"
                            >
                                Facturar a un tercero
                            </button>
                        </div>
                    </div>
                )}

                {/* ITEMS A FACTURAR */}
                {mostrarItems && responsablePago && (
                    <div className="bg-white rounded-xl shadow-lg p-6 mb-6">
                        <h2 className="text-xl font-bold text-indigo-950 mb-4 pb-3 border-b-2 border-indigo-200">
                            Items a Facturar
                        </h2>

                        <div className="mb-4 p-4 bg-indigo-50 rounded-lg">
                            <p className="text-indigo-950 font-semibold">
                                Responsable de Pago: <span className="font-normal">{responsablePago.nombre}</span>
                            </p>
                            <p className="text-indigo-950 font-semibold mt-2">
                                Tipo de Factura: <span className="font-normal">{responsablePago.tipoFactura}</span>
                            </p>
                        </div>

                        <div className="space-y-3 mb-6">
                            {itemsFacturar.map((item) => (
                                <label
                                    key={item.id}
                                    className="flex items-center justify-between p-4 border-2 border-gray-200 rounded-lg hover:border-indigo-400 transition-colors cursor-pointer"
                                >
                                    <div className="flex items-center gap-3">
                                        <input
                                            type="checkbox"
                                            checked={itemsSeleccionados.includes(item.id)}
                                            onChange={() => toggleItem(item.id)}
                                            className="w-5 h-5 text-indigo-950 focus:ring-indigo-500"
                                        />
                                        <span className="text-indigo-950 font-medium">{item.descripcion}</span>
                                    </div>
                                    <span className="text-indigo-950 font-semibold">${item.precio.toFixed(2)}</span>
                                </label>
                            ))}
                        </div>

                        {/* RESUMEN */}
                        <div className="border-t-2 border-indigo-200 pt-4 space-y-2">
                            <div className="flex justify-between text-gray-700">
                                <span>Subtotal:</span>
                                <span>${calcularTotal().toFixed(2)}</span>
                            </div>
                            {responsablePago.tipoFactura === 'A' && (
                                <div className="flex justify-between text-gray-700">
                                    <span>IVA (21%):</span>
                                    <span>${calcularIVA().toFixed(2)}</span>
                                </div>
                            )}
                            <div className="flex justify-between text-2xl font-bold text-indigo-950 pt-2 border-t-2 border-indigo-200">
                                <span>Total:</span>
                                <span>${(calcularTotal() + calcularIVA()).toFixed(2)}</span>
                            </div>
                        </div>

                        <div className="mt-6 flex justify-center gap-4">
                            <Link href="/menu">
                                <button
                                    type="button"
                                    className="px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold"
                                >
                                    Cancelar
                                </button>
                            </Link>
                            <button
                                type="button"
                                onClick={generarFactura}
                                onKeyDown={(e) => handleKeyDown(e, generarFactura)}
                                className="px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105"
                            >
                                Aceptar
                            </button>
                        </div>
                    </div>
                )}
            </div>

            {/* CARTELES */}
            {mostrarTercero && (
                <FacturarUnTerceroModal
                    onClose={() => setMostrarTercero(false)}
                    onAceptar={handleFacturarTercero}
                />
            )}

            {mostrarMenorEdad && (
                <MenorEdad onClose={() => setMostrarMenorEdad(false)} />
            )}
        </main>
    );
}

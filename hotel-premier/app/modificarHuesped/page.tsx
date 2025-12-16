'use client';

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import axios from "axios";
import { validarFormularioHuesped } from "../validaciones/validaciones";
import { TipoHuesped } from "../tabla/page";
import CancelarModificacion from "../carteles/cancelarModificacion";
import ModificacionExitosa from "../carteles/modificacionExitosa";
import ErrorDniExistente from "../carteles/errorDniExistente";
import ConfirmarEliminarHuesped from "../carteles/confirmarEliminarHuesped";
import HuespedEliminado from "../carteles/huespedEliminado";
import NoSePuedeEliminarHuesped from "../carteles/noSePuedeEliminarHUesped";

export default function ModificarHuesped() {
    const router = useRouter();
    
    // Estados de carteles
    const [mostrarCancelar, setMostrarCancelar] = useState(false);
    const [mostrarExito, setMostrarExito] = useState(false);
    const [mostrarErrorDni, setMostrarErrorDni] = useState(false);
    const [mostrarConfirmarEliminar, setMostrarConfirmarEliminar] = useState(false);
    const [mostrarEliminado, setMostrarEliminado] = useState(false);
    const [mostrarNoSePuedeEliminar, setMostrarNoSePuedeEliminar] = useState(false);
    
    // Estado del huésped original (para comparar DNI)
    const [huespedOriginal, setHuespedOriginal] = useState<TipoHuesped | null>(null);
    
    // Estados de errores
    const [errores, setErrores] = useState<string[]>([]);
    const [erroresTipo, setErroresTipo] = useState<string[]>([]);
    
    // Estado del formulario
    const [formData, setFormData] = useState({
        apellido: "",
        nombre: "",
        tipoDocumento: "",
        dni: "",
        cuit: "",
        posicionIva: "",
        calle: "",
        numero: "",
        departamento: "",
        piso: "",
        codigoPostal: "",
        localidad: "",
        provincia: "",
        pais: "",
        fechaNacimiento: "",
        telefono: "",
        email: "",
        ocupacion: "",
        nacionalidad: "",
    });

    // Cargar huésped desde sessionStorage al montar
    useEffect(() => {
        const huespedStr = sessionStorage.getItem('huespedSeleccionado');
        if (!huespedStr) {
            router.push('/buscarHuesped');
            return;
        }

        try {
            const huesped: TipoHuesped = JSON.parse(huespedStr);
            setHuespedOriginal(huesped);
            
            // Cargar datos en el formulario
            setFormData({
                apellido: huesped.apellido || "",
                nombre: huesped.nombre || "",
                tipoDocumento: huesped.huespedID.tipoDocumento || "",
                dni: huesped.huespedID.dni || "",
                cuit: huesped.cuit || "",
                posicionIva: huesped.posicionIva || "CONSUMIDOR FINAL",
                calle: huesped.direccionHuesped?.calle || "",
                numero: huesped.direccionHuesped?.numero?.toString() || "",
                departamento: huesped.direccionHuesped?.departamento || "",
                piso: huesped.direccionHuesped?.piso?.toString() || "",
                codigoPostal: huesped.direccionHuesped?.codigoPostal?.toString() || "",
                localidad: huesped.direccionHuesped?.localidad || "",
                provincia: huesped.direccionHuesped?.provincia || "",
                pais: huesped.direccionHuesped?.pais || "",
                fechaNacimiento: huesped.fechaNacimiento || "",
                telefono: huesped.telefono || "",
                email: huesped.email || "",
                ocupacion: huesped.ocupacion || "",
                nacionalidad: huesped.nacionalidad || "",
            });
        } catch (error) {
            console.error("Error al cargar huésped:", error);
            router.push('/buscarHuesped');
        }
    }, [router]);

    // Manejar cambios en los campos
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        const valorMayus = name === "email" || name === "fechaNacimiento" ? value : value.toUpperCase();
        setFormData({ ...formData, [name]: valorMayus });
    };

    // Manejar blur para validaciones
    const handleBlur = (e: React.FocusEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name } = e.target;
        const { errores, erroresTipo } = validarFormularioHuesped(formData);

        setErrores(prev => prev.filter(campo => campo !== name));
        setErroresTipo(prev => prev.filter(campo => campo !== name));

        if (errores.includes(name)) {
            setErrores(prev => [...prev.filter(c => c !== name), name]);
        }
        if (erroresTipo.includes(name)) {
            setErroresTipo(prev => [...prev.filter(c => c !== name), name]);
        }
    };

    // Clase para inputs con errores
    const getInputClass = (campo: string) => {
        const baseClass = "w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent";
        if (errores.includes(campo) || erroresTipo.includes(campo)) {
            return `${baseClass} border-red-500 bg-red-50`;
        }
        return `${baseClass} border-gray-300 hover:border-indigo-400`;
    };

    // Manejar Shift+Tab
    const handleKeyDownInput = (e: React.KeyboardEvent<HTMLInputElement | HTMLSelectElement>) => {
        if (e.key === "Tab" && e.shiftKey) {
            e.preventDefault();
            const form = e.currentTarget.form;
            if (form) {
                const inputs = Array.from(form.querySelectorAll("input, select"));
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

    // Modificar huésped
    const modificarHuesped = async () => {
        const payload = {
            huespedID: {
                tipoDocumento: formData.tipoDocumento,
                dni: formData.dni,
            },
            nombre: formData.nombre,
            apellido: formData.apellido,
            cuit: formData.cuit,
            posicionIva: formData.posicionIva,
            fechaNacimiento: formData.fechaNacimiento,
            telefono: formData.telefono,
            email: formData.email,
            ocupacion: formData.ocupacion,
            nacionalidad: formData.nacionalidad,
            direccion: {
                calle: formData.calle,
                numero: formData.numero ? parseInt(formData.numero) : null,
                departamento: formData.departamento,
                piso: formData.piso ? parseInt(formData.piso) : null,
                codigoPostal: formData.codigoPostal ? parseInt(formData.codigoPostal) : null,
                localidad: formData.localidad,
                provincia: formData.provincia,
                pais: formData.pais,
            },
        };

        try {
            await axios.put("http://localhost:8080/huespedes/modificar", payload);
            setErrores([]);
            setErroresTipo([]);
            setMostrarExito(true);
        } catch (err) {
            console.error("Error al modificar:", err);
            alert("Error al conectar con el servidor.");
        }
    };

    // Guardar sin validar DNI (cuando se acepta igualmente)
    const handleGuardarSinValidarDni = async () => {
        setMostrarErrorDni(false);
        setErrores([]);
        await modificarHuesped();
    };

    // Manejar envío del formulario
    const handleSiguiente = async () => {
        const { errores: erroresValidacion, erroresTipo: erroresTipoValidacion } = validarFormularioHuesped(formData);

        setErrores(erroresValidacion);
        setErroresTipo(erroresTipoValidacion);

        // Validar campos obligatorios primero (paso 2.A)
        if (erroresValidacion.length > 0 || erroresTipoValidacion.length > 0) {
            return;
        }

        // Verificar si el DNI cambió y ya existe (paso 2.B)
        const dniCambio = formData.tipoDocumento !== huespedOriginal?.huespedID.tipoDocumento || 
                          formData.dni !== huespedOriginal?.huespedID.dni;

        if (dniCambio) {
            try {
                const existe = await axios.get("http://localhost:8080/huespedes", {
                    params: {
                        dni: formData.dni,
                        tipoDocumento: formData.tipoDocumento
                    }
                });

                if (existe.data.length > 0) {
                    setMostrarErrorDni(true);
                    return;
                }
            } catch (error) {
                console.error("Error verificando DNI:", error);
            }
        }

        // Si todo está OK, modificar
        await modificarHuesped();
    };

    // Verificar si el huésped se ha alojado
    const verificarAlojamiento = async () => {
        if (!huespedOriginal) return false;
        
        try {
            // Intentar verificar si el huésped tiene reservas o estadías
            // Esto puede variar según la implementación del backend
            try {
                const response = await axios.get(`http://localhost:8080/huespedes/${huespedOriginal.huespedID.tipoDocumento}/${huespedOriginal.huespedID.dni}/tieneAlojamientos`);
                return response.data === true || response.data === "true";
            } catch {
                // Si ese endpoint no existe, intentar verificar por reservas
                try {
                    const reservas = await axios.get(`http://localhost:8080/reservas`, {
                        params: {
                            tipoDocumento: huespedOriginal.huespedID.tipoDocumento,
                            dni: huespedOriginal.huespedID.dni
                        }
                    });
                    return reservas.data && reservas.data.length > 0;
                } catch {
                    // Si no hay forma de verificar, asumimos que no se ha alojado
                    return false;
                }
            }
        } catch (error) {
            console.error("Error verificando alojamientos:", error);
            return false;
        }
    };

    // Manejar borrado
    const handleBorrar = async () => {
        if (!huespedOriginal) return;

        // Verificar si se ha alojado
        const tieneAlojamientos = await verificarAlojamiento();
        
        if (tieneAlojamientos) {
            setMostrarConfirmarEliminar(false);
            setMostrarNoSePuedeEliminar(true);
            return;
        }

        // Mostrar confirmación
        setMostrarConfirmarEliminar(true);
    };

    // Confirmar eliminación
    const confirmarEliminacion = async () => {
        if (!huespedOriginal) return;

        try {
            await axios.delete(
                `http://localhost:8080/huespedes/${huespedOriginal.huespedID.tipoDocumento}/${huespedOriginal.huespedID.dni}`
            );
            setMostrarConfirmarEliminar(false);
            setMostrarEliminado(true);
        } catch (error) {
            console.error("Error al eliminar:", error);
            alert("Error al eliminar el huésped.");
        }
    };

    // Cerrar cartel de eliminado y volver al menú
    const handleCerrarEliminado = () => {
        sessionStorage.removeItem('huespedSeleccionado');
        setMostrarEliminado(false);
        router.push('/menu');
    };

    if (!huespedOriginal) {
        return (
            <main className="flex items-center justify-center min-h-screen bg-gradient-to-br from-gray-50 to-gray-100">
                <div className="text-center">
                    <p className="text-gray-600">Cargando datos del huésped...</p>
                </div>
            </main>
        );
    }

    return (
        <main className="flex gap-8 px-8 py-8 items-start bg-gradient-to-br from-gray-50 to-gray-100 min-h-screen">
            <div className="w-full max-w-7xl mx-auto">
                {/* TÍTULO */}
                <div className="bg-white rounded-xl shadow-lg p-6 mb-6">
                    <h1 className="text-3xl font-bold text-indigo-950 mb-2">Modificar Huésped</h1>
                    <p className="text-gray-600">Modifique los datos que desee cambiar y presione "Siguiente"</p>
                </div>

                {/* FORMULARIOS */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    {/* FORMULARIO 1 - Datos Personales */}
                    <div className="bg-white rounded-xl shadow-lg p-6">
                        <h2 className="text-xl font-bold text-indigo-950 mb-4 pb-3 border-b-2 border-indigo-200">
                            Datos Personales
                        </h2>
                        <form className="flex flex-col space-y-4">
                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Apellido*:</label>
                                <input
                                    name="apellido"
                                    value={formData.apellido}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("apellido")}
                                    placeholder="Apellido"
                                />
                                {erroresTipo.includes("apellido") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese solo letras.</p>
                                )}
                                {errores.includes("apellido") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Nombre*:</label>
                                <input
                                    name="nombre"
                                    value={formData.nombre}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("nombre")}
                                    placeholder="Nombre"
                                />
                                {erroresTipo.includes("nombre") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese solo letras.</p>
                                )}
                                {errores.includes("nombre") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Tipo de Documento*:</label>
                                <select
                                    name="tipoDocumento"
                                    value={formData.tipoDocumento}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    className={getInputClass("tipoDocumento")}
                                >
                                    <option value="">Seleccionar tipo</option>
                                    <option value="DNI">DNI</option>
                                    <option value="LE">LE</option>
                                    <option value="LC">LC</option>
                                    <option value="PASAPORTE">Pasaporte</option>
                                    <option value="OTRO">Otro</option>
                                </select>
                                {errores.includes("tipoDocumento") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Número de Documento*:</label>
                                <input
                                    name="dni"
                                    value={formData.dni}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("dni")}
                                    placeholder="Número de Documento"
                                />
                                {erroresTipo.includes("dni") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese un DNI válido.</p>
                                )}
                                {errores.includes("dni") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">CUIT:</label>
                                <input
                                    name="cuit"
                                    value={formData.cuit}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("cuit")}
                                    placeholder="XX-XXXXXXXX-X"
                                />
                                {erroresTipo.includes("cuit") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese un CUIT válido.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Posición frente al IVA*:</label>
                                <input
                                    name="posicionIva"
                                    value={formData.posicionIva}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("posicionIva")}
                                    placeholder="Ej: CONSUMIDOR FINAL"
                                />
                                {erroresTipo.includes("posicionIva") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese solo letras.</p>
                                )}
                                {errores.includes("posicionIva") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>
                        </form>
                    </div>

                    {/* FORMULARIO 2 - Dirección */}
                    <div className="bg-white rounded-xl shadow-lg p-6">
                        <h2 className="text-xl font-bold text-indigo-950 mb-4 pb-3 border-b-2 border-indigo-200">
                            Dirección
                        </h2>
                        <form className="flex flex-col space-y-4">
                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Calle*:</label>
                                <input
                                    name="calle"
                                    value={formData.calle}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("calle")}
                                    placeholder="Calle"
                                />
                                {erroresTipo.includes("calle") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese solo letras.</p>
                                )}
                                {errores.includes("calle") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Número*:</label>
                                <input
                                    name="numero"
                                    value={formData.numero}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("numero")}
                                    placeholder="Número"
                                />
                                {erroresTipo.includes("numero") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese solo números.</p>
                                )}
                                {errores.includes("numero") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Departamento:</label>
                                <input
                                    name="departamento"
                                    value={formData.departamento}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className="w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent border-gray-300 hover:border-indigo-400"
                                    placeholder="Departamento"
                                />
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Piso:</label>
                                <input
                                    name="piso"
                                    value={formData.piso}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className="w-full p-3 border-2 rounded-lg text-indigo-950 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent border-gray-300 hover:border-indigo-400"
                                    placeholder="Piso"
                                />
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Código Postal*:</label>
                                <input
                                    name="codigoPostal"
                                    value={formData.codigoPostal}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("codigoPostal")}
                                    placeholder="Código Postal"
                                />
                                {erroresTipo.includes("codigoPostal") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese solo números.</p>
                                )}
                                {errores.includes("codigoPostal") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Localidad*:</label>
                                <input
                                    name="localidad"
                                    value={formData.localidad}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("localidad")}
                                    placeholder="Localidad"
                                />
                                {erroresTipo.includes("localidad") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese solo letras.</p>
                                )}
                                {errores.includes("localidad") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Provincia*:</label>
                                <input
                                    name="provincia"
                                    value={formData.provincia}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("provincia")}
                                    placeholder="Provincia"
                                />
                                {erroresTipo.includes("provincia") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese solo letras.</p>
                                )}
                                {errores.includes("provincia") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">País*:</label>
                                <input
                                    name="pais"
                                    value={formData.pais}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("pais")}
                                    placeholder="País"
                                />
                                {erroresTipo.includes("pais") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese solo letras.</p>
                                )}
                                {errores.includes("pais") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>
                        </form>
                    </div>

                    {/* FORMULARIO 3 - Contacto y Otros */}
                    <div className="bg-white rounded-xl shadow-lg p-6">
                        <h2 className="text-xl font-bold text-indigo-950 mb-4 pb-3 border-b-2 border-indigo-200">
                            Contacto y Otros
                        </h2>
                        <form className="flex flex-col space-y-4">
                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Fecha de Nacimiento*:</label>
                                <input
                                    name="fechaNacimiento"
                                    value={formData.fechaNacimiento}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="date"
                                    className={getInputClass("fechaNacimiento")}
                                />
                                {erroresTipo.includes("fechaNacimiento") && (
                                    <p className="text-red-600 text-xs mt-1">Fecha inválida.</p>
                                )}
                                {errores.includes("fechaNacimiento") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Teléfono*:</label>
                                <input
                                    name="telefono"
                                    value={formData.telefono}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("telefono")}
                                    placeholder="Teléfono"
                                />
                                {erroresTipo.includes("telefono") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese un teléfono válido.</p>
                                )}
                                {errores.includes("telefono") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Email:</label>
                                <input
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="email"
                                    className={getInputClass("email")}
                                    placeholder="Email"
                                />
                                {erroresTipo.includes("email") && (
                                    <p className="text-red-600 text-xs mt-1">Email inválido.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Ocupación*:</label>
                                <input
                                    name="ocupacion"
                                    value={formData.ocupacion}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("ocupacion")}
                                    placeholder="Ocupación"
                                />
                                {erroresTipo.includes("ocupacion") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese solo letras.</p>
                                )}
                                {errores.includes("ocupacion") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>

                            <div>
                                <label className="text-indigo-950 font-semibold mb-2 block text-sm">Nacionalidad*:</label>
                                <input
                                    name="nacionalidad"
                                    value={formData.nacionalidad}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    onKeyDown={handleKeyDownInput}
                                    type="text"
                                    className={getInputClass("nacionalidad")}
                                    placeholder="Nacionalidad"
                                />
                                {erroresTipo.includes("nacionalidad") && (
                                    <p className="text-red-600 text-xs mt-1">Ingrese solo letras.</p>
                                )}
                                {errores.includes("nacionalidad") && (
                                    <p className="text-red-600 text-xs mt-1">Campo obligatorio.</p>
                                )}
                            </div>
                        </form>
                    </div>
                </div>

                {/* BOTONES DE ACCIÓN */}
                <div className="flex justify-center gap-4 mt-6">
                    <button
                        type="button"
                        onKeyDown={(e) => handleKeyDown(e, () => setMostrarCancelar(true))}
                        onClick={() => setMostrarCancelar(true)}
                        className="px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all shadow-md hover:shadow-lg font-semibold"
                    >
                        Cancelar
                    </button>

                    <button
                        type="button"
                        onKeyDown={(e) => handleKeyDown(e, handleBorrar)}
                        onClick={handleBorrar}
                        className="px-6 py-3 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-all shadow-md hover:shadow-lg font-semibold"
                    >
                        Borrar
                    </button>

                    <button
                        type="button"
                        onKeyDown={(e) => handleKeyDown(e, handleSiguiente)}
                        onClick={handleSiguiente}
                        className="px-6 py-3 bg-gradient-to-r from-indigo-950 to-indigo-900 text-white rounded-lg hover:from-indigo-900 hover:to-indigo-800 transition-all shadow-md hover:shadow-lg font-semibold transform hover:scale-105"
                    >
                        Siguiente
                    </button>
                </div>

                {/* MENSAJE DE ERRORES */}
                {(errores.length > 0 || erroresTipo.length > 0) && (
                    <div className="bg-red-50 border-l-4 border-red-500 text-red-700 p-4 rounded-lg mt-6 shadow-md">
                        <p className="font-semibold mb-2">Errores encontrados:</p>
                        <ul className="list-disc list-inside">
                            {errores.map((error, index) => (
                                <li key={index} className="text-sm">El campo "{error}" es obligatorio.</li>
                            ))}
                            {erroresTipo.map((error, index) => (
                                <li key={index} className="text-sm">El campo "{error}" tiene un formato inválido.</li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>

            {/* CARTELES */}
            {mostrarCancelar && (
                <CancelarModificacion
                    onSi={() => {
                        sessionStorage.removeItem('huespedSeleccionado');
                        router.push('/menu');
                    }}
                    onNo={() => setMostrarCancelar(false)}
                />
            )}

            {mostrarExito && (
                <ModificacionExitosa
                    onClose={() => {
                        sessionStorage.removeItem('huespedSeleccionado');
                        setMostrarExito(false);
                        router.push('/menu');
                    }}
                />
            )}

            {mostrarErrorDni && (
                <ErrorDniExistente
                    mensaje="¡CUIDADO! El tipo y número de documento ya existen en el sistema"
                    onCorregir={() => {
                        setErrores(["tipoDocumento", "dni"]);
                        setMostrarErrorDni(false);
                        // Enfocar en tipo de documento
                        const tipoDocInput = document.querySelector('select[name="tipoDocumento"]') as HTMLSelectElement;
                        if (tipoDocInput) tipoDocInput.focus();
                    }}
                    onAceptar={handleGuardarSinValidarDni}
                />
            )}

            {mostrarConfirmarEliminar && huespedOriginal && (
                <ConfirmarEliminarHuesped
                    nombre={huespedOriginal.nombre}
                    apellido={huespedOriginal.apellido}
                    tipoDocumento={huespedOriginal.huespedID.tipoDocumento}
                    dni={huespedOriginal.huespedID.dni}
                    onEliminar={confirmarEliminacion}
                    onCancelar={() => setMostrarConfirmarEliminar(false)}
                />
            )}

            {mostrarEliminado && huespedOriginal && (
                <HuespedEliminado
                    nombre={huespedOriginal.nombre}
                    apellido={huespedOriginal.apellido}
                    tipoDocumento={huespedOriginal.huespedID.tipoDocumento}
                    dni={huespedOriginal.huespedID.dni}
                    onClose={handleCerrarEliminado}
                />
            )}

            {mostrarNoSePuedeEliminar && (
                <NoSePuedeEliminarHuesped
                    onClose={() => {
                        setMostrarNoSePuedeEliminar(false);
                    }}
                />
            )}
        </main>
    );
}

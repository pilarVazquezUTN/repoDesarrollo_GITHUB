"use client";
import { useState } from "react"; 
import CancelarAltaHuepsed from "../carteles/cancelarAltaHuesped";
import CargarOtroHuesped from "../carteles/huespedCargadoSatisfacoriamente";
import ErrorDniExistente from "../carteles/errorDniExistente";
import axios from "axios";

export default function DarAltaHuesped(){
    
    const [openCancelar, setOpenCancelar] = useState(false);
    const [openAceptar, setOpenAceptar] = useState(false);
    const [openError, setOpenError] = useState(false);
    const [mensajeError, setMensajeError] = useState("");

    const initialState = {
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
    };

    const [formData, setFormData] = useState(initialState);
    const [errores, setErrores] = useState<string[]>([]);
    const [erroresTipo, setErroresTipo] = useState<string[]>([]);

    // Expresiones regulares para validaciones
    const soloLetras = /^[A-ZÁÉÍÓÚÑ\s]*$/;
    const soloNumeros = /^[0-9]*$/;

    


    const nuevoHuesped = () => {
        setFormData(initialState);
        setErrores([]);
        setErroresTipo([]);
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target; 
        const valorMayus = value.toUpperCase();//REVISAR

        setFormData({ ...formData, [name]: value.toUpperCase() });

        if (errores.includes(name)) {
            setErrores(errores.filter((campo) => campo !== name));
        }

        
    };

    const getInputClass = (campo: string) => {
        const baseClass = "p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950 uppercase";
        if (errores.includes(campo) || erroresTipo.includes(campo)) {
            return `${baseClass} border-red-500 border-2`;
        }
        return baseClass;
    };

    const guardarHuesped = async () => {

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
            await axios.post("http://localhost:8080/huespedes", payload);
            setErrores([]);
            setErroresTipo([]);
            setOpenAceptar(true);
        } catch (err) {
            console.error("Error al guardar:", err);
            alert("Error al conectar con el servidor.");
        }
    };

    const handleGuardarSinValidarDni = async () => {
        setOpenError(false);
        setErrores([]);
        await guardarHuesped();
    };


    const handleSubmit = async () => { 
        
        const camposRequeridos = [
            "apellido", "nombre", "tipoDocumento", "dni",
            "posicionIva", "calle", "numero",
            "codigoPostal", "localidad", "provincia", "pais",
            "fechaNacimiento", "telefono", "ocupacion", "nacionalidad"
        ];

        //  VALIDACIÓN ESPECIAL: CUIT obligatorio si es RESPONSABLE INSCRIPTO
        if (formData.posicionIva === "RESPONSABLE INSCRIPTO" && formData.cuit.trim() === "") {
            camposRequeridos.push("cuit");
        }

        const nuevosErrores = camposRequeridos.filter((campo) => {
            const valor = formData[campo as keyof typeof formData];
            return !valor || valor.trim() === "";
        });

        if (nuevosErrores.length > 0) {
            setErrores(nuevosErrores);
            return;
        }

        //  Validación de tipo de dato
        let tipoInvalido = false;

        const nuevosErroresTipo: string[] = [];
        const camposSoloLetras = ["nombre", "apellido", "ocupacion", "nacionalidad", "provincia", "localidad", "pais", "calle", "departamento", "posicionIva"];
        const camposSoloNumeros = ["dni", "numero", "piso", "codigoPostal", "telefono", "cuit"];
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        for (const campo in formData) {
        const valor = formData[campo as keyof typeof formData];

            if (camposSoloLetras.includes(campo) && !soloLetras.test(valor)) {
                nuevosErroresTipo.push(campo);
            } else if (camposSoloNumeros.includes(campo) && !soloNumeros.test(valor)) {
                nuevosErroresTipo.push(campo);
            } else if (campo === "email" && valor && !emailRegex.test(valor)) {
                nuevosErroresTipo.push(campo);
            } else if (campo === "fechaNacimiento" && valor) {
                const hoy = new Date();
                const fechaIngresada = new Date(valor);
                if (fechaIngresada > hoy) {
                    nuevosErroresTipo.push(campo);
                }
            }
        }

        if (nuevosErroresTipo.length > 0) {
            setErroresTipo(nuevosErroresTipo);
            return; // No enviamos si hay errores de tipo
        }


        // Verificar DNI existente
        try {
            const existe = await axios.get("http://localhost:8080/huespedes", {
                params: {
                    dni: formData.dni,
                    tipoDocumento: formData.tipoDocumento
                }
            });

            if (existe.data.length > 0) {
                setMensajeError("¡CUIDADO!");
                setOpenError(true);
                return;
            }
        } catch (error) {
            console.error("Error verificando DNI:", error);
            setMensajeError("Error verificando si el huésped existe.");
            setOpenError(true);
            return;
        }

        await guardarHuesped();
    };



    return(
        <main className="flex gap-80 px-6 py-6 items-start justify-center"> 

            {/* FORM 1 */}
            <form className="flex flex-col">
                <label className="text-indigo-950 font-medium mb-1">Apellido*:</label>
                <input name="apellido" value={formData.apellido} onChange={handleChange} type="text" placeholder="Apellido" className={ getInputClass("apellido")}/>
                {erroresTipo.includes("apellido") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>
                )}

                <label className="text-indigo-950 font-medium mb-1">Nombre*:</label>
                <input name="nombre" value={formData.nombre} onChange={handleChange} type="text" placeholder="Nombre" className={getInputClass("nombre")} />
                {erroresTipo.includes("nombre") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>
                )}
                
                <label className="text-indigo-950 font-medium mb-1">Tipo de Documento*:</label>
                <select 
                    name="tipoDocumento" 
                    value={formData.tipoDocumento} 
                    onChange={handleChange} 
                    className={getInputClass("tipoDocumento")
                        .replace("placeholder-gray-400", "text-gray-400")
                        .replace("text-indigo-950", "focus:text-indigo-950")
                        .replace("uppercase", "")}
                >
                    <option value="" className="text-gray-400">Seleccionar tipo</option>
                    <option value="DNI">DNI</option>
                    <option value="PASAPORTE">PASAPORTE</option>
                    <option value="CUIT">CUIT</option>
                </select>

                <label className="text-indigo-950 font-medium mb-1">Número de Documento*:</label>
                <input name="dni" value={formData.dni} onChange={handleChange} type="text" placeholder="Número de Documento" className={getInputClass("dni")} />
                {erroresTipo.includes("dni") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo números.</p>
                )}
                
                <label className="text-indigo-950 font-medium mb-1">CUIT:</label>
                <input name="cuit" value={formData.cuit} onChange={handleChange} type="text" placeholder="XX-XXXXXXXX-X" className={getInputClass("cuit")} />
                {erroresTipo.includes("cuit") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo números.</p>
                )}

                <label className="text-indigo-950 font-medium mb-1">Posicion frente al IVA*:</label>
                <input name="posicionIva" value={formData.posicionIva} onChange={handleChange} type="text" placeholder="Ej: RESPONSABLE INSCRIPTO" className={getInputClass("posicionIva")} />
                {erroresTipo.includes("posicionIva") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>
                )}
                <p className="mt-3">Campos obligatorios*</p>
            </form>

            {/* FORM 2 */}
            <form className="flex flex-col">
                <label className="text-indigo-950 font-medium mb-1">Calle*:</label>
                <input name="calle" value={formData.calle} onChange={handleChange} type="text" placeholder="calle" className={getInputClass("calle")} />
                {erroresTipo.includes("calle") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>
                )}

                <label className="text-indigo-950 font-medium mb-1">Número*:</label>
                <input name="numero" value={formData.numero} onChange={handleChange} type="text" placeholder="numero" className={getInputClass("numero")} />
                {erroresTipo.includes("numero") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo números.</p>
                )}

                <label className="text-indigo-950 font-medium mb-1">Departamento:</label>
                <input name="departamento" value={formData.departamento} onChange={handleChange} type="text" placeholder="departamento" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950 uppercase" />
                {erroresTipo.includes("departamento") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>
                )}

                <label className="text-indigo-950 font-medium mb-1">Piso:</label>
                <input name="piso" value={formData.piso} onChange={handleChange} type="text" placeholder="piso" className="p-2 border rounded mb-4 placeholder-gray-400 text-indigo-950 uppercase" />
                {erroresTipo.includes("piso") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo números.</p>
                )}

                <label className="text-indigo-950 font-medium mb-1">Código Postal*:</label>
                <input name="codigoPostal" value={formData.codigoPostal} onChange={handleChange} type="text" placeholder="codigo postal" className={getInputClass("codigoPostal")} />
                {erroresTipo.includes("codigoPostal") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo números.</p>
                )}

                <label className="text-indigo-950 font-medium mb-1">Localidad*:</label>
                <input name="localidad" value={formData.localidad} onChange={handleChange} type="text" placeholder="localidad" className={getInputClass("localidad")} />
                {erroresTipo.includes("localidad") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>
                )}

                <label className="text-indigo-950 font-medium mb-1">Provincia*:</label>
                <input name="provincia" value={formData.provincia} onChange={handleChange} type="text" placeholder="provincia" className={getInputClass("provincia")} />
                {erroresTipo.includes("provincia") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>
                )}

                <label className="text-indigo-950 font-medium mb-1">País*:</label>
                <input name="pais" value={formData.pais} onChange={handleChange} type="text" placeholder="pais" className={getInputClass("pais")} />
                {erroresTipo.includes("pais") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>
                )}

            </form>

            {/* FORM 3 */}
            <form className="flex flex-col">
                <label className="text-indigo-950 font-medium mb-1">Fecha de nacimiento*:</label>
                <input name="fechaNacimiento" value={formData.fechaNacimiento} onChange={handleChange} type="date" className={getInputClass("fechaNacimiento")} />
                {erroresTipo.includes("fechaNacimiento") && (
                    <p className="text-red-500 text-sm mb-3">Fecha inválida.</p>
                )}

                <label className="text-indigo-950 font-medium mb-1">Teléfono*:</label>
                <input name="telefono" value={formData.telefono} onChange={handleChange} type="text" placeholder="telefono" className={getInputClass("telefono")} />
                {erroresTipo.includes("telefono") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo números.</p>
                )}

                <label className="text-indigo-950 font-medium mb-1">Email:</label>
                <input name="email" value={formData.email} onChange={handleChange} type="text" placeholder="email" className={getInputClass("email")} />
                {erroresTipo.includes("email") && (
                    <p className="text-red-500 text-sm mb-3">Email inválido.</p>
                )}
                <label className="text-indigo-950 font-medium mb-1">Ocupación*:</label>
                <input name="ocupacion" value={formData.ocupacion} onChange={handleChange} type="text" placeholder="ocupacion" className={getInputClass("ocupacion")} />
                {erroresTipo.includes("ocupacion") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>
                )}

                <label className="text-indigo-950 font-medium mb-1">Nacionalidad*:</label>
                <input name="nacionalidad" value={formData.nacionalidad} onChange={handleChange} type="text" placeholder="nacionalidad" className={getInputClass("nacionalidad")} />
                {erroresTipo.includes("nacionalidad") && (
                    <p className="text-red-500 text-sm mb-3">Ingrese solo letras.</p>
                )}

                <div className="flex flex-row gap-3 items-center">   

                    <button 
                        type="button" 
                        className="px-4 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
                        onClick={() => setOpenCancelar(true)}
                    >
                        Cancelar
                    </button>

                    <button 
                        type="button" 
                        className="px-4 py-2 bg-indigo-950 text-white rounded-md hover:bg-indigo-800 transition"
                        onClick={handleSubmit}
                    >
                        Aceptar
                    </button>

                </div>
            </form>

            {/* CARTEL DE CANCELAR */}
            {openCancelar && (
                <CancelarAltaHuepsed onClose={() => setOpenCancelar(false)} />
            )}

            {/* CARTEL DE ÉXITO */}
            {openAceptar && (
                <CargarOtroHuesped
                    onClose={() => setOpenAceptar(false)}
                    onNuevo={nuevoHuesped}
                    nombre={formData.nombre}
                    apellido={formData.apellido}
                />
            )}

            {/* CARTEL DE ERROR */}
            {openError && (
                <ErrorDniExistente
                    mensaje={mensajeError}
                    onCorregir={() => {
                        setErrores(["tipoDocumento", "dni"]); 
                        setOpenError(false);
                    }}
                    onAceptar={handleGuardarSinValidarDni}
                />
            )}

        </main>
    );
}

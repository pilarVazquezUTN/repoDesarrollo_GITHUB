// app/validaciones/validaciones.ts

import { parseISO, isAfter } from "date-fns";

//FUNCIONES GENERALES:

export const esSoloLetras = (valor: string) =>
  /^[A-ZÁÉÍÓÚÑ\s]*$/i.test(valor);

export const esSoloNumeros = (valor: string) =>
  /^[0-9]+$/.test(valor);

export const esEmailValido = (valor: string) =>
  /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(valor);

export const validarDNI = (valor: string) =>
  /^\d{8}$/.test(valor);

export const validarCUIT = (valor: string) => {
  // Permitir: 11 dígitos o el formato XX-XXXXXXXX-X
  const regex = /^(\d{2}-?\d{8}-?\d{1})$/;

  return regex.test(valor);
};

export const esObligatorio = (valor: any) => {
  if (valor === null || valor === undefined) return false;
  if (typeof valor === "string") return valor.trim() !== "";
  return true; // para números, fechas o cualquier otro tipo válido
};

export const telefonoValido = (valor: string) => {
  return /^\d{6,15}$/.test(valor);
};;

//DAR ALTA HUESPED
export const validarFormularioHuesped = (formData: any) => {
  const camposRequeridos = [
    "apellido", "nombre", "tipoDocumento", "dni",
    "posicionIva", "calle", "numero",
    "codigoPostal", "localidad", "provincia", "pais",
    "fechaNacimiento", "telefono", "ocupacion", "nacionalidad"
  ];

  if (formData.posicionIva === "RESPONSABLE INSCRIPTO" && formData.cuit.trim() === "") {
    camposRequeridos.push("cuit");
  }

  const errores = camposRequeridos.filter(campo => !esObligatorio(formData[campo]));


  const erroresTipo: string[] = [];

  const camposSoloLetras = [
    "nombre", "apellido", "ocupacion", "nacionalidad",
    "provincia", "localidad", "pais", "calle", "departamento", "posicionIva"
  ];
  const camposSoloNumeros = [
    "dni", "numero", "piso", "codigoPostal", "telefono"
  ];

  for (const campo in formData) {
    const valor = formData[campo];


    if (camposSoloLetras.includes(campo) && valor && !esSoloLetras(valor)) {
      erroresTipo.push(campo);
    } 
    else if (camposSoloNumeros.includes(campo) && valor && !esSoloNumeros(valor)) {
      erroresTipo.push(campo);
    } 
    else if (campo === "email" && valor && !esEmailValido(valor)) {
      erroresTipo.push(campo);
    } 
    else if (campo === "fechaNacimiento" && valor) {
      const hoy = new Date();
      const fecha = new Date(valor);
      if (fecha > hoy) {
        erroresTipo.push(campo);
      }
    }

    if (campo === "dni" && valor){

       validarDNI(valor);
       if(!validarDNI(valor)){
        erroresTipo.push(campo); 
        } 
    }

    if(campo === "cuit" && valor){
        validarCUIT(valor);
        if(!validarCUIT(valor)){
            erroresTipo.push(campo);
        }
    }

    if (campo === "telefono" && valor && !telefonoValido(valor)) {
      erroresTipo.push(campo);
    }

  }

  return { errores, erroresTipo };
};




//RESERVAS
export const validarFechasReserva = (desde: string, hasta: string) => {
  const hoy = new Date();
  hoy.setHours(0, 0, 0, 0);

  const d = desde ? parseISO(desde) : null;
  const h = hasta ? parseISO(hasta) : null;

  const errores = {
    desdeInvalido: false,
    hastaInvalido: false,
    ordenInvalido: false,
    valido: false
  };

  if (d && d < hoy) errores.desdeInvalido = true;
  if (h && h < hoy) errores.hastaInvalido = true;
  if (d && h && isAfter(d, h)) errores.ordenInvalido = true;

  const valido =
    !!desde &&
    !!hasta &&
    !errores.desdeInvalido &&
    !errores.hastaInvalido &&
    !errores.ordenInvalido;

  return { ...errores, valido };
};

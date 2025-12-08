
➔ CU04: Reservar habitación

* Listar todas las habitaciones GET //HabitacionController
  URL: /api/habitaciones
* Listar reservas entre dos fechas GET //ReservaController
  URL: /api/reservas?desde=YYYY-MM-DD&hasta=YYYY-MM-DD
* Guardar Reserva POST //ReservaController
  URL: /api/reservas

➔ CU06: Cancelar reserva

* Listar reservas por apellido y nombre GET //ReservaController
  URL: /api/reservas/buscar?apellido=APELLIDO&nombre=NOMBRE
* Eliminar Reserva DELETE //ReservaController
  URL: /api/reservas/{id}

➔ CU07: Facturar

* Facturas por habitacion GET //FacturaController
  URL: /api/facturas/habitacion/{nroHabitacion}
* Actualizar factura PUT //FacturaController
  URL: /api/facturas/{id}

➔ CU11: Dar de baja huésped

* BuscarHuesped por Apellido, nombre , Tipo DNI o DNI GET //HuespedController
  URL: /api/huespedes/buscar?apellido=A&nombre=B&tipo=DNI&numero=12345678
* EliminarHuesped DELETE //HuespedController
  URL: /api/huespedes/{id}

➔ CU16: Ingresar pago

* Agregar pago POST //PagoController
  URL: /api/pagos

➔ CU19: Ingresar nota de crédito

* Agregar nota de credito POST //NotaDeCreditoController
  URL: /api/notas-credito

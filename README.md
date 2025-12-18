# 🏨 Hotel Premier – Sistema de Gestión Hotelera

Proyecto desarrollado como **Trabajo Práctico Final 2025** para las materias **Diseño de Sistemas de Información** y **Desarrollo de Software** (UTN – FRSF).

El sistema permite gestionar reservas, estadías, huéspedes, facturación y pagos de un hotel, cumpliendo con los casos de uso definidos en el enunciado oficial del TP.

## 📑 Índice

- [📌 Tecnologías utilizadas](#-tecnologías-utilizadas)
- [📂 Estructura del proyecto](#-estructura-del-proyecto)
- [🚀 Cómo ejecutar el proyecto](#-cómo-ejecutar-el-proyecto)
- [🌐 Endpoints](#-endpoints)
- [🚨 Manejo de errores y excepciones](#-manejo-de-errores-y-excepciones)
- [🧠 Patrones de diseño implementados](#-patrones-de-diseño-implementados)
- [🧪 Testing](#-testing)
- [📐 Diagramas](#-diagramas)
- [✅ Casos de Uso implementados](#-casos-de-uso-implementados)

---

## 📌 Tecnologías utilizadas

### Backend

- Java 17
- Spring Boot
- Spring Data JPA (DAO)
- PostgreSQL
- Maven
- JUnit 5 + Mockito
- Swagger (OpenAPI)

### Frontend

- Next.js
- React
- Axios
- HTML / CSS

### Otros

- Git / GitHub
- PlantUML (diagramas)

---

## 📂 Estructura del proyecto

```
.
├── backend 					 → Backend Spring Boot
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── hotelPremier
│       │   │           ├── classes
│       │   │           │   ├── Dominio
│       │   │           │   ├── DTO
│       │   │           │   ├── excepciones
│       │   │           │   └── mapper
│       │   │           ├── config
│       │   │           ├── controller
│       │   │           ├── repository
│       │   │           └── service
│       │   └── resources
│       └── test
│           └── java
│               └── com
│                   └── hotelPremier
│                       └── service
│
├── frontend 					  → Frontend en Next.js
│   ├── app
│   ├── components
│   └── public
│
├── sql						  → Archivo .sql de backup con tablas y datos precargados
│
├── Postman				          → Archivo con export de postman, con ejemplos de endpoints
│
├── EntregaDiseñoCU_Diagramas			  → Diagramas 
│
└── README.md

```

---

## 🚀 Cómo ejecutar el proyecto

---

### 1️⃣ Cargar base de datos en PostgreSQL (pgAdmin)

El repositorio incluye un backup de la base de datos con las tablas y datos necesarios para probar el sistema.

#### Pasos para restaurar el backup en pgAdmin

1. AbrirpgAdmin (Obligatorio Postrgre 18)
2. Crear una base de datos vacía (por ejemplo `hotelpremier`)
3. Hacer clic derecho sobre la base de datos creada
4. Seleccionar Restore
5. En la opción Filename, seleccionar el archivo: /sql/hotelpremier.backup
6. Presionar Restore y esperar a que finalice el proceso

#### Configuración en application.properties

Una vez restaurada la base de datos, verificar que los datos de conexión en`application.properties` coincidan con la base creada:

spring.datasource.url=jdbc:postgresql://localhost:5432/hotelpremier
spring.datasource.username=USUARIO
spring.datasource.password=PASSWORD

De esta forma, el backend podrá conectarse correctamente a la base de datos restaurada.

---

### 2️⃣ Backend (Spring Boot)

Desde la carpeta `/backend`:

```bash
mvn spring-boot:run
```

Servidor disponible en:

```
http://localhost:8080
```

Swagger:

```
http://localhost:8080/swagger-ui.html
```

---

### 3️⃣ Frontend (Next.js)

Desde la carpeta `/frontend`:

```bash
npm run dev
```

Aplicación disponible en:

```
http://localhost:3000
```

---

### 4️⃣ Importar colección de Postman

El repositorio incluye una **colección de Postman** con **endpoints de ejemplo** para probar la API REST del sistema.

Para importar la colección:

1. Abrir **Postman**
2. Ir a **File → Import**
3. Seleccionar la opción **Files**
4. Importar el archivo: /Postman/Endpoints.json

Una vez importada la colección, se dispondrá de endpoints organizados por recurso y requests de ejemplo para cada Caso de Uso, permitiendo validar rápidamente el funcionamiento del backend.

---

## 🌐 Endpoints

A continuación se detallan los **endpoints REST implementados**, organizados por recurso y alineados con los Casos de Uso solicitados en la consigna.

---

### 👤 Huéspedes

- `GET /huespedes?dni=XXX&nombre=YYY&apellido=ZZZ&tipoDocumento=WWW `Búsqueda de huéspedes por distintos criterios (CU02).
- `PUT /huespedes/modificar` Modificación de datos de un huésped existente (CU10).
- `DELETE /huespedes/{tipo}/{dni}`
  Baja lógica del huésped según tipo y número de documento (CU11).

---

### 📅 Reservas

- `POST /reservas `Alta de una o varias reservas para un rango de fechas (CU04).
- `GET /reservas?desde=YYYY-MM-DD&hasta=YYYY-MM-DD` Listado de reservas entre dos fechas.
- `GET /reservas/buscar?apellido=XXX&nombre=YYY` Búsqueda de reservas por datos del huésped (CU06).
- `PUT /reservas/cancelar`
  Cancelación de una o varias reservas existentes (CU06).

---

### 🛏️Habitaciones

- `GET /habitaciones?tipo=XXX`
  Listado de habitaciones, opcionalmente filtradas por tipo.

---

### 🛎️ Estadías

- `GET /estadias/enCurso/{numHabitacion}`
  Obtiene la estadía en curso asociada a una habitación determinada (CU15).

---

### 🧾 Facturas

- `GET /facturas/habitacion/{nro}` Obtiene las facturas asociadas a una habitación.
- `GET /facturas/filtrar?cuit=XXX&tipo=YYY&numero=ZZZ` Búsqueda y filtrado de facturas por distintos criterios.
- `POST /facturas`
  Generación de una nueva factura para una estadía (CU07).

---

### 💳 Pagos

- `POST /pagos`
  Registro de uno o varios pagos asociados a una factura (CU16).

---

### 📝 Notas de Crédito

- `POST /notadecredito`
  Emisión de una nota de crédito para cancelar total o parcialmente facturas existentes (CU19).

---

### 👨‍💼Responsables de Pago

- `GET /responsablesPago?dni=XXX&tipoDocumento=YYY&cuit=ZZZ`
  Búsqueda de responsables de pago (persona física o jurídica).

---

## 🚨 Manejo de errores y excepciones

El backend implementa un **criterio unificado de clasificación de errores**, utilizando **excepciones personalizadas** y códigos HTTP adecuados.

| Tipo de error                       | Excepción                   | Código HTTP |
| ----------------------------------- | ---------------------------- | ------------ |
| Recurso no existe                   | RecursoNoEncontradoException | 404          |
| Regla de negocio / estado inválido | NegocioException             | 409          |
| Validación de datos de entrada     | IllegalArgumentException     | 400          |
| Error técnico inesperado           | Exception                    | 500          |

Las excepciones se manejan de forma centralizada mediante un **handler global** (`@ControllerAdvice`), garantizando respuestas consistentes y claras.

---

## 🧠 Patrones de diseño implementados

En esta sección se describen los **patrones de diseño aplicados en el proyecto**, explicados de forma práctica y apoyados en **ejemplos concretos del código**, evitando definiciones teóricas generales.

---

### 1️⃣ DAO (Data Access Object)

En el proyecto, cada entidad principal tiene un **Repository** que se encarga exclusivamente del acceso a la base de datos. Las clases de servicio no realizan consultas ni operaciones de persistencia directamente.

**Ejemplos de uso en el sistema:**

- El servicio de huéspedes consulta y guarda datos a través de `HuespedRepository`.
- Las reservas se crean, buscan y cancelan usando `ReservaRepository`.
- Facturas y pagos se persisten mediante `FacturaRepository` y `PagoRepository`.

De esta forma, toda la lógica de negocio queda en los servicios y el acceso a datos queda centralizado en los DAO.

---

### 2️⃣ Strategy

El cálculo del importe de una factura no está fijo en una única clase. Cuando se genera una factura, el sistema utiliza una estrategia de cálculo que se encarga de obtener el total.

**Ejemplo de uso en el sistema:**

- Al facturar una estadía, el servicio delega el cálculo a una implementación de `CalculoFacturaStrategy`.
- La estrategia suma el valor de la estadía, los consumos y aplica descuentos o notas de crédito si corresponde.

Esto permite cambiar o extender la forma de cálculo sin modificar la lógica principal de facturación.

---

### 3️⃣ State

El sistema representa los estados importantes mediante **objetos de estado**, en lugar de usar strings o banderas.

**Ejemplo de uso en el sistema:**

- Una `Estadia` mantiene un objeto que representa su estado actual.
- Si la estadía está en curso, se permiten ciertas operaciones; si está finalizada, esas operaciones se bloquean.

Este enfoque evita condicionales repetidos y mantiene el comportamiento asociado al estado correspondiente.

---

### 4️⃣ Observer

El patrón Observer se utiliza cuando un cambio en una entidad debe ser informado a otros componentes del sistema.

**Ejemplo de uso en el sistema:**

- Cuando una estadía cambia de estado, se notifica a los observadores registrados. (Como cuando cambia a estado ENCURSO reserva cambia a FINALIZADA)
- Esto permite que otras partes del sistema reaccionen sin que la estadía tenga que conocerlas directamente.

El uso de este patrón facilita la extensión del sistema ante nuevos eventos del negocio.

---

## 🧪 Testing

Se implementaron tests unitarios en la capa de servicio utilizando **JUnit y Mockito**, alcanzando una cobertura mínima del **80%** en al menos 3 servicios.

Para ejecutar los tests:

```bash
mvn test
```

---

## 📐 Diagramas

Incluidos en el repositorio:

- Diagrama de clases
- Diagramas de secuencia
- Diagrama entidad-relación

Todos realizados en **PlantUML**, respetando el diseño aprobado en la materia Diseño de Sistemas.

---

## ✅ Casos de Uso implementados

- CU04 – Reservar habitación
- CU05 – Mostrar estado de habitaciones
- CU06 – Cancelar reserva
- CU07 – Facturar
- CU11 – Dar baja huésped
- CU16 – Ingresar pago
- CU19 – Ingresar nota de crédito

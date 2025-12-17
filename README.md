
# 🏨 Hotel Premier – Sistema de Gestión Hotelera

Proyecto desarrollado como **Trabajo Práctico Final 2025** para las materias **Diseño de Sistemas de Información** y **Desarrollo de Software** (UTN – FRSF).

El sistema permite gestionar reservas, estadías, huéspedes, facturación y pagos de un hotel, cumpliendo con los casos de uso definidos en el enunciado oficial del TP.

---

## 📌 Tecnologías utilizadas

### Backend

* Java 17
* Spring Boot
* Spring Data JPA (DAO)
* PostgreSQL
* Maven
* JUnit 5 + Mockito
* Swagger (OpenAPI)

### Frontend

* Next.js
* React
* Axios
* HTML / CSS

### Otros

* Git / GitHub
* PlantUML (diagramas)

---

## 📂 Estructura del proyecto

```
/HotelPremier
│
├── /api            → Backend Spring Boot
│   ├── /classes
│   │   ├── /Dominio
│   │   ├── /DTO
│   │   ├── /mapper
│   │   ├── /repository
│   │   ├── /service
│   │   └── /controller
│   └── HotelPremierApplication.java
│
├── /frontend       → Frontend Next.js
│   ├── /app
│   ├── /components
│   └── /public
│
├── /sql
│   ├── schema.sql
│   └── seed.sql
│
└── README.md
```

---

## ▶️ Cómo ejecutar el proyecto

### 1️⃣ Base de Datos

Crear una base de datos PostgreSQL:

```sql
CREATE DATABASE hotelpremier;
```

Ejecutar los scripts:

```bash
psql -d hotelpremier -f schema.sql
psql -d hotelpremier -f seed.sql
```

Configurar credenciales en `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hotelpremier
spring.datasource.username=usuario
spring.datasource.password=password
```

---

### 2️⃣ Backend (Spring Boot)

Desde la carpeta `/api`:

```bash
mvn clean install
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
npm install
npm run dev
```

Aplicación disponible en:

```
http://localhost:3000
```

---

## 🔌 Endpoints principales

A continuación se detallan los  **endpoints REST implementados** , organizados por recurso y alineados con los Casos de Uso solicitados en la consigna.

---

### 👤 Huéspedes

* `GET /huespedes?dni=XXX&nombre=YYY&apellido=ZZZ&tipoDocumento=WWW`
  Búsqueda de huéspedes por distintos criterios (CU02).
* `PUT /huespedes/modificar`
  Modificación de datos de un huésped existente (CU10).
* `DELETE /huespedes/{tipo}/{dni}`
  Baja lógica del huésped según tipo y número de documento (CU11).

---

### 📅 Reservas

* `POST /reservas`
  Alta de una o varias reservas para un rango de fechas (CU04).
* `GET /reservas?desde=YYYY-MM-DD&hasta=YYYY-MM-DD`
  Listado de reservas entre dos fechas.
* `GET /reservas/buscar?apellido=XXX&nombre=YYY`
  Búsqueda de reservas por datos del huésped (CU06).
* `PUT /reservas/cancelar`
  Cancelación de una o varias reservas existentes (CU06).

---

### 🏨 Habitaciones

* `GET /habitaciones?tipo=XXX`
  Listado de habitaciones, opcionalmente filtradas por tipo.

---

### 🛎️ Estadías

* `GET /estadias/enCurso/{numHabitacion}`
  Obtiene la estadía en curso asociada a una habitación determinada (CU15).

---

### 🧾 Facturas

* `GET /facturas/habitacion/{nro}`
  Obtiene las facturas asociadas a una habitación.
* `GET /facturas/filtrar?cuit=XXX&tipo=YYY&numero=ZZZ`
  Búsqueda y filtrado de facturas por distintos criterios.
* `POST /facturas`
  Generación de una nueva factura para una estadía (CU07).

---

### 💳 Pagos

* `POST /pagos`
  Registro de uno o varios pagos asociados a una factura (CU16).

---

### 📝 Notas de Crédito

* `POST /notadecredito`
  Emisión de una nota de crédito para cancelar total o parcialmente facturas existentes (CU19).

---

### 🏢 Responsables de Pago

* `GET /responsablesPago?dni=XXX&tipoDocumento=YYY&cuit=ZZZ`
  Búsqueda de responsables de pago (persona física o jurídica).

---

## 🧠 Patrones de diseño implementados

En esta sección se describen los  **patrones de diseño aplicados en el proyecto** , explicados de forma práctica y apoyados en  **ejemplos concretos del código** , evitando definiciones teóricas generales.

---

### 1️⃣ DAO (Data Access Object)

En el proyecto, cada entidad principal tiene un **Repository** que se encarga exclusivamente del acceso a la base de datos. Las clases de servicio no realizan consultas ni operaciones de persistencia directamente.

**Ejemplos de uso en el sistema:**

* El servicio de huéspedes consulta y guarda datos a través de `HuespedRepository`.
* Las reservas se crean, buscan y cancelan usando `ReservaRepository`.
* Facturas y pagos se persisten mediante `FacturaRepository` y `PagoRepository`.

De esta forma, toda la lógica de negocio queda en los servicios y el acceso a datos queda centralizado en los DAO.

---

### 2️⃣ Strategy (Cálculo de Facturas)

El cálculo del importe de una factura no está fijo en una única clase. Cuando se genera una factura, el sistema utiliza una estrategia de cálculo que se encarga de obtener el total.

**Ejemplo de uso en el sistema:**

* Al facturar una estadía, el servicio delega el cálculo a una implementación de `CalculoFacturaStrategy`.
* La estrategia suma el valor de la estadía, los consumos y aplica descuentos o notas de crédito si corresponde.

Esto permite cambiar o extender la forma de cálculo sin modificar la lógica principal de facturación.

---

### 3️⃣ State (Estados del dominio)

El sistema representa los estados importantes mediante  **objetos de estado** , en lugar de usar strings o banderas.

**Ejemplo de uso en el sistema:**

* Una `Estadia` mantiene un objeto que representa su estado actual.
* Si la estadía está en curso, se permiten ciertas operaciones; si está finalizada, esas operaciones se bloquean.

Este enfoque evita condicionales repetidos y mantiene el comportamiento asociado al estado correspondiente.

---

### 4️⃣ Observer (Notificación de eventos)

El patrón Observer se utiliza cuando un cambio en una entidad debe ser informado a otros componentes del sistema.

**Ejemplo de uso en el sistema:**

* Cuando una estadía cambia de estado, se notifica a los observadores registrados.
* Esto permite que otras partes del sistema reaccionen sin que la estadía tenga que conocerlas directamente.

El uso de este patrón facilita la extensión del sistema ante nuevos eventos del negocio.

---

## 🧪 Testing

Se implementaron tests unitarios en la capa de servicio utilizando  **JUnit y Mockito** , alcanzando una cobertura mínima del **80%** en al menos 3 servicios.

Para ejecutar los tests:

```bash
mvn test
```

---

## 📐 Diagramas

Incluidos en el repositorio:

* Diagrama de clases
* Diagramas de secuencia
* Diagrama entidad-relación

Todos realizados en  **PlantUML** , respetando el diseño aprobado en la materia Diseño de Sistemas.

---

## ✅ Casos de Uso implementados

* CU04 – Reservar habitación
* CU05 – Mostrar estado de habitaciones
* CU06 – Cancelar reserva
* CU07 – Facturar
* CU11 – Dar baja huésped
* CU16 – Ingresar pago
* CU19 – Ingresar nota de crédito

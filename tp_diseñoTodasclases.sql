CREATE TABLE direccion (
id_direccion INTEGER primary key,
calle VARCHAR(20),
numero INTEGER,
departamento VARCHAR(10),
piso INTEGER,
cod_postal INTEGER,
localidad VARCHAR(20),
provincia VARCHAR(20),
pais VARCHAR(20)
)

CREATE TABLE estadia(
id_estadia INTEGER primary key,
check_in Date,
check_out Date,
nro_habitacion integer,
constraint fk_nrohab foreign key (nro_habitacion) references habitacion(nro_habitacion)
)
--entre estadia y huesped nueva relacion 
create table genera(
id_estadia integer,
dni integer,
tipoDocumento varchar(40),
constraint pk_tipodoc_dni_idestad primary key (id_estadia, dni, tipoDocumento ),
constraint fk_idestadia foreign key (id_estadia) references estadia(id_estadia),
constraint fk_tipodoc_dni foreign key (tipoDocumento, dni ) references huesped (tipodocumento , dni) 
)
--por ser debil
CREATE TABLE servicio_extra(
id_servicio INTEGER,
id_estadia integer,
tipo_servicio VARCHAR(40),
precio FLOAT,
constraint pk_servicio_estadia primary key(id_servicio,id_estadia),
constraint fk_estadia foreign key (id_estadia) references estadia(id_estadia)

)


--uso la distribucion plana

CREATE TABLE habitacion(
nro_habitacion integer primary key,
precio integer,
tipohabitacion varchar(30),
capacidadPersonas integer,
estado boolean,
camasKingSize integer,
camaDoble integer,
camasIndividuales integer
) 


--CUIT ES INTEGER SACARLE LAS BARRAS ANTES DE GUARDAR
create table huesped(
tipoDocumento varchar(20),
numeroDocumento integer,
telefono integer,
nacionalidad varchar(30),
cuit integer,
apellido varchar(40),
fechaNacimiento date, 
email varchar(40),
posicionIva varchar(40),
nombre varchar (40),
ocupacion varchar(40),
id_direccion integer,
constraint pk_tipo_nrodoc primary key (tipoDocumento,dni),
constraint fk_id_direccion foreign key (id_direccion) references direccion (id_direccion)

)

create table reserva(
id_reserva integer,
apellido varchar(40),
nombre varchar(40),
telefono integer,
fecha_desde date,
fecha_hasta date,
estado varchar(40), 
nro_habitacion integer,
constraint fk_nrohab foreign key (nro_habitacion) references habitacion(nro_habitacion)
)

create table responsablePago(
id_responsablePago integer primary key,
dni integer,
cuit integer,
razonSocial varchar(40),
nombre varchar(40),

id_direccion integer,
tipoDoc varchar(20),
nro_factura integer,

constraint fk_id_direccion foreign key (id_direccion) references direccion (id_direccion),
constraint fk_dni_tipo foreign key (dni, tipoDoc) references huesped (dni, tipodocumento),
constraint fk_nrofactura foreign key (nro_factura) references factura (nro_factura)
)

create table factura(
nro_factura integer primary key,
fecha date,
total float,
estado varchar(40),
tipo varchar(2),
id_estadia integer,
id_notacredito integer,
id_pago integer,
constraint fk_id_estadia foreign key (id_estadia) references estadia (id_estadia),
constraint fk_idnotacredito foreign key (id_notacredito) references notadecredito (id_notacredito),
constraint fk_idpago foreign key (id_pago) references pago (id_pago)
)


create table notaDeCredito(
id_notaCredito integer primary key,
monto float
)

create table pago(
id_pago integer primary key,
monto float,
fecha date
)

create table medio_pago(
id_mediodepago integer primary key,
tipomediodepago varchar(40), --aca pongo el tipo mediodepago
monto float,
fecha date,
plazo date,
banco varchar(40),
cuotas integer,
dniTitular integer,
tipocambio float,
tipoMoneda varchar(40),
id_pago integer,
constraint fk_idpago foreign key (id_pago) references pago (id_pago)
)













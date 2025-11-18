CREATE TABLE direccion (
id_direccion INTEGER primary key,
calle VARCHAR(20),
numero INTEGER,
departamento VARCHAR(10),
piso INTEGER,
codigoPostal INTEGER,
localidad VARCHAR(20),
provincia VARCHAR(20),
pais VARCHAR(20)
)

--uso la distribucion plana

CREATE TABLE habitacion(
numero integer primary key,
precio integer,
tipohabitacion varchar(30),
cantidadPersonas integer,
estado boolean,
camasKingSize integer,
camaDoble integer,
camasIndividuales integer
) ;


create table huesped(
tipoDocumento varchar(20),
numeroDocumento varchar(50),
telefono varchar(50),
nacionalidad varchar(50),
cuit varchar(50),
apellido varchar(40),
fechaNacimiento date, 
email varchar(40),
posicionIva varchar(40),
nombre varchar (40),
ocupacion varchar(40),
id_direccion integer,
constraint pk_tipo_nrodoc primary key (tipoDocumento,dni),
constraint fk_id_direccion foreign key (id_direccion) references direccion (id_direccion)

);
CREATE TABLE estadia(
id_estadia INTEGER primary key,
checkin Date,
checkout Date,
nro_habitacion integer,
constraint fk_nrohab foreign key (nro_habitacion) references habitacion(nro_habitacion)
);
--entre estadia y huesped nueva relacion 
create table genera(
id_estadia integer,
dni varchar(50),
tipoDocumento varchar(40),
constraint pk_tipodoc_dni_idestad primary key (id_estadia, dni, tipoDocumento ),
constraint fk_idestadia foreign key (id_estadia) references estadia(id_estadia),
constraint fk_tipodoc_dni foreign key (tipoDocumento, dni ) references huesped (tipodocumento , dni) 
);


--por ser debil
CREATE TABLE servicio_extra(
id_servicio INTEGER,
id_estadia integer,
tipo_servicio VARCHAR(40),
precio FLOAT,
constraint pk_servicio_estadia primary key(id_servicio,id_estadia),
constraint fk_estadia foreign key (id_estadia) references estadia(id_estadia)

);







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
);

create table notaDeCredito(
id_notaCredito integer primary key,
monto float
);

create table pago(
id_pago integer primary key,
monto float,
fecha date
);

create table medio_pago(
id_mediodepago integer primary key,
tipomediodepago varchar(40), --aca pongo el tipo mediodepago
monto float,
fecha date,
plazo date,
banco varchar(40),
cuotas integer,
dniTitular varchar(50),
tipocambio float,
tipoMoneda varchar(40),
id_pago integer,
constraint fk_idpago foreign key (id_pago) references pago (id_pago)
);


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
);



create table responsablePago(
id_responsablePago integer primary key,
dni varchar(50),
cuit varchar(50),
razonSocial varchar(40),
nombre varchar(40),
tipoPersona varchar(40),

id_direccion integer,
tipoDoc varchar(20),
nro_factura integer,

constraint fk_id_direccion foreign key (id_direccion) references direccion (id_direccion),
constraint fk_dni_tipo foreign key (dni, tipoDoc) references huesped (dni, tipodocumento),
constraint fk_nrofactura foreign key (nro_factura) references factura (nro_factura)
);



Create table usuario (
nombre varchar(40),
contrasenia varchar(40) primary key

);

INSERT INTO usuario (nombre, contrasenia) VALUES
('pilar', 'vazquez135'),
('Ernestina', 'Solorzano852'),
('Guillermina', 'Fornari147'),
('Victoria', 'Sovrano28316');

INSERT INTO direccion (id_direccion, calle, numero, departamento, piso, cod_postal, localidad, provincia, pais) VALUES
(1,'AV. CORRIENTES',1234,'A',2,1000,'CENTRO','BUENOS AIRES','ARGENTINA'),
(2,'SANTA FE',2234,'A',1,3000,'CENTRO','SANTA FE','ARGENTINA'),
(3,'MARCO SASTRE',786,'A',1,3000,'SANTA FE','SANTA FE','ARGENTINA'),
(4,'SANTA FE',5567,'A',1,3000,'CENTRO','SANTA FE','ARGENTINA'),
(5,'4 DE ENERO',3449,'B',8,3000,'SANTA FE','SANTA FE','ARGENTINA'),
(6,'CORRIENTES',3163,'A',2,3000,'CENTRO','SANTA FE','ARGENTINA'),
(7,'CORRIENTES',3163,'A',2,3000,'CENTRO','SANTA FE','ARGENTINA'),
(8,'SANTA FE',4456,'C',5,3000,'CENTRO','SANTA FE','ARGENTINA'),
(9,'MENDOZA',123,'D',7,1000,'BUENOS AIRES','BUENOS AIRES','ARGENTINA'),
(10,'SAAVEDRA',123,'A',2,1000,'BUENOS AIRES','BUENOS AIRES','ARGENTINA'),
(11,'LOS MOLINOS',4568,'A',4,1400,'TUCUMAN','TUCUMAN','ARGENTINA'),
(12,'LAS ALMENDRAS',12,'O',4,1200,'LAS ALMENAS','SANTA FE','ARGENTINA'),
(13,'LAS MACETAS',2345,'B',7,3000,'SANTA FE CAPITAL','SANTA FE','ARGENTINA');


INSERT INTO huesped (
    tipodocumento, dni, telefono, nacionalidad, cuit,
    apellido, fechanacimiento, email, posiva, nombre,
    ocupacion, id_direccion
) VALUES
('DNI','12345678','1155555555','ARGENTINA','20123456789','PÉREZ','1995-05-20','JUANPEREZ@MAIL.COM','RESPONSABLE INSCRIPTO','JUAN','INGENIERO',1),
('DNI','23456789','3425789789','ARGENTINA',NULL,'PEREYRA','2003-11-14','pilita@gmail.com','RESPONSABLE INSCRIPTO','PILAR','ESTUDIANTE',2),
('DNI','44773109','543428547896','ARGENTINA','27447731091','SOVRANO','2003-06-18','VICTORIASOVRANO@GMAIL.COM','RESPONSABLE INSCRIPTO','VICTORIA','ESTUDIANTE',3),
('DNI','43647177','543424023698','ARGENTINA','20436471778','DASILVA','2003-02-01','VALENDASILVA@GMAIL.COM','RESPONSABLE INSCRIPTO','VALENTIN','ESTUDIANTE',4),
('DNI','44981795','3437457594','ARGENTINA','27449817953','SOLORZANO','2003-08-06','MARIAANTONIA@GMAIL.COM','MONOTRIBUTISTA','ERNESTINA','COMERCIANTE',5),
('DNI','45411122','3425908612','ARGENTINA',NULL,'VAZQUEZ','2003-11-13',NULL,'CONSUMIDOR FINAL','PILAR','ESTUDIANTE',6),
('DNI','22215539','3424458808','ARGENTINA','27222155399','AYALA','2003-11-13',NULL,'CONSUMIDOR FINAL','FERNANDA','ODONTÓLOGA',7),
('DNI','45678901','543428501478','ARGENTINA','23456789014','FORNARI','2003-02-22','GUILLEFORNARI@GMAIL.COM','RESPONSABLE INSCRIPTO','GUILLE','ESTUDIANTE',8),
('DNI','22456789','112345671','ARGENTINA',NULL,'MILEI','1970-12-20','ELLEON@GMAIL.COM','CONSUMIDOR FINAL','JAVIER','PRESIDENTE',9),
('DNI','22862121','114785523','ARGENTINA','20228621217','MASSA','1972-04-28',NULL,'RESPONSABLE INSCRIPTO','SERGIO','ROBAR',10),
('DNI','45411489','3457896523','ARGENTINA','20454114899','MARTINEZ','2000-10-04',NULL,'CONSUMIDOR FINAL','RITA','DOCENTE',11),
('DNI','20245589','3426789789','ARGENTINA','21202455899','MARTINEZ','1975-09-15','oscarcito@gmail.com','CONSUMIDOR FINAL','OSCAR','ABOGADO',12),
('DNI','18534526','3425789987','ARGENTINA','27185345263','ARGENTO','1971-07-20','PAOLITA@GMAIL.COM','CONSUMIDOR FINAL','PAOLA','ACTRIZ',13);











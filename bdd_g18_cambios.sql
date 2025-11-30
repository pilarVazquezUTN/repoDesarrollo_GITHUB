CREATE TABLE direccion (
id_direccion SERIAL primary key,
calle VARCHAR(20),
numero INTEGER,
departamento VARCHAR(10),
piso INTEGER,
codigoPostal INTEGER,
localidad VARCHAR(20),
provincia VARCHAR(20),
pais VARCHAR(20)
); 

--uso la distribucion plana 

CREATE TABLE habitacion(
numero integer primary key,
precio integer,
tipohabitacion varchar(40),
cantidadPersonas integer,
estado varchar(40),
camasKingSize integer,
camaDoble integer,
camasIndividuales integer
);



create table huesped(
dni varchar(40),
tipoDocumento varchar(20),
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
constraint fk_nrohab foreign key (nro_habitacion) references habitacion(numero)
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
id_reserva integer primary key,
apellido varchar(40),
nombre varchar(40),
telefono varchar(40),
fecha_desde date,
fecha_hasta date,
estado varchar(40),
nro_habitacion integer,
constraint fk_nrohab foreign key (nro_habitacion) references habitacion(numero)
);

create table notaDeCredito(
id_notaCredito integer primary key,
monto float
);
create table factura(
nro_factura integer primary key,
fecha date,
total float,
estado varchar(40),
tipo varchar(2),
id_estadia integer,
id_notacredito integer,
constraint fk_id_estadia foreign key (id_estadia) references estadia (id_estadia),
constraint fk_idnotacredito foreign key (id_notacredito) references notadecredito (id_notacredito)

);
create table pago(
id_pago integer primary key,
monto float,
fecha date,
nro_factura integer,
constraint fk_nrofactura foreign key (nro_factura) references factura (nro_factura)
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


create table responsablePago(
id_responsablePago integer primary key,
dni varchar(50),

id_direccion integer,
tipoDoc varchar(20),
nro_factura integer,

constraint fk_id_direccion foreign key (id_direccion) references direccion (id_direccion),
constraint fk_dni_tipo foreign key (dni, tipoDoc) references huesped (dni, tipodocumento),
constraint fk_nrofactura foreign key (nro_factura) references factura (nro_factura)
);



Create table usuario (
nombre varchar(40)  primary key,
contrasenia varchar(40)

);



INSERT INTO usuario (nombre, contrasenia) VALUES
('pilar', 'vazquez135'),
('Ernestina', 'Solorzano852'),
('Guillermina', 'Fornari147'),
('Victoria', 'Sovrano28316');

INSERT INTO direccion (id_direccion, calle, numero, departamento, piso, codigoPostal, localidad, provincia, pais) VALUES
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
    apellido, fechanacimiento, email, posicionIva, nombre,
    ocupacion, id_direccion
) VALUES
('DNI','12345678','1155555555','ARGENTINA','20123456789','PÉREZ',
    TO_DATE('20/05/1995','DD/MM/YYYY'),'JUANPEREZ@MAIL.COM','RESPONSABLE INSCRIPTO','JUAN','INGENIERO',1),

('DNI','23456789','3425789789','ARGENTINA',NULL,'PEREYRA',
    TO_DATE('14/11/2003','DD/MM/YYYY'),'pilita@gmail.com','RESPONSABLE INSCRIPTO','PILAR','ESTUDIANTE',2),

('DNI','44773109','543428547896','ARGENTINA','27447731091','SOVRANO',
    TO_DATE('18/06/2003','DD/MM/YYYY'),'VICTORIASOVRANO@GMAIL.COM','RESPONSABLE INSCRIPTO','VICTORIA','ESTUDIANTE',3),

('DNI','43647177','543424023698','ARGENTINA','20436471778','DASILVA',
    TO_DATE('01/02/2003','DD/MM/YYYY'),'VALENDASILVA@GMAIL.COM','RESPONSABLE INSCRIPTO','VALENTIN','ESTUDIANTE',4),

('DNI','44981795','3437457594','ARGENTINA','27449817953','SOLORZANO',
    TO_DATE('06/08/2003','DD/MM/YYYY'),'MARIAANTONIA@GMAIL.COM','MONOTRIBUTISTA','ERNESTINA','COMERCIANTE',5),

('DNI','45411122','3425908612','ARGENTINA',NULL,'VAZQUEZ',
    TO_DATE('13/11/2003','DD/MM/YYYY'),NULL,'CONSUMIDOR FINAL','PILAR','ESTUDIANTE',6),

('DNI','22215539','3424458808','ARGENTINA','27222155399','AYALA',
    TO_DATE('13/11/2003','DD/MM/YYYY'),NULL,'CONSUMIDOR FINAL','FERNANDA','ODONTÓLOGA',7),

('DNI','45678901','543428501478','ARGENTINA','23456789014','FORNARI',
    TO_DATE('22/02/2003','DD/MM/YYYY'),'GUILLEFORNARI@GMAIL.COM','RESPONSABLE INSCRIPTO','GUILLE','ESTUDIANTE',8),

('DNI','22456789','112345671','ARGENTINA',NULL,'MILEI',
    TO_DATE('20/12/1970','DD/MM/YYYY'),'ELLEON@GMAIL.COM','CONSUMIDOR FINAL','JAVIER','PRESIDENTE',9),

('DNI','22862121','114785523','ARGENTINA','20228621217','MASSA',
    TO_DATE('28/04/1972','DD/MM/YYYY'),NULL,'RESPONSABLE INSCRIPTO','SERGIO','ROBAR',10),

('DNI','45411489','3457896523','ARGENTINA','20454114899','MARTINEZ',
    TO_DATE('04/10/2000','DD/MM/YYYY'),NULL,'CONSUMIDOR FINAL','RITA','DOCENTE',11),

('DNI','20245589','3426789789','ARGENTINA','21202455899','MARTINEZ',
    TO_DATE('15/09/1975','DD/MM/YYYY'),'oscarcito@gmail.com','CONSUMIDOR FINAL','OSCAR','ABOGADO',12),

('DNI','18534526','3425789987','ARGENTINA','27185345263','ARGENTO',
    TO_DATE('20/07/1971','DD/MM/YYYY'),'PAOLITA@GMAIL.COM','CONSUMIDOR FINAL','PAOLA','ACTRIZ',13);

INSERT INTO habitacion (numero, precio, tipohabitacion, cantidadPersonas, estado, camasKingSize, camaDoble, camasIndividuales) VALUES
-- Individual Estándar (10)
(1, 50800, 'IndividualEstandar', 1, 'libre', 0, 0, 1),
(2, 50800, 'IndividualEstandar', 1, 'libre', 0, 0, 1),
(3, 50800, 'IndividualEstandar', 1, 'libre', 0, 0, 1),
(4, 50800, 'IndividualEstandar', 1, 'libre', 0, 0, 1),
(5, 50800, 'IndividualEstandar', 1, 'libre', 0, 0, 1),
(6, 50800, 'IndividualEstandar', 1, 'libre', 0, 0, 1),
(7, 50800, 'IndividualEstandar', 1, 'libre', 0, 0, 1),
(8, 50800, 'IndividualEstandar', 1, 'libre', 0, 0, 1),
(9, 50800, 'IndividualEstandar', 1, 'libre', 0, 0, 1),
(10, 50800, 'IndividualEstandar', 1, 'libre', 0, 0, 1),

-- Doble Estándar (18)
(11, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(12, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(13, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(14, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(15, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(16, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(17, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(18, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(19, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(20, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(21, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(22, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(23, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(24, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(25, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(26, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(27, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),
(28, 70230, 'DobleEstandar', 2, 'libre', 0, 1, 0),

-- Doble Superior (8)
(29, 90560, 'DobleSuperior', 2, 'libre', 1, 0, 0),
(30, 90560, 'DobleSuperior', 2, 'libre', 1, 0, 0),
(31, 90560, 'DobleSuperior', 2, 'libre', 1, 0, 0),
(32, 90560, 'DobleSuperior', 2, 'libre', 1, 0, 0),
(33, 90560, 'DobleSuperior', 2, 'libre', 1, 0, 0),
(34, 90560, 'DobleSuperior', 2, 'libre', 1, 0, 0),
(35, 90560, 'DobleSuperior', 2, 'libre', 1, 0, 0),
(36, 90560, 'DobleSuperior', 2, 'libre', 1, 0, 0),

-- Superior Family Plan (10)
(37, 110500, 'SuperiorFamilyPlan', 5, 'libre', 1, 1, 3),
(38, 110500, 'SuperiorFamilyPlan', 5, 'libre', 1, 1, 3),
(39, 110500, 'SuperiorFamilyPlan', 5, 'libre', 1, 1, 3),
(40, 110500, 'SuperiorFamilyPlan', 5, 'libre', 1, 1, 3),
(41, 110500, 'SuperiorFamilyPlan', 5, 'libre', 1, 1, 3),
(42, 110500, 'SuperiorFamilyPlan', 5, 'libre', 1, 1, 3),
(43, 110500, 'SuperiorFamilyPlan', 5, 'libre', 1, 1, 3),
(44, 110500, 'SuperiorFamilyPlan', 5, 'libre', 1, 1, 3),
(45, 110500, 'SuperiorFamilyPlan', 5, 'libre', 1, 1, 3),
(46, 110500, 'SuperiorFamilyPlan', 5, 'libre', 1, 1, 3),

-- Suite (2)
(47, 128600, 'Suite', 2, 'libre', 1, 0, 0),
(48, 128600, 'Suite', 2, 'libre', 1, 0, 0);


INSERT INTO reserva (id_reserva, apellido, nombre, telefono, fecha_desde, fecha_hasta, estado, nro_habitacion) VALUES
(1001, 'PÉREZ', 'JUAN', '1155555555',
        TO_DATE('15/12/2025','DD/MM/YYYY'),
        TO_DATE('20/12/2025','DD/MM/YYYY'),
        'Confirmada', 1),

(1002, 'PEREYRA', 'PILAR', '3425789789',
        TO_DATE('05/01/2026','DD/MM/YYYY'),
        TO_DATE('10/01/2026','DD/MM/YYYY'),
        'Confirmada', 11),

(1003, 'SOVRANO', 'VICTORIA', '543428547896',
        TO_DATE('25/11/2025','DD/MM/YYYY'),
        TO_DATE('27/11/2025','DD/MM/YYYY'),
        'Pendiente', 37),

(1004, 'DASILVA', 'VALENTIN', '543424023698',
        TO_DATE('14/02/2026','DD/MM/YYYY'),
        TO_DATE('20/02/2026','DD/MM/YYYY'),
        'Confirmada', 47),

(1007, 'AYALA', 'FERNANDA', '3424458808',
        TO_DATE('24/12/2025','DD/MM/YYYY'),
        TO_DATE('28/12/2025','DD/MM/YYYY'),
        'Confirmada', 29);


INSERT INTO estadia (id_estadia, checkin, checkout, nro_habitacion)
VALUES (2001,
        TO_DATE('15/12/2025','DD/MM/YYYY'),
        TO_DATE('20/12/2025','DD/MM/YYYY'),
        1);

INSERT INTO estadia (id_estadia, checkin, checkout, nro_habitacion)
VALUES (2002,
        TO_DATE('05/01/2026','DD/MM/YYYY'),
        TO_DATE('10/01/2026','DD/MM/YYYY'),
        11);

INSERT INTO estadia (id_estadia, checkin, checkout, nro_habitacion)
VALUES (2003,
        TO_DATE('24/12/2025','DD/MM/YYYY'),
        TO_DATE('28/12/2025','DD/MM/YYYY'),
        29);

INSERT INTO estadia (id_estadia, checkin, checkout, nro_habitacion)
VALUES (2004,
        TO_DATE('14/02/2026','DD/MM/YYYY'),
        TO_DATE('20/02/2026','DD/MM/YYYY'),
        47);


GRANT ALL PRIVILEGES ON DATABASE hotelpremier3 TO pili;

-- Esto actualiza la secuencia al valor máximo que existe actualmente en la tabla
--pili: agregue esto porque sino me crea el id 1, ahora con esta funcion arranca desde el ultimo+1
SELECT setval('direccion_id_direccion_seq', (SELECT MAX(id_direccion) FROM direccion));

--AGREGAR ESTO PARA Q SE GENERE EL ID CUANDO HAGO EL INSET EN LA BDD
SELECT setval(
    pg_get_serial_sequence('reserva', 'id_reserva'),
    (SELECT MAX(id_reserva) FROM reserva)
);
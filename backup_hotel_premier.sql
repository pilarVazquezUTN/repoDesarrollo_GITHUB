--
-- PostgreSQL database dump
--

\restrict vNKMDBEytPndoITWk2BvrihjJ9WdAMFjqXbAW3Z7nyKyoy95ile4IOTvdpcRTT3

-- Dumped from database version 13.23
-- Dumped by pg_dump version 13.23

-- Started on 2025-12-16 21:15:38

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3139 (class 0 OID 17121)
-- Dependencies: 205
-- Data for Name: direccion; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.direccion (id_direccion, calle, codigopostal, departamento, localidad, numero, pais, piso, provincia) FROM stdin;
3	Mitre	5000	B	Córdoba	889	Argentina	2	Córdoba
4	Sarmiento	3000	\N	Santa Fe	2020	Argentina	\N	Santa Fe
7	San Martín	2000	A	Rosario	1234	Argentina	2	Santa Fe
8	Belgrano	5000	B	Córdoba	450	Argentina	1	Córdoba
9	Rivadavia	1000	C	Buenos Aires	890	Argentina	3	Buenos Aires
13	San Martín	3000	A	Santa Fe	123	Argentina	1	Santa Fe
14	Mitre	5000	B	Córdoba	889	Argentina	2	Córdoba
17	Sarmiento	\N	\N	Santa Fe	3200	Argentina	\N	Santa Fe
18	Av. Santa Fe	\N	\N	Buenos Aires	780	Argentina	\N	Buenos Aires
19	Ruta 21 Km 12	\N	\N	San Lorenzo	500	Argentina	\N	Santa Fe
20	San Martin	2000	A	Rosario	1456	Argentina	3	Santa Fe
21	Av. Colon	5000	B	Cordoba	2340	Argentina	7	Cordoba
22	Belgrano	3000	C	Santa Fe	876	Argentina	2	Santa Fe
23	Mitre	5500	\N	Mendoza	1543	Argentina	\N	Mendoza
24	Av. Corrientes	1043	D	Buenos Aires	3890	Argentina	10	Buenos Aires
25	25 de Mayo	3100	\N	Parana	432	Argentina	\N	Entre Rios
26	Av. Alem	8000	E	Bahia Blanca	2211	Argentina	5	Buenos Aires
27	Rivadavia	4000	F	San Miguel de Tucuman	998	Argentina	1	Tucuman
28	Av. Illia	5152	\N	Villa Carlos Paz	1765	Argentina	\N	Cordoba
29	Lavalle	5400	B	San Juan	640	Argentina	4	San Juan
30	España	2000	A	Rosario	742	Argentina	2	Santa Fe
31	9 de Julio	3000	\N	Santa Fe	1560	Argentina	\N	Santa Fe
32	Av. Pellegrini	2000	C	Rosario	2890	Argentina	6	Santa Fe
33	Dean Funes	5000	B	Cordoba	430	Argentina	1	Cordoba
34	Av. San Juan	1233	D	Buenos Aires	1120	Argentina	8	Buenos Aires
35	Brown	8000	\N	Bahia Blanca	980	Argentina	\N	Buenos Aires
36	Urquiza	3100	A	Parana	221	Argentina	3	Entre Rios
37	Las Heras	5500	E	Mendoza	1745	Argentina	5	Mendoza
38	Gral. Paz	4000	\N	San Miguel de Tucuman	640	Argentina	\N	Tucuman
39	Av. Libertador	5400	B	San Juan	3050	Argentina	4	San Juan
\.


--
-- TOC entry 3141 (class 0 OID 17132)
-- Dependencies: 207
-- Data for Name: estadia; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.estadia (id_estadia, checkin, checkout, nro_habitacion, id_reserva, estado) FROM stdin;
301	2025-11-02	2025-11-04	101	201	FINALIZADA
303	2025-11-20	2025-11-23	103	203	PENDIENTE
304	2025-11-03	2025-11-07	201	204	FINALIZADA
305	2025-11-10	2025-11-14	202	205	FINALIZADA
306	2025-11-24	2025-11-28	203	206	PENDIENTE
307	2025-11-05	2025-11-10	301	207	FINALIZADA
308	2025-11-12	2025-11-16	302	208	FINALIZADA
309	2025-11-08	2025-11-15	401	209	FINALIZADA
310	2025-11-18	2025-11-25	402	210	PENDIENTE
311	2025-11-09	2025-11-13	501	211	FINALIZADA
312	2025-11-16	2025-11-20	502	212	FINALIZADA
302	2025-11-06	2025-12-16	102	202	FINALIZADA
\.


--
-- TOC entry 3155 (class 0 OID 42062)
-- Dependencies: 221
-- Data for Name: factura; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.factura (id, estado, fecha, tipo, total, id_estadia, id_notacredito, pago, responsablepago) FROM stdin;
3	PENDIENTE	2025-12-16	A	2.089912e+06	302	\N	\N	1
4	PENDIENTE	2025-12-16	A	2.089912e+06	302	\N	\N	1
5	PENDIENTE	2025-12-16	A	2.120646e+06	302	\N	\N	1
6	PENDIENTE	2025-12-16	A	2.089912e+06	302	\N	\N	1
7	PENDIENTE	2025-12-16	A	2.089912e+06	302	\N	\N	1
\.


--
-- TOC entry 3134 (class 0 OID 16925)
-- Dependencies: 200
-- Data for Name: habitacion; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.habitacion (tipohabitacion, numero, cantidadpersonas, estado, precio, camadoble, camasindividuales, camaskingsize) FROM stdin;
IndividualEstandar	101	1	Disponible	50800	\N	1	\N
IndividualEstandar	102	1	Disponible	50800	\N	1	\N
IndividualEstandar	103	1	Disponible	50800	\N	1	\N
IndividualEstandar	104	1	Disponible	50800	\N	1	\N
IndividualEstandar	105	1	Disponible	50800	\N	1	\N
IndividualEstandar	106	1	Disponible	50800	\N	1	\N
IndividualEstandar	107	1	Disponible	50800	\N	1	\N
IndividualEstandar	108	1	Disponible	50800	\N	1	\N
IndividualEstandar	109	1	Disponible	50800	\N	1	\N
IndividualEstandar	110	1	Disponible	50800	\N	1	\N
DobleEstandar	201	2	Disponible	70230	0	2	\N
DobleEstandar	202	2	Disponible	70230	1	0	\N
DobleEstandar	203	2	Disponible	70230	0	2	\N
DobleEstandar	204	2	Disponible	70230	1	0	\N
DobleEstandar	205	2	Disponible	70230	0	2	\N
DobleEstandar	206	2	Disponible	70230	1	0	\N
DobleEstandar	207	2	Disponible	70230	0	2	\N
DobleEstandar	208	2	Disponible	70230	1	0	\N
DobleEstandar	209	2	Disponible	70230	0	2	\N
DobleEstandar	210	2	Disponible	70230	1	0	\N
DobleEstandar	211	2	Disponible	70230	0	2	\N
DobleEstandar	212	2	Disponible	70230	1	0	\N
DobleEstandar	213	2	Disponible	70230	0	2	\N
DobleEstandar	214	2	Disponible	70230	1	0	\N
DobleEstandar	215	2	Disponible	70230	0	2	\N
DobleEstandar	216	2	Disponible	70230	1	0	\N
DobleEstandar	217	2	Disponible	70230	0	2	\N
DobleEstandar	218	2	Disponible	70230	1	0	\N
DobleSuperior	301	2	Disponible	90560	1	0	1
DobleSuperior	302	2	Disponible	90560	0	2	1
DobleSuperior	303	2	Disponible	90560	1	0	1
DobleSuperior	304	2	Disponible	90560	0	2	1
DobleSuperior	305	2	Disponible	90560	1	0	1
DobleSuperior	306	2	Disponible	90560	0	2	1
DobleSuperior	307	2	Disponible	90560	1	0	1
DobleSuperior	308	2	Disponible	90560	0	2	1
SuperiorFamilyPlan	401	5	FUERADESERVICIO	110500	1	3	\N
SuperiorFamilyPlan	402	5	Disponible	110500	2	1	\N
SuperiorFamilyPlan	403	5	Disponible	110500	1	3	\N
SuperiorFamilyPlan	404	5	Disponible	110500	2	1	\N
SuperiorFamilyPlan	405	5	Disponible	110500	1	3	\N
SuperiorFamilyPlan	406	5	Disponible	110500	2	1	\N
SuperiorFamilyPlan	407	5	Disponible	110500	1	3	\N
SuperiorFamilyPlan	408	5	Disponible	110500	2	1	\N
SuperiorFamilyPlan	409	5	Disponible	110500	1	3	\N
SuperiorFamilyPlan	410	5	Disponible	110500	2	1	\N
Suite	501	2	Disponible	128600	1	\N	1
Suite	502	2	Disponible	128600	1	\N	1
\.


--
-- TOC entry 3153 (class 0 OID 42015)
-- Dependencies: 219
-- Data for Name: huesped; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.huesped (dni, tipodocumento, apellido, cuit, email, fechanacimiento, nacionalidad, nombre, ocupacion, posicioniva, telefono, id_direccion, id_responsablepago) FROM stdin;
39214567	DNI	Fernandez	20-39214567-6	martin.fernandez@gmail.com	1989-06-18	Argentina	Martin	Ingeniero Civil	RI	3516123456	3	\N
40567891	DNI	Quiroga	\N	laura.quiroga@hotmail.com	1994-02-11	Argentina	Laura	Diseñadora de Interiores	CF	3425987765	4	\N
41877234	DNI	Bustamante	\N	florencia.bustamante@gmail.com	1998-04-27	Argentina	Florencia	Estudiante	CF	3516889900	8	\N
36789456	DNI	Ibarra	20-36789456-1	roberto.ibarra@outlook.com	1981-12-09	Argentina	Roberto	Transportista	RI	1144556677	9	\N
41005678	DNI	Peralta	\N	melina.peralta@gmail.com	1997-07-15	Argentina	Melina	Psicologa	CF	3424558899	13	\N
35499876	DNI	Gimenez	27-35499876-8	andrea.gimenez@hotmail.com	1979-09-21	Argentina	Andrea	Docente	RI	3424123456	17	\N
42678123	DNI	Cabrera	\N	luciano.cabrera@gmail.com	2001-01-30	Argentina	Luciano	Estudiante Universitario	CF	1133445566	18	\N
39988765	DNI	Morales	20-39988765-2	soledad.morales@gmail.com	1991-05-06	Argentina	Soledad	Administrativa	CF	3415667788	19	\N
37123498	DNI	Paredes	23-37123498-5	juan.paredes@yahoo.com	1984-03-12	Argentina	Juan	Tecnico Electronico	RI	3415990011	20	\N
38765432	DNI	Rios	20-38765432-1	marcos.rios@gmail.com	1987-03-10	Argentina	Marcos	Analista de Sistemas	RI	3416122233	30	\N
40991234	DNI	Luna	\N	paula.luna@hotmail.com	1995-08-22	Argentina	Paula	Licenciada en Marketing	CF	3424889900	31	\N
37222345	DNI	Castro	23-37222345-9	sebastian.castro@yahoo.com	1983-01-18	Argentina	Sebastian	Comerciante	MT	3415332211	32	\N
41890123	DNI	Vega	\N	carolina.vega@gmail.com	1999-11-02	Argentina	Carolina	Estudiante	CF	3516778899	33	\N
42567890	DNI	Ojeda	\N	julian.ojeda@gmail.com	2000-09-14	Argentina	Julian	Estudiante Universitario	CF	1133224455	35	\N
39444567	DNI	Farias	27-39444567-6	veronica.farias@hotmail.com	1991-12-05	Argentina	Veronica	Recursos Humanos	RI	3424556677	36	\N
37888901	DNI	Silva	\N	leandro.silva@gmail.com	1985-04-09	Argentina	Leandro	Vendedor	CF	2615338899	37	\N
40222334	DNI	Reynoso	20-40222334-2	gabriela.reynoso@gmail.com	1993-07-26	Argentina	Gabriela	Contadora	RI	3516001122	38	\N
36999887	DNI	Nuñez	\N	tomas.nunez@yahoo.com	1982-02-16	Argentina	Tomas	Supervisor de Planta	CF	3814559900	39	\N
38123999	DNI	ALVARES	23-38123999-4	diego.alvarez@yahoo.com	1986-10-02	Argentina	Diego	Comerciante	MT	3415345678	7	\N
\.


--
-- TOC entry 3147 (class 0 OID 33591)
-- Dependencies: 213
-- Data for Name: medio_pago; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.medio_pago (tipo_medio, id_mediodepago, fecha, monto, banco, numero_cheque, plazo, tipo_moneda, cuotas, dni_titular) FROM stdin;
\.


--
-- TOC entry 3149 (class 0 OID 33602)
-- Dependencies: 215
-- Data for Name: nota_credito; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.nota_credito (id_notacredito, importe) FROM stdin;
\.


--
-- TOC entry 3151 (class 0 OID 33610)
-- Dependencies: 217
-- Data for Name: pago; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.pago (id_pago, fecha, monto, factura) FROM stdin;
\.


--
-- TOC entry 3152 (class 0 OID 33616)
-- Dependencies: 218
-- Data for Name: pago_medio; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.pago_medio (id_pago, id_mediodepago) FROM stdin;
\.


--
-- TOC entry 3142 (class 0 OID 25196)
-- Dependencies: 208
-- Data for Name: persona_fisica; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.persona_fisica (id_responsablepago, dni, tipodocumento) FROM stdin;
3	41222333	DNI
1	40991234	DNI
\.


--
-- TOC entry 3143 (class 0 OID 25204)
-- Dependencies: 209
-- Data for Name: persona_juridica; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.persona_juridica (cuit, razonsocial, id_responsablepago, id_direccion_empresa) FROM stdin;
30-70112233-4	GlobalNet SRL	4	18
33-90011223-7	Estancias del Río S.A.	5	19
\.


--
-- TOC entry 3158 (class 0 OID 42134)
-- Dependencies: 224
-- Data for Name: relac_estadia_huesped; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.relac_estadia_huesped (dni, tipodocumento, id_estadia) FROM stdin;
38765432	DNI	301
40991234	DNI	302
37222345	DNI	303
41890123	DNI	304
42567890	DNI	305
39444567	DNI	305
37888901	DNI	306
40222334	DNI	306
36999887	DNI	307
39214567	DNI	307
40567891	DNI	308
38123999	DNI	308
38765432	DNI	309
40991234	DNI	309
37222345	DNI	309
41890123	DNI	309
42567890	DNI	310
39444567	DNI	310
37888901	DNI	310
40222334	DNI	310
36999887	DNI	310
39214567	DNI	311
40567891	DNI	311
38123999	DNI	312
41890123	DNI	312
\.


--
-- TOC entry 3157 (class 0 OID 42073)
-- Dependencies: 223
-- Data for Name: reserva; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.reserva (id_reserva, apellido, estado, fecha_desde, fecha_hasta, nombre, telefono, nro_habitacion) FROM stdin;
201	Gomez	FINALIZADA	2025-11-02	2025-11-04	Lucia	3416112233	101
203	Molina	PENDIENTE	2025-11-20	2025-11-23	Sergio	2615334455	103
204	Perez	FINALIZADA	2025-11-03	2025-11-07	Mariana	3424778899	201
205	Ramos	FINALIZADA	2025-11-10	2025-11-14	Federico	1144556677	202
206	Suarez	PENDIENTE	2025-11-24	2025-11-28	Natalia	3516998877	203
207	Lopez	FINALIZADA	2025-11-05	2025-11-10	Agustin	3415889900	301
208	Fernandez	FINALIZADA	2025-11-12	2025-11-16	Camila	3424667788	302
209	Sanchez	FINALIZADA	2025-11-08	2025-11-15	Carlos	3516443322	401
210	Benitez	PENDIENTE	2025-11-18	2025-11-25	Laura	3415776655	402
211	Diaz	FINALIZADA	2025-11-09	2025-11-13	Martin	2615223344	501
212	Romero	FINALIZADA	2025-11-16	2025-11-20	Valentina	3516332211	502
202	Torres	FINALIZADA	2025-12-31	2025-11-09	Adrian	3516223344	102
1	DA SILVA	PENDIENTE	2025-12-16	2025-12-20	VALENTIN	3425986867	101
\.


--
-- TOC entry 3145 (class 0 OID 25214)
-- Dependencies: 211
-- Data for Name: responsablepago; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.responsablepago (id_responsablepago, id_direccion) FROM stdin;
1	14
3	17
4	18
5	19
\.


--
-- TOC entry 3135 (class 0 OID 16984)
-- Dependencies: 201
-- Data for Name: servicio_extra; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.servicio_extra (id_estadia, id_servicio, precio, tipo_servicio) FROM stdin;
\.


--
-- TOC entry 3136 (class 0 OID 16989)
-- Dependencies: 202
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: pili
--

COPY public.usuario (nombre, contrasenia) FROM stdin;
\.


--
-- TOC entry 3172 (class 0 OID 0)
-- Dependencies: 204
-- Name: direccion_id_direccion_seq; Type: SEQUENCE SET; Schema: public; Owner: pili
--

SELECT pg_catalog.setval('public.direccion_id_direccion_seq', 39, true);


--
-- TOC entry 3173 (class 0 OID 0)
-- Dependencies: 206
-- Name: estadia_id_estadia_seq; Type: SEQUENCE SET; Schema: public; Owner: pili
--

SELECT pg_catalog.setval('public.estadia_id_estadia_seq', 5, true);


--
-- TOC entry 3174 (class 0 OID 0)
-- Dependencies: 220
-- Name: factura_id_seq; Type: SEQUENCE SET; Schema: public; Owner: pili
--

SELECT pg_catalog.setval('public.factura_id_seq', 7, true);


--
-- TOC entry 3175 (class 0 OID 0)
-- Dependencies: 212
-- Name: medio_pago_id_mediodepago_seq; Type: SEQUENCE SET; Schema: public; Owner: pili
--

SELECT pg_catalog.setval('public.medio_pago_id_mediodepago_seq', 1, false);


--
-- TOC entry 3176 (class 0 OID 0)
-- Dependencies: 214
-- Name: nota_credito_id_notacredito_seq; Type: SEQUENCE SET; Schema: public; Owner: pili
--

SELECT pg_catalog.setval('public.nota_credito_id_notacredito_seq', 1, false);


--
-- TOC entry 3177 (class 0 OID 0)
-- Dependencies: 216
-- Name: pago_id_pago_seq; Type: SEQUENCE SET; Schema: public; Owner: pili
--

SELECT pg_catalog.setval('public.pago_id_pago_seq', 1, false);


--
-- TOC entry 3178 (class 0 OID 0)
-- Dependencies: 222
-- Name: reserva_id_reserva_seq; Type: SEQUENCE SET; Schema: public; Owner: pili
--

SELECT pg_catalog.setval('public.reserva_id_reserva_seq', 1, true);


--
-- TOC entry 3179 (class 0 OID 0)
-- Dependencies: 210
-- Name: responsablepago_id_responsablepago_seq; Type: SEQUENCE SET; Schema: public; Owner: pili
--

SELECT pg_catalog.setval('public.responsablepago_id_responsablepago_seq', 5, true);


--
-- TOC entry 3180 (class 0 OID 0)
-- Dependencies: 203
-- Name: responsablepago_seq; Type: SEQUENCE SET; Schema: public; Owner: pili
--

SELECT pg_catalog.setval('public.responsablepago_seq', 1, false);


-- Completed on 2025-12-16 21:15:39

--
-- PostgreSQL database dump complete
--

\unrestrict vNKMDBEytPndoITWk2BvrihjJ9WdAMFjqXbAW3Z7nyKyoy95ile4IOTvdpcRTT3


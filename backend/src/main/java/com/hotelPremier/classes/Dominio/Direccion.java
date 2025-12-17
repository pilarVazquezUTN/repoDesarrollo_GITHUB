    package com.hotelPremier.classes.Dominio;

    import jakarta.persistence.*;

    @Entity
    @Table(name="direccion")
    public class Direccion {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_direccion")
        private Integer id_direccion;

        @Column(name="calle")
        private String calle;

        @Column(name="numero")
        private Integer numero;

        @Column(name="localidad")
        private String localidad;

        @Column(name="departamento")
        private String departamento;

        @Column(name="piso")
        private Integer piso;

        @Column(name="codigopostal")
        private Integer codigoPostal;

        @Column(name="provincia")
        private String provincia;

        @Column(name="pais")
        private String pais;

        public Integer getID() { return id_direccion; }
        public void setID(Integer id){ this.id_direccion = id; }

        public String getCalle() { return calle; }
        public void setCalle(String calle) { this.calle = calle; }

        public Integer getNumero() { return numero; }
        public void setNumero(Integer numero) { this.numero = numero; }

        public String getLocalidad() { return localidad; }
        public void setLocalidad(String localidad) { this.localidad = localidad; }

        public String getDepartamento() { return departamento; }
        public void setDepartamento(String departamento) { this.departamento = departamento; }

        public Integer getPiso() { return piso; }
        public void setPiso(Integer piso) { this.piso = piso; }

        public Integer getCodigoPostal() { return codigoPostal; }
        public void setCodigoPostal(Integer codigoPostal) { this.codigoPostal = codigoPostal; }

        public String getProvincia() { return provincia; }
        public void setProvincia(String provincia) { this.provincia = provincia; }

        public String getPais() { return pais; }
        public void setPais(String pais) { this.pais = pais; }
    }

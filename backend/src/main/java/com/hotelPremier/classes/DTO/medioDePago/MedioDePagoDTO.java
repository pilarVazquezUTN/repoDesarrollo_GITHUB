package com.hotelPremier.classes.DTO.medioDePago;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "tipo",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ChequeDTO.class,            name = "CHEQUE"),
    @JsonSubTypes.Type(value = TarjetaCreditoDTO.class,    name = "TARJETA_CREDITO"),
    @JsonSubTypes.Type(value = TarjetaDebitoDTO.class,     name = "TARJETA_DEBITO"),
    @JsonSubTypes.Type(value = MonedaLocalDTO.class,       name = "MONEDA_LOCAL"),
    @JsonSubTypes.Type(value = MonedaExtranjeraDTO.class,  name = "MONEDA_EXTRANJERA")
})
public class MedioDePagoDTO {

    // IMPORTANTE: este nombre tiene que coincidir con el "name" del @JsonSubTypes
    private String tipo;

    private Integer id_mediodepago;
    private float monto;
    private Date fecha;

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getId_mediodepago() { return id_mediodepago; }
    public void setId_mediodepago(Integer id_mediodepago) { this.id_mediodepago = id_mediodepago; }

    public float getMonto() { return monto; }
    public void setMonto(float monto) { this.monto = monto; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}

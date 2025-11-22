/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author monic
 */
public class DetalleAmortizacion {
    private String idDetalle;
    private String idIntangible; // Relación con intangible
    private int numero_cuota;
    private double monto;
    private double amortizacionMensual;
    private double amortizacionAnual;
    private String fechaCuota;
    private String fechaRegistro; // Fecha en que se registró la amortización
    private int mes; // Mes de la amortización (1-12)
    private int anio; // Año de la amortización

    public DetalleAmortizacion() {
    }

    public DetalleAmortizacion(String idDetalle, String idIntangible, int numero_cuota, double monto, 
                               double amortizacionMensual, double amortizacionAnual, String fechaCuota,
                               String fechaRegistro, int mes, int anio) {
        this.idDetalle = idDetalle;
        this.idIntangible = idIntangible;
        this.numero_cuota = numero_cuota;
        this.monto = monto;
        this.amortizacionMensual = amortizacionMensual;
        this.amortizacionAnual = amortizacionAnual;
        this.fechaCuota = fechaCuota;
        this.fechaRegistro = fechaRegistro;
        this.mes = mes;
        this.anio = anio;
    }

    public String getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(String idDetalle) {
        this.idDetalle = idDetalle;
    }

    public String getIdIntangible() {
        return idIntangible;
    }

    public void setIdIntangible(String idIntangible) {
        this.idIntangible = idIntangible;
    }

    public int getNumero_cuota() {
        return numero_cuota;
    }

    public void setNumero_cuota(int numero_cuota) {
        this.numero_cuota = numero_cuota;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getAmortizacionMensual() {
        return amortizacionMensual;
    }

    public void setAmortizacionMensual(double amortizacionMensual) {
        this.amortizacionMensual = amortizacionMensual;
    }

    public double getAmortizacionAnual() {
        return amortizacionAnual;
    }

    public void setAmortizacionAnual(double amortizacionAnual) {
        this.amortizacionAnual = amortizacionAnual;
    }

    public String getFechaCuota() {
        return fechaCuota;
    }

    public void setFechaCuota(String fechaCuota) {
        this.fechaCuota = fechaCuota;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }
}

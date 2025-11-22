/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.math.BigDecimal;

/**
 * Modelo para representar una fila del reporte de amortización
 * @author monic
 */
public class Reporte {
    private String codigo;
    private String nombre;
    private String tipo;
    private String fecha;
    private BigDecimal costo;
    private int vidaAnios;
    private BigDecimal amortAnual;
    private BigDecimal amortMensual;
    private BigDecimal amortPeriodo;
    private BigDecimal amortAcum;
    private BigDecimal valorLibros;
    private int cuotasPend;

    public Reporte() {
    }

    public Reporte(String codigo, String nombre, String tipo, String fecha, BigDecimal costo,
                   int vidaAnios, BigDecimal amortAnual, BigDecimal amortMensual,
                   BigDecimal amortPeriodo, BigDecimal amortAcum, BigDecimal valorLibros,
                   int cuotasPend) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fecha = fecha;
        this.costo = costo;
        this.vidaAnios = vidaAnios;
        this.amortAnual = amortAnual;
        this.amortMensual = amortMensual;
        this.amortPeriodo = amortPeriodo;
        this.amortAcum = amortAcum;
        this.valorLibros = valorLibros;
        this.cuotasPend = cuotasPend;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public int getVidaAnios() {
        return vidaAnios;
    }

    public void setVidaAnios(int vidaAnios) {
        this.vidaAnios = vidaAnios;
    }

    public BigDecimal getAmortAnual() {
        return amortAnual;
    }

    public void setAmortAnual(BigDecimal amortAnual) {
        this.amortAnual = amortAnual;
    }

    public BigDecimal getAmortMensual() {
        return amortMensual;
    }

    public void setAmortMensual(BigDecimal amortMensual) {
        this.amortMensual = amortMensual;
    }

    public BigDecimal getAmortPeriodo() {
        return amortPeriodo;
    }

    public void setAmortPeriodo(BigDecimal amortPeriodo) {
        this.amortPeriodo = amortPeriodo;
    }

    public BigDecimal getAmortAcum() {
        return amortAcum;
    }

    public void setAmortAcum(BigDecimal amortAcum) {
        this.amortAcum = amortAcum;
    }

    public BigDecimal getValorLibros() {
        return valorLibros;
    }

    public void setValorLibros(BigDecimal valorLibros) {
        this.valorLibros = valorLibros;
    }

    public int getCuotasPend() {
        return cuotasPend;
    }

    public void setCuotasPend(int cuotasPend) {
        this.cuotasPend = cuotasPend;
    }
}

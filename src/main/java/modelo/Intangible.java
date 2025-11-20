/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author monic
 */
public class Intangible {
    String idIntangible;
    String version;
    String nombre_intangible;
    String nombre_proveedor;
    String tipoLicencia;
    String codigo;
    double costo;
    String vidaUtil;
    String estado;
    String fechaAdquisicion;
    String fechaVencimiento;
    String modalidad_amortizacion;

    public Intangible(String idIntangible, String version, String nombre_intangible, String nombre_proveedor, String tipoLicencia, String codigo, double costo, String vidaUtil, String estado, String fechaAdquisicion, String fechaVencimiento) {
        this.idIntangible = idIntangible;
        this.version = version;
        this.nombre_intangible = nombre_intangible;
        this.nombre_proveedor = nombre_proveedor;
        this.tipoLicencia = tipoLicencia;
        this.codigo = codigo;
        this.costo = costo;
        this.vidaUtil = vidaUtil;
        this.estado = estado;
        this.fechaAdquisicion = fechaAdquisicion;
        this.fechaVencimiento = fechaVencimiento;
    }

    public Intangible(String modalidad_amortizacion) {
        this.modalidad_amortizacion = modalidad_amortizacion;
    }

    public Intangible() {
        
    }

    public String getIdIntangible() {
        return idIntangible;
    }

    public void setIdIntangible(String idIntangible) {
        this.idIntangible = idIntangible;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNombre_intangible() {
        return nombre_intangible;
    }

    public void setNombre_intangible(String nombre_intangible) {
        this.nombre_intangible = nombre_intangible;
    }

    public String getNombre_proveedor() {
        return nombre_proveedor;
    }

    public void setNombre_proveedor(String nombre_proveedor) {
        this.nombre_proveedor = nombre_proveedor;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public String getVidaUtil() {
        return vidaUtil;
    }

    public void setVidaUtil(String vidaUtil) {
        this.vidaUtil = vidaUtil;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(String fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getModalidad_amortizacion() {
        return modalidad_amortizacion;
    }

    public void setModalidad_amortizacion(String modalidad_amortizacion) {
        this.modalidad_amortizacion = modalidad_amortizacion;
    }
    
    
    
    

}

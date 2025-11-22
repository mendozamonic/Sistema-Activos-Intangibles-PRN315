/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Modelo para representar el historial de amortizaciones (trazabilidad)
 * @author monic
 */
public class Historial {
    // Campos para amortizaciones
    private String idDetalle;           // ID de la amortización
    private String idIntangible;        // ID del intangible
    private String codigo;              // Código del intangible
    private String nombreIntangible;    // Nombre del intangible
    private String proveedor;           // Proveedor
    private String tipoLicencia;        // Tipo de licencia
    private int numeroCuota;            // Número de cuota
    private double monto;               // Monto amortizado
    private double amortizacionMensual; // Amortización mensual
    private double amortizacionAnual;   // Amortización anual
    private String fechaCuota;          // Fecha de la cuota (mes/año)
    private String fechaRegistro;       // Fecha en que se registró
    private int mes;                    // Mes de la cuota
    private int anio;                   // Año de la cuota
    
    // Campos contextuales y cálculos para trazabilidad mejorada
    private double costoOriginal;        // Costo original del intangible
    private String vidaUtil;             // Vida útil del intangible
    private String fechaAdquisicion;     // Fecha de adquisición
    private int totalCuotas;             // Total de cuotas esperadas
    private double amortizacionAcumulada; // Amortización acumulada hasta esta cuota
    private double valorEnLibros;        // Valor en libros después de esta cuota
    private double porcentajeCompletado; // Porcentaje de amortización completado
    
    // Campos legacy (mantenidos para compatibilidad si se necesitan)
    private String idHistorial;
    private String idCompra;
    private String idReporte;           // ID del reporte relacionado
    private String fechaCompra;
    private String fechaVencimiento;
    private String idUsuario;
    private String nombreUsuario;
    private String version;

    // Getters y Setters para amortizaciones
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreIntangible() {
        return nombreIntangible;
    }

    public void setNombreIntangible(String nombreIntangible) {
        this.nombreIntangible = nombreIntangible;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
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

    // Getters y Setters para campos contextuales y cálculos
    public double getCostoOriginal() {
        return costoOriginal;
    }

    public void setCostoOriginal(double costoOriginal) {
        this.costoOriginal = costoOriginal;
    }

    public String getVidaUtil() {
        return vidaUtil;
    }

    public void setVidaUtil(String vidaUtil) {
        this.vidaUtil = vidaUtil;
    }

    public String getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(String fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public int getTotalCuotas() {
        return totalCuotas;
    }

    public void setTotalCuotas(int totalCuotas) {
        this.totalCuotas = totalCuotas;
    }

    public double getAmortizacionAcumulada() {
        return amortizacionAcumulada;
    }

    public void setAmortizacionAcumulada(double amortizacionAcumulada) {
        this.amortizacionAcumulada = amortizacionAcumulada;
    }

    public double getValorEnLibros() {
        return valorEnLibros;
    }

    public void setValorEnLibros(double valorEnLibros) {
        this.valorEnLibros = valorEnLibros;
    }

    public double getPorcentajeCompletado() {
        return porcentajeCompletado;
    }

    public void setPorcentajeCompletado(double porcentajeCompletado) {
        this.porcentajeCompletado = porcentajeCompletado;
    }

    // Getters y Setters legacy (para compatibilidad)
    public String getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(String idHistorial) {
        this.idHistorial = idHistorial;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public String getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(String idReporte) {
        this.idReporte = idReporte;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

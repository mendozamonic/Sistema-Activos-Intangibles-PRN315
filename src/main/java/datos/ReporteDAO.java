/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.DetalleAmortizacion;
import modelo.Intangible;
import modelo.Reporte;

/**
 * DAO para generar reportes de amortización
 * @author monic
 */
public class ReporteDAO {
    
    private static final RoundingMode RM = RoundingMode.HALF_UP;
    
    /**
     * Genera el reporte de amortización para un período específico
     * @param modo "mes" o "anio"
     * @param mes Mes del reporte (1-12)
     * @param anio Año del reporte
     * @return Lista de reportes por intangible
     * @throws SQLException Si hay error al acceder a la base de datos
     */
    public List<Reporte> generarReporte(String modo, int mes, int anio) throws SQLException {
        List<Reporte> reporte = new ArrayList<>();
        
        // Obtener todos los intangibles
        IntangibleDAO intangibleDAO = new IntangibleDAO();
        List<Intangible> listaIntangibles = intangibleDAO.listarIntangibles();
        
        // Obtener DAO de amortizaciones
        DetalleAmortizacionDAO amortizacionDAO = new DetalleAmortizacionDAO();
        
        for (Intangible intangible : listaIntangibles) {
            BigDecimal costo = BigDecimal.valueOf(intangible.getCosto());
            int vidaAnios = parseVidaAnios(intangible.getVidaUtil());
            int totalMeses = vidaAnios * 12;
            
            // Calcular amortizaciones teóricas
            BigDecimal amortAnual = div2(costo, vidaAnios);
            BigDecimal amortMensual = div2(costo, totalMeses);
            
            // Obtener amortizaciones registradas
            List<DetalleAmortizacion> amortizacionesRegistradas;
            try {
                amortizacionesRegistradas = amortizacionDAO.listarPorIntangible(intangible.getIdIntangible());
            } catch (SQLException e) {
                amortizacionesRegistradas = new ArrayList<>();
            }
            
            // Calcular amortización acumulada REAL (suma de las registradas)
            BigDecimal amortAcumReal = calcularAmortizacionAcumulada(amortizacionesRegistradas, costo);
            
            // Calcular amortización del período específico (mes o año)
            BigDecimal amortPeriodo = calcularAmortizacionPeriodo(amortizacionesRegistradas, modo, mes, anio);
            
            // Calcular valor en libros
            BigDecimal valorLibros = calcularValorEnLibros(costo, amortAcumReal);
            
            // Calcular cuotas pendientes
            int cuotasPend = calcularCuotasPendientes(totalMeses, amortizacionesRegistradas.size());
            
            // Crear objeto Reporte
            Reporte reporteFila = new Reporte(
                    intangible.getCodigo(),
                    intangible.getNombre_intangible(),
                    intangible.getTipoLicencia(),
                    intangible.getFechaAdquisicion(),
                    costo,
                    vidaAnios,
                    amortAnual,
                    amortMensual,
                    amortPeriodo,
                    amortAcumReal,
                    valorLibros,
                    cuotasPend
            );
            
            reporte.add(reporteFila);
        }
        
        return reporte;
    }
    
    /**
     * Calcula la amortización acumulada real sumando todas las amortizaciones registradas
     */
    private BigDecimal calcularAmortizacionAcumulada(List<DetalleAmortizacion> amortizacionesRegistradas, BigDecimal costo) {
        BigDecimal amortAcumReal = BigDecimal.ZERO;
        for (DetalleAmortizacion detalle : amortizacionesRegistradas) {
            amortAcumReal = amortAcumReal.add(BigDecimal.valueOf(detalle.getMonto()));
        }
        amortAcumReal = amortAcumReal.setScale(2, RM);
        
        // Limitar la amortización acumulada al costo máximo
        if (amortAcumReal.compareTo(costo) > 0) {
            amortAcumReal = costo;
        }
        
        return amortAcumReal;
    }
    
    /**
     * Calcula la amortización del período específico (mes o año)
     */
    private BigDecimal calcularAmortizacionPeriodo(List<DetalleAmortizacion> amortizacionesRegistradas, 
                                                     String modo, int mes, int anio) {
        BigDecimal amortPeriodo = BigDecimal.ZERO;
        
        if ("anio".equalsIgnoreCase(modo)) {
            // Sumar todas las amortizaciones registradas en ese año
            for (DetalleAmortizacion detalle : amortizacionesRegistradas) {
                if (detalle.getAnio() == anio) {
                    amortPeriodo = amortPeriodo.add(BigDecimal.valueOf(detalle.getMonto()));
                }
            }
        } else {
            // Sumar amortizaciones registradas en ese mes/año específico
            for (DetalleAmortizacion detalle : amortizacionesRegistradas) {
                if (detalle.getMes() == mes && detalle.getAnio() == anio) {
                    amortPeriodo = amortPeriodo.add(BigDecimal.valueOf(detalle.getMonto()));
                }
            }
        }
        
        return amortPeriodo.setScale(2, RM);
    }
    
    /**
     * Calcula el valor en libros (costo - amortización acumulada)
     */
    private BigDecimal calcularValorEnLibros(BigDecimal costo, BigDecimal amortAcumReal) {
        BigDecimal valorLibros = costo.subtract(amortAcumReal);
        if (valorLibros.signum() < 0) {
            valorLibros = BigDecimal.ZERO;
        }
        return valorLibros;
    }
    
    /**
     * Calcula las cuotas pendientes de amortización
     */
    private int calcularCuotasPendientes(int totalMeses, int cuotasRegistradas) {
        return Math.max(0, totalMeses - cuotasRegistradas);
    }
    
    /**
     * Divide un BigDecimal entre un entero con 2 decimales
     */
    private BigDecimal div2(BigDecimal a, int b) {
        if (a == null || b <= 0) {
            return BigDecimal.ZERO;
        }
        return a.divide(BigDecimal.valueOf(b), 2, RM);
    }
    
    /**
     * Parsea la vida útil de string a años
     * Vida útil: 12 meses = 1 año, 18 meses = 2 años, "12" = 12 años
     */
    private int parseVidaAnios(String vida) {
        if (vida == null) return 1;
        String s = vida.toLowerCase().trim();
        String num = s.replaceAll("\\D+", "");
        if (num.isEmpty()) return 1;
        
        int n = Integer.parseInt(num);
        
        if (s.contains("mes")) {
            return Math.max(1, (int) Math.ceil(n / 12.0));
        }
        return Math.max(1, n);
    }
}


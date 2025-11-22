package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.DetalleAmortizacion;
import modelo.Historial;
import modelo.Intangible;

public class HistorialDAO {

    public static class Filtros {
        public String proveedor;      // nombre proveedor
        public String codigo;         // codigo_ of intangible
        public String nombreIntangible; // nombre del intangible
        public String tipoLicencia;   // tipo de licencia
        public String idIntangible;    // ID específico del intangible
        public String fechaDesde;     // yyyy-MM-dd (fecha de cuota)
        public String fechaHasta;     // yyyy-MM-dd (fecha de cuota)
        public Integer mes;            // Mes específico (1-12)
        public Integer anio;           // Año específico
        public int offset = 0;
        public int limit = 10;
    }

    public List<Historial> listar(Filtros f) throws SQLException {
        List<Historial> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
           .append(" da.iddetalle, da.idintangible, da.numero_cuota, da.monto, ")
           .append(" da.amortizacion_mensual, da.amortizacion_anual, ")
           .append(" da.fecha_cuota, ")
           .append(" EXTRACT(MONTH FROM da.fecha_cuota)::int AS mes, ")
           .append(" EXTRACT(YEAR FROM da.fecha_cuota)::int AS anio, ")
           .append(" i.codigo_, i.nombre_intangible, i.nombre_proveedor, i.tipo_licencia_, ")
           .append(" i.costo AS costo_original, i.vida_util, i.fecha_adquisicion, ")
           .append(" (SELECT COALESCE(SUM(d2.monto), 0) ")
           .append("   FROM detalleamortizacion d2 ")
           .append("   WHERE d2.idintangible = da.idintangible ")
           .append("     AND (d2.fecha_cuota < da.fecha_cuota ")
           .append("          OR (d2.fecha_cuota = da.fecha_cuota AND d2.numero_cuota <= da.numero_cuota)) ")
           .append(" ) AS amortizacion_acumulada ")
           .append(" FROM detalleamortizacion da")
           .append(" JOIN intangible i ON i.idintangible = da.idintangible")
           .append(" WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (f != null) {
            if (f.idIntangible != null && !f.idIntangible.trim().isEmpty()) {
                sql.append(" AND i.idintangible = ?");
                params.add(f.idIntangible.trim());
            }
            if (f.nombreIntangible != null && !f.nombreIntangible.trim().isEmpty()) {
                sql.append(" AND LOWER(i.nombre_intangible) LIKE LOWER(?)");
                params.add("%" + f.nombreIntangible.trim() + "%");
            }
        }

        sql.append(" ORDER BY da.fecha_cuota DESC, da.numero_cuota DESC");
        sql.append(" LIMIT ? OFFSET ?");

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            for (Object p : params) {
                if (p instanceof java.sql.Date) {
                    ps.setDate(idx++, (java.sql.Date) p);
                } else if (p instanceof Integer) {
                    ps.setInt(idx++, (Integer) p);
                } else if (p != null) {
                    ps.setString(idx++, p.toString());
                }
            }
            int limit = (f != null && f.limit > 0) ? f.limit : 10;
            int offset = (f != null && f.offset >= 0) ? f.offset : 0;
            ps.setInt(idx++, limit);
            ps.setInt(idx, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Historial h = new Historial();
                    h.setIdDetalle(rs.getString("iddetalle"));
                    h.setIdIntangible(rs.getString("idintangible"));
                    h.setCodigo(rs.getString("codigo_"));
                    h.setNombreIntangible(rs.getString("nombre_intangible"));
                    h.setProveedor(rs.getString("nombre_proveedor"));
                    h.setTipoLicencia(rs.getString("tipo_licencia_"));
                    h.setNumeroCuota(rs.getInt("numero_cuota"));
                    h.setMonto(rs.getDouble("monto"));
                    h.setAmortizacionMensual(rs.getDouble("amortizacion_mensual"));
                    h.setAmortizacionAnual(rs.getDouble("amortizacion_anual"));
                    
                    // Información contextual del intangible
                    h.setCostoOriginal(rs.getDouble("costo_original"));
                    h.setVidaUtil(rs.getString("vida_util"));
                    java.sql.Date fechaAdq = rs.getDate("fecha_adquisicion");
                    if (fechaAdq != null) {
                        h.setFechaAdquisicion(fechaAdq.toString());
                    }
                    
                    java.sql.Date fechaCuota = rs.getDate("fecha_cuota");
                    if (fechaCuota != null) {
                        h.setFechaCuota(fechaCuota.toString());
                        h.setFechaRegistro(fechaCuota.toString());
                    }
                    
                    h.setMes(rs.getInt("mes"));
                    h.setAnio(rs.getInt("anio"));
                    
                    // Calcular amortización acumulada
                    double amortAcum = rs.getDouble("amortizacion_acumulada");
                    h.setAmortizacionAcumulada(amortAcum);
                    
                    // Calcular valor en libros
                    double costo = h.getCostoOriginal();
                    double valorLibros = Math.max(0, costo - amortAcum);
                    h.setValorEnLibros(valorLibros);
                    
                    // Calcular total de cuotas y porcentaje completado
                    int totalCuotas = calcularTotalCuotas(h.getVidaUtil());
                    h.setTotalCuotas(totalCuotas);
                    
                    if (totalCuotas > 0) {
                        double porcentaje = (h.getNumeroCuota() / (double) totalCuotas) * 100.0;
                        h.setPorcentajeCompletado(Math.min(100.0, porcentaje));
                    } else {
                        h.setPorcentajeCompletado(0.0);
                    }
                    
                    lista.add(h);
                }
            }
        }
        return lista;
    }

    public int contar(Filtros f) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*)")
           .append(" FROM detalleamortizacion da")
           .append(" JOIN intangible i ON i.idintangible = da.idintangible")
           .append(" WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (f != null) {
            if (f.proveedor != null && !f.proveedor.trim().isEmpty()) {
                sql.append(" AND LOWER(i.nombre_proveedor) LIKE LOWER(?)");
                params.add("%" + f.proveedor.trim() + "%");
            }
            if (f.codigo != null && !f.codigo.trim().isEmpty()) {
                sql.append(" AND LOWER(i.codigo_) LIKE LOWER(?)");
                params.add("%" + f.codigo.trim() + "%");
            }
            if (f.nombreIntangible != null && !f.nombreIntangible.trim().isEmpty()) {
                sql.append(" AND LOWER(i.nombre_intangible) LIKE LOWER(?)");
                params.add("%" + f.nombreIntangible.trim() + "%");
            }
            if (f.tipoLicencia != null && !f.tipoLicencia.trim().isEmpty()) {
                sql.append(" AND LOWER(i.tipo_licencia_) LIKE LOWER(?)");
                params.add("%" + f.tipoLicencia.trim() + "%");
            }
            if (f.idIntangible != null && !f.idIntangible.trim().isEmpty()) {
                sql.append(" AND i.idintangible = ?");
                params.add(f.idIntangible.trim());
            }
            if (f.fechaDesde != null && !f.fechaDesde.trim().isEmpty()) {
                sql.append(" AND da.fecha_cuota >= ?");
                params.add(java.sql.Date.valueOf(f.fechaDesde.trim()));
            }
            if (f.fechaHasta != null && !f.fechaHasta.trim().isEmpty()) {
                sql.append(" AND da.fecha_cuota <= ?");
                params.add(java.sql.Date.valueOf(f.fechaHasta.trim()));
            }
            if (f.mes != null && f.mes > 0) {
                sql.append(" AND EXTRACT(MONTH FROM da.fecha_cuota) = ?");
                params.add(f.mes);
            }
            if (f.anio != null && f.anio > 0) {
                sql.append(" AND EXTRACT(YEAR FROM da.fecha_cuota) = ?");
                params.add(f.anio);
            }
        }

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            for (Object p : params) {
                if (p instanceof java.sql.Date) {
                    ps.setDate(idx++, (java.sql.Date) p);
                } else if (p instanceof Integer) {
                    ps.setInt(idx++, (Integer) p);
                } else if (p != null) {
                    ps.setString(idx++, p.toString());
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     * Calcula el total de cuotas basado en la vida útil
     * Ejemplos:
     * - "1 mes" → 1 cuota
     * - "12 meses" → 12 cuotas
     * - "18 meses" → 18 cuotas
     * - "2 años" o "2" → 24 cuotas (2 años × 12 meses)
     * - "3 años" o "3" → 36 cuotas (3 años × 12 meses)
     */
    private int calcularTotalCuotas(String vidaUtil) {
        if (vidaUtil == null) return 12;
        String s = vidaUtil.toLowerCase().trim();
        String num = s.replaceAll("\\D+", "");
        if (num.isEmpty()) return 12;

        int n = Integer.parseInt(num);

        if (s.contains("mes")) {
            // Si contiene "mes", usar directamente el número de meses
            return Math.max(1, n);
        } else {
            // Si no contiene "mes", interpretar como años y multiplicar por 12
            return Math.max(1, n) * 12;
        }
    }
    
    /**
     * Calcula los valores del historial usando la MISMA lógica que listar()
     * Este método reutiliza tus cálculos existentes sin modificarlos
     */
    public Historial calcularValoresHistorial(String idDetalle, String idIntangible) throws SQLException {
        // Obtener el detalle de amortización
        DetalleAmortizacionDAO detalleDAO = new DetalleAmortizacionDAO();
        DetalleAmortizacion detalle = detalleDAO.obtenerPorId(idDetalle);
        
        if (detalle == null) return null;
        
        // Obtener el intangible
        IntangibleDAO intangibleDAO = new IntangibleDAO();
        Intangible intangible = intangibleDAO.obtenerIntangiblePorId(idIntangible);
        
        if (intangible == null) return null;
        
        Historial h = new Historial();
        h.setIdDetalle(detalle.getIdDetalle());
        h.setIdIntangible(detalle.getIdIntangible());
        h.setFechaCuota(detalle.getFechaCuota());
        h.setNumeroCuota(detalle.getNumero_cuota());
        h.setMonto(detalle.getMonto());
        h.setCostoOriginal(intangible.getCosto());
        h.setVidaUtil(intangible.getVidaUtil());
        
        // USAR TU MISMA LÓGICA DE CÁLCULO (copiada de listar())
        // Calcular amortización acumulada usando tu misma subconsulta SQL
        String sqlAcum = "SELECT COALESCE(SUM(d2.monto), 0) " +
                         "FROM detalleamortizacion d2 " +
                         "WHERE d2.idintangible = ? " +
                         "AND (d2.fecha_cuota < ? OR (d2.fecha_cuota = ? AND d2.numero_cuota <= ?))";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlAcum)) {
            ps.setString(1, idIntangible);
            java.sql.Date fechaCuota = java.sql.Date.valueOf(detalle.getFechaCuota());
            ps.setDate(2, fechaCuota);
            ps.setDate(3, fechaCuota);
            ps.setInt(4, detalle.getNumero_cuota());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double amortAcum = rs.getDouble(1);
                    h.setAmortizacionAcumulada(amortAcum);
                    
                    // Calcular valor en libros (TU MISMA FÓRMULA)
                    double costo = h.getCostoOriginal();
                    double valorLibros = Math.max(0, costo - amortAcum);
                    h.setValorEnLibros(valorLibros);
                }
            }
        }
        
        // Calcular total de cuotas y porcentaje (TU MISMA LÓGICA)
        int totalCuotas = calcularTotalCuotas(h.getVidaUtil());
        h.setTotalCuotas(totalCuotas);
        
        if (totalCuotas > 0) {
            double porcentaje = (h.getNumeroCuota() / (double) totalCuotas) * 100.0;
            h.setPorcentajeCompletado(Math.min(100.0, porcentaje));
        } else {
            h.setPorcentajeCompletado(0.0);
        }
        
        return h;
    }
    
    /**
     * Guarda un registro en la tabla historial
     * La tabla historial ahora solo tiene un ID auto-generado (SERIAL)
     * Se inserta un registro cada vez que se registra una amortización para mantener trazabilidad
     */
    public boolean guardarHistorial(Historial h) throws SQLException {
        // La tabla historial solo tiene idhistorial que se genera automáticamente
        // Insertamos un registro vacío para que se genere el ID automáticamente
        String sql = "INSERT INTO historial DEFAULT VALUES";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }
}



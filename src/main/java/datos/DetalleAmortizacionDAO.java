/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.DetalleAmortizacion;
import modelo.Intangible;

/**
 *
 * @author monic
 */
public class DetalleAmortizacionDAO {
    private Connection conexion;
    
    public DetalleAmortizacionDAO() throws SQLException {
        conexion = Conexion.getConnection();
    }
    
    /**
     * Inserta una nueva amortización en la base de datos
     */
    public boolean insertarAmortizacion(DetalleAmortizacion detalle) throws SQLException {
        String sql = "INSERT INTO detalleamortizacion (" +
                     "iddetalle, idintangible, numero_cuota, monto, " +
                     "amortizacion_mensual, amortizacion_anual, fecha_cuota" +
                     ") VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, detalle.getIdDetalle());
            ps.setString(2, detalle.getIdIntangible());
            ps.setInt(3, detalle.getNumero_cuota());
            ps.setDouble(4, detalle.getMonto());
            ps.setDouble(5, detalle.getAmortizacionMensual());
            ps.setDouble(6, detalle.getAmortizacionAnual());
            
            if (detalle.getFechaCuota() != null && !detalle.getFechaCuota().isEmpty()) {
                ps.setDate(7, Date.valueOf(detalle.getFechaCuota()));
            } else {
                ps.setNull(7, Types.DATE);
            }
            
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }
    
    /**
     * Verifica si ya existe una amortización para un intangible en un mes/año específico
     */
    public boolean existeAmortizacion(String idIntangible, int mes, int anio) throws SQLException {
        // Usar EXTRACT para obtener mes y año de la fecha
        String sql = "SELECT COUNT(*) FROM detalleamortizacion " +
                     "WHERE idintangible = ? AND EXTRACT(MONTH FROM fecha_cuota) = ? AND EXTRACT(YEAR FROM fecha_cuota) = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idIntangible);
            ps.setInt(2, mes);
            ps.setInt(3, anio);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    /**
     * Obtiene el siguiente número de cuota para un intangible
     */
    public int obtenerSiguienteNumeroCuota(String idIntangible) throws SQLException {
        String sql = "SELECT COALESCE(MAX(numero_cuota), 0) + 1 " +
                     "FROM detalleamortizacion WHERE idintangible = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idIntangible);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 1;
    }
    
    /**
     * Genera un ID único para detalle amortización (máximo 10 caracteres)
     * Formato: "DET" + número secuencial de 7 dígitos
     */
    public String generarIdDetalle() throws SQLException {
        // Obtener el máximo número usado en IDs que empiezan con "DET"
        String sql = "SELECT iddetalle FROM detalleamortizacion " +
                     "WHERE iddetalle LIKE 'DET%' " +
                     "ORDER BY iddetalle DESC LIMIT 1";
        
        int siguienteNumero = 1;
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String ultimoId = rs.getString("iddetalle");
                if (ultimoId != null && ultimoId.length() >= 4 && ultimoId.startsWith("DET")) {
                    try {
                        String numeroStr = ultimoId.substring(3);
                        siguienteNumero = Integer.parseInt(numeroStr) + 1;
                    } catch (NumberFormatException e) {
                        // Si no se puede parsear, usar timestamp
                        siguienteNumero = (int)(System.currentTimeMillis() % 10000000);
                    }
                }
            }
        } catch (SQLException e) {
            // Si falla, usar timestamp como respaldo
            siguienteNumero = (int)(System.currentTimeMillis() % 10000000);
        }
        
        // Formato: "DET" + número de 7 dígitos (total 10 caracteres)
        String id = "DET" + String.format("%07d", siguienteNumero);
        
        // Verificar que no exista y generar uno nuevo si es necesario
        String sqlCheck = "SELECT COUNT(*) FROM detalleamortizacion WHERE iddetalle = ?";
        int intentos = 0;
        while (intentos < 100) {
            try (PreparedStatement ps = conexion.prepareStatement(sqlCheck)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        return id; // ID único encontrado
                    }
                }
            }
            // Si existe, incrementar el número
            siguienteNumero++;
            id = "DET" + String.format("%07d", siguienteNumero);
            intentos++;
        }
        
        // Si después de 100 intentos no encontramos uno único, usar timestamp
        long timestamp = System.currentTimeMillis();
        return "DET" + String.format("%07d", (int)(timestamp % 10000000));
    }
    
    /**
     * Lista todas las amortizaciones de un intangible específico
     */
    public List<DetalleAmortizacion> listarPorIntangible(String idIntangible) throws SQLException {
        List<DetalleAmortizacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalleamortizacion " +
                     "WHERE idintangible = ? " +
                     "ORDER BY fecha_cuota DESC, numero_cuota";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idIntangible);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleAmortizacion detalle = mapearResultSet(rs);
                    lista.add(detalle);
                }
            }
        }
        return lista;
    }
    
    /**
     * Lista todas las amortizaciones registradas
     */
    public List<DetalleAmortizacion> listarTodas() throws SQLException {
        List<DetalleAmortizacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalleamortizacion " +
                     "ORDER BY fecha_cuota DESC, numero_cuota DESC";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                DetalleAmortizacion detalle = mapearResultSet(rs);
                lista.add(detalle);
            }
        }
        return lista;
    }
    
    /**
     * Obtiene el total de cuotas amortizadas para un intangible
     */
    public int contarCuotasAmortizadas(String idIntangible) throws SQLException {
        String sql = "SELECT COUNT(*) FROM detalleamortizacion WHERE idintangible = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idIntangible);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     * Obtiene la amortización acumulada real (suma de montos registrados) para un intangible
     */
    public double obtenerAmortizacionAcumulada(String idIntangible) throws SQLException {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM detalleamortizacion WHERE idintangible = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idIntangible);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }
    
    /**
     * Obtiene información del intangible con sus amortizaciones registradas
     */
    public Intangible obtenerIntangibleConAmortizaciones(String idIntangible) throws SQLException {
        IntangibleDAO intangibleDAO = new IntangibleDAO();
        return intangibleDAO.obtenerIntangiblePorId(idIntangible);
    }
    
    /**
     * Mapea un ResultSet a un objeto DetalleAmortizacion
     */
    private DetalleAmortizacion mapearResultSet(ResultSet rs) throws SQLException {
        DetalleAmortizacion detalle = new DetalleAmortizacion();
        
        detalle.setIdDetalle(rs.getString("iddetalle"));
        detalle.setIdIntangible(rs.getString("idintangible"));
        detalle.setNumero_cuota(rs.getInt("numero_cuota"));
        detalle.setMonto(rs.getDouble("monto"));
        detalle.setAmortizacionMensual(rs.getDouble("amortizacion_mensual"));
        detalle.setAmortizacionAnual(rs.getDouble("amortizacion_anual"));
        
        Date fechaCuota = rs.getDate("fecha_cuota");
        if (fechaCuota != null) {
            LocalDate fecha = fechaCuota.toLocalDate();
            detalle.setFechaCuota(fecha.toString());
            // Extraer mes y año de la fecha
            detalle.setMes(fecha.getMonthValue());
            detalle.setAnio(fecha.getYear());
            // Usar fecha_cuota como fecha_registro también (ya que no existe esa columna)
            detalle.setFechaRegistro(fecha.toString());
        } else {
            detalle.setMes(0);
            detalle.setAnio(0);
        }
        
        return detalle;
    }
    
    /**
     * Obtiene una amortización por su ID
     */
    public DetalleAmortizacion obtenerPorId(String idDetalle) throws SQLException {
        String sql = "SELECT * FROM detalleamortizacion WHERE iddetalle = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idDetalle);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Actualiza una amortización existente
     */
    public boolean actualizarAmortizacion(DetalleAmortizacion detalle) throws SQLException {
        String sql = "UPDATE detalleamortizacion SET " +
                     "idintangible = ?, numero_cuota = ?, monto = ?, " +
                     "amortizacion_mensual = ?, amortizacion_anual = ?, fecha_cuota = ? " +
                     "WHERE iddetalle = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, detalle.getIdIntangible());
            ps.setInt(2, detalle.getNumero_cuota());
            ps.setDouble(3, detalle.getMonto());
            ps.setDouble(4, detalle.getAmortizacionMensual());
            ps.setDouble(5, detalle.getAmortizacionAnual());
            
            if (detalle.getFechaCuota() != null && !detalle.getFechaCuota().isEmpty()) {
                ps.setDate(6, Date.valueOf(detalle.getFechaCuota()));
            } else {
                ps.setNull(6, Types.DATE);
            }
            
            ps.setString(7, detalle.getIdDetalle());
            
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }
    
    /**
     * Verifica si existe una amortización para un intangible en un mes/año específico, excluyendo un ID específico
     */
    public boolean existeAmortizacionExcluyendo(String idIntangible, int mes, int anio, String idDetalleExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM detalleamortizacion " +
                     "WHERE idintangible = ? AND EXTRACT(MONTH FROM fecha_cuota) = ? AND EXTRACT(YEAR FROM fecha_cuota) = ? AND iddetalle != ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idIntangible);
            ps.setInt(2, mes);
            ps.setInt(3, anio);
            ps.setString(4, idDetalleExcluir);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    /**
     * Elimina una amortización específica
     */
    public boolean eliminarAmortizacion(String idDetalle) throws SQLException {
        String sql = "DELETE FROM detalleamortizacion WHERE iddetalle = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idDetalle);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        }
    }
}


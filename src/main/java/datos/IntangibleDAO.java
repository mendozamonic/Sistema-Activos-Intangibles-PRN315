/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import jakarta.servlet.ServletException;
import modelo.Intangible;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntangibleDAO {
    private Connection conexion;
    
    public IntangibleDAO() throws SQLException {
        conexion = Conexion.getConnection();
    }
    
    // LISTAR
     public List<Intangible> listarIntangibles() {
        
              List<Intangible> lista = new ArrayList<>();
        String sql = "SELECT * FROM intangible ORDER BY idintangible";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Intangible intangible = new Intangible();
                intangible.setIdIntangible(rs.getString("idintangible"));
                intangible.setVersion(rs.getString("version"));
                intangible.setNombre_intangible(rs.getString("nombre_intangible"));
                intangible.setNombre_proveedor(rs.getString("nombre_proveedor"));
                intangible.setTipoLicencia(rs.getString("tipo_licencia_"));
                intangible.setCodigo(rs.getString("codigo_"));
                intangible.setCosto(rs.getDouble("costo"));
                intangible.setVidaUtil(rs.getString("vida_util"));
                intangible.setEstado(rs.getString("estado_"));
                intangible.setFechaAdquisicion(rs.getString("fecha_adquisicion"));
                intangible.setFechaVencimiento(rs.getString("fecha_vencimiento"));
                intangible.setModalidad_amortizacion(rs.getString("modalidad_amortizacion"));
                
                lista.add(intangible);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar intangibles: " + e.getMessage());
            e.printStackTrace();
        }
        
        return lista;
    }
    
    // OBTENER POR ID (para editar)
    public Intangible obtenerIntangiblePorId(String id) throws SQLException {
        String sql = "SELECT * FROM intangible WHERE idintangible = ?";
        Intangible intangible = null;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    intangible = new Intangible();
                    intangible.setIdIntangible(rs.getString("idintangible"));
                    intangible.setVersion(rs.getString("version"));
                    intangible.setNombre_intangible(rs.getString("nombre_intangible"));
                    intangible.setNombre_proveedor(rs.getString("nombre_proveedor"));
                    intangible.setTipoLicencia(rs.getString("tipo_licencia_"));
                    intangible.setCodigo(rs.getString("codigo_"));
                    intangible.setCosto(rs.getDouble("costo"));
                    intangible.setVidaUtil(rs.getString("vida_util"));
                    intangible.setEstado(rs.getString("estado_"));
                    intangible.setFechaAdquisicion(rs.getString("fecha_adquisicion"));
                    intangible.setFechaVencimiento(rs.getString("fecha_vencimiento"));
                    intangible.setModalidad_amortizacion(rs.getString("modalidad_amortizacion"));
                }
            }
        }
        return intangible;
    }

    // INSERTAR (para agregarIntangibles.jsp)
    public boolean insertarIntangible(Intangible i) throws SQLException {
        String sql = "INSERT INTO intangible ("
                + "idintangible, version, nombre_intangible, nombre_proveedor, "
                + "tipo_licencia_, codigo_, costo, vida_util, estado_, "
                + "fecha_adquisicion, fecha_vencimiento, modalidad_amortizacion"
                + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, i.getIdIntangible());
            ps.setString(2, i.getVersion());
            ps.setString(3, i.getNombre_intangible());
            ps.setString(4, i.getNombre_proveedor());
            ps.setString(5, i.getTipoLicencia());
            ps.setString(6, i.getCodigo());
            ps.setDouble(7, i.getCosto());
            ps.setString(8, i.getVidaUtil());
            ps.setString(9, i.getEstado());

            // ---- FECHA ADQUISICION (DATE) ----
            String fechaAdqStr = i.getFechaAdquisicion();
            if (fechaAdqStr != null && !fechaAdqStr.isEmpty()) {
                ps.setDate(10, Date.valueOf(fechaAdqStr)); // "yyyy-MM-dd"
            } else {
                ps.setNull(10, Types.DATE);
            }

            // ---- FECHA VENCIMIENTO (DATE) ----
            String fechaVenStr = i.getFechaVencimiento();
            if (fechaVenStr != null && !fechaVenStr.isEmpty()) {
                ps.setDate(11, Date.valueOf(fechaVenStr));
            } else {
                ps.setNull(11, Types.DATE);
            }

            ps.setString(12, i.getModalidad_amortizacion());

            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }
    
    

    // ACTUALIZAR (para modificarIntangibles.jsp)
    public boolean modificarIntangible(Intangible i) throws SQLException {
        String sql = "UPDATE intangible SET " +
                     "version = ?, nombre_intangible = ?, nombre_proveedor = ?, " +
                     "tipo_licencia_ = ?, codigo_ = ?, costo = ?, vida_util = ?, " +
                     "estado_ = ?, fecha_adquisicion = ?, fecha_vencimiento = ?, " +
                     "modalidad_amortizacion = ? " +
                     "WHERE idintangible = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, i.getVersion());
            ps.setString(2, i.getNombre_intangible());
            ps.setString(3, i.getNombre_proveedor());
            ps.setString(4, i.getTipoLicencia());
            ps.setString(5, i.getCodigo());
            ps.setDouble(6, i.getCosto());
            ps.setString(7, i.getVidaUtil());
            ps.setString(8, i.getEstado());
            ps.setString(9, i.getFechaAdquisicion());
            ps.setString(10, i.getFechaVencimiento());
            ps.setString(11, i.getModalidad_amortizacion());
            ps.setString(12, i.getIdIntangible());
                     
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    // ELIMINAR
    public boolean eliminarIntangible(String id) {
        String sql = "DELETE FROM intangible WHERE idintangible = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar intangible: " + e.getMessage());
            return false;
        }
    
    }
    
    
    
    
    private String validarIntangible(Intangible i) {
    StringBuilder errores = new StringBuilder();

    // 1) Costo
    if (i.getCosto() <= 0) {
        errores.append("• El costo debe ser mayor que 0.<br>");
    }

    // 2) Fechas y relación con vida útil / modalidad
    try {
        LocalDate fa = LocalDate.parse(i.getFechaAdquisicion());   // yyyy-MM-dd
        LocalDate fv = LocalDate.parse(i.getFechaVencimiento());

        if (!fv.isAfter(fa)) {
            errores.append("• La fecha de vencimiento debe ser posterior a la fecha de adquisición.<br>");
        } else {
            long meses = ChronoUnit.MONTHS.between(fa, fv);

            int vidaNum = extraerNumero(i.getVidaUtil());

            if (vidaNum > 0) {
                String modalidad = i.getModalidad_amortizacion();
                if ("MENSUAL".equalsIgnoreCase(modalidad)) {
                    if (meses != vidaNum) {
                        errores.append("• Con modalidad MENSUAL la vida útil debe coincidir con los meses entre la fecha de adquisición y vencimiento (actualmente hay ")
                               .append(meses)
                               .append(" meses).<br>");
                    }
                } else if ("ANUAL".equalsIgnoreCase(modalidad)) {
                    long anios = meses / 12;
                    if (anios != vidaNum) {
                        errores.append("• Con modalidad ANUAL la vida útil debe coincidir con los años entre la fecha de adquisición y vencimiento (actualmente hay ")
                               .append(anios)
                               .append(" año(s)).<br>");
                    }
                }
            }
        }

    } catch (DateTimeParseException e) {
        errores.append("• Formato de fechas inválido. Use el selector de calendario.<br>");
    }

    if (errores.length() == 0) {
        return null; // todo OK
    } else {
        return errores.toString();
    }
}

/** Extrae el primer número entero de un texto, por ejemplo "12 meses" -> 12. */
private int extraerNumero(String texto) {
    if (texto == null) return 0;
    Matcher m = Pattern.compile("(\\d+)").matcher(texto);
    if (m.find()) {
        return Integer.parseInt(m.group(1));
    }
    return 0;
}
    
    
    
    
}

package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Historial;

public class HistorialDAO {

    public static class Filtros {
        public String usuario;        // idusuario or part of nombre
        public String proveedor;      // nombre proveedor
        public String codigo;         // codigo_ of intangible
        public String fechaDesde;     // yyyy-MM-dd
        public String fechaHasta;     // yyyy-MM-dd
        public int offset = 0;
        public int limit = 10;
    }

    public List<Historial> listar(Filtros f) throws SQLException {
        List<Historial> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
           .append(" h.idhistorial,")
           .append(" c.idcompra, c.monto, c.fecha_compra, c.fecha_vencimiento,")
           .append(" i.idintangible, i.codigo_, i.version, i.tipo_licencia_,")
           .append(" COALESCE(p.nombre_proveedor, i.nombre_proveedor) AS proveedor,")
           .append(" u.idusuario, u.nombre_completo")
           .append(" FROM historial h")
           .append(" JOIN compra c ON c.idcompra = h.idcompra")
           .append(" JOIN intangible i ON i.idintangible = c.idintangible")
           .append(" JOIN usuario u ON u.idusuario = i.idusuario")
           .append(" LEFT JOIN proveedor p ON p.idprovedor = c.idprovedor")
           .append(" WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (f != null) {
            if (f.usuario != null && !f.usuario.trim().isEmpty()) {
                sql.append(" AND (LOWER(u.idusuario) LIKE LOWER(?) OR LOWER(u.nombre_completo) LIKE LOWER(?))");
                String like = "%" + f.usuario.trim() + "%";
                params.add(like);
                params.add(like);
            }
            if (f.proveedor != null && !f.proveedor.trim().isEmpty()) {
                sql.append(" AND LOWER(COALESCE(p.nombre_proveedor, i.nombre_proveedor)) LIKE LOWER(?)");
                params.add("%" + f.proveedor.trim() + "%");
            }
            if (f.codigo != null && !f.codigo.trim().isEmpty()) {
                sql.append(" AND LOWER(i.codigo_) LIKE LOWER(?)");
                params.add("%" + f.codigo.trim() + "%");
            }
            if (f.fechaDesde != null && !f.fechaDesde.trim().isEmpty()) {
                sql.append(" AND c.fecha_compra >= ?");
                params.add(java.sql.Date.valueOf(f.fechaDesde.trim()));
            }
            if (f.fechaHasta != null && !f.fechaHasta.trim().isEmpty()) {
                sql.append(" AND c.fecha_compra <= ?");
                params.add(java.sql.Date.valueOf(f.fechaHasta.trim()));
            }
        }

        sql.append(" ORDER BY c.fecha_compra DESC, h.idhistorial DESC");
        sql.append(" LIMIT ? OFFSET ?");

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            for (Object p : params) {
                if (p instanceof java.sql.Date) {
                    ps.setDate(idx++, (java.sql.Date) p);
                } else {
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
                    h.setIdHistorial(rs.getString("idhistorial"));
                    h.setIdCompra(rs.getString("idcompra"));
                    h.setMonto(rs.getDouble("monto"));
                    java.sql.Date fc = rs.getDate("fecha_compra");
                    java.sql.Date fv = rs.getDate("fecha_vencimiento");
                    h.setFechaCompra(fc != null ? fc.toString() : null);
                    h.setFechaVencimiento(fv != null ? fv.toString() : null);
                    h.setIdIntangible(rs.getString("idintangible"));
                    h.setCodigo(rs.getString("codigo_"));
                    h.setVersion(rs.getString("version"));
                    h.setTipoLicencia(rs.getString("tipo_licencia_"));
                    h.setProveedor(rs.getString("proveedor"));
                    h.setIdUsuario(rs.getString("idusuario"));
                    h.setNombreUsuario(rs.getString("nombre_completo"));
                    lista.add(h);
                }
            }
        }
        return lista;
    }

    public int contar(Filtros f) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*)")
           .append(" FROM historial h")
           .append(" JOIN compra c ON c.idcompra = h.idcompra")
           .append(" JOIN intangible i ON i.idintangible = c.idintangible")
           .append(" JOIN usuario u ON u.idusuario = i.idusuario")
           .append(" LEFT JOIN proveedor p ON p.idprovedor = c.idprovedor")
           .append(" WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (f != null) {
            if (f.usuario != null && !f.usuario.trim().isEmpty()) {
                sql.append(" AND (LOWER(u.idusuario) LIKE LOWER(?) OR LOWER(u.nombre_completo) LIKE LOWER(?))");
                String like = "%" + f.usuario.trim() + "%";
                params.add(like);
                params.add(like);
            }
            if (f.proveedor != null && !f.proveedor.trim().isEmpty()) {
                sql.append(" AND LOWER(COALESCE(p.nombre_proveedor, i.nombre_proveedor)) LIKE LOWER(?)");
                params.add("%" + f.proveedor.trim() + "%");
            }
            if (f.codigo != null && !f.codigo.trim().isEmpty()) {
                sql.append(" AND LOWER(i.codigo_) LIKE LOWER(?)");
                params.add("%" + f.codigo.trim() + "%");
            }
            if (f.fechaDesde != null && !f.fechaDesde.trim().isEmpty()) {
                sql.append(" AND c.fecha_compra >= ?");
                params.add(java.sql.Date.valueOf(f.fechaDesde.trim()));
            }
            if (f.fechaHasta != null && !f.fechaHasta.trim().isEmpty()) {
                sql.append(" AND c.fecha_compra <= ?");
                params.add(java.sql.Date.valueOf(f.fechaHasta.trim()));
            }
        }

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            for (Object p : params) {
                if (p instanceof java.sql.Date) {
                    ps.setDate(idx++, (java.sql.Date) p);
                } else {
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
}



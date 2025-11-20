/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;
import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author lulic
 */
public class UsuarioDAO {
    


    
    private static final String SQL_VALIDAR = 
        "SELECT idusuario, contrasena, nombre_completo FROM usuario WHERE idusuario = ? AND contrasena = ?";
    
    private static final String SQL_INSERTAR = 
        "INSERT INTO usuario (idusuario, contrasena, nombre_completo) VALUES (?, ?, ?)";
    
    private static final String SQL_BUSCAR_POR_ID = 
        "SELECT idusuario, contrasena, nombre_completo FROM usuario WHERE idusuario = ?";
    
    public Usuario validar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuarioValidado = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_VALIDAR);
            stmt.setString(1, usuario.getIdUsuario());
            stmt.setString(2, usuario.getContrasena());
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuarioValidado = new Usuario();
                usuarioValidado.setIdUsuario(rs.getString("idusuario"));
                usuarioValidado.setContrasena(rs.getString("contrasena"));
                usuarioValidado.setNombreCompleto(rs.getString("nombre_completo")); 
            }
        } catch (SQLException e) {
            System.err.println("Error SQL en validar: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error en validar: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        
        return usuarioValidado;
    }
    
    public boolean insertar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            System.out.println("=== INTENTANDO INSERTAR USUARIO ===");
            System.out.println("Usuario: " + usuario.getIdUsuario());
            System.out.println("Nombre: " + usuario.getNombreCompleto());
            System.out.println("Contraseña: " + (usuario.getContrasena() != null ? "***" : "null"));
            
            conn = Conexion.getConnection();
            System.out.println("Conexión establecida correctamente");
            
            stmt = conn.prepareStatement(SQL_INSERTAR);
            stmt.setString(1, usuario.getIdUsuario());        
            stmt.setString(2, usuario.getContrasena());      
            stmt.setString(3, usuario.getNombreCompleto());  
            
            System.out.println("Ejecutando INSERT...");
            System.out.println("SQL: " + SQL_INSERTAR);
            System.out.println("Parámetros: [" + usuario.getIdUsuario() + ", ***, " + usuario.getNombreCompleto() + "]");
            
            int filasAfectadas = stmt.executeUpdate();
            exito = (filasAfectadas > 0);
            
            System.out.println("Filas afectadas: " + filasAfectadas);
            System.out.println("Usuario insertado: " + (exito ? "ÉXITO" : "FALLÓ"));
            
        } catch (SQLException e) {
            System.err.println("=== ERROR SQL AL INSERTAR USUARIO ===");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Mensaje: " + e.getMessage());
            
            // Check for specific constraint violations
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                // Constraint violation (23xxx)
                if (e.getMessage().contains("nombre_completo") || e.getMessage().contains("value too long")) {
                    System.err.println("ERROR: El nombre completo excede el límite de caracteres permitidos.");
                } else if (e.getMessage().contains("idusuario") || e.getMessage().contains("duplicate") || 
                          e.getMessage().contains("unique")) {
                    System.err.println("ERROR: El usuario ya existe en la base de datos.");
                }
            }
            
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("=== ERROR AL INSERTAR USUARIO ===");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("Tipo: " + e.getClass().getName());
            if (e.getCause() != null) {
                System.err.println("Causa: " + e.getCause().getMessage());
            }
            e.printStackTrace();
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        
        return exito;
    }
    
    public Usuario buscarPorId(String idUsuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_BUSCAR_POR_ID);
            stmt.setString(1, idUsuario);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getString("idusuario"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setNombreCompleto(rs.getString("nombre_completo")); 
            }
        } catch (SQLException e) {
            System.err.println("Error SQL en buscarPorId: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error en buscarPorId: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        
        return usuario;
    }
}
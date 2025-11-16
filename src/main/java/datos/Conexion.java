/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

/**
 *
 * @author monic
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException; 

public class Conexion {
      
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/proyecto";
    private static final String JDBC_USER = "proyecto";  
    private static final String JDBC_PASSWORD = "proyecto";
    
    
    public static Connection getConnection() throws SQLException {
        try {
            
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        }
        
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }
    
    
    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
  
    public static void close(java.sql.PreparedStatement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    
    public static void close(java.sql.ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
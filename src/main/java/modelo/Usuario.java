/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author monic
 */
public class Usuario {
   
    private String idUsuario;
    private String contrasena;
    private String nombreCompleto;
    
    // Constructores
    public Usuario() {
    }
    
    public Usuario(String idUsuario, String contrasena) {
        this.idUsuario = idUsuario;
        this.contrasena = contrasena;
    }
    
    public Usuario(String idUsuario, String contrasena, String nombreCompleto) {
        this.idUsuario = idUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
    }
    
    // Getters y Setters
    public String getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
}



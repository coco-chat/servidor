/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Modelos;

/**
 *
 * @author juanc
 */
public class Modelo_grupos {
    
    private int id;
    private int admin;
    private String nombre;
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setAdmin(int admin) {
        this.admin = admin;
    }
    
    public int getAdmin() {
        return this.admin;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
}

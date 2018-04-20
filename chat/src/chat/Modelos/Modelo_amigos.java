/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Modelos;

import java.io.Serializable;

/**
 *
 * @author juanc
 */
public class Modelo_amigos implements Serializable {
    
    private int id;
    private int amigo1;
    private int amigo2;
    private String apodo1;
    private String apodo2;
    
    public Modelo_amigos() {
        
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setAmigo1(int amigo1) {
        this.amigo1 = amigo1;
    }
    
    public int getAmigo1() {
        return this.amigo1;
    }
    
    public void setAmigo2(int amigo2) {
        this.amigo2 = amigo2;
    }
    
    public int getAmigo2() {
        return this.amigo2;
    }
    
    public void setApodo1(String apodo1) {
        this.apodo1 = apodo1;
    }
    
    public String getApodo1() {
        return this.apodo1;
    }
    
    public void setApodo2(String apodo2) {
        this.apodo2 = apodo2;
    }
    
    public String getApodo2() {
        return this.apodo2;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Models;

import java.io.Serializable;

/**
 *
 * @author juanc
 */
public class Amigo implements Serializable {
    
    private int id;
    private int amigo1;
    private int amigo2;
    private String apodo1;
    private String apodo2;

    public Amigo() {
        id = -1;
        amigo1 = -1;
        amigo2 = -1;
        apodo1 = " ";
        apodo2 = " ";
    }
    
    /**
     * Asigna un id
     * @param id el id a asignar
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtiene el id
     * @return el id del objeto
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Asigna el id del amigo 1
     * @param amigo1 el id del amigo 1
     */
    public void setAmigo1(int amigo1) {
        this.amigo1 = amigo1;
    }
    
    /**
     * Obtiene el id del amigo 1
     * @return el id del amigo 1
     */
    public int getAmigo1() {
        return this.amigo1;
    }
    
    /**
     * Asigna el id del amigo 2
     * @param amigo2 el id del amigo 2
     */
    public void setAmigo2(int amigo2) {
        this.amigo2 = amigo2;
    }
    
    /**
     * Obtiene el id del amigo 2
     * @return el id del amigo 2
     */
    public int getAmigo2() {
        return this.amigo2;
    }
    
    /**
     * Asigna el apodo del amigo 1
     * @param apodo1 apodo del amigo 1
     */
    public void setApodo1(String apodo1) {
        this.apodo1 = apodo1;
    }
    
    /**
     * Obtiene el apodo del amigo 1
     * @return el apodo del amigo 1
     */
    public String getApodo1() {
        return this.apodo1;
    }
    
    /**
     * Asigna el apodo del usuario 2
     * @param apodo2 el apodo del usuario 2
     */
    public void setApodo2(String apodo2) {
        this.apodo2 = apodo2;
    }
    
    /**
     * Optiene el apodo del usuario 2
     * @return 
     */
    public String getApodo2() {
        return this.apodo2;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Modelos;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class Modelo_nuevoGrupo implements Serializable{
    private Modelo_grupos grupo;
    private List<Modelo_pet_grupos> integrantes;

    public Modelo_nuevoGrupo() {
    }

    public Modelo_grupos getGrupo() {
        return grupo;
    }

    public void setGrupo(Modelo_grupos grupo) {
        this.grupo = grupo;
    }

    public List<Modelo_pet_grupos> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Modelo_pet_grupos> integrantes) {
        this.integrantes = integrantes;
    }
    
    
}

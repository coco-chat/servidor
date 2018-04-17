/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Procesos;

import chat.Modelos.Modelo_Comunicacion;
import chat.Modelos.Modelo_Comunicacion.MTypes;
import com.google.gson.Gson;
import chat.Modelos.Modelo_usuarios;
import chat.Controladores.Controlador_usuarios;


/**
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class ProcesoJson {
    private final Gson gson = new Gson();
    private Modelo_Comunicacion mensajeEntrante;
    private Modelo_Comunicacion mensajeSaliente;
    
    public ProcesoJson(){}
    
    public String procesar(String JSON){
        mensajeEntrante = gson.fromJson(
                JSON, Modelo_Comunicacion.class
        );
        switch(mensajeEntrante.getTipo()){
            case RQ_LOGIN: 
                return login((Modelo_usuarios)mensajeEntrante.getContenido());
        }
        return "";
    }
    
    public String login(Modelo_usuarios usuario){
        Controlador_usuarios usuarios = new Controlador_usuarios();
        mensajeSaliente.setTipo(MTypes.ACK_LOGIN);
        mensajeSaliente.setContenido("OK");
        return gson.toJson(mensajeSaliente);
    }
}

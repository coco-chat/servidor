/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Procesos;

import chat.Modelos.Modelo_Comunicacion;
import chat.Modelos.Modelo_Comunicacion.MTypes;
import chat.Controladores.Controlador_cuentas;
import com.google.gson.Gson;
import chat.Modelos.Modelo_usuarios;
import chat.Controladores.Controlador_usuarios;
import java.net.Socket;
import chat.Modelos.Modelo_Mensaje;
import java.util.HashMap;


/**
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class ProcesoJson {
    private final Gson gson = new Gson();
    private final HashMap<Hilo, Integer> hashTable;
    private final Hilo client;
    
    public ProcesoJson(HashMap<Hilo, Integer>  hashTable, Hilo client){
        this.hashTable = hashTable;
        this.client = client;
    }
    
    public String procesar(String JSON){
        Modelo_Comunicacion mensajeEntrante;
        mensajeEntrante = gson.fromJson(
                JSON, Modelo_Comunicacion.class
        );
        switch(mensajeEntrante.getTipo()){
            case RQ_LOGIN: 
                return login(
                        gson.fromJson(
                            mensajeEntrante.getContenido().toString(),
                            Modelo_usuarios.class
                        )
                );
            case RQ_REG:
                return registro(
                        gson.fromJson(
                            mensajeEntrante.getContenido().toString(),
                            Modelo_usuarios.class
                        )
                );
            case RQ_MENSAJE:
                return mensaje(
                        gson.fromJson(
                            mensajeEntrante.getContenido().toString(),
                            Modelo_Mensaje.class
                        )
                );
            default:
                return notFound();
        }
    }
    
    public String notFound(){
        Modelo_Comunicacion mensajeSaliente = new Modelo_Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        mensajeSaliente.setContenido(404);
        return gson.toJson(mensajeSaliente);
    }
    
    public void removeSocket(){
        hashTable.remove(this.client);
    }
    
    public String login(Modelo_usuarios usuario){
        Modelo_Comunicacion mensajeSaliente = new Modelo_Comunicacion();
        Controlador_cuentas cuentas = new Controlador_cuentas();
        int result = cuentas.Login(usuario.getUsername(), usuario.getPassword());
        mensajeSaliente.setTipo(MTypes.ACK_LOGIN);
        if (result == -1) {
            mensajeSaliente.setContenido(410);
        }else{
            mensajeSaliente.setContenido(210);
            this.hashTable.put(client, result);
        }
        return gson.toJson(mensajeSaliente);
    }
    
    public String registro(Modelo_usuarios usuario){
        int result;
        Controlador_cuentas cuenta = new Controlador_cuentas();
        Modelo_Comunicacion mensajeSaliente = new Modelo_Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        
        result = cuenta.Register(usuario.getUsername(), usuario.getPassword());
        if(result == 404){
            mensajeSaliente.setContenido(422);
            return gson.toJson(mensajeSaliente);
        }
        if(result == 405){
            mensajeSaliente.setContenido(421);
            return gson.toJson(mensajeSaliente);
        }
        result = cuenta.Login(usuario.getUsername(), usuario.getPassword());
        
        if (result == -1) {
            mensajeSaliente.setContenido(420);    
        }else{
            mensajeSaliente.setContenido(220);
            hashTable.put(this.client,result);
        }
        return gson.toJson(mensajeSaliente);
    }
    
    public String mensaje(Modelo_Mensaje mensaje){
        Modelo_Comunicacion mensajeSaliente = new Modelo_Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        mensajeSaliente.setContenido(client);
        return "";
    }
}

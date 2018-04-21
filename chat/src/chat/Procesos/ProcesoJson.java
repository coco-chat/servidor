/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Procesos;

import chat.Controladores.Controlador_amigos;
import chat.Modelos.Modelo_Comunicacion;
import chat.Modelos.Modelo_Comunicacion.MTypes;
import chat.Controladores.Controlador_cuentas;
import chat.Controladores.Controlador_grupos;
import com.google.gson.Gson;
import chat.Modelos.Modelo_usuarios;
import chat.Modelos.Modelo_pet_amigos;
import chat.Controladores.Controlador_pet_amigos;
import chat.Controladores.Controlador_pet_grupos;
import chat.Controladores.Controlador_usuarios;
import java.net.Socket;
import chat.Modelos.Modelo_Mensaje;
import chat.Modelos.Modelo_amigos;
import chat.Modelos.Modelo_nuevoGrupo;
import chat.Modelos.Modelo_pet_grupos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
            case RQ_NAMIGO:
                return agregarAmigo(
                        gson.fromJson(
                            mensajeEntrante.getContenido().toString(),
                            Modelo_usuarios.class
                        )
                );
            case RQ_LOGOUT:
                return logout();
            case RQ_AAMIGO:
                return aceptarAmigo(
                        gson.fromJson(
                            mensajeEntrante.getContenido().toString(),
                            Modelo_usuarios.class
                        )
                );
            case RQ_CONECTADOS:
                return getConectados();
            case RQ_DESCONECTADOS:
                return getDesconectados();
            case RQ_GRUPO:
                return crearGrupo(
                        gson.fromJson(
                            mensajeEntrante.getContenido().toString(),
                            Modelo_nuevoGrupo.class
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
        Modelo_Comunicacion mensajeRemoto = new Modelo_Comunicacion();
        Hilo destino = isConectado(mensaje.getDestino().getId());
        if(destino!=null){
            mensajeRemoto.setTipo(MTypes.SEND_MENSAJE);
            mensajeRemoto.setContenido(mensaje);
            destino.enviarMensaje(gson.toJson(mensajeRemoto));
            mensajeSaliente.setContenido(260);
        }else mensajeSaliente.setContenido(460);
        return gson.toJson(mensajeSaliente);
    }
    
    public String logout(){
        hashTable.remove(this.client);
        client.closeSocket();
        Modelo_Comunicacion mensajeSaliente = new Modelo_Comunicacion();
        
        mensajeSaliente.setContenido(230);
        mensajeSaliente.setTipo(MTypes.ACK);
        
        return gson.toJson(mensajeSaliente);
    }
    
    public String agregarAmigo(Modelo_usuarios usuario){
        Modelo_Comunicacion mensajeSaliente = new Modelo_Comunicacion();
        Modelo_pet_amigos peticion = new Modelo_pet_amigos();
        Controlador_pet_amigos agregar = new Controlador_pet_amigos();
        
        if(agregar.verificarPeticion(hashTable.get(client), usuario.getId()) == 0) {
            peticion.setSolicitado(usuario.getId());
            peticion.setSolicitante(hashTable.get(client));
            agregar.Insert(peticion);
            mensajeSaliente.setContenido(240);
            mensajeSaliente.setTipo(MTypes.ACK);
        }else{
            mensajeSaliente.setContenido(441);
            mensajeSaliente.setTipo(MTypes.ACK);   
        }
        return gson.toJson(mensajeSaliente);
    }
    
    public String crearGrupo(Modelo_nuevoGrupo nuevoGrupo){
        Controlador_grupos grupos = new Controlador_grupos();
        Controlador_pet_grupos petGrupos = new Controlador_pet_grupos();
        Modelo_Comunicacion mensajeSaliente = new Modelo_Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        grupos.Insert(nuevoGrupo.getGrupo());
        for(Modelo_pet_grupos pet: nuevoGrupo.getIntegrantes())
            petGrupos.Insert(pet);
        mensajeSaliente.setContenido(270);
        return gson.toJson(mensajeSaliente);
    }
    
    public String aceptarAmigo(Modelo_usuarios usuario){
        Modelo_Comunicacion mensajeSaliente = new Modelo_Comunicacion();
        Modelo_amigos peticion = new Modelo_amigos();
        Controlador_amigos aceptar = new Controlador_amigos();
        peticion.setAmigo1(usuario.getId());
        peticion.setAmigo2(hashTable.get(client));
        aceptar.Insert(peticion);
        mensajeSaliente.setContenido(250);
        mensajeSaliente.setTipo(MTypes.ACK);
        return gson.toJson(mensajeSaliente);
    }

    public Hilo isConectado(int id){
        for(Object val : hashTable.entrySet()){
            Map.Entry entry = (Map.Entry) val;
            if((int)entry.getValue() == id){
                return (Hilo)entry.getKey();
            }
        }
        return null;
    }
    
    public String getConectados(){
        Controlador_usuarios usuarios = new Controlador_usuarios();
        List<Modelo_usuarios> usuariosList = usuarios.Select();
        Modelo_Comunicacion mensajeSaliente = new Modelo_Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_CONECTADOS);
        for(Modelo_usuarios usuario: usuariosList){
            if(isConectado(usuario.getId())== null){
                usuariosList.remove(usuario);
            }
        }
        mensajeSaliente.setContenido(usuariosList);
        return gson.toJson(mensajeSaliente);
    }
    
    public String getDesconectados(){
        Controlador_usuarios usuarios = new Controlador_usuarios();
        List<Modelo_usuarios> usuariosList = usuarios.Select();
        Modelo_Comunicacion mensajeSaliente = new Modelo_Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_CONECTADOS);
        for(Modelo_usuarios usuario: usuariosList){
            if(isConectado(usuario.getId())!= null){
                usuariosList.remove(usuario);
            }
        }
        mensajeSaliente.setContenido(usuariosList);
        return gson.toJson(mensajeSaliente);
    }
}

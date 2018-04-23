/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Procesos;

import chat.Controladores.AmigosController;
import chat.Modelos.Comunicacion;
import chat.Modelos.Comunicacion.MTypes;
import chat.Controladores.CuentasController;
import chat.Controladores.GruposController;
import com.google.gson.Gson;
import chat.Modelos.Usuario;
import chat.Modelos.PetAmigo;
import chat.Controladores.PetAmigosController;
import chat.Controladores.PetGruposController;
import chat.Controladores.UsuariosController;
import chat.Modelos.PetGrupo;
import java.net.Socket;
import chat.Modelos.Mensaje;
import chat.Modelos.Amigo;
import chat.Modelos.Grupo;
import chat.Modelos.NuevoGrupo;
import chat.Modelos.PetGrupo;
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
        Comunicacion mensajeEntrante;
        mensajeEntrante = gson.fromJson(JSON, Comunicacion.class
        );
        switch(mensajeEntrante.getTipo()){
            case RQ_LOGIN: 
                return login(gson.fromJson(mensajeEntrante.getContenido().toString(),
                            Usuario.class
                        )
                );
            case RQ_REG:
                return registro(gson.fromJson(mensajeEntrante.getContenido().toString(),
                            Usuario.class
                        )
                );
            case RQ_MENSAJE:
                return mensaje(gson.fromJson(mensajeEntrante.getContenido().toString(),
                            Mensaje.class
                        )
                );
            case RQ_NAMIGO:
                return agregarAmigo(gson.fromJson(mensajeEntrante.getContenido().toString(),
                            Usuario.class
                        )
                );
            case RQ_LOGOUT:
                return logout();
            case RQ_AAMIGO:
                return aceptarAmigo(gson.fromJson(mensajeEntrante.getContenido().toString(),
                            Usuario.class
                        )
                );
            case RQ_DAMIGO:
                return olvidarAmigo(gson.fromJson(mensajeEntrante.getContenido().toString(),
                                Amigo.class
                        )
                );
            case RQ_APODO:
                return actualizarApodoAmigo(gson.fromJson(mensajeEntrante.getContenido().toString(),
                                Amigo.class
                        )
                );
            case RQ_CONECTADOS:
                return getConectados();
            case RQ_DESCONECTADOS:
                return getDesconectados();
            case RQ_GRUPO:
                return crearGrupo(gson.fromJson(mensajeEntrante.getContenido().toString(),
                            NuevoGrupo.class
                        )
                );
            case RQ_NMIEMBRO:
                return nuevoMiembro(gson.fromJson(mensajeEntrante.getContenido().toString(),
                            PetGrupo.class
                        )
                );
            case RQ_CGRUPO://?
                return cambiarNombreGrupo(gson.fromJson(mensajeEntrante.getContenido().toString(),
                                Grupo.class
                        )
                );
            default:
                return notFound();
        }
    }
    
    public String notFound(){
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        mensajeSaliente.setContenido(404);
        return gson.toJson(mensajeSaliente);
    }
    
    public String login(Usuario usuario){
        Comunicacion mensajeSaliente = new Comunicacion();
        CuentasController cuentas = new CuentasController();
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
    
    public String registro(Usuario usuario){
        int result;
        CuentasController cuenta = new CuentasController();
        Comunicacion mensajeSaliente = new Comunicacion();
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
    
    public String mensaje(Mensaje mensaje){
        Comunicacion mensajeSaliente = new Comunicacion();
        Comunicacion mensajeRemoto = new Comunicacion();
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
        Comunicacion mensajeSaliente = new Comunicacion();
        
        mensajeSaliente.setContenido(230);
        mensajeSaliente.setTipo(MTypes.ACK);
        
        return gson.toJson(mensajeSaliente);
    }
    
    public String agregarAmigo(Usuario usuario){
        Comunicacion mensajeSaliente = new Comunicacion();
        PetAmigo peticion = new PetAmigo();
        PetAmigosController agregar = new PetAmigosController();
        
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
    
    public String crearGrupo(NuevoGrupo nuevoGrupo){
        GruposController grupos = new GruposController();
        PetGruposController petGrupos = new PetGruposController();
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        grupos.Insert(nuevoGrupo.getGrupo());
        for(PetGrupo pet: nuevoGrupo.getIntegrantes())
            petGrupos.Insert(pet);
        mensajeSaliente.setContenido(270);
        return gson.toJson(mensajeSaliente);
    }
    
    public String aceptarAmigo(Usuario usuario){
        Comunicacion mensajeSaliente = new Comunicacion();
        Amigo peticion = new Amigo();
        AmigosController aceptar = new AmigosController();
        peticion.setAmigo1(usuario.getId());
        peticion.setAmigo2(hashTable.get(client));
        aceptar.Insert(peticion);
        mensajeSaliente.setContenido(241);
        mensajeSaliente.setTipo(MTypes.ACK);
        return gson.toJson(mensajeSaliente);
    }
    
    public String olvidarAmigo (Amigo amistad) {
        Comunicacion mensajeSaliente = new Comunicacion();
        AmigosController relacionAmigos = new AmigosController();       
        if(relacionAmigos.Delete(amistad) == 1) {
            mensajeSaliente.setContenido(242);
            mensajeSaliente.setTipo(MTypes.ACK);
        } else {
            mensajeSaliente.setContenido(442);
            mensajeSaliente.setTipo(MTypes.ACK);
        }
        return gson.toJson(mensajeSaliente);
    }
    
    public String actualizarApodoAmigo (Amigo amistad) {
        Comunicacion mensajeSaliente = new Comunicacion();
        AmigosController actualizarApodo = new AmigosController();
        /*
        Amigo amistad = new Amigo();
        
        amistad.setAmigo1(amigo.getId());
        amistad.setAmigo2(hashTable.get(client));
        amistad.setApodo1();
        */
                
        //No se actualiza el apodo mismo, se queda en null y se detecta
        //después para no mostrarlo

        if (actualizarApodo.Update(amistad) == 1) {
            mensajeSaliente.setContenido(243);
            mensajeSaliente.setTipo(MTypes.ACK);
        } else {
            mensajeSaliente.setContenido(443);
            mensajeSaliente.setTipo(MTypes.ACK);
        }
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
        UsuariosController usuarios = new UsuariosController();
        List<Usuario> usuariosList = usuarios.Select();
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_CONECTADOS);
        for(Usuario usuario: usuariosList){
            if(isConectado(usuario.getId())== null){
                usuariosList.remove(usuario);
            }
        }
        mensajeSaliente.setContenido(usuariosList);
        return gson.toJson(mensajeSaliente);
    }
    
    public String getDesconectados(){
        UsuariosController usuarios = new UsuariosController();
        List<Usuario> usuariosList = usuarios.Select();
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_CONECTADOS);
        for(Usuario usuario: usuariosList){
            if(isConectado(usuario.getId())!= null){
                usuariosList.remove(usuario);
            }
        }
        mensajeSaliente.setContenido(usuariosList);
        return gson.toJson(mensajeSaliente);
    }
    
    public String nuevoMiembro(PetGrupo nuevoMiembro){
        PetGruposController pet_grupo = new PetGruposController();
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        int result = pet_grupo.Insert(nuevoMiembro);
        if(result == -1)mensajeSaliente.setContenido(471);
        else mensajeSaliente.setContenido(271);
        return gson.toJson(mensajeSaliente);
    }
    
    public String cambiarNombreGrupo (Grupo grupo) {
        Comunicacion mensajeSaliente = new Comunicacion();
        GruposController cambiarNombreIntermediario = new GruposController();
        if (cambiarNombreIntermediario.Update(grupo) == 1) {
            mensajeSaliente.setContenido(272);
            mensajeSaliente.setTipo(MTypes.ACK);
        } else  {
            mensajeSaliente.setContenido(472);
            mensajeSaliente.setTipo(MTypes.ACK);
        }
        return gson.toJson(mensajeSaliente);
    }
}

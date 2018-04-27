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
import chat.Controladores.IntegrantesController;
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
import chat.Modelos.InfoGrupo;
import chat.Modelos.Integrante;
import chat.Modelos.MensajeGrupo;
import chat.Modelos.NuevoGrupo;
import chat.Modelos.PetGrupo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
                return login(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),
                        Usuario.class
                        )
                );
            case RQ_REG:
                return registro(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),
                        Usuario.class
                        )
                );
            case RQ_MENSAJE:
                return mensaje(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),
                        Mensaje.class
                        )
                );
            case RQ_MENSAJE_GRUPO:
                return mensajeGrupo(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),
                        MensajeGrupo.class
                        )
                );
            case RQ_NAMIGO:
                return agregarAmigo(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),
                        Usuario.class
                        )
                );
            case RQ_LOGOUT:
                return logout();
            case RQ_AAMIGO:
                return aceptarAmigo(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),
                        Usuario.class
                        )
                );
            case RQ_DAMIGO:
                return olvidarAmigo(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),
                        Amigo.class
                        )
                );
            case RQ_APODO:
                return actualizarApodoAmigo(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),
                        Amigo.class
                        )
                );
            case RQ_CONECTADOS:
                return getConectados();
            case RQ_DESCONECTADOS:
                return getDesconectados();
            case RQ_GRUPO:
                return crearGrupo(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),
                        NuevoGrupo.class
                        )
                );
            case RQ_NMIEMBRO:
                return nuevoMiembro(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),
                        PetGrupo.class
                        )
                );
            case RQ_DMIEMBRO: 
                return deleteMiembro(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),
                        Integrante.class)
                );
            case RQ_CGRUPO:
                return cambiarNombreGrupo(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),
                        Grupo.class
                        )
                );
            case RQ_AMIGOS:
                return getAmigos();
            case RQ_INFOGRUPO:
                return getInfoGrupo(
                        gson.fromJson(mensajeEntrante.getContenido().toString(),Grupo.class)
                );
            default:
                return notFound();
        }
    }
    
    private String notFound(){
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
    
    /**
     * Recibe el objeto Mensaje Grupo Model
     * lo completa, lo guarda en .json --> ACK
     * @param mensaje
     * @return 
     */
    
    /**
     * Abrir archivo
     * pasar todos en un Lista de objetos
     * Verifico si los usuarios conectados se encuentran en algún objeto
     * Si están se envía el mensaje, y se actualiza el objeto borrando al usuario que se le envío el mensaje
     * Si no pues nada
     * 
     * Se verifican por ultima vez todos los objetos Mensaje Grupo para eliminar los que ya no tengan usuarios a los que enviar mensajes
     * Se sobre escribe el .json con todos los objetos
     * 
     * @param mensaje
     * @return 
     */
    
    public String mensajeGrupo(MensajeGrupo mensaje){
        
        //Dependiendo de Wero se llenan los integrantes o se reciben en el modelo
        
        Comunicacion mensajeSaliente = new Comunicacion();
        Comunicacion mensajeRemoto = new Comunicacion();
        
        List <Integer> listaIntegrantes = mensaje.getIntegrantes();
        
        for (int i = 0; i < listaIntegrantes.size(); ++i){
            Hilo destino = isConectado(mensaje.getIntegrantes().indexOf(i));
            if (destino != null) {
                mensajeRemoto.setTipo(MTypes.RQ_MENSAJE_GRUPO);
                mensajeRemoto.setContenido(mensaje);
                destino.enviarMensaje(gson.toJson(mensajeRemoto));
                mensajeSaliente.setContenido(280);
            } else mensajeSaliente.setContenido(480);
        }                  
        
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
        
        nuevoGrupo.getGrupo().setAdmin(hashTable.get(client));
        int id = grupos.Insert(nuevoGrupo.getGrupo());
        for(PetGrupo pet: nuevoGrupo.getIntegrantes()){
            pet.setGrupo(id);
            petGrupos.Insert(pet);
        }
        mensajeSaliente.setContenido(270);
        return gson.toJson(mensajeSaliente);
    }
    
    public String aceptarAmigo(Usuario usuario){
        Comunicacion mensajeSaliente = new Comunicacion();
        Amigo peticion = new Amigo();
        AmigosController aceptar = new AmigosController();
        PetAmigosController petController = new PetAmigosController();
        PetAmigo pet = new PetAmigo();
        UsuariosController usuariosController = new UsuariosController();
        String nombreA="";
        String nombreB="";
        
        List<Usuario> usuarios = usuariosController.Select();
        for(Usuario x: usuarios){
            if(x.getId()==usuario.getId())nombreA=x.getUsername();
            else if(x.getId()==hashTable.get(client))nombreB=x.getUsername();
        }
        
        peticion.setAmigo1(usuario.getId());
        peticion.setApodo1(nombreA);
        peticion.setAmigo2(hashTable.get(client));
        peticion.setApodo2(nombreB);
        aceptar.Insert(peticion);
        
        pet.setId(usuario.getId()+"-"+hashTable.get(client));
        petController.Delete(pet);
        
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

        if (actualizarApodo.Update(amistad) == 1) {
            mensajeSaliente.setContenido(243);
            mensajeSaliente.setTipo(MTypes.ACK);
        } else {
            mensajeSaliente.setContenido(443);
            mensajeSaliente.setTipo(MTypes.ACK);
        }
        return gson.toJson(mensajeSaliente);
    }

    private Hilo isConectado(int id){
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
    
     public String deleteMiembro(Integrante Miembro) {
        Comunicacion mensajeSaliente = new Comunicacion();
        IntegrantesController manejadorIntegrantesGrupo = new IntegrantesController();
        if (manejadorIntegrantesGrupo.Delete(Miembro) == 1) {
            mensajeSaliente.setContenido(273);
            mensajeSaliente.setTipo(MTypes.ACK);
        } else {
            mensajeSaliente.setContenido(473);
            mensajeSaliente.setTipo(MTypes.ACK);
        }
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
    
    public String getAmigos(){
        AmigosController amigosController = new AmigosController();
        Comunicacion mensajeSaliente = new Comunicacion();
        int id = hashTable.get(client);
        List<Amigo> amigos = (List<Amigo>) amigosController
                .Select()
                .stream()
                .filter(x->x.getAmigo1()==id || x.getAmigo2()==id);
        mensajeSaliente.setTipo(MTypes.SEND_AMIGOS);
        mensajeSaliente.setContenido(amigos);
        return gson.toJson(mensajeSaliente);
    }
    
    public String getGrupos(){
        IntegrantesController integrantesController = new IntegrantesController();
        Comunicacion mensajeSaliente = new Comunicacion();
        //obtener grupos mediante el controlador
        return "";
    }
    public String getInfoGrupo(Grupo grupo){
        Comunicacion mensajeSaliente = new Comunicacion();
        UsuariosController usuarioMiembro = new UsuariosController();
        List<Usuario> usuarios = new ArrayList<Usuario>();
     
        IntegrantesController integrantes = new IntegrantesController();
        InfoGrupo infogrupo = new InfoGrupo();
        
        int id = grupo.getId();
        
        List<Integrante> integranteLista = integrantes.Select();
        
        for(Integrante integrante: integranteLista){
            if(integrante.getGrupo() == id){
                //integranteGrupo.add(integrante);
                Usuario usuario = new Usuario();
                usuario = usuarioMiembro.getUsuario(integrante.getUsuario());
                usuarios.add(usuario);
            }
        }
        infogrupo.setGrupo(grupo);
        infogrupo.setMiembros(usuarios);
        infogrupo.setAdmin(usuarioMiembro.getUsuario(grupo.getAdmin()));
        
        mensajeSaliente.setTipo(MTypes.SEND_INFOGRUPO);
        mensajeSaliente.setContenido(infogrupo);
        
        return gson.toJson(mensajeSaliente);
    }
}

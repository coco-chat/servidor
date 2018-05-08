/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Process;

import chat.Models.Comunicacion;
import chat.Models.Comunicacion.MTypes;
import com.google.gson.Gson;
import chat.Models.Usuario;
import chat.Models.Mensaje;
import chat.Models.Amigo;
import chat.Models.Grupo;
import chat.Models.Integrante;
import chat.Models.MensajeGrupo;
import chat.Models.NuevoGrupo;
import chat.Models.PetGrupo;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class ProcesoJson {
    private final Gson gson = new Gson();
    private final HashMap<Hilo, Integer> hashTable;
    private final Hilo client;
    private final File file;
    private int id;
    private RQAmigos amigos;
    private RQCuenta cuenta;
    private RQGrupos grupos;
    private RQUsuarios usuarios;
    private RQMensajes mensajes;

    public ProcesoJson() {
        hashTable = new HashMap<>();
        client = new Hilo();
        amigos = new RQAmigos();
        cuenta = new RQCuenta();
        grupos = new RQGrupos();
        usuarios = new RQUsuarios();
        mensajes = new RQMensajes();
        this.file = null;
    }    
    
    public ProcesoJson(HashMap<Hilo, Integer>  hashTable, Hilo client, File file){
        this.hashTable = hashTable;
        this.client = client;
        this.file = file;
        amigos = new RQAmigos();
        cuenta = new RQCuenta();
        grupos = new RQGrupos();
        usuarios = new RQUsuarios();
        mensajes = new RQMensajes();
    }
    
    public String procesar(String JSON){
        Comunicacion mensajeEntrante;
        String contenido;
        mensajeEntrante = gson.fromJson(JSON, Comunicacion.class);
        contenido = gson.toJson(mensajeEntrante.getContenido());
        
        switch(mensajeEntrante.getTipo()){
            case RQCUENTA_LOGIN: return cuentaLogin(contenido);
            case RQCUENTA_REG: return cuentaRegister(contenido);
            
            case RQAMIGOS_ADD: return amigosAdd(contenido);
            case RQAMIGOS_DELETE:return amigosDelete(contenido);
            case RQAMIGOS_INVITE:return amigosInvite(contenido);
            case RQAMIGOS_UPDATE: return amigosUpdate(contenido);
            case RQAMIGOS_GETALL: return amigosGetAll();
            case RQAMIGOS_CONECTADOS: return amigosGetConectados();
            case RQAMIGOS_DESCONECTADOS: return amigosGetDesconectados();
            case RQAMIGOS_GETPET: return amigosGetPet();
            case RQAMIGOS_GETUSUARIOS: return amigosGetUsuarios();
            case RQAMIGOS_REJECT: return amigosReject(contenido);
            
            case RQGRUPOS_CREATE: return gruposCreate(contenido);
            case RQGRUPOS_INVITE: return gruposInvite(contenido);
            case RQGRUPOS_ADD: return gruposAdd(contenido); 
            case RQGRUPOS_DELETE: return gruposDelete(contenido);
            case RQGRUPOS_GETINFO: return gruposGetInfo(contenido);
            case RQGRUPOS_GETPET: return gruposGetPet();
            case RQGRUPOS_GETALL: return gruposGetAll();
            case RQGRUPOS_UPDATE: return gruposUpdate(contenido);
            case RQGRUPOS_REJECT: return gruposReject(contenido);
            
            case RQUSUARIOS_GETALL: return usuariosGetAll();
            case RQUSUARIOS_CONECTADOS: return usuariosGetConectados();
            case RQUSUARIOS_DESCONECTADOS: return usuariosGetDesconectados();
            
            case RQMENSAJES_SENDPERSONAL: return mensajesSendPersonal(contenido);
            case RQMENSAJES_SENDGRUPO: return mensajesSendGrupo(contenido);
            case RQMENSAJES_GETGRUPO: return mensajesGetGrupo();
            
            case RQ_LOGOUT: return logout();
            
            default:
                return notFound();
        }
    }
    
    // Gracias Vanya <3
    
    /*
    --------------------
    |SIN CLASE EXTERNA |
    --------------------
    */
    private String notFound(){
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        mensajeSaliente.setContenido(404);
        return gson.toJson(mensajeSaliente);
    }
    
    public String logout(){
        hashTable.remove(this.client);
        client.closeSocket();
        Comunicacion mensajeSaliente = new Comunicacion();
        
        mensajeSaliente.setContenido(230);
        mensajeSaliente.setTipo(MTypes.ACK);
        usuarioDisconnect();
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
    
    public void sendToAll(String data){
        Hilo hilo;
        for(Object val : hashTable.entrySet()){
            Map.Entry entry = (Map.Entry) val;
            if(!entry.getKey().equals(client)){
                hilo = (Hilo)entry.getKey();
                hilo.enviarMensaje(data);
            }
        }
    }
    
    public void usuarioConnect(){
        Usuario usuario = new Usuario();
        usuario.setId(id);
        Hilo hilo;
        for(Object val : hashTable.entrySet()){
            Map.Entry entry = (Map.Entry) val;
            if(!entry.getKey().equals(client)){
                hilo = (Hilo)entry.getKey();
                hilo.checkConnectUsuario(gson.toJson(usuario));
            }
        }
    }
    
    public void usuarioDisconnect(){
        Usuario usuario = new Usuario();
        usuario.setId(id);
        Hilo hilo;
        for(Object val : hashTable.entrySet()){
            Map.Entry entry = (Map.Entry) val;
            if(!entry.getKey().equals(client)){
                hilo = (Hilo)entry.getKey();
                hilo.checkDisconnectUsuario(gson.toJson(usuario));
            }
        }
    }
    
    public void initialize(int id){
        hashTable.put(this.client,id);
        this.id = id;
        this.usuarioConnect();
        this.amigos = new RQAmigos(id);
        this.usuarios = new RQUsuarios(id);
        this.grupos = new RQGrupos(id);
        this.mensajes = new RQMensajes(id);
    }
         
    
    /*
    --------------------
    |     CUENTA       |
    --------------------
    */
    private String cuentaLogin(String contenido){
        Usuario usuario= gson.fromJson(contenido,Usuario.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK_LOGIN);
        int result = cuenta.login(usuario);
        if (result == -1) {
            mensajeSaliente.setContenido(410);
            client.closeSocket();
        }else{
            mensajeSaliente.setContenido(210);
            this.initialize(result);
        }
        return gson.toJson(mensajeSaliente);
    }
    
    private String cuentaRegister(String contenido){
        Usuario usuario = gson.fromJson(contenido, Usuario.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        int result = cuenta.register(usuario);        
        if (result == -1) {
            mensajeSaliente.setContenido(420);    
        }else{
            mensajeSaliente.setContenido(220);
            this.initialize(result);
        }
        return gson.toJson(mensajeSaliente);
    }
    
    
    
    /*
    --------------------
    |     AMIGOS       |
    --------------------
    */
    private String amigosAdd(String contenido){
        Usuario usuario = gson.fromJson(contenido, Usuario.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        if(amigos.add(usuario)==-1)
            mensajeSaliente.setContenido(441);
        else mensajeSaliente.setContenido(241);
        
        return gson.toJson(mensajeSaliente);
    }
    
    public String amigosDelete (String contenido) {
        Amigo amistad = gson.fromJson(contenido, Amigo.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        if(amigos.delete(amistad) == 1) mensajeSaliente.setContenido(242);
        else mensajeSaliente.setContenido(442);
        
        return gson.toJson(mensajeSaliente);
    }
    
    public String amigosInvite(String contenido){
        Usuario usuario = gson.fromJson(contenido, Usuario.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        if(amigos.invite(usuario)==-1)mensajeSaliente.setContenido(441);
        else mensajeSaliente.setContenido(241);
        return gson.toJson(mensajeSaliente);
    }
    
    public String amigosUpdate (String contenido) {
        Amigo amistad = gson.fromJson(contenido, Amigo.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        if (amigos.update(amistad) == 1)mensajeSaliente.setContenido(243);
        else mensajeSaliente.setContenido(443);
        return gson.toJson(mensajeSaliente);
    }
    
    public String amigosGetAll() {
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_AMIGOS);
        mensajeSaliente.setContenido(amigos.getAll());
        return gson.toJson(mensajeSaliente);
    }
    
    public String amigosGetConectados(){
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_AMIGOSCON);
        mensajeSaliente.setContenido(amigos.getConectados(this));
        return gson.toJson(mensajeSaliente);
    }
    
    public String amigosGetDesconectados() {
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_AMIGOSDES);
        mensajeSaliente.setContenido(amigos.getDesconectados(this));
        return gson.toJson(mensajeSaliente);
    }
    
    public String amigosGetPet(){
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_PETGRUPOS);
        mensajeSaliente.setContenido(amigos.getPet());
        return gson.toJson(mensajeSaliente);
    }
    
    public String amigosGetUsuarios(){
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_USUARIOS);
        mensajeSaliente.setContenido(amigos.getUsuarios());
        return gson.toJson(mensajeSaliente);
    }
    
    public String amigosConnectCheck(String contenido){
        Usuario usuario = gson.fromJson(contenido, Usuario.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setContenido(usuario);
        if(amigos.check(usuario))
            mensajeSaliente.setTipo(MTypes.SENDAMIGO_CONECTADO);
        else mensajeSaliente.setTipo(MTypes.SENDUSUARIO_CONECTADO);
        return gson.toJson(mensajeSaliente);
    }
    
    public String amigosDisconnectCheck(String contenido){
        Usuario usuario = gson.fromJson(contenido, Usuario.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setContenido(usuario);
        if(amigos.check(usuario))
            mensajeSaliente.setTipo(MTypes.SENDAMIGO_DESCONECTADO);
        else mensajeSaliente.setTipo(MTypes.SENDUSUARIO_DESCONECTADO);
        return gson.toJson(mensajeSaliente);
    }
    
    public String amigosReject(String contenido){
        Usuario usuario = gson.fromJson(contenido, Usuario.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        if(amigos.reject(usuario)==-1)mensajeSaliente.setContenido(490);
        else mensajeSaliente.setContenido(290);
        return gson.toJson(mensajeSaliente);
    }
    
    
    /*
    --------------------
    |     GRUPOS       |
    --------------------
    */
    public String gruposCreate(String contenido){
        NuevoGrupo nuevoGrupo = gson.fromJson(contenido, NuevoGrupo.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        if(grupos.create(nuevoGrupo)==-1)mensajeSaliente.setContenido(470);
        else mensajeSaliente.setContenido(270);
        return gson.toJson(mensajeSaliente);
    }
    
    public String gruposInvite(String contenido){
        PetGrupo usuario = gson.fromJson(contenido, PetGrupo.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        if(grupos.invite(usuario) == -1)mensajeSaliente.setContenido(471);
        else mensajeSaliente.setContenido(271);
        return gson.toJson(mensajeSaliente);
    }
    
    public String gruposAdd(String contenido){
        Grupo grupo = gson.fromJson(contenido, Grupo.class);
        Comunicacion mensajeSaliente = new Comunicacion();        
        mensajeSaliente.setTipo(MTypes.ACK);
        if(grupos.add(grupo)==-1)
            mensajeSaliente.setContenido(471);
        else mensajeSaliente.setContenido(271);
        return gson.toJson(mensajeSaliente);
    }
    
    public String gruposDelete(String contenido) {
        Integrante integrante = gson.fromJson(contenido, Integrante.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);  
        if (grupos.delete(integrante) == 1)mensajeSaliente.setContenido(273);
        else mensajeSaliente.setContenido(473);
        return gson.toJson(mensajeSaliente);
    }
    
    public String gruposGetInfo(String contenido){
        Grupo grupo = gson.fromJson(contenido, Grupo.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_INFOGRUPO);
        mensajeSaliente.setContenido(grupos.getInfo(grupo));
        return gson.toJson(mensajeSaliente);
    }
    
    public String gruposGetPet(){
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_PETGRUPOS);
        mensajeSaliente.setContenido(grupos.getPet());
        return gson.toJson(mensajeSaliente);
    }
    
    public String gruposGetAll(){
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_GRUPOS);
        mensajeSaliente.setContenido(grupos.getAll());
        return gson.toJson(mensajeSaliente);
    }
    
    public String gruposUpdate (String contenido) {
        Grupo grupo = gson.fromJson(contenido, Grupo.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        if (grupos.update(grupo) == 1) mensajeSaliente.setContenido(272);
        else mensajeSaliente.setContenido(472);
        return gson.toJson(mensajeSaliente);
    }
    
    public String gruposReject (String contenido){
        Grupo grupo = gson.fromJson(contenido, Grupo.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        if(grupos.reject(grupo)==-1)mensajeSaliente.setContenido(490);
        else mensajeSaliente.setContenido(290);
        return gson.toJson(mensajeSaliente);
    }
    
    
    
    /*
    --------------------
    |    USUARIOS      |
    --------------------
    */
    public String usuariosGetAll(){
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_LUSUARIOS);
        mensajeSaliente.setContenido(usuarios.getAll());
        return gson.toJson(mensajeSaliente);
    }
    
    public String usuariosGetDesconectados(){
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_DESCONECTADOS);
        mensajeSaliente.setContenido(usuarios.getDesconectados(this));
        return gson.toJson(mensajeSaliente);
    }
    
    public String usuariosGetConectados(){
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_CONECTADOS);
        mensajeSaliente.setContenido(usuarios.getConectados(this));
        return gson.toJson(mensajeSaliente);
    }
     
     
    /*
    --------------------
    |   MENSAJES       |
    --------------------
    */ 
    public String mensajesSendPersonal(String contenido){
        Mensaje mensaje = gson.fromJson(contenido, Mensaje.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        if(mensajes.sendPersonal(mensaje, this)==0)
            mensajeSaliente.setContenido(260);
        else mensajeSaliente.setContenido(460);
        return gson.toJson(mensajeSaliente);
    }
    
    public String mensajesSendGrupo(String contenido) {
        MensajeGrupo mensaje = gson.fromJson(contenido, MensajeGrupo.class);
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);
        if (mensajes.sendGrupo(mensaje,this,file)==-1)
            mensajeSaliente.setContenido(480);            
        else mensajeSaliente.setContenido(280);//Mensaje no almacenado ni enviado
        return gson.toJson(mensajeSaliente);
    }
    
    public String enviarMensajesIntegranteGrupo (Usuario u, Grupo gpo) {
         //return mensajes.getGrupo(u, this, this.file, gpo);
         return "";
    }
    
    public String mensajesGetGrupo(){
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_MENSAJES_GRUPOS);
        mensajeSaliente.setContenido(mensajes.getGrupoAll(this.file));
        return gson.toJson(mensajeSaliente);
    }
}

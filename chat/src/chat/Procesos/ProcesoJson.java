/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Procesos;

import chat.Controladores.AmigosController;
import chat.Controladores.ArchivosController;
import chat.Modelos.Comunicacion;
import chat.Modelos.Comunicacion.MTypes;
import chat.Controladores.CuentasController;
import chat.Controladores.GruposController;
import chat.Controladores.IntegrantesController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
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
        String contenido;
        mensajeEntrante = gson.fromJson(JSON, Comunicacion.class);
        contenido = gson.toJson(mensajeEntrante.getContenido());
        
        switch(mensajeEntrante.getTipo()){
            case RQ_LOGIN: 
                return login(gson.fromJson(contenido,Usuario.class));
            case RQ_REG:
                return registro(
                        gson.fromJson(contenido,
                        Usuario.class
                        )
                );
            case RQ_MENSAJE:
                return mensaje(
                        gson.fromJson(contenido,
                        Mensaje.class
                        )
                );
            case RQ_MENSAJE_GRUPO:
                return mensajeGrupo(
                        gson.fromJson(contenido,
                        MensajeGrupo.class
                        )
                );
            case RQ_NAMIGO:
                return agregarAmigo(
                        gson.fromJson(contenido,
                        Usuario.class
                        )
                );
            case RQ_LOGOUT:
                return logout();
            case RQ_AAMIGO:
                return aceptarAmigo(
                        gson.fromJson(contenido,
                        Usuario.class
                        )
                );
            case RQ_DAMIGO:
                return olvidarAmigo(
                        gson.fromJson(contenido,
                        Amigo.class
                        )
                );
            case RQ_APODO:
                return actualizarApodoAmigo(
                        gson.fromJson(contenido,
                        Amigo.class
                        )
                );
            case RQ_CONECTADOS:
                return getConectados();
            case RQ_DESCONECTADOS:
                return getDesconectados();
            case RQ_CGRUPO:
                return crearGrupo(
                        gson.fromJson(contenido,
                        NuevoGrupo.class
                        )
                );
            case RQ_NMIEMBRO:
                return nuevoMiembro(
                        gson.fromJson(contenido,
                        PetGrupo.class
                        )
                );
            case RQ_DMIEMBRO: 
                return deleteMiembro(
                        gson.fromJson(contenido,
                        Integrante.class)
                );
            case RQ_GRUPO:
                return cambiarNombreGrupo(
                        gson.fromJson(contenido,
                        Grupo.class
                        )
                );
            case RQ_AMIGOSCON:
                return getAmigosCon();
            case RQ_AMIGOSDES:
                return getAmigosDes();
            case RQ_INFOGRUPO:
                return getInfoGrupo(
                        gson.fromJson(contenido,Grupo.class)
                );
            case RQ_GRUPOS:
                return getGrupos();
                
            case RQ_MENSAJES_GRUPO_NO_RECIBIDOS:
                    return enviarMensajesIntegranteGrupo(
                            gson.fromJson(contenido,Usuario.class)
                    );
            case RQ_LUSUARIOS:
                    return getUsuariosAll();
            case RQ_AMIGOS:
                    return getAmigos();
            case RQ_USUARIOS:
                    return getUsuarios();
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
        mensaje.getOrigen().setId(hashTable.get(client));
        if(destino!=null){
            mensajeRemoto.setTipo(MTypes.SEND_MENSAJE);
            mensajeRemoto.setContenido(mensaje);
            destino.enviarMensaje(gson.toJson(mensajeRemoto));
            mensajeSaliente.setContenido(260);
        }else mensajeSaliente.setContenido(460);
        return gson.toJson(mensajeSaliente);
    }
      
     /** Gracias Vanya <3
     * 
     */
    /** 
     * Este método recibe un objeto de tipo mensaje de grupo que será almacenado
     * de ser enviado a todos los integrantes de grupo.
     * 
     * 
     * Primero se crean los objetos "uCtrl", de clase UsuariosController, y 
     * "integrantesGrupo", de clase IntegrantesController. En seguida se crea 
     * un objeto de tipo Usuario con nombre "remitente", al que se le dará el 
     * valor del usuario que haya enviado el mensaje, mediante la búsqueda del
     * id de usuario de la tabla usuarios de la base de datos. Este id es 
     * obtenido de la tabla hash de usuarios conectados buscando el Hilo del 
     * cliente que envió la solicitud de envío de mensaje de grupo. Luego de 
     * esto, el objeto "remitente" es enviado al método setRemitente del objeto 
     * "mensaje".
     * Después se crea un objeto de tipo Grupo, que toma el valor del grupo al 
     * que se enviará el mensaje; éste que se obtiene de la propiedad grupo del
     * objeto "mensaje". 
     * 
     * Después de esto se declaran tres listas:
     * - una de tipo Usuario de nombre "users"  para almacenar todos los 
     *  usuarios registrados en la base de datos. 
     * - otra de tipo Integer de nombre "integGpoX" para almacenar los 
     * integrantes pertenecientes al grupo enviado en el objeto "mensaje"
     * - y otra también de tipo integer que será utilizada para almacenar el id
     * de los grupos en los que un determinado usuario es integrante.
     * 
     * La primera y segunda lista son instanciadas en el momento de su declaración,
     * la primera con el resultado de la ejecución del método Select del objeto 
     * "uCtrl" y la segunda creando un nuevo espacio de memoria de tipo ArrayList.
     * 
     * Después de esto se crea un ciclo iterativo que será ejecutado por cada
     * elemento (usuario) existente en la lista "integGpoX". Creará una nueva
     * instancia del objeto "gruposUsuario" cada vez que se ejecute, para 
     * almacenar todos id de los grupos a los que pertenece el usuario iterado en
     * el ciclo iterativo, con ello se ejecutará un ciclo iterativo anidado por
     * cada grupo al que pertenece determinado usuario para verificar dentro de 
     * este si el grupo al que será enviado el mensaje se encuentra dentro de la
     * lista de grupos a los que pertenece el usuario iterado. Si es cierto el 
     * usuario es agregado a la lista integ "integGpoX".
     * 
     * Después de terminar el ciclo se establecen los integrantes al objeto 
     * "mensaje" invocando el método setIntegrantes enviando como parámetro la
     * lista de nombre "integGpoX".
     * 
     * Se instancía un objeto de tipo Comunicacion con nombre "mensajeSaliente"
     * para enviar un mensaje de reconocimiento al usuario que envió el mensaje
     * de grupo, que especificará si se pudo almacenar el mensaje en el servidor
     * y fueron enviados los mensaje de grupo a los usuarios conectados 
     * 
     * @param mensaje
     * @return 
     */
    public String mensajeGrupo(MensajeGrupo mensaje) {
        
        UsuariosController uCtrl = new UsuariosController();
        IntegrantesController integrantesGrupo = new IntegrantesController();
                           
        Usuario remitente = uCtrl.getUsuario(this.hashTable.get(client));
        Grupo gpoEnviado = mensaje.getGrupo();
        
        mensaje.setRemitente(remitente);
        mensaje.getMensaje().setOrigen(remitente);
                        
        List <Usuario> users = uCtrl.Select();
        List <Integer> integGpoX = new ArrayList<>();
        List <Integer> gruposUsuario;

        for (Usuario u : users) {
            if (u != mensaje.getRemitente()) {
                gruposUsuario = new ArrayList<>();
                gruposUsuario = integrantesGrupo.getListOfGrupos(u.getId());
                for (int gp : gruposUsuario) {
                    if (gp == gpoEnviado.getId()) { //Menos el mismo usuario
                        integGpoX.add(u.getId());
                    }
                }
            }                    
        }
        
        mensaje.setIntegrantes(integGpoX);
        
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.ACK);               
                
        ArchivosController mensajesGposFile = new ArchivosController (
                System.getProperty("user.dir") + "\\mensajesGrupos.json"
        );
        
        boolean checkWriteMensajeGrupo =
                mensajesGposFile.writeFile(gson.toJson(mensaje));
        
        if (mensajesGposFile.writeFile(gson.toJson(mensaje))) {
            //Mensaje almacenado                              
            
            //No se si quieran esta lista para saber a quienes se les envió
            //mensajes de grupos
            List <String> respuestasEnvioSmsGpo = new ArrayList<>(); 
            for (Usuario user : users) {
                respuestasEnvioSmsGpo.add(
                        enviarMensajesIntegranteGrupo(user)
                );
            }
            
            mensajeSaliente.setContenido(280);
            
        } else {
            mensajeSaliente.setContenido(480);//Mensaje no almacenado ni enviado
        }        
        
        return gson.toJson(mensajeSaliente);
        
    }
    
    public String enviarMensajesIntegranteGrupo (Usuario u) {
        Comunicacion mensajeSaliente = new Comunicacion();                          
        int id_Usuario = u.getId();
        Hilo destino = isConectado(id_Usuario);
        
        if(destino != null) {       //Usuario conectado
            int smsEnviados = 0;
            IntegrantesController integrantesGrupo = new IntegrantesController();
            ArchivosController fileManager = new ArchivosController();            
            
            List <Integer> gruposPertenece = 
                    integrantesGrupo.getListOfGrupos(id_Usuario);
            List <String> mensajesPorRecibir = fileManager.readFile();             
            List <MensajeGrupo> smsPorRecibir = new ArrayList<>();
                        
            //Los mensajes de grupos que recibir
            for (String gpSmsOnStr : mensajesPorRecibir) {
                MensajeGrupo smsParaAñadir = new MensajeGrupo();
                smsParaAñadir = gson.fromJson(gpSmsOnStr, MensajeGrupo.class);
                smsPorRecibir.add(smsParaAñadir);
            }
            
            //Verificamos si hay mensajes por enviar
            if (smsPorRecibir.size() > 0) {
                for (MensajeGrupo smsOnObj : smsPorRecibir) {                    
                    for (Integer gpoPertenece : gruposPertenece) {
                        if (gpoPertenece == smsOnObj.getGrupo().getId()) {                                                        
                            //Enviar mensaje a usuario destino
                            Comunicacion mensajeRemoto = new Comunicacion();        
                            mensajeRemoto.setTipo(MTypes.SEND_MENSAJE_GRUPO);
                            mensajeRemoto.setContenido(smsOnObj.getMensaje());                          
                            destino.enviarMensaje(gson.toJson(mensajeRemoto));
                            ++smsEnviados;
                            
                            //Usuario elimnado de mensajeGrupo que enviar
                            smsOnObj.getIntegrantes().remove(id_Usuario);
                        }
                    }                    
                }           
                
                //Los mensajesGrupo actualizados (integrante por enviar eliminado)
                //se pasan a una List de string para sobre escibir el .json
                //que contiene los mensajesGrupo a enviar                                
                mensajesPorRecibir = new ArrayList<>();
                for (MensajeGrupo smsOnObj : smsPorRecibir) {
                    if (smsOnObj.getIntegrantes().size() < 1) {
                        //Ya no hay integrantes a los que enviar el mensajeGrupo
                        //Eliminar de mensajesGrupo por enviar
                        smsPorRecibir.remove(smsOnObj);
                    } else {
                        mensajesPorRecibir.add(gson.toJson(smsOnObj));
                    }
                }
                
                if (mensajesPorRecibir.size() > 0) {
                    //Se actualizaron los mensajes grupo que se enviarán y aún 
                    //hay mensajes por enviar
                    fileManager.overwriteFile(mensajesPorRecibir);
                    mensajeSaliente.setTipo(MTypes.ACK);
                    mensajeSaliente.setContenido(720);  //Ahí me dicen que pongo
                } else {
                    //Se actualizaron los mensajes grupo que se enviarán pero 
                    //ya no hay mensajes por enviar
                    mensajeSaliente.setTipo(MTypes.ACK);
                    mensajeSaliente.setContenido(721);  //Ahí me dicen que pongo
                }
                
            } else if (smsPorRecibir.size() < 1) {
                //No hay mensajes de grupo que enviar                
                mensajeSaliente.setTipo(MTypes.ACK);
                mensajeSaliente.setContenido(482);
            }                                       
        } else {
            //Usuario no conectado
            mensajeSaliente.setTipo(MTypes.ACK);
            mensajeSaliente.setContenido(481);   
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
        IntegrantesController integrantesController = new IntegrantesController();
        Integrante integrante = new Integrante();
        mensajeSaliente.setTipo(MTypes.ACK);
        
        nuevoGrupo.getGrupo().setAdmin(hashTable.get(client));
        int id = grupos.Insert(nuevoGrupo.getGrupo());
        integrante.setGrupo(id);
        integrante.setUsuario(hashTable.get(client));
        integrantesController.Insert(integrante);
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
        List<Amigo> amigos = relacionAmigos.Select();
        
        int id = hashTable.get(client);
        Amigo oldAmistad = new Amigo();
        for(Amigo amigo:amigos){
            if(amigo.getAmigo1()==amistad.getAmigo1()&&amigo.getAmigo2()==id)
                oldAmistad=amigo;
            if(amigo.getAmigo2()==amistad.getAmigo1()&&amigo.getAmigo1()==id)
                oldAmistad=amigo;
        }        
        if(relacionAmigos.Delete(oldAmistad) == 1) {
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
        AmigosController amigosController = new AmigosController();
        int id = this.hashTable.get(client);
        int idUsr = amistad.getAmigo1();
        
        List<Amigo> amigos = amigosController.Select();
        Amigo oldAmistad = new Amigo();
        for(Amigo amigo:amigos){
            if(amigo.getAmigo1()==id && amigo.getAmigo2()==idUsr){
                oldAmistad=amigo;
                oldAmistad.setApodo2(amistad.getApodo1());
                break;
            }
            if(amigo.getAmigo1()==idUsr && amigo.getAmigo2()==id){
                oldAmistad=amigo;
                oldAmistad.setApodo1(amistad.getApodo1());
                break;
            }
        }        
        
        if (amigosController.Update(oldAmistad) == 1) {
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
        AmigosController amigosController = new AmigosController();
        List<Usuario> usuariosList = usuarios.Select();
        List<Usuario> result = new ArrayList<>();
        List<Amigo> amigos = amigosController.Select();
        List<Integer> amigosUsuario = new ArrayList<>();
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_CONECTADOS);
        
        int id=hashTable.get(client);
        boolean cont = false;
        for(Amigo amigo:amigos){
            if(amigo.getAmigo1()==id)
                amigosUsuario.add(amigo.getAmigo2());
            else if(amigo.getAmigo2()==id)
                amigosUsuario.add(amigo.getAmigo1());
        }
        
        for(Usuario usuario: usuariosList){
            if(isConectado(usuario.getId())!=null && usuario.getId() != id){
                cont = false;
                for(Integer amigo:amigosUsuario){
                    if(amigo == usuario.getId()){
                        cont = true;
                        break;
                    }
                }
                if(cont == false){
                    usuario.setPassword(" ");
                    result.add(usuario);
                }
            }
        }
        mensajeSaliente.setContenido(result);
        return gson.toJson(mensajeSaliente);
    }
    
    public String getDesconectados(){
        UsuariosController usuarios = new UsuariosController();
        AmigosController amigosController = new AmigosController();
        List<Usuario> usuariosList = usuarios.Select();
        List<Usuario> result = new ArrayList<>();
        List<Amigo> amigos = amigosController.Select();
        List<Integer> amigosUsuario = new ArrayList<>();
        Comunicacion mensajeSaliente = new Comunicacion();
        mensajeSaliente.setTipo(MTypes.SEND_DESCONECTADOS);
        
        int id=hashTable.get(client);
        boolean cont = false;
        for(Amigo amigo:amigos){
            if(amigo.getAmigo1()==id)
                amigosUsuario.add(amigo.getAmigo2());
            else if(amigo.getAmigo2()==id)
                amigosUsuario.add(amigo.getAmigo1());
        }
        
        for(Usuario usuario: usuariosList){
            if(isConectado(usuario.getId())== null){
                cont = false;
                for(Integer amigo:amigosUsuario){
                    if(amigo == usuario.getId()){
                        cont = true;
                        break;
                    }
                }
                if(cont == false){
                    usuario.setPassword(" ");
                    result.add(usuario);
                }
                
            }
        }
        mensajeSaliente.setContenido(result);
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
    
    public String getAmigosCon(){
        AmigosController amigosController = new AmigosController();
        Comunicacion mensajeSaliente = new Comunicacion();
        int id = hashTable.get(client);
        List<Amigo> amigosAll = amigosController.Select();
        List<Amigo> result = new ArrayList<>();
        for(Amigo amigo:amigosAll){
            if(amigo.getAmigo1()==id){
                if(isConectado(amigo.getAmigo2())!= null){
                    amigo.setAmigo1(-1);
                    result.add(amigo);
                }
            }else if(amigo.getAmigo2()==id){
                if(isConectado(amigo.getAmigo1())!=null){
                    amigo.setAmigo2(-1);
                    result.add(amigo);
                }
            }
        }
        mensajeSaliente.setTipo(MTypes.SEND_AMIGOSCON);
        mensajeSaliente.setContenido(result);
        return gson.toJson(mensajeSaliente);
    }
   
    public String getGrupos(){
        IntegrantesController integrantesController = new IntegrantesController();
        GruposController gruposController = new GruposController();
        Comunicacion mensajeSaliente = new Comunicacion();
        List<Integer> idsGrupo = integrantesController.getListOfGrupos(hashTable.get(client));
        List<Grupo> gruposAll = gruposController.Select();
        List<Grupo> result = new ArrayList<>();
        
        mensajeSaliente.setTipo(MTypes.SEND_GRUPOS);
        for(Grupo grupo:gruposAll){
            for(Integer id:idsGrupo){
                if(grupo.getId()==id)result.add(grupo);
            }
        }
        mensajeSaliente.setContenido(result);
        return gson.toJson(mensajeSaliente);
    }
   
    public String getUsuariosAll(){
        UsuariosController usuariosController = new UsuariosController();
        Comunicacion mensajeSaliente = new Comunicacion();
        List<Usuario> usuarios = usuariosController.Select();
        List<Usuario> result = new ArrayList<>();
        int id = hashTable.get(client);
        for(Usuario usuario:usuarios){
            usuario.setPassword(" ");
            if(usuario.getId()!=id)result.add(usuario);
        }
        mensajeSaliente.setTipo(MTypes.SEND_LUSUARIOS);
        mensajeSaliente.setContenido(result);
        return gson.toJson(mensajeSaliente);
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
    
    public String getAmigosDes(){
         AmigosController amigosController = new AmigosController();
        Comunicacion mensajeSaliente = new Comunicacion();
        int id = hashTable.get(client);
        List<Amigo> amigosAll = amigosController.Select();
        List<Amigo> result = new ArrayList<>();
        for(Amigo amigo:amigosAll){
            if(amigo.getAmigo1()==id){
                if(isConectado(amigo.getAmigo2())== null){
                    amigo.setAmigo1(-1);
                    result.add(amigo);
                }
            }else if(amigo.getAmigo2()==id){
                if(isConectado(amigo.getAmigo1())==null){
                    amigo.setAmigo2(-1);
                    result.add(amigo);
                }
            }
        }
        mensajeSaliente.setTipo(MTypes.SEND_AMIGOSDES);
        mensajeSaliente.setContenido(result);
        return gson.toJson(mensajeSaliente);
    }
    
    public String getAmigos(){
        Comunicacion mensajeSaliente = new Comunicacion();
        AmigosController amigosController = new AmigosController();
        List<Amigo> amigos = amigosController.Select();
        List<Amigo> result = new ArrayList<>();
        
        mensajeSaliente.setTipo(MTypes.SEND_AMIGOS);
        int id = hashTable.get(client);
        for (Amigo amigo:amigos){
            if(amigo.getAmigo1()==id){
                amigo.setAmigo1(-1);
                result.add(amigo);
            }else if(amigo.getAmigo2()==id){
                amigo.setAmigo2(-1);
                result.add(amigo);
            }
        }
        mensajeSaliente.setContenido(result);
        return gson.toJson(mensajeSaliente);
    }
    
    public String getUsuarios(){
        Comunicacion mensajeSaliente = new Comunicacion();
        UsuariosController usuariosController = new UsuariosController();
        PetAmigosController petAmigoController = new PetAmigosController();
        AmigosController amigosController = new AmigosController();
        List<Usuario> usuarios = usuariosController.Select();
        List<Usuario> result = new ArrayList<>();
        List<Integer> idUsuario = new ArrayList<>();
        List<PetAmigo> petAmigos = petAmigoController.Select();
        List<Amigo> amigos = amigosController.Select();
        boolean cont;
        
        mensajeSaliente.setTipo(MTypes.SEND_USUARIOS);
        int id=hashTable.get(client);
        for(PetAmigo petAmigo:petAmigos){
            if(petAmigo.getSolicitado()==id)
                idUsuario.add(petAmigo.getSolicitante());
            else if(petAmigo.getSolicitante()==id)
                idUsuario.add(petAmigo.getSolicitado());
        }
        
        for(Amigo amigo:amigos){
            if(amigo.getAmigo1()==id)
                idUsuario.add(amigo.getAmigo2());
            else if(amigo.getAmigo2()==id)
                idUsuario.add(amigo.getAmigo1());
        }
        
        for(Usuario usuario: usuarios){
            if(isConectado(usuario.getId())== null){
                cont = false;
                for(Integer idUsr:idUsuario){
                    if(idUsr == usuario.getId()){
                        cont = true;
                        break;
                    }
                }
                if(cont == false){
                    usuario.setPassword(" ");
                    result.add(usuario);
                }
                
            }
        }
        mensajeSaliente.setContenido(result);
        return gson.toJson(mensajeSaliente);
    }
}

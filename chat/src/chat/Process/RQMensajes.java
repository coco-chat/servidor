/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Process;

import chat.Controllers.ArchivosController;
import chat.Controllers.IntegrantesController;
import chat.Controllers.UsuariosController;
import chat.Models.Comunicacion;
import chat.Models.Grupo;
import chat.Models.Integrante;
import chat.Models.Mensaje;
import chat.Models.MensajeGrupo;
import chat.Models.Usuario;
import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class RQMensajes {
    private final int id;

    public RQMensajes() {
        id = -1;
    }

    public RQMensajes(int id) {
        this.id = id;
    }
    
    public int sendPersonal(Mensaje mensaje, ProcesoJson proceso){
        Comunicacion mensajeRemoto = new Comunicacion();
        Hilo destino = proceso.isConectado(mensaje.getDestino().getId());
        mensaje.getOrigen().setId(id);
        Gson gson = new Gson();
        if(destino!=null){
            mensajeRemoto.setTipo(Comunicacion.MTypes.SEND_MENSAJE);
            mensajeRemoto.setContenido(mensaje);
            return destino.enviarMensaje(gson.toJson(mensajeRemoto));
        }else return -1;
    }
    
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
    public int sendGrupo(MensajeGrupo mensaje, ProcesoJson proceso, File file){
        IntegrantesController integrantesController = new IntegrantesController();
        UsuariosController usuariosController = new UsuariosController();
        ArchivosController mensajesGposFile = new ArchivosController (file);
        Comunicacion mensajeSaliente = new Comunicacion();      
        Gson gson = new Gson();
        Usuario usuario = new Usuario();
        
        mensaje.setRemitente(usuariosController.getUsuario(id));
        mensajeSaliente.setTipo(Comunicacion.MTypes.ACK); 
                        
        List <Usuario> users = usuariosController.Select();
        List <Usuario> integGpoX = new ArrayList<>();
        List <Integrante> integrantes = integrantesController.Select();

        for(Integrante integrante:integrantes){
            if(integrante.getGrupo()==mensaje.getGrupo().getId()){
                if(integrante.getUsuario()!=id)
                    usuario = usuariosController.getUsuario(
                            integrante.getUsuario()
                    );
                    usuario.setPassword(" ");
                    if(getGrupo(usuario,proceso, mensaje)==-1)
                        integGpoX.add(usuario);
            }
        }
        mensaje.setIntegrantes(integGpoX);
        mensajesGposFile.writeFile(gson.toJson(mensaje));
        return 0;        
    }
    
    public int getGrupo(Usuario usuario, ProcesoJson proceso, MensajeGrupo mensaje){
        Gson gson = new Gson();
        Hilo hilo = proceso.isConectado(usuario.getId());
        Comunicacion mensajeSalida = new Comunicacion();
        if(hilo!=null){
            mensajeSalida.setTipo(Comunicacion.MTypes.SEND_MENSAJE_GRUPO);
            mensaje.setIntegrantes(new ArrayList<>());
            mensajeSalida.setContenido(mensaje);
            return hilo.enviarMensaje(gson.toJson(mensajeSalida));
        }return -1;
    }
    
    public List<MensajeGrupo> getGrupoAll(File file){
        ArchivosController archivosController = new ArchivosController(file);
        List<String> mensajes = archivosController.readFile();
        List<MensajeGrupo> mensajesGrupo = new ArrayList<>();
        List<MensajeGrupo> result = new ArrayList<>();
        List<Usuario> save;
        MensajeGrupo mensajeGrupo;
        MensajeGrupo auxiliar;
        Gson gson = new Gson();
        
        for(String mensaje:mensajes){
            mensajeGrupo = gson.fromJson(mensaje, MensajeGrupo.class);
            
            save = new ArrayList<>();
            for(Usuario integrante:mensajeGrupo.getIntegrantes()){
                if(integrante.getId()==id){
                    auxiliar = gson.fromJson(mensaje, MensajeGrupo.class);
                    auxiliar.setIntegrantes(new ArrayList<>());
                    result.add(auxiliar);
                    break;
                }else save.add(integrante);
            }
            mensajeGrupo.setIntegrantes(save);
            if(mensajeGrupo.getIntegrantes().size()>0)
                mensajesGrupo.add(mensajeGrupo);
        }
        
        mensajes = new ArrayList<>();
        for(MensajeGrupo mensaje: mensajesGrupo){
            mensajes.add(gson.toJson(mensaje));
        }
        archivosController.overwriteFile(mensajes);
        
        return result;
    }
}

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
import chat.Models.Mensaje;
import chat.Models.MensajeGrupo;
import chat.Models.Usuario;
import com.google.gson.Gson;
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
    public int sendGrupo(MensajeGrupo mensaje, ProcesoJson proceso){
        Gson gson = new Gson();
        UsuariosController uCtrl = new UsuariosController();
        IntegrantesController integrantesGrupo = new IntegrantesController();
                           
        Usuario remitente = uCtrl.getUsuario(id);
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
        mensajeSaliente.setTipo(Comunicacion.MTypes.ACK);               
                
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
                        getGrupo(user,proceso)
                );
            }
            
            return 0;
            
        } else {
            return -1;//Mensaje no almacenado ni enviado
        }        
    }
    
    public String getGrupo(Usuario u, ProcesoJson proceso){
        Gson gson = new Gson();
        Comunicacion mensajeSaliente = new Comunicacion();                          
        int id_Usuario = u.getId();
        Hilo destino = proceso.isConectado(id_Usuario);
        
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
                MensajeGrupo smsParaAdd = new MensajeGrupo();
                smsParaAdd = gson.fromJson(gpSmsOnStr, MensajeGrupo.class);
                smsPorRecibir.add(smsParaAdd);
            }
            
            //Verificamos si hay mensajes por enviar
            if (smsPorRecibir.size() > 0) {
                for (MensajeGrupo smsOnObj : smsPorRecibir) {                    
                    for (Integer gpoPertenece : gruposPertenece) {
                        if (gpoPertenece == smsOnObj.getGrupo().getId()) {                                                        
                            //Enviar mensaje a usuario destino
                            Comunicacion mensajeRemoto = new Comunicacion();        
                            mensajeRemoto.setTipo(Comunicacion.MTypes.SEND_MENSAJE_GRUPO);
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
                    mensajeSaliente.setTipo(Comunicacion.MTypes.ACK);
                    mensajeSaliente.setContenido(720);  //Ahí me dicen que pongo
                } else {
                    //Se actualizaron los mensajes grupo que se enviarán pero 
                    //ya no hay mensajes por enviar
                    mensajeSaliente.setTipo(Comunicacion.MTypes.ACK);
                    mensajeSaliente.setContenido(721);  //Ahí me dicen que pongo
                }
                
            } else if (smsPorRecibir.size() < 1) {
                //No hay mensajes de grupo que enviar                
                mensajeSaliente.setTipo(Comunicacion.MTypes.ACK);
                mensajeSaliente.setContenido(482);
            }                                       
        } else {
            //Usuario no conectado
            mensajeSaliente.setTipo(Comunicacion.MTypes.ACK);
            mensajeSaliente.setContenido(481);   
        }
        return gson.toJson(mensajeSaliente);      
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Process;

import chat.Controllers.AmigosController;
import chat.Controllers.PetAmigosController;
import chat.Controllers.UsuariosController;
import chat.Models.Amigo;
import chat.Models.PetAmigo;
import chat.Models.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class RQAmigos {
    private final int id;

    public RQAmigos() {
        id = -1;
    }

    public RQAmigos(int id) {
        this.id = id;
    }
    
    /**
     * Añade un amigo aceptando la solucitud de amistad
     * @param usuario amigo solicitado
     * @return estado de la insercion. En caso de error -1
     */
    public int add(Usuario usuario){
        UsuariosController usuariosController = new UsuariosController();
        PetAmigosController petController = new PetAmigosController();
        AmigosController aceptar = new AmigosController();
        Amigo peticion = new Amigo();
        PetAmigo pet = new PetAmigo();
        String nombreA="";
        String nombreB="";
        int result;
        
        List<Usuario> usuarios = usuariosController.Select();
        for(Usuario x: usuarios){
            if(x.getId()==usuario.getId())nombreA=x.getUsername();
            else if(x.getId()==id)nombreB=x.getUsername();
        }
        
        peticion.setAmigo1(usuario.getId());
        peticion.setApodo1(nombreA);
        peticion.setAmigo2(id);
        peticion.setApodo2(nombreB);
        
        pet.setId(usuario.getId()+"-"+id);
        result = petController.Delete(pet);
        if(result==-1)return result;
        
        result = aceptar.Insert(peticion);
        return result;
    }
    
    /**
     * Elimina un amigo
     * @param amistad amistad a eliminar
     * @return el estado de la eliminación. En caso de error -1
     */
    public int delete(Amigo amistad){
        AmigosController relacionAmigos = new AmigosController();
        amistad.setId(amistad.getAmigo1());
        return relacionAmigos.Delete(amistad);
    }
    
    /**
     * Actualiza una amistad
     * @param amistad la amistad a actualizar
     * @return el estado de la actualizacion. En caso de error -1
     */
    public int update(Amigo amistad){
        AmigosController amigosController = new AmigosController();
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
        return amigosController.Update(oldAmistad);
    }
    
    /**
     * Obtiene los amigos del usuario específico
     * @return lista de amigos del usuario
     */
    public List<Amigo> getAll(){
        AmigosController amigosController = new AmigosController();
        List<Amigo> amigos = amigosController.Select();
        List<Amigo> result = new ArrayList<>();

        for (Amigo amigo : amigos) {
            if (amigo.getAmigo1() == id) {
                amigo.setAmigo1(-1);
                result.add(amigo);
            } else if (amigo.getAmigo2() == id) {
                amigo.setAmigo2(-1);
                result.add(amigo);
            }
        }
        return result;
    }
    
    /**
     * Agregar una petición de amigo
     * @param usuario el usuario a pedir
     * @return el estado de la invitacion. En caso de error -1
     */
    public int invite(Usuario usuario){
        PetAmigo peticion = new PetAmigo();
        PetAmigosController agregar = new PetAmigosController();
        
        if(agregar.verificarPeticion(id, usuario.getId()) == 0) {
            peticion.setSolicitado(usuario.getId());
            peticion.setSolicitante(id);
            if(agregar.Insert(peticion).isEmpty())return -1;
            return 0;
        }
        return -1;
    }
    
    /**
     * Obtiene los amigos conectados
     * @param proceso la referencia de los usuarios conectados
     * @return lista de amigos conectados
     */
    public List<Amigo> getConectados(ProcesoJson proceso){
        AmigosController amigosController = new AmigosController();
        List<Amigo> amigosAll = amigosController.Select();
        List<Amigo> result = new ArrayList<>();
        for(Amigo amigo:amigosAll){
            if(amigo.getAmigo1()==id){
                if(proceso.isConectado(amigo.getAmigo2())!= null){
                    amigo.setAmigo1(-1);
                    result.add(amigo);
                }
            }else if(amigo.getAmigo2()==id){
                if(proceso.isConectado(amigo.getAmigo1())!=null){
                    amigo.setAmigo2(-1);
                    result.add(amigo);
                }
            }
        }
        return result;
    }
    
    /**
     * Obtiene los amigos desconectados
     * @param proceso la referencia de los usuarios conectados
     * @return lista de amigos desconectados
     */
    public List<Amigo> getDesconectados(ProcesoJson proceso){
        AmigosController amigosController = new AmigosController();
        List<Amigo> amigosAll = amigosController.Select();
        List<Amigo> result = new ArrayList<>();
        for (Amigo amigo : amigosAll) {
            if (amigo.getAmigo1() == id) {
                if (proceso.isConectado(amigo.getAmigo2()) == null) {
                    amigo.setAmigo1(-1);
                    result.add(amigo);
                }
            } else if (amigo.getAmigo2() == id) {
                if (proceso.isConectado(amigo.getAmigo1()) == null) {
                    amigo.setAmigo2(-1);
                    result.add(amigo);
                }
            }
        }
        return result;
    }
    
    /**
     * Obtiene los usuarios con los que tiene una peticion de amigos
     * @return lista de usuarios con peticiones pendientes
     */
    public List<Usuario> getPet(){
        PetAmigosController peticionesController = new PetAmigosController();
        UsuariosController usuariosController = new UsuariosController();
        List<PetAmigo> peticiones = peticionesController.Select();
        List<Usuario> solicitantes = new ArrayList<>();
        Usuario usuario;
        
        for(PetAmigo petAmigo:peticiones){
            if(petAmigo.getSolicitado()==id){
                usuario= usuariosController.getUsuario(petAmigo.getSolicitante());
                usuario.setPassword(" ");
                solicitantes.add(usuario);
            }
        }
        return solicitantes;
    }
    
    /**
     * Obtiene los usuarios posibles amigos
     * @return lista de posibles amigos
     */
    public List<Usuario> getUsuarios(){
        UsuariosController usuariosController = new UsuariosController();
        PetAmigosController petAmigoController = new PetAmigosController();
        AmigosController amigosController = new AmigosController();
        List<Usuario> usuarios = usuariosController.Select();
        List<Usuario> result = new ArrayList<>();
        List<Integer> idUsuario = new ArrayList<>();
        List<PetAmigo> petAmigos = petAmigoController.Select();
        List<Amigo> amigos = amigosController.Select();
        boolean cont;
        
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
            if(usuario.getId()!=id){
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
        return result;
    }
    
    /**
     * Comprueba si un usuario es un amigo
     * @param usuario el usuario a comprobar
     * @return true si es amigo false no lo es
     */
    public boolean check(Usuario usuario){
        AmigosController amigosController = new AmigosController();
        List<Amigo> amigos = amigosController.Select();
        List<Amigo> amigosUsuario = new ArrayList<>();
        
        for(Amigo amigo:amigos){
            if(amigo.getAmigo1()==id)amigosUsuario.add(amigo);
            if(amigo.getAmigo2()==id)amigosUsuario.add(amigo);
        }
        
        for(Amigo amigo:amigosUsuario){
            if(amigo.getAmigo1()==usuario.getId())return true;
            if(amigo.getAmigo2()==usuario.getId())return true;
        }
        return false;
    }
    
    /**
     * Elimina una petición de amigos
     * @param usuario el usuario para eliminar la petición
     * @return el estado de la eliminación. En caso de erro r-1
     */
     public int reject(Usuario usuario){
        PetAmigosController petController = new PetAmigosController();
        PetAmigo pet = new PetAmigo();
        pet.setId(usuario.getId()+"-"+id);
        return petController.Delete(pet);
    }
}

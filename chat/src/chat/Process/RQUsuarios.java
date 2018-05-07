/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Process;

import chat.Controllers.AmigosController;
import chat.Controllers.UsuariosController;
import chat.Models.Amigo;
import chat.Models.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class RQUsuarios {
    private final int id;

    public RQUsuarios() {
        id = -1;
    }
    
    public RQUsuarios(int id){
        this.id = id;
    }
    
    public List<Usuario> getAll(){
        UsuariosController usuariosController = new UsuariosController();
        List<Usuario> usuarios = usuariosController.Select();
        List<Usuario> result = new ArrayList<>();
        for(Usuario usuario:usuarios){
            usuario.setPassword(" ");
            if(usuario.getId()!=id)result.add(usuario);
        }
        return result;
    }
    
    public List<Usuario> getConectados(ProcesoJson proceso){
        UsuariosController usuarios = new UsuariosController();
        AmigosController amigosController = new AmigosController();
        List<Usuario> usuariosList = usuarios.Select();
        List<Usuario> result = new ArrayList<>();
        List<Amigo> amigos = amigosController.Select();
        List<Integer> amigosUsuario = new ArrayList<>();
        boolean cont;
        for(Amigo amigo:amigos){
            if(amigo.getAmigo1()==id)
                amigosUsuario.add(amigo.getAmigo2());
            else if(amigo.getAmigo2()==id)
                amigosUsuario.add(amigo.getAmigo1());
        }
        
        for(Usuario usuario: usuariosList){
            if(proceso.isConectado(usuario.getId())!=null && usuario.getId() != id){
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
        return result;
    }
    
    public List<Usuario> getDesconectados(ProcesoJson proceso){
        UsuariosController usuarios = new UsuariosController();
        AmigosController amigosController = new AmigosController();
        List<Usuario> usuariosList = usuarios.Select();
        List<Usuario> result = new ArrayList<>();
        List<Amigo> amigos = amigosController.Select();
        List<Integer> amigosUsuario = new ArrayList<>();
        
        boolean cont = false;
        for(Amigo amigo:amigos){
            if(amigo.getAmigo1()==id)
                amigosUsuario.add(amigo.getAmigo2());
            else if(amigo.getAmigo2()==id)
                amigosUsuario.add(amigo.getAmigo1());
        }
        
        for(Usuario usuario: usuariosList){
            if(proceso.isConectado(usuario.getId())== null){
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
        return result;
    }
}

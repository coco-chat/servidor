/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Process;

import chat.Controllers.GruposController;
import chat.Controllers.IntegrantesController;
import chat.Controllers.PetGruposController;
import chat.Controllers.UsuariosController;
import chat.Models.Grupo;
import chat.Models.InfoGrupo;
import chat.Models.Integrante;
import chat.Models.NuevoGrupo;
import chat.Models.PetGrupo;
import chat.Models.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class RQGrupos {
    private final int id;

    public RQGrupos() {
        id=-1;
    }

    public RQGrupos(int id) {
        this.id = id;
    }
    
    public int create(NuevoGrupo grupo){
        GruposController grupos = new GruposController();
        PetGruposController petGrupos = new PetGruposController();
        IntegrantesController integrantesController = new IntegrantesController();
        Integrante integrante = new Integrante();
        
        grupo.getGrupo().setAdmin(id);
        int idGrupo = grupos.Insert(grupo.getGrupo());
        if(idGrupo == -1)return -1;
        integrante.setGrupo(idGrupo);
        integrante.setUsuario(id);
        integrantesController.Insert(integrante);
        for(PetGrupo pet: grupo.getIntegrantes()){
            pet.setGrupo(id);
            if(petGrupos.Insert(pet)==-1)return -1;
        }
        return idGrupo;
    }
    
    public int invite(PetGrupo usuario){
        PetGruposController pet_grupo = new PetGruposController();
        return pet_grupo.Insert(usuario);
    }
    
    public int add(Grupo grupo){
        IntegrantesController integrantesController = new IntegrantesController();
        PetGruposController petGruposController = new PetGruposController();
        List<PetGrupo> petGrupos = petGruposController.Select();
        PetGrupo petGrupoOld = new PetGrupo();
        
        Integrante integrante = new Integrante();
        integrante.setGrupo(grupo.getId());
        integrante.setUsuario(id);
        if(integrantesController.Insert(integrante)==-1)return -1;
        
        for(PetGrupo petGrupo:petGrupos){
            if(petGrupo.getGrupo()==grupo.getId()&&petGrupo.getUsuario()==id)
                petGrupoOld = petGrupo;
        }
        petGruposController.Delete(petGrupoOld);
        return 0;
    }
    
    public int delete(Integrante miembro){
        IntegrantesController manejadorIntegrantesGrupo = new IntegrantesController();
        List<Integrante> integrantes = manejadorIntegrantesGrupo.Select();
        Integrante integranteOld = new Integrante();
        
        int idGrupo = miembro.getGrupo();
        int idUsuario = miembro.getUsuario();
        
        for(Integrante integrante:integrantes){
            if(integrante.getGrupo()==idGrupo&&integrante.getUsuario()==idUsuario){
                integranteOld=integrante;
                break;
            }
        }
        return manejadorIntegrantesGrupo.Delete(integranteOld);
    }
    
    public InfoGrupo getInfo(Grupo grupo){
        UsuariosController usuariosController = new UsuariosController();
        PetGruposController petGruposController = new PetGruposController();
        List<Usuario> miembros = new ArrayList<Usuario>();
        List<PetGrupo> petGrupos = petGruposController.Select();
        List<Integer> idUsuarios = new ArrayList<>();
        List<Usuario> usuarios = usuariosController.Select();
        List<Usuario> noMiembros = new ArrayList<>();
        boolean flag;
     
        IntegrantesController integrantes = new IntegrantesController();
        InfoGrupo infogrupo = new InfoGrupo();
        
        int idGrupo = grupo.getId();
        
        List<Integrante> integranteLista = integrantes.Select();
        
        for(Integrante integrante: integranteLista){
            if(integrante.getGrupo() == idGrupo){
                Usuario usuario = new Usuario();
                usuario = usuariosController.getUsuario(integrante.getUsuario());
                miembros.add(usuario);
                idUsuarios.add(usuario.getId());
            }
        }
        
        for(PetGrupo petGrupo: petGrupos){
            if(petGrupo.getGrupo()==idGrupo)
                idUsuarios.add(petGrupo.getUsuario());
        }
        
        for(Usuario usuario: usuarios){
            flag = false;
            for(Integer idUsuario:idUsuarios){
                if(idUsuario==usuario.getId()){
                    flag = true;
                    break;
                }
            }
            if(flag==false)
                noMiembros.add(usuario);
        }
        
        infogrupo.setGrupo(grupo);
        infogrupo.setMiembros(miembros);
        if(grupo.getAdmin()==id)infogrupo.setAdmin(new Usuario());
        else infogrupo.setAdmin(
                usuariosController.getUsuario(grupo.getAdmin())
        );
        infogrupo.setNoMiembros(noMiembros);
        return infogrupo;
    }
    
    public List<Grupo> getPet(){
        PetGruposController peticionesController = new PetGruposController();
        GruposController gruposController = new GruposController();
        List<PetGrupo> peticiones = peticionesController.Select();
        List<Grupo> grupos = new ArrayList<>();
        Grupo grupo;        
        for(PetGrupo petGrupo:peticiones){
            if(petGrupo.getUsuario()==id){
                grupo = gruposController.getGrupo(petGrupo.getGrupo());
                grupos.add(grupo);
            }
        }
        return grupos;
    }
    
    public List<Grupo> getAll(){
        IntegrantesController integrantesController = new IntegrantesController();
        GruposController gruposController = new GruposController();
        List<Integer> idsGrupo = integrantesController.getListOfGrupos(id);
        List<Grupo> gruposAll = gruposController.Select();
        List<Grupo> result = new ArrayList<>();
        
        for(Grupo grupo:gruposAll){
            for(Integer idUsuario:idsGrupo){
                if(grupo.getId()==idUsuario)result.add(grupo);
            }
        }
        return result;
    }
    
    public int update(Grupo grupo){
        GruposController cambiarNombreIntermediario = new GruposController();
        return cambiarNombreIntermediario.Update(grupo);
    }
}

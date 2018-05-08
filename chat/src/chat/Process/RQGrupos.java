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
            pet.setGrupo(idGrupo);
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
        GruposController gruposController = new GruposController();
        PetGruposController petGruposController = new PetGruposController();
        List<Integrante> integrantes = manejadorIntegrantesGrupo.Select();
        List<Integrante> integrantesGrupo = new ArrayList<>();
        Integrante integranteOld = null;
        
        int idGrupo = miembro.getGrupo();
        int idUsuario = miembro.getUsuario();
        
        for(Integrante integrante:integrantes)
            if(integrante.getGrupo()==idGrupo)integrantesGrupo.add(integrante);
        
        for(Integrante integrante:integrantesGrupo){
            if(integrante.getUsuario()==idUsuario){
                integranteOld=integrante;
                break;
            }
        }
        
        if(integrantesGrupo.size()<=3&&integranteOld!=null){
            for(Integrante integrante:integrantesGrupo){
                manejadorIntegrantesGrupo.Delete(integrante);
            }
            for(PetGrupo petGrupo : petGruposController.Select()){
                if(petGrupo.getGrupo()==idGrupo)
                    petGruposController.Delete(petGrupo);
            }
            return gruposController.Delete(gruposController.getGrupo(idGrupo));
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
        Usuario usuarioAdd;
        boolean flag;
     
        IntegrantesController integrantes = new IntegrantesController();
        InfoGrupo infogrupo = new InfoGrupo();
        
        int idGrupo = grupo.getId();
        
        List<Integrante> integranteLista = integrantes.Select();
        
        for(Integrante integrante: integranteLista){
            if(integrante.getGrupo() == idGrupo){
                if(integrante.getUsuario()!=id){
                    usuarioAdd = usuariosController.getUsuario(integrante.getUsuario());
                    miembros.add(usuarioAdd);
                    idUsuarios.add(usuarioAdd.getId());
                }
            }
        }
        
        for(PetGrupo petGrupo: petGrupos){
            if(petGrupo.getGrupo()==idGrupo)
                idUsuarios.add(petGrupo.getUsuario());
        }
        
        for(Usuario usuario: usuarios){
            flag = false;
            for(Integer idUsuario:idUsuarios){
                if(idUsuario==usuario.getId() || usuario.getId()==id){
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
                if(grupo.getAdmin()==id)grupo.setAdmin(-1);
                if(grupo.getId()==idUsuario)result.add(grupo);
            }
        }
        return result;
    }
    
    public int update(Grupo grupo){
        GruposController cambiarNombreIntermediario = new GruposController();
        return cambiarNombreIntermediario.Update(grupo);
    }
    
     public int reject(Grupo grupo){
        PetGruposController petGruposController = new PetGruposController();
        List<PetGrupo> petGrupos = petGruposController.Select();
        PetGrupo petGrupoOld = new PetGrupo();
                
        for(PetGrupo petGrupo:petGrupos){
            if(petGrupo.getGrupo()==grupo.getId()&&petGrupo.getUsuario()==id)
                petGrupoOld = petGrupo;
        }
        return petGruposController.Delete(petGrupoOld);
    }
}

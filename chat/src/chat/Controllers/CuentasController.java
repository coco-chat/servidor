/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controllers;

import chat.Conexion;
import chat.Models.Usuario;
import chat.Hashing.Hash;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanc
 */
public class CuentasController {
    
    private Conexion db;
    
    public CuentasController() {
        this.db = new Conexion();
    }
    
    /**
     * Método para verificar un login
     * @param username String, nombre de usuario
     * @param password String, contraseña
     * @return int, 404 -> error de login, 200 -> correcto, 0 -> error de server
     */
    public int Login(String username, String password) {
        
        try {
            if (!(username.equals("") || password.equals(""))) {
                username = username.trim();
                UsuariosController c = new UsuariosController();
                List<Usuario> listaUsuarios = c.Select();
                List<Usuario> listaResultados = new ArrayList<>();
                for (Usuario x : listaUsuarios) {
                    if (x.getUsername().equals(username)) {
                        listaResultados.add(x);
                    }
                }
                if (listaResultados.size() == 1) {
                    if (listaResultados.get(0).getPassword()
                            .equals(Hash.sha1(password))) {
                        return listaResultados.get(0).getId();
                    }
                }
            }
            return -1;
        } catch(Exception ex) {
            return 0;
        }
        
    }
    
    /**
     * Método para crear un usuario
     * @param username String, nombre de usuario
     * @param password String, contraseña de usuario
     * @return int, 0 -> error de servidor, 404 -> nombre de usuario existente
     * 200 -> usuario agregado correctamente
     */
    public int Register(String username, String password) {
        try {
            if (!(username.equals("") || password.equals(""))) {
                username = username.trim();
                UsuariosController c = new UsuariosController();
                List<Usuario> listaUsuarios = c.Select();
                for (Usuario x : listaUsuarios) {
                    if (x.getUsername().equals(username)) {
                        return 405;
                    }
                }
                Usuario usuario = new Usuario();
                usuario.setUsername(username);
                usuario.setPassword(password);
                c.Insert(usuario);
                return 200;
            }
            return 404;
        } catch(Exception ex) {
            return 0;
        }
    }
    
}

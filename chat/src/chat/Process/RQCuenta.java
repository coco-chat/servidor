/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Process;
import chat.Controllers.CuentasController;
import chat.Models.Usuario;

/**
 *
 * @author kevin
 */
public class RQCuenta {

    public RQCuenta() {
    }
    
    public int login(Usuario usuario){
        CuentasController cuentas = new CuentasController();
        return cuentas.Login(usuario.getUsername(), usuario.getPassword());
    }
    
    public int register(Usuario usuario){
        CuentasController cuentas = new CuentasController();
        cuentas.Register(usuario.getUsername(), usuario.getPassword());
        return login(usuario);
    }
}

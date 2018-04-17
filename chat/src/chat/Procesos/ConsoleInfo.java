/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Procesos;

/**
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class ConsoleInfo {
    public static void accion(int hilo, String accion, String host, String mensaje){
        String result = "[H"+hilo+"]["+accion+"]["+host+"]"+mensaje;
        synchronized(System.out){ System.out.println(result);}
    }
    public static void error(int hilo, String accion, String mensaje){
        String result = "[H"+hilo+"]["+accion+"]Error: "+mensaje;
        synchronized(System.out){ System.out.println(result);}
    }
    public static void especial(String mensaje){
        synchronized(System.out){ System.out.println(mensaje);}
    }
}

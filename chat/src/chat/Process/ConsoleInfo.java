/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Process;

/**
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class ConsoleInfo {
    /**
     * Imprime la realización de una accion de un hilo en específico
     * @param hilo id del hilo que realiza la accion
     * @param accion acción que realiza el hilo 
     * @param host ip con la que tiene comunicación el hilo
     * @param mensaje el contenido de la accion
     */
    public static void accion(int hilo, String accion, String host, String mensaje){
        String result = "[H"+hilo+"]["+accion+"]["+host+"]"+mensaje;
        synchronized(System.out){ System.out.println(result);}
    }
    
    /**
     * Imprime un mensaje de error en consola
     * @param hilo id del hilo
     * @param accion donde realizó el error
     * @param mensaje el contenido del error
     */
    public static void error(int hilo, String accion, String mensaje){
        String result = "[H"+hilo+"]["+accion+"]Error: "+mensaje;
        synchronized(System.out){ System.out.println(result);}
    }
    /**
     * Mensaje especial
     * @param mensaje el mensaje a imiprimir
     */
    public static void especial(String mensaje){
        synchronized(System.out){ System.out.println(mensaje);}
    }
}

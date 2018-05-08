/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import chat.Process.Hilo;
import chat.Process.ConsoleInfo;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
/**
 *
 * @author vanya
 */
public class Chat {
    
    public static void main(String[] args) {
        HashMap<Hilo, Integer> hilos = new HashMap();
        File file = new File(
                System.getProperty("user.dir") + "\\mensajesGrupos.json"
        );
        Thread hilo;
        Socket client;
        int cont = 0;
        
        try {
            ServerSocket socket = new ServerSocket(4567);
            ConsoleInfo.especial("Esperando conexiones");
            while(true){
                client = socket.accept();
                hilo = new Thread(new Hilo(cont, client, hilos,file));
                hilo.start();
                ConsoleInfo.especial("Hilo ["+cont+"] creado");
                cont++;
            }
            
        } catch (IOException ex) {
            ConsoleInfo.error(cont,"Creacion de socket","Socket no creado");
        }
        
    }
}

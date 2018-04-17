/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import chat.Procesos.Hilo;
import chat.Procesos.ConsoleInfo;
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
        HashMap<Integer, Socket> socketPerUsuario = new HashMap();
        Thread hilo;
        Socket client;
        int cont = 0;
        
        try {
            ServerSocket socket = new ServerSocket(4567);
            ConsoleInfo.especial("Esperando conexiones");
            
            while(true){
                client = socket.accept();
                hilo = new Thread(new Hilo(cont, client, socketPerUsuario));
                hilo.start();
                ConsoleInfo.especial("Hilo ["+cont+"] creado");
                cont++;
            }
            
        } catch (IOException ex) {
            ConsoleInfo.error(cont,"Creacion de socket","Socket no creado");
        }
    }
}

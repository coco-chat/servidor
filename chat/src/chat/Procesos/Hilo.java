/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Procesos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class Hilo implements Runnable{
    private Socket client;
    private DataInputStream reader;
    private DataOutputStream writer;
    private int id;
    private HashMap<Hilo, Integer> hashTable;
    private ProcesoJson procesador;

    public Hilo() {
    }
    
    public Hilo(int id, Socket client, HashMap<Hilo, Integer> hashTable){
        this.client = client;
        this.id = id;
        this.hashTable = hashTable;
        procesador = new ProcesoJson(hashTable, this);
        try {
            reader = new DataInputStream(this.client.getInputStream());
            writer = new DataOutputStream(this.client.getOutputStream());
        } catch (IOException ex) {
            ConsoleInfo.error(this.id,"DataInput","No creado");
        }
        
    }
    
    @Override
    public void run() {
        String data;
        while(!this.client.isClosed()){
            try {
                data = reader.readUTF();
                ConsoleInfo.accion(
                        id,
                        "Recibido",
                        client.getRemoteSocketAddress().toString(),
                        data
                );
                data = procesador.procesar(data);
                ConsoleInfo.accion(
                        id, 
                        "Enviado", 
                        client.getRemoteSocketAddress().toString(), 
                        data
                );
                writer.writeUTF(data);
            } catch (IOException ex){
                if(hashTable.get(this)!=null){
                    procesador.logout();
                }
                ConsoleInfo.error(this.id,"Leer","Conexión Finalizada");
                break;
            }
        }
    }
    
    public void closeSocket(){
        try {
            this.client.close();
        } catch (IOException ex) {
            ConsoleInfo.error(this.id, "Cerrar socket", "Socket no cerrado");
        }
    }
    
    public void enviarMensaje(String mensaje){
        String ip = client.getRemoteSocketAddress().toString();
        String[] dir;
        Socket cliente;
        DataOutputStream mandar;
        try {
            dir = ip.split(":");
            dir[0] = dir[0].replace("/", "");
            cliente = new Socket(dir[0], 7654);
            mandar = new DataOutputStream(cliente.getOutputStream());
            mandar.writeUTF(mensaje);
            
            ConsoleInfo.accion(
                id, 
                "Enviado", 
                ip, 
                mensaje
            );
            
            mandar.close();
            cliente.close();
        } catch (IOException ex) {
            ConsoleInfo.error(this.id,"Enviar datos","Mensaje no enviado");
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Process;

import chat.Models.Usuario;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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
    private File file;

    public Hilo() {
    }
    
    public Hilo(int id, Socket client, HashMap<Hilo, Integer> hashTable, File file){
        this.client = client;
        this.id = id;
        this.hashTable = hashTable;
        this.file = file;
        procesador = new ProcesoJson(hashTable, this, file);
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
                writer.writeUTF(data);
                ConsoleInfo.accion(
                        id, 
                        "Enviado", 
                        client.getRemoteSocketAddress().toString(), 
                        data
                );
            } catch (IOException ex){
                if(hashTable.get(this)!=null){
                    procesador.logout();
                }
                ConsoleInfo.error(this.id,"Leer","Conexión Finalizada");
                break;
            }
        }
        ConsoleInfo.especial("[H"+id+"]Hilo cerrado");
    }
    
    public void closeSocket(){
        try {
            this.client.close();
        } catch (IOException ex) {
            ConsoleInfo.error(this.id, "Cerrar socket", "Socket no cerrado");
        }
    }
    
    public int enviarMensaje(String mensaje){
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
            return 0;
        } catch (IOException ex) {
            ConsoleInfo.error(this.id,"Enviar datos","Mensaje no enviado");
            return -1;
        }
    }
    
    public void checkConnectUsuario(String contenido){
        enviarMensaje(procesador.amigosCheck(contenido));
    }
}

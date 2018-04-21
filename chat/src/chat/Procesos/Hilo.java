/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Procesos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
                        "Recivido",
                        client.getLocalAddress().toString(),
                        data
                );
                data = procesador.procesar(data);
                ConsoleInfo.accion(
                        id, 
                        "enviado", 
                        client.getLocalAddress().toString(), 
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
        try {
            ConsoleInfo.accion(
                id, 
                "enviado", 
                client.getLocalAddress().toString(), 
                mensaje
            );
            this.writer.writeUTF(mensaje);
        } catch (IOException ex) {
            ConsoleInfo.error(this.id,"Enviar datos","Mensaje no enviado");
        }
    }
    
}

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

/**
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class Hilo implements Runnable{
    private Socket client;
    private DataInputStream reader;
    private DataOutputStream writer;
    private int id;
    private HashMap<Integer, Socket> hashTable;
    private final ProcesoJson procesador = new ProcesoJson();
    
    public Hilo(int id, Socket client, HashMap<Integer, Socket> hashTable){
        this.client = client;
        this.id = id;
        this.hashTable = hashTable;
        
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
                writer.writeUTF(data);
                ConsoleInfo.accion(
                        id, 
                        "enviado", 
                        client.getLocalAddress().toString(), 
                        data
                );
            } catch (IOException ex){
                ConsoleInfo.error(this.id,"Leer","Conexión Finalizada");
                break;
            }
        }
    }
    
}

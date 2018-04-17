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
import chat.Modelos.Modelo_Comunicacion;
import chat.Modelos.Modelo_Comunicacion.MTypes;
import com.google.gson.Gson;

/**
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class Hilo implements Runnable{
    private Socket client;
    private DataInputStream reader;
    private DataOutputStream writer;
    private int id;
    HashMap<Integer, Socket> hashTable;
    
    
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
                final Gson gson = new Gson();
                Modelo_Comunicacion mensaje = new Modelo_Comunicacion();
                mensaje.setTipo(MTypes.ACK);
                mensaje.setContenido("hola");
                String result=gson.toJson(mensaje);
                System.out.println(result);
            } catch (IOException ex){
                ConsoleInfo.error(this.id,"Leer","Conexión Finalizada");
                break;
            }
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controladores;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emiliano
 */
public class ArchivosController {
    
    private File archivo;
    private DataOutputStream writer;
    private DataInputStream reader;
    
    public ArchivosController () {
        this.archivo = null;
        this.writer = null;
        this.reader = null;
    }
    
    public ArchivosController(String path) {
        archivo = new File(path);
        try {
            FileOutputStream x = new FileOutputStream(archivo, true); // Defino esto explicitamente porque de otra manera no se crea el archivo
            FileInputStream y = new FileInputStream(archivo);
            writer = new DataOutputStream(x);
            reader = new DataInputStream(y);
        } catch(Exception ex) {
            Logger.getLogger(ArchivosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean writeFile(String cad) {
        synchronized(writer) {
            try {
                writer.writeChars(cad);
                return true;
            } catch(IOException ex) {
                return false;
            }
        }
    }
    
    public ArrayList<String> readFile() {
        ArrayList<String> lista = new ArrayList<>();
        String linea;
        synchronized(reader) {
            try {
                while((linea = reader.readUTF()) != null) {
                    lista.add(linea);
                }
                return lista;
            } catch(IOException ex) {
                return null;
            }
        }
    }
    
    public boolean overwriteFile(ArrayList<String> data) {
        synchronized(writer) {
            try {
                writer = new DataOutputStream(new FileOutputStream(archivo));
                for(String x : data) {
                    writer.writeChars(x);
                }
                writer = new DataOutputStream(new FileOutputStream(archivo, true));
                return true;
            } catch(Exception ex) {
                return false;
            }
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            // Defino esto explicitamente porque de otra manera no se crea el archivo
            FileOutputStream x = new FileOutputStream(archivo, true);
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
                writer.writeUTF(cad);
                return true;
            } catch(IOException ex) {
                return false;
            }
        }
    }
    
    public ArrayList<String> readFile() {
        synchronized(reader) {
            ArrayList<String> lista = new ArrayList<>();
            try {
                while(reader.available() > 0) {
                    lista.add(reader.readUTF());
                }
                return lista;
            } catch(IOException ex) {
                return null;
            }
        }
    }
    
    public boolean overwriteFile(List<String> data) {
        synchronized(writer) {
            try {
                writer = new DataOutputStream(new FileOutputStream(archivo));
                for(String cad : data) {
                    writer.writeUTF(cad);
                }
                writer = new DataOutputStream(new FileOutputStream(archivo, true));
                return true;
            } catch(Exception ex) {
                return false;
            }
        }
    }
    
    public boolean close() {
        try {
            writer.close();
            reader.close();
            return true;
        } catch(IOException ex) {
            return false;
        }
    }
    
}

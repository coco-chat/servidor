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

/**
 *
 * @author Emiliano
 */
public class ArchivosController {
    
    private File archivo;
    private DataOutputStream writter;
    private DataInputStream reader;
    
    public ArchivosController () {
        writter = null;
        reader = null;
        archivo = null;
    }
    
    public ArchivosController(String path) {
        this.archivo = new File(path);
        try {
            this.reader = new DataInputStream(new FileInputStream(archivo));
            this.writter = new DataOutputStream(new FileOutputStream(archivo, true));
        } catch(FileNotFoundException ex) {
            // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa
        }
    }
    
    public boolean writeFile(String cad) {
        synchronized(writter) {
            try {
                writter.writeChars(cad);
            } catch(IOException ex) {
                return false;
            }
        }
        return true;
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
        try {
            writter = new DataOutputStream(new FileOutputStream(archivo));
            synchronized(writter) {
                try {
                    for(String x : data) {
                        writter.writeChars(x);
                    }
                    return true;
                } catch(IOException ex) {
                    return false;
                }
            }
        } catch(FileNotFoundException ex) {
            return false;
        }
    }
    
    
}

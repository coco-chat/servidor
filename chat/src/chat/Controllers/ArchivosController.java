/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Emiliano
 */
public class ArchivosController {
    private final File archivo;
    
    
    public ArchivosController () {
        this.archivo = null;
    }
    
    /**
     * Crea un nuevo objeto file
     * @param path la dirección del archivo
     */
    public ArchivosController(String path) {
        archivo = new File(path);
    }
    
    /**
     * Asigna un objeto file determinado
     * @param archivo el objeto a asignar
     */
    public ArchivosController(File archivo){
        this.archivo=archivo;
    }
    
    /**
     * Añade una linea al archivo
     * @param cad la cadena a agregar
     * @return true si fue exitoso, false si hubo error
     */
    public boolean writeFile(String cad) {
        synchronized(archivo) {
            try {
                if(!archivo.isFile())archivo.createNewFile();
                FileWriter FWriter = new FileWriter(archivo,true);
                BufferedWriter writer = new BufferedWriter(FWriter);
                writer.append(cad+"\n");
                writer.close();
                return true;
            } catch(IOException ex) {
                return false;
            }
        }
    }
    
    public ArrayList<String> readFile() {
        synchronized(archivo) {
            String auxiliar;
            ArrayList<String> lista = new ArrayList<>();
            try {
                if(archivo.isFile()){
                    FileReader FReader = new FileReader(archivo);
                    BufferedReader reader = new BufferedReader(FReader);
                    auxiliar = reader.readLine();
                    while(auxiliar!=null) {
                        lista.add(auxiliar);
                        auxiliar=reader.readLine();
                    }
                    reader.close();
                }else archivo.createNewFile();
                return lista;
            } catch(IOException ex) {
                return null;
            }           
        }
    }
    
    public boolean overwriteFile(List<String> data) {
        synchronized(archivo) {
            try {
                FileWriter FWriter = new FileWriter(archivo);
                BufferedWriter writer = new BufferedWriter(FWriter);
                for(String cad : data) {
                    cad+="\n";
                    writer.write(cad);
                }
                writer.close();
                return true;
            } catch(Exception ex) {
                return false;
            }
        }
    }
}

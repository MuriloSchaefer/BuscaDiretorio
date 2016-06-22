/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscadiretorio;

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author murilo
 */
public class BuscaDiretorio {

    private static final int tam = 10; // tamanho do buffer
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Buffer buffer = new Buffer(tam);
        
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fc.showOpenDialog(null);
            File dir = null;
            if (result == JFileChooser.APPROVE_OPTION){
                dir = fc.getSelectedFile();
            }
            
            if(dir.exists()){
                Produtor produtor = new Produtor(dir, buffer);
            }
    }
    
}

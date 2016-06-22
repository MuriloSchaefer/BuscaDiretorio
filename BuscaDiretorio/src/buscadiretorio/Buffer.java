/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscadiretorio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author murilo
 */
public class Buffer {
    private List<File> arquivos;
    
    public Buffer(int n){
        this.arquivos = new ArrayList<>(n);
    }
    public void addArquivo(int index, File arq){
        this.arquivos.add(index, arq);
    }
    
    public void removeArquivo(int index, File arq){
        this.removeArquivo(index, arq);
    }
}

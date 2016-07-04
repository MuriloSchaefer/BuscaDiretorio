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
 * classe responsável por gerar o buffer
 * @author murilo
 */
public class Buffer {
    private List<File> arquivos;
    private int tamanho;
    
    public Buffer(int tamanho){
        this.arquivos = new ArrayList<>(tamanho);
        this.tamanho = tamanho;
    }
    public void addArquivo(int index, File arq){
        this.arquivos.add(index, arq);
    }
    
    public void removeArquivo(int index){
        arquivos.remove(index);
    }
   
    public void setArquivo(int index, File arq){
        arquivos.set(index, arq);
    }
    
    public boolean isFull(){
        if (arquivos.size() == tamanho){
            return true;
        } 
        return false;
    }
    
    
    public int getTamanho(){
        return this.tamanho;
    }
    
    public ArrayList<File> getArquivos(){
        return new ArrayList<File>(arquivos);
    }
}

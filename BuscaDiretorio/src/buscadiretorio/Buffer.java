package buscadiretorio;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * classe responsável por gerar o buffer
 * @author murilo
 */
public class Buffer {
    private List<File> arquivos;
    private int tamanho;
    private static Integer numProdutores = 0;
    private static Integer numConsumidores = 0;
    
    public Buffer(int tamanho){
        this.arquivos = new ArrayList<>(tamanho);
        for (int i = 0; i < tamanho; i++) {
            arquivos.add(null);
        }
        this.tamanho = tamanho;
        numProdutores = 0;
        numConsumidores =0;
    }
    
    public synchronized void addProdutor(){
        this.numProdutores = this.numProdutores+1;
    }
    public synchronized void removeProdutor(){
        this.numProdutores = this.numProdutores-1;
    }
    
    public synchronized void addConsumidor(){
        this.numConsumidores = this.numConsumidores+1;
    }
    public synchronized void removeConsumidor(){
        this.numConsumidores = this.numConsumidores-1;
    }
    
    public Integer getNumConsumidores(){
        return numConsumidores;
    }
    
    public Integer getNumProdutores(){
        return numProdutores;
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
    
    public int getTamanho(){
        return this.tamanho;
    }
    
    public ArrayList<File> getArquivos(){
        return new ArrayList<File>(arquivos);
    }
    
    @Override
    public String toString(){
        String saida = "[";
        for (File arquivo : arquivos) {
                if(arquivo != null)
                    saida += " "+ arquivo.getName();
                else
                    saida += " null";
        }
        saida += "]";
        return saida;
    }
}

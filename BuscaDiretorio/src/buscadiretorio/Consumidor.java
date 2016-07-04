/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscadiretorio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author murilo
 */
public class Consumidor implements Runnable{
    private Buffer buffer;
    private Semaphore empty;
    private Semaphore full;
    private Semaphore mutex;
    private static String palavra;
    private static int output=0;
    private static Integer numProdutores, numConsumidores;
    
    public Consumidor(Buffer buffer, Semaphore empty, Semaphore full, Semaphore mutex, String palavra, Integer numProdutores, Integer numConsumidores){
        System.out.println("consumidor se reproduziu");
        this.buffer = buffer;
        this.empty = empty;
        this.full = full;
        this.mutex = mutex;
        this.palavra = palavra;
        this.numProdutores = numProdutores;
        this.numConsumidores = numConsumidores+1;
    }
    
    public synchronized void wakeup(){
        notifyAll();
        System.out.println("notfyall cons");
    }
    public synchronized void sleep() throws InterruptedException{
        System.out.println("wait cons");
        wait();
    }
    public boolean pesquisa(File arquivo) throws FileNotFoundException{
        String resultado = null;
        Scanner scanner = new Scanner(arquivo);
        while(scanner.hasNext()){
            String leitura = scanner.next();
            if(palavra.equalsIgnoreCase(leitura)){
                return true;
            }
        }
        return false;
    }
    
    
    public File consome() throws InterruptedException{
        File arquivo;
        mutex.acquire();
        full.acquire();
        arquivo = buffer.getArquivos().get(output);
        buffer.setArquivo(output, null);
        output = (output+1)%buffer.getTamanho();
        empty.release();
        mutex.release();
        wakeup();
        return arquivo;
    }

    @Override
    public void run() {
        File arquivo;
        while(this.numProdutores>0){
            while(full.availablePermits() == 0){
                try {
                    sleep();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                arquivo = consome();
                if(pesquisa(arquivo)){
                    System.out.println("encontrei neste arquivo: "+ arquivo.toPath());
                } else {
                    System.out.println("não encontrei nada neste arquivo "+ arquivo.toPath());
                }
            } catch (InterruptedException | FileNotFoundException ex) {
                Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(numProdutores);
        //SINCRONIZAR
        this.numConsumidores -=1;
    }
    
}

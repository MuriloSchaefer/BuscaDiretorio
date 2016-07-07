/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscadiretorio;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
    private static List<File> encontrados;
    
    public Consumidor(Buffer buffer, Semaphore empty, Semaphore full, Semaphore mutex, String palavra, List<File> encontrados){
        //System.out.println("consumidor se reproduziu");
        this.buffer = buffer;
        this.empty = empty;
        this.full = full;
        this.mutex = mutex;
        this.palavra = palavra;
        this.numProdutores = numProdutores;
        this.numConsumidores = numConsumidores;
        this.encontrados = encontrados;
        buffer.addConsumidor();
    }
    
    public boolean pesquisa(File arquivo) throws FileNotFoundException{        
        Scanner scanner = new Scanner(arquivo);
        System.out.println(Thread.currentThread().getName() + " pesquisando " + arquivo.getPath());
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
        empty.acquire();
        mutex.acquire();
        /*System.out.println("mutex: "+ mutex.availablePermits());
        System.out.println("empty: "+ empty.availablePermits());
        System.out.println("full: "+ full.availablePermits());*/
        System.out.println(Thread.currentThread().getName() + "\toutput: "+ output);
        arquivo = buffer.getArquivos().get(output);
        buffer.setArquivo(output, null);
        System.out.println(Thread.currentThread().getName() + "\tbuffer: "+buffer.toString());
        output = (output+1)%buffer.getTamanho();
        /*System.out.println("mutex: "+ mutex.availablePermits());
        System.out.println("empty: "+ empty.availablePermits());
        System.out.println("full: "+ full.availablePermits());
        System.out.println("output: "+ output);*/
        mutex.release();
        full.release();
        return arquivo;
    }
    
    public synchronized File chamaConsome() throws InterruptedException{
        File arquivo = null;
        arquivo = consome();
        return arquivo;
    }

    @Override
    public void run() {
        File arquivo = null;
        while(buffer.getNumProdutores()>0 || empty.availablePermits() > 0){
            while(empty.availablePermits() == 0 && buffer.getNumProdutores()>0){
                try {
                    System.out.println(Thread.currentThread().getName() + "\tconsumidor dormiu ");
                    Thread.sleep(1); //adormece a thread
                } catch (InterruptedException ex) {
                    Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                //System.out.println(Thread.currentThread().getName() + "\tacordou");
            }
            try{
                //System.out.println(buffer.toString());
                if(empty.availablePermits() > 0)
                    arquivo = chamaConsome();
                if(arquivo != null){
                    if(pesquisa(arquivo)){
                        System.out.println(Thread.currentThread().getName() + "\tencontrei neste arquivo: "+ arquivo.toPath());
                        encontrados.add(arquivo);
                    } else {
                        System.out.println(Thread.currentThread().getName() + "\tnão encontrei nada neste arquivo "+ arquivo.toPath());
                    }
                }
            }catch(FileNotFoundException e){
                System.out.println(Thread.currentThread().getName() + "\tArquivo não encontrado");
            } catch (InterruptedException ex) {
                System.out.println(Thread.currentThread().getName() + "\tnão consumido");
            }
            
        }
        buffer.removeConsumidor(); 
        System.out.println(Thread.currentThread().getName() + "\tconsumidor se matou");
    }
    
}

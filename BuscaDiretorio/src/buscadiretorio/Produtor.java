/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscadiretorio;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * O produtor varre o diretorio e subdiretorios em busca de arquivos ainda não pesquisados
 * @author murilo, lyssa e ana
 */
public class Produtor implements Runnable {
    private List<File> pesquisados;
    private List<File> arquivos;
    private Buffer buffer;
    private Semaphore empty;
    private Semaphore full;
    private Semaphore mutex;
    private static int input = 0;
    private static Integer numProdutores, numConsumidores;
    
    
    public Produtor(File dir, Buffer buffer, Semaphore empty, Semaphore full, Semaphore mutex){
        File[] fileList = dir.listFiles();
        this.buffer = buffer;
        this.empty = empty;
        this.full = full;
        this.mutex = mutex;
        arquivos = new ArrayList();
        for(int i =0 ; i< fileList.length; i++){
            arquivos.add(fileList[i]);
        }
        this.numConsumidores = numConsumidores;
        this.numProdutores = numProdutores;
        buffer.addProdutor();
        
    }
    
    public void produzir(File arquivo) throws InterruptedException{
        full.acquire();
        mutex.acquire();
        buffer.setArquivo(input, arquivo);
        input = (input+1)%buffer.getTamanho();
        mutex.release();
        empty.release();
    }

    @Override
    public void run() {
        if (arquivos != null){
            for(int i = 0; i<arquivos.size(); i++){ //percorre lista de arquivos daquele diretorio
                
                File arquivo = arquivos.get(i);
                if(arquivo.isDirectory()){
                    Thread produtor = new Thread(new Produtor(arquivo, buffer, empty, full, mutex));
                    produtor.run();
                } else {
                    while(full.availablePermits() == 0){
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Produtor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } 
                    try {
                        produzir(arquivo);
                    } catch (InterruptedException ex) {
                        System.out.println(Thread.currentThread().getName() + "\tnao consegui produzir");
                    }
                }
            }
        }
        buffer.removeProdutor();
    }
    
}

package buscadiretorio;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileNotFoundException;
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
    private File dir;
    private Buffer buffer;
    private Semaphore empty;
    private Semaphore full;
    private Semaphore mutex;
    private static int input = 0;
    
    
    public Produtor(File dir, Buffer buffer, Semaphore empty, Semaphore full, Semaphore mutex){
        this.buffer = buffer;
        this.empty = empty;
        this.full = full;
        this.mutex = mutex;
        this.dir = dir;
        buffer.addProdutor();
        
    }
    
    public void percorreDiretorio(File diretorio) throws FileNotFoundException, InterruptedException{
        
        if(diretorio == null){
            return;
        }
        
        File[] fileList = diretorio.listFiles();    
        for (File arquivo : fileList) {
            if(arquivo.isDirectory()){
                percorreDiretorio(arquivo);
            }else{
                while(full.availablePermits() == 0){
                    Thread.sleep(1);
                } 
                try {
                    produzir(arquivo);
                } catch (InterruptedException ex) {
                    System.out.println(Thread.currentThread().getName() + "\tnao consegui produzir");
                }
            }
        }
        
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
        Thread.currentThread().setName("prod"+buffer.getNumProdutores().toString());
        try {
            percorreDiretorio(dir);
        } catch (FileNotFoundException | InterruptedException ex) {
            Logger.getLogger(Produtor.class.getName()).log(Level.SEVERE, null, ex);
        }
        buffer.removeProdutor();
    }
    
}

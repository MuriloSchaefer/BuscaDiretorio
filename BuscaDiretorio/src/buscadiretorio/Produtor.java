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
    
    
    public Produtor(File dir, Buffer buffer, Semaphore empty, Semaphore full, Semaphore mutex, Integer numProdutores, Integer numConsumidores){
        System.out.println("produtor se reproduziu");
        File[] fileList = dir.listFiles();
        this.buffer = buffer;
        this.empty = empty;
        this.full = full;
        this.mutex = mutex;
        arquivos = new ArrayList();
        for(int i =0 ; i< fileList.length; i++){
            arquivos.add(fileList[i]);
        }
        this.numProdutores = numProdutores+1;
        this.numConsumidores = numConsumidores;
        
    }
    
    public synchronized void wakeup(){
        notifyAll();
        System.out.println("notfyall prod");
    }
    public synchronized void sleep() throws InterruptedException{
        System.out.println("wait prod");
        wait();
        
    }
    
    public void produzir(File arquivo) throws InterruptedException{
        //System.out.println("Vou tentar coloca-lo agora, talvez.");
        mutex.acquire();
        //System.out.println("vou conseguir! :D");
        empty.acquire();
        //System.out.println("tofu");
        buffer.addArquivo(input, arquivo);
        input = (input+1)%buffer.getTamanho();
        //System.out.println("esta tudo no lugar.");
        full.release();
        mutex.release();
        wakeup();
        //System.out.println("pode voltar a trabalhar.");
    }

    @Override
    public void run() {
        //System.out.println("Run");
        ArrayList<File> files = (ArrayList<File>) arquivos;
        //System.out.println(Arrays.asList(files));
        if (arquivos != null){
            //System.out.println("Arquivo não nulo");
            for(int i = 0; i<arquivos.size(); i++){ //percorre lista de arquivos daquele diretorio
                
                //System.out.println("arq: "+ i);
                File arquivo = arquivos.get(i);
                //System.out.println(arquivo.toString());
                if(arquivo.isDirectory()){
                    //System.out.println("Novo produtor para a pastinha");
                    Thread produtor = new Thread(new Produtor(arquivo, buffer, empty, full, mutex, numProdutores, numConsumidores));
                    produtor.run();
                } else {
                    //System.out.println("não é uma pastinha, vamos colocar ele no buffer");
                    if(buffer.isFull()){
                        try {
                            //System.out.println("Buffer cheio, que dó");
                            sleep();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Produtor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else{
                        //System.out.println("tem um lugarzinho no buffer, vamos colocar la");
                        try {
                            produzir(arquivo);
                        } catch (InterruptedException ex) {
                            //System.out.println("Exception ao tentar parar a thread: "+ ex.getMessage());
                        }
                            
                    }
                }
            }
        }
        System.out.println(numProdutores);
        try {
            mutex.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Produtor.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.numProdutores -=1;
        mutex.release();
    }
    
}

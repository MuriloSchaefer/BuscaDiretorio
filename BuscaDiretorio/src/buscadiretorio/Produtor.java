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
        //System.out.println("produtor se reproduziu");
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
        //System.out.println("Vou tentar coloca-lo agora, talvez.");
        full.acquire();
        mutex.acquire();
        System.out.println("coloquei no buffer " + arquivo.getPath());
        //System.out.println("tofu");
        buffer.setArquivo(input, arquivo);
        input = (input+1)%buffer.getTamanho();
        //System.out.println("esta tudo no lugar.");
        mutex.release();
        empty.release();
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
                    Thread produtor = new Thread(new Produtor(arquivo, buffer, empty, full, mutex));
                    produtor.run();
                } else {
                    while(full.availablePermits() == 0){
                        //System.out.println("to cheio");
                        try {
                            //Thread.yield();
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
        /*System.out.println("Produtor morreu:");
        System.out.println("num Produtores: "+ buffer.getNumProdutores());
        System.out.println("num Consumidores: "+ buffer.getNumConsumidores());*/
        
    }
    
}

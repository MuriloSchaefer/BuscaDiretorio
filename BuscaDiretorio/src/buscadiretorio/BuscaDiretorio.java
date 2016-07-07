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
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFileChooser;
import java.util.concurrent.Semaphore;
/**
 *
 * @author murilo
 */


public class BuscaDiretorio {

    private static final int tam = 5; // tamanho do buffer
    private static Buffer buffer;
    private static Semaphore empty;
    private static Semaphore full;
    private static Semaphore mutex;
    private static String palavra;
    private static List<File> encontrados;
    /**
     * @param args the command line arguments
     */
    /*static class Reminder {
        Timer timer;
        public Reminder(int seconds) {
            timer = new Timer();
            timer.schedule(new RemindTask(), seconds*1000);
        }

        class RemindTask extends TimerTask {
            public void run() {
                int tam = buffer.getTamanho();
                if(full.availablePermits() > (tam*3)/4){
                      Thread consumidor = new Thread(new Consumidor(buffer, empty, full, mutex, palavra));
                }
                timer.cancel();//Terminate the timer thread
                Reminder reminder = new Reminder(1);
            }
        }
    }*/
    
    
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        buffer = new Buffer(tam);
        empty = new Semaphore(0);
        full = new Semaphore(tam);
        mutex = new Semaphore(1);
       // Reminder reminder = new Reminder(1);
        palavra = "produto";
        encontrados = new ArrayList<>();
        long tempoInicial = 0;
        
        
        //abre a busca do diretorio
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fc.showOpenDialog(null);
        File dir = null;
        if (result == JFileChooser.APPROVE_OPTION){
            dir = fc.getSelectedFile();
        }
        //se o dir for um endereco valido


        if(dir.exists()){
            //System.out.println("diretorio existe");
            
            tempoInicial = System.currentTimeMillis();
            Thread produtor = new Thread(new Produtor(dir, buffer, empty, full, mutex));
            produtor.start();
            for (int i = 0; i < 3; i++) {
                Thread consumidor = new Thread(new Consumidor(buffer, empty, full, mutex, palavra, encontrados));
                consumidor.start();
            }
            //produtor.join();
            //consumidor.join();
            System.out.println("Buscando...");
            while(buffer.getNumConsumidores() != 0){
                /*System.out.println("n Consumidores: "+ buffer.getNumConsumidores());
                System.out.println("n Produtores: "+ buffer.getNumProdutores());*/
                Thread.sleep(100);
            }
            System.out.println("Encontrados: ");
            String saida = "";
            for (File arquivo : encontrados) {
                saida += "\n\t"+ arquivo.getPath();
            }
            System.out.println(saida);
            /*while(true){
                ArrayList<File> files = buffer.getArquivos();
                System.out.println(Arrays.asList(files));
            }*/
        }
        long tempoExecucao = System.currentTimeMillis() - tempoInicial;
        System.out.println("Tempo de execucao: "+ tempoExecucao + " ms");
    }
    
}

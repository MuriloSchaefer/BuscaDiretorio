/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscadiretorio;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static Integer numProdutores = 0, numConsumidores = 0;
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
        empty = new Semaphore(tam);
        full = new Semaphore(0);
        mutex = new Semaphore(1);
       // Reminder reminder = new Reminder(1);
        palavra = "bololo";
        ArrayList<File> pesquisados = new ArrayList<>();
        
        
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
                System.out.println("diretorio existe");
                Thread produtor = new Thread(new Produtor(dir, buffer, empty, full, mutex, numProdutores, numConsumidores));
                Thread consumidor = new Thread(new Consumidor(buffer, empty, full, mutex, palavra,numProdutores, numConsumidores));
                produtor.start();
                consumidor.start();
                //produtor.join();
               // consumidor.join();
                /*while(true){
                    ArrayList<File> files = buffer.getArquivos();
                    System.out.println(Arrays.asList(files));
                }*/
            }
    }
    
}

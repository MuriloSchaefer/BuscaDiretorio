package buscadiretorio;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

    
    public static void main(String[] args) throws InterruptedException {

        buffer = new Buffer(tam);
        empty = new Semaphore(0);
        full = new Semaphore(tam);
        mutex = new Semaphore(1);
        palavra = "produto";
        encontrados = new ArrayList<>();
        long tempoInicial = 0;
        long tempoExecucao = 0;
        int nCons = 1;
        File dir = null;
        
        if(args.length>0){
            palavra = args[0];
            dir = new File(args[1]);
            if(args.length > 2)
            nCons = Integer.valueOf(args[2]);
        } else{
            //abre a busca do diretorio
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fc.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION){
                dir = fc.getSelectedFile();
            }
        }
        
        if(dir.exists()){
            tempoInicial = System.currentTimeMillis();
            Thread produtor = new Thread(new Produtor(dir, buffer, empty, full, mutex));
            produtor.start();
            
            for (int i = 0; i < nCons; i++) {
                Thread consumidor = new Thread(new Consumidor(buffer, empty, full, mutex, palavra, encontrados));
                consumidor.start();
            }
            
            while(buffer.getNumConsumidores() != 0){
                Thread.yield();
            }
            tempoExecucao = System.currentTimeMillis() - tempoInicial;
            /*System.out.println("Encontrados: ");
            String saida = "";
            for (File arquivo : encontrados) {
                saida += "\n\t"+ arquivo.getPath();
            }
            System.out.println(saida);*/
        }
        System.out.println("Qtd Threads consumidoras: "+ nCons + " Tempo execucao: "+ tempoExecucao+ " ms");
    }
    
}

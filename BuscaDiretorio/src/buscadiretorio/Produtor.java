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

/**
 * O produtor varre o diretorio e subdiretorios em busca de arquivos ainda não pesquisados
 * @author murilo, lyssa e ana
 */
public class Produtor implements Runnable {
    private List<File> pesquisados;
    private List<File> arquivos;
    private Buffer buffer;
    
    public Produtor(File dir, Buffer buffer){
        this.pesquisados = new ArrayList<>();
        this.arquivos = null;
        this.buffer = buffer;
        
        this.arquivos = new ArrayList<>(Arrays.asList(dir.listFiles())); //convete o vetor de arquivos em lista
    }

    @Override
    public void run() {
        if (arquivos != null){
            for(int i = 0; i<arquivos.size(); i++){ //percorre lista de arquivos daquele diretorio
                File arquivo = arquivos.get(i);
                if(arquivo.isDirectory()){
                    Produtor produtor = new Produtor(arquivo, buffer);
                }
            }
        }
    }
    
}

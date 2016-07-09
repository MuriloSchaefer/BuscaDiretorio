#!/bin/bash

caminho="/home/murilo/Área\ de\ Trabalho/Receitas"
palavra="produto"
nCons=5

echo $caminho

#echo "entre com a palavra: "
#read palavra
#echo "entre com o diretorio:"
#read caminho
for j in {1..5}
do
	java -jar BuscaDiretorioThread.jar $palavra /home/murilo/Área\ de\ Trabalho/2\ ano $nCons
done
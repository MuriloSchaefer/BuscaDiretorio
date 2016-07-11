#!/bin/bash --noprofile


MIN_CONS=1
MAX_CONS=15
STEP_CONS=3
TAM_BUF=5
DIR=/home/murilo/Área\ de\ Trabalho/2\ ano #deve-se alterar na linha 61 também, pois esta com erro, não sei o que é.
WORD="produto"
REPEAT=5

function PrintUsage() {
   echo "Argumentos: "
   echo -e "\t -h : ajuda"
   echo -e "\t -w WORD : palavra a ser procurada"
   echo -e "\t -d DIR : diretorio a ser procurado - não funciona"
   echo -e "\t -t VALUE: tamanho do buffer"
   echo -e "\t -m VALUE: valor minimo de consumidores"
   echo -e "\t -n VALUE: valor maximo de consumidores"
   echo -e "\t -s VALUE: valor do passo de consumidores"
   echo -e "\t -r VALUE: quantidade de repetições em cada iteração"
   exit 1
}
 
while getopts "hw:d:m:n:s:r:t:" OPTION
do
   case $OPTION in
      h) PrintUsage
         ;;
      w) WORD=$OPTARG
         ;;
      d) DIR=$OPTARG
         ;;
      m) MIN_CONS=$OPTARG
         ;;
      n) MAX_CONS=$OPTARG
         ;;
      s) STEP_CONS=$OPTARG
         ;;
      t) TAM_BUF=$OPTARG
         ;;
      r) REPEAT=$OPTARG
         ;;
      ?) PrintUsage
         ;;
   esac
done
shift $((OPTIND-1))



#exit 0
echo ""
echo "palavra desejada: " $WORD " diretorio: " $DIR
echo ""
for (( i=$MIN_CONS; i<=$MAX_CONS; i+=STEP_CONS ))
do
	for (( j=1; j<$REPEAT; j++ ))
	do
		#java -jar BuscaDiretorio/dist/BuscaDiretorioThread.jar $WORD $DIR $i
		java -jar BuscaDiretorio/dist/BuscaDiretorioThread.jar $WORD /home/murilo/Área\ de\ Trabalho/2\ ano $TAM_BUF $i
	done
	echo ""
done
#!/bin/bash --noprofile


MIN_CONS=1
MAX_CONS=15
STEP_CONS=3
DIR=/home/murilo/Área\ de\ Trabalho/2\ ano #deve-se alterar na linha 46 também, pois esta com erro, não sei o que é.
WORD="produto"
REPEAT=5

function PrintUsage() {
   echo "Argumentos: "
   echo -e "\t -h : ajuda"
   echo -e "\t -w WORD : palavra a ser procurada"
   echo -e "\t -d DIR : diretorio a ser procurado - não funciona"
   echo -e "\t -r VALUE: quantidade de repetições"
   exit 1
}
 
while getopts "hw:d:m:n:s:r:" OPTION
do
   case $OPTION in
      h) PrintUsage
         ;;
      w) WORD=$OPTARG
         ;;
      d) DIR=$OPTARG
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
for (( j=1; j<=$REPEAT; j++ ))
do
	#java -jar dist/BuscaDiretorioMonothread.jar $WORD $DIR $i
	java -jar dist/BuscaDiretorioMonothread.jar $WORD /home/murilo/Área\ de\ Trabalho/2\ ano
done
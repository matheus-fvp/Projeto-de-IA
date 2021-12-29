/**
 * SingleLinkMain
 * Para executar o programa basta abrir esta pasta no terminal/prompt de comando e compilar todos os arquivos 
 * .java usando o comando javac *.java e em seguida o comando java SingleLinkMain. A execução
 * do programa é demorada por causa do tamanho do conjunto de dados monkey
 * @author Matheus Fernando Vieira Pinto
 */
public class SingleLinkMain {
    public static void main(String[] args) {

        String src = "./datasets/c2ds1-2sp.txt";
        IndRandCorrigido ar;
        SingleLink sl;

        //para o conjunto de dados c2ds1-2sp
        sl = new SingleLink(src, 2, 5);
        sl.executar("./resultados/c2ds1-2sp");
        for(int i = 2; i <= 5; i++) {
            ar = new IndRandCorrigido("./datasets/c2ds1-2spReal.clu", "./resultados/c2ds1-2sp/clusters-" + i + ".clu");
            ar.calcular("./resultados/c2ds1-2sp/Indice-Rand-Corrigido-" + i + ".txt", i);
        }

        //para o conjunto de dados c2ds3-2g
        src = "./datasets/c2ds3-2g.txt";
        sl = new SingleLink(src, 2, 5);
        sl.executar("./resultados/c2ds3-2g");
        for(int i = 2; i <= 5; i++) {
            ar = new IndRandCorrigido("./datasets/c2ds3-2gReal.clu", "./resultados/c2ds3-2g/clusters-" + i + ".clu");
            ar.calcular("./resultados/c2ds3-2g/Indice-Rand-Corrigido-" + i + ".txt", i);
        }

        //para o conjunto de dados monkey
        src = "./datasets/monkey.txt";
        sl = new SingleLink(src, 5, 12);
        sl.executar("./resultados/monkey");
        for(int i = 5; i <= 12; i++) {
            ar = new IndRandCorrigido("./datasets/monkeyReal1.clu", "./resultados/monkey/clusters-" + i + ".clu");
            ar.calcular("./resultados/monkey/Indice-Rand-Corrigido-" + i + ".txt", i);
        }
    }
}
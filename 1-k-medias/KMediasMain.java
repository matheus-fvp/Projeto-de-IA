/**
 * * Para executar o programa basta abrir essa pasta no terminal e compilar todos os arquivos 
 * .java usando o comando javac *.java e em seguida o comando java KMediasMainkMain.
 * @author Matheus Fernando Vieira Pinto
 */
public class KMediasMain {
    public static void main(String[] args) {
        String src = "./datasets/c2ds1-2sp.txt";
        KMedias kM;
        IndRandCorrigido ar;

        //conjunto de dados c2ds1-2sp
        for(int i = 2; i <= 5; i++) {
            kM = new KMedias(src, i, 1000);
            kM.executaKMedias();
            kM.salvarNoArquivo(String.format("./resultados/c2ds1-2sp/c2ds1-2sp-%d.clu", i));
            kM.salvarNoArquivo2(String.format("./resultados/c2ds1-2sp/c2ds1-2sp-grafico-%d.csv", i)); //gera arquivos ,csv para facilitar a geração de graficos posteriormente
            ar = new IndRandCorrigido("./datasets/c2ds1-2spReal.clu", String.format("./resultados/c2ds1-2sp/c2ds1-2sp-%d.clu", i));
            ar.calcular(String.format("./resultados/c2ds1-2sp/indice-rand-%d-clusters", i), i);
        }

        //conjunto de dados c2ds3-2g
        src = "./datasets/c2ds3-2g.txt";
        for(int i = 2; i <= 5; i++) {
            kM = new KMedias(src, i, 1000);
            kM.executaKMedias();
            kM.salvarNoArquivo(String.format("./resultados/c2ds3-2g/c2ds3-2g-%d.clu", i));
            kM.salvarNoArquivo2(String.format("./resultados/c2ds3-2g/c2ds3-2g-grafico-%d.csv", i));
            ar = new IndRandCorrigido("./datasets/c2ds3-2gReal.clu", String.format("./resultados/c2ds3-2g/c2ds3-2g-%d.clu", i));
            ar.calcular(String.format("./resultados/c2ds3-2g/indice-rand-%d-clusters", i), i);
        }

        //para o conjunto de dados monkey
        src = "./datasets/monkey.txt";
        for(int i = 5; i <= 12; i++) {
            kM = new KMedias(src, i, 1000);
            kM.executaKMedias();
            kM.salvarNoArquivo(String.format("./resultados/monkey/monkey-%d.clu", i));
            kM.salvarNoArquivo2(String.format("./resultados/monkey/monkey-graficos-%d.csv", i));
            ar = new IndRandCorrigido("./datasets/monkeyReal1.clu", String.format("./resultados/monkey/monkey-%d.clu", i));
            ar.calcular(String.format("./resultados/monkey/indice-rand-%d-clusters", i), i);
        }

    }
}

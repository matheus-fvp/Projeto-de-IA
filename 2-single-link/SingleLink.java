import java.io.*;
import java.util.ArrayList;
import java.util.*;

/**
 * Descrição: Classe que representa o algoritmo de clusterização hierarquico Single link.
 * @author Matheus Fernando Vieira Pinto
 */
public class SingleLink {

    private ArrayList<Dado> dados = new ArrayList<>(); //atributo que representa o conjunto de dados que sera clusterizado.
    private ArrayList<ArrayList<Distancia>> matrizDist; //matriz de dissimilaridade necessaria para a execução do algoritmo.
    private int kMin; //quantidade minima de clusters em uma partição
    private int kMax; //quantidade maxima de clusters em uma partição
    
    /**
     * Construtor que inicializa os atributos kmin e Kmax, além de ler os dados do arquivo fornecido 
     * como parametro e armazenalos no atributo dados.
     * @param dados
     * @param kMin
     * @param kMax
     */
    public SingleLink(String dados, int kMin, int kMax) {
        this.kMin = kMin;
        this.kMax = kMax;
        leArquivo(dados); //chama o metodo que realiza a leitura dos dados do arquivo.
    }

    /**
     * Método responsavel por executar o algoritmo single-link
     * @param destino caminho do diretorio no qual as partições obtidas serão armazenadas
     */
    public void executar(String destino) {
        int qtdClusters; //variavel auxiliar para armazenar a quantidade de clusters
        while((qtdClusters = qtdClusters())  > this.kMin) {
            System.out.println(qtdClusters);
            //verifica se a partição esta dentro do intervalo de clusters solicitado pelo usuário
            if(qtdClusters <= kMax) {
                Collections.sort(this.dados, new DadosComparator()); //ordena os dados pelo atributo id para armazenar os dados no arquivo
                this.salvarNoArquivo(String.format(destino + "/clusters-%d.clu", qtdClusters)); //armazena os dados em um novo arquivo denominado clusters-%d.clu onde %d é a quantidade de clusters
                salvarNoArquivo2(String.format(destino + "/grafico-%d.csv", qtdClusters)); //salva no formato .csv para facilitar a geração de gráficos
            }

            double distMin= Double.POSITIVE_INFINITY; //considera a distancia minima inicial como um valor muito grande
            Distancia d; //referencia para um objeto do tipo distância para auxiliar na execução do algoritmo
            int index;
            this.calculaMatrizDistancia(); //realiza o calculo da matriz de dissimilaridade
            for(ArrayList<Distancia> dist: matrizDist) { 
                Collections.sort(dist); //a menor distancia sempre ficara na primeira posição de cada array list da matriz de dissimilaridade.
            }
            int i = 0; 
            index = i;
            for(ArrayList<Distancia> dist: matrizDist) {
                if(dist.get(0).getDistancia() < distMin) { //verifica qual é a menor distancia entre dois cluster utiilizando a matriz de dissimilaridade
                    distMin = dist.get(0).getDistancia(); 
                    index = i; //index armazena o indice da matriz no qual esta localizada a menor distância
                }
                i++;
            }
            d = matrizDist.get(index).get(0); //obtem a menor distância bem como suas informações
            int menorCluster = d.getCluster1(); //considera o menor cluster como o primeiro cluster de d
            int maiorCluster = d.getCluster2(); //considera o maior cluster como o segundo cluster de d
            if(d.getCluster2() < d.getCluster1()) { //caso o oposto seja verdadeiro acontece a troca
                menorCluster = d.getCluster2();
                maiorCluster = d.getCluster1();
            }
            ArrayList<Dado> cluster = recuperaCluster(maiorCluster); //obtem o maior cluster do conjunto de dados
            for(Dado objeto: cluster) {
                objeto.setCluster(menorCluster); //faz a união dos objetos do maior cluster com os do menor cluster, nesse caso todos os objetos do cluster maior passam a pertencer ao cluster menor
            }
            for(Dado objeto: this.dados) { //diminui em uma unidade a quantidade de clusters da partição atual
                if(objeto.getCluster() > maiorCluster)
                    objeto.setCluster(objeto.getCluster()-1);
            }
        } 
        //esse passo é necessário para armazenar a partição com a quantidade Kmin de clusters.
        Collections.sort(this.dados, new DadosComparator()); 
        this.salvarNoArquivo(String.format(destino + "/clusters-%d.clu", qtdClusters));
        salvarNoArquivo2(String.format(destino + "/grafico-%d.csv", qtdClusters));
    }

    /**
     * Metodo responsavel pelo calculo da distancia euclidiana entre dois objetos do tipo Dado
     * @param objeto1
     * @param objeto2
     * @return
     */
    private double distanciaEuclidiana(Dado objeto1, Dado objeto2) {
        return Math.sqrt(Math.pow(objeto1.getD1()-objeto2.getD1(), 2) + Math.pow(objeto1.getD2() - objeto2.getD2(), 2));
    }

    /**
     * Metodo responsavel pelo calculo da matriz de dissimilaridade que sera utilizada durante a execução
     * do algoritmo
     */
    private void calculaMatrizDistancia() {
        Collections.sort(this.dados); //ordena os dados primeiro para facilitar a recuperação dos clusters
        int qtdClusters = qtdClusters(); //obtem a quantidade de clusters que a partição atual possui
        matrizDist = new ArrayList<>(); //instancia a matriz de distancia para armazenar as distancias entre os clusters
        double distancia; //variavel auxiliar para armazenar a menor distancia

        //realiza o recalculo da matriz de distância
        for(int i = 0; i < qtdClusters-1; i++) { //inicia pelo cluster 0
            ArrayList<Dado> cluster1 = recuperaCluster(i); //reucpera da partição o cluster de numero i
            ArrayList<Distancia> linha = new ArrayList<>(); //cria um array list que armazena objetos do tipo Distancia para armazenar a menor distância entre o cluster i e os demais clusters
            for(int k = i+1; k < qtdClusters; k++) { //vai calcular a menor distância para cada cluster subsequente.
                distancia = Double.POSITIVE_INFINITY; //considera a distância minima inicial com um valor muito alto
                ArrayList<Dado> cluster2 = recuperaCluster(k); //obtem o cluster de indice k
                for(Dado obj1: cluster1) { //calcula a menor distância entre os pares de objetos de dois clusters diferentes.
                    for(Dado obj2: cluster2) {
                        double dist = distanciaEuclidiana(obj1, obj2);
                        if(dist < distancia)
                            distancia = dist; //armazena a menor distância
                    }
                }
                Distancia d = new Distancia(i, k, distancia); //armazena a menor distância entre dois clusters.
                linha.add(d); //simula a adição de um elemento em uma coluna da matriz
            }
            matrizDist.add(linha); //adiciona uma linha a matriz de dissimilaridade
        }
        
    }

    /**
     * Metodo que retorna a quantidade de clusters da partição dados
     * @return
     */
    private int qtdClusters() {
        if(this.dados.isEmpty())
            return 0; //retorna 0 se o atributo dados não possuir elementos
        int qtd = 0;
        //conta quantos clusters uma determinada partição possui
        for(Dado d: this.dados) {
            if(d.getCluster() > qtd)
                qtd++;
        }
        return (qtd + 1);
    }

    /**
     * metodo que retorna todos os dados de um determinada partição armazenados em um array List
     * @param id
     * @return
     */
    private ArrayList<Dado> recuperaCluster(int id) {
        if(this.dados.isEmpty())
            return null; //se não possuir elementos não ha clusters
        ArrayList<Dado> cluster = new ArrayList<>(); //array list que vai armazenar os objetos pertencentes a um determinado cluster
        boolean stop = false; //variavel auxiliar para interromper o loop cao todos os objetos de um determinado cluster sejam obtido
        for(int i = 0; i < this.dados.size() && !stop; i++) {
            Dado d = this.dados.get(i);
            if(d.getCluster() == id) {
                int k = i;
                //como o array List de dados está ordenado pelo atributo cluster e so obter todos os objetos em sequencia apos encontrar a primeira ocorrencia de um objeto de um determinado cluster e parar quando encontrar um objeto que pertence a outro cluster
                while(k < this.dados.size() && id == (d = this.dados.get(k)).getCluster()) {
                    cluster.add(d);
                    k++;
                }
                stop = true;
            }
        }

        return cluster;
    }

    /**
     * Metodo que le os dados de um arquivo e os armazena em uma estrutura do tipo array list que é representada pelo atributo dados
     * @param filename
     */
    private void leArquivo(String filename) {
        File dados = new File(filename);
        try {
            BufferedReader br = new BufferedReader(new FileReader(dados));
            boolean aux = false;
            String objeto;
            int i = 0;
            while((objeto = br.readLine()) != null) {
                if(aux) {
                    objeto = objeto.trim(); //remove os espaços
                    StringTokenizer atributos = new StringTokenizer(objeto);
                    this.dados.add(new Dado(atributos.nextToken(), Double.parseDouble(atributos.nextToken()), Double.parseDouble(atributos.nextToken()), i));
                    i++;
                }
                aux = true;
            }
            br.close();
        }catch(IOException e) {
            System.out.println("Erro ao abrir o arquivo!");
        }
    }

    /**
     * Descrição: Método que salva as informações obtidas em um determinado arquivo
     * @param name: nome do arquivo no qual as informações seram armazenadas.
     */
    private void salvarNoArquivo(String name) {
        File file = new File(name);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            for(Dado d: this.dados) {
                bw.write(String.format("%s %d", d.getId(), d.getCluster()));
                bw.newLine();
            }
            bw.close();
        }catch(IOException e) {
            System.err.println("Erro " + e);
        }
    }

    /**
     * método utilizado para gerar arquivos .csv para facilitar a contruçõa dos 
     * gráficos posteriormente. Ele é opcional
     * @param name
     */
    private void salvarNoArquivo2(String name) {
        File file = new File(name);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("id;d1;d2;cluster\n");
            for(Dado d: this.dados) {
                bw.write(d + "\n");
                //bw.newLine();
            }
            bw.close();
        }catch(IOException e) {
            System.err.println("Erro " + e);
        }
    }

    /**
     * Metodo que mostra a partição em um determinado momento
     */
    public void mostraDados() {
        for(Dado d: dados) {
            System.out.println(d);
        }
        
    }
}

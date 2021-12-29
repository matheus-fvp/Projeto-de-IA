import java.io.*;
import java.util.*;
/**
 * Descrição: Este código é referente ao desenvolvimento do
 * algoritmo de agrupamento k-médias para o trabalho da disciplina
 * de Inteligência artificial
 * @author Matheus Fernando Vieira Pinto
 */
public class KMedias {

    private int kClusters; //representa a quantidade de clusters que se deseja obter
    private int qtdInteracoes; //representa a quantidade de iterações que o algoritmo ira realizar
    private ArrayList<Dado> centroides = new ArrayList<>(); //armazena os centroides 
    private ArrayList<Dado> dados = new ArrayList<>(); //armazena a partição inicial fornecida pelo usuario

    /**
     * Construtor  que inicializa o k-médias ele recebe como parametro o nome do arquivo
     * onde os dados estão armazenados, a quantidade de clusters desejada e a quantidade de
     * iterações que o algoritmo ira realizar
     * @param dados
     * @param kClusters
     * @param qtdInteracoes
     */
    public KMedias(String dados, int kClusters, int qtdInteracoes) {
        this.kClusters = kClusters; 
        this.qtdInteracoes = qtdInteracoes;
        this.leArquivo(dados); //faz a leitura do arquivo
        this.inicializaCentroides(); //inicializa os centroides com base no conjunto de dados fornceido
    }

    /**
     * Descrição: Método que inicializa os k centroides de forma
     * aleatória.
     */
    private void inicializaCentroides() {
        TreeSet<Integer> aleatorios = new TreeSet<>();
        Random r = new Random();
        while(aleatorios.size() < this.kClusters) {
            aleatorios.add(r.nextInt(this.dados.size()));
        }
        int j = 0;
        for(Integer i: aleatorios) {
            this.centroides.add(this.dados.get(i.intValue()));
            this.centroides.get(j).setCluster(j);
            j++;
        }

    }

    /**
     * Método que faz o re-calculo dos centroides após uma iteração do algoritmo
     */
    private void recalculaCentroides() {
        for(Dado centroide: this.centroides) { //para cada centroide 
            double soma1 = 0;
            double soma2 = 0;
            int qtdObjetos = 0;
            for(Dado objeto: this.dados) { //para cada objeto do conjunto de dados
                if(objeto.getCluster() == centroide.getCluster()) { //verifica se o objeto pertence ao cluster do centroide
                    soma1 += objeto.getD1(); //aumula a soma do atributo d1 de todos os objetos que pertencem ao cluster do centroide analisado
                    soma2 += objeto.getD2(); //acumula a soma do atributo d2 de todos os objetos que pertencem ao cluster do centroide analisado
                    qtdObjetos++; //armazena a quantidade de objetos pertencentes aquele cluster
                }
            }
            centroide.setD1(soma1/qtdObjetos); //o atributo d1 do centroide é alterado com o valor da média do somatorio do atributo d1 de todos os objetos do cluster dividido pela quantidade de objetos
            centroide.setD2(soma2/qtdObjetos); //o atributo d2 do centroide é alterado com o valor da média do somatorio do atributo d2 de todos os objetos do cluster dividido pela quantidade de objetos
        }
    }

    /**
     * Método que realiza a execução do k-médias
     */
    public void executaKMedias() {
        boolean primeiraIteracao; //flag para verificar se é a primeira iteração do algoritmo
        double[] menoresDistancias = new double[this.dados.size()]; //cada posição i do vetor armazena a menor distancia do objeto i do conjunto de dados até um determinado cluster
        int[]  particaoResultante = new int[this.dados.size()]; //armazena a nova partição apos uma iterção do algoritmo

        for(int i = 0; i < this.qtdInteracoes; i++) {
            primeiraIteracao = true;
            for(Dado centroide: this.centroides) { //para cada centroide
                int indice = 0;
                for(Dado objeto: this.dados) { //para cada objeto do conjunto de dados
                    double distancia; //variavel auxiliar para armazenar a distância calculada
                    distancia = Math.sqrt(Math.pow(objeto.getD1() - centroide.getD1(), 2)+Math.pow(objeto.getD2() - centroide.getD2(), 2)); //obtem de uma determinado objeto em relação ao centroide
                    if(primeiraIteracao) { //na primeira iteração
                        menoresDistancias[indice] = distancia; //a menor distância do objeto é em relação ao primeiro cluster
                        particaoResultante[indice] = centroide.getCluster(); //o objeto passa a pertencer ao cluster do centroide
                    }
                    else if(distancia < menoresDistancias[indice]) { //verifica se a distância de um determinado centroide até o objeto é menor que a distancia atual armazenada 
                        menoresDistancias[indice] = distancia; //o objeto passa apertencer ao cluster do centroide com menor distância
                        particaoResultante[indice] = centroide.getCluster(); //o objeto passa a pertencer ao centroide do cluster
                    }
                    indice++;
                }
                primeiraIteracao = false;
            }
            for(int k = 0; k < particaoResultante.length; k++) { //reorganiza a partição 
                this.dados.get(k).setCluster(particaoResultante[k]);
            }
            this.recalculaCentroides(); //refaz o calculo do centroide
        }
    }

    /**
     * Descrição: Método para obter os dados do arquivo fornecido como
     * parâmetro.
     * @param name
     */
    private void leArquivo(String name) {
        File dados = new File(name);
        try {
            BufferedReader br = new BufferedReader(new FileReader(dados));
            boolean aux = false;
            String objeto;
            while((objeto = br.readLine()) != null) {
                if(aux) {
                    objeto = objeto.trim(); //remove os espaços
                    StringTokenizer atributos = new StringTokenizer(objeto);
                    this.dados.add(new Dado(atributos.nextToken(), Double.parseDouble(atributos.nextToken()), Double.parseDouble(atributos.nextToken())));
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
    public void salvarNoArquivo(String name) {
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
     * Metodo utilizado para gerar arquivos .csv para facilitar a geração de gráficos
     * @param name
     */
    public void salvarNoArquivo2(String name) {
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

    public void mostraDados() {
        for(Dado d: dados) {
            System.out.println(d);
        }
        System.out.println("Centroides:");
        for(Dado d: centroides) {
            System.out.println(d);
        }
    }

}
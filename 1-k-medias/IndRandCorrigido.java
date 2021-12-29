import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
/**
 * Descrição: Classe que representa o algoritmo responsavel por calcular o indice rand
 * corrigido entre duas partições
 * @author Matheus Fernando Vieira Pinto
 */

public class IndRandCorrigido {
    ArrayList<Dado> particao1 = new ArrayList<>(); //representa a primeira partição
    ArrayList<Dado> particao2 = new ArrayList<>(); //representa a segunda partição

    /**
     * @param file1 arquivo onde esta localizada a primeira partição
     * @param file2 arquivo onde esta localizada a segunda partição
     */
    public IndRandCorrigido(String file1, String file2) {
        this.lerDados(file1, file2); //faz a leitura dos arquivos e construção das partições
        this.verificaQtdDeClusters(particao1); 
        this.verificaQtdDeClusters(particao2);
    }

    private double calculaIRA() {
        int p1 = this.verificaQtdDeClusters(particao1); //qtd de clusters da primeira partição
        int p2 = this.verificaQtdDeClusters(particao2); //qtd de cluster da segunda partição
        if(p1 != 0 && p2 != 0) { //verifica se as partições possuem elementos
            int matriz[][] = new int[p1][p2]; //representa a tabela de contigencia
            int[] a = new int[p1]; //cada posição do vetor vai armazenar a soma dos elementos de uma determinada linha da matriz 
            int[] b = new int[p2]; //cada posição do vetor vai armazenar a soma dos elementos de cada coluna da matriz
            int soma; //variavel auxiliar para armazenar a soma.
            for(int i = 0; i < p1; i++) { //para cada cluster da partição 1
                ArrayList<Dado> d1 = this.retornaCluster(this.particao1, i); //obtem rodos os elementos de um determindado cluster
                soma = 0;
                for(int j = 0; j < p2; j++) { //para cada cluster da segunda partição
                    ArrayList<Dado> d2 = this.retornaCluster(this.particao2, j); //obtem todos os elementos de um determinado cluster
                    for(Dado part1: d1) { 
                        boolean stop = false;
                        for(int k = 0; k < d2.size() && !stop; k++) {
                            if(part1.getId().equals(d2.get(k).getId())) { //verifica se o elemento é comum a ambas as partições
                                matriz[i][j]++;
                                stop = true;
                            }
                        }
                    }
                    soma += matriz[i][j];
                }
                a[i] = soma; //adiciona a soma de todos os elementos de uma determinada linha da tabela de contigencia
            }
            //realiza a soma dos elementos de cada coluna da tabela de contigencia
            for(int j = 0; j < p2; j++) {
                soma = 0;
                for(int i = 0; i < p1; i++) {
                    soma += matriz[i][j];
                }
                b[j] = soma; //adiciona a soma dos elementos de uma determinada coluna em uma posiçao do vetor b
            }

            //faz o calculo da formula disponibilizada em: https://en.wikipedia.org/wiki/Rand_index
            long comNij = 0;
            long ai = 0;
            long bj = 0;
            for(int i = 0; i < p1; i++)
                for(int j = 0; j < p2; j++)
                    comNij += combinacao(matriz[i][j]);

            for(int i = 0; i < a.length; i++)
                ai += combinacao(a[i]);
            for(int i = 0; i < b.length; i++)
                bj += combinacao(b[i]);
            
            double expressao1 = comNij - (ai * bj) / this.combinacao(this.particao1.size());
            double expressao2 = (ai + bj) / 2 - (ai * bj) / this.combinacao(this.particao1.size()); 
            return expressao1/expressao2;
        }

        return -100;


    }

    /**
     * metodo que calcula uma combinação de acordo com a formula do AR
     * @param n
     * @return
     */
    private long combinacao(int n) {
        long combinacao = 0;
        int aux = n - 2; 

        combinacao = ((aux + 1) * (aux + 2)) / 2;
        return combinacao;

    }

    /**
     * metodo que obtem um determinado cluster de uma partição
     * @param particao
     * @param numCluster
     * @return
     */
    private ArrayList<Dado> retornaCluster(ArrayList<Dado> particao, int numCluster) {
        ArrayList<Dado> cluster = new ArrayList<>(); //vai armazenar todos os objetos de um determinado cluster
            for(Dado d: particao) {
                if(d.getCluster() == numCluster)
                    cluster.add(d);
            }
        return cluster;
    }

    /**
     * Método que realiza a leitura de ambas as partições a serem analisadas
     * @param arquivo1 arquivo onde a primeira partição esta localizada
     * @param arquivo2 arquivo onde a segunda partição esta localizada
     */
    private void lerDados(String arquivo1, String arquivo2) {
        File file1 = new File(arquivo1);
        File file2 = new File(arquivo2);
        try {
            BufferedReader br1 = new BufferedReader(new FileReader(file1));
            BufferedReader br2 = new BufferedReader(new FileReader(file2));

            this.lerDadosAux(br1, this.particao1);
            this.lerDadosAux(br2, this.particao2);

            br1.close();
            br2.close();
        }catch(IOException e) {
            System.err.println("Erro: " + e);
        }catch(Exception e) {
            System.err.println("Erro: " + e);
        }
    }

    /**
     * Método auxiliar para fazer a leitura de um determinado conjunto de dados de um
     * arquivo e armazenalos em uma estrutura do tipo arrayList
     * @param br
     * @param particao
     * @throws Exception
     */
    private void lerDadosAux(BufferedReader br, ArrayList<Dado> particao) throws Exception {
        String dados;
            while((dados = br.readLine()) != null) {
                dados = dados.trim();
                StringTokenizer atributos = new StringTokenizer(dados);
                particao.add(new Dado(atributos.nextToken(), Integer.parseInt(atributos.nextToken())));
            }
    }

    /**
     * metodo que verifica a quantidade de clusters em uma determinada partição
     * @param particao
     * @return
     */
    private int verificaQtdDeClusters(ArrayList<Dado> particao) { 
        if(particao.isEmpty())
            return 0;

        int qtdCluster = 0; 
        for(Dado d: particao) {
            if(d.getCluster() > qtdCluster) {
                qtdCluster = d.getCluster();
            }
        }
        return qtdCluster + 1;
    }

    public void mostraDados() {
        System.out.println("Dados da 1 partição: ");
        for(Dado d: this.particao1) {
            System.out.println(d.getId() + " " + d.getCluster());
        }
        System.out.println("Dados da 2 partição: ");
        for(Dado d: this.particao2) {
            System.out.println(d.getId() + " " + d.getCluster());
        }
    }

    /**
     * Metodo que chama os metodos necessarios para calcular e armazenar o AR em um arquivo
     * @param name arquivo onde o resultado sera armazenado
     * @param cluster qtd de clusters das partição analisada
     */
    public void calcular(String name, int cluster) {
        File file = new File(name);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(String.format("Indice-Rand-Corrigido-%d = %.5f", cluster, this.calculaIRA()));
            bw.newLine();
            bw.close();
        }catch(IOException e) {
            System.err.println("Erro " + e);
        }
    }

}

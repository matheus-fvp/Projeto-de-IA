/**
 * Descrição: Classe que representa um objeto de um conjunto de dados,
 * para esse trabalho o bjeto possui uma idetificação, duas variaveis
 * reais e o numero do cluster ao qual ele pertence.
 * @author Matheus Fernando Vieira Pinto
 */

public class Dado implements Comparable<Dado> {
    private String id; //atributo que armazena a identificação do dado
    private double d1; //atributo que representa a primeira variavel do dado
    private double d2; //atributo que representa o segunda variável do dada
    private int cluster; //atributo que representa o número do cluster ao qual o dado pertence

    /**
     * Construtor que inicializa os atributos do dado de acordo com os parametros fornecidos. Além disso
     * ele inicializa o atributo cluster com um valor padrão -1.
     * @param id
     * @param d1
     * @param d2
     */
    public Dado(String id, double d1, double d2) {
        this.id = id;
        this.d1 = d1;
        this.d2 = d2;
        this.cluster = -1; //não pertence a nenhum cluster ainda
    }

    /**
     * Construtor que inicializa os atributos de um determinado dado com os valores fornecidos como 
     * parametro.
     * @param id
     * @param d1
     * @param d2
     * @param cluster
     */
    public Dado(String id, double d1, double d2, int cluster) {
        this.id = id;
        this.d1 = d1;
        this.d2 = d2;
        this.cluster = cluster;
    }

    /**
     * Construtor que inicializa apenas os atributos id e cluster com os valores fornecidos como
     * parametro
     * @param id
     * @param cluster
     */
    public Dado(String id, int cluster) {
        this.id = id;
        this.cluster = cluster;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    public double getD1() {
        return d1;
    }

    public void setD1(double d1) {
        this.d1 = d1;
    }

    public double getD2() {
        return d2;
    }

    public void setD2(double d2) {
        this.d2 = d2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("%s;%.5f;%.5f;%d", this.id, this.d1, this.d2, this.cluster);
    }
    /**
     * Metodo utilizado para comparar os cluster de um determinado dado
     */
    @Override
    public int compareTo(Dado o) {
        return this.cluster - o.cluster;
    }
}

/**
 * Descrição: Classe que representa uma distancia entre dois cluster.
 * @author Matheus Fernando Vieira Pinto
 */

public class Distancia implements Comparable<Distancia>{
    private int cluster1; //armazena um dos cluster
    private int cluster2; //armazena o outro cluster
    private double distancia; //armazena a distância entre o cluster1 e o cluster2

    public Distancia(int cluster1, int cluster2, double distancia) {
        this.cluster1 = cluster1;
        this.cluster2 = cluster2;
        this.distancia = distancia;
    }

    public int getCluster1() {
        return cluster1;
    }

    public int getCluster2() {
        return cluster2;
    }

    public double getDistancia() {
        return distancia;
    }

    @Override
    public int compareTo(Distancia o) {
        if(this.distancia < o.distancia)
            return -1; //move o objeto para a esquerda do arraylist
        else if(this.distancia > o.distancia)
            return 1; //move o objeto para a direita do arraylist
        else 
            return 0; //mantém na mesma posição
    }
    
}

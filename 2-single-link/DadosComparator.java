import java.util.Comparator;

/**
 * Descrição: Classe utilizada para ordenar o conjunto de dados utilizando o atributo id
 * @author Matheus fernando Vieira Pinto
 */
public class DadosComparator implements Comparator<Dado> {

    @Override
    public int compare(Dado d1, Dado d2) {
        int result = d1.getId().compareTo(d2.getId());
        if(d1.getId().length() == d2.getId().length()) {
            if(result < 0)
                return -1;
            else if(result > 0)
                return 1;
            else
                return 0;
        }
        else if(d1.getId().length() < d2.getId().length())
            return -1;
        else
            return 1;
    }
    
}
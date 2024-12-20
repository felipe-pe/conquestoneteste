/**
 * Este script implementa uma abordagem otimizada para encontrar os três maiores números.
 * - Cria uma sublista inicial com os três primeiros elementos da lista original.
 * - Ordena a sublista em ordem decrescente e a mantém atualizada conforme percorre a lista.
 * - Utiliza trocas de posições (swap) para manter a ordem sem precisar ordenar repetidamente.
 * - Adequado para listas de tamanho médio, equilibrando simplicidade e desempenho.
 */



import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class SomaTresMaioresOtimizado {

    public static int somaTresMaiores(List<Integer> numeros) {
        if (numeros == null || numeros.isEmpty()) {
            return 0;
        }

        int tamanho = numeros.size();
        if (tamanho < 3) {
            int soma = 0;
            for (Integer n : numeros) {
                soma += n;
            }
            return soma;
        }

        // Cria uma cópia mutável dos primeiros três elementos
        List<Integer> sublista = new ArrayList<>(numeros.subList(0, 3));

        // Ordena a sublista em ordem decrescente
        sublista.sort(Collections.reverseOrder());

        // Encontra os três maiores
        for (int i = 3; i < tamanho; i++) {
            int n = numeros.get(i);
            if (n > sublista.get(2)) {
                sublista.set(2, n);
                if (sublista.get(2) > sublista.get(1)) {
                    Collections.swap(sublista, 1, 2);
                    if (sublista.get(1) > sublista.get(0)) {
                        Collections.swap(sublista, 0, 1);
                    }
                }
            }
        }

        return sublista.get(0) + sublista.get(1) + sublista.get(2);
    }
}

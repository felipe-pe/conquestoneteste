/**
 * Este script é uma variante manual da abordagem do array fixo.
 * - Utiliza três variáveis (`top1`, `top2`, `top3`) para armazenar os maiores valores.
 * - A lógica é semelhante à do baseline, mas com melhorias no controle de fluxo e clareza.
 * - Focado em simplicidade com boa eficiência, evitando estruturas complexas.
 * - Ideal para listas pequenas ou médias sem requisitos avançados de desempenho.
 */


import java.util.List;

public class SomaTresMaioresTop3Manual {

    /**
     * Calcula a soma dos três maiores números em uma lista de inteiros.
     * Caso a lista tenha menos de três elementos, soma todos os disponíveis.
     *
     * @param numeros Lista de números inteiros.
     * @return Soma dos três maiores números, ou soma de todos se houver menos de três.
     */
    public static int somaTresMaiores(List<Integer> numeros) {
        // Verifica se a lista é nula ou vazia
        if (numeros == null || numeros.isEmpty()) {
            return 0;
        }

        int tamanho = numeros.size();

        // Caso a lista tenha menos de três elementos, soma todos
        if (tamanho < 3) {
            return numeros.stream().mapToInt(Integer::intValue).sum();
        }

        // Inicializa os três maiores valores com o menor valor possível
        int top1 = Integer.MIN_VALUE;
        int top2 = Integer.MIN_VALUE;
        int top3 = Integer.MIN_VALUE;

        // Itera pela lista para encontrar os três maiores números
        for (Integer n : numeros) {
            if (n > top1) {
                top3 = top2;
                top2 = top1;
                top1 = n;
            } else if (n > top2) {
                top3 = top2;
                top2 = n;
            } else if (n > top3) {
                top3 = n;
            }
        }

        // Retorna a soma dos três maiores valores
        return top1 + top2 + top3;
    }
}

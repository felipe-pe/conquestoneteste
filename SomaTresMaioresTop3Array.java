/**
 * Este script utiliza um array fixo (`top3`) para armazenar os três maiores números.
 * - Verifica e atualiza os três valores diretamente durante a iteração.
 * - Mais eficiente para listas grandes, pois evita criar ou manipular sublistas.
 * - O uso de arrays torna a lógica mais estruturada e fácil de expandir para mais posições se necessário.
 */



import java.util.List;

public class SomaTresMaioresTop3Array {

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

        // Array para armazenar os três maiores números
        int[] top3 = {Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};

        // Itera pela lista para identificar os três maiores números
        for (Integer n : numeros) {
            if (n > top3[0]) {
                // n é maior que o maior valor atual, então ajusta os três maiores
                top3[2] = top3[1];
                top3[1] = top3[0];
                top3[0] = n;
            } else if (n > top3[1]) {
                // n é maior que o segundo maior valor
                top3[2] = top3[1];
                top3[1] = n;
            } else if (n > top3[2]) {
                // n é maior que o terceiro maior valor
                top3[2] = n;
            }
        }

        // Retorna a soma dos três maiores valores
        return top3[0] + top3[1] + top3[2];
    }
}

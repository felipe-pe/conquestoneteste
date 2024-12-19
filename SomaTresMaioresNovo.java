import java.util.List;

public class SomaTresMaioresNovo {

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

        // Se houver menos de três elementos, soma todos
        if (numeros.size() < 3) {
            int soma = 0;
            for (Integer n : numeros) {
                soma += n;
            }
            return soma;
        }

        // Caso haja 3 ou mais elementos
        int top1 = Integer.MIN_VALUE;
        int top2 = Integer.MIN_VALUE;
        int top3 = Integer.MIN_VALUE;

        // Encontrar os três maiores valores de forma simples e direta
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

        // Retorna a soma dos três maiores
        return top1 + top2 + top3;
    }

    public static void main(String[] args) {
        // Testes simples
        System.out.println(somaTresMaiores(List.of(10, 4, 3, 2, 5, 7))); // Esperado: 22 (10+7+5)
        System.out.println(somaTresMaiores(List.of(1, 2)));             // Esperado: 3  (1+2)
        System.out.println(somaTresMaiores(List.of()));                // Esperado: 0  (lista vazia)
    }
}

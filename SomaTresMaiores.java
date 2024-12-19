import java.util.List;

public class SomaTresMaiores {

    /**
     * Calcula a soma dos três maiores números em uma lista.
     *
     * @param numeros Lista de números inteiros.
     * @return A soma dos três maiores números ou de todos os números se a lista tiver menos de três elementos.
     */
    public static int somaTresMaiores(List<Integer> numeros) {
        // Verifica se a lista é nula ou vazia
        if (numeros == null || numeros.size() == 0) {
            return 0;
        }

        int tamanho = numeros.size();

        // Se houver menos de três elementos, retorna a soma de todos
        if (tamanho < 3) {
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

        // Encontrar os três maiores valores
        for (Integer n : numeros) {
            if (n > top1) {
                // n é maior que o atual top1, então realocamos top1->top2 e top2->top3
                top3 = top2;
                top2 = top1;
                top1 = n;
            } else if (n > top2) {
                // n não é maior que top1, mas é maior que top2
                top3 = top2;
                top2 = n;
            } else if (n > top3) {
                // n não é maior que top1 ou top2, mas é maior que top3
                top3 = n;
            }
        }

        // Retorna a soma dos três maiores
        return top1 + top2 + top3;
    }

    public static void main(String[] args) {
        // Testes básicos
        System.out.println("Teste 1: " + somaTresMaiores(List.of(10, 4, 3, 2, 5, 7))); // Esperado: 22 (10+7+5)
        System.out.println("Teste 2: " + somaTresMaiores(List.of(1, 2)));              // Esperado: 3  (1+2)
        System.out.println("Teste 3: " + somaTresMaiores(List.of()));                 // Esperado: 0  (lista vazia)
        System.out.println("Teste 4: " + somaTresMaiores(List.of(7, 7, 7, 7)));       // Esperado: 21 (7+7+7)
        System.out.println("Teste 5: " + somaTresMaiores(List.of(-10, -5, -1, 0, 3)));// Esperado: 2  (3+0+(-1))
        System.out.println("Teste 6: " + somaTresMaiores(List.of(-3, 2, 5, 1, -1, 6)));// Esperado: 13 (6+5+2)
    }
}

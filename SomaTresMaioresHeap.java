import java.util.List;
import java.util.PriorityQueue;

public class SomaTresMaioresHeap {

    /**
     * Calcula a soma dos três maiores números em uma lista de inteiros.
     * Usa uma Min-Heap para manter os três maiores números com eficiência.
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

        // Usando uma Min-Heap para manter os três maiores números
        PriorityQueue<Integer> heap = new PriorityQueue<>(3);

        for (Integer n : numeros) {
            heap.add(n);

            // Se o tamanho da heap ultrapassar 3, removemos o menor
            if (heap.size() > 3) {
                heap.poll();
            }
        }

        // Soma os três maiores números na heap
        return heap.stream().mapToInt(Integer::intValue).sum();
    }

    public static void main(String[] args) {
        // Teste com parâmetros reduzidos para warmup
        System.out.println("Teste 1: " + somaTresMaiores(List.of(10, 4, 3, 2, 5, 7)));  // Esperado: 22 (10+7+5)
        System.out.println("Teste 2: " + somaTresMaiores(List.of(1, 2)));              // Esperado: 3  (1+2)
        System.out.println("Teste 3: " + somaTresMaiores(List.of()));                 // Esperado: 0  (lista vazia)
        System.out.println("Teste 4: " + somaTresMaiores(List.of(7, 7, 7, 7)));       // Esperado: 21 (7+7+7)
        System.out.println("Teste 5: " + somaTresMaiores(List.of(-10, -5, -1, 0, 3))); // Esperado: 2 (3+0+(-1))
        System.out.println("Teste 6: " + somaTresMaiores(List.of(-3, 2, 5, 1, -1, 6))); // Esperado: 13 (6+5+2)
    }
}

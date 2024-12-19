import java.util.List;

public class SomaTresMaioresTest {

    public static void main(String[] args) {
        // Teste 1: Lista com mais de três números
        testarCaso("Mais de 3 números", List.of(10, 4, 3, 2, 5, 7), 22);

        // Teste 2: Lista com exatamente três números
        testarCaso("Exatamente 3 números", List.of(8, 3, 5), 16);

        // Teste 3: Lista com menos de três números
        testarCaso("Menos de 3 números", List.of(1, 2), 3);

        // Teste 4: Lista com apenas um número
        testarCaso("Apenas 1 número", List.of(7), 7);

        // Teste 5: Lista vazia
        testarCaso("Lista vazia", List.of(), 0);

        // Teste 6: Lista com números repetidos
        testarCaso("Números repetidos", List.of(7, 7, 7, 7), 21);

        // Teste 7: Lista com números negativos
        testarCaso("Números negativos", List.of(-10, -5, -1, 0, 3), 2);

        // Teste 8: Lista mista (positivos e negativos)
        testarCaso("Mista (positivos e negativos)", List.of(-3, 2, 5, 1, -1, 6), 13);
    }

    private static void testarCaso(String descricao, List<Integer> entrada, int esperado) {
        int resultado = SomaTresMaiores.somaTresMaiores(entrada);
        System.out.printf("Teste: %s | Entrada: %s | Esperado: %d | Resultado: %d | %s%n",
                descricao, entrada, esperado, resultado, (resultado == esperado ? "PASSOU" : "FALHOU"));
    }
}

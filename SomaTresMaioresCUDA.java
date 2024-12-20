/**
 * Este script utiliza uma abordagem baseada em CUDA para calcular os três maiores números.
 * - Usa uma função nativa (`findTop3`) que delega o cálculo para um código implementado em C++/CUDA.
 * - Focado em desempenho para listas muito grandes, aproveitando o paralelismo da GPU.
 * - Não contém lógica direta de cálculo em Java, pois a implementação é terceirizada para uma biblioteca nativa.
 * - Requer o arquivo de biblioteca compartilhada (.dll, .so) chamado `SomaTresMaioresCUDAImpl`.
 */


import java.util.List;

public class SomaTresMaioresCUDA {

    // Método nativo que chama a função findTop3 do código C++/CUDA
    public native int findTop3(List<Integer> numbers);

    // Carrega a biblioteca nativa
    static {
        System.loadLibrary("SomaTresMaioresCUDAImpl");
    }

    // Método para calcular a soma dos três maiores, usando a implementação nativa
    public int somaTresMaiores(List<Integer> numeros) {
        return findTop3(numeros);
    }
}
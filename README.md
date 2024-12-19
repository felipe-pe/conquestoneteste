# Projeto SomaTresMaiores

Este projeto contém duas implementações em Java para calcular a soma dos três maiores números de uma lista de inteiros, com uma delas aderindo estritamente ao escopo original do desafio e a outra sendo uma variação mais flexível.

## Objetivo

O objetivo principal é implementar um método que receba uma lista de inteiros (`List<Integer>`) e retorne a soma dos três maiores valores distintos. Caso a lista possua menos de três números distintos, a soma de todos os números será retornada. Se a lista estiver vazia ou for nula, o método retornará 0.

### Escopo Original

O escopo original do desafio, que é estritamente implementado na versão `Novo`, é o seguinte:

*   **Linguagem:** Java
*   **Método:** `int somaTresMaiores(List<Integer> numeros)`
*   **Regras:**
    *   Se a lista tiver menos de três números, o método deve retornar a soma de todos os números.
    *   Se a lista estiver vazia ou for nula, o método deve retornar 0.
    *   O método deve ser eficiente e legível.

### Variação (Extrapolação)

A versão `SomaTresMaiores.java` (sem o "Novo" no nome) é uma variação que **extrapola** o escopo original, permitindo **listas com mais de três elementos**, retornando a soma dos três maiores mesmo em listas maiores.

## Arquivos

O projeto é composto pelos seguintes arquivos:

*   **`SomaTresMaiores.java`:** Implementação que **extrapola** o escopo original, lidando com listas de qualquer tamanho e retornando a soma dos três maiores números.
*   **`SomaTresMaioresNovo.java`:** Implementação que **segue estritamente o escopo original** do desafio.
*   **`SomaTresMaioresTest.java`:** Classe de testes para a versão extrapolada (`SomaTresMaiores.java`), incluindo casos com listas vazias, listas com menos de três números e listas com mais de três números. (Usa execução manual de testes via `main`).
*   **`SomaTresMaioresNovoTest.java`:** Classe de testes para a versão que segue o escopo original (`SomaTresMaioresNovo.java`), testando com diversos cenários. (Usa JUnit 5 para execução dos testes).

## Exemplo de Uso

### Exemplo de Entrada e Saída

#### `SomaTresMaiores.java` (versão extrapolada)

| Entrada                 | Saída Esperada | Explicação                                       |
| ----------------------- | -------------- | ------------------------------------------------- |
| `[10, 4, 3, 2, 5, 7]` | `22`           | Soma dos três maiores: `10 + 7 + 5`             |
| `[1, 2]`              | `3`            | Soma dos únicos elementos: `1 + 2`               |
| `[]`                  | `0`            | Lista vazia                                      |

#### `SomaTresMaioresNovo.java` (versão que segue o escopo original)

| Entrada                 | Saída Esperada | Explicação                                       |
| ----------------------- | -------------- | ------------------------------------------------- |
| `[10, 4, 3, 2, 5, 7]` | `22`           | Soma dos três maiores: `10 + 7 + 5`             |
| `[1, 2]`              | `3`            | Soma dos únicos elementos: `1 + 2`               |
| `[]`                  | `0`            | Lista vazia                                      |
| `null`                | `0`            | Lista nula                                       |
| `[7, 7, 7, 7]`        | `21`           | Soma dos três maiores (repetidos): `7 + 7 + 7` |
| `[-10, -5, -1, 0, 3]`  | `2`            | Soma dos três maiores: `3 + 0 + (-1)`          |

## Como Executar

### Pré-requisitos

*   Ter o Java instalado (versão 11 ou superior, por exemplo).
*   (Recomendado) Ter o Maven ou o Gradle instalado para gerenciar as dependências do projeto (JUnit).

### Compilação

Para compilar os arquivos `.java` com o javac, abra um terminal na raiz do projeto e execute:

```bash
javac SomaTresMaiores.java SomaTresMaioresTest.java
javac SomaTresMaioresNovo.java SomaTresMaioresNovoTest.java

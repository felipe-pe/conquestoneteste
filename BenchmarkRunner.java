import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BenchmarkRunner {

    private static final String CONNECTION_STRING = "mongodb://localhost:27017"; 
    private static final String DATABASE_NAME = "benchmarkDatabase"; 
    private static final String COLLECTION_NAME = "benchmarkResults";

    // Ajustes para rodar mais rápido
    private static final int WARMUP_ITERATIONS = 100; 
    private static final int NUM_REPETITIONS = 1;      // menos repetições
    private static int numTestCases = 1000;            // menos casos de teste
    private static int maxListSize = 1000;             // listas menores
    private static int maxNumber = 1000;
    private static int batchSize = 500;                // tamanho dos lotes inalterado

    public static void main(String[] args) throws IOException {
        try (MongoClient mongoClient = getMongoClient()) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Limpa dados anteriores antes do início dos testes
            System.out.println("Limpando dados anteriores do MongoDB...");
            collection.deleteMany(new Document());

            // Gera todos os casos de teste uma única vez, com números negativos e positivos
            System.out.println("Gerando casos de teste...");
            List<List<Integer>> testSets = generateTestSets(numTestCases, maxListSize, maxNumber, false);

            // Lista de métodos
            List<BenchmarkMethod> methods = new ArrayList<>();
            methods.add(new BenchmarkMethod("SomaTresMaioresBaseline", SomaTresMaioresBaseline::somaTresMaiores));
            methods.add(new BenchmarkMethod("SomaTresMaioresNovoSimples", SomaTresMaioresNovoSimples::somaTresMaiores));
            methods.add(new BenchmarkMethod("SomaTresMaioresOtimizado", SomaTresMaioresOtimizado::somaTresMaiores));
            methods.add(new BenchmarkMethod("SomaTresMaioresTop3Manual", SomaTresMaioresTop3Manual::somaTresMaiores));
            methods.add(new BenchmarkMethod("SomaTresMaioresTop3Array", SomaTresMaioresTop3Array::somaTresMaiores));
            methods.add(new BenchmarkMethod("SomaTresMaioresCUDA", new SomaTresMaioresCUDA()::somaTresMaiores));
            methods.add(new BenchmarkMethod("SomaTresMaioresHeap", SomaTresMaioresHeap::somaTresMaiores));

            // Warm-up para todos os métodos
            for (BenchmarkMethod method : methods) {
                System.out.println("Iniciando warm-up para " + method.name + "...");
                warmup(method.function, WARMUP_ITERATIONS, maxListSize, maxNumber);
                System.out.println("Warm-up concluído para " + method.name);
            }

            // Executa cada método sequencialmente, sob as mesmas condições
            for (BenchmarkMethod method : methods) {
                System.out.println("Iniciando benchmark para o método: " + method.name);
                if (method.name.equals("SomaTresMaioresCUDA")) {
                    // Caso queira apenas positivos para CUDA, gere outro set:
                    System.out.println("Gerando casos de teste positivos para CUDA...");
                    List<List<Integer>> cudaTestSets = generateTestSets(numTestCases, maxListSize, maxNumber, true);
                    benchmarkInBatchesComNumerosPositivos(method.name, method.function, cudaTestSets, batchSize, collection);
                } else {
                    benchmarkInBatches(method.name, method.function, testSets, batchSize, collection);
                }
                System.out.println("Benchmark concluído para o método: " + method.name);
            }

            System.out.println("Todos os benchmarks foram concluídos.");
        }
    }

    /**
     * Executa o benchmark em lotes, usando o conjunto de testes pré-gerados.
     * Não gera novamente as listas.
     */
    private static void benchmarkInBatches(String methodName, SomaTresMaioresFunction function,
                                           List<List<Integer>> testSets, int batchSize,
                                           MongoCollection<Document> collection) throws IOException {
        String logFile = methodName + "_benchmark.log";
        try (FileWriter writer = new FileWriter(logFile, false)) { // false sobrescreve o arquivo
            writer.write("Repetition,Test Case,Num Elements,Result,Time (ns)\n");
            
            int totalTestCases = testSets.size();
            int testsPerRepetition = totalTestCases / NUM_REPETITIONS;

            int caseNumberGlobal = 1;
            for (int rep = 0; rep < NUM_REPETITIONS; rep++) {
                System.out.println("Método: " + methodName + " - Iniciando repetição " + (rep + 1) + "...");
                int startIndex = rep * testsPerRepetition;
                int endIndex = Math.min(startIndex + testsPerRepetition, totalTestCases);

                int totalBatches = testsPerRepetition / batchSize;
                for (int batch = 0; batch < totalBatches; batch++) {
                    // Log simples de progresso
                    if (batch % 10 == 0) { 
                        System.out.println("Método: " + methodName + " - Repetição " + (rep + 1) + ": processando batch " + (batch + 1) + " de " + totalBatches);
                    }
                    List<Document> documentsToInsert = new ArrayList<>();
                    for (int i = 0; i < batchSize; i++) {
                        int caseIndex = startIndex + batch * batchSize + i;
                        if (caseIndex >= endIndex) break;
                        List<Integer> testCase = testSets.get(caseIndex);

                        long startTime = System.nanoTime();
                        int result = function.apply(testCase);
                        long endTime = System.nanoTime();
                        long duration = endTime - startTime;

                        Document doc = new Document();
                        doc.append("repetition", rep + 1);
                        doc.append("methodName", methodName);
                        doc.append("caseNumber", caseNumberGlobal);
                        doc.append("listSize", testCase.size());
                        doc.append("testCase", testCase);
                        doc.append("result", result);
                        doc.append("time", duration);

                        documentsToInsert.add(doc);
                        writer.write(String.format("%d,%d,%d,%d,%d\n", rep + 1, caseNumberGlobal, testCase.size(), result, duration));
                        caseNumberGlobal++;
                    }
                    if (!documentsToInsert.isEmpty()) {
                        collection.insertMany(documentsToInsert);
                    }
                }
                System.out.println("Método: " + methodName + " - Repetição " + (rep + 1) + " concluída.");
            }
        }
        System.out.println("Benchmark for " + methodName + " logged to MongoDB and " + logFile);
    }

    /**
     * Versão para o CUDA com números positivos, usando também testSets pré-gerados (somente positivos).
     */
    private static void benchmarkInBatchesComNumerosPositivos(String methodName, SomaTresMaioresFunction function,
                                                              List<List<Integer>> testSets, int batchSize,
                                                              MongoCollection<Document> collection) throws IOException {
        String logFile = methodName + "_benchmark.log";
        try (FileWriter writer = new FileWriter(logFile, false)) {
            writer.write("Repetition,Test Case,Num Elements,Result,Time (ns)\n");

            int totalTestCases = testSets.size();
            int testsPerRepetition = totalTestCases / NUM_REPETITIONS;

            int caseNumberGlobal = 1;
            for (int rep = 0; rep < NUM_REPETITIONS; rep++) {
                System.out.println("Método: " + methodName + " - Iniciando repetição " + (rep + 1) + "...");
                int startIndex = rep * testsPerRepetition;
                int endIndex = Math.min(startIndex + testsPerRepetition, totalTestCases);

                int totalBatches = testsPerRepetition / batchSize;
                for (int batch = 0; batch < totalBatches; batch++) {
                    if (batch % 10 == 0) { 
                        System.out.println("Método: " + methodName + " - Repetição " + (rep + 1) + ": processando batch " + (batch + 1) + " de " + totalBatches);
                    }
                    List<Document> documentsToInsert = new ArrayList<>();
                    for (int i = 0; i < batchSize; i++) {
                        int caseIndex = startIndex + batch * batchSize + i;
                        if (caseIndex >= endIndex) break;
                        List<Integer> testCase = testSets.get(caseIndex);

                        long startTime = System.nanoTime();
                        int result = function.apply(testCase);
                        long endTime = System.nanoTime();
                        long duration = endTime - startTime;

                        Document doc = new Document();
                        doc.append("repetition", rep + 1);
                        doc.append("methodName", methodName);
                        doc.append("caseNumber", caseNumberGlobal);
                        doc.append("listSize", testCase.size());
                        doc.append("testCase", testCase);
                        doc.append("result", result);
                        doc.append("time", duration);

                        documentsToInsert.add(doc);
                        writer.write(String.format("%d,%d,%d,%d,%d\n", rep + 1, caseNumberGlobal, testCase.size(), result, duration));
                        caseNumberGlobal++;
                    }
                    if (!documentsToInsert.isEmpty()) {
                        collection.insertMany(documentsToInsert);
                    }
                }
                System.out.println("Método: " + methodName + " - Repetição " + (rep + 1) + " concluída.");
            }
        }
        System.out.println("Benchmark for " + methodName + " logged to MongoDB and " + logFile);
    }

    /**
     * Função para warm-up
     */
    private static void warmup(SomaTresMaioresFunction function, int iterations, int maxListSize, int maxNumber) {
        Random random = new Random();
        for (int i = 0; i < iterations; i++) {
            int listSize = random.nextInt(maxListSize + 1);
            List<Integer> testCase = new ArrayList<>();
            if (function instanceof SomaTresMaioresCUDA) {
                // Caso CUDA: somente positivos
                for (int j = 0; j < listSize; j++) {
                    testCase.add(random.nextInt(maxNumber));
                }
            } else {
                // Métodos comuns: números positivos e negativos
                for (int j = 0; j < listSize; j++) {
                    testCase.add(random.nextInt(2 * maxNumber) - maxNumber);
                }
            }
            function.apply(testCase); // executa sem medir tempo
        }
    }

    /**
     * Gera antecipadamente todos os casos de teste.
     * @param onlyPositive se true, gera somente números [0, maxNumber)
     *                     se false, gera entre [-maxNumber, maxNumber).
     */
    private static List<List<Integer>> generateTestSets(int numTestCases, int maxListSize, int maxNumber, boolean onlyPositive) {
        Random random = new Random();
        List<List<Integer>> testSets = new ArrayList<>(numTestCases);
        for (int i = 0; i < numTestCases; i++) {
            int listSize = random.nextInt(maxListSize + 1);
            List<Integer> testCase = new ArrayList<>(listSize);
            for (int j = 0; j < listSize; j++) {
                int val = onlyPositive ? random.nextInt(maxNumber) : random.nextInt(2 * maxNumber) - maxNumber;
                testCase.add(val);
            }
            testSets.add(testCase);
        }
        return testSets;
    }

    private static MongoClient getMongoClient() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                .serverApi(serverApi)
                .build();

        return MongoClients.create(settings);
    }

    @FunctionalInterface
    interface SomaTresMaioresFunction {
        int apply(List<Integer> numeros);
    }

    static class BenchmarkMethod {
        String name;
        SomaTresMaioresFunction function;

        public BenchmarkMethod(String name, SomaTresMaioresFunction function) {
            this.name = name;
            this.function = function;
        }
    }
}

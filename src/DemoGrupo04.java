import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// Demonstração completa do sistema de rastreio de encomendas
// Sessão 1: configura o grafo, cadastra encomendas e testa as rotas
// Sessão 2: reabre o banco do log e prova que o estado foi recuperado
public class DemoGrupo04 {

    public static void main(String[] args) throws IOException {
        Path arquivoLog = Path.of("rastreio-grupo04.log");
        Files.deleteIfExists(arquivoLog);

        System.out.println("=== Grupo 04 - Rastreio de Encomendas ===");

        primeiraSessao(arquivoLog);
        segundaSessao(arquivoLog);
        mostrarArquivoGerado(arquivoLog);
    }

    // configura os CDs e as rotas direcionadas no grafo
    // precisa ser chamado a cada sessão porque o grafo não é persistido
    private static void configurarGrafo(Grafo grafo) {
        grafo.adicionarVertice("PMW");
        grafo.adicionarVertice("GRU");
        grafo.adicionarVertice("BSB");
        grafo.adicionarVertice("CGH");
        grafo.adicionarVertice("VCP");
        grafo.adicionarVertice("CWB");

        grafo.adicionarAresta("PMW", "GRU");
        grafo.adicionarAresta("GRU", "CGH");
        grafo.adicionarAresta("GRU", "BSB");
        grafo.adicionarAresta("BSB", "CGH");
        grafo.adicionarAresta("VCP", "GRU");
        grafo.adicionarAresta("CWB", "VCP");
        grafo.adicionarAresta("CWB", "BSB");
    }

    private static void primeiraSessao(Path arquivoLog) {
        System.out.println("\n--- Sessao 1: configurando grafo e cadastrando encomendas ---");

        try (Rastreio banco = new Rastreio(arquivoLog)) {
            configurarGrafo(banco.getGrafo());

            banco.put("BR1234567XYZ", "PMW|CGH|EM_TRANSITO");
            banco.put("BR9876543ABC", "CWB|BSB|AGUARDANDO");
            banco.put("BR1111111AAA", "VCP|CGH|ENTREGUE");
            banco.put("BR2222222BBB", "GRU|BSB|EM_TRANSITO");
            banco.put("BR3333333CCC", "BSB|PMW|EM_TRANSITO"); // sem rota de volta

            System.out.println("Rota BR1234567XYZ: " + banco.rastrearRota("BR1234567XYZ"));
            System.out.println("Rota BR3333333CCC: " + banco.rastrearRota("BR3333333CCC"));

            banco.delete("BR1111111AAA");

            System.out.println("Tamanho ao fechar sessao 1: " + banco.tamanho());
            System.out.println("Chaves em ordem: " + banco.listarChavesEmOrdem());
        }
    }

    private static void segundaSessao(Path arquivoLog) {
        System.out.println("\n--- Sessao 2: reabrindo banco e verificando persistencia ---");

        try (Rastreio banco = new Rastreio(arquivoLog)) {
            configurarGrafo(banco.getGrafo());

            System.out.println("Tamanho recuperado: " + banco.tamanho());
            System.out.println("BR1234567XYZ -> " + banco.get("BR1234567XYZ"));
            System.out.println("BR9876543ABC -> " + banco.get("BR9876543ABC"));
            System.out.println("Chaves em ordem: " + banco.listarChavesEmOrdem());
            System.out.println("Rota BR1234567XYZ apos reabrir: " + banco.rastrearRota("BR1234567XYZ"));
        }
    }

    private static void mostrarArquivoGerado(Path arquivoLog) throws IOException {
        System.out.println("\n--- Conteudo do log ---");
        List<String> linhas = Files.readAllLines(arquivoLog, StandardCharsets.UTF_8);
        for (String linha : linhas) {
            System.out.println(linha);
        }
    }
}

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

// Grafo direcionado usando lista de adjacência
// cada vértice é um código de CD (centro de distribuição)
public class Grafo {

    // mapa onde a chave é o vértice e o valor é o conjunto de vizinhos diretos
    private final Map<String, Set<String>> adjacencia = new HashMap<>();

    // adiciona um vértice se ele ainda não existir (idempotente)
    public void adicionarVertice(String cd) {
        adjacencia.putIfAbsent(cd, new HashSet<>());
    }

    // adiciona aresta direcionada de origem para destino
    // cria os vértices automaticamente se não existirem
    public void adicionarAresta(String origem, String destino) {
        adicionarVertice(origem);
        adicionarVertice(destino);
        adjacencia.get(origem).add(destino);
    }

    // BFS para encontrar o caminho mais curto entre dois CDs
    // retorna a sequência de vértices do caminho ou lista vazia se não houver rota
    public List<String> buscarCaminho(String origem, String destino) {
        if (!adjacencia.containsKey(origem) || !adjacencia.containsKey(destino)) {
            return Collections.emptyList();
        }
        if (origem.equals(destino)) {
            return Collections.singletonList(origem);
        }

        // fila de vértices a visitar e mapa de predecessores para reconstruir o caminho
        Queue<String> fila = new LinkedList<>();
        Map<String, String> anterior = new HashMap<>();

        fila.add(origem);
        anterior.put(origem, null); // origem não tem predecessor

        while (!fila.isEmpty()) {
            String atual = fila.poll();
            for (String vizinho : adjacencia.getOrDefault(atual, Collections.emptySet())) {
                if (!anterior.containsKey(vizinho)) {
                    anterior.put(vizinho, atual);
                    if (vizinho.equals(destino)) {
                        return reconstruirCaminho(anterior, destino);
                    }
                    fila.add(vizinho);
                }
            }
        }

        return Collections.emptyList(); // destino não alcançável
    }

    // percorre o mapa de predecessores de trás pra frente para montar o caminho
    private List<String> reconstruirCaminho(Map<String, String> anterior, String destino) {
        List<String> caminho = new ArrayList<>();
        String atual = destino;
        while (atual != null) {
            caminho.add(0, atual);
            atual = anterior.get(atual);
        }
        return caminho;
    }
}

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

// Banco chave-valor para rastreio de encomendas
// herda toda a lógica de persistência do BancoChaveValor e adiciona o grafo de rotas
public class Rastreio implements SGBD {

    private final Map<String, String> memoria = new HashMap<>();
    private final NavigableMap<String, String> indiceOrdenado = new TreeMap<>();
    private final LogAppendOnly log;

    // grafo de rotas entre CDs, populado manualmente no Demo a cada execução
    private final Grafo grafo = new Grafo();

    public Rastreio(Path arquivoLog) {
        this.log = new LogAppendOnly(arquivoLog);
        reconstruirEstado();
    }

    // expõe o grafo para que o Demo possa configurar as rotas
    public Grafo getGrafo() {
        return grafo;
    }

    // busca a rota de uma encomenda no grafo
    // retorna lista vazia se a encomenda não existir ou não houver caminho
    public List<String> rastrearRota(String codigoEncomenda) {
        String valor = get(codigoEncomenda);
        if (valor == null) {
            return Collections.emptyList();
        }
        // valor no formato "ORIGEM|DESTINO|STATUS", pipe escapado no regex
        String[] partes = valor.split("\\|");
        String origem = partes[0];
        String destino = partes[1];
        return grafo.buscarCaminho(origem, destino);
    }

    @Override
    public synchronized void put(String chave, String valor) {
        validarChave(chave);
        Objects.requireNonNull(valor, "valor nao pode ser nulo");

        RegistroLog registro = new RegistroLog(TipoOperacao.PUT, chave, valor);
        log.registrar(registro);

        memoria.put(chave, valor);
        indiceOrdenado.put(chave, valor);
    }

    @Override
    public synchronized String get(String chave) {
        validarChave(chave);
        return memoria.get(chave);
    }

    @Override
    public synchronized void delete(String chave) {
        validarChave(chave);

        RegistroLog registro = new RegistroLog(TipoOperacao.DEL, chave, "");
        log.registrar(registro);

        memoria.remove(chave);
        indiceOrdenado.remove(chave);
    }

    @Override
    public synchronized List<String> listarChavesEmOrdem() {
        return new ArrayList<>(indiceOrdenado.keySet());
    }

    @Override
    public synchronized int tamanho() {
        return memoria.size();
    }

    @Override
    public synchronized void fechar() {
        log.close();
    }

    // relê o log do disco e reconstrói o estado em memória
    private void reconstruirEstado() {
        for (RegistroLog registro : log.lerTodos()) {
            aplicarEmMemoria(registro);
        }
    }

    private void aplicarEmMemoria(RegistroLog registro) {
        if (registro.getOperacao() == TipoOperacao.PUT) {
            memoria.put(registro.getChave(), registro.getValor());
            indiceOrdenado.put(registro.getChave(), registro.getValor());
            return;
        }
        memoria.remove(registro.getChave());
        indiceOrdenado.remove(registro.getChave());
    }

    private void validarChave(String chave) {
        Objects.requireNonNull(chave, "chave nao pode ser nula");
        if (chave.isBlank()) {
            throw new IllegalArgumentException("chave nao pode ser vazia");
        }
    }
}

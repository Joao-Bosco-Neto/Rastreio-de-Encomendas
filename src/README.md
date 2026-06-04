# Aula 15 — Demo de Banco de Dados Chave-Valor

Este diretório contém uma **implementação didática** de um banco de dados chave-valor em Java, preparada para a **Aula 15** da disciplina de Estrutura de Dados.

O objetivo não é construir um SGBD completo, mas demonstrar, de forma clara e executável em sala, os seguintes conceitos:

- **TAD** usando `interface` Java;
- operações básicas `put`, `get` e `delete`;
- uso de `HashMap` como estrutura principal em memória;
- uso de `TreeMap` como visão ordenada das chaves;
- persistência com **append-only log**;
- reconstrução do estado ao reiniciar o banco.

---

## 1. Estrutura dos arquivos

| Arquivo | Papel didático |
|---|---|
| `SGBD.java` | TAD do banco: define o contrato público |
| `TipoOperacao.java` | Enum com os tipos de operação persistidos |
| `RegistroLog.java` | Converte operação em linha de log e vice-versa |
| `LogAppendOnly.java` | Camada de persistência em arquivo |
| `BancoChaveValor.java` | Implementação concreta do TAD |
| `DemoAula15.java` | Programa principal para demonstração em sala |

---

## 2. O que o exemplo demonstra

Durante a execução, a demo:

1. cria um arquivo de log;
2. abre uma primeira sessão do banco;
3. grava, atualiza e remove dados;
4. fecha o banco;
5. reabre o mesmo arquivo de log;
6. reconstrói automaticamente o estado em memória;
7. exibe o conteúdo final recuperado;
8. imprime o conteúdo bruto do arquivo de log.

Assim, os alunos conseguem visualizar:

- a diferença entre **estado em memória** e **estado persistido**;
- por que o `HashMap` sozinho não basta;
- como o log funciona como **fonte da verdade**;
- como o banco reconstitui o estado após reinício.

---

## 3. Como compilar

No terminal, dentro deste diretório:

```text
javac *.java
```

Se a compilação ocorrer corretamente, serão gerados os arquivos `.class`.

---

## 4. Como executar

Ainda neste diretório, execute:

```text
java -cp . DemoAula15
```

---

## 5. Saída esperada

A saída deve seguir esta ideia:

```text
=== Aula 15 - Demo de Banco Chave-Valor ===
Arquivo de log: ...\aula15-demo.log

--- Sessao 1: gravando dados ---
cliente:001 -> Ana
cliente:002 -> Bruno
Chaves em ordem: [cliente:001, cliente:002, cliente:003]
Tamanho ao fechar a sessao 1: 2
Chaves finais da sessao 1: [cliente:001, cliente:002]

--- Sessao 2: reabrindo e recuperando do log ---
Tamanho recuperado: 2
cliente:001 -> Ana
cliente:002 -> Bruno Silva
cliente:003 -> null
Chaves em ordem apos reconstruir: [cliente:001, cliente:002]

--- Conteudo do log gerado ---
PUT|cliente:003|Carla
PUT|cliente:001|Ana
PUT|cliente:002|Bruno
PUT|cliente:002|Bruno Silva
DEL|cliente:003|
```

O ponto principal da demonstração é observar que:

- `cliente:002` reaparece com o valor **mais recente**;
- `cliente:003` não reaparece, pois foi removido;
- a reconstrução acontece a partir do arquivo de log.

---

## 6. Como explicar em sala

### 6.1 TAD

Explique que `SGBD.java` é o **Tipo Abstrato de Dados**:

- define o que o banco faz;
- não expõe como o banco é implementado;
- permite trocar a implementação sem alterar o código cliente.

### 6.2 Estruturas em memória

Explique que:

- o `HashMap` serve para busca rápida por chave;
- o `TreeMap` serve para listar as chaves em ordem;
- as duas estruturas resolvem problemas diferentes e podem coexistir.

### 6.3 Persistência

Explique que:

- cada `put` e `delete` gera uma linha no log;
- o log é gravado em disco;
- ao abrir novamente, o banco lê esse histórico e refaz o estado.

### 6.4 Ligação com ACID

Explique que este exemplo trata principalmente de:

- **Durabilidade**: o dado continua existindo após reinício;
- **Atomicidade parcial**: cada operação é registrada como unidade individual;
- **Consistência mínima**: chaves válidas e estrutura coerente;
- **Isolamento básico**: operações sincronizadas.

---

## 7. Como usar isso como base para a A2

Os grupos podem reaproveitar este exemplo como ponto de partida:

1. manter o TAD `SGBD`;
2. manter o log append-only;
3. manter a reconstrução do estado;
4. adaptar o domínio (`cliente`, `livro`, `voo`, `pedido` etc.);
5. substituir ou complementar a estrutura auxiliar pela árvore exigida no grupo.

Regra recomendada:

> primeiro fazer o núcleo comum funcionar; depois implementar a estrutura específica do grupo.

---

## 8. Limitações intencionais do exemplo

Este banco didático **não** pretende resolver:

- SQL;
- transações completas;
- múltiplos índices sofisticados;
- concorrência avançada;
- compactação automática do log;
- serialização rica de objetos.

Essas ausências são intencionais: o foco da aula é a compreensão estrutural do problema.

---

## 9. Arquivo gerado

A demo cria o arquivo:

```text
aula15-demo.log
```

Esse arquivo pode ser aberto em um editor de texto durante a aula para mostrar:

- que cada operação foi persistida;
- que o formato do log é legível;
- que o banco consegue reconstruir o estado a partir dele.

---

## 10. Resumo final

Este exemplo existe para mostrar, de maneira concreta, que:

- um `HashMap` não é um banco de dados por si só;
- um banco didático pode ser modelado como **TAD + implementação concreta**;
- persistência e recuperação são o núcleo conceitual desta etapa da disciplina;
- a A2 pode ser iniciada com uma base pequena, correta e demonstrável.

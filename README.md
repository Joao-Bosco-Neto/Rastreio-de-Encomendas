# Rastreio de Encomendas

## Descrição

Este projeto é um SGBD didático chave-valor implementado em Java para a disciplina de Estrutura de Dados da UNITINS. Ele simula o rastreio de encomendas de uma transportadora usando um banco com persistência em disco via append-only log e um grafo direcionado para modelar a malha logística entre centros de distribuição.

## Integrantes do Grupo

- João Bosco
- Samuel Araújo
- Gustavo da Silva Oliveira

## Estrutura do Projeto

```
src/
├── SGBD.java           — Interface que define as operações do banco chave-valor (inserir, buscar, remover, listar)
├── TipoOperacao.java   — Enum com os tipos de operação registrados no log (PUT e DEL)
├── RegistroLog.java    — Representa uma entrada no arquivo de log append-only
├── LogAppendOnly.java  — Implementa a camada de persistência em disco via log sequencial append-only
├── Grafo.java          — Grafo direcionado com lista de adjacência e BFS para calcular o menor caminho
├── Rastreio.java       — Integra o banco de dados e o grafo para registrar e rastrear encomendas
└── DemoGrupo04.java    — Classe principal com a demonstração completa do sistema em duas sessões
```

## Como Compilar e Executar

```bash
javac src/*.java -d out/
java -cp out DemoGrupo04
```

## O Que o Programa Demonstra

O programa roda em duas sessões:


**Sessão 1:** configura o grafo com os centros de distribuição e as rotas entre eles, cadastra as encomendas no banco e chama `rastrearRota` para encontrar o caminho mais curto entre a origem e o destino de cada encomenda usando BFS.

**Sessão 2:** reabre o banco a partir do log gravado em disco e prova que todo o estado foi recuperado corretamente, sem perda de dados, demonstrando a persistência do sistema.

## Estruturas de Dados Utilizadas

- **HashMap** — índice primário em memória que mapeia cada código de rastreio ao seu valor mais recente, garantindo acesso O(1).
- **TreeMap** — mantém as chaves em ordem alfabética para listagem ordenada do banco.
- **Grafo direcionado com lista de adjacência** — modela a malha logística entre os centros de distribuição; usa **BFS** para encontrar o caminho mais curto (menor número de saltos) entre dois centros.

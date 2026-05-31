# Design Patterns - Sistema de Pedidos

[![Repositório GitHub](https://img.shields.io/badge/GitHub-Los--Bros%2FDesignPattern-blue?logo=github)](https://github.com/Los-Bros/DesignPattern)

Este repositório contém a implementação de um sistema de gerenciamento de **Pedidos**, desenvolvido com foco na aplicação prática de **Padrões de Projeto (Design Patterns)** orientados a objetos.

---

## 🏛️ Padrões de Projeto Aplicados

Baseado no diagrama de classes e nos requisitos da disciplina, o projeto foi arquitetado para incorporar os seguintes padrões de projeto:

### 1. Strategy (Frete) - *Aula 4*
**Objetivo:** Permitir que o algoritmo de cálculo de frete varie independentemente dos clientes que o utilizam.
* **Contexto no Projeto:** O cálculo do valor do frete varia de acordo com a modalidade de transporte escolhida. Para evitar múltiplos e complexos blocos `if/else` ou `switch/case` dentro da classe `Pedido`, extraímos essa lógica.
* **Implementação:** * Criamos a interface `Frete` que estabelece o contrato com o método `calcula(BigDecimal valorPedido)`.
    * As classes concretas `Terrestre` e `Aéreo` implementam essa interface com suas próprias regras e taxas.
    * A classe `Pedido` possui uma composição com a interface `Frete` (`tipoFrete`), delegando o cálculo em tempo de execução. Isso respeita fortemente o princípio do Aberto/Fechado (Open/Closed Principle).

### 2. State (Status) - *Aula 7*
**Objetivo:** Permitir que um objeto altere seu comportamento quando seu estado interno muda. O objeto parecerá ter mudado de classe.
* **Contexto no Projeto:** Um `Pedido` possui um ciclo de vida rigoroso (Aguardando Pagamento → Pago → Enviado ou Cancelado). As ações que o sistema pode realizar (como despachar o pedido ou cancelá-lo) dependem totalmente do estado atual em que o pedido se encontra.
* **Implementação:**
    * A interface `Status` define os comportamentos que são sensíveis ao estado: `sucessoAoPagar()`, `despacharPedido()` e `cancelarPedido()`.
    * Criamos classes de estado concretas (`AguardandoPagamento`, `Pago`, `Enviado` e `Cancelado`), cada uma mantendo uma referência para o `Pedido` e implementando apenas as transições de estado que fazem sentido para aquela fase.
    * A classe `Pedido` apenas repassa a chamada de seus próprios métodos para o objeto `Status` atual (`statusAtual`).

### 3. Template Method (Pagamento)
**Objetivo:** Definir o esqueleto de um algoritmo em uma operação, postergando alguns passos para as subclasses.
* **Contexto no Projeto:** O fluxo da operação `realizarPagamento()` do Pedido segue um roteiro padronizado (ex: validar dados, processar a cobrança, atualizar o status do pedido, notificar o usuário), mas o detalhe de *como* algumas etapas são processadas varia de acordo com o método de pagamento (Cartão, Boleto, Pix).
* **Implementação:**
    * Existe um método principal que orquestra a ordem exata de execução dos passos.
    * Passos que são invariáveis (como a atualização final do Status) são mantidos na superclasse.
    * Passos variáveis (como a comunicação com a API do banco) são definidos como métodos abstratos ou ganchos (*hooks*), obrigando as subclasses de pagamento a fornecerem a implementação específica, preservando a estrutura unificada do algoritmo.

---

## 📦 Estrutura de Domínio (Entidades base)

Além da forte aplicação de padrões comportamentais, o sistema apoia-se em uma modelagem de domínio tradicional (composição e agregação):

* **`Pedido`:** Entidade central. Mantém a lista de itens, gerencia totais e controla o ciclo de vida da compra.
* **`ItemPedido`:** Representa a relação de dependência entre um Pedido e um Produto, armazenando a quantidade e o valor unitário no momento exato da compra.
* **`Produto`:** Entidade de catálogo, contendo dados mestres como id, descrição e valor base.

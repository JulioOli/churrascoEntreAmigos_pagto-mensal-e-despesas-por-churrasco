# 📋 Análise de Coerência: Diagrama de Classes vs Código Java

**Data:** 13/11/2025  
**Sprint Atual:** Sprint 2 (Funcionalidades Financeiras)  
**Objetivo:** Garantir 100% de coerência entre o diagrama de classes (artefatos_astah_v3) e o código implementado

---

## 🔍 ANÁLISE COMPARATIVA

### ✅ Classes IMPLEMENTADAS no Código

| Classe | Pacote | Status | Observações |
|--------|--------|--------|-------------|
| `Usuario` | `com.churrascoapp.model` | ✅ Implementada | OK |
| `Churrasco` | `com.churrascoapp.model` | ✅ Implementada | OK |
| `Compra` | `com.churrascoapp.model` | ✅ Implementada | OK |
| `Carrinho` | `com.churrascoapp.model` | ✅ Implementada | OK |
| `Pagamento` | `com.churrascoapp.model` | ✅ Implementada | OK |
| `ItemCarrinho` | `com.churrascoapp.model` | ✅ Implementada | OK |
| `ItemCatalogo` | `com.churrascoapp.model` | ✅ Implementada | OK |
| `CompraController` | `com.churrascoapp.controller` | ✅ Implementada | OK |
| `EventoController` | `com.churrascoapp.controller` | ✅ Implementada | OK |
| `UsuarioController` | `com.churrascoapp.controller` | ✅ Implementada | OK |
| `CatalogoController` | `com.churrascoapp.controller` | ✅ Implementada | OK |
| `CompraService` | `com.churrascoapp.service` | ✅ Implementada | OK |
| `ChurrascoService` | `com.churrascoapp.service` | ✅ Implementada | OK |
| `EventoService` | `com.churrascoapp.service` | ✅ Implementada | OK |
| `UsuarioService` | `com.churrascoapp.service` | ✅ Implementada | OK |
| `CatalogoService` | `com.churrascoapp.service` | ✅ Implementada | OK |
| `CompraDAO` | `com.churrascoapp.dao` | ✅ Implementada | Renomear para `CompraRepository` |
| `EventoDAO` | `com.churrascoapp.dao` | ✅ Implementada | Renomear para `ChurrascoRepository` |
| `UsuarioDAO` | `com.churrascoapp.dao` | ✅ Implementada | OK |
| `CatalogoDAO` | `com.churrascoapp.dao` | ✅ Implementada | OK |
| `PagamentoDAO` | `com.churrascoapp.dao` | ✅ Implementada | Renomear para `PagamentoRepository` |

---

### ❌ Classes FALTANDO no Código (Definidas no Diagrama)

#### 🔴 PRIORIDADE ALTA - Sprint 2 (Funcionalidades Financeiras)

| Classe | Tipo | Pacote Destino | Descrição |
|--------|------|----------------|-----------|
| `AlertaConsumo` | Model | `com.churrascoapp.model` | **CRÍTICO** - Alertas de consumo excessivo |
| `Comprovante` | Model | `com.churrascoapp.model` | **CRÍTICO** - Comprovantes de compras |
| `PrestacaoConta` | Model | `com.churrascoapp.model` | **CRÍTICO** - Prestação de contas financeiras |
| `Convite` | Model | `com.churrascoapp.model` | Convites para churrascos |
| `Participacao` | Model | `com.churrascoapp.model` | Participação de usuários em churrascos |
| `email` | Model | `com.churrascoapp.model` | Classe para envio de e-mails |

#### 🟡 PRIORIDADE MÉDIA - Camada de Controle

| Classe | Tipo | Pacote Destino | Descrição |
|--------|------|----------------|-----------|
| `AuthController` | Controller | `com.churrascoapp.controller` | Autenticação de usuários |
| `CarrinhoController` | Controller | `com.churrascoapp.controller` | Gestão de carrinho de compras |
| `ChurrascoController` | Controller | `com.churrascoapp.controller` | CRUD de churrascos |

#### 🟡 PRIORIDADE MÉDIA - Camada de Serviço

| Classe | Tipo | Pacote Destino | Descrição |
|--------|------|----------------|-----------|
| `AuthService` | Service | `com.churrascoapp.service` | Lógica de autenticação |
| `CarrinhoService` | Service | `com.churrascoapp.service` | Lógica de carrinho |

#### 🟢 PRIORIDADE BAIXA - Camada de Persistência

| Classe | Tipo | Pacote Destino | Descrição |
|--------|------|----------------|-----------|
| `CarrinhoRepository` | Repository | `com.churrascoapp.dao` | Persistência de carrinho |
| `ChurrascoRepository` | Repository | `com.churrascoapp.dao` | Corrigir nome de `EventoDAO` |
| `CompraRepository` | Repository | `com.churrascoapp.dao` | Corrigir nome de `CompraDAO` |

---

## 🔧 PROBLEMAS IDENTIFICADOS

### 1. Nomenclatura Inconsistente

#### ⚠️ DAOs vs Repositories
O diagrama usa `Repository`, mas o código usa `DAO`:
- ❌ `CompraDAO` → ✅ Deve ser `CompraRepository`
- ❌ `EventoDAO` → ✅ Deve ser `ChurrascoRepository` (corrigir typo do diagrama: "ChurrascoRespository")
- ❌ `PagamentoDAO` → ✅ Deve ser `PagamentoRepository`

#### ⚠️ Eventos vs Churrascos
- O código usa `EventoController`, `EventoService`, `EventoDAO`
- O diagrama usa `ChurrascoController`, `ChurrascoService`, `ChurrascoRepository`
- **DECISÃO:** Manter `Churrasco` (nome do domínio)

---

## 📊 ESTATÍSTICAS

- **Total de Classes no Diagrama:** ~40 classes
- **Classes Implementadas:** 21 (52,5%)
- **Classes Faltando:** 19 (47,5%)
- **Classes com Nome Incorreto:** 5 (12,5%)

---

## 🎯 PLANO DE AÇÃO

### FASE 1: Criação de Classes de Modelo Faltantes (Sprint 2 - Prioridade Alta)

1. ✅ Criar `AlertaConsumo.java`
2. ✅ Criar `Comprovante.java`
3. ✅ Criar `PrestacaoConta.java`
4. ✅ Criar `Convite.java`
5. ✅ Criar `Participacao.java`
6. ✅ Criar `email.java`

### FASE 2: Renomear Classes Existentes para Conformidade

7. ✅ Renomear `EventoDAO` → `ChurrascoRepository`
8. ✅ Renomear `CompraDAO` → `CompraRepository`
9. ✅ Renomear `PagamentoDAO` → `PagamentoRepository`
10. ✅ Atualizar referências em Services e Controllers

### FASE 3: Criar Controllers Faltantes

11. ✅ Criar `AuthController.java`
12. ✅ Criar `CarrinhoController.java`
13. ✅ Criar `ChurrascoController.java` (renomear de `EventoController`)

### FASE 4: Criar Services Faltantes

14. ✅ Criar `AuthService.java`
15. ✅ Criar `CarrinhoService.java`

### FASE 5: Criar Repositories Faltantes

16. ✅ Criar `CarrinhoRepository.java`
17. ✅ Verificar e corrigir todos os Repositories

---

## ⏭️ PRÓXIMOS PASSOS IMEDIATOS

1. **Extrair atributos e métodos do diagrama RTF** para cada classe
2. **Implementar classes faltantes** com atributos e métodos corretos
3. **Renomear classes** para conformidade com o diagrama
4. **Testar compilação** do projeto
5. **Validar funcionalidades** existentes

---

## 📌 OBSERVAÇÕES IMPORTANTES

- **Cliente valoriza coerência 100%**: Nomes e métodos devem ser EXATAMENTE iguais ao diagrama
- **Sprint 2 - Foco em Financeiro**: Priorizar `AlertaConsumo`, `Comprovante`, `PrestacaoConta`
- **Manter sistema funcional**: Todas as alterações devem preservar funcionalidades existentes
- **Typo no diagrama**: "ChurrascoRespository" deve ser corrigido para "ChurrascoRepository"

---

**Analista Responsável:** GitHub Copilot  
**Última Atualização:** 13/11/2025

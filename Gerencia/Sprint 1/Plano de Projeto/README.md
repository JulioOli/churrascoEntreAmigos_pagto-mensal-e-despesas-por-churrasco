# Plano de Projeto - Sistema de Gestão de Churrascos Entre Amigos

Este documento apresenta o plano completo para o desenvolvimento do Sistema de Gestão de Churrascos Entre Amigos, desenvolvido como parte da disciplina de Engenharia de Software 2.

## 📋 Sobre o Projeto

O Sistema de Gestão de Churrascos Entre Amigos é um software projetado para facilitar a organização, gestão financeira e coordenação de eventos sociais entre grupos de amigos.

### Principais Funcionalidades
- Gestão de usuários e autenticação
- Criação e gerenciamento de eventos
- Sistema de convites automatizado
- Controle de pagamentos e check-in
- Cálculo automático de lista de compras
- Sistema de prestação de contas
- Relatórios financeiros detalhados

## 👥 Equipe do Projeto

### Gerentes
- Julio Cesar Iioulos da Silva
- Ricardo Santos de Oliveira
- Igor Barbosa dos Santos

### SQA (Garantia de Qualidade)
- Luiz
- Sara

### A&P (Análise e Projeto)
- Augusto
- Coleta

### COD (Codificação)
- Guilherme Carrara
- Guilherme Digiorgi

## 📅 Cronograma

- **Sprint 0 (Preparação):** 27 dias - Documentação e planejamento
- **Sprint 1 (Desenvolvimento Inicial):** 26 dias - Cadastros e funcionalidades básicas
- **Sprint 2 (Desenvolvimento Avançado):** 23 dias - Sistema financeiro e cálculos
- **Sprint 3 (Finalização):** 10 dias - Testes finais e entrega

**Duração Total:** 86 dias úteis

## 🛠 Como Compilar o Documento

### Pré-requisitos
- LaTeX (texlive-latex-base, texlive-latex-recommended, texlive-latex-extra)
- Make (opcional, mas recomendado)

### Instalação das Dependências (Ubuntu/Debian)
```bash
sudo apt-get update
sudo apt-get install texlive-latex-base texlive-latex-recommended texlive-latex-extra texlive-lang-portuguese
```

### Compilação

#### Usando Make (Recomendado)
```bash
# Compilação completa
make all

# Compilação rápida
make quick

# Visualizar PDF
make view

# Verificar dependências
make check-deps

# Limpar arquivos temporários
make clean

# Ajuda completa
make help
```

#### Usando LaTeX diretamente
```bash
pdflatex main.tex
pdflatex main.tex
pdflatex main.tex
```

**Nota:** Execute o comando três vezes para garantir que todas as referências e índices sejam gerados corretamente.

## 📁 Estrutura do Projeto

```
projeto-completo/
├── main.tex                    # Arquivo principal
├── capa.tex                    # Capa do documento
├── Makefile                    # Arquivo de compilação
├── README.md                   # Este arquivo
├── unesp.png                   # Logo da UNESP
├── images/                     # Imagens do projeto
│   └── projeto_3___churrasco_2025-10-01_04.09pm.png
└── sections/                   # Seções do documento
    ├── summary.tex             # Resumo
    ├── introduction.tex        # Introdução
    ├── risk_assessment.tex     # Avaliação de riscos
    ├── estimations.tex         # Estimativas e cronogramas
    └── team_organization.tex   # Organização da equipe
```

## 📊 Gráfico de Gantt

O projeto inclui um gráfico de Gantt detalhado (localizado em `images/projeto_3___churrasco_2025-10-01_04.09pm.png`) que mostra:
- Sequência temporal de todas as atividades
- Dependências críticas entre tarefas
- Alocação de recursos por equipe
- Marcos principais de cada sprint
- Caminho crítico do projeto

## 🔍 Comandos Úteis

```bash
# Verificar sintaxe LaTeX
make check

# Contar palavras
make wordcount

# Criar backup
make backup

# Criar distribuição
make dist
```

## 📝 Notas Importantes

1. **Caminho Crítico:** Todas as tarefas estão no caminho crítico, qualquer atraso impacta a entrega final
2. **Compilação:** Execute sempre três passadas do LaTeX para garantir referências corretas
3. **Imagens:** Certifique-se de que todas as imagens estão na pasta correta antes da compilação

## 🎯 Marcos do Projeto

- **M0 (Dia 27):** Documentação completa e aprovada
- **M1 (Dia 53):** Módulo de cadastros funcionando
- **M2 (Dia 76):** Sistema financeiro implementado
- **M3 (Dia 86):** Sistema completo e entregue

## 📧 Contato

Para dúvidas ou sugestões sobre este documento, entre em contato com a equipe de gerentes do projeto.

---

**Universidade Estadual Paulista "Júlio de Mesquita Filho"**  
**Faculdade de Ciências e Tecnologia**  
**Departamento de Matemática e Computação**  
**Engenharia de Software 2**

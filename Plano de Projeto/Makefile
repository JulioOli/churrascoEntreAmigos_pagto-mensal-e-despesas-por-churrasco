# Makefile para compilação do documento LaTeX
# Sistema de Gestão de Churrascos Entre Amigos - Plano de Projeto

# Variáveis
MAIN = main
OUTPUT_DIR = output
TEX_FILES = $(wildcard *.tex) $(wildcard sections/*.tex)
IMAGE_FILES = $(wildcard images/*.png) $(wildcard *.png)

# Comandos padrão
LATEX = pdflatex
BIBTEX = bibtex

# Compilação completa
all: $(MAIN).pdf

# Compilação principal
$(MAIN).pdf: $(TEX_FILES) $(IMAGE_FILES)
	$(LATEX) $(MAIN).tex
	$(LATEX) $(MAIN).tex
	$(LATEX) $(MAIN).tex

# Limpeza de arquivos temporários
clean:
	rm -f *.aux *.log *.toc *.out *.fdb_latexmk *.fls *.synctex.gz

# Limpeza completa (incluindo PDF)
distclean: clean
	rm -f $(MAIN).pdf

# Compilação rápida (apenas uma passada)
quick:
	$(LATEX) $(MAIN).tex

# Visualizar PDF (se disponível)
view: $(MAIN).pdf
	@if command -v evince >/dev/null 2>&1; then \
		evince $(MAIN).pdf & \
	elif command -v okular >/dev/null 2>&1; then \
		okular $(MAIN).pdf & \
	elif command -v xdg-open >/dev/null 2>&1; then \
		xdg-open $(MAIN).pdf & \
	else \
		echo "Nenhum visualizador de PDF encontrado"; \
	fi

# Verificar dependências
check-deps:
	@echo "Verificando dependências do LaTeX..."
	@command -v pdflatex >/dev/null 2>&1 || { echo "pdflatex não encontrado. Instale texlive-latex-base"; exit 1; }
	@command -v latex >/dev/null 2>&1 || { echo "latex não encontrado. Instale texlive-latex-base"; exit 1; }
	@echo "Dependências verificadas com sucesso!"

# Instalar dependências no Ubuntu/Debian
install-deps:
	sudo apt-get update
	sudo apt-get install texlive-latex-base texlive-latex-recommended texlive-latex-extra texlive-lang-portuguese

# Contar palavras no documento
wordcount:
	@echo "Contagem de palavras por seção:"
	@for file in sections/*.tex; do \
		echo "$$file: $$(detex $$file | wc -w) palavras"; \
	done
	@echo "Total: $$(detex $(MAIN).tex | wc -w) palavras"

# Verificar sintaxe LaTeX
check:
	$(LATEX) -draftmode -halt-on-error $(MAIN).tex

# Criar arquivo de distribuição
dist: $(MAIN).pdf
	@mkdir -p dist
	@cp $(MAIN).pdf dist/
	@cp -r images dist/ 2>/dev/null || true
	@cp README.md dist/ 2>/dev/null || true
	@echo "Distribuição criada em dist/"

# Backup do projeto
backup:
	@DATE=$$(date +%Y%m%d_%H%M%S); \
	tar -czf backup_$$DATE.tar.gz *.tex sections/ images/ Makefile

# Help
help:
	@echo "Comandos disponíveis:"
	@echo "  all         - Compilação completa do documento"
	@echo "  quick       - Compilação rápida (uma passada)"
	@echo "  clean       - Remove arquivos temporários"
	@echo "  distclean   - Remove todos os arquivos gerados"
	@echo "  view        - Abre o PDF no visualizador padrão"
	@echo "  check-deps  - Verifica se as dependências estão instaladas"
	@echo "  install-deps- Instala dependências no Ubuntu/Debian"
	@echo "  wordcount   - Conta palavras do documento"
	@echo "  check       - Verifica sintaxe LaTeX"
	@echo "  dist        - Cria pacote de distribuição"
	@echo "  backup      - Cria backup do projeto"
	@echo "  help        - Mostra esta ajuda"

# Força recompilação
.PHONY: all clean distclean quick view check-deps install-deps wordcount check dist backup help

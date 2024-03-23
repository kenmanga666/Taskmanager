# Nom du binaire à produire
BINARY_NAME = TaskManager

# Liste explicite des fichiers source
SOURCES :=  src/mainPage.java \
			src/TaskManager.java \
			src/Task.java \
			src/TaskListCellRenderer.java \
			src/TaskDetails.java \
			src/subTaskManager.java

# Assurez-vous que le shell et l'encodage sont définis pour UTF-8
SHELL := /bin/bash
export LC_ALL=C.UTF-8

# Commande par défaut
default: build

# Commande de build
build:
	@echo -e "Construction du projet..."
	javac -d build -cp . $(SOURCES)

# Commande pour nettoyer le projet (supprimer le binaire)
clean:
	@echo -e "Nettoyage..."
	rm -rf build
	rm -f $(BINARY_NAME)

# Commande pour exécuter le programme
run:
	@echo -e "Execution du programme..."
	java -cp build src/$(BINARY_NAME)

# Option 'phony' pour indiquer que 'clean', 'run', et 'build' ne sont pas des fichiers
.PHONY: build clean run

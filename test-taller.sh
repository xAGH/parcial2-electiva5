#!/bin/bash

# Configuración básica
API_BASE_URL="http://localhost:8090"

# Función para imprimir resultados de forma clara
print_result() {
  echo -e "\n\033[1;34m$1\033[0m"
  echo -e "URL: \033[0;33m$2\033[0m"
  if [ -n "$3" ]; then
    echo -e "Respuesta: \033[0;36m$3\033[0m"
  fi
  echo -e "\033[0;32m$4\033[0m"
  echo "--------------------------------------------------"
}

# 1. Crear una nueva tarea (POST /api/tasks)
echo -e "\n\033[1;35m=== Iniciando pruebas del Taller ===\033[0m"

echo -e "\n\033[1;33m1. Peticion del taller en docker\033[0m"
response=$(curl -s -X GET "$API_BASE_URL")
print_result "GET /" "$API_BASE_URL" "$(echo "$response".)"

echo -e "\n\033[1;35m=== Pruebas completadas ===\033[0m"
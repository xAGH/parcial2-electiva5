#!/bin/bash

# Configuración básica
API_BASE_URL="http://localhost:8088/api/tasks"
HEADER="Content-Type: application/json"

# Función para imprimir resultados de forma clara
print_result() {
  echo -e "\n\033[1;34m$1\033[0m"
  echo -e "URL: \033[0;33m$2\033[0m"
  if [ -n "$3" ]; then
    echo -e "Datos enviados: \033[0;36m$3\033[0m"
  fi
  echo -e "Respuesta:"
  echo -e "\033[0;32m$4\033[0m"
  echo "--------------------------------------------------"
}

# Función para verificar si jq está instalado
verify_jq() {
  if ! command -v jq &> /dev/null; then
    echo -e "\033[1;31mError: jq no está instalado.\033[0m"
    echo "Instala jq para formatear las respuestas JSON:"
    echo "  Ubuntu/Debian: sudo apt-get install jq"
    echo "  Mac: brew install jq"
    echo "O elimina todas las instancias de '| jq' del script."
    exit 1
  fi
}

# Verificar jq
verify_jq

# 1. Crear una nueva tarea (POST /api/tasks)
echo -e "\n\033[1;35m=== Iniciando pruebas del Parcial ===\033[0m"

task_data='{
  "title": "Tarea de prueba inicial",
  "description": "Esta es una tarea creada para las pruebas",
  "status": "PENDING"
}'

echo -e "\n\033[1;33m1. Creando tarea inicial...\033[0m"
response=$(curl -s -X POST "$API_BASE_URL" -H "$HEADER" -d "$task_data")
print_result "POST /api/tasks" "$API_BASE_URL" "$task_data" "$(echo "$response" | jq .)"

# Extraer el ID de la tarea creada
task_id=$(echo "$response" | jq -r '.data.id')
if [ "$task_id" == "null" ] || [ -z "$task_id" ]; then
  echo -e "\033[1;31mError: No se pudo crear la tarea inicial. Abortando pruebas.\033[0m"
  exit 1
fi

echo -e "\033[1;32mTarea creada con ID: $task_id\033[0m"

# 2. Obtener la tarea recién creada (GET /api/tasks/{id})
echo -e "\n\033[1;33m2. Obteniendo tarea recién creada...\033[0m"
response=$(curl -s -X GET "$API_BASE_URL/$task_id" -H "$HEADER")
print_result "GET /api/tasks/{id}" "$API_BASE_URL/$task_id" "" "$(echo "$response" | jq .)"

# 3. Obtener todas las tareas (GET /api/tasks)
echo -e "\n\033[1;33m3. Listando todas las tareas...\033[0m"
response=$(curl -s -X GET "$API_BASE_URL" -H "$HEADER")
print_result "GET /api/tasks" "$API_BASE_URL" "" "$(echo "$response" | jq .)"

# 4. Actualizar la tarea (PATCH /api/tasks/{id})
echo -e "\n\033[1;33m4. Actualizando la tarea...\033[0m"
update_data='{
  "title": "Tarea actualizada",
  "description": "Descripción modificada por el script",
  "status": "IN_PROGRESS"
}'
response=$(curl -s -X PATCH "$API_BASE_URL/$task_id" -H "$HEADER" -d "$update_data")
print_result "PATCH /api/tasks/{id}" "$API_BASE_URL/$task_id" "$update_data" "$(echo "$response" | jq .)"

# 5. Verificar la actualización (GET /api/tasks/{id})
echo -e "\n\033[1;33m5. Verificando la actualización...\033[0m"
response=$(curl -s -X GET "$API_BASE_URL/$task_id" -H "$HEADER")
print_result "GET /api/tasks/{id}" "$API_BASE_URL/$task_id" "" "$(echo "$response" | jq .)"

# 6. Probar con una tarea inexistente (GET /api/tasks/9999)
echo -e "\n\033[1;33m6. Probando con ID inexistente...\033[0m"
response=$(curl -s -X GET "$API_BASE_URL/9999" -H "$HEADER")
print_result "GET /api/tasks/9999" "$API_BASE_URL/9999" "" "$(echo "$response" | jq .)"

# 7. Eliminar la tarea (DELETE /api/tasks/{id})
echo -e "\n\033[1;33m7. Eliminando la tarea...\033[0m"
response=$(curl -s -X DELETE "$API_BASE_URL/$task_id" -H "$HEADER")
print_result "DELETE /api/tasks/{id}" "$API_BASE_URL/$task_id" "" "$(echo "$response" | jq .)"

# 8. Verificar que la tarea fue eliminada (GET /api/tasks/{id})
echo -e "\n\033[1;33m8. Verificando eliminación...\033[0m"
response=$(curl -s -X GET "$API_BASE_URL/$task_id" -H "$HEADER")
print_result "GET /api/tasks/{id} después de eliminar" "$API_BASE_URL/$task_id" "" "$(echo "$response" | jq .)"

echo -e "\n\033[1;35m=== Pruebas completadas ===\033[0m"
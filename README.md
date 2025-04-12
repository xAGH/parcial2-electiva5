
# Microservicio de Gestión de Tareas

## Resumen

Este repositorio contiene un **Microservicio de Gestión de Tareas** construido con **Spring Boot**. Proporciona una API RESTful para gestionar tareas con operaciones CRUD. El servicio permite a los usuarios crear, leer, actualizar y eliminar tareas. Utiliza **Docker** para la contenerización y **Docker Compose** para la orquestación.

## Tecnologías Utilizadas

- **Spring Boot** (para construir la API REST)
- **Java 21** (JDK)
- **Lombok** (para reducir el código repetitivo)
- **Docker** (para contenerización)
- **Docker Compose** (para orquestación)
- **Postman** (para pruebas de la API)
- **JUnit** y **Mockito** (para pruebas unitarias)
- **JaCoCo** (para cobertura de código)

## Características

- **Operaciones CRUD de Tareas**
  - `GET /api/tasks` - Obtener todas las tareas
  - `GET /api/tasks/{id}` - Obtener tarea por ID
  - `POST /api/tasks` - Crear una nueva tarea
  - `PATCH /api/tasks/{id}` - Actualizar una tarea existente
  - `DELETE /api/tasks/{id}` - Eliminar una tarea

- **Estructura de Respuesta API**: Respuestas estandarizadas para casos de éxito y error, utilizando el DTO `ApiResponse`.

- **Validación de Tareas**: Validación de entrada utilizando anotaciones (por ejemplo, `@NotBlank`, `@NotNull`).

## Configuración

Para comenzar con el servicio Task Manager, sigue estos pasos:

### Requisitos Previos

- Tener **Docker** y **Docker Compose** instalados en tu máquina.
- Tener **JDK 21** instalado para construir y ejecutar la aplicación (si se ejecuta fuera de Docker).
- **Postman** para probar los puntos finales de la API.

### Clonación del Repositorio

Clona este repositorio en tu máquina local:

```bash
git clone https://github.com/xAGH/parcial2-electiva5
cd task-manager
```

### Ejecutar la Aplicación

Para ejecutar la aplicación con Docker Compose, sigue estos pasos:

1. **Construir e iniciar los contenedores** usando Docker Compose:

```bash
docker-compose up
```

Esto iniciará la aplicación junto con otros servicios definidos en el archivo `docker-compose.yml`.

2. La aplicación será accesible en `http://localhost:8080` por defecto.

## 🌐 Despliegue en Producción

Este microservicio está actualmente desplegado y accesible públicamente en:

➡️ **[https://web.xagh.dev/parcial](https://web.xagh.dev/parcial)**

Puedes acceder a la API directamente desde esa URL para realizar pruebas o integraciones sin necesidad de levantar el entorno localmente.

### Ejecutar Pruebas

Hay dos formas de ejecutar las pruebas:

#### 1. **Pruebas Unitarias y de Integración**

Las pruebas unitarias e integradas están escritas utilizando **JUnit** y **Mockito**. Para ejecutar las pruebas:

```bash
./gradlew test jacocoTestReport
```

Esto ejecutará todas las pruebas unitarias y generará un reporte de cobertura de código usando **JaCoCo**. Además, permitirá acceder a `/tests/index.html` y `/coverage/index.html` para ver el resultado de los test y la covertura de los mismos respectivamente.

#### 2. **Pruebas Manuales de la API con Postman**

Importa la **colección de Postman** ubicada en la carpeta `/postman_collection` en Postman. Esta colección incluye solicitudes predefinidas para probar todas las operaciones CRUD de la API.

Puedes encontrar la colección en: `postman_collection/TaskManager.postman_collection.json`.

#### 3. **Ejecutar Pruebas de la API Usando cURL**

También hay un script `test-endpoints.sh` incluido en el repositorio para ejecutar pruebas de la API utilizando **cURL**. Puedes ejecutarlo de la siguiente manera:

```bash
bash test-endpoints.sh
```

Este script ejecuta una serie de solicitudes `cURL` para probar los puntos finales de la API de Task Manager.

### Detener la Aplicación

Para detener la aplicación y todos los servicios, usa el siguiente comando:

```bash
docker-compose down
```

## Documentación de la API

A continuación se resumen los puntos finales de la API disponibles:

### `GET /api/tasks`
- **Descripción**: Recupera todas las tareas.
- **Respuesta**: Una lista de tareas.

### `GET /api/tasks/{id}`
- **Descripción**: Recupera una tarea específica por ID.
- **Respuesta**: Datos de la tarea si se encuentra, 404 si no se encuentra.

### `POST /api/tasks`
- **Descripción**: Crea una nueva tarea.
- **Cuerpo**: 
    ```json
    {
      "title": "Título de la Tarea",
      "description": "Descripción de la Tarea",
      "status": "PENDIENTE"
    }
    ```
- **Respuesta**: La tarea creada.

### `PATCH /api/tasks/{id}`
- **Descripción**: Actualiza una tarea existente.
- **Cuerpo**:
    ```json
    {
      "title": "Título Actualizado",
      "description": "Descripción Actualizada",
      "status": "COMPLETADA"
    }
    ```
- **Respuesta**: La tarea actualizada.

### `DELETE /api/tasks/{id}`
- **Descripción**: Elimina una tarea por ID.
- **Respuesta**: Mensaje de éxito.

## Estructura del Proyecto

```plaintext
.
├── docker-compose.yml       # Configuración de Docker Compose
├── postman_collection       # Carpeta de colecciones de Postman
│   └── TaskManager.postman_collection.json  # Colección de Postman para la API
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── xagh/
│   │   │       └── eam/
│   │   │           └── taskmanager/
│   │   │               ├── controller/
│   │   │               │   └── TaskController.java
│   │   │               ├── dto/
│   │   │               │   ├── ApiResponse.java
│   │   │               │   ├── TaskRequest.java
│   │   │               │   └── TaskUpdateRequest.java
│   │   │               ├── model/
│   │   │               │   └── Task.java
│   │   │               │   └── TaskStatus.java
│   │   │               └── service/
│   │   │                   └── TaskService.java
│   └── resources/
│       ├── application.properties
├── test-endpoints.sh                # Script de pruebas con cURL
├── build.gradle            # Configuración de Gradle para construir el proyecto
├── Dockerfile              # Dockerfile para construir la imagen
└── README.md               # Este archivo README
```

## Contacto

Si tienes alguna pregunta o problema, no dudes en ponerte en contacto a contact@xagh.dev.


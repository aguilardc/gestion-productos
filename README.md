# GESTION DE PRODUCTOS

## Descripción

Sistema de gestión de productos implementado con **Spring Boot WebFlux**,
siguiendo principios de **Clean Architecture, SOLID** y **Clean Code**.
API completamente reactiva con persistencia R2DBC y consumo de servicios externos.

## 🏗️ Arquitectura y Principios Aplicados

#### Clean Architecture (Hexagonal)

#### Principios SOLID

#### Patrones Aplicados

- Ports & Adapters (Arquitecura Hexagonal)
- Repository Pattern (con Spring Data R2DBC)
- DTO Pattern (separacion Api/Dominio)
- Mapper PAttern (Conversión entre capas)
- Strategy Pattern (diferentes adapters para ports)

### Requisitos

- Java 17 o superior
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## ⚙️ Instalación y Ejecución

### 1. Clonar el repositorio

```BASH
git clone [URL]
cd gestion-producto
```

### 2. Compilar el proyecto

```BASH
mvn clean install
```

### 3. Ejecutar la aplicación

```BASH
mvn spring-boot:run
```

### 4 Verificar que está funcionando

La aplicación arranca en: http://localhost:8080

```BASH
curl http://localhost:8080/api/products
```

## 📡 Endpoints Disponibles

### CRUD de productos

### 1. Crear Producto

```BASH
POST http://localhost:8080/api/products
Content-Type: application/json

{
    "name": "Nintendo Switch",
    "price": 299.99
    "stock": 25
}
```

**Response (201 created):**

```json
{
  "id": 9,
  "name": "Nintendo Switch",
  "price": 299.99,
  "stock": 25
}
```

### 2. Obtener Producto por ID

```BASH
GET http://localhost:8080/api/products/1
```

**Response (200 OK):**

```json
{
  "id": 1,
  "name": "Laptop Dell XPS 15",
  "price": 1299.99,
  "stock": 10
}
```

**Error (404 Not Found):**

```json
{
  "status": 404,
  "message": "Product not found with id: 999",
  "timestamp": "2025-09-29T10:30:00"
}
```

### 3. Listar Todos los Productos

```BASH
GET http://localhost:8080/api/products
```

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "name": "Laptop Dell XPS 15",
    "price": 1299.99,
    "stock": 10
  },
  {
    "id": 2,
    "name": "iPhone 15 Pro",
    "price": 999.99,
    "stock": 3
  }
]
```

### 4. Listar Productos con Stock Bajo (< 5)

```BASH
GET http://localhost:8080/api/products/low-stock
```

**Response (200 OK):**

```json
[
  {
    "id": 2,
    "name": "iPhone 15 Pro",
    "price": 999.99,
    "stock": 3
  },
  {
    "id": 4,
    "name": "Sony WH-1000XM5",
    "price": 399.99,
    "stock": 2
  },
  {
    "id": 7,
    "name": "Apple Watch Series 9",
    "price": 429.99,
    "stock": 1
  }
]
```

### 5. Actualizar Producto

```BASH
PUT http://localhost:8080/api/products/1
Content-Type: application/json

{
  "name": "Laptop Dell XPS 15 (Updated)",
  "price": 1199.99,
  "stock": 15
}
```

**Response (200 OK):**

```json
{
  "id": 1,
  "name": "Laptop Dell XPS 15 (Updated)",
  "price": 1199.99,
  "stock": 15
}
```

### 6. Eliminar Producto

```BASH
DELETE http://localhost:8080/api/products/1
```

**Response (204 No Content)**

## Conversión de Moneda

### 7. Obtener Productos con Precios Convertidos

```BASH
GET http://localhost:8080/api/currency/products?from=PEN&to=USD
```

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "name": "Laptop Dell XPS 15",
    "price": 370.49715,
    "stock": 10
  },
  {
    "id": 2,
    "name": "iPhone 15 Pro",
    "price": 284.99715,
    "stock": 3
  }
]
```

**Nota**: Los precios están convertidos de PEN a USD usando la API externa de tipo de cambio.

#### Otras conversiones disponibles:

```bash
# USD a MXN (Pesos Mexicanos)
GET http://localhost:8080/api/currency/products?from=USD&to=MXN

# USD a JPY (Yen Japonés)
GET http://localhost:8080/api/currency/products?from=USD&to=JPY

# USD a GBP (Libra Esterlina)
GET http://localhost:8080/api/currency/products?from=USD&to=GBP
```

## 🧪 Validaciones

La API valida automáticamente los datos de entrada:

### Ejemplo de Error de Validación

```bash
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "AB",
  "price": -10,
  "stock": -5
}
```

**Response (400 Bad Request):**

```json
{
  "timestamp": "2025-09-29T10:30:00",
  "status": 400,
  "errors": {
    "name": "Name must be between 3 and 100 characters",
    "price": "Price must be greater than 0",
    "stock": "Stock cannot be negative"
  }
}
```

## 🧪 Ejecutar Tests

**Ejecutar todos los test**

```bash
mvn test
```

**Ejecutar solo test unitarios**

```bash
mvn test -Dtest=ProductServiceTest
```

**Ejecutar solo tests de integración**

```bash
mvn test -Dtest=ProductControllerIntegrationTest
```

## 📊 Tests Implementados

### Tests Unitarios (ProductServiceTest.java)

- ✅ Crear producto exitosamente
- ✅ Validar producto inválido
- ✅ Obtener producto por ID
- ✅ Manejar producto no encontrado
- ✅ Listar todos los productos
- ✅ Listar productos con stock bajo
- ✅ Actualizar producto
- ✅ Eliminar producto
- ✅ Validar actualización de producto inexistente

### Tests de Integración (ProductControllerIntegrationTest.java)

- ✅ Crear producto vía REST API
- ✅ Obtener todos los productos
- ✅ Obtener productos con stock bajo
- ✅ Retornar 404 cuando no existe producto
- ✅ Retornar 400 cuando validación falla

## 🗄️ Base de Datos

### H2

### Datos precargados

La aplicación inicia con 8 productos de ejemplo:

| ID | Nombre               | Precio     | Stock |
|:---|:---------------------|:-----------|:------|
| 1  | Laptop Dell XPS 15   | S/ 1299.99 | 10    |
| 2  | iPhone 15 Pro        | S/ 999.99  | 3 ⚠️  |
| 3  | Samsung Galaxy S24   | S/ 849.99  | 15    |
| 4  | Sony WH-1000XM5      | S/ 399.99  | 2 ⚠️  |
| 5  | MacBook Pro M3       | S/ 1999.99 | 8     |
| 6  | iPad Air             | S/ 599.99  | 4 ⚠️  |
| 7  | Apple Watch Series 9 | S/ 429.99  | 1 ⚠️  |
| 8  | AirPods Pro          | S/ 249.99  | 20    |

**⚠️ = Stock bajo (< 5 unidades)**

## 🚨 Manejo de Errores

La Api implementa manejo global de excepciones:

| Código | Descripción           | Ejemplo                      |
|:-------|:----------------------|:-----------------------------|
| 201    | Created               | Producto creado exitosamente |
| 200    | OK                    | Operación exitosa            |
| 204    | No Content            | Producto eliminado           |
| 400    | Bad Request           | Validación fallida           |
| 404    | Not Found             | Producto no existe           |
| 403    | Service Unavailable   | API externa no disponible    |
| 500    | Internal Server Error | Error inesperado             |

## 👨‍💻 Autor

### Nevison Aguilar
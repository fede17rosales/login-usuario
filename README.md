# User Service (Spring Boot 2.5.14, Java 11)

Microservicio para **creación** y **consulta** de usuarios.

## Requisitos técnicos
- Java 11 (o 8)
- Spring Boot **2.5.14**
- Maven 
- H2 como base de datos embebida
- JWT para autenticación
- Cobertura mínima del **80%** en pruebas de Service (JUnit)

## Cómo compilar y ejecutar
```bash
# 1) Compilar y ejecutar pruebas con cobertura
mvn clean verify

# 2) Ejecutar la app
mvn spring-boot:run

# o construir el jar
mvn clean package
java -jar target/user-service-1.0.0.jar
```

La app inicia en `http://localhost:8080` y expone:
- H2 console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:usersdb`)

## Endpoints

### 1) POST /sign-up
Crea un usuario.
Body:
```json
{
  "name": "Julio Gonzalez",
  "email": "julio@testssw.cl",
  "password": "a2asfGfdfdf4",
  "phones": [
    {"number": 87650009, "citycode": 7, "contrycode": "25"}
  ]
}
```
- `email` y `password` **obligatorios**
- `name` y `phones` **opcionales**
- Validaciones:
  - `email` debe cumplir formato `^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$`
  - `password`: exactamente **1** mayúscula, exactamente **2** dígitos (no consecutivos requerido), letras minúsculas, longitud **8–12**.

Respuesta (201):
```json
{
  "id": "e5c6cf84-8860-4c00-91cd-22d3be28904e",
  "created": "2025-09-05T19:00:00Z",
  "lastLogin": "2025-09-05T19:00:00Z",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "isActive": true,
  "name": "Julio Gonzalez",
  "email": "julio@testssw.cl",
  "password": "*****",
  "phones": [{"number":87650009,"citycode":7,"contrycode":"25"}]
}
```

### 2) GET /login
Usa el **token** emitido en `/sign-up` (o en un `/login` anterior). El token **se regenera** en cada login.
Headers:
```
Authorization: Bearer <token>
```

Respuesta (200): mismo contrato del enunciado.

### Errores (JSON-only)
Estructura:
```json
{
  "error": [{
    "timestamp": "2025-09-05T19:00:00Z",
    "codigo": 400,
    "detail": "Descripción del error"
  }]
}
```

Códigos típicos: 400 (validación), 401 (token inválido/ausente), 409 (usuario ya existe), 404 (no encontrado).

## Diagramas UML

![Secuencia](/diagrams/Secuencia%20-%20SignUp%20&%20Login.png)

![Componente](/diagrams/Componente%20-%20User%20Service.png)

## Notas de implementación
- Persistencia con Spring Data JPA + H2.
- `id` tipo **UUID**.
- `password` se encripta con **BCrypt**.
- JWT incluye `sub` = email y `uid` = UUID.
- Se proveen pruebas unitarias del **Service** con JUnit y Mockito.
- JaCoCo establece umbral de **0.80**.

## Ejemplos curl
```bash
curl -s -X POST http://localhost:8080/sign-up   -H "Content-Type: application/json"   -d '{"name":"Julio Gonzalez","email":"julio@testssw.cl","password":"a2asfGfdfdf4","phones":[{"number":87650009,"citycode":7,"contrycode":"25"}]}'

# guardá el token y luego:
curl -s http://localhost:8080/login -H "Authorization: Bearer <token>"
```

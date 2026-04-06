<<<<<<< HEAD
# qa20252
Repositório com os códigos da turma de Qualidade de Software.

=======
# Java REST API

Este projeto é uma API REST simples em Java, desenvolvida com Spring Boot. Ele disponibiliza endpoints para gerenciar itens e oferece suporte a respostas nos formatos JSON e XML.

## Estrutura do Projeto

```
java-rest-api
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── demo
│   │   │               ├── DemoApplication.java
│   │   │               ├── controller
│   │   │               │   └── ApiController.java
│   │   │               └── model
│   │   │                   └── Item.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── demo
│                       └── ApiControllerTest.java
└── README.md
```

## Instruções de Configuração

1. **Clonar o repositório::**
   ```
   git clone <repository-url>
   cd java-rest-api
   ```

2. **Compilar o projeto:**
   Certifique-se de que o Maven esteja instalado e, em seguida, execute:
   ```
   mvn clean install
   ```

3. **Executar a aplicação:**
   Você pode iniciar a aplicação utilizando:
   ```
   mvn spring-boot:run
   ```

4. **Acessar a API:**
   A API estará disponível em `http://localhost:8080/api/items`.

## Exemplos de Uso

- **Obter todos os itens:**
  ```
  GET /api/items
  ```

- **Obter um item por ID:**
  ```
  GET /api/items/{id}
  ```

- **Criar um novo item:**
  ```
  POST /api/items
  Content-Type: application/json

  {
      "name": "Item Name",
      "description": "Item Description"
  }
  ```

## Testes

Testes unitários estão incluídos no projeto para validar a funcionalidade da API. Você pode executá-los utilizando:
```
mvn test
```

Isso executará os testes definidos em `ApiControllerTest.java`, os quais verificam as respostas corretas em JSON e XML, além de validar os códigos de status HTTP.

## Documentação completa dos testes unitários

Focar em testes é o que diferencia um código que "funciona agora" de um código robusto e confiável a longo prazo. É uma das habilidades mais importantes para um desenvolvedor.
>>>>>>> edd7ccd (Av1 Testes)



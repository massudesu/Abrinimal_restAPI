# Abrinimal

API REST para gestão de adoções de animais resgatados, com interface web. O sistema controla animais, adotantes, voluntários, adoções e o histórico de vacinas aplicadas.

## Tecnologias

- Java 25
- Spring Boot 4.1.3 (Spring Web MVC + Spring Data JPA)
- MySQL
- springdoc-openapi (Swagger UI)
- HTML, CSS e JavaScript

## Funcionalidades

- CRUD (GET, POST, PUT e DELETE) para as cinco entidades
- Tratamento de erros com status HTTP adequados (400, 404 e 409)
- Interface web com listagem, busca, formulário de criação/edição e exclusão com confirmação
- Documentação interativa da API via Swagger UI
- Tema claro/escuro automático e layout responsivo

## Modelo de dados

| Entidade | Descrição | Principais campos |
|---|---|---|
| `Animais` | Animais resgatados | nome, espécie, porte, idade estimada, status de adoção, data do resgate |
| `Adotantes` | Pessoas que adotam | nome, CPF, telefone, endereço, tipo de moradia |
| `Voluntarios` | Equipe da ONG | nome, e-mail, telefone, função |
| `Adocoes` | Registro de adoção | data, termo assinado, observações, animal, adotante, voluntário |
| `VacinaAplicada` | Vacinas dos animais | nome da vacina, data de aplicação, próxima dose, animal |

**Relacionamentos**

- Uma adoção pertence a um animal, um adotante e um voluntário (`@ManyToOne`)
- Uma vacina aplicada pertence a um animal, um animal pode ter várias vacinas
- Adotantes e voluntários podem ter várias adoções

**Enums**

- `PorteEnum`: `PEQUENO`, `MEDIO`, `GRANDE`
- `MoradiaEnum`: `CASA`, `APARTAMENTO`

## Como executar

### Pré-requisitos

- JDK 25
- MySQL em execução na porta 3306

### 1. Criar o banco

```sql
CREATE DATABASE abrinimal;
```

### 2. Configurar a conexão

Ajuste `src/main/resources/application.properties` com o seu usuário e senha do MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/abrinimal
spring.datasource.username=root
spring.datasource.password=root
```

As tabelas são criadas automaticamente (`spring.jpa.hibernate.ddl-auto=update`).

### 3. Rodar a aplicação

```bash
./mvnw spring-boot:run
```

No Windows: `mvnw.cmd spring-boot:run`

### 4. Acessar

| O quê | URL |
|---|---|
| Interface web | http://localhost:8080/ |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| API | http://localhost:8080/api/... |

## Endpoints

Todos os recursos seguem o mesmo padrão:

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/{recurso}` | Lista todos os registros |
| `POST` | `/api/{recurso}` | Cria um registro |
| `PUT` | `/api/{recurso}/{id}` | Atualiza um registro existente |
| `DELETE` | `/api/{recurso}/{id}` | Remove um registro |

Recursos disponíveis: `animais`, `adotantes`, `voluntarios`, `adocoes` e `vacinaaplicada`.

### Exemplos de corpo (JSON)

**Animal**

```json
{
  "nome": "Thor",
  "especie": "Cachorro",
  "porte": "GRANDE",
  "idadeEstimada": 3,
  "statusAdocao": "Disponível",
  "dataResgate": "2026-10-15"
}
```

**Adotante**

```json
{
  "nome": "Maria Silva",
  "cpf": "000.000.000-00",
  "telefone": "(11) 91234-5678",
  "endereco": "Rua das Flores, 123",
  "tipoMoradia": "CASA"
}
```

**Voluntário**

```json
{
  "nome": "João Souza",
  "email": "joao@email.com",
  "telefone": "(11) 99876-5432",
  "funcao": "Cuidador"
}
```

**Adoção** (as relações são enviadas apenas pelo `id`)

```json
{
  "dataAdocao": "2026-11-20",
  "termoAssinado": true,
  "observacoes": "Adoção acompanhada presencialmente.",
  "animal": { "id": 1 },
  "adotante": { "id": 1 },
  "voluntario": { "id": 1 }
}
```

**Vacina aplicada**

```json
{
  "nomeVacina": "V10",
  "dataAplicacao": "2026-10-20",
  "proximaDose": "2027-10-20",
  "animal": { "id": 1 }
}
```

### Respostas de erro

| Status | Quando acontece |
|---|---|
| `400 Bad Request` | Dados inválidos no `POST` ou `PUT` |
| `404 Not Found` | Registro inexistente no `PUT` ou `DELETE` |
| `409 Conflict` | Tentativa de excluir um registro que possui vínculos (ex.: animal com adoção registrada) |

## Estrutura do projeto

```
Abrinimal_restAPI/
├── pom.xml
└── src/main/
    ├── java/org/dt2/abrinimal/
    │   ├── AbrinimalApplication.java
    │   ├── controllers/      # endpoints REST
    │   ├── entities/         # entidades JPA
    │   ├── enums/            # PorteEnum, MoradiaEnum
    │   └── repositories/     # interfaces JpaRepository
    └── resources/
        ├── application.properties
        └── static/           # front-end
            ├── index.html
            ├── style.css
            └── script.js
```

## Front-end

A interface fica em `src/main/resources/static/` e é servida pelo próprio Spring Boot

## Documentação com swagger


## Author
Nicolas Massuda<@massudesu>

# Golden Raspberry Awards API

API RESTful para gerenciamento dos dados do Golden Raspberry Awards, incluindo filmes, produtores, estúdios e cálculo de intervalos de prêmios.

## Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+

### Executar a aplicação
```bash
# Clone o repositório
git clone https://github.com/dsvleando/Golden-Raspberry-Awards.git
cd golden-raspberry-awards
./mvnw spring-boot:run
```

### Acessar
- **API**: http://localhost:3333
- **Swagger**: http://localhost:3333/swagger-ui/index.html
- **H2 Console**: http://localhost:3333/h2-console
  - URL: `jdbc:h2:mem:golden-raspberry-awards`
  - User: `outsera` / Pass: `outsera`

## Testes

### Todos os testes
```bash
./mvnw test
```

### Apenas testes de integração
```bash
./mvnw test -Dtest="*IT"
```

### Testes específicos
```bash
./mvnw test -Dtest="ProposalDataValidationIT"  # Valida dados da proposta
./mvnw test -Dtest="MovieUpdateIT"             # Testa atualização de filmes
./mvnw test -Dtest="DataLoadIT"                # Valida carregamento CSV
```

## Testes de Integração

| Teste | Objetivo | Validações |
|-------|----------|------------|
| **DataLoadIT** | Carregamento CSV | 206 filmes, 300 produtores, 59 estúdios |
| **ProposalDataValidationIT** | Dados da proposta | Intervalos min/max, cálculos corretos |
| **MovieUpdateIT** | Atualização filmes | CRUD completo, relacionamentos |
| **ProducerIntervalsIT** | Lógica intervalos | Listas não vazias, cálculos |
| **ProducerIntervalsControllerIT** | Endpoint REST | Status 200, JSON válido |

## Endpoints Principais

### Movies
- `GET /api/movies` - Listar filmes (paginado)
- `GET /api/movies/{id}` - Buscar por ID
- `GET /api/movies/winning` - Filmes vencedores
- `POST /api/movies` - Criar filme
- `PUT /api/movies/{id}` - Atualizar filme
- `DELETE /api/movies/{id}` - Excluir filme

### Producers
- `GET /api/producers` - Listar produtores
- `GET /api/producers/award-intervals` - **Intervalos de prêmios**
- `POST /api/producers` - Criar produtor

### Studios
- `GET /api/studios` - Listar estúdios
- `POST /api/studios` - Criar estúdio

## Exemplo de Uso

### Obter intervalos de prêmios
```bash
curl http://localhost:3333/api/producers/award-intervals
```

### Resposta esperada
```json
{
  "min": [{"producer": "Bo Derek", "interval": 6, "previousWin": 1984, "followingWin": 1990}],
  "max": [{"producer": "Matthew Vaughn", "interval": 13, "previousWin": 2002, "followingWin": 2015}]
}
```

## Tecnologias

- **Spring Boot 3.2.5** - Framework principal
- **H2 Database** - Banco in-memory
- **Spring Data JPA** - Persistência
- **ModelMapper** - Mapeamento de objetos
- **SpringDoc OpenAPI** - Documentação Swagger
- **JUnit 5 + AssertJ** - Testes

## Arquitetura

**Hexagonal Architecture** (Ports & Adapters):
- `domain/` - Regras de negócio
- `application/` - Casos de uso
- `infrastructure/` - Implementações técnicas
- `presentation/` - Controllers REST

## Dados

- **CSV**: `movielist.csv` (206 filmes carregados automaticamente)
- **Banco**: H2 in-memory com schema criado automaticamente
- **Tabelas**: `tb_movie`, `tb_producer`, `tb_studio` + relacionamentos

---

**Versão**: 0.0.1-SNAPSHOT

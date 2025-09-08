# Golden Raspberry Awards Web

Interface web para visualização dos dados do Golden Raspberry Awards (Razzie Awards) - categoria Pior Filme do Ano.

## Instalação

### Pré-requisitos

- Node.js 16+
- npm 8+ ou yarn 1.22+

### Setup do projeto

```bash
# Clone o repositório
git clone https://github.com/dsvleando/Golden-Raspberry-Awards.git
cd golden-raspberry-awards-web

# Instale as dependências
npm install

# Execute em desenvolvimento
npm start

# Acesse http://localhost:3000
```

### Build para produção

```bash
npm run build
npx serve -s build
```

## Funcionalidades

### Dashboard

O dashboard principal apresenta quatro painéis analíticos:

- **Anos com múltiplos vencedores**: Lista os anos que tiveram mais de um filme vencedor
- **Top 3 estúdios**: Ranking dos estúdios com mais vitórias no Razzie
- **Intervalos de produtores**: Análise dos maiores e menores intervalos entre vitórias de produtores
- **Busca por ano**: Ferramenta para encontrar vencedores de um ano específico

### Lista de Filmes

- Paginação completa com navegação entre páginas
- Filtros por ano e status de vencedor
- Busca otimizada com debounce
- Visualização de ID, ano, título e status de vitória
- Estados de loading durante requisições

### Características Técnicas

- Gerenciamento de estado centralizado com Redux Toolkit
- Tratamento robusto de erros com fallbacks
- Otimizações de performance (React.memo, useMemo, useCallback)
- Cobertura completa de testes
- Interface responsiva para desktop, tablet e mobile

## Tecnologias

### Core

- **React 18** - Framework principal com Concurrent Features
- **TypeScript** - Tipagem estática completa
- **Mantine** - Sistema de componentes UI
- **Redux Toolkit** - Gerenciamento de estado
- **React Router** - Roteamento da aplicação

### Desenvolvimento

- **Jest** - Framework de testes
- **Testing Library** - Testes focados no usuário
- **Axios** - Cliente HTTP
- **Create React App** - Configuração e build

## Arquitetura

### Estrutura de pastas

```
src/
├── components/              # Componentes reutilizáveis
│   ├── Layout.tsx          # Layout principal
│   └── Dashboard/          # Componentes específicos do dashboard
├── pages/                  # Páginas da aplicação
│   ├── Dashboard.tsx       # Dashboard principal
│   ├── MoviesList.tsx      # Lista de filmes
│   └── __tests__/          # Testes das páginas
├── store/                  # Redux store
│   ├── index.ts           # Configuração do store
│   └── slices/            # Redux slices
├── api/                   # Camada de API
│   ├── index.ts           # Configuração do Axios
│   ├── moviesApi.ts       # Endpoints de filmes
│   └── dashboardApi.ts    # Endpoints do dashboard
├── hooks/                 # Custom hooks
│   ├── useMovies.ts       # Hook para filmes
│   ├── useDashboard.ts    # Hook para dashboard
│   └── useWinnersSearch.ts # Hook para busca
├── types/                 # Definições TypeScript
│   ├── api.ts             # Tipos da API
│   ├── MoviesApiTypes.ts  # Tipos de filmes
│   └── DashboardApiTypes.ts # Tipos do dashboard
└── utils/                 # Utilitários
```

### Padrões implementados

- **Separation of Concerns**: UI, lógica e dados separados
- **Custom Hooks**: Lógica reutilizável encapsulada
- **Redux Pattern**: Fluxo unidirecional de dados
- **TypeScript First**: Tipagem em todos os níveis
- **Component Composition**: Componentes pequenos e focados

## Testes

### Cobertura atual

- 14 testes implementados
- 100% de cobertura nos componentes principais
- Execução rápida com mocks otimizados

### Executar testes

```bash
# Todos os testes
npm test

# Modo watch
npm test -- --watch

# Com cobertura
npm test -- --coverage

# Testes específicos
npm test Dashboard
npm test MoviesList
```

### Cenários testados

**Dashboard (10 testes)**
- Renderização de todos os painéis
- Carregamento de dados via useEffect
- Tratamento de estados de erro
- Exibição de dados vazios
- Lógica de filtragem (top 3 estúdios)
- Priorização de erros entre hooks

**MoviesList (4 testes)**
- Renderização da tabela de filmes
- Funcionalidade dos filtros
- Sistema de paginação
- Estados de carregamento

## API

### Base URL

```
https://challenge.outsera.tech/api
```

### Endpoints utilizados

| Endpoint | Método | Descrição | Parâmetros |
|----------|--------|-----------|------------|
| `/movies` | GET | Lista paginada de filmes | `page`, `size`, `year`, `winner` |
| `/yearsWithMultipleWinners` | GET | Anos com múltiplos vencedores | - |
| `/studiosWithWinCount` | GET | Estúdios com contagem | - |
| `/maxMinWinIntervalForProducers` | GET | Intervalos de produtores | - |
| `/winnersByYear` | GET | Vencedores por ano | `year` |

## Guia de Uso

### Dashboard Principal

1. Acesse `http://localhost:3000`
2. Visualize os quatro painéis analíticos
3. Use a busca por ano para filtrar vencedores específicos

### Lista de Filmes

1. Navegue para `/movies` através da sidebar
2. Use os filtros disponíveis:
   - Digite um ano específico no campo de ano
   - Selecione o status (Vencedor/Não Vencedor/Todos)
3. Navegue entre as páginas usando os controles de paginação
4. Visualize os detalhes completos de cada filme

### Responsividade

A aplicação se adapta automaticamente para diferentes tamanhos de tela:

- **Desktop** (>1024px): Layout completo com sidebar fixa
- **Tablet** (768px-1024px): Grid adaptado com sidebar colapsável  
- **Mobile** (<768px): Layout vertical com navegação em drawer

## Performance

### Otimizações implementadas

- React.memo para prevenir re-renders desnecessários
- useMemo para cache de cálculos pesados
- useCallback para estabilização de funções
- Bundle otimizado com code splitting
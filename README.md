# Calculadora de Contas Compartilhadas

> Divida contas de forma justa e proporcional à renda de cada participante.

![React](https://img.shields.io/badge/React-18.2-blue?logo=react)
![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-blue?logo=spring)


## Sobre o Projeto

Aplicação fullstack para calcular e dividir despesas compartilhadas de forma proporcional ao salário de cada participante. Ideal para casais, colegas de apartamento ou grupos que dividem custos mensais.

### Funcionalidades

- **Divisão Proporcional**: Calcula automaticamente quanto cada pessoa deve pagar baseado na renda
- **Múltiplos Participantes**: Adicione quantas pessoas forem necessárias
- **Reserva Financeira**: Configure percentual de reserva individual para cada participante
- **Gestão de Contas**: Adicione, edite e remova contas compartilhadas
- **Cálculo Automático**: Atualização em tempo real de todos os valores
- **Exportação PNG**: Gere imagem do resumo para compartilhar
- **Tema Escuro**: Interface adaptável para modo claro e escuro
- **Persistência Local**: Dados salvos automaticamente no navegador

## Motivação

Este projeto foi desenvolvido como:
- Base de estudos para aprofundamento em **React + TypeScript**
- Base de estudos para aprofundamento em **Java + Spring Boot**
- Desenvolvimento guiado por testes (TDD) em Java
- Prática de arquitetura escalável e modular
- Solução real para um problema pessoal de divisão de despesas

## Tecnologias Utilizadas

### Frontend
- **React 18.2** - Biblioteca UI
- **Context API** - Gerenciamento de estado global
- **Tailwind CSS 3.4** - Framework CSS utility-first

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.2** - Framework web
- **Spring Data JPA & H2** - Camada de acesso e Banco de dados relacional
- **JUnit 5** - Framework de testes


## Como Executar

### Pré-requisitos
- Node.js 18+ instalado
- Java 21+ instalado
- Maven 3.6+ instalado
- npm ou yarn

### Instalação

```bash
# Clone o repositório
git clone

#Passo 1: Frontend
# Entre na pasta
cd front-end

# Instale as dependências
npm install

# Execute o projeto
npm run dev

#Passo 2: Backend
#Entre na pasta
cd back-end

# Execute os Testes Unitários (TDD)
./mvnw test

# Inicie o Servidor da API
./mvnw spring-boot:run
```

Acesse: `http://localhost:5173`

## 📚 Documentação da API (Swagger)

A API do Calc-Contas é autodocumentada ativamente pela especificação **Springdoc OpenAPI 3**.
Para consultar Mocks, testar Endpoints interativamente e ver as proteções de Modelos (DTOs) sem necessitar de Postman:

1. Suba a aplicação Spring Boot localmente na porta 8080.
2. Acesse seu navegador na rota oficial: `http://localhost:8080/swagger-ui/index.html`

### Endpoints RESTful 
O Domínio do serviço é protegido por regras estritas de CORS, Null-Validation e Partial Updates:
- **`GET`, `POST`, `PATCH`, `DELETE` -> `/api/v1/person`**: Gestão dos participantes, incluindo salário base e reserva.
- **`GET`, `POST`, `PATCH`, `DELETE` -> `/api/v1/bill`**: Gestão das contas universais do mês.
- **`GET` -> `/api/v1/calculation`**: Endpoint motor da aplicação. Retorna o resumo completo, distribuições proporcionais e cálculos de reserva em uma única chamada.

### Padronização de Erros
Todas as exceções da API são capturadas globalmente e retornam um JSON estruturado para facilitar o tratamento no Frontend:

```json
{
  "timestamp": "2024-05-05T10:00:00Z",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Mensagem detalhada do erro",
  "path": "/api/v1/endpoint"
}
```

## Estrutura do Projeto

```text
front-end/
├── src/
│   ├── components/         # Componentes React
│   │   ├── bills/          # Componentes de contas
│   │   ├── people/         # Componentes de pessoas
│   │   ├── ImageGenerator  # Gerador de imagem PNG
│   │   ├── Summary         # Resumo final
│   │   └── ThemeToggle     # Toggle de tema
│   ├── context/            # Context API (estado global)
│   │   ├── AppContext      # Estado da aplicação
│   │   └── ThemeContext    # Estado do tema
│   ├── services/           # Lógica de negócio
│   │   └── calculation     # Serviço de cálculos
│   ├── types/              # Definições TypeScript
│   │   ├── Bill            # Tipos de contas
│   │   └── Person          # Tipos de pessoas
│   ├── App.tsx             # Componente raiz
│   ├── main.tsx            # Entry point
│   └── index.css           # Estilos globais

back-end/
├── src/
│   ├── main/java/com/pms/calprop/
│   │   ├── controllers/    # Controladores REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entities/       # Entidades JPA
│   │   ├── exceptions/     # Exceções Customizadas da API
│   │   ├── repositories/   # Repositórios JPA
│   │   ├── services/       # Serviços de negócio
│   │   └── CalpropApplication.java # Aplicação Spring Boot
│   └── test/java/...       # Suíte de Testes Unitários (Mockito/JUnit)
└── pom.xml                 # Gerenciador de Dependências Maven
```

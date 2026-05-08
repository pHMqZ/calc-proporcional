# Calculadora de Contas Compartilhadas

> Divida contas de forma justa e proporcional à renda de cada participante.

![React](https://img.shields.io/badge/React-19.0-blue?logo=react)
![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-blue?logo=spring)
![Playwright](https://img.shields.io/badge/Playwright-E2E-green?logo=playwright)
![Vitest](https://img.shields.io/badge/Vitest-Testing-green?logo=vitest)

## Sobre o Projeto

Aplicação fullstack para calcular e dividir despesas compartilhadas de forma proporcional ao salário de cada participante. Ideal para casais, colegas de apartamento ou grupos que dividem custos mensais.

### Funcionalidades

- **Divisão Proporcional**: Calcula automaticamente quanto cada pessoa deve pagar baseado na renda
- **Múltiplos Participantes**: Adicione quantas pessoas forem necessárias
- **Reserva Financeira**: Configure percentual de reserva individual para cada participante
- **Gestão de Contas**: Adicione, edite e remova contas compartilhadas
- **Cálculo Automático**: Toda a lógica de rateio é processada via backend
- **Exportação PNG**: Gere imagem do resumo para compartilhamento
- **Tema Escuro**: Interface adaptável (Modo Dark/Light)
- **Persistência em Banco**: Dados armazenados via API REST em banco de dados H2

---

## 🛠️ Tecnologias Utilizadas

### Frontend
- **React 19** com **TypeScript**
- **Vitest + Happy-DOM**: Suíte de testes unitários e integração
- **Playwright**: Testes End-to-End (E2E) com padrão Page Object Model (POM)
- **pure-gen**: Geração de massa de dados randômica para testes
- **Tailwind CSS**: Design premium com suporte a Dark Mode
- **Context API**: Gerenciamento de estado global e sincronização com API

### Backend
- **Java 21** & **Spring Boot 3.5.3**
- **Spring Data JPA & H2**: Persistência de dados
- **JUnit 5 & Mockito**: Testes de validação e lógica de negócio
- **MapStruct**: Mapeamento de DTOs e entidades

---

## 🚀 Como Executar

### Pré-requisitos
- Node.js 20+
- Java 21+
- Maven 3.6+

### Instalação e Execução

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/calc-contas.git

# Passo 1: Backend (API)
cd back-end
./mvnw spring-boot:run

# Passo 2: Frontend (Web)
cd ../front-end
npm install
npm run dev
```

Acesse: `http://localhost:5173`

---

## 🧪 Suíte de Testes e Qualidade

O projeto possui uma pirâmide de testes completa, garantindo a integridade desde a unidade até o fluxo do usuário final.

### 1. Testes de Unidade e Integração
```bash
# Backend (JUnit)
cd back-end
./mvnw test

# Frontend (Vitest)
cd front-end
npm test
```

### 2. Testes End-to-End (E2E)
Os testes E2E validam fluxos completos (ex: adicionar pessoa -> adicionar conta -> validar resumo) simulando o comportamento real do usuário no navegador.

```bash
# Executar todos os testes E2E (Front + Back devem estar rodando)
cd front-end
npm run test:e2e

# Abrir interface visual do Playwright
npx playwright test --ui
```

### CI/CD (GitHub Actions)
O projeto conta com uma esteira de integração contínua que valida automaticamente todos os **Pull Requests** para as branches `uat` e `main`. O merge só é permitido se todos os testes passarem.

---

## 📂 Estrutura do Projeto

```text
front-end/
├── src/
│   ├── components/    # Componentes UI (Bills, People, Summary, etc)
│   ├── e2e-tests/     # Testes End-to-End (Playwright)
│   │   ├── pages/     # Page Object Model (POM)
│   │   ├── steps/     # Especificações de testes (.spec.ts)
│   │   └── utils/     # Geradores de massa (pure-gen)
│   ├── context/       # Providers (App e Theme)
│   ├── services/      # Integração com API (Axios)
│   ├── tests/         # Testes Unitários e de Integração (Vitest)
│   └── types/         # Interfaces TypeScript

back-end/
├── src/
│   ├── main/java/...  # Camadas Controller, Service, DTO, Repository
│   └── test/java/...  # Testes de Validação e Unidade (JUnit)
```

---

## 📚 Documentação da API (Swagger)

Com o backend rodando, acesse a documentação interativa:
`http://localhost:8080/swagger-ui/index.html`

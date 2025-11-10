# Calculadora de Contas Compartilhadas

> Divida contas de forma justa e proporcional à renda de cada participante.

![React](https://img.shields.io/badge/React-18.2-blue?logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-5.2-blue?logo=typescript)
![Tailwind CSS](https://img.shields.io/badge/Tailwind-3.4-38bdf8?logo=tailwindcss)
![Vite](https://img.shields.io/badge/Vite-5.0-646cff?logo=vite)

## Sobre o Projeto

Uma aplicação web para calcular e dividir despesas compartilhadas de forma proporcional ao salário de cada participante. Ideal para casais, colegas de apartamento ou grupos que dividem custos mensais.

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
- Prática de arquitetura escalável e modular
- Solução real para um problema pessoal de divisão de despesas

## Tecnologias Utilizadas

### Core
- **React 18.2** - Biblioteca UI
- **TypeScript 5.2** - Tipagem estática
- **Vite 5.0** - Build tool e dev server

### Styling
- **Tailwind CSS 3.4** - Framework CSS utility-first
- **CSS Custom Properties** - Variáveis e temas

### Arquitetura
- **Context API** - Gerenciamento de estado global
- **Custom Hooks** - Lógica reutilizável
- **Service Layer** - Separação de lógica de negócio

## Como Executar

### Pré-requisitos
- Node.js 18+ instalado
- npm ou yarn

### Instalação

```bash
# Clone o repositório
git clone

# Entre na pasta
cd calculadora-contas

# Instale as dependências
npm install

# Execute o projeto
npm run dev
```

Acesse: `http://localhost:5173`

## Estrutura do Projeto

```
src/
├── components/          # Componentes React
│   ├── bills/          # Componentes de contas
│   ├── people/         # Componentes de pessoas
│   ├── ImageGenerator  # Gerador de imagem PNG
│   ├── Summary         # Resumo final
│   └── ThemeToggle     # Toggle de tema
├── context/            # Context API (estado global)
│   ├── AppContext      # Estado da aplicação
│   └── ThemeContext    # Estado do tema
├── services/           # Lógica de negócio
│   └── calculation     # Serviço de cálculos
├── types/              # Definições TypeScript
│   ├── Bill           # Tipos de contas
│   └── Person         # Tipos de pessoas
├── App.tsx            # Componente raiz
├── main.tsx           # Entry point
└── index.css          # Estilos globais
```

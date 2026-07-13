# Finance Ecosystem - SaaS de Gestão Financeira

Ecossistema financeiro completo e moderno construído sob os princípios de **Clean Architecture** no back-end e uma experiência de usuário rica no front-end, operando de forma híbrida como aplicação **Web** e **Desktop**.

## Tecnologias Utilizadas

- **Back-end:** Java 17, Spring Boot, Spring Data JPA, Spring Security, JWT, JasperReports (PDFs Executivos).
- **Front-end Web:** React 19, Vite, TailwindCSS, Axios.
- **Desktop Client:** Electron, TypeScript, Concurrently.
- **DevOps:** Docker, Docker Compose, PostgreSQL.

---

## Como Rodar a Aplicação com Docker (Web + API + Banco)

A forma mais rápida de ver a aplicação Web no ar é utilizando o Docker Compose. Ele configurará automaticamente o Banco de Dados, a API Rest e o Servidor Web do Frontend.

1. Certifique-se de ter o **Docker** instalado na sua máquina.
2. Na raiz do projeto, execute o comando:
   ```bash
   docker-compose up --build
Acesse o sistema pelo navegador em: http://localhost

---

## Como Rodar em Modo de Desenvolvimento (Nativo + App Desktop)
Caso queira rodar os serviços localmente ou abrir a versão Desktop (Electron):

Pré-requisitos
Java 17+ instalado

Node.js 20+ instalado

PostgreSQL rodando localmente na porta 5432 (ou use o container do banco)

1. Inicializando o Back-end
Vá até a pasta do back-end: cd finance-backend

Certifique-se de ajustar o arquivo src/main/resources/application.properties com as credenciais do seu banco de dados local.

Inicie o Spring Boot:

Bash
./mvnw spring-boot:run
2. Inicializando o Front-end & App Desktop
Vá até a pasta do front-end: cd finance-frontend

Instale as dependências do Node:

Bash
npm install
Para rodar a versão Web no navegador:

Bash
npm run dev:web
Para rodar a versão Desktop Nativa (Electron):

Bash
npm run dev:desktop
Funcionalidades Implementadas
[x] Autenticação Segura via JWT (com persistência de sessão).

[x] CRUD de Categorias customizáveis com paleta de cores e ícones.

[x] Lançamento de Transações Dinâmicas (Faturamento e Despesas).

[x] Dashboard Analítico com Cards de KPIs e histórico em tempo real.

[x] Emissão de Relatório Financeiro Executivo em PDF via JasperReports, contendo balanço consolidado e indicadores de fluxo.

[x] Cliente Desktop multiplataforma nativo via Electron com interface imersiva.


---

## Autor

Willian Ferreira Gonçalves
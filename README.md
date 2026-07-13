# Finance Ecosystem - SaaS de Gestão Financeira

O Finance Ecosystem é uma plataforma completa e robusta de gerenciamento e controle financeiro pessoal. O ecossistema foi projetado sob os princípios distribuídos e modernos de Clean Architecture, integrando uma API REST em Spring Boot altamente segura, um banco de dados relacional isolado, um sistema de cache em memória para performance e um painel web SPA responsivo em React servido via Nginx.

Opera de forma híbrida como aplicação **Web** e **Desktop**.

---

## Tecnologias

**RESUMO:**
- **Back-end:** Java, Spring Boot, Spring Data JPA, Spring Security, JWT, JasperReports (PDFs Executivos).
- **Front-end Web:** React 19, Vite, TailwindCSS, Axios.
- **Desktop Client:** Electron, TypeScript, Concurrently.
- **DevOps:** Docker, Docker Compose, PostgreSQL.

**DETALHAMENTO:**
- **React (v19+) & Vite:** Interface web SPA declarativa, componentizada e com tempo de compilação ultrarápido.
- **Tailwind CSS:** Framework utilitário para estilização responsiva e customização fluida de temas.
- **Spring Boot 4**: Core do backend, utilizando a versão estável mais recente da plataforma Java para APIs escaláveis.
- **Spring Security & JWT:** Controle rígido de autenticação sem estado (stateless) e autorização baseada em Tokens Bearer.
- **PostgreSQL 16:** Banco de dados relacional de produção para persistência segura e integridade dos dados financeiros.
- **Redis 7:** Camada de cache em memória de altíssima velocidade para otimização de consultas pesadas e relatórios.
- **Docker & Docker Compose:** Containerização completa e orquestração de toda a infraestrutura de microsserviços.

---

## Arquitetura e Recursos do Sistema

Diferente de sistemas simplificados, este ecossistema foca em conceitos rigorosos de engenharia de software e segurança de ponta a ponta:
1. **Arquitetura Descentralizada:** Separação estrita entre a camada de apresentação e a camada de negócios/dados.
2. **Segurança JWT Avançada:** Fluxo de autenticação blindado com filtros customizados (JwtAuthenticationFilter) injetados diretamente na Security Filter Chain do Spring.
3. **Gerenciamento de Cache:** Implementação de Time-To-Live (TTL) de 10 minutos via Redis para evitar requisições redundantes ao banco de dados em relatórios recorrentes.
4. **Isolamento de Credenciais:** Arquitetura orientada a ambientes seguros, onde dados sensíveis e credenciais de infraestrutura são extraídos para variáveis locais (.env), nunca sendo expostos no código-fonte.
5. **Comunicação Segura de Redes:** Orquestração via redes virtuais do Docker (bridge) e políticas estritas de CORS controladas pelo Spring Security para as origens de produção.

---

## Pré-requisitos
- Java 17+ instalado
- Node.js 20+ instalado
- PostgreSQL rodando localmente na porta 5432 (ou use o container do banco)

---

## Como Executar o Projeto

### Opção 1: Ambiente Local (Desenvolvimento Híbrido)

Certifique-se de ter o Node.js, Java 21 (JDK) e o Docker instalados na máquina para rodar os bancos.

1. Clone o repositório completo
   ```bash
   git clone https://github.com/seu-usuario/finance-ecosystem.git
   cd finance-ecosystem

2. Suba apenas os bancos de dados em segundo plano
   ```bash
   docker-compose up -d postgres redis

3. Inicie o Backend (IntelliJ ou Terminal)
   - No IntelliJ, abra as configurações de inicialização e adicione as variáveis contidas no .env da raiz (DB_NAME, DB_USER, DB_PASSWORD).
   - Ou execute via terminal na pasta do backend:
   ```bash
   cd finance-backend
   ./mvnw spring-boot:run

4. Inicie o Frontend
   - Em uma nova janela de terminal, navegue até a pasta do frontend e inicie o servidor do Vite:
   ```bash
   cd finance-frontend
   npm install
   npm run dev

### Opção 2: Via Docker Compose (Orquestração de Produção)

Para rodar todo o ecossistema de forma isolada, limpa e idêntica ao servidor de produção, o projeto utiliza o Docker Compose integrado ao arquivo .env da raiz.

O ecossistema é inicializado sob um controle rígido de dependências: o banco PostgreSQL inicia primeiro, valida sua própria saúde estrutural (healthcheck) e só então libera o início do servidor Spring Boot, evitando falhas de inicialização por falha de conexão.

1. Certifique-se de que o seu arquivo .env está configurado corretamente na raiz do projeto.
2. Execute o comando de construção e inicialização total:
   ```bash
   docker-compose up --build

3. O Docker irá orquestrar os containers nas seguintes portas locais:
   - Frontend Web: **`http://localhost`** (Porta 80 padrão)
   - Backend API: **`http://localhost:8080`**
   - PostgreSQL: **`localhost:5432`**
   - Redis: **`localhost:6379`**

**ATENÇÃO:** Caso você já possua serviços rodando localmente nas portas 8080 ou 5432 (como instâncias nativas de Tomcat, Jenkins ou outro servidor Postgres local), certifique-se de pausá-los antes de rodar o comando, garantindo que os containers consigam expor suas portas físicas sem conflitos no sistema operacional.

---

## Autor

Willian Ferreira Gonçalves

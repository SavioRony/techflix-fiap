# Arquitetura do Projeto - Tech Flix

Para atender aos requisitos propostos no desenvolvimento da aplicação web de streaming de vídeos, a arquitetura do projeto será baseada nos princípios da Clean Architecture, utilizando as tecnologias Spring Framework, Spring WebFlux, Spring Boot e Spring Data.

## Clean Architecture

A Clean Architecture prioriza a separação do projeto em camadas, cada uma com uma responsabilidade específica. Neste projeto, a separação foi realizada da seguinte forma:

### Camada de Entidades (Entities)

- **domain → entity:** Classes de entidade que representam conceitos do domínio da aplicação. Elas não possuem ligações com frameworks ou detalhes de implementação.

### Casos de Uso (Use Cases)

- **application → usecases:** Classes que implementam as regras de negócio da aplicação. Elas dependem apenas das entidades do domínio e não possuem detalhes de implementação ou frameworks.

- **application → gateways:** Interfaces que definem contratos para a comunicação entre as camadas. Funcionam como portas de entrada e saída para operações externas, como persistência.

### Adaptadores de Interface (Interface Adapters)

#### Implementações

- **infrastructure → gateways:** Implementações das interfaces definidas nos casos de uso. Podem incluir mapeamentos entre formatos de dados e chamadas a serviços externos.

    - **Repository gateway:** Implementa as operações definidas nos casos de uso para persistência de dados.

    - **Mapper:** Realiza a conversão entre as entidades de domínio e os modelos utilizados nos adaptadores externos.

- **infrastructure → persistence:** Implementações específicas para a persistência de dados, como bancos de dados ou sistemas de arquivos.

    - **Repository:** Implementa as operações de persistência definidas nos casos de uso.

    - **Entidade:** Representa a estrutura da tabela no banco de dados.

### Frameworks e Drivers (Frameworks and Drivers)

- **infrastructure → controllers:** Responsável por receber e enviar dados da e para a interface do usuário. Pode depender das implementações dos casos de uso e adaptadores.

    - **Controller:** Recebe requisições HTTP, interage com os casos de uso e retorna as respostas adequadas.

    - **DTO:** Classes de Transferência de Dados que representam objetos simples usados na comunicação entre a interface do usuário e os casos de uso.

    - **Mapper:** Realiza a conversão entre entidades de domínio e DTOs.

- **main:** Onde a aplicação é inicializada e configurada. Pode depender das implementações dos casos de uso e adaptadores.

    - **Configuração:** Define como os componentes do sistema são instanciados e configurados.

A separação em camadas permite que a aplicação seja altamente testável, facilitando a manutenção e evolução do sistema.

## Tecnologias Utilizadas

1. **Spring WebFlux:** Utilizado para criar endpoints reativos, permitindo que a aplicação lide com um grande número de requisições simultâneas.

2. **Spring Boot:** Responsável pela configuração e inicialização da aplicação. Facilita a configuração e criação de projetos Spring.

3. **Spring Data:** Utilizado para a camada de persistência, com suporte a bancos de dados reativos, como o MongoDB.

## Requisitos Funcionais

- **Criação, Atualização, Listagem e Exclusão de Vídeos:** Implementados nos controllers, que utilizam os serviços para realizar as operações.

- **Campos dos Vídeos:** Título, descrição, URL e data de publicação.

- **Listagem Paginada e Ordenável:** Implementada nos controllers, utilizando os serviços para buscar os vídeos no formato desejado.

- **Filtros de Busca:** Implementados nos controllers, utilizando os serviços para realizar as consultas filtradas.

- **Marcação de Vídeos como Favoritos:** Implementado nos controllers, usando os serviços e casos de uso relacionados.

- **Categorias para Vídeos:** Implementadas nos controllers e serviços, permitindo a associação de categorias aos vídeos e filtragem por categoria.

- **Sistema de Recomendação:** Implementado nos controllers e serviços, utilizando informações de vídeos marcados como favoritos.

- **Endpoint para Estatísticas:** Implementado nos controllers, utilizando os serviços para calcular e retornar as estatísticas desejadas.

## Requisitos Técnicos

- **Spring WebFlux, Boot e Data:** Utilizados conforme requisitos.

- **Arquitetura Clean Architecture:** Implementada com a separação de controllers, services, use cases e repositories.

- **Testes Unitários e de Integração:** Implementados para garantir a qualidade do código em todas as camadas da aplicação.

- **Validações de Entrada:** Implementadas nos endpoints para garantir a integridade dos dados recebidos.

- **Gerenciamento de Dependências:** Utilizado Maven ou Gradle para gerenciar as dependências do projeto.

# Guia de Uso - Tech Flix
Este é um guia de uso para a aplicação Tech Flix, uma plataforma para cadastrar, buscar e visualizar vídeos. Siga as instruções abaixo para começar:

## Configuração do Ambiente
Certifique-se de ter o Docker e o Docker Compose instalados em seu sistema. Em seguida, crie um arquivo chamado docker-compose.yml com o seguinte conteúdo:
```
version: '3'

services:
  sonarqube:
    image: sonarqube:latest
    container_name: sonarqube
    environment:
      - SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true
    ports:
      - "9000:9000"

  mongo:
    image: mongodb/mongodb-community-server:latest
    container_name: mongo
    ports:
      - "27017:27017"
```
Execute o comando abaixo para iniciar os serviços do MongoDB e do SonarQube:
```bash
docker-compose up -d
```
## Iniciando a Aplicação
Clone este repositório em seu ambiente local:
```bash
git clone https://seu-repositorio.git
cd tech-flix
```
Execute o projeto e acesse o link:  ``http://localhost:8080``

### Cadastro de Vídeos
1. Na seção **Cadastro de Vídeos**, preencha os campos:
    - **Título**: Insira o título do vídeo.
    - **Categoria**: Selecione a categoria do vídeo.
    - **Escolher arquivo**: Selecione um arquivo de vídeo para upload.
2. Clique no botão **Enviar** para cadastrar o vídeo.

### Filtros
1. **Buscar por Título**:
    - Preencha o campo **Título** no formulário **Buscar por Título**.


- Clique no botão **Buscar**.
2. **Buscar por Categoria**:
    - Selecione a categoria desejada no formulário **Buscar por Categoria**.
    - Clique no botão **Buscar**.
3. **Buscar por Data**:
    - Preencha os campos **Data de Início** e **Data de Fim** no formulário **Buscar por Data**.
    - Clique no botão **Buscar**.
4. **Recomendação de Vídeos**:
    - Clique no botão **Carregar Recomendados** para obter sugestões de vídeos.
### Lista de Vídeos
- A lista de vídeos será exibida na seção **Lista de Vídeos**.
### Marcar Vídeo como Favorito
- Ao lado do vídeo desejado, clique no ícone de coração para marcá-lo como favorito que será usado como base na recomendação dos vídeos.
### Atualização de Vídeo
1. Clique no botão **Atualizar** ao lado do vídeo desejado.
2. No modal de atualização, preencha os campos:
    - **Novo Título**: Insira o novo título do vídeo.
    - **Nova Categoria**: Selecione a nova categoria do vídeo.
3. Clique no botão **Atualizar** para salvar as alterações.

### Exclusão de Vídeo
1. Clique no botão **Excluir** ao lado do vídeo desejado.
2. Confirme a exclusão no modal de confirmação.

### Estatísticas
- Para visualizar as estatísticas, clique no botão Ver Estatísticas.
- Na página de estatísticas, você verá informações sobre a quantidade total de vídeos, vídeos favoritados e a média de visualizações.

### Encerrando o Ambiente
- Para encerrar os serviços, execute o seguinte comando no diretório do docker-compose.yml:

```bash
docker-compose down
```
Agora você está pronto para explorar a plataforma Tech Flix. Divirta-se!

# Roda e configura sonar local

Iniciar o servidor:
```bash
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest
```
Assim que sua instância estiver instalada e funcionando, faça login em http://localhost:9000 usando credenciais de administrador do sistema
```
login: admin
password: admin
```
Após acessar, crie um projeto e gere um token local conforme esse artigo:
- https://www.linkedin.com/pulse/qualidade-do-c%C3%B3digo-com-sonarqube-e-docker-tiago-perroni/?originalSubdomain=pt

Agora basta abrir o terminal e executar o comando fornecido no sonar como este exemplo:
```bash
mvn clean verify sonar:sonar   -Dsonar.projectKey=TECHFLIX   -Dsonar.projectName='TECHFLIX'   -Dsonar.host.url=http://localhost:9000   -Dsonar.token=sqp_b224e35b15ccf47bdf0d3fa9453daa2e8a876a3c
```

## Referências
- https://www.baeldung.com/sonarqube-jacoco-code-coverage
- https://www.linkedin.com/pulse/qualidade-do-c%C3%B3digo-com-sonarqube-e-docker-tiago-perroni/?originalSubdomain=pt
- https://docs.sonarsource.com/sonarqube/latest/try-out-sonarqube/
- https://www.mongodb.com/docs/manual/tutorial/install-mongodb-community-with-docker/
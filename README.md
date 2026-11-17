# Loja Web — Java EE 8 + JDBC

Loja Web (Servlets, HttpSession, JSP/JSTL) agora com **persistência em banco de dados** (JDBC + DAO) no lugar das listas em memória.

## Banco de dados

- Padrão: **H2 em modo arquivo** (nada para instalar). O arquivo é criado em `<pasta do usuário>/loja-web-db.mv.db`.
- Para usar **PostgreSQL** (driver já incluído) ou MySQL, edite `src/main/resources/db.properties`.
- Na subida da aplicação, `InicializadorBanco` cria as tabelas `usuario` e `produto` (se não existirem) e insere os dados iniciais (referência em `src/main/resources/schema.sql`).
- Senhas são gravadas como hash SHA-256.

### Usuários de teste
| E-mail | Senha |
|---|---|
| carlos.lima@email.com | carlos123 |
| ana.santos@email.com | ana123 |

## Estrutura

- `model/` — Produto, Usuario, ItemCarrinho
- `dao/` — `ConexaoBD`, `ProdutoDAO`, `UsuarioDAO` (JDBC com PreparedStatement)
- `listener/` — `InicializadorBanco`
- `servlet/` — Login, Logout, Produto, Carrinho, CarrinhoItem

## Endpoints

- GET `/produtos` (produtos lidos do banco)
- GET `/carrinho`
- POST `/carrinho/itens` (parâmetro `id`; valida o produto no banco)
- DELETE `/carrinho/itens?id={id}`
- DELETE `/carrinho`
- GET/POST `/login`, POST `/logout`

O carrinho continua em `HttpSession` (atributo `carrinho`), sem scriptlets e sem `static` para carrinhos.

## Executar

Requisitos: JDK 21 e **Apache Tomcat 9** (namespace `javax.*`).

```
mvn clean package
```
Copie `target/loja-web-sessao.war` para `webapps/` do Tomcat 9 e acesse `http://localhost:8080/loja-web-sessao/`.

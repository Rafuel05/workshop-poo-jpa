# Workshop de Persistência Orientada a Objetos com JPA

Este repositório contém o material e o passo a passo para um workshop prático sobre Persistência Orientada a Objetos utilizando JPA. Cada branch representa um passo no desenvolvimento do projeto.

---

## Branch 1 - Criação do Projeto

Esta branch inicial foca na configuração do ambiente e na criação do projeto base.

### O que deve ser feito nesta etapa:

1.  **Criação do Banco de Dados PostgreSQL:**
    * Abra seu cliente PostgreSQL (psql, pgAdmin, DBeaver, etc.).
    * Execute o seguinte comando SQL para criar o banco de dados:

        ```sql
        CREATE DATABASE bdprodutos;
        ```

    * Certifique-se de que o banco de dados `bdprodutos` foi criado com sucesso.

2.  **Criação do Projeto Java/Maven:**
    * Utilize sua IDE (IntelliJ IDEA, Eclipse, VS Code com extensions para Java, etc.) ou a linha de comando para criar um novo projeto Maven.
    * Configure o projeto com as seguintes definições:
        * **`groupId`**: `ifmt.cba`
        * **`artifactId`**: `introducao-jpa`
        * **`version`**: `1.0-SNAPSHOT`

    * Se estiver usando a linha de comando, você pode usar o seguinte comando Maven:

        ```bash
        mvn archetype:generate \
            -DgroupId=ifmt.cba \
            -DartifactId=introducao-jpa \
            -DarchetypeArtifactId=maven-archetype-quickstart \
            -DarchetypeVersion=1.4 \
            -DinteractiveMode=false
        ```

    * Após a criação, navegue até o diretório `introducao-jpa` e verifique a estrutura básica do projeto Maven.

    ## Branch 2 - Unidade de Persistência

Nesta branch, configuraremos a unidade de persistência do JPA, que é fundamental para que o Hibernate (ou outro provedor JPA) saiba como se conectar ao banco de dados e gerenciar as entidades.

### O que deve ser feito nesta etapa:

1.  **Criação do diretório `META-INF`:**
    * Dentro do diretório `src/main/resources` do seu projeto Maven, crie um novo diretório chamado `META-INF`. A estrutura deve ficar assim: `src/main/resources/META-INF`.

2.  **Criação do arquivo `persistence.xml`:**
    * Dentro do diretório `src/main/resources/META-INF`, crie um novo arquivo chamado `persistence.xml`.
    * Cole o seguinte conteúdo neste arquivo:

        ```xml
        <?xml version="1.0" encoding="UTF-8"?>
        <persistence xmlns="[http://xmlns.jcp.org/xml/ns/persistence](http://xmlns.jcp.org/xml/ns/persistence)"
                     xmlns:xsi="[http://www.w3.org/2001/XMLSchema-instance](http://www.w3.org/2001/XMLSchema-instance)"
                     xsi:schemaLocation="[http://xmlns.jcp.org/xml/ns/persistence](http://xmlns.jcp.org/xml/ns/persistence) [http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd](http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd)"
                     version="2.1">

            <persistence-unit name="UnidadeProdutos" transaction-type="RESOURCE_LOCAL">
                <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
                <exclude-unlisted-classes>true</exclude-unlisted-classes>
                <properties>
                    <property name="javax.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/bdprodutos" />
                    <property name="javax.persistence.jdbc.user" value="postgres" />
                    <property name="javax.persistence.jdbc.driver" value="org.postgresql.Driver" />
                    <property name="javax.persistence.jdbc.password" value="postgres" />
                    <property name="javax.persistence.schema-generation.database.action" value="create" /> <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect" />
                    <property name="hibernate.show_sql" value="true" /> <property name="hibernate.format_sql" value="true" /> </properties>
            </persistence-unit>
        </persistence>
        ```

### Entendendo o `persistence.xml`:

Este arquivo é a peça central da configuração do JPA. Vamos entender os elementos mais importantes:

* **`<persistence-unit name="UnidadeProdutos" transaction-type="RESOURCE_LOCAL">`**:
    * Define uma unidade de persistência única. O `name` ("UnidadeProdutos") é o identificador que usaremos em nosso código Java para referenciá-la.
    * `transaction-type="RESOURCE_LOCAL"`: Indica que a transação será gerenciada diretamente pela aplicação (não por um servidor de aplicação Java EE).

* **`<provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>`**:
    * Especifica qual implementação do JPA será utilizada. Neste caso, estamos usando o Hibernate como provedor.

* **`<exclude-unlisted-classes>true</exclude-unlisted-classes>`**:
    * Com `true`, o JPA só persistirá as classes que forem explicitamente listadas no `persistence.xml` (geralmente dentro de `<class>`) ou aquelas que forem automaticamente detectadas por anotações em frameworks (como Spring Boot). No nosso caso, como não listaremos explicitamente, ele buscará as classes anotadas.

* **`<properties>`**:
    * Contém todas as propriedades de configuração para o provedor JPA e o banco de dados.

    * **Propriedades JDBC (`javax.persistence.jdbc.*`)**:
        * `javax.persistence.jdbc.url`: A URL de conexão com o banco de dados PostgreSQL.
            * `jdbc:postgresql://localhost:5432/bdprodutos`: Indica que estamos conectando a um banco PostgreSQL rodando localmente (localhost) na porta 5432, e o banco de dados se chama `bdprodutos`.
        * `javax.persistence.jdbc.user`: O nome de usuário para acessar o banco de dados (neste caso, `postgres`).
        * `javax.persistence.jdbc.driver`: A classe do driver JDBC para PostgreSQL (`org.postgresql.Driver`).
        * `javax.persistence.jdbc.password`: A senha do usuário do banco de dados (neste caso, `postgres`).

    * **Propriedade de Geração de Schema (`javax.persistence.schema-generation.database.action`)**:
        * `value="none"`: Esta propriedade controla como o JPA interage com o schema do banco de dados.
            * `none`: Não faz nenhuma alteração no schema. (Recomendado para produção).
            * `create`: Cria o schema do banco de dados a cada inicialização da aplicação, deletando o anterior se existir. (Bom para testes e desenvolvimento inicial).
            * `create-drop`: Cria o schema na inicialização e o remove no desligamento da aplicação. (Excelente para testes unitários).
            * `update`: Tenta atualizar o schema existente para refletir as mudanças nas entidades. (Pode ser arriscado em produção).

    * **Propriedades Específicas do Hibernate (`hibernate.*`)**:
        * `hibernate.dialect`: Informa ao Hibernate qual dialeto SQL usar para o banco de dados específico (PostgreSQL, neste caso). Isso permite que o Hibernate gere SQL otimizado para o seu banco.
        * `hibernate.show_sql`: Se `true`, o Hibernate irá imprimir todas as instruções SQL geradas no console. Muito útil para depuração.
        * `hibernate.format_sql`: Se `true` e `hibernate.show_sql` também for `true`, o SQL impresso será formatado de forma legível.
    
    ## Branch 3 - Mapeando Entidade

Nesta branch, adicionaremos as dependências necessárias no `pom.xml`, criaremos a primeira entidade `GrupoProdutoVO` e a registraremos na unidade de persistência para que o Hibernate possa mapeá-la para o banco de dados.

### O que deve ser feito nesta etapa:

1.  **Adicionar Dependências no `pom.xml`:**
    * Abra o arquivo `pom.xml` na raiz do seu projeto.
    * Dentro da tag `<project>`, adicione a tag `<dependencies>` se ela ainda não existir.
    * Adicione as seguintes dependências dentro da tag `<dependencies>`:

        ```xml
        <dependencies>
            <dependency>
                <groupId>org.postgresql</groupId>
                <artifactId>postgresql</artifactId>
                <version>42.6.0</version>
            </dependency>

            <dependency>
                <groupId>org.hibernate.orm</groupId>
                <artifactId>hibernate-core</artifactId>
                <version>6.2.0.Final</version>
            </dependency>

            <dependency>
                <groupId>jakarta.persistence</groupId>
                <artifactId>jakarta.persistence-api</artifactId>
                <version>3.1.0</version>
            </dependency>
        </dependencies>
        ```
    * **Observação:** Adicionei a dependência `jakarta.persistence-api`. Embora o `hibernate-core` traga transitivamente muitas classes do JPA, é uma boa prática declarar explicitamente a API que você está usando para as anotações.

2.  **Criar a Entidade `GrupoProdutoVO`:**
    * Dentro do diretório `src/main/java`, crie a estrutura de pacotes `ifmt.cba.VO`.
    * Crie o arquivo `GrupoProdutoVO.java` dentro desse pacote e cole o seguinte código:

        ```java
        package ifmt.cba.VO;

        import java.io.Serializable;
        import jakarta.persistence.Entity;
        import jakarta.persistence.GeneratedValue;
        import jakarta.persistence.GenerationType;
        import jakarta.persistence.Id;
        import jakarta.persistence.Table;


        @Entity
        @Table(name = "grupoproduto")
        public class GrupoProdutoVO implements Serializable {
            
            @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE) // Ou GenerationType.IDENTITY, GenerationType.AUTO
            private int codigo;
            private String nome;
            private float margemLucro;
            private float promocao;

            public int getCodigo(){
                return codigo;
            }

            public void setCodigo(int codigo){
                this.codigo = codigo;
            }

            public String getNome() {
                return nome;
            }

            public void setNome(String nome) {
                this.nome = nome;
            }

            public float getMargemLucro() {
                return margemLucro;
            }

            public void setMargemLucro(float margemLucro) {
                this.margemLucro = margemLucro;
            }

            public float getPromocao() {
                return promocao;
            }

            public void setPromocao(float promocao) {
                this.promocao = promocao;
            }

            @Override
            public String toString() {
                return this.nome+" -- "+this.codigo;
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + codigo;
                result = prime * result + ((nome == null) ? 0 : nome.hashCode());
                result = prime * result + Float.floatToIntBits(margemLucro);
                result = prime * result + Float.floatToIntBits(promocao);
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                GrupoProdutoVO other = (GrupoProdutoVO) obj;
                if (codigo != other.codigo)
                    return false;
                if (nome == null) {
                    if (other.nome != null)
                        return false;
                } else if (!nome.equals(other.nome))
                    return false;
                if (Float.floatToIntBits(margemLucro) != Float.floatToIntBits(other.margemLucro))
                    return false;
                if (Float.floatToIntBits(promocao) != Float.floatToIntBits(other.promocao))
                    return false;
                return true;
            }
        }
        ```

3.  **Atualizar o `persistence.xml`:**
    * Abra o arquivo `src/main/resources/META-INF/persistence.xml`.
    * Adicione a tag `<class>` para a `GrupoProdutoVO` dentro da sua `<persistence-unit>`.
    * **MUITO IMPORTANTE:** Altere a propriedade `javax.persistence.schema-generation.database.action` para `drop-and-create` ou `update`. Para este passo inicial, `drop-and-create` é recomendado para garantir que a tabela seja criada a cada execução, limpando dados anteriores de testes.

        ```xml
        <?xml version="1.0" encoding="UTF-8"?>
        <persistence xmlns="[http://xmlns.jcp.org/xml/ns/persistence](http://xmlns.jcp.org/xml/ns/persistence)"
                     xmlns:xsi="[http://www.w3.org/2001/XMLSchema-instance](http://www.w3.org/2001/XMLSchema-instance)"
                     xsi:schemaLocation="[http://xmlns.jcp.org/xml/ns/persistence](http://xmlns.jcp.org/xml/ns/persistence) [http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd](http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd)"
                     version="2.1">

            <persistence-unit name="UnidadeProdutos" transaction-type="RESOURCE_LOCAL">
                <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
                <exclude-unlisted-classes>true</exclude-unlisted-classes>
                
                <class>ifmt.cba.VO.GrupoProdutoVO</class> 
                
                <properties>
                    <property name="javax.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/bdprodutos" />
                    <property name="javax.persistence.jdbc.user" value="postgres" />
                    <property name="javax.persistence.jdbc.driver" value="org.postgresql.Driver" />
                    <property name="javax.persistence.jdbc.password" value="postgres" />
                    <property name="javax.persistence.schema-generation.database.action" value="drop-and-create" /> 
                    <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect" />
                    <property name="hibernate.show_sql" value="true" />
                    <property name="hibernate.format_sql" value="true" />
                </properties>
            </persistence-unit>
        </persistence>
        ```

### Entendendo a Entidade `GrupoProdutoVO`:

* **`@Entity`**: Anotação essencial que marca a classe como uma entidade JPA, indicando que ela será mapeada para uma tabela no banco de dados.
* **`@Table(name = "grupoproduto")`**: Opcional, mas altamente recomendado. Define o nome da tabela no banco de dados. Se omitido, o Hibernate usaria o nome da classe (`GrupoProdutoVO`) por padrão, o que nem sempre é o ideal.
* **`@Id`**: Marca o campo `codigo` como a chave primária da entidade. Todo objeto persistente precisa de uma chave primária.
* **`@GeneratedValue(strategy = GenerationType.SEQUENCE)`**: Configura a estratégia de geração de valores para a chave primária.
    * `GenerationType.SEQUENCE`: Utiliza uma sequência do banco de dados para gerar IDs (comum em PostgreSQL).
    * Outras opções comuns:
        * `GenerationType.IDENTITY`: Utiliza uma coluna de identidade (como `AUTO_INCREMENT` no MySQL, ou `SERIAL` no PostgreSQL).
        * `GenerationType.AUTO`: Deixa o provedor JPA escolher a melhor estratégia.
        * `GenerationType.TABLE`: Utiliza uma tabela separada para gerenciar a sequência de IDs (menos comum).
* **`Serializable`**: É uma boa prática implementar `Serializable` em entidades JPA, pois elas podem ser serializadas para diversos fins (cache, comunicação remota, etc.).
* **Métodos `getters` e `setters`**: Permitem o acesso e modificação dos atributos. O Hibernate usa esses métodos para ler e escrever os dados.
* **`equals()` e `hashCode()`**: Implementação crucial para a correta manipulação de objetos em coleções (Set, Map) e para garantir a identidade de objetos no contexto de persistência.
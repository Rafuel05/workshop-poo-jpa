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
                    <property name="javax.persistence.schema-generation.database.action" value="none" /> 
                    <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect" />
                    <property name="hibernate.show_sql" value="true" /> 
                    <property name="hibernate.format_sql" value="true" /> 
                </properties>
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
    * Com `true`, o JPA (neste caso, o Hibernate) idealmente só gerenciaria as classes que fossem explicitamente listadas no `persistence.xml` usando a tag `<class>NomeDaClasse</class>`. Se nenhuma classe for listada e esta opção for `true`, nenhuma entidade será gerenciada automaticamente apenas por estar no classpath, a menos que o provedor JPA tenha mecanismos de auto-detecção que ignorem parcialmente essa flag em certos contextos (o que o Hibernate pode fazer para classes anotadas). Para garantir que apenas as classes que queremos sejam gerenciadas, listá-las explicitamente com `<class>` é a prática mais segura quando `<exclude-unlisted-classes>` é `true`. Se `<exclude-unlisted-classes>` for `false` (ou omitido, pois `false` é o padrão para Java SE), o provedor JPA tentará descobrir todas as classes de entidade anotadas no classpath.

* **`<properties>`**:
    * Contém todas as propriedades de configuração para o provedor JPA e o banco de dados.

    * **Propriedades JDBC (`javax.persistence.jdbc.*`)**:
        * `javax.persistence.jdbc.url`: A URL de conexão com o banco de dados PostgreSQL.
            * `jdbc:postgresql://localhost:5432/bdprodutos`: Indica que estamos conectando a um banco PostgreSQL rodando localmente (localhost) na porta 5432, e o banco de dados se chama `bdprodutos`. **Ajuste a porta se necessário (ex: 5434).**
        * `javax.persistence.jdbc.user`: O nome de usuário para acessar o banco de dados (neste caso, `postgres`).
        * `javax.persistence.jdbc.driver`: A classe do driver JDBC para PostgreSQL (`org.postgresql.Driver`).
        * `javax.persistence.jdbc.password`: A senha do usuário do banco de dados (neste caso, `postgres`).

    * **Propriedade de Geração de Schema (`javax.persistence.schema-generation.database.action`)**:
        * `value="none"`: Esta propriedade controla como o JPA interage com o schema do banco de dados.
            * `none`: Não faz nenhuma alteração no schema. (Recomendado para produção ou quando o schema já existe e é gerenciado externamente).
            * `create`: Cria o schema do banco de dados a cada inicialização da `EntityManagerFactory` se ele não existir, ou pode apagar e recriar dependendo do provedor. Tabelas existentes não são geralmente afetadas se já correspondem às entidades.
            * `drop-and-create`: Apaga o schema existente e o recria na inicialização da `EntityManagerFactory`. (Bom para testes e desenvolvimento inicial, pois garante um estado limpo).
            * `update`: Tenta atualizar o schema existente para refletir as mudanças nas entidades. (Use com cautela, especialmente em produção, pois pode não lidar com todas as refatorações de schema complexas).

    * **Propriedades Específicas do Hibernate (`hibernate.*`)**:
        * `hibernate.dialect`: Informa ao Hibernate qual dialeto SQL usar para o banco de dados específico (PostgreSQL, neste caso). Isso permite que o Hibernate gere SQL otimizado para o seu banco.
        * `hibernate.show_sql`: Se `true`, o Hibernate irá imprimir todas as instruções SQL geradas no console. Muito útil para depuração.
        * `hibernate.format_sql`: Se `true` e `hibernate.show_sql` também for `true`, o SQL impresso será formatado de forma legível.
    
## Branch 3 - Mapeando Entidade e Testando a Persistência

Nesta branch, adicionaremos as dependências necessárias no `pom.xml`, criaremos a primeira entidade `GrupoProdutoVO`, a registraremos na unidade de persistência para que o Hibernate possa mapeá-la para o banco de dados e, finalmente, testaremos a configuração.

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
                <version>42.6.0</version> </dependency>

            <dependency>
                <groupId>org.hibernate.orm</groupId>
                <artifactId>hibernate-core</artifactId>
                <version>6.2.0.Final</version> </dependency>

            <dependency>
                <groupId>jakarta.persistence</groupId>
                <artifactId>jakarta.persistence-api</artifactId>
                <version>3.1.0</version>
            </dependency>
        </dependencies>
        ```
    * **Observação:** É uma boa prática declarar explicitamente a dependência `jakarta.persistence-api`, pois ela define as anotações padrão (`@Entity`, `@Id`, etc.) que você usará. O `hibernate-core` implementa esta API.

2.  **Criar a Entidade `GrupoProdutoVO`:**
    * Dentro do diretório `src/main/java`, crie a estrutura de pacotes `ifmt.cba.VO` (se `ifmt.cba` for seu `groupId`, o caminho completo seria `src/main/java/ifmt/cba/VO`).
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
        @Table(name = "grupoproduto") // Define o nome da tabela no banco
        public class GrupoProdutoVO implements Serializable {
            
            private static final long serialVersionUID = 1L; // Boa prática para Serializable

            @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE) // PostgreSQL geralmente usa SEQUENCE
            private int codigo;
            
            private String nome;
            private float margemLucro; // Em Java, float pode ter problemas de precisão para valores monetários. BigDecimal é geralmente preferido.
            private float promocao;

            // Construtor padrão (exigido pelo JPA)
            public GrupoProdutoVO() {}

            // Getters e Setters
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
                return "GrupoProdutoVO{" +
                       "codigo=" + codigo +
                       ", nome='" + nome + '\'' +
                       ", margemLucro=" + margemLucro +
                       ", promocao=" + promocao +
                       '}';
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + codigo;
                return result; // Simplificado, mas geralmente se baseia no ID.
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
                return true; // Simplificado, mas geralmente se baseia no ID.
            }
        }
        ```

3.  **Atualizar o `persistence.xml` para Incluir a Entidade:**
    * Abra o arquivo `src/main/resources/META-INF/persistence.xml`.
    * **Importante:** Adicione a tag `<class>` para a `GrupoProdutoVO` dentro da sua `<persistence-unit>`. A tag `<class>` deve vir **antes** de `<exclude-unlisted-classes>`.
    * Altere a propriedade `javax.persistence.schema-generation.database.action` para `drop-and-create`. Isso fará com que o Hibernate apague e recrie as tabelas com base nas suas entidades toda vez que a aplicação iniciar, o que é útil para desenvolvimento.

        ```xml
        <?xml version="1.0" encoding="UTF-8"?>
        <persistence xmlns="[http://xmlns.jcp.org/xml/ns/persistence](http://xmlns.jcp.org/xml/ns/persistence)"
                     xmlns:xsi="[http://www.w3.org/2001/XMLSchema-instance](http://www.w3.org/2001/XMLSchema-instance)"
                     xsi:schemaLocation="[http://xmlns.jcp.org/xml/ns/persistence](http://xmlns.jcp.org/xml/ns/persistence) [http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd](http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd)"
                     version="2.1">

            <persistence-unit name="UnidadeProdutos" transaction-type="RESOURCE_LOCAL">
                <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
                
                <class>ifmt.cba.VO.GrupoProdutoVO</class> 
                
                <exclude-unlisted-classes>true</exclude-unlisted-classes>
                
                <properties>
                    <property name="javax.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/bdprodutos" /> <property name="javax.persistence.jdbc.user" value="postgres" />
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
* **`@Table(name = "grupoproduto")`**: Opcional se o nome da tabela for igual ao nome da classe (ignorando maiúsculas/minúsculas, dependendo do SGBD e do Hibernate). É uma boa prática especificar explicitamente para clareza e para evitar problemas com convenções de nomenclatura.
* **`@Id`**: Marca o campo `codigo` como a chave primária da entidade.
* **`@GeneratedValue(strategy = GenerationType.SEQUENCE)`**: Configura a estratégia de geração de valores para a chave primária.
    * `GenerationType.SEQUENCE`: Utiliza uma sequência do banco de dados para gerar IDs. O Hibernate criará uma sequência padrão (ex: `grupoproduto_seq`) se não especificado de outra forma.
    * Outras opções: `IDENTITY` (para colunas auto-incrementais), `AUTO` (deixa o provedor escolher), `TABLE`.
* **`implements Serializable`**: Útil para entidades JPA, especialmente se elas forem passadas entre camadas, armazenadas em cache ou usadas em ambientes distribuídos. `serialVersionUID` é recomendado.
* **Construtor Padrão**: Um construtor público ou protegido sem argumentos é exigido pelo JPA.
* **`getters` e `setters`**: O Hibernate usa métodos de acesso (ou acesso direto a campos, dependendo da configuração) para ler e escrever os dados dos atributos persistentes.
* **`equals()` e `hashCode()`**: Importante para o gerenciamento correto de entidades pelo `EntityManager`, especialmente em coleções e para determinar o estado da entidade (nova, gerenciada, desanexada). Geralmente, a implementação para entidades se baseia na chave primária.

4.  **Criar e Executar uma Classe para Testar o Contexto de Persistência:**
    * Dentro do diretório `src/main/java`, crie o pacote `ifmt.cba.apps` (ou ajuste conforme a estrutura do seu projeto, por exemplo, `src/main/java/ifmt/cba/apps`).
    * Crie o arquivo `ContextoPersistencia.java` dentro desse pacote com o seguinte conteúdo:

        ```java
        package ifmt.cba.apps;

        import jakarta.persistence.EntityManager;
        import jakarta.persistence.EntityManagerFactory;
        import jakarta.persistence.Persistence;

        public class ContextoPersistencia {
            public static void main(String[] args) {
                
                EntityManagerFactory emf = null;
                EntityManager em = null;

                try{
                    // "UnidadeProdutos" é o nome definido no persistence.xml
                    emf = Persistence.createEntityManagerFactory("UnidadeProdutos"); 
                    em = emf.createEntityManager();
                    System.out.println("Contexto de persistencia e EntityManager criados com sucesso!");
                    System.out.println("Hibernate deve ter criado a tabela 'grupoproduto' no banco 'bdprodutos'. Verifique!");

                    // Aqui você poderia adicionar código para persistir ou consultar entidades no futuro.
                    // Exemplo:
                    // em.getTransaction().begin();
                    // GrupoProdutoVO novoGrupo = new GrupoProdutoVO();
                    // novoGrupo.setNome("Eletrônicos");
                    // novoGrupo.setMargemLucro(0.25f);
                    // novoGrupo.setPromocao(0.05f);
                    // em.persist(novoGrupo);
                    // em.getTransaction().commit();
                    // System.out.println("Novo grupo persistido: " + novoGrupo);

                } catch(Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Contexto de persistencia nao foi criado -- ERRO: "+ ex.getMessage());
                } finally {
                    if (em != null && em.isOpen()){ // Verificar se está aberto antes de fechar
                        em.close();
                    }
                    if (emf!= null && emf.isOpen()){ // Verificar se está aberto antes de fechar
                        emf.close();
                    }
                }
            }
        }
        ```
    * **Explicação da Classe `ContextoPersistencia.java`:**
        * Esta classe simples tem um método `main` para testar a inicialização do JPA.
        * `Persistence.createEntityManagerFactory("UnidadeProdutos")`: Tenta criar uma `EntityManagerFactory` baseada na unidade de persistência chamada "UnidadeProdutos" (definida no `persistence.xml`). Se o `persistence.xml` estiver correto e as dependências JDBC e Hibernate estiverem no classpath, isso deve funcionar.
        * `emf.createEntityManager()`: Cria um `EntityManager`, que é a interface principal para interagir com o contexto de persistência (salvar, buscar, remover entidades).
        * O bloco `try-catch-finally` garante que os recursos (`EntityManager` e `EntityManagerFactory`) sejam fechados corretamente, mesmo se ocorrerem erros.
        * Ao executar esta classe (como uma aplicação Java), se tudo estiver configurado corretamente e a propriedade `javax.persistence.schema-generation.database.action` estiver como `drop-and-create` ou `create`, você deverá ver a mensagem de sucesso e o Hibernate deverá ter criado a tabela `grupoproduto` no seu banco de dados `bdprodutos`. Verifique seu cliente de banco de dados para confirmar.

---

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
            
            @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE) // PostgreSQL geralmente usa SEQUENCE
            private int codigo;
            
            private String nome;

            @Column(name="margem_lucro")
            private float margemLucro; 
            
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

## Branch 4 - Persistindo Objeto

Nesta branch, vamos criar uma aplicação simples para inserir dados de um `GrupoProdutoVO` no banco de dados. Utilizaremos `JOptionPane` para entrada de dados e demonstraremos o ciclo básico de persistência de um objeto com JPA.

### O que deve ser feito nesta etapa:

1.  **Criar a Classe de Inclusão (`Incluir1.java`):**
    * No seu projeto, dentro do pacote `ifmt.cba.apps` (ou o pacote que você está usando para as classes de aplicação/teste), crie uma nova classe Java chamada `Incluir1.java`.
    * Cole o seguinte código nesta classe:

        ```java
        package ifmt.cba.apps;

        import jakarta.persistence.EntityManager;
        import jakarta.persistence.EntityManagerFactory;
        import jakarta.persistence.Persistence;
        import javax.swing.JOptionPane; // Import para JOptionPane

        import ifmt.cba.vo.GrupoProdutoVO; // Certifique-se que o pacote 'vo' e a classe estão corretos

        public class Incluir1 {
            public static void main(String args[]) {
                EntityManagerFactory emf = null;
                EntityManager em = null;
                GrupoProdutoVO grupoVO = new GrupoProdutoVO(); // Cria uma instância da entidade

                try {
                    // Coleta de dados do usuário via JOptionPane
                    String nome = JOptionPane.showInputDialog("Forneca o nome do grupo de produto");
                    float margem = Float.parseFloat(
                        JOptionPane.showInputDialog("Forneca o percentual da margem de lucro do grupo de produto (ex: 0.25 para 25%)")
                    );
                    float promocao = Float.parseFloat(
                        JOptionPane.showInputDialog("Forneca o percentual de promocao do grupo de produto (ex: 0.1 para 10%)")
                    );

                    // Define os atributos do objeto VO com os dados fornecidos
                    grupoVO.setNome(nome);
                    grupoVO.setMargemLucro(margem);
                    grupoVO.setPromocao(promocao);

                    // 1. Obter o EntityManagerFactory (usando o nome da unidade de persistência do persistence.xml)
                    emf = Persistence.createEntityManagerFactory("UnidadeProdutos");
                    // 2. Obter o EntityManager
                    em = emf.createEntityManager();
                    
                    // 3. Iniciar uma transação
                    em.getTransaction().begin();
                    // 4. Persistir o objeto (torná-lo gerenciado e marcá-lo para inserção)
                    em.persist(grupoVO);
                    // 5. Comitar a transação (efetivar a inserção no banco de dados)
                    em.getTransaction().commit();
                    
                    System.out.println("Inclusao realizada com sucesso. Código gerado: " + grupoVO.getCodigo());

                } catch (NumberFormatException ex) {
                    System.err.println("Erro ao converter numero: " + ex.getMessage());
                    JOptionPane.showMessageDialog(null, "Por favor, insira valores numéricos válidos para margem e promoção.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    // Se a transação estiver ativa e ocorreu um erro, faz rollback
                    if (em != null && em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    System.err.println("Inclusao nao realizada - ERRO: " + ex.getMessage());
                    ex.printStackTrace(); // Imprime o rastreamento completo do erro no console de erro
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro na inclusão: " + ex.getMessage(), "Erro de Persistência", JOptionPane.ERROR_MESSAGE);
                } finally {
                    // 6. Fechar o EntityManager e o EntityManagerFactory
                    if (em != null && em.isOpen()) {
                        em.close();
                    }
                    if (emf != null && emf.isOpen()) {
                        emf.close();
                    }
                }
            }
        }
        ```

### Entendendo o Código `Incluir1.java`:

* **Coleta de Dados:**
    * `JOptionPane.showInputDialog(...)` é usado para exibir caixas de diálogo e obter a entrada do usuário para o nome, margem de lucro e promoção do grupo de produtos.
    * `Float.parseFloat(...)` converte a entrada de texto para valores `float`.

* **Instanciação e Configuração do Objeto:**
    * `GrupoProdutoVO grupoVO = new GrupoProdutoVO();` cria um novo objeto da nossa entidade.
    * `grupoVO.setNome(nome);` e métodos `set` similares são usados para popular o objeto com os dados coletados.

* **Ciclo de Vida da Persistência JPA:**
    1.  **`EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadeProdutos");`**
        * Cria uma fábrica de `EntityManager` baseada na unidade de persistência "UnidadeProdutos" definida no `persistence.xml`. Esta operação é custosa e geralmente é feita uma vez por aplicação.
    2.  **`EntityManager em = emf.createEntityManager();`**
        * Cria um `EntityManager`, que é a interface principal para interagir com o contexto de persistência. Cada `EntityManager` gerencia um conjunto de entidades.
    3.  **`em.getTransaction().begin();`**
        * Inicia uma transação. Todas as operações de escrita (persistir, atualizar, remover) no JPA devem ocorrer dentro de uma transação.
    4.  **`em.persist(grupoVO);`**
        * Coloca o objeto `grupoVO` no contexto de persistência. Neste ponto, o objeto se torna *gerenciado* pelo JPA. Se for um objeto novo (sem ID ou com ID gerável), ele será agendado para inserção no banco quando a transação for comitada.
    5.  **`em.getTransaction().commit();`**
        * Confirma a transação. Neste momento, as alterações pendentes (como a inserção do `grupoVO`) são sincronizadas com o banco de dados. O Hibernate gerará o SQL `INSERT` correspondente. Se a chave primária (`codigo`) for gerada pelo banco (ex: via `GenerationType.SEQUENCE` ou `IDENTITY`), após o `commit` (ou às vezes após o `persist`, dependendo da estratégia de geração e do provedor JPA), o objeto `grupoVO` terá seu campo `codigo` preenchido.

## Branch 5 - Alterando Objeto Persistido

Nesta branch, aprenderemos como recuperar um objeto existente do banco de dados, permitir que o usuário modifique seus dados e, em seguida, persistir essas alterações. Usaremos a classe `Alterar1.java` como exemplo, que buscará um `GrupoProdutoVO` pelo nome, permitirá a edição de seus campos e salvará as modificações.

### O que deve ser feito nesta etapa:

1.  **Criar a Classe de Alteração (`Alterar1.java`):**
    * No seu projeto, dentro do pacote `ifmt.cba.apps` (ou o pacote que você está usando para as classes de aplicação), crie uma nova classe Java chamada `Alterar1.java`.
    * Cole o seguinte código nesta classe:

        ```java
        package ifmt.cba.apps;

        import java.util.List;
        import jakarta.persistence.EntityManager;
        import jakarta.persistence.EntityManagerFactory;
        import jakarta.persistence.Persistence;
        import jakarta.persistence.Query; // Import para jakarta.persistence.Query
        import javax.swing.JOptionPane;

        import ifmt.cba.vo.GrupoProdutoVO; // Certifique-se que o pacote 'vo' e a classe estão corretos

        public class Alterar1 {
            public static void main(String args[]) {
                EntityManagerFactory emf = null;
                EntityManager em = null;
                GrupoProdutoVO grupoVO = null;

                try {
                    emf = Persistence.createEntityManagerFactory("UnidadeProdutos");
                    em = emf.createEntityManager();
                    em.getTransaction().begin(); // Inicia a transação

                    // Solicita o nome do grupo de produto a ser localizado
                    String pNomeBusca = JOptionPane.showInputDialog("Forneca o nome do grupo de produto a ser localizado para alteração");

                    if (pNomeBusca == null || pNomeBusca.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "A busca pelo nome foi cancelada ou nenhum nome foi fornecido.");
                        if (em.getTransaction().isActive()) {
                            em.getTransaction().rollback();
                        }
                        return; // Encerra a execução se o nome não for fornecido
                    }

                    // Cria a consulta para buscar o GrupoProdutoVO pelo nome (case-insensitive)
                    Query consulta = em.createQuery("SELECT gp FROM GrupoProdutoVO gp WHERE UPPER(gp.nome) = :pNomeBusca");
                    consulta.setParameter("pNomeBusca", pNomeBusca.toUpperCase());

                    @SuppressWarnings("unchecked")
                    List<GrupoProdutoVO> lista = consulta.getResultList();

                    if (!lista.isEmpty()) {
                        grupoVO = lista.get(0); // Pega o primeiro resultado

                        // Solicita os novos dados, preenchendo com os valores atuais
                        String nomeNovo = JOptionPane.showInputDialog("Alterar nome do grupo:", grupoVO.getNome());
                        float margemNova = Float.parseFloat(
                            JOptionPane.showInputDialog("Alterar percentual da margem de lucro (ex: 0.25):", grupoVO.getMargemLucro())
                        );
                        float promocaoNova = Float.parseFloat(
                            JOptionPane.showInputDialog("Alterar percentual de promocao (ex: 0.10):", grupoVO.getPromocao())
                        );

                        // Altera os dados do objeto VO encontrado (entidade gerenciada)
                        if (nomeNovo != null && !nomeNovo.trim().isEmpty()) {
                            grupoVO.setNome(nomeNovo);
                        }
                        grupoVO.setMargemLucro(margemNova);
                        grupoVO.setPromocao(promocaoNova);

                        // Não é necessário chamar em.merge() ou em.persist() explicitamente aqui,
                        // pois 'grupoVO' é uma entidade gerenciada e as alterações são
                        // detectadas e sincronizadas no commit da transação.
                        em.getTransaction().commit();
                        System.out.println("Alteracao realizada com sucesso para o grupo: " + grupoVO.getNome() + " (Código: " + grupoVO.getCodigo() + ")");
                        JOptionPane.showMessageDialog(null, "Alteração realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        System.out.println("Grupo de Produto com nome \"" + pNomeBusca + "\" nao localizado para alteração.");
                        JOptionPane.showMessageDialog(null, "Grupo de Produto com nome \"" + pNomeBusca + "\" não localizado.", "Não Encontrado", JOptionPane.WARNING_MESSAGE);
                        if (em.getTransaction().isActive()) {
                            em.getTransaction().rollback(); // Desfaz a transação se nada foi alterado
                        }
                    }

                } catch (NumberFormatException ex) {
                    System.err.println("Erro ao converter valor numérico: " + ex.getMessage());
                    if (em != null && em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    JOptionPane.showMessageDialog(null, "Por favor, insira valores numéricos válidos para margem e promoção.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    System.err.println("Alteracao nao realizada - ERRO: " + ex.getMessage());
                    ex.printStackTrace();
                    if (em != null && em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro na alteração: " + ex.getMessage(), "Erro de Persistência", JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (em != null && em.isOpen()) {
                        em.close();
                    }
                    if (emf != null && emf.isOpen()) {
                        emf.close();
                    }
                }
            }
        }
        ```

### Entendendo o Código `Alterar1.java`:

* **Busca do Objeto:**
    * O usuário é solicitado a fornecer o nome do grupo de produto que deseja alterar.
    * Uma consulta JPQL (`SELECT gp FROM GrupoProdutoVO gp WHERE UPPER(gp.nome) = :pNomeBusca`) é usada para buscar o objeto no banco de dados de forma insensível a maiúsculas/minúsculas.
    * `consulta.setParameter("pNomeBusca", pNomeBusca.toUpperCase())` define o parâmetro da consulta.
    * `consulta.getResultList()` retorna uma lista de objetos encontrados. Espera-se que o nome seja um identificador razoavelmente único para este exemplo, ou o primeiro da lista será usado.

* **Manipulação do Resultado:**
    * Se a lista de resultados não estiver vazia (`!lista.isEmpty()`), o primeiro objeto (`grupoVO = lista.get(0);`) é selecionado para alteração.
    * Se nenhum objeto for encontrado, uma mensagem é exibida e a transação (se iniciada) é desfeita (`rollback`).

* **Modificação dos Dados:**
    * `JOptionPane.showInputDialog` é usado novamente, desta vez com dois argumentos: a mensagem de prompt e o valor atual do atributo do objeto (`grupoVO.getNome()`, `grupoVO.getMargemLucro()`, etc.). Isso preenche a caixa de diálogo com os dados existentes, facilitando a modificação pelo usuário.
    * Os setters do objeto `grupoVO` (`setNome`, `setMargemLucro`, `setPromocao`) são chamados para atualizar seus atributos com os novos valores fornecidos.

* **Persistência das Alterações (Atualização):**
    * **Importante:** Quando você recupera um objeto usando o `EntityManager` e ele está dentro de uma transação ativa, esse objeto se torna *gerenciado* pelo contexto de persistência.
    * Quaisquer modificações feitas nos atributos de uma entidade gerenciada (através de seus métodos setters) são automaticamente rastreadas pelo JPA.
    * Quando `em.getTransaction().commit()` é chamado, o JPA detecta essas alterações ("dirty checking") e gera automaticamente as instruções SQL `UPDATE` necessárias para sincronizar o estado do objeto com o banco de dados.
    * Portanto, **não é necessário chamar `em.persist()` ou `em.merge()` explicitamente** para atualizar um objeto que já está sendo gerenciado e foi modificado dentro de uma transação.

## Branch 6 - Excluindo Objeto Persistido

Nesta etapa, vamos aprender como remover (excluir) um objeto que já existe no banco de dados.

### O que deve ser feito nesta etapa:

1.  **Criar a Classe de Exclusão (`Excluir1.java`):**
    * No pacote `ifmt.cba.apps`, crie `Excluir1.java`:

        ```java
        package ifmt.cba.apps;

        import java.util.List;
        import jakarta.persistence.EntityManager;
        import jakarta.persistence.EntityManagerFactory;
        import jakarta.persistence.Persistence;
        import jakarta.persistence.Query;
        import javax.swing.JOptionPane;
        import ifmt.cba.vo.GrupoProdutoVO;

        public class Excluir1 {
            public static void main(String args[]) {
                EntityManagerFactory emf = null;
                EntityManager em = null;

                try {
                    emf = Persistence.createEntityManagerFactory("UnidadeProdutos");
                    em = emf.createEntityManager();
                    em.getTransaction().begin();

                    String pNome = JOptionPane.showInputDialog("Forneca o nome do grupo de produto a ser excluido");

                    if (pNome == null || pNome.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "A exclusão foi cancelada ou nenhum nome foi fornecido.");
                        if (em.getTransaction().isActive()) { em.getTransaction().rollback(); }
                        return;
                    }

                    Query consulta = em.createQuery("SELECT gp FROM GrupoProdutoVO gp WHERE UPPER(gp.nome) = :pNome");
                    consulta.setParameter("pNome", pNome.toUpperCase());

                    @SuppressWarnings("unchecked")
                    List<GrupoProdutoVO> lista = consulta.getResultList();

                    if (!lista.isEmpty()) {
                        GrupoProdutoVO grupoVOParaExcluir = lista.get(0);
                        em.remove(grupoVOParaExcluir); // Marca para remoção
                        em.getTransaction().commit(); // Efetiva a remoção
                        System.out.println("Exclusao realizada com sucesso para o grupo: " + pNome);
                        JOptionPane.showMessageDialog(null, "Exclusão realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        System.out.println("Grupo de Produto com nome \"" + pNome + "\" nao localizado para exclusão.");
                        JOptionPane.showMessageDialog(null, "Grupo de Produto com nome \"" + pNome + "\" não localizado.", "Não Encontrado", JOptionPane.WARNING_MESSAGE);
                        if (em.getTransaction().isActive()) { em.getTransaction().rollback(); }
                    }
                } catch (Exception ex) {
                    System.err.println("Exclusao nao realizada - ERRO: " + ex.getMessage());
                    ex.printStackTrace();
                    if (em != null && em.getTransaction().isActive()) { em.getTransaction().rollback(); }
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro na exclusão: " + ex.getMessage(), "Erro de Persistência", JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (em != null && em.isOpen()) { em.close(); }
                    if (emf != null && emf.isOpen()) { emf.close(); }
                }
            }
        }
        ```

### Entendendo o Código `Excluir1.java`:
* Solicita o nome do objeto a ser excluído.
* Busca o objeto no banco.
* Se encontrado, `em.remove(objeto)` marca a entidade para remoção.
* A exclusão efetiva ocorre no `commit` da transação.
* Trata casos de objeto não encontrado e outros erros.

## Branch 7 - Listando Objetos (Consulta com LIKE e Ordenação)

Nesta sétima etapa, focaremos em como consultar e listar múltiplos objetos do banco de dados. Criaremos a classe `Consulta1.java` que permitirá ao usuário buscar `GrupoProdutoVO` por parte do nome, utilizando a cláusula `LIKE` do JPQL para correspondência parcial de strings. Além disso, os resultados serão ordenados alfabeticamente. 🔎📋

### O que deve ser feito nesta etapa:

1.  **Criar a Classe de Consulta (`Consulta1.java`):**
    * No pacote `ifmt.cba.apps` do seu projeto, crie uma nova classe Java chamada `Consulta1.java`.
    * Cole o seguinte código nesta classe:

        ```java
        package ifmt.cba.apps;

        import java.util.List;
        import jakarta.persistence.EntityManager;
        import jakarta.persistence.EntityManagerFactory;
        import jakarta.persistence.Persistence;
        import jakarta.persistence.Query;
        import javax.swing.JOptionPane;
        import ifmt.cba.vo.GrupoProdutoVO; // Certifique-se de que o pacote 'vo' está correto

        public class Consulta1 {
            public static void main(String args[]) {
                EntityManagerFactory emf = null;
                EntityManager em = null;

                try {
                    emf = Persistence.createEntityManagerFactory("UnidadeProdutos");
                    em = emf.createEntityManager();
                    // Para consultas (SELECT), uma transação explícita (begin/commit) não é sempre necessária.

                    String nomeBusca = JOptionPane.showInputDialog("Forneca parte do nome do grupo de produto a ser localizado");

                    if (nomeBusca == null) { // Usuário clicou em Cancelar ou fechou a caixa de diálogo
                        System.out.println("Consulta cancelada pelo usuário.");
                        if (em != null && em.getTransaction().isActive()) { // Se houvesse transação, faria rollback
                           em.getTransaction().rollback();
                        }
                        return; // Encerra a execução
                    }
                    if (nomeBusca.trim().isEmpty()){
                        nomeBusca = "%"; // Busca todos se o campo estiver vazio
                        System.out.println("Nenhum nome fornecido. Listando todos os grupos...");
                    } else {
                        nomeBusca = "%" + nomeBusca.toUpperCase() + "%"; // Adiciona wildcards para LIKE
                    }


                    // Cria a consulta JPQL para buscar GrupoProdutoVO cujo nome contenha o texto fornecido (LIKE)
                    // e ordena os resultados pelo nome.
                    Query consulta = em.createQuery("SELECT gp FROM GrupoProdutoVO gp WHERE UPPER(gp.nome) LIKE :pNome ORDER BY gp.nome");
                    // Adiciona os wildcards '%' para a cláusula LIKE e converte para maiúsculas
                    consulta.setParameter("pNome", nomeBusca);

                    @SuppressWarnings("unchecked") // Para suprimir o warning da conversão de List
                    List<GrupoProdutoVO> lista = consulta.getResultList(); // Executa a consulta

                    if (!lista.isEmpty()) { // Verifica se a lista de resultados não está vazia
                        System.out.println("Grupos de Produtos Encontrados (" + lista.size() + "):");
                        for (GrupoProdutoVO grupo : lista) {
                            System.out.println("------------------------------------");
                            System.out.println("Codigo.......: " + grupo.getCodigo());
                            System.out.println("Nome.........: " + grupo.getNome());
                            System.out.println("Margem Lucro.: " + grupo.getMargemLucro());
                            System.out.println("Promocao.....: " + grupo.getPromocao());
                        }
                        System.out.println("------------------------------------");
                        JOptionPane.showMessageDialog(null, lista.size() + " grupo(s) de produto(s) encontrado(s). Verifique o console.", "Consulta Realizada", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        String mensagem = nomeBusca.equals("%") ? "Nenhum Grupo de Produto cadastrado." : "Nenhum Grupo de Produto localizado com o nome contendo: \"" + nomeBusca.replace("%","") + "\"";
                        System.out.println(mensagem);
                        JOptionPane.showMessageDialog(null, mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    System.err.println("Consulta nao realizada - ERRO: " + ex.getMessage());
                    ex.printStackTrace(); // Imprime o rastreamento completo do erro
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro na consulta: " + ex.getMessage(), "Erro de Persistência", JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (em != null && em.isOpen()) {
                        em.close();
                    }
                    if (emf != null && emf.isOpen()) {
                        emf.close();
                    }
                }
            }
        }
        ```

### Entendendo o Código `Consulta1.java`:

* **Entrada do Usuário para Busca:**
    * `JOptionPane.showInputDialog` solicita ao usuário que forneça uma parte do nome do grupo de produtos que deseja buscar.
    * Se o usuário cancelar ou não fornecer entrada, a consulta é interrompida ou, como implementado, busca todos os registros se o campo for deixado vazio (transformando `nomeBusca` em `"%"`).

* **Construção da Consulta JPQL:**
    * **`SELECT gp FROM GrupoProdutoVO gp WHERE UPPER(gp.nome) LIKE :pNome ORDER BY gp.nome`**
        * `SELECT gp FROM GrupoProdutoVO gp`: Seleciona todos os objetos (`gp`) da entidade `GrupoProdutoVO`.
        * `WHERE UPPER(gp.nome) LIKE :pNome`: Esta é a cláusula de filtragem.
            * `UPPER(gp.nome)`: Converte o nome do grupo no banco de dados para maiúsculas para uma comparação insensível a maiúsculas/minúsculas.
            * `LIKE :pNome`: Compara o nome (em maiúsculas) com o parâmetro fornecido (`:pNome`). A cláusula `LIKE` permite o uso de caracteres curinga.
        * `ORDER BY gp.nome`: Ordena os resultados da consulta em ordem alfabética com base no campo `nome` da entidade.
    * **Parâmetro da Consulta:**
        * `consulta.setParameter("pNome", nomeBusca);`: Define o valor do parâmetro nomeado `:pNome` na consulta.
        * O valor `nomeBusca` é preparado como `"%TEXTO_DIGITADO_EM_MAIUSCULAS%"` (ou apenas `"%"` se nada for digitado) para que a cláusula `LIKE` funcione corretamente, encontrando qualquer nome que *contenha* o texto fornecido.

* **Execução da Consulta e Processamento dos Resultados:**
    * `List<GrupoProdutoVO> lista = consulta.getResultList();`: Executa a consulta JPQL e retorna uma lista de objetos `GrupoProdutoVO` que satisfazem os critérios.
    * O código então verifica se a lista não está vazia (`!lista.isEmpty()`).
    * Se houver resultados, ele itera sobre a lista (`for (GrupoProdutoVO grupo : lista)`) e imprime os detalhes de cada `GrupoProdutoVO` no console.
    * Se nenhum objeto for encontrado, uma mensagem informativa é exibida.

* **Transações em Consultas:**
    * Para operações de apenas leitura (como `SELECT` queries no JPA), não é estritamente necessário iniciar e comitar uma transação (`em.getTransaction().begin()`, `em.getTransaction().commit()`). O JPA pode executar consultas `SELECT` fora de uma transação explícita na maioria dos casos.

## Branch 8 - Implementação de Produto (Relacionamento Muitos-para-Um)

Nesta oitava e última branch do nosso workshop, vamos introduzir uma nova entidade, `ProdutoVO`, que terá um relacionamento do tipo Muitos-para-Um (`@ManyToOne`) com a entidade `GrupoProdutoVO` que já criamos. Isso significa que muitos produtos podem pertencer a um grupo de produto.

Vamos cobrir:
1.  A criação da classe de entidade `ProdutoVO`.
2.  A atualização do `persistence.xml` para incluir esta nova entidade.
3.  A adaptação da classe `Incluir2.java` para permitir a inclusão de um novo `ProdutoVO`, associando-o a um `GrupoProdutoVO` existente.
4.  A adaptação da classe `Consulta2.java` para listar objetos `ProdutoVO` e exibir informações do seu grupo associado.

Este passo é fundamental para entender como mapear e trabalhar com relacionamentos entre entidades no JPA.

### O que deve ser feito nesta etapa:

1.  **Criar a Entidade `ProdutoVO.java`:**
    * No pacote `ifmt.cba.VO` (ou `ifmt.cba.vo`), crie a classe `ProdutoVO.java`. Esta classe representará os produtos em nosso sistema.
    * Cole o seguinte código:

        ```java
        package ifmt.cba.VO; // ou ifmt.cba.vo

        import jakarta.persistence.Column;
        import jakarta.persistence.Entity;
        import jakarta.persistence.GeneratedValue;
        import jakarta.persistence.GenerationType;
        import jakarta.persistence.Id;
        import jakarta.persistence.ManyToOne; // Import para o relacionamento
        import jakarta.persistence.Table;
        import java.io.Serializable; // Boa prática adicionar Serializable

        @Entity
        @Table(name = "produto") // Nome da tabela no banco de dados
        public class ProdutoVO implements Serializable { // Implementar Serializable
            
            private static final long serialVersionUID = 1L; // Para Serializable

            @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE)
            private int codigo;

            @Column(length = 50, nullable = false) // Nome não pode ser nulo
            private String nome;

            private int estoque;

            @Column(name = "preco_compra")
            private float precoCompra;

            @Column(name = "margem_lucro")
            private float margemLucro;

            private float promocao; // Percentual de promoção, ex: 0.1 para 10%

            private float venda; // Preço de venda final (pode ser calculado ou definido)

            @ManyToOne // Muitos Produtos para Um GrupoProduto
            private GrupoProdutoVO grupo; // Campo que representa o relacionamento

            // Construtor padrão (necessário para JPA)
            public ProdutoVO() {}

            // Getters e Setters para todos os atributos
            public int getCodigo() { return codigo; }
            public void setCodigo(int codigo) { this.codigo = codigo; }
            public String getNome() { return nome; }
            public void setNome(String nome) { this.nome = nome; }
            public int getEstoque() { return estoque; }
            public void setEstoque(int estoque) { this.estoque = estoque; }
            public float getPrecoCompra() { return precoCompra; }
            public void setPrecoCompra(float precoCompra) { this.precoCompra = precoCompra; }
            public float getMargemLucro() { return margemLucro; }
            public void setMargemLucro(float margemLucro) { this.margemLucro = margemLucro; }
            public float getPromocao() { return promocao; }
            public void setPromocao(float promocao) { this.promocao = promocao; }
            public float getVenda() { return venda; }
            public void setVenda(float venda) { this.venda = venda; }
            public GrupoProdutoVO getGrupo() { return grupo; }
            public void setGrupo(GrupoProdutoVO grupo) { this.grupo = grupo; }

            // Implementação de hashCode e equals é crucial, especialmente com relacionamentos
            // e ao trabalhar com coleções ou o contexto de persistência do JPA.
            // A implementação fornecida pelo usuário considera todos os campos.
            // Alternativamente, para entidades, focar no @Id (após persistência) é comum.
            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + codigo;
                result = prime * result + ((nome == null) ? 0 : nome.hashCode());
                result = prime * result + estoque;
                result = prime * result + Float.floatToIntBits(precoCompra);
                result = prime * result + Float.floatToIntBits(margemLucro);
                result = prime * result + Float.floatToIntBits(promocao);
                result = prime * result + Float.floatToIntBits(venda);
                // Considerar o hashCode do 'grupo' pode ser complexo se ele for lazy-loaded
                // e não estiver carregado. Uma abordagem comum é usar o ID do grupo se disponível.
                result = prime * result + ((grupo == null || grupo.getCodigo() == 0) ? 0 : grupo.hashCode());
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null) return false;
                if (getClass() != obj.getClass()) return false;
                ProdutoVO other = (ProdutoVO) obj;
                if (codigo != 0 && other.codigo != 0) { // Se ambos têm ID, compare por ID
                    return codigo == other.codigo;
                }
                // Se os IDs não estão definidos (objetos novos), compara por outros campos.
                // A implementação fornecida pelo usuário compara todos os campos.
                if (nome == null) {
                    if (other.nome != null) return false;
                } else if (!nome.equals(other.nome)) return false;
                if (estoque != other.estoque) return false;
                if (Float.floatToIntBits(precoCompra) != Float.floatToIntBits(other.precoCompra)) return false;
                if (Float.floatToIntBits(margemLucro) != Float.floatToIntBits(other.margemLucro)) return false;
                if (Float.floatToIntBits(promocao) != Float.floatToIntBits(other.promocao)) return false;
                if (Float.floatToIntBits(venda) != Float.floatToIntBits(other.venda)) return false;
                if (grupo == null) {
                    if (other.grupo != null) return false;
                } else if (grupo.getCodigo() != 0 && other.grupo.getCodigo() != 0) {
                     if (!grupo.equals(other.grupo)) return false; // Compara grupos se ambos têm ID
                } else if (grupo.getNome() != null && other.grupo.getNome() != null) {
                     if (!grupo.getNome().equals(other.grupo.getNome())) return false; // Fallback para nome se ID não disponível
                } else if (grupo != other.grupo) { // Fallback para referência se nomes também nulos
                    return false;
                }
                return true;
            }

            @Override
            public String toString() {
                return "ProdutoVO [codigo=" + codigo + ", nome=" + nome + ", grupo=" + (grupo != null ? grupo.getNome() : "N/A") + "]";
            }
        }
        ```
    * **Entendendo a Entidade `ProdutoVO`:**
        * `@Entity`, `@Table(name = "produto")`: Define a classe como uma entidade JPA mapeada para a tabela "produto".
        * `@Id`, `@GeneratedValue`: Configuram a chave primária `codigo` com geração automática.
        * `@Column(length = 50, nullable = false)`: Exemplo de como especificar o tamanho máximo de uma coluna de string e que ela não pode ser nula.
        * `@Column(name = "preco_compra")`: Mapeia o atributo `precoCompra` para a coluna `preco_compra` no banco (útil se os nomes diferem da convenção Java).
        * **`@ManyToOne`**: Esta é a anotação chave para o relacionamento. Indica que muitos `ProdutoVO` podem estar associados a um `GrupoProdutoVO`. O JPA criará uma coluna de chave estrangeira na tabela `produto` (por padrão, `grupo_codigo`) para armazenar o ID do `GrupoProdutoVO` associado.
        * `private GrupoProdutoVO grupo;`: O campo que representa a associação.

2.  **Atualizar `persistence.xml`:**
    * Para que o Hibernate reconheça e gerencie a nova entidade `ProdutoVO`, precisamos listá-la no arquivo `src/main/resources/META-INF/persistence.xml`.
    * Adicione a seguinte linha dentro da tag `<persistence-unit>`, junto com a declaração da classe `GrupoProdutoVO`:

        ```xml
        <class>ifmt.cba.VO.ProdutoVO</class>
        ```
    * O `persistence.xml` atualizado (parcialmente) ficará assim:

        ```xml
        <persistence-unit name="UnidadeProdutos" transaction-type="RESOURCE_LOCAL">
            <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
            
            <class>ifmt.cba.VO.GrupoProdutoVO</class> 
            <class>ifmt.cba.VO.ProdutoVO</class> <exclude-unlisted-classes>true</exclude-unlisted-classes>
            
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
        ```
    * Com `javax.persistence.schema-generation.database.action` como `drop-and-create`, o Hibernate irá criar a tabela `produto` com a coluna de chave estrangeira apropriada quando a aplicação for executada.

3.  **Implementar `Incluir2.java` (Incluir Produto Relacionado):**
    * Esta classe será responsável por cadastrar um novo `ProdutoVO`, associando-o a um `GrupoProdutoVO` já existente.
    * No pacote `ifmt.cba.apps`, crie ou atualize a classe `Incluir2.java` com o seguinte código (conforme fornecido por você):

        ```java
        package ifmt.cba.apps;

        import java.util.List;
        import javax.swing.JOptionPane;
        import ifmt.cba.VO.GrupoProdutoVO; // Corrigido para VO maiúsculo, conforme sua entidade
        import ifmt.cba.VO.ProdutoVO;    // Corrigido para VO maiúsculo
        import jakarta.persistence.EntityManager;
        import jakarta.persistence.EntityManagerFactory;
        import jakarta.persistence.Persistence;
        import jakarta.persistence.Query;

        public class Incluir2 {
            public static void main(String args[]) {
                EntityManagerFactory emf = null;
                EntityManager em = null;
                ProdutoVO produtoVO = null; // Não precisa inicializar aqui se for dentro do if
                GrupoProdutoVO grupoVO = null;

                try {
                    emf = Persistence.createEntityManagerFactory("UnidadeProdutos");
                    em = emf.createEntityManager();
                    em.getTransaction().begin();

                    String pNomeGrupo = JOptionPane.showInputDialog("Forneca o nome do GRUPO DE PRODUTO ao qual o novo produto pertencerá");
                    if (pNomeGrupo == null || pNomeGrupo.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Nome do grupo não fornecido. Operação cancelada.");
                        em.getTransaction().rollback();
                        return;
                    }

                    Query consultaGrupo = em.createQuery("SELECT gp FROM GrupoProdutoVO gp WHERE UPPER(gp.nome) = :pNomeGrupo");
                    consultaGrupo.setParameter("pNomeGrupo", pNomeGrupo.toUpperCase());
                    List<GrupoProdutoVO> listaGrupos = consultaGrupo.getResultList();

                    if (!listaGrupos.isEmpty()) {
                        grupoVO = listaGrupos.get(0); // Pega o primeiro grupo encontrado

                        String nomeProd = JOptionPane.showInputDialog("Forneca o nome do NOVO PRODUTO");
                        if (nomeProd == null || nomeProd.trim().isEmpty()) {
                             JOptionPane.showMessageDialog(null, "Nome do produto não fornecido. Operação cancelada.");
                             em.getTransaction().rollback();
                             return;
                        }
                        float compra = Float.parseFloat(JOptionPane.showInputDialog("Preco de compra do produto"));
                        float venda = Float.parseFloat(JOptionPane.showInputDialog("Preco de venda do produto"));
                        float margem = Float.parseFloat(JOptionPane.showInputDialog("Percentual da margem de lucro (ex: 0.25)"));
                        float promocao = Float.parseFloat(JOptionPane.showInputDialog("Percentual de promocao (ex: 0.10)"));
                        int estoque = Integer.parseInt(JOptionPane.showInputDialog("Estoque inicial do produto"));

                        produtoVO = new ProdutoVO();
                        produtoVO.setNome(nomeProd);
                        produtoVO.setPrecoCompra(compra);
                        produtoVO.setVenda(venda); // Assumindo que 'venda' é o preço final. Poderia ser calculado.
                        produtoVO.setMargemLucro(margem);
                        produtoVO.setPromocao(promocao);
                        produtoVO.setEstoque(estoque);
                        produtoVO.setGrupo(grupoVO); // Crucial: Associando o produto ao grupo recuperado

                        em.persist(produtoVO); // Persiste o novo ProdutoVO
                        em.getTransaction().commit();
                        System.out.println("Inclusao de ProdutoVO realizada com sucesso. Código: " + produtoVO.getCodigo());
                        JOptionPane.showMessageDialog(null, "Produto incluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        System.out.println("Grupo de Produto com nome \"" + pNomeGrupo + "\" nao localizado. Produto não pode ser incluído.");
                        JOptionPane.showMessageDialog(null, "Grupo de Produto não localizado!", "Erro", JOptionPane.ERROR_MESSAGE);
                        em.getTransaction().rollback(); // Importante: rollback se o grupo não existe
                    }
                } catch (NumberFormatException ex) {
                    System.err.println("Erro de formato numérico: " + ex.getMessage());
                     if (em != null && em.getTransaction().isActive()) { em.getTransaction().rollback(); }
                    JOptionPane.showMessageDialog(null, "Por favor, insira valores numéricos válidos.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    System.err.println("Inclusao de ProdutoVO nao realizada - ERRO: " + ex.getMessage());
                    ex.printStackTrace();
                    if (em != null && em.getTransaction().isActive()) { em.getTransaction().rollback(); }
                    JOptionPane.showMessageDialog(null, "Erro ao incluir produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (em != null && em.isOpen()) { em.close(); }
                    if (emf != null && emf.isOpen()) { emf.close(); }
                }
            }
        }
        ```
    * **Entendendo `Incluir2.java`:**
        * Primeiro, localiza um `GrupoProdutoVO` pelo nome.
        * Se encontrado, coleta os dados para um novo `ProdutoVO`.
        * **`produtoVO.setGrupo(grupoVO);`**: Esta linha é fundamental. Ela estabelece a associação entre o novo `ProdutoVO` e o `GrupoProdutoVO` existente.
        * `em.persist(produtoVO);`: Salva o novo produto. O JPA cuidará de persistir a chave estrangeira correta na tabela `produto`.

4.  **Implementar `Consulta2.java` (Consultar Produtos e seus Grupos):**
    * Esta classe demonstrará como consultar produtos e acessar informações do grupo ao qual pertencem.
    * No pacote `ifmt.cba.apps`, crie ou atualize a classe `Consulta2.java` (conforme fornecido por você):

        ```java
        package ifmt.cba.apps;

        import java.util.List;
        import javax.swing.JOptionPane;
        import ifmt.cba.VO.ProdutoVO; // Corrigido para VO maiúsculo
        // Não é necessário importar GrupoProdutoVO aqui se só acessamos via produto.getGrupo()
        import jakarta.persistence.EntityManager;
        import jakarta.persistence.EntityManagerFactory;
        import jakarta.persistence.Persistence;
        import jakarta.persistence.Query;
        import jakarta.persistence.NoResultException; // Para tratar consulta que não retorna nada

        public class Consulta2 {
            public static void main(String args[]) {
                EntityManagerFactory emf = null;
                EntityManager em = null;
                try {
                    emf = Persistence.createEntityManagerFactory("UnidadeProdutos");
                    em = emf.createEntityManager();

                    String nomeProdBusca = JOptionPane.showInputDialog("Forneca parte do nome do PRODUTO a ser localizado (deixe em branco para todos)");

                    String jpql = "SELECT p FROM ProdutoVO p";
                    boolean temNome = false;
                    if (nomeProdBusca != null && !nomeProdBusca.trim().isEmpty()) {
                        jpql += " WHERE UPPER(p.nome) LIKE :pNomeProd";
                        temNome = true;
                    }
                    jpql += " ORDER BY p.nome";

                    Query consulta = em.createQuery(jpql);
                    if (temNome) {
                        consulta.setParameter("pNomeProd", "%" + nomeProdBusca.toUpperCase() + "%");
                    }

                    @SuppressWarnings("unchecked")
                    List<ProdutoVO> listaProdutos = consulta.getResultList();

                    if (!listaProdutos.isEmpty()) {
                        System.out.println("Produtos Encontrados (" + listaProdutos.size() + "):");
                        StringBuilder sb = new StringBuilder();
                        for (ProdutoVO produto : listaProdutos) {
                            sb.append("---------------------------------------\n");
                            sb.append("Codigo.......: ").append(produto.getCodigo()).append("\n");
                            sb.append("Nome.........: ").append(produto.getNome()).append("\n");
                            // Acessando o nome do grupo relacionado
                            // É importante garantir que o grupo não seja nulo antes de chamar getNome()
                            // E que a relação @ManyToOne em ProdutoVO não seja FetchType.LAZY se não houver transação ativa
                            // ou que o acesso ocorra dentro de uma transação.
                            // Para este exemplo simples, assumimos que será EAGER ou acessado em contexto ativo.
                            sb.append("Grupo........: ").append(produto.getGrupo() != null ? produto.getGrupo().getNome() : "N/A").append("\n");
                            sb.append("Preco Compra.: ").append(produto.getPrecoCompra()).append("\n");
                            sb.append("Preco Venda..: ").append(produto.getVenda()).append("\n");
                            sb.append("Margem Lucro.: ").append(produto.getMargemLucro()).append("\n");
                            sb.append("Estoque......: ").append(produto.getEstoque()).append("\n");
                        }
                        sb.append("---------------------------------------\n");
                        System.out.println(sb.toString());
                        // Para exibir em JOptionPane, limitamos a quantidade ou usamos JTextArea
                        if (listaProdutos.size() < 10) { // Exibe no JOptionPane se poucos resultados
                           JOptionPane.showMessageDialog(null, sb.toString(), "Produtos Encontrados", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                           JOptionPane.showMessageDialog(null, listaProdutos.size() + " produto(s) encontrado(s). Verifique o console para a lista completa.", "Consulta Realizada", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        System.out.println("Nenhum produto localizado com os critérios fornecidos.");
                        JOptionPane.showMessageDialog(null, "Nenhum produto localizado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NoResultException nre) {
                    System.out.println("Nenhum produto localizado (NoResultException).");
                     JOptionPane.showMessageDialog(null, "Nenhum produto localizado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    System.err.println("Consulta de ProdutoVO nao realizada - ERRO: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao consultar produtos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (em != null && em.isOpen()) { em.close(); }
                    if (emf != null && emf.isOpen()) { emf.close(); }
                }
            }
        }
        ```
    * **Entendendo `Consulta2.java`:**
        * Realiza uma consulta na entidade `ProdutoVO`.
        * Permite buscar por parte do nome do produto ou listar todos se nenhum nome for fornecido.
        * **`produto.getGrupo().getNome()`**: Demonstra como navegar pelo relacionamento. A partir de um objeto `ProdutoVO`, você pode acessar o objeto `GrupoProdutoVO` associado e, então, seus atributos (como o nome do grupo).
        * **Considerações sobre FetchType:** Se o relacionamento `@ManyToOne` em `ProdutoVO` for `FetchType.LAZY` (o padrão para relacionamentos `-ToOne` fora de um contexto de transação pode variar ou ser problemático), você precisaria garantir que `produto.getGrupo()` seja acessado enquanto o `EntityManager` está aberto e, idealmente, dentro de uma transação, ou usar estratégias como JOIN FETCH na consulta JPQL para carregar o grupo antecipadamente, ou configurar como `FetchType.EAGER` (o que pode ter implicações de desempenho se não for necessário sempre). Para este exemplo simples, presumimos que o acesso funcionará diretamente.

        Esta branch conclui os principais aspectos do CRUD e introduz o conceito fundamental de relacionamentos entre entidades, que é uma parte poderosa do JPA. Parabéns por chegar até aqui!
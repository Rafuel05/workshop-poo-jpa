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
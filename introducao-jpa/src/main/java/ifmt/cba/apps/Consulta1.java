package ifmt.cba.apps;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import javax.swing.JOptionPane;
import ifmt.cba.VO.GrupoProdutoVO; // Certifique-se de que o pacote 'vo' está correto

public class Consulta1 {
    public static void main(String args[]) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("UnidadeProdutos");
            em = emf.createEntityManager();
            // Para consultas (SELECT), uma transação explícita (begin/commit) não é sempre necessária,
            // a menos que você precise de um comportamento transacional específico ou
            // esteja fazendo outras operações que modifiquem dados.

            String nome = JOptionPane.showInputDialog("Forneca o nome do grupo de produto a ser localizado");

            if (nome == null) { // Usuário clicou em Cancelar ou fechou a caixa de diálogo
                System.out.println("Consulta cancelada pelo usuário.");
                return;
            }

            // Cria a consulta JPQL para buscar GrupoProdutoVO cujo nome contenha o texto fornecido (LIKE)
            // e ordena os resultados pelo nome.
            Query consulta = em.createQuery("SELECT gp FROM GrupoProdutoVO gp WHERE UPPER(gp.nome) LIKE :pNome ORDER BY gp.nome");
            // Adiciona os wildcards '%' para a cláusula LIKE e converte para maiúsculas
            consulta.setParameter("pNome", "%" + nome.toUpperCase() + "%");

            @SuppressWarnings("unchecked") // Para suprimir o warning da conversão de List
            List<GrupoProdutoVO> lista = consulta.getResultList(); // Executa a consulta

            if (!lista.isEmpty()) { // Verifica se a lista de resultados não está vazia
                System.out.println("Grupos de Produtos Encontrados:");
                for (GrupoProdutoVO grupo : lista) {
                    System.out.println("------------------------------------"); // Separador visual para cada grupo
                    System.out.println("Codigo.......: " + grupo.getCodigo());
                    System.out.println("Nome.........: " + grupo.getNome());
                    System.out.println("Margem Lucro.: " + grupo.getMargemLucro()); // Corrigido para "Margem Lucro.:" (imagem tinha "....")
                    System.out.println("Promocao.....: " + grupo.getPromocao());
                }
                System.out.println("------------------------------------"); // Separador final
            } else {
                System.out.println("Nenhum Grupo de Produto localizado com o nome contendo: \"" + nome + "\"");
                JOptionPane.showMessageDialog(null, "Nenhum Grupo de Produto localizado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
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
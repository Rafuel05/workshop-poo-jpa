package ifmt.cba.apps;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import javax.swing.JOptionPane;

import ifmt.cba.VO.GrupoProdutoVO; 

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
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                return; // Encerra a execução
            }

            Query consulta = em.createQuery("SELECT gp FROM GrupoProdutoVO gp WHERE UPPER(gp.nome) = :pNome");
            consulta.setParameter("pNome", pNome.toUpperCase());

            
            List<GrupoProdutoVO> lista = consulta.getResultList();

            if (!lista.isEmpty()) { // Se lista.size() > 0
                GrupoProdutoVO grupoVOParaExcluir = lista.get(0); // Pega o primeiro item da lista
                em.remove(grupoVOParaExcluir); // Marca a entidade para remoção
                em.getTransaction().commit(); // Efetiva a remoção no banco de dados
                System.out.println("Exclusao realizada com sucesso para o grupo: " + pNome);
                JOptionPane.showMessageDialog(null, "Exclusão realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("Grupo de Produto com nome \"" + pNome + "\" nao localizado para exclusão.");
                JOptionPane.showMessageDialog(null, "Grupo de Produto com nome \"" + pNome + "\" não localizado.", "Não Encontrado", JOptionPane.WARNING_MESSAGE);
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback(); // Desfaz a transação se nada foi alterado
                }
            }
        } catch (Exception ex) {
            System.err.println("Exclusao nao realizada - ERRO: " + ex.getMessage());
            ex.printStackTrace(); // Imprime o rastreamento completo do erro
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Faz rollback em caso de erro
            }
            JOptionPane.showMessageDialog(null, "Ocorreu um erro na exclusão: " + ex.getMessage(), "Erro de Persistência", JOptionPane.ERROR_MESSAGE);
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
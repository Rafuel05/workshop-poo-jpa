package ifmt.cba.apps;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query; // Import para jakarta.persistence.Query
import javax.swing.JOptionPane;

import ifmt.cba.VO.GrupoProdutoVO; 

public class Alterar1 { 
    public static void main(String args[]) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        GrupoProdutoVO grupoVO = null; // Inicializa como null

        try {
            emf = Persistence.createEntityManagerFactory("UnidadeProdutos");
            em = emf.createEntityManager();
            em.getTransaction().begin(); // Inicia a transação

            // Solicita o nome do grupo de produto a ser localizado
            String pNome = JOptionPane.showInputDialog("Forneca o nome do grupo de produto a ser localizado");

            // Cria a consulta para buscar o GrupoProdutoVO pelo nome (case-insensitive)
            Query consulta = em.createQuery("SELECT gp FROM GrupoProdutoVO gp WHERE UPPER(gp.nome) = :pNome");
            consulta.setParameter("pNome", pNome.toUpperCase()); // Define o parâmetro da consulta

            
            List<GrupoProdutoVO> lista = consulta.getResultList(); // Executa a consulta

            if (!lista.isEmpty()) { // Verifica se algum resultado foi encontrado
                grupoVO = lista.get(0); // Pega o primeiro (e esperado único) resultado

                // Solicita os novos dados, preenchendo com os valores atuais
                String nomeNovo = JOptionPane.showInputDialog("Forneca o novo nome do grupo", grupoVO.getNome());
                float margemNova = Float.parseFloat(
                    JOptionPane.showInputDialog("Forneca o novo percentual da margem de lucro", grupoVO.getMargemLucro())
                );
                float promocaoNova = Float.parseFloat(
                    JOptionPane.showInputDialog("Forneca o novo percentual de promocao", grupoVO.getPromocao())
                );

                // Altera os dados do objeto VO encontrado
                grupoVO.setNome(nomeNovo);
                grupoVO.setMargemLucro(margemNova);
                grupoVO.setPromocao(promocaoNova);

                
                
                
                em.getTransaction().commit(); // Comita a transação, efetivando as alterações
                System.out.println("Alteracao realizada com sucesso");

            } else {
                System.out.println("Grupo de Produto nao localizado");
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
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
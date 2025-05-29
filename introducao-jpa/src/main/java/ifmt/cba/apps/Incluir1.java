package ifmt.cba.apps;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javax.swing.JOptionPane; 

import ifmt.cba.VO.GrupoProdutoVO;

public class Incluir1 { 
    public static void main(String args[]) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        GrupoProdutoVO grupoVO = new GrupoProdutoVO();

        try {
            String nome = JOptionPane.showInputDialog("Forneca o nome do grupo de produto");
            float margem = Float.parseFloat(
                JOptionPane.showInputDialog("Forneca o percentual da margem de lucro do grupo de produto")
            );
            float promocao = Float.parseFloat(
                JOptionPane.showInputDialog("Forneca o percentual de promocao do grupo de produto")
            );

            grupoVO.setNome(nome);
            grupoVO.setMargemLucro(margem);
            grupoVO.setPromocao(promocao);

            emf = Persistence.createEntityManagerFactory("UnidadeProdutos");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(grupoVO);
            em.getTransaction().commit();
            System.out.println("Inclusao realizada com sucesso");

        } catch (Exception ex) {
            System.out.println("Inclusao nao realizada - " + ex.getMessage());
            // ex.printStackTrace(); // Descomente para ver o rastreamento completo da pilha do erro
        } finally {
            if (em != null && em.isOpen()) { // Verifica se está aberto antes de fechar
                em.close();
            }
            if (emf != null && emf.isOpen()) { // Verifica se está aberto antes de fechar
                emf.close();
            }
        }
    }
}
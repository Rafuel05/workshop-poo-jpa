package ifmt.cba.apps;

import java.util.List;
import javax.swing.JOptionPane;

import ifmt.cba.VO.GrupoProdutoVO;
import ifmt.cba.VO.ProdutoVO; 
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

public class Incluir2 {
    public static void main(String args[]) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        ProdutoVO produtoVO = null;
        GrupoProdutoVO grupoVO = null;

        try {
            emf = Persistence.createEntityManagerFactory("UnidadeProdutos");
            em = emf.createEntityManager();
            em.getTransaction().begin();

            String pNome = JOptionPane.showInputDialog("Forneca o nome do grupo de produto a ser localizado");
            Query consulta = em.createQuery(
                "SELECT gp FROM GrupoProdutoVO gp WHERE UPPER(gp.nome) = :pNome");

            consulta.setParameter("pNome", pNome.toUpperCase());
            List<GrupoProdutoVO> lista = consulta.getResultList();
            if (lista.size() > 0) {
                grupoVO = lista.get(0);
                String nome = JOptionPane.showInputDialog("Forneca o nome do produto");
                float compra = Float.parseFloat(JOptionPane.showInputDialog(
                    "Forneca o preco de compra do produto"));
                float venda = Float.parseFloat(JOptionPane.showInputDialog(
                    "Forneca o preco de venda do produto"));
                float margem = Float.parseFloat(JOptionPane.showInputDialog(
                    "Forneca o percentual da margem de lucro do produto"));
                float promocao = Float.parseFloat(JOptionPane.showInputDialog(
                    "Forneca o percentual de promocao do produto"));
                int estoque = Integer.parseInt(JOptionPane.showInputDialog(
                    "Forneca o estoque inicial do produto"));

                produtoVO = new ProdutoVO();
                produtoVO.setNome(nome);
                produtoVO.setPrecoCompra(compra);
                produtoVO.setVenda(venda);
                produtoVO.setMargemLucro(margem);
                produtoVO.setPromocao(promocao);
                produtoVO.setEstoque(estoque);
                produtoVO.setGrupo(grupoVO); // Define o relacionamento com GrupoProdutoVO

                em.persist(produtoVO); // Persiste o ProdutoVO
                em.getTransaction().commit();
                System.out.println("Inclusao realizada com sucesso");
            } else {
                System.out.println("Grupo de Produto nao localizado");
            }
        } catch (Exception ex) {
            System.out.println("Inclusao nao realizada - " + ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }
}
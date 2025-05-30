package ifmt.cba.apps;

import java.util.List;
import javax.swing.JOptionPane;

import ifmt.cba.VO.ProdutoVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

public class Consulta2 {
    public static void main(String args[]) {
       
        EntityManagerFactory emf = null;
        EntityManager em = null;
       
        try {
            emf = Persistence.createEntityManagerFactory("UnidadeProdutos");
            em = emf.createEntityManager();
            String nome = JOptionPane.showInputDialog("Forneca o nome do produto a ser localizado");
          
            Query consulta = em
                 .createQuery("SELECT p FROM ProdutoVO p WHERE UPPER(p.nome) LIKE :pNome ORDER BY p.nome");
            consulta.setParameter("pNome", "%" + nome.toUpperCase() + "%");
            List<ProdutoVO> lista = consulta.getResultList();
            
            if (lista.size() > 0) {
                for (ProdutoVO produto : lista) {
                    System.out.println("---------------------------------------"); 
                    System.out.println("Codigo.......: " + produto.getCodigo());
                    System.out.println("Nome.........: " + produto.getNome());
                    System.out.println("Grupo........: " + produto.getGrupo().getNome());
                    System.out.println("Preco Compra.: " + produto.getPrecoCompra());
                    System.out.println("Preco Venda..: " + produto.getVenda());
                    System.out.println("Margem Lucro.: " + produto.getMargemLucro());
                    System.out.println("Estoque......: " + produto.getEstoque());
                }
            } else {
                System.out.println("Produto nao localizado");
            }
        } catch (Exception ex) {
            System.out.println("Consulta nao realizada - " + ex.getMessage());
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
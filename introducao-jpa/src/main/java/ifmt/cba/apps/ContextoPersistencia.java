package ifmt.cba.apps;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ContextoPersistencia {
    public static void main(String[] args) {
        
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try{
            emf=Persistence.createEntityManagerFactory("UnidadeProdutos");
            em = emf.createEntityManager();
            System.out.println("Contexto de persistencia criado com sucesso");
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Contexto de persistencia nao foi criado -- "+ ex.getMessage());
        }finally{
            if (em != null){
                em.close();
            }
            if (emf!= null){
                emf.close();
            }
        }
    }
}

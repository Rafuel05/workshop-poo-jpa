package ifmt.cba.VO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "produto")
public class ProdutoVO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int codigo;

    @Column(length = 50)
    private String nome;

    private int estoque;

    @Column(name = "preco_compra")
    private float precoCompra;

    @Column(name = "margem_lucro")
    private float margemLucro;

    private float promocao;

    private float venda;

    @ManyToOne
    private GrupoProdutoVO grupo;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public float getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(float precoCompra) {
        this.precoCompra = precoCompra;
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

    public float getVenda() {
        return venda;
    }

    public void setVenda(float venda) {
        this.venda = venda;
    }

    public GrupoProdutoVO getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoProdutoVO grupo) {
        this.grupo = grupo;
    }

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
        result = prime * result + ((grupo == null) ? 0 : grupo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProdutoVO other = (ProdutoVO) obj;
        if (codigo != other.codigo)
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (estoque != other.estoque)
            return false;
        if (Float.floatToIntBits(precoCompra) != Float.floatToIntBits(other.precoCompra))
            return false;
        if (Float.floatToIntBits(margemLucro) != Float.floatToIntBits(other.margemLucro))
            return false;
        if (Float.floatToIntBits(promocao) != Float.floatToIntBits(other.promocao))
            return false;
        if (Float.floatToIntBits(venda) != Float.floatToIntBits(other.venda))
            return false;
        if (grupo == null) {
            if (other.grupo != null)
                return false;
        } else if (!grupo.equals(other.grupo))
            return false;
        return true;
    }

    
}

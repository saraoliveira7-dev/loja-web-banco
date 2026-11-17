package br.edu.ifto.web.model;

import java.io.Serializable;

public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private String icone;

    public Produto() {
    }

    public Produto(int id, String nome, String descricao, double preco) {
        this(id, nome, descricao, preco, "");
    }

    public Produto(int id, String nome, String descricao, double preco, String icone) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.icone = icone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }
}

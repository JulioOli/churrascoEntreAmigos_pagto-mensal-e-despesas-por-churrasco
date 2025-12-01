
package com.churrascoapp.model;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String papel; // COORDENADOR, EGRESSO, DOCENTE

    public Usuario(){}

    public Usuario(String id, String nome, String email, String senha, String papel) {
        this.id = id; this.nome = nome; this.email = email; this.senha = senha; this.papel = papel;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getPapel() { return papel; }
    public void setPapel(String papel) { this.papel = papel; }

    @Override
    public String toString() {
        return "Usuario{id="+id+", nome="+nome+", email="+email+", papel="+papel+"}";
    }

    // CSV serialization: id;nome;email;senha;papel
    public String toCSV() {
        return String.join(";", id, nome, email, senha, papel);
    }

    public static Usuario fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 5) return null;
        return new Usuario(p[0], p[1], p[2], p[3], p[4]);
    }
}

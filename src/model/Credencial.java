package model;

public class Credencial {
    private String usuario;
    private String senha;
    private String servico;

    public Credencial(String servico, String usuario, String senha) {
        this.servico = servico;
        this.usuario = usuario;
        this.senha = senha;
    }
        public String getServico() {
            return servico;
        }

        public void setServico(String servico) {
            this.servico = servico;
        }

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha){
            this.senha = senha;
        }

    @Override
    public String toString() {
        return servico + ";" + usuario + ";" + senha;
    }

    public static Credencial fromString(String linha) {
        String[] partes = linha.split(";");
        if (partes.length != 3) return null;
        return new Credencial(partes[0], partes[1], partes[2]);
    }
    }


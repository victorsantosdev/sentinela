
package app.victor.sentinela.imbituba.bdclasses;

import app.victor.sentinela.imbituba.json.JsonParsingUsuario;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    @SerializedName(JsonParsingUsuario.USUARIO_ID)
    private int UsuarioID;
    @SerializedName(JsonParsingUsuario.USUARIO_NOME)
    private String UsuarioNome;
    @SerializedName(JsonParsingUsuario.USUARIO_SENHA)
    private String UsuarioSenha;
 
    public Usuario(){}
 
    public Usuario(String nome, String senha) {
        super();
        this.UsuarioNome = nome;
        this.UsuarioSenha = senha;
    } 
     
    public int getId() {
        return UsuarioID;
    }
    
    public void setId(int id) {
        this.UsuarioID = id;
    }
    
    public String getNome() {
        return UsuarioNome;
    }
    
    public void setNome(String nome) {
        this.UsuarioNome = nome;
    }
    
    public void setSenha(String senha) {
        this.UsuarioSenha = senha;
    }
    
    public String getSenha() {
        return UsuarioSenha;
    }
    
    @Override
    public String toString() {
        return "Usuario [id=" + UsuarioID + " nome= " + UsuarioNome + ", senha=" + UsuarioSenha + "]";
    }
}


package app.victor.sentinela.bdclasses;

import app.victor.sentinela.json.JsonParsingTerreno;
import com.google.gson.annotations.SerializedName;

public class Terreno {

    @SerializedName(JsonParsingTerreno.TERRENO_ID)
    private int TerrenoID;
    @SerializedName(JsonParsingTerreno.TERRENO_PROPRIETARIO)
    private String TerrenoProprietario;
    @SerializedName(JsonParsingTerreno.TERRENO_ENDERECO)
    private String TerrenoEndereco;
    @SerializedName(JsonParsingTerreno.TERRENO_NUMERO)
    private int TerrenoNumero;
    @SerializedName(JsonParsingTerreno.TERRENO_BAIRRO)
    private String TerrenoBairro;
    @SerializedName(JsonParsingTerreno.TERRENO_CIDADE)
    private String TerrenoCidade;
    @SerializedName(JsonParsingTerreno.TERRENO_ESTADO)
    private String TerrenoEstado;
    @SerializedName(JsonParsingTerreno.TERRENO_TOPOGRAFIA)
    private String TerrenoTopografia;
    @SerializedName(JsonParsingTerreno.TERRENO_AREA)
    private double TerrenoArea;
    @SerializedName(JsonParsingTerreno.TERRENO_CONFIGURACAO)
    private String TerrenoConfiguracao;
    @SerializedName(JsonParsingTerreno.TERRENO_SITUACAO_CADASTRAL)
    private String TerrenoSituacaoCadastral;
    @SerializedName(JsonParsingTerreno.TERRENO_FOTOPATH)
    private String TerrenoFotoPath;
    
    
    public int getTerreno_id() {
        return TerrenoID;
    }
    public void setTerreno_id(int id) {
        this.TerrenoID = id;
    }
    public String getProprietario() {
        return TerrenoProprietario;
    }
    public void setProprietario(String proprietario) {
        this.TerrenoProprietario = proprietario;
    }
    public String getEndereco() {
        return TerrenoEndereco;
    }
    public void setEndereco(String endereco) {
        this.TerrenoEndereco = endereco;
    }
    
    public int getNumero() {
        return TerrenoNumero;
    }
    
    public String getNumeroString() {
        return ""+TerrenoNumero;
    }
    
    public void setNumero(int numero) {
        this.TerrenoNumero = numero;
    }
    
    public String getBairro() {
        return TerrenoBairro;
    }
    public void setBairro(String bairro) {
        this.TerrenoBairro = bairro;
    }
    
    public String getCidade() {
        return TerrenoCidade;
    }
    public void setCidade(String cidade) {
        this.TerrenoCidade = cidade;
    }
    
    public String getEstado() {
        return TerrenoEstado;
    }
    public void setEstado(String estado) {
        this.TerrenoEstado = estado;
    }
    
    public String getTopografia() {
        return TerrenoTopografia;
    }
    public void setTopografia(String topografia) {
        this.TerrenoTopografia = topografia;
    }
    public double getArea() {
        return TerrenoArea;
    }
    
    public String getAreaString() {
        return ""+TerrenoArea;
    }
    
    public void setArea(double area) {
        this.TerrenoArea = area;
    }
    public String getConfiguracao() {
        return TerrenoConfiguracao;
    }
    public void setConfiguracao(String configuracao) {
        this.TerrenoConfiguracao = configuracao;
    }
    public String getSituacao_cadastral() {
        return TerrenoSituacaoCadastral;
    }
    public void setSituacao_cadastral(String situacao_cadastral) {
        this.TerrenoSituacaoCadastral = situacao_cadastral;
    } 
    
    public String getFotoPath() {
        return TerrenoFotoPath;
    }
    public void setFotoPath(String foto_path) {
        this.TerrenoFotoPath = foto_path;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(TerrenoArea);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((TerrenoBairro == null) ? 0 : TerrenoBairro.hashCode());
        result = prime * result + ((TerrenoCidade == null) ? 0 : TerrenoCidade.hashCode());
        result = prime * result + ((TerrenoConfiguracao == null) ? 0 : TerrenoConfiguracao.hashCode());
        result = prime * result + ((TerrenoEndereco == null) ? 0 : TerrenoEndereco.hashCode());
        result = prime * result + ((TerrenoEstado == null) ? 0 : TerrenoEstado.hashCode());
        result = prime * result + ((TerrenoFotoPath == null) ? 0 : TerrenoFotoPath.hashCode());
        result = prime * result + TerrenoID;
        result = prime * result + TerrenoNumero;
        result = prime * result + ((TerrenoProprietario == null) ? 0 : TerrenoProprietario.hashCode());
        result = prime * result + ((TerrenoSituacaoCadastral == null) ? 0 : TerrenoSituacaoCadastral.hashCode());
        result = prime * result + ((TerrenoTopografia == null) ? 0 : TerrenoTopografia.hashCode());
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
        Terreno other = (Terreno) obj;
        if (Double.doubleToLongBits(TerrenoArea) != Double.doubleToLongBits(other.TerrenoArea))
            return false;
        if (TerrenoBairro == null) {
            if (other.TerrenoBairro != null)
                return false;
        } else if (!TerrenoBairro.equals(other.TerrenoBairro))
            return false;
        if (TerrenoCidade == null) {
            if (other.TerrenoCidade != null)
                return false;
        } else if (!TerrenoCidade.equals(other.TerrenoCidade))
            return false;
        if (TerrenoConfiguracao == null) {
            if (other.TerrenoConfiguracao != null)
                return false;
        } else if (!TerrenoConfiguracao.equals(other.TerrenoConfiguracao))
            return false;
        if (TerrenoEndereco == null) {
            if (other.TerrenoEndereco != null)
                return false;
        } else if (!TerrenoEndereco.equals(other.TerrenoEndereco))
            return false;
        if (TerrenoEstado == null) {
            if (other.TerrenoEstado != null)
                return false;
        } else if (!TerrenoEstado.equals(other.TerrenoEstado))
            return false;
        if (TerrenoFotoPath == null) {
            if (other.TerrenoFotoPath != null)
                return false;
        } else if (!TerrenoFotoPath.equals(other.TerrenoFotoPath))
            return false;
        if (TerrenoID != other.TerrenoID)
            return false;
        if (TerrenoNumero != other.TerrenoNumero)
            return false;
        if (TerrenoProprietario == null) {
            if (other.TerrenoProprietario != null)
                return false;
        } else if (!TerrenoProprietario.equals(other.TerrenoProprietario))
            return false;
        if (TerrenoSituacaoCadastral == null) {
            if (other.TerrenoSituacaoCadastral != null)
                return false;
        } else if (!TerrenoSituacaoCadastral.equals(other.TerrenoSituacaoCadastral))
            return false;
        if (TerrenoTopografia == null) {
            if (other.TerrenoTopografia != null)
                return false;
        } else if (!TerrenoTopografia.equals(other.TerrenoTopografia))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Terreno [TerrenoID=" + TerrenoID + ", TerrenoProprietario=" + TerrenoProprietario + ", TerrenoEndereco=" + TerrenoEndereco + ", TerrenoNumero=" + TerrenoNumero
                + ", TerrenoBairro=" + TerrenoBairro + ", TerrenoCidade=" + TerrenoCidade + ", TerrenoEstado=" + TerrenoEstado + ", TerrenoTopografia=" + TerrenoTopografia
                + ", TerrenoArea=" + TerrenoArea + ", TerrenoConfiguracao=" + TerrenoConfiguracao + ", TerrenoSituacaoCadastral=" + TerrenoSituacaoCadastral + ", TerrenoFotoPath="
                + TerrenoFotoPath + "]";
    }
    
}

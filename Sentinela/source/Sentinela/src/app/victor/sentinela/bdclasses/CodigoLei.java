
package app.victor.sentinela.bdclasses;

import app.victor.sentinela.json.JsonParsingCodigoLei;

import com.google.gson.annotations.SerializedName;

public class CodigoLei {

    @SerializedName(JsonParsingCodigoLei.CODIGOLEI_ID)
    private int CodigoLeiID;
    @SerializedName(JsonParsingCodigoLei.CODIGOLEI_CODIGO)
    private String Codigo;
    @SerializedName(JsonParsingCodigoLei.CODIGOLEI_DESCRICAO)
    private String Descricao;
    @SerializedName(JsonParsingCodigoLei.CODIGOLEI_VALORINFRACAO)
    private double ValorInfracao;
    
    public int getCodigoLeiID() {
        return CodigoLeiID;
    }
    public void setCodigoLeiID(int id) {
        this.CodigoLeiID = id;
    }
    public String getCodigoLei() {
        return Codigo;
    }
    public void setCodigoLei(String codigo) {
        this.Codigo = codigo;
    }
    public String getDescricao() {
        return Descricao;
    }
    public void setDescricao(String descricao) {
        this.Descricao = descricao;
    }
    public double getValorInfracao() {
        return ValorInfracao;
    }
    public void setValorInfracao(double valor) {
        this.ValorInfracao = valor;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((Codigo == null) ? 0 : Codigo.hashCode());
        result = prime * result + CodigoLeiID;
        result = prime * result + ((Descricao == null) ? 0 : Descricao.hashCode());
        long temp;
        temp = Double.doubleToLongBits(ValorInfracao);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        CodigoLei other = (CodigoLei) obj;
        if (Codigo == null) {
            if (other.Codigo != null)
                return false;
        } else if (!Codigo.equals(other.Codigo))
            return false;
        if (CodigoLeiID != other.CodigoLeiID)
            return false;
        if (Descricao == null) {
            if (other.Descricao != null)
                return false;
        } else if (!Descricao.equals(other.Descricao))
            return false;
        if (Double.doubleToLongBits(ValorInfracao) != Double.doubleToLongBits(other.ValorInfracao))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "CodigoLei [CodigoLeiID=" + CodigoLeiID + ", Codigo=" + Codigo + ", Descricao=" + Descricao + ", ValorInfracao=" + ValorInfracao + "]";
    }
    
}

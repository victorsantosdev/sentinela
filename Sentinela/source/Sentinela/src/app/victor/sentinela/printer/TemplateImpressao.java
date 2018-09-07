package app.victor.sentinela.printer;

import android.os.Parcel;
import android.os.Parcelable;

public class TemplateImpressao implements Parcelable {
    private String Agente;
    private String DateTime;
    private String TipoInfracao;
    private String TerrenoProprietario;
    private String TerrenoEndereco;
    private int TerrenoNumero;
    private String TerrenoBairro;
    private String TerrenoCidade;
    private String TerrenoEstado;
    private String CodigoLei;
    private String CodigoLeiDescricao;
    
    public String getAgente() {
		return Agente;
	}
	public void setAgente(String agente) {
		Agente = agente;
	}
	public String getDateTime() {
		return DateTime;
	}
	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}

	public String getTipoInfracao() {
		return TipoInfracao;
	}
	public void setTipoInfracao(String tipoInfracao) {
		TipoInfracao = tipoInfracao;
	}
	public String getTerrenoProprietario() {
		return TerrenoProprietario;
	}
	public void setTerrenoProprietario(String terrenoProprietario) {
		TerrenoProprietario = terrenoProprietario;
	}
	public String getTerrenoEndereco() {
		return TerrenoEndereco;
	}
	public void setTerrenoEndereco(String terrenoEndereco) {
		TerrenoEndereco = terrenoEndereco;
	}
	public int getTerrenoNumero() {
		return TerrenoNumero;
	}
	
	   public String getTerrenoNumeroString() {
	        return ""+TerrenoNumero;
	    }
	    
	
	public void setTerrenoNumero(int terrenoNumero) {
		TerrenoNumero = terrenoNumero;
	}
	public String getTerrenoBairro() {
		return TerrenoBairro;
	}
	public void setTerrenoBairro(String terrenoBairro) {
		TerrenoBairro = terrenoBairro;
	}
	public String getTerrenoCidade() {
		return TerrenoCidade;
	}
	public void setTerrenoCidade(String terrenoCidade) {
		TerrenoCidade = terrenoCidade;
	}
	public String getTerrenoEstado() {
		return TerrenoEstado;
	}
	public void setTerrenoEstado(String terrenoEstado) {
		TerrenoEstado = terrenoEstado;
	}
	public String getCodigoLei() {
		return CodigoLei;
	}
	public void setCodigoLei(String codigoLei) {
		CodigoLei = codigoLei;
	}
	public String getCodigoLeiDescricao() {
		return CodigoLeiDescricao;
	}
	public void setCodigoLeiDescricao(String codigoLeiDescricao) {
		CodigoLeiDescricao = codigoLeiDescricao;
	}
	public String getPrazoRegularizacao() {
		return PrazoRegularizacao;
	}
	public void setPrazoRegularizacao(String prazoRegularizacao) {
		PrazoRegularizacao = prazoRegularizacao;
	}
	public double getValorInfracao() {
		return ValorInfracao;
	}
	public void setValorInfracao(double valorInfracao) {
		ValorInfracao = valorInfracao;
	}
	private String PrazoRegularizacao;
    private double ValorInfracao;
	@Override
	public String toString() {
		return "TemplateImpressao [Agente=" + Agente + ", DateTime=" + DateTime + ", TipoInfracao="
				+ TipoInfracao + ", TerrenoProprietario=" + TerrenoProprietario + ", TerrenoEndereco=" + TerrenoEndereco
				+ ", TerrenoNumero=" + TerrenoNumero + ", TerrenoBairro=" + TerrenoBairro + ", TerrenoCidade="
				+ TerrenoCidade + ", TerrenoEstado=" + TerrenoEstado + ", CodigoLei=" + CodigoLei
				+ ", CodigoLeiDescricao=" + CodigoLeiDescricao + ", PrazoRegularizacao=" + PrazoRegularizacao
				+ ", ValorInfracao=" + ValorInfracao + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Agente == null) ? 0 : Agente.hashCode());
		result = prime * result + ((CodigoLei == null) ? 0 : CodigoLei.hashCode());
		result = prime * result + ((CodigoLeiDescricao == null) ? 0 : CodigoLeiDescricao.hashCode());
		result = prime * result + ((DateTime == null) ? 0 : DateTime.hashCode());
		result = prime * result + ((PrazoRegularizacao == null) ? 0 : PrazoRegularizacao.hashCode());
		result = prime * result + ((TerrenoBairro == null) ? 0 : TerrenoBairro.hashCode());
		result = prime * result + ((TerrenoCidade == null) ? 0 : TerrenoCidade.hashCode());
		result = prime * result + ((TerrenoEndereco == null) ? 0 : TerrenoEndereco.hashCode());
		result = prime * result + ((TerrenoEstado == null) ? 0 : TerrenoEstado.hashCode());
		result = prime * result + TerrenoNumero;
		result = prime * result + ((TerrenoProprietario == null) ? 0 : TerrenoProprietario.hashCode());
		result = prime * result + ((TipoInfracao == null) ? 0 : TipoInfracao.hashCode());
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
		TemplateImpressao other = (TemplateImpressao) obj;
		if (Agente == null) {
			if (other.Agente != null)
				return false;
		} else if (!Agente.equals(other.Agente))
			return false;
		if (CodigoLei == null) {
			if (other.CodigoLei != null)
				return false;
		} else if (!CodigoLei.equals(other.CodigoLei))
			return false;
		if (CodigoLeiDescricao == null) {
			if (other.CodigoLeiDescricao != null)
				return false;
		} else if (!CodigoLeiDescricao.equals(other.CodigoLeiDescricao))
			return false;
		if (DateTime == null) {
			if (other.DateTime != null)
				return false;
		} else if (!DateTime.equals(other.DateTime))
			return false;
		
		if (PrazoRegularizacao == null) {
			if (other.PrazoRegularizacao != null)
				return false;
		} else if (!PrazoRegularizacao.equals(other.PrazoRegularizacao))
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
		if (TerrenoNumero != other.TerrenoNumero)
			return false;
		if (TerrenoProprietario == null) {
			if (other.TerrenoProprietario != null)
				return false;
		} else if (!TerrenoProprietario.equals(other.TerrenoProprietario))
			return false;
		if (TipoInfracao == null) {
			if (other.TipoInfracao != null)
				return false;
		} else if (!TipoInfracao.equals(other.TipoInfracao))
			return false;
		if (Double.doubleToLongBits(ValorInfracao) != Double.doubleToLongBits(other.ValorInfracao))
			return false;
		return true;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
    
	public TemplateImpressao(String agente, String dateTime, String tipoInfracao, String terrenoProprietario,
			String terrenoEndereco, int terrenoNumero, String terrenoBairro, String terrenoCidade, String terrenoEstado,
			String codigoLei, String codigoLeiDescricao) {
        this.Agente = agente;
        this.DateTime = dateTime;
        this.TipoInfracao = tipoInfracao;
        this.TerrenoProprietario = terrenoProprietario;
        this.TerrenoEndereco = terrenoEndereco;
        this.TerrenoNumero = terrenoNumero;
        this.TerrenoBairro = terrenoBairro;
        this.TerrenoCidade = terrenoCidade;
        this.TerrenoEstado = terrenoEstado;
        this.CodigoLei = codigoLei;
        this.CodigoLeiDescricao = codigoLeiDescricao;
    }
	
	public TemplateImpressao() {}

    public TemplateImpressao(Parcel source) {
    	Agente = source.readString();
        DateTime = source.readString();
        TipoInfracao = source.readString();
        TerrenoProprietario = source.readString();
        TerrenoEndereco = source.readString();
        TerrenoNumero = source.readInt();
        TerrenoBairro = source.readString();
        TerrenoCidade = source.readString();
        TerrenoEstado = source.readString();
        CodigoLei = source.readString();
        CodigoLeiDescricao = source.readString();
    }
	

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Agente);
        dest.writeString(DateTime);
        dest.writeString(TipoInfracao);
        dest.writeString(TerrenoProprietario);
        dest.writeString(TerrenoEndereco);
        dest.writeInt(TerrenoNumero);
        dest.writeString(TerrenoBairro);
        dest.writeString(TerrenoCidade);
        dest.writeString(TerrenoEstado);
        dest.writeString(CodigoLei);
        dest.writeString(CodigoLeiDescricao);

    }


    public static final Creator<TemplateImpressao> CREATOR = new Creator<TemplateImpressao>() {
        @Override
        public TemplateImpressao[] newArray(int size) {
            return new TemplateImpressao[size];
        }

        @Override
        public TemplateImpressao createFromParcel(Parcel source) {
            return new TemplateImpressao(source);
        }
    };
    
    public StringBuffer formatPrintData() {
    	
    	StringBuffer sb = new StringBuffer();
        sb.append("{reset}{center}{w}{h} "+ TipoInfracao);
        sb.append("{br}");
        sb.append("{br}");
        sb.append("{reset}{b}Agente: {left}{h}"+Agente+"{br}");
        sb.append("{reset}{b}Data/Hora: {left}{h}"+DateTime+"{br}");
        sb.append("{reset}{b}Proprietario: {left}{h}"+TerrenoProprietario+"{br}");
        sb.append("{reset}{b}Endereco: {h}"+TerrenoEndereco+", " + TerrenoNumero+"{br}");
        sb.append("{reset}{b}Bairro: {left}{h}"+TerrenoBairro+"{br}");
        sb.append("{reset}{b}Cidade/Estado: {left}{h}"+TerrenoCidade+"/"+ TerrenoEstado+"{br}");
        sb.append("{reset}{b}Codigo Lei: {left}{h}"+CodigoLei+"{br}");
        sb.append("{reset}{b}Descricao: {left}{h}"+CodigoLeiDescricao+"{br}");

        sb.append("{br}");
        sb.append("{br}");
        
    	return sb;
   	
    }

	

}

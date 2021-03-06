
package app.victor.sentinela.bdclasses;

public class Infracao {
    private int InfracaoID;
    private int AgenteID;
    private int TerrenoID;
    private int CodigoLeiID;
    private String InfracaoFotoPath;
    private String InfracaoData;
    
    public int getInfracaoID() {
        return InfracaoID;
    }
    public void setInfracaoID(int infracaoID) {
        InfracaoID = infracaoID;
    }
    public int getAgenteID() {
        return AgenteID;
    }
    public void setAgenteID(int agenteID) {
        AgenteID = agenteID;
    }
    public int getTerrenoID() {
        return TerrenoID;
    }
    public void setTerrenoID(int terrenoID) {
        TerrenoID = terrenoID;
    }
    public int getCodigoLeiID() {
        return CodigoLeiID;
    }
    public void setCodigoLeiID(int codigoLeiID) {
        CodigoLeiID = codigoLeiID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + AgenteID;
        result = prime * result + CodigoLeiID;
        result = prime * result + InfracaoID;
        result = prime * result + TerrenoID;
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
        Infracao other = (Infracao) obj;
        if (AgenteID != other.AgenteID)
            return false;
        if (CodigoLeiID != other.CodigoLeiID)
            return false;
        if (InfracaoID != other.InfracaoID)
            return false;
        if (TerrenoID != other.TerrenoID)
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "Infracao [InfracaoID=" + InfracaoID + ", AgenteID=" + AgenteID + ", TerrenoID=" + TerrenoID + ", CodigoLeiID=" + CodigoLeiID + "]";
    }

}

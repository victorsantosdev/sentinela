package app.victor.sentinela.bdclasses;

public class Notificacao {
    
    private int NotificacaoID;
    private int AgenteID;
    private int TerrenoID;
    private int CodigoLeiID;
    
    public int getNotificacaoID() {
        return NotificacaoID;
    }
    public void setNotificacaoID(int notificacaoID) {
        NotificacaoID = notificacaoID;
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
        result = prime * result + NotificacaoID;
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
        Notificacao other = (Notificacao) obj;
        if (AgenteID != other.AgenteID)
            return false;
        if (CodigoLeiID != other.CodigoLeiID)
            return false;
        if (NotificacaoID != other.NotificacaoID)
            return false;
        if (TerrenoID != other.TerrenoID)
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Notificacao [NotificacaoID=" + NotificacaoID + ", AgenteID=" + AgenteID + ", TerrenoID=" + TerrenoID + ", CodigoLeiID=" + CodigoLeiID + "]";
    }
    
}

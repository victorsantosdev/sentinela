package app.victor.sentinela.imbituba.bdclasses;

public class Notificacao {
    
    private int NotificacaoID;
    private int AgenteID;
    private int TerrenoID;
    private int CodigoLeiID;
    private String NotificacaoFotoPath;
    private String NotificacaoData;
    
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
    public String getNotificacaoFotoPath() {
        return NotificacaoFotoPath;
    }
    public void setNotificacaoFotoPath(String notificacaoFotoPath) {
        NotificacaoFotoPath = notificacaoFotoPath;
    }
    public String getNotificacaoData() {
        return NotificacaoData;
    }
    public void setNotificacaoData(String notificacaoData) {
        NotificacaoData = notificacaoData;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + AgenteID;
        result = prime * result + CodigoLeiID;
        result = prime * result + NotificacaoID;
        result = prime * result + ((NotificacaoData == null) ? 0 : NotificacaoData.hashCode());
        result = prime * result + ((NotificacaoFotoPath == null) ? 0 : NotificacaoFotoPath.hashCode());
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
        if (NotificacaoData == null) {
            if (other.NotificacaoData != null)
                return false;
        } else if (!NotificacaoData.equals(other.NotificacaoData))
            return false;
        if (NotificacaoFotoPath == null) {
            if (other.NotificacaoFotoPath != null)
                return false;
        } else if (!NotificacaoFotoPath.equals(other.NotificacaoFotoPath))
            return false;
        if (TerrenoID != other.TerrenoID)
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Notificacao [NotificacaoID=" + NotificacaoID + ", AgenteID=" + AgenteID + ", TerrenoID=" + TerrenoID + ", CodigoLeiID=" + CodigoLeiID + ", NotificacaoFotoPath="
                + NotificacaoFotoPath + ", NotificacaoData=" + NotificacaoData + "]";
    }
    
}

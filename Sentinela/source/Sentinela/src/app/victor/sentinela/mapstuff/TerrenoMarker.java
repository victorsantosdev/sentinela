package app.victor.sentinela.mapstuff;
import app.victor.sentinela.bdclasses.Terreno;

    public class TerrenoMarker {
    
        private String mLabel;
        private Double mLatitude;
        private Double mLongitude;
        private Terreno mTerreno;
        
        public TerrenoMarker(String label, Double latitude, Double longitude, Terreno terreno)
        {
            this.mLabel = label;
            this.mLatitude = latitude;
            this.mLongitude = longitude;
            this.mTerreno = terreno;
        }

        public String getmLabel()
        {
            return mLabel;
        }

        public void setmLabel(String mLabel)
        {
            this.mLabel = mLabel;
        }

        public Double getmLatitude()
        {
            return mLatitude;
        }

        public void setmLatitude(Double mLatitude)
        {
            this.mLatitude = mLatitude;
        }

        public Double getmLongitude()
        {
            return mLongitude;
        }

        public void setmLongitude(Double mLongitude)
        {
            this.mLongitude = mLongitude;
        }
        
        public Terreno getmTerreno()
        {
            return mTerreno;
        }

        public void setmTerreno(Terreno mTerreno)
        {
            this.mTerreno = mTerreno;
        }
    }
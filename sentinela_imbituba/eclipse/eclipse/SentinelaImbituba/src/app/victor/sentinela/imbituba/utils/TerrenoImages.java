package app.victor.sentinela.imbituba.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class TerrenoImages {
    
        private Bitmap foto_bin;
        private String foto_nome;

        public Bitmap foto_bin() {
           return foto_bin;
        }

        public String foto_nome() {
           return foto_nome;
        }   

    public static class DownloadTerrenosImagesTask extends AsyncTask<String, Integer, TerrenoImages>{
        private Context mContext;
        

        public DownloadTerrenosImagesTask (Context context) {
            mContext = context;
        }
        
        public TerrenoImages downloadUrl(String strUrl, TerrenoImages tImg) throws IOException{
    
            InputStream iStream = null;
            tImg.foto_bin = null;
            tImg.foto_nome = null;
            try{
                
                //URL url = new URL("http://192.168.137.1:30000/timg?path=/"+strUrl);
                
                URL url = new URL("http://10.0.2.2:30000/timg?path=/"+strUrl); //localhost with the emulator
                Log.v("URL imagens", "url_imagens: "+url);

                String img_name = strUrl.replaceAll("app_imagensterrenos/", "");
                tImg.foto_nome = img_name;
                Log.v("Nome da imagem a ser salva", "nome da imagem a ser salva: "+img_name);

                
                /** Creating an http connection to communcate with url */
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                /** Connecting to url */
                urlConnection.connect();

                /** Reading data from url */
                iStream = urlConnection.getInputStream();

                /** Creating a bitmap from the stream returned from the url */
                tImg.foto_bin = BitmapFactory.decodeStream(iStream);

            }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
            }finally{
                iStream.close();
            }
            //return bitmap;
            return tImg;
        }       
        
        @Override
        protected TerrenoImages doInBackground(String... url) {
            TerrenoImages imageData = new TerrenoImages();
            try{
                imageData = downloadUrl(url[0], imageData);
            }catch(Exception e){
                Log.d("Background Image Task",e.toString());
            }
            return imageData;
        }
        
        
        protected void onPostExecute(TerrenoImages terrenosImg) {

            /** Getting a reference to ImageView to display the
             * downloaded image
             */
            /*debug purposes
            ImageView iView = (ImageView) findViewById(R.id.imageview_teste);

            /// Displaying the downloaded image
            iView.setImageBitmap(result);

            // Showing a message, on completion of download process
            Toast.makeText(getBaseContext(), "Image downloaded successfully", Toast.LENGTH_SHORT).show();
            */         
            ImageHandling.saveToTerrenosFolder(mContext, terrenosImg.foto_bin, terrenosImg.foto_nome);

        }
    }

}

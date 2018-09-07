package app.victor.sentinela.activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color; //usado se quiser colocar cor quando o botao eh habilitado/desabilitado
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import app.victor.sentinela.R;
import app.victor.sentinela.bdclasses.CodigoLei;
import app.victor.sentinela.bdclasses.Terreno;
import app.victor.sentinela.bdclasses.Usuario;
import app.victor.sentinela.bancodados.CodigoLeiORM;
import app.victor.sentinela.bancodados.DatabaseWrapper;
import app.victor.sentinela.bancodados.TerrenoORM;
import app.victor.sentinela.bancodados.UsuarioORM;
import app.victor.sentinela.json.*;
import app.victor.sentinela.login.LoginActivity;
import app.victor.sentinela.utils.TerrenoImages;

public class HomeActivity extends Activity {
    private Button btn_sinc;
    public Button btn_startSession;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity_layout);
        btn_startSession = (Button) findViewById(R.id.btn_startsession);

        //testa se a base de dados ja esta criada
        if (!DatabaseWrapper.doesDatabaseExist(this)) {
           desabilitaBotao(btn_startSession);
        } else {
            habilitaBotao(btn_startSession);
        }
    }        

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void btn_startsession(View view) {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void desabilitaBotao(Button btn) {
        //btn.setTextColor(Color.RED);
        btn.setClickable(false);
    }
    
    public void habilitaBotao(Button btn) {
        //btn.setTextColor(Color.GREEN); 
        btn.setClickable(true);
    }
    
    public void btn_sincbanco(View view) {
        if(isNetworkAvailable()){
            btn_sinc = (Button) findViewById(R.id.btn_sincbanco);
            btn_sinc.setClickable(false);

            SincronizaBanco sincronizaBanco = new SincronizaBanco(HomeActivity.this);
            sincronizaBanco.execute();
        }else{
            Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
        }
      
       
    }

    public void infoAutor(View view) {
        Toast.makeText(getApplicationContext(), "Autor: Victor Santos\nemail: victor.inboxfx@gmail.com\nContato: (48) 8411-9029", Toast.LENGTH_LONG).show();
    }

    /*******************************************************************************/
    private boolean isNetworkAvailable(){
        boolean available = false;
        /** Getting the system's connectivity service */
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        /** Getting active network interface  to get the network's status */
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo !=null && networkInfo.isAvailable())
            available = true;

        /** Returning the status of the network */
        return available;
    }

    
    /************************ refatorar utilizando intent services ******************************************/

    private class SincronizaBanco extends AsyncTask<String[], Void, String[]> {
        private Context mContext;

        public SincronizaBanco(Context context) {
            mContext = context;
        }

        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {

            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[8192];
                n = in.read(b);

                if (n > 0)
                    out.append(new String(b, 0, n));
            }
            return out.toString();
        }

        protected String[] doInBackground(String[]... params) {
            List<String> result = new ArrayList<String>();
            List<String> exception = new ArrayList<String>();
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            
            
            //rede santos
            //String ServerIP = "http://192.168.25.81";
            //rede lmpt
            String ServerIP = "http://10.42.0.96";
            //rede bia
            //String ServerIP = "http://192.168.137.1";
            //localhost with the emulator
            //String ServerIP = "http://10.0.2.2";
            
            String ServerPort = "30000";
            String ServerURL = ServerIP+":"+ServerPort;

            HttpGet httpGet = new HttpGet(ServerURL+"/terrenos");
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                result.add(getASCIIContentFromEntity(entity));

            } catch (Exception e) {
                Log.e("ERROR", "Can't connect to webservice:terrenos");
                exception.add(e.getLocalizedMessage());
                return exception.toArray(new String[result.size()]);

            }
            httpGet = new HttpGet(ServerURL+"/codigoslei");
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                result.add(getASCIIContentFromEntity(entity));

            } catch (Exception e) {
                Log.e("ERROR", "Can't connect to webservice: codigoslei");
                exception.add(e.getLocalizedMessage());
                return exception.toArray(new String[result.size()]);
            }

            httpGet = new HttpGet(ServerURL+"/usuarios");
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                result.add(getASCIIContentFromEntity(entity));

            } catch (Exception e) {
                Log.e("ERROR", "Can't connect to webservice: usuarios");
                exception.add(e.getLocalizedMessage());
                return exception.toArray(new String[result.size()]);
            }       
            return result.toArray(new String[result.size()]);
        }

        /*
         * aqui que acontece de fato a criacao do banco sqlite local a partir do
         * banco externo
         */
        @Override
        protected void onPostExecute(String[] results) {
            if (results.length > 0) {
                /* Terrenos */
                List<Terreno> terrenosList = JsonParsingTerreno.NativeJsonParseMethodForTerrenos(results[0].toString());

                for (int i = 0; i < terrenosList.size(); i++) {
                    Log.v("Debug Lista", "GSON object" + i + "terreno: " + terrenosList.get(i).toString());
                    TerrenoORM.insertTerreno(mContext, terrenosList.get(i));
                    
                    TerrenoImages.DownloadTerrenosImagesTask downloadImageTask = new TerrenoImages.DownloadTerrenosImagesTask(HomeActivity.this);

                    // Starting the task created above 
                    downloadImageTask.execute(terrenosList.get(i).getFotoPath());
                }

                /* Codigos de Lei */
                List<CodigoLei> codigosLeiList = JsonParsingCodigoLei.NativeJsonParseMethodForCodigosInfracoes(results[1].toString());

                for (int i = 0; i < codigosLeiList.size(); i++) {
                    Log.v("Debug Lista", "GSON object" + i + "CodigoLei: " + codigosLeiList.get(i).toString());
                    CodigoLeiORM.insertCodigoLei(mContext, codigosLeiList.get(i));
                }

                /* Usuarios */

                // printa o parse do json recebido do servidor
                List<Usuario> usuariosList = JsonParsingUsuario.NativeJsonParseMethodForUsuarios(results[2].toString());

                for (int i = 0; i < usuariosList.size(); i++) {
                    Log.v("Debug Lista", "GSON object" + i + "usuario: " + usuariosList.get(i).toString());
                    UsuarioORM.insertUsuario(mContext, usuariosList.get(i));
                }

                DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(mContext);
                Log.v("Debug BD", "Fechando o banco na Main Activity");
                databaseWrapper.closeDB();

            } else {
                Log.e("ERROR", "No response from webservice");
                Toast.makeText(getApplicationContext(), "No response from webservice", Toast.LENGTH_LONG).show();

            }
            Button b_ss = (Button) findViewById(R.id.btn_startsession);
            Button b_sinc = (Button) findViewById(R.id.btn_sincbanco);

            b_sinc.setClickable(true);
            habilitaBotao(b_ss);
            Toast.makeText(getApplicationContext(), "Tabelas sincronizadas com sucesso!\nTerrenos, Multas e Usuarios.", Toast.LENGTH_LONG).show();

        }

    }
}

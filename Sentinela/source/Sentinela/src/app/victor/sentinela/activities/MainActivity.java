package app.victor.sentinela.activities;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import app.victor.sentinela.R;
import app.victor.sentinela.bancodados.TerrenoORM;

public class MainActivity extends Activity {
    private String cidadeSelecionada;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_layout);
        setUserSessionData();

        spinner = (Spinner) findViewById(R.id.cidades_spinner);
        List<String> cidadeTerrenos = TerrenoORM.getCidadeTerrenos(MainActivity.this);
        for (int i = 0; i < cidadeTerrenos.size(); i++) {
            Log.i("Aplicacao", "Aplicacao: Cidade na lista: " + cidadeTerrenos.get(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, cidadeTerrenos);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                cidadeSelecionada = spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {}
        });
    }

    public void btn_prosseguirMapas(View view) {
        Toast.makeText(MainActivity.this, "cidade selecionada: " + cidadeSelecionada, Toast.LENGTH_SHORT).show();
        Intent goMaps = new Intent(this, app.victor.sentinela.mapstuff.MapsActivity.class);
        goMaps.putExtra("cidadeSelecionada", cidadeSelecionada);
        // moveTasktoBack antes de uma activity com maps faz com que o
        // aplicativo feche sem retornar nenhum tipo de warning nem log
        // moveTaskToBack(true);
        startActivity(goMaps);
    }

    public void setUserSessionData() {
        TextView sessionInfo = (TextView) findViewById(R.id.username_sessionData);
        SharedPreferences sharedPreferences = getSharedPreferences(app.victor.sentinela.login.LoginActivity.userPreferences, Context.MODE_PRIVATE);

        String usuarioLogado = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.usuarioLogado, "");
        String usuarioUltimoLogin = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.ultimoLogin, "");
        if (!usuarioLogado.equals("") && !usuarioUltimoLogin.equals("")) {
            sessionInfo.setText("Usu·rio Logado: " + usuarioLogado + "\n⁄ltimo login: " + usuarioUltimoLogin);
        }
    }

    public void logout(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(app.victor.sentinela.login.LoginActivity.userPreferences, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        String usuarioLogadoCheck = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.usuarioLogado, "");
        if (!usuarioLogadoCheck.equals("")) {
            Toast.makeText(getApplicationContext(), "Logout do usu·rio "+ usuarioLogadoCheck, Toast.LENGTH_LONG).show();
        editor.clear();
        editor.commit();      
        }

        moveTaskToBack(true); 
        MainActivity.this.finish();
        Intent goLogin = new Intent(this, app.victor.sentinela.login.LoginActivity.class);
        //Limpa a stack de activities pra resolver o bug do usu√°rio apertar o bot√£o de voltar e ele voltar na sess√£o j√° logado pelo sharedPreferences
        goLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goLogin);
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

}

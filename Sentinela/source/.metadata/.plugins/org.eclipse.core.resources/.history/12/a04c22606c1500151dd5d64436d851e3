/* Autor: Victor Santos
 * Aplicativo de questionário Android 
 * 30/01/2015
 * victor.ifsc@gmail.com
 * 
 * Activity onde o usuário escolhe quantas perguntas ele deseja criar. 
 * 
 * */

package app.victor.questionario.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import app.victor.questionario.login.LoginActivity;
import app.victor.questionarioandroid.R;

public class QuantidadePerguntasActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quantidadeperguntas_layout);
        setUserSessionData();
    }
    
    public void btn_prosseguirCadastro(View view) {

        EditText etQuantidadePerguntas   = (EditText)findViewById(R.id.num_perguntas);
        if (etQuantidadePerguntas.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Digite o número de perguntas que deseja cadastrar", Toast.LENGTH_SHORT).show();
        } 
        else {
            int qtdPerguntas = Integer.parseInt(etQuantidadePerguntas.getText().toString().trim());  
            Intent intent = new Intent(this, RegistraPerguntasActivity.class);
            //Passa o valor de número de perguntas para a activity de cadastro
            intent.putExtra("numPerguntas", qtdPerguntas);
            startActivity(intent);
        }
    }
    
    public void setUserSessionData() {
        TextView sessionInfo=(TextView)findViewById(R.id.username_sessionData); 
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.userPreferences, Context.MODE_PRIVATE);

        String usuarioLogado = sharedPreferences.getString(LoginActivity.usuarioLogado, "");
        String usuarioUltimoLogin = sharedPreferences.getString(LoginActivity.ultimoLogin, "");
        if (!usuarioLogado.trim().equals("") && !usuarioUltimoLogin.trim().equals("")) {
            sessionInfo.setText("Usuário Logado: " + usuarioLogado +"\nÚltimo login: " + usuarioUltimoLogin);
        }
    }

    public void logout(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.userPreferences, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        String usuarioLogadoCheck = sharedPreferences.getString(LoginActivity.usuarioLogado, "");
        if (!usuarioLogadoCheck.equals("")) {
            Toast.makeText(getApplicationContext(), "Logout do usuário "+ usuarioLogadoCheck, Toast.LENGTH_LONG).show();
        editor.clear();
        editor.commit();
        }
        QuantidadePerguntasActivity.this.finish();
        Intent goLogin = new Intent(this, LoginActivity.class);
        //Limpa a stack de activities pra resolver o bug do usuário apertar o botão de voltar e ele voltar na sessão já logado pelo sharedPreferences
        goLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goLogin);
    }
    
}
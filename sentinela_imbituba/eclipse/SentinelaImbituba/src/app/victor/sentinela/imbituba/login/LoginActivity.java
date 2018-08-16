package app.victor.sentinela.imbituba.login;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import app.victor.sentinela.imbituba.activities.MainActivity;
import app.victor.sentinela.imbituba.bancodados.UsuarioORM;
import app.victor.sentinela.imbituba.R;

@SuppressLint("NewApi")
public class LoginActivity extends Activity {
    private EditText etUsername;
    private EditText etPassword;
    //campos utilizados no sharedPreferences
    public static final String userPreferences = "userPreferences";
    public static final String usuarioLogado = "usuarioLogado";
    public static final String ultimoLogin = "ultimoLogin";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity_layout);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_senha);

        //limpa o conteúdo do sharedPreferences quando sobe
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.userPreferences, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        //limpa o conteúdo do sharedPreferences quando sobe
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.userPreferences, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        //reinsere a activity atual quando apertado o botão voltar, para evitar que saia da aplicação
        //devido a limpeza na stack de funções no método logout das demais activities 
        startActivity(new Intent(this,LoginActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        sharedPreferences = getSharedPreferences(userPreferences, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(usuarioLogado)) {
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        } 
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SimpleDateFormat")
    public void login_action(View view) {
        String errosParse = "";
        // variável utilizada para armazenar a resposta do banco ao checar
        // usuário e senha
        String retBanco = "";
        // Faz a verificacao em camadas, para não apresentar todos os erros de
        // uma vez sem necessidade
        int estagioVerificacao = 0;

        if (etUsername.getText().toString().trim().equals("") && estagioVerificacao == 0) {
            errosParse += "Nome do usuário vazio.\n";
            Toast.makeText(getApplicationContext(), errosParse, Toast.LENGTH_SHORT).show();
            estagioVerificacao = 1;
        }
        if (etPassword.getText().toString().trim().equals("") && estagioVerificacao == 0) {
            errosParse += "Senha vazia.\n";
            Toast.makeText(getApplicationContext(), errosParse, Toast.LENGTH_SHORT).show();
            estagioVerificacao = 1;
        }

        if (estagioVerificacao == 0) {
            retBanco += UsuarioORM.loginValido(this, etUsername.getText().toString().trim(), etPassword.getText().toString().trim());

            if (!retBanco.trim().equals("")) {
                errosParse += retBanco.trim();
                Toast.makeText(getApplicationContext(), errosParse, Toast.LENGTH_SHORT).show();
            }

            if (errosParse.equals("")) {
                //Cadastra a sessão com o nome do usuário logado através do contato com o BD

                Editor editor = sharedPreferences.edit();
                //limpa o sharedPreferences antes de gravar as informações do Login
                editor.clear();
                editor.commit();

                String typedUsername = etUsername.getText().toString().trim();

                //marca a data e a hora do último login
                SimpleDateFormat dataHora = new SimpleDateFormat("dd/MM/yyyy--HH:mm");
                String currentDateandTime = dataHora.format(new Date());

                editor.putString(usuarioLogado, typedUsername);
                editor.putString(ultimoLogin, currentDateandTime);
                editor.commit();

                Toast.makeText(getApplicationContext(), "Bem-vindo usuário: " + etUsername.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
            }
        }
    }
}

/* Autor: Victor Santos
 * Aplicativo de questionário Android 
 * 31/01/2015
 * victor.ifsc@gmail.com
 * 
 * Activity de registro de perguntas. O usuário especifica na Activity QuantidadePerguntasActivity quantas 
 * perguntas ele deseja criar e as elabora nesta Activity.
 * 
 * */

package app.victor.questionario.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import app.victor.questionario.bancodados.Perguntas;
import app.victor.questionario.bancodados.PerguntasORM;
import app.victor.questionario.login.LoginActivity;
import app.victor.questionarioandroid.R;

public class RegistraPerguntasActivity extends Activity {

    //variáveis posicionadas de maneira global à classe OnCreate para terem seus valores
    //acessados pela função btn_cadastraPerguntas
    EditText campoPergunta;
    List<EditText> allCamposPergunta;
    Spinner respostaEscolha;
    List<Spinner> allRespostasEscolha;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarperguntas_layout);
        setUserSessionData();

        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.ll_perguntas);

        // Recebe a quantidade de perguntas passada pelo Intent
        Bundle extras = getIntent().getExtras();
        int numPerguntas = extras.getInt("numPerguntas");

        allCamposPergunta = new ArrayList<EditText>();
        allRespostasEscolha = new ArrayList<Spinner>();

        // gera a quantidade de textViews e editTexts proporcionais ao número de
        // perguntas a serem geradas

        for (int i = 1; i <= numPerguntas; i++) {

            // monta o cabeçalho da pergunta
            TextView cabecalho_pergunta = new TextView(this);
            cabecalho_pergunta.setPadding(0, 75, 0, 0); // espacamento entre
            // componentes
            cabecalho_pergunta.setText("Pergunta nº" + i);
            cabecalho_pergunta.setId(i);
            linearlayout.addView(cabecalho_pergunta);

            // monta o editText para o usuario digitar a pergunta
            campoPergunta = new EditText(RegistraPerguntasActivity.this);
            allCamposPergunta.add(campoPergunta);
            campoPergunta.setId(i);
            campoPergunta.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            linearlayout.addView(campoPergunta);

            // monta o cabeçalho da pergunta
            TextView tipo_resposta = new TextView(this);
            tipo_resposta.setText("Tipo de resposta para a pergunta nº" + i);
            tipo_resposta.setId(i);
            linearlayout.addView(tipo_resposta);

            // //monta a spinner para o usuario escolher o tipo de resposta
            respostaEscolha = new Spinner(RegistraPerguntasActivity.this);
            allRespostasEscolha.add(respostaEscolha);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.tiporesposta_array));
            respostaEscolha.setAdapter(adapter);
            linearlayout.addView(respostaEscolha);
        }
    }

    public void btn_cadastraPerguntas(View view) {
        // Coletar os dados contidos nos EditTexts e cadastra as perguntas no
        // banco de dados Sqlite

        String[] perguntas_texto = new String[allCamposPergunta.size()];
        String[] perguntas_tiporesposta = new String[allRespostasEscolha.size()];
        String errosParse = "";
        //Faz a verificacao em camadas, para não apresentar todos os erros de uma vez sem necessidade
        int estagioVerificacao = 0;

        //teste de erros: campos de pergunta vazios ou com espaço (tentativa de burlar)
        for (int i = 0; i < allCamposPergunta.size(); i++) {
            if (allCamposPergunta.get(i).getText().toString().trim().equals("")) {
                errosParse += "Campo de pergunta " + (i+1) + " vazio.\n";
                estagioVerificacao = 1;
            }
        }

        //teste de perguntas iguais
        if (estagioVerificacao == 0) {
            for (int i = 0; i < allCamposPergunta.size(); i++) {
                for (int j = i+1; j < allCamposPergunta.size(); j++) {
                    if (allCamposPergunta.get(i).getText().toString().trim().equals(allCamposPergunta.get(j).getText().toString().trim())) {
                        errosParse += "Campo de pergunta " + (i+1) + " é igual a campo de pergunta " + (j+1) +".\n";
                        estagioVerificacao = 1;
                    }
                }
            }
        }

        //testa se a pergunta já existe no banco
        if (estagioVerificacao == 0) {

            for (int i = 0; i < allCamposPergunta.size(); i++) {
                if ( PerguntasORM.checkPerguntaExisteBanco(this, allCamposPergunta.get(i).getText().toString().trim() )) {
                    errosParse += "Pergunta " + (i+1) + " já existe no banco, digite outra.\n";
                    Toast.makeText(getApplicationContext(), errosParse, Toast.LENGTH_SHORT).show();
                    estagioVerificacao = 1;
                }    
            }
        }

        //se errosParse é vazio não teve nenhum erro
        if (errosParse.equals("")) {
            for (int i = 0; i < allCamposPergunta.size(); i++) {
                perguntas_texto[i] = allCamposPergunta.get(i).getText().toString().trim();
                perguntas_tiporesposta[i] = allRespostasEscolha.get(i).getSelectedItem().toString();

                Log.v("Debug Lista", "Pergunta nº" + i + " Texto: " +perguntas_texto[i].toString() + " Tipo de resposta: " + perguntas_tiporesposta[i].toString());
                if (perguntas_tiporesposta[i].toString().trim().equals("Sim/Não")) {
                    Log.v("Debug Lista", "Resposta da pergunta " + i + " é do tipo " + perguntas_tiporesposta[i].toString().trim());
                }

                Perguntas pergunta = new Perguntas(perguntas_texto[i].toString().trim(),perguntas_tiporesposta[i].toString().trim() );
                PerguntasORM.insertPergunta(this, pergunta);
            }
            Toast.makeText(getApplicationContext(), "Perguntas cadastradas com sucesso no banco de dados!", Toast.LENGTH_LONG).show();

            //ao terminar volta para main para preencher o questionário cadastrado
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), errosParse, Toast.LENGTH_SHORT).show();
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
        RegistraPerguntasActivity.this.finish();
        Intent goLogin = new Intent(this, LoginActivity.class);
        //Limpa a stack de activities pra resolver o bug do usuário apertar o botão de voltar e ele voltar na sessão já logado pelo sharedPreferences
        goLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goLogin);
    }
}

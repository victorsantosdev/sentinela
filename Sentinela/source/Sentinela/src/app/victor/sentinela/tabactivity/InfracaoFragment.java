
package app.victor.sentinela.tabactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import app.victor.sentinela.R;

public class InfracaoFragment extends Fragment {
    
    private static int terrenoEscolhidoID = 0;
    private static String cidadeSelecionada;

    TextView sessionInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_multa, container, false);
        sessionInfo = (TextView) rootView.findViewById(R.id.username_sessionData);
        setUserSessionData();
        cidadeSelecionada = DetalhesTerrenoActivity.getCidadeSelecionada();
        terrenoEscolhidoID = DetalhesTerrenoActivity.getTerrenoSelecionadoID();
        Toast.makeText(getActivity().getApplicationContext(), "ID do terreno escolhido"+ terrenoEscolhidoID, Toast.LENGTH_LONG).show();

        
        return rootView;
    }
    
    public void setUserSessionData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(app.victor.sentinela.login.LoginActivity.userPreferences, Context.MODE_PRIVATE);

        String usuarioLogado = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.usuarioLogado, "");
        String usuarioUltimoLogin = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.ultimoLogin, "");
        if (!usuarioLogado.equals("") && !usuarioUltimoLogin.equals("")) {
            sessionInfo.setText("Usuario Logado: " + usuarioLogado + "\nUltimo login: " + usuarioUltimoLogin);
        }
    }
    
    public void logout(View view){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(app.victor.sentinela.login.LoginActivity.userPreferences, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        String usuarioLogadoCheck = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.usuarioLogado, "");
        if (!usuarioLogadoCheck.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "Logout do usuario "+ usuarioLogadoCheck, Toast.LENGTH_LONG).show();
        editor.clear();
        editor.commit();      
        }

        getActivity().moveTaskToBack(true); 
        getActivity().finish();
        Intent goLogin = new Intent(getActivity(), app.victor.sentinela.login.LoginActivity.class);
        //Limpa a stack de activities pra resolver o bug do usuário apertar o botão de voltar e ele voltar na sessão já logado pelo sharedPreferences
        goLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goLogin);
     }
    
    public void btn_voltar() {
        Intent backToMaps = new Intent(getActivity(), app.victor.sentinela.mapstuff.MapsActivity.class);
        //moveTaskToBack(true);
        backToMaps.putExtra("CidadeSelecionada", cidadeSelecionada);
        startActivity(backToMaps);    }
}


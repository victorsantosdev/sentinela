package app.victor.sentinela.tabactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.victor.sentinela.R;
import app.victor.sentinela.bancodados.TerrenoORM;
import app.victor.sentinela.bdclasses.Terreno;
import app.victor.sentinela.utils.ImageHandling;

public class DetalhesTerrenoFragment extends Fragment {
    TextView sessionInfo;
    private static int terrenoEscolhidoID = 0;
    private static String cidadeSelecionada;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detalhesterreno, container, false);
        sessionInfo = (TextView) rootView.findViewById(R.id.username_sessionData);
        setUserSessionData();
        
        terrenoEscolhidoID = DetalhesTerrenoActivity.getTerrenoSelecionadoID();
        Toast.makeText(getActivity().getApplicationContext(), "ID do terreno escolhido"+ terrenoEscolhidoID, Toast.LENGTH_LONG).show();

        cidadeSelecionada = DetalhesTerrenoActivity.getCidadeSelecionada();
        
        Terreno terrenoEscolhido = TerrenoORM.getTerrenofromID(getActivity().getApplicationContext(), terrenoEscolhidoID);
        
        /* preenche as informacoes */
        
        Bitmap fotoTerrenoEscolhido = ImageHandling.loadImageFromTerrenosFolder(getActivity().getApplicationContext(), terrenoEscolhido.getFotoPath());
        ImageView imgViewFotoTerreno = (ImageView) rootView.findViewById(R.id.imageViewTerrenoFoto);
        imgViewFotoTerreno.setImageBitmap(fotoTerrenoEscolhido);
        
        TextView proprietarioTerreno = (TextView) rootView.findViewById(R.id.terrenoProprietario); 
        proprietarioTerreno.setText(terrenoEscolhido.getProprietario());
        
        TextView enderecoTerreno = (TextView) rootView.findViewById(R.id.terrenoEndereco); 
        enderecoTerreno.setText(terrenoEscolhido.getEndereco()+", "+terrenoEscolhido.getNumeroString());
        
        TextView bairroTerreno = (TextView) rootView.findViewById(R.id.terrenoBairro); 
        bairroTerreno.setText(terrenoEscolhido.getBairro());
        
        TextView cidadeTerreno = (TextView) rootView.findViewById(R.id.terrenoCidade); 
        cidadeTerreno.setText(terrenoEscolhido.getCidade()+"/"+terrenoEscolhido.getEstado());
        
        TextView topografiaTerreno = (TextView) rootView.findViewById(R.id.terrenoTopografia); 
        topografiaTerreno.setText(terrenoEscolhido.getTopografia());
        
        TextView areaTerreno = (TextView) rootView.findViewById(R.id.terrenoArea); 
        areaTerreno.setText(terrenoEscolhido.getAreaString());
        
        TextView configuracaoTerreno = (TextView) rootView.findViewById(R.id.terrenoConfiguracao); 
        configuracaoTerreno.setText(terrenoEscolhido.getConfiguracao());
        
        TextView situacaoTerreno = (TextView) rootView.findViewById(R.id.terrenoSituacao); 
        situacaoTerreno.setText(terrenoEscolhido.getSituacao_cadastral());
        
        
//        ImageView logout_img = (ImageView) rootView.findViewById(R.id.img_logout); 
//        logout_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logout(v);        
//            }        
//         });
//            
//        Button button_voltar = (Button) rootView.findViewById(R.id.btn_voltar); 
//        button_voltar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btn_voltar(v);        
//            }        
//         });        
        
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
//    
//    public void logout(View v){
//        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(app.victor.sentinela.imbituba.login.LoginActivity.userPreferences, Context.MODE_PRIVATE);
//        Editor editor = sharedPreferences.edit();
//        String usuarioLogadoCheck = sharedPreferences.getString(app.victor.sentinela.imbituba.login.LoginActivity.usuarioLogado, "");
//        if (!usuarioLogadoCheck.equals("")) {
//            Toast.makeText(getActivity().getApplicationContext(), "Logout do usuário "+ usuarioLogadoCheck, Toast.LENGTH_LONG).show();
//        editor.clear();
//        editor.commit();      
//        }
//
//        getActivity().moveTaskToBack(true); 
//        getActivity().finish();
//        Intent goLogin = new Intent(getActivity(), app.victor.sentinela.imbituba.login.LoginActivity.class);
//        //Limpa a stack de activities pra resolver o bug do usuário apertar o botão de voltar e ele voltar na sessão já logado pelo sharedPreferences
//        goLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(goLogin);
//     }
//
//    public void btn_voltar(View view) {
//        Intent backToMaps = new Intent(this.getActivity(), app.victor.sentinela.imbituba.mapstuff.MapsActivity.class);
//        //moveTaskToBack(true);
//        backToMaps.putExtra("CidadeSelecionada", cidadeSelecionada);
//        startActivity(backToMaps);
//    }
}
package app.victor.sentinela.tabactivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import app.victor.sentinela.R;
import app.victor.sentinela.bancodados.TerrenoORM;
import app.victor.sentinela.bdclasses.Terreno;
import app.victor.sentinela.bancodados.CodigoLeiORM;
import app.victor.sentinela.bdclasses.CodigoLei;
import app.victor.sentinela.printer.TemplateImpressao;


public class NotificacaoFragment extends Fragment implements OnClickListener {

    private static int terrenoSelecionadoID = 0;
    private static String cidadeSelecionada;
    private TextView sessionInfo;
    private Spinner spinner_codigoLei, spinner_tipo_autuacao;
    private String tipo_autuacao_selecionada;
    private String codigo_lei_selecionado;
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notificacao, container, false);
        sessionInfo = (TextView) view.findViewById(R.id.username_sessionData);
        setUserSessionData();
        spinner_codigoLei = (Spinner) view.findViewById(R.id.spinner_codigolei);
        spinner_tipo_autuacao = (Spinner) view.findViewById(R.id.spinner_tipo_autuacao);
        
        
    	//fill the spinners with data
        List<CodigoLei> codigos_disponiveis = CodigoLeiORM.getCodigosLei(getActivity().getApplicationContext());
        
        List<String> codigos_disponiveis_str = new ArrayList<String>(codigos_disponiveis.size());
        for (CodigoLei cod_obj : codigos_disponiveis) {
        	codigos_disponiveis_str.add(cod_obj != null ? cod_obj.getCodigoLei().toString()+"-"+cod_obj.getDescricao().toString() : null);
        }

        
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, codigos_disponiveis_str);
        	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        	dataAdapter.notifyDataSetChanged();

        	spinner_codigoLei.setAdapter(dataAdapter);
        	
        	spinner_codigoLei.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                	codigo_lei_selecionado = spinner_codigoLei.getItemAtPosition(position).toString();
                	Log.i("NotificacaoFragment", "NFrag: spinner_codigoLei: " + spinner_codigoLei.getItemAtPosition(position).toString());
                	Toast.makeText(getActivity().getApplicationContext(), "NFrag: spinner_codigoLei: " + spinner_codigoLei.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapter) {
                	
                	
                }
            });
        	
        	spinner_tipo_autuacao.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                	tipo_autuacao_selecionada = spinner_tipo_autuacao.getItemAtPosition(position).toString();
                	Log.i("NotificacaoFragment", "NFrag: spinner_tipo_autuacao: " + spinner_tipo_autuacao.getItemAtPosition(position).toString());
                	Toast.makeText(getActivity().getApplicationContext(), "NFrag: spinner_tipo_autuacao:  " + spinner_tipo_autuacao.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapter) {}
            });
        	

        cidadeSelecionada = DetalhesTerrenoActivity.getCidadeSelecionada();
        terrenoSelecionadoID = DetalhesTerrenoActivity.getTerrenoSelecionadoID();

        Toast.makeText(getActivity().getApplicationContext(), "ID do terreno escolhido " + terrenoSelecionadoID, Toast.LENGTH_LONG).show();

        Terreno terrenoEscolhido = TerrenoORM.getTerrenofromID(getActivity().getApplicationContext(), terrenoSelecionadoID);

        TextView proprietarioTerreno = (TextView) view.findViewById(R.id.terrenoProprietario);
        proprietarioTerreno.setText(terrenoEscolhido.getProprietario());

        TextView enderecoTerreno = (TextView) view.findViewById(R.id.terrenoEndereco);
        enderecoTerreno.setText(terrenoEscolhido.getEndereco() + ", " + terrenoEscolhido.getNumeroString());

        TextView bairroTerreno = (TextView) view.findViewById(R.id.terrenoBairro);
        bairroTerreno.setText(terrenoEscolhido.getBairro());

        TextView cidadeTerreno = (TextView) view.findViewById(R.id.terrenoCidade);
        cidadeTerreno.setText(terrenoEscolhido.getCidade() + "/" + terrenoEscolhido.getEstado());


        Button buttonVoltar = (Button) view.findViewById(R.id.btn_voltar);
        buttonVoltar.setOnClickListener(this);

        Button buttonImprimir = (Button) view.findViewById(R.id.btn_imprimir);
        buttonImprimir.setOnClickListener(this);

        return view;
    }

   
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.imageViewFotoInfracao:
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, REQUEST_PICTURE);
//                break;
            case R.id.btn_voltar:
                Intent backToMaps = new Intent(getActivity(), app.victor.sentinela.mapstuff.MapsActivity.class);
                // moveTaskToBack(true);
                backToMaps.putExtra("CidadeSelecionada", cidadeSelecionada);
                startActivity(backToMaps);
                break;
            case R.id.btn_imprimir:
                Intent goPrinter = new Intent(getActivity(), app.victor.sentinela.printer.BluetoothPrinterActivity.class);
                Terreno terrenoEscolhido = TerrenoORM.getTerrenofromID(getActivity().getApplicationContext(), terrenoSelecionadoID);
                TemplateImpressao dadosImpressao = new TemplateImpressao();
                dadosImpressao.setAgente(getUserSession_User());
                dadosImpressao.setDateTime(getCurrentDateTime());
                dadosImpressao.setTipoInfracao(tipo_autuacao_selecionada);
                dadosImpressao.setTerrenoProprietario(terrenoEscolhido.getProprietario());
                dadosImpressao.setTerrenoEndereco(terrenoEscolhido.getEndereco());
                dadosImpressao.setTerrenoNumero(terrenoEscolhido.getNumero());
                dadosImpressao.setTerrenoBairro(terrenoEscolhido.getBairro());
                dadosImpressao.setTerrenoCidade(terrenoEscolhido.getCidade());
                dadosImpressao.setTerrenoEstado(terrenoEscolhido.getEstado());
                CodigoLei codigoLei = new CodigoLei();
                codigoLei = CodigoLei.getCodigoLeiFromDescription(codigo_lei_selecionado);
                
                dadosImpressao.setCodigoLei(codigoLei.getCodigoLei());
                dadosImpressao.setCodigoLeiDescricao(codigoLei.getDescricao());
                
                goPrinter.putExtra("dataToBePrinted", dadosImpressao);
                // moveTaskToBack(true);
                startActivity(goPrinter);
                break;
                
            default: break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentDateTime() {
        Calendar c;
        SimpleDateFormat dateTime;
        String formatedDate;
        c = Calendar.getInstance();
        dateTime = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
        formatedDate = dateTime.format(c.getTime());
        return formatedDate;        
    }

    public void setUserSessionData() {
        //passar pra funcao, jogar como parametro a textview a escrever usuarioLogado e usuarioUltimoLogin
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(app.victor.sentinela.login.LoginActivity.userPreferences, Context.MODE_PRIVATE);

        String usuarioLogado = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.usuarioLogado, "");
        String usuarioUltimoLogin = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.ultimoLogin, "");
        if (!usuarioLogado.equals("") && !usuarioUltimoLogin.equals("")) {
            sessionInfo.setText("Usuaio Logado: " + usuarioLogado + "\nUltimo login: " + usuarioUltimoLogin);
        }
    }
    
    public String getUserSession_User() {
        //passar pra funcao, jogar como parametro a textview a escrever usuarioLogado e usuarioUltimoLogin
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(app.victor.sentinela.login.LoginActivity.userPreferences, Context.MODE_PRIVATE);

        String usuarioLogado = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.usuarioLogado, "");
        String usuarioUltimoLogin = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.ultimoLogin, "");
        if (!usuarioLogado.equals("") && !usuarioUltimoLogin.equals("")) {
        	return usuarioLogado;
        }
        else return null;  
    }

    public String getUserSession_LastLogin() {
        //passar pra funcao, jogar como parametro a textview a escrever usuarioLogado e usuarioUltimoLogin
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(app.victor.sentinela.login.LoginActivity.userPreferences, Context.MODE_PRIVATE);

        String usuarioLogado = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.usuarioLogado, "");
        String usuarioUltimoLogin = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.ultimoLogin, "");
        if (!usuarioLogado.equals("") && !usuarioUltimoLogin.equals("")) {
        	return usuarioUltimoLogin;
        }
        else return null; 
    }
    
    public void logout(View view) {
        //passar pra funcao, colocar a Activity que esta chamando a funcao como parametro, alem de passar o widget a ser clicado, ex: logout(view, this);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(app.victor.sentinela.login.LoginActivity.userPreferences, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        String usuarioLogadoCheck = sharedPreferences.getString(app.victor.sentinela.login.LoginActivity.usuarioLogado, "");
        if (!usuarioLogadoCheck.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "Logout do usuario " + usuarioLogadoCheck, Toast.LENGTH_LONG).show();
            editor.clear();
            editor.commit();
        }

        getActivity().moveTaskToBack(true);
        getActivity().finish();
        Intent goLogin = new Intent(getActivity(), app.victor.sentinela.login.LoginActivity.class);
        // Limpa a stack de activities pra resolver o bug do usuário apertar o
        // botão de voltar e ele voltar na sessão já logado pelo
        // sharedPreferences
        goLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goLogin);
    }



//    public void btn_voltar(View view) {
//    	
//        Intent backToMaps = new Intent(this, app.victor.sentinela.mapstuff.MapsActivity.class);
//        //moveTaskToBack(true);
//        backToMaps.putExtra("cidadeSelecionada", cidadeSelecionada);
//        startActivity(backToMaps);
//    }

}

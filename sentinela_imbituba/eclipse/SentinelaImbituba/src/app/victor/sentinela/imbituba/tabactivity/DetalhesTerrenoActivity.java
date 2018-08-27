package app.victor.sentinela.imbituba.tabactivity;

import app.victor.sentinela.imbituba.R;
import app.victor.sentinela.imbituba.utils.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class DetalhesTerrenoActivity extends FragmentActivity implements
ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Detalhes", "Notificacao", "Multa" };
    private static int terrenoSelecionadoID = 0;
    private static String cidadeSelecionada;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhesterreno_layout);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        if (getIntent()!= null) {
            // Recebe a quantidade de perguntas passada pelo Intent
            if (getIntent().hasExtra("terrenoSelecionadoID") && getIntent().hasExtra("cidadeSelecionada")) {
                terrenoSelecionadoID = getIntent().getIntExtra("terrenoSelecionadoID", 0);
                Toast.makeText(getApplicationContext(), " DetalhesTerrenoActivity ID do terreno escolhido: "+ terrenoSelecionadoID, Toast.LENGTH_LONG).show();

                cidadeSelecionada = getIntent().getStringExtra("cidadeSelecionada");

                Toast.makeText(getApplicationContext(), " DetalhesTerrenoActivity cidadeSelecionada: "+ cidadeSelecionada, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e("ERROR", "Intent is NULL");
        }
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    public static int getTerrenoSelecionadoID() {
        return terrenoSelecionadoID;
    }

    public static String getCidadeSelecionada() {
        return cidadeSelecionada;
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public void logout(View view){

        SharedPreferences sharedPreferences = getSharedPreferences(app.victor.sentinela.imbituba.login.LoginActivity.userPreferences, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        String usuarioLogadoCheck = sharedPreferences.getString(app.victor.sentinela.imbituba.login.LoginActivity.usuarioLogado, "");
        if (!usuarioLogadoCheck.equals("")) {
            Toast.makeText(getApplicationContext(), "Logout do usuario "+ usuarioLogadoCheck, Toast.LENGTH_LONG).show();
            editor.clear();
            editor.commit();      
        }

        moveTaskToBack(true); 
        finish();
        Intent goLogin = new Intent(this, app.victor.sentinela.imbituba.login.LoginActivity.class);
        //Limpa a stack de activities pra resolver o bug do usuário apertar o botão de voltar e ele voltar na sessão já logado pelo sharedPreferences
        goLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goLogin);
    }

    public void btn_voltar(View view) {
        Intent backToMaps = new Intent(this, app.victor.sentinela.imbituba.mapstuff.MapsActivity.class);
        //moveTaskToBack(true);
        backToMaps.putExtra("cidadeSelecionada", cidadeSelecionada);
        startActivity(backToMaps);
    }

}

package app.victor.sentinela.imbituba.utils;

import app.victor.sentinela.imbituba.tabactivity.DetalhesTerrenoFragment;
import app.victor.sentinela.imbituba.tabactivity.InfracaoFragment;
import app.victor.sentinela.imbituba.tabactivity.NotificacaoFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// fragment com detalhe do terreno apenas
			return new DetalhesTerrenoFragment();
		case 1:
			// fragment de registro de notificacao
			return new NotificacaoFragment();
		case 2:
			// fragment de autuacao
			return new InfracaoFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}

package app.victor.sentinela.utils;

import app.victor.sentinela.tabactivity.DetalhesTerrenoFragment;
import app.victor.sentinela.tabactivity.NotificacaoFragment;
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
		
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}

package ru.deliver.deliverApp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

import ru.deliver.deliverApp.DateBase.DBHelper;
import ru.deliver.deliverApp.Setup.Logs;
import ru.deliver.deliverApp.Utils.Favourite;
import ru.deliver.deliverApp.Utils.InfoFavouriteItem;
import ru.deliver.deliverApp.Utils.MyFragmentTabHost;

/**
 * Created by Evgenij on 20.08.13.
 * Точка входа в приложение
 */

public class Main extends FragmentActivity implements TabHost.OnTabChangeListener
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

    final String TAB1 = "tab1";
    final String TAB2 = "tab2";
    final String TAB3 = "tab3";
    final String TAB4 = "tab4";

	//---------------------------------
	//VARIABLES
	//---------------------------------

    private MyFragmentTabHost mTabHost;

    private DeliverFragment     mFragment1;
    private CalculatorFragment  mFragment2;
	private CallFragment		mFragment3;
	private OfficesFragment		mFragment4;

	private Fragment secondLevelFr;

    public ArrayList<Favourite> mFavourites;

	//---------------------------------
	//SUPER
	//---------------------------------

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

        mFragment1 = new DeliverFragment();
        mFragment2 = new CalculatorFragment();
		mFragment3 = new CallFragment();
		mFragment4 = new OfficesFragment();

        mFavourites = new ArrayList<Favourite>();

        mTabHost = (MyFragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.Main_real_tab);

        //initDB();
        initTab();
        initFav();
	}

    @Override
    public void onTabChanged(String tabId)
    {
        if(tabId.equals(TAB1))
		{
			//clearBackStack();
			if(secondLevelFr == null)
			{
				Logs.i("InfoFragment == null");
            	pushFragment(mFragment1);
			}
			else
			{
				Logs.i("InfoFragment != null");
			}
		}
        else if(tabId.equals(TAB2))
		{
			clearBackStack();
            pushFragment(mFragment2);
		}
		else if(tabId.equals(TAB3))
		{
			clearBackStack();
			pushFragment(mFragment3);
		}
		else if(tabId.equals(TAB4))
		{
			clearBackStack();
			pushFragment(mFragment4);
		}
    }

	@Override
	protected void onResume ()
	{
		Logs.i("onResume ");
		FragmentManager manager = getSupportFragmentManager();
		manager.addOnBackStackChangedListener(backStackListener);
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		Logs.i("onPause ");
		getSupportFragmentManager().removeOnBackStackChangedListener(backStackListener);
		super.onPause();
	}

	FragmentManager.OnBackStackChangedListener backStackListener = new FragmentManager.OnBackStackChangedListener()
	{
	@Override
		public void onBackStackChanged()
		{
			FragmentManager manager = getSupportFragmentManager();
			Logs.i("backstack! "+manager.getBackStackEntryCount());
		}
	};

	//---------------------------------
	//METHODS
	//---------------------------------

    /**
     * Тестовая функция для заполнения БД
     * */
    private void initDB()
    {
        DBHelper helper = new DBHelper(this, null);
        helper.openDB();

        Favourite f = new Favourite();
        f.setNumber(12212);
        f.setFrom("Krasnoyarsk");
        f.setTo("Moskva");
        helper.addFav(f);

        InfoFavouriteItem ifi = new InfoFavouriteItem();
        ifi.setDate("12/05/2012");
        ifi.setTime("11:44");
        ifi.setDescription("Send");
        helper.addFavInfo(12212, ifi);

        ifi = new InfoFavouriteItem();
        ifi.setDate("16/05/2012");
        ifi.setTime("10:04");
        ifi.setDescription("Cancel");
        helper.addFavInfo(12212, ifi);

        helper.closeDB();
    }

    /**
     * Загрузка всех избранных из БД
     * */
    private void initFav()
    {
        DBSaver saver = new DBSaver();
        saver.execute();
    }

    /**
     * Создание закладок и их графическое представление
     * */
	private void initTab()
	{
		TabHost.TabSpec spec = mTabHost.newTabSpec(TAB1);
		spec.setContent(new TabHost.TabContentFactory()
		{
			@Override
			public View createTabContent(String tag)
			{
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(createTabView(getString(R.string.Tab1_Name), R.drawable.ic_launcher));
		mTabHost.addTab(spec, DeliverFragment.class, null);

		spec = mTabHost.newTabSpec(TAB2);
		spec.setContent(new TabHost.TabContentFactory()
		{
			@Override
			public View createTabContent(String tag)
			{
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(createTabView(getString(R.string.Tab2_Name), R.drawable.ic_launcher));
		mTabHost.addTab(spec, CalculatorFragment.class, null);

		spec = mTabHost.newTabSpec(TAB3);
		spec.setContent(new TabHost.TabContentFactory()
		{
			@Override
			public View createTabContent(String tag)
			{
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(createTabView(getString(R.string.Tab3_Name), R.drawable.ic_launcher));
		mTabHost.addTab(spec, CallFragment.class, null);

		spec = mTabHost.newTabSpec(TAB4);
		spec.setContent(new TabHost.TabContentFactory()
		{
			@Override
			public View createTabContent(String tag)
			{
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(createTabView(getString(R.string.Tab4_Name), R.drawable.ic_launcher));
		mTabHost.addTab(spec, OfficesFragment.class, null);
	}

	private View createTabView(final String text, final int resID)
	{
		View view = LayoutInflater.from(this).inflate(R.layout.tab, null);
		ImageView iv = (ImageView)view.findViewById(R.id.tab_icon);
		iv.setImageDrawable(getResources().getDrawable(resID));
		((TextView)view.findViewById(R.id.tab_text)).setText(text);
		return view;
	}

	private void clearBackStack()
	{
		Logs.i("try to clearBackStack");
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		Logs.i("try to find fragment");
		if(secondLevelFr != null)
		{
			Logs.i("fragment != null");
			ft.remove(secondLevelFr);
			ft.commit();
			manager.popBackStack();
			secondLevelFr = null;
		}
		else
			Logs.i("fragment == null");
	}

	public void goToInfo(boolean isNew)
	{
		Logs.i("Push fragment DeliverInfoFragment");
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		secondLevelFr = null;
		DeliverInfoFragment next = new DeliverInfoFragment(isNew);
		secondLevelFr = next;
		ft.replace(R.id.Main_real_tab, next);
		ft.addToBackStack("Info");
		ft.commit();
	}

	private void pushFragment(Fragment fragment)
	{
		Logs.i("Push fragment " + fragment.getClass().getCanonicalName());
		FragmentManager manager = getSupportFragmentManager();

		if(manager.findFragmentByTag("Info") != null)
			Logs.i("InfoFragment is on back stack");

		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(android.R.id.tabcontent, fragment);
		ft.commit();
	}

	//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------

    class DBSaver extends AsyncTask<Void, Void, Void>
    {
        DBHelper helper;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            helper = new DBHelper(Main.this, null);

            helper.openDB();
            Logs.i("Открыли БД");
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Logs.i("Читаем БД");
            mFavourites = helper.findAllFavourites();
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            helper.closeDB();
            Logs.i("Закрыли БД");
            //TODO update adapter data
            if(mTabHost.getCurrentTab() == 0)
            {
                mFragment1.mFavAdapter.addAllItems(mFavourites);
            }
        }
    }
}

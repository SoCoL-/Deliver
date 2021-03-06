package ru.deliver.deliverApp;

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
import android.widget.Toast;

import java.util.ArrayList;

import ru.deliver.deliverApp.Network.AnswerServer;
import ru.deliver.deliverApp.Network.NetManager;
import ru.deliver.deliverApp.Setup.Logs;
import ru.deliver.deliverApp.Setup.Settings;
import ru.deliver.deliverApp.Utils.Favourite;
import ru.deliver.deliverApp.Utils.MyFragmentTabHost;
import ru.deliver.deliverApp.Utils.Offices;

/**
 * Created by Evgenij on 20.08.13.
 * Точка входа в приложение
 */

/**
 * Sanya (12:56:43 28/08/2013)
 24-0392597
 Sanya (12:56:53 28/08/2013)
 24-0392595
 Sanya (12:56:58 28/08/2013)
 24-0392592
 Sanya (12:57:03 28/08/2013)
 24-0392590
 * */

public class Main extends FragmentActivity implements TabHost.OnTabChangeListener, AnswerServer
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

    final String TAB1 = "tab1";
    final String TAB2 = "tab2";
    final String TAB3 = "tab3";
    final String TAB4 = "tab4";

    public static final int FRAGMENT_DELIVERY_INFO = 0;
    public static final int FRAGMENT_OFFICES_INFO = 1;

	//---------------------------------
	//VARIABLES
	//---------------------------------

    private MyFragmentTabHost mTabHost;

    /*public  DeliverFragment     mFragment1;
    private CalculatorFragment  mFragment2;
	private CallFragment		mFragment3;
	private OfficesFragment		mFragment4;*/

	private Fragment secondLevelFr;

    public ArrayList<Favourite> mFavourites;    //Список избранных отправлений из БД
    public Favourite mBufDeparture;             //Полученное от сервака отправление по номеру
    public ArrayList<Offices> mOffices;         //Список филиалов

    public boolean isInternet = true;           //Флаг наличия интернета на телефоне

	//---------------------------------
	//SUPER
	//---------------------------------

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

        isInternet = Settings.isInternet(this);

        mFavourites = new ArrayList<Favourite>();
        mOffices = new ArrayList<Offices>();

        mTabHost = (MyFragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.Main_real_tab);

        NetManager mNetManager = new NetManager(this);
        mNetManager.setInterface(this);
        mNetManager.sendOffices();

        initTab();
	}

    @Override
    public void onTabChanged(String tabId)
    {
        if(tabId.equals(TAB1))
		{
			if(secondLevelFr == null)
			{
				Logs.i("InfoFragment == null");
            	//pushFragment(mFragment1);
			}
			else
			{
				Logs.i("InfoFragment != null");
			}
            clearBackStack();
		}
        else if(tabId.equals(TAB2))
		{
			clearBackStack();
            //pushFragment(mFragment2);
		}
		else if(tabId.equals(TAB3))
		{
			clearBackStack();
			//pushFragment(mFragment3);
		}
		else if(tabId.equals(TAB4))
		{
			clearBackStack();
			//pushFragment(mFragment4);
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
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
		spec.setIndicator(createTabView(getString(R.string.Tab1_Name), R.drawable.ic_tab_departure));
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
		spec.setIndicator(createTabView(getString(R.string.Tab2_Name), R.drawable.ic_tab_calculator));
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
		spec.setIndicator(createTabView(getString(R.string.Tab3_Name), R.drawable.ic_tab_delivery));
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
		spec.setIndicator(createTabView(getString(R.string.Tab4_Name), R.drawable.ic_tab_contact));
		mTabHost.addTab(spec, OfficesFragment.class, null);

        String mTAG = getIntent().getExtras().getString("TAB");
        if(mTAG == null || mTAG.equals(""))
            mTAG = TAB1;
        mTabHost.setCurrentTabByTag(mTAG);
	}

	private View createTabView(final String text, final int resID)
	{
		View view = LayoutInflater.from(this).inflate(R.layout.tab, null);
		ImageView iv = (ImageView)view.findViewById(R.id.tab_icon);
		iv.setImageDrawable(getResources().getDrawable(resID));
        ((TextView)view.findViewById(R.id.tab_text)).setTextSize(10);
		((TextView)view.findViewById(R.id.tab_text)).setText(text);
		return view;
	}

	private void clearBackStack()
	{
		Logs.i("try to clearBackStack");
		FragmentManager manager = getSupportFragmentManager();
		//FragmentTransaction ft = manager.beginTransaction();
		Logs.i("try to find fragment");
		if(secondLevelFr != null)
		{
			Logs.i("fragment != null");
			//ft.remove(secondLevelFr);
			//ft.commit();
			manager.popBackStack();
			secondLevelFr = null;
		}
		else
			Logs.i("fragment == null");
	}

	public void goToInfo(Bundle b, int fragmentID)
	{
		Logs.i("Push fragment DeliverInfoFragment");
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		secondLevelFr = null;
        if(fragmentID == FRAGMENT_DELIVERY_INFO)
        {
            DeliverInfoFragment next = new DeliverInfoFragment();
            secondLevelFr = next;
            next.setArguments(b);
            ft.hide(manager.findFragmentByTag(mTabHost.getCurrentTabTag()));
            ft.add(R.id.Main_real_tab, next);
            ft.addToBackStack("Info");
        }
        else if(fragmentID == FRAGMENT_OFFICES_INFO)
        {
            OfficesInfoFragment oif = new OfficesInfoFragment();
            oif.setArguments(b);
            secondLevelFr = oif;
            ft.hide(manager.findFragmentByTag(mTabHost.getCurrentTabTag()));
            ft.add(R.id.Main_real_tab, oif);
            ft.addToBackStack("offices_info");
        }
		ft.commit();
	}

    public void updateFavourites()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                FragmentManager manager = getSupportFragmentManager();
                ((DeliverFragment)manager.findFragmentByTag(mTabHost.getCurrentTabTag())).initFav();
            }
        });
    }

    @Override
    public void ResponceOK(final String TAG, final ArrayList<String> params)
    {
        if(TAG.equals(Settings.REQ_TAG_OFFI))
        {
            Logs.i("Get answer from NetManager. Update Adapter in OfficesFragment");
            if(mTabHost.getCurrentTabTag().equals(TAB4))
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        FragmentManager manager = getSupportFragmentManager();
                        ((OfficesFragment)manager.findFragmentByTag(mTabHost.getCurrentTabTag())).setAdapters();
                    }
                });
            }
        }
    }

    @Override
    public void ResponceError(String TAG, final String text)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(Main.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------

}

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

    private TabHost mTabHost;

    private DeliverFragment     mFragment1;
    private CalculatorFragment  mFragment2;
	private CallFragment		mFragment3;
	private OfficesFragment		mFragment4;

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
		mFragment3 = new CallFragment(this);
		mFragment4 = new OfficesFragment();

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setup();

        initTab();
	}

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
        mTabHost.addTab(spec);

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
        mTabHost.addTab(spec);

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
        mTabHost.addTab(spec);

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
        mTabHost.addTab(spec);
    }

    private View createTabView(final String text, final int resID)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.tab, null);
        ImageView iv = (ImageView)view.findViewById(R.id.tab_icon);
        iv.setImageDrawable(getResources().getDrawable(resID));
        ((TextView)view.findViewById(R.id.tab_text)).setText(text);
        return view;
    }

    @Override
    public void onTabChanged(String tabId)
    {
        if(tabId.equals(TAB1))
            pushFragment(mFragment1);
        else if(tabId.equals(TAB2))
            pushFragment(mFragment2);
		else if(tabId.equals(TAB3))
			pushFragment(mFragment3);
		else if(tabId.equals(TAB4))
			pushFragment(mFragment4);
    }

    private void pushFragment(Fragment fragment)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(android.R.id.tabcontent, fragment);
        ft.commit();
    }

    //---------------------------------
	//METHODS
	//---------------------------------

	//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}

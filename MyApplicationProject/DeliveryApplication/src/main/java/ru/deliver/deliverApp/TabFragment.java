package ru.deliver.deliverApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import ru.deliver.deliverApp.Setup.Logs;

public class TabFragment extends Fragment implements TabHost.OnTabChangeListener
{
	private View mRoot;
	private TabHost mTabHost;
	private int mCurrTab;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState)
	{
		mRoot = inflater.inflate(R.layout.tab_fragment, null);
		mTabHost = (TabHost)mRoot.findViewById(android.R.id.tabhost);

		createTabs();
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mTabHost.setOnTabChangedListener(this);
		mTabHost.setCurrentTab(mCurrTab);
	}

	private void createTabs()
	{
		mTabHost.setup();

		mTabHost.addTab(createNewTab(getString(R.string.Tab1_Name), R.string.Tab1_Name, R.id.tab1, R.drawable.ic_launcher));
		mTabHost.addTab(createNewTab(getString(R.string.Tab2_Name), R.string.Tab2_Name, R.id.tab2, R.drawable.ic_launcher));
		mTabHost.addTab(createNewTab(getString(R.string.Tab3_Name), R.string.Tab3_Name, R.id.tab3, R.drawable.ic_launcher));
		mTabHost.addTab(createNewTab(getString(R.string.Tab4_Name), R.string.Tab4_Name, R.id.tab4, R.drawable.ic_launcher));

		//For Test
		FragmentManager fm = getFragmentManager();
		if (fm.findFragmentByTag(getString(R.string.Tab1_Name)) == null)
		{
			fm.beginTransaction()
					.replace(R.id.tab1, new DeliverFragment(), getString(R.string.Tab1_Name))
					.commit();
		}
	}

	private TabHost.TabSpec createNewTab(String tag, int labelID, int tabContentID, int resID)
	{
		/*View indicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab, (ViewGroup)mRoot.findViewById(android.R.id.tabs), false);

		TextView tv = (TextView)indicator.findViewById(R.id.tab_text);
		tv.setText(labelID);

		ImageView iv = (ImageView)indicator.findViewById(R.id.tab_icon);
		iv.setBackgroundResource(resID);*/

		TabHost.TabSpec ts = mTabHost.newTabSpec(tag);
		//ts.setIndicator(indicator);
		ts.setIndicator(getString(labelID), getResources().getDrawable(resID));
		ts.setContent(tabContentID);

		return ts;
	}

	private void changeTab(int numberTab)
	{
		mCurrTab = numberTab;
	}

	@Override
	public void onTabChanged(String s)
	{
		Logs.i("Tab change: tabID = " + s);
	}
}

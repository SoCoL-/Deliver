package ru.deliver.deliverApp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import ru.deliver.deliverApp.Adapters.FavAdapterDeliver;
import ru.deliver.deliverApp.Utils.EditWithDrawable;
import ru.deliver.deliverApp.Utils.ItemFav;

/**
 * Created by Evgenij on 20.08.13.
 *
 * Отображение первого таба
 */
public class DeliverFragment extends Fragment
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

	//---------------------------------
	//VARIABLES
	//---------------------------------

    private FragmentActivity mActivity;
	private FavAdapterDeliver mFavAdapter;

	//For test!!!!!!!!!!!
	ArrayList<ItemFav> mFavsInfo;
	//End for test

	//---------------------------------
	//SUPER
	//---------------------------------

    public DeliverFragment(FragmentActivity activity)
    {
        this.mActivity = activity;
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.deliver_fragment, container, false);

		//EditWithDrawable mEdit = (EditWithDrawable)view.findViewById(R.id.Deliver_number);
        Button mBtn = (Button)view.findViewById(R.id.Deliver_find);
		ListView mListFavs = (ListView)view.findViewById(R.id.Deliver_List);
		mFavAdapter = new FavAdapterDeliver();
		mListFavs.setAdapter(mFavAdapter);

		//Only for test!!!!!!!!!!!!!!
		mFavsInfo = new ArrayList<ItemFav>();
		mFavsInfo.add(new ItemFav("12-12345", getString(R.string.FavState_Done)));
		mFavsInfo.add(new ItemFav("13-54321", getString(R.string.FavState_Send)));
		mFavsInfo.add(new ItemFav("32-99999", getString(R.string.FavState_Cancel)));
		mFavsInfo.add(new ItemFav("32-99999", getString(R.string.FavState_Cancel)));
		mFavsInfo.add(new ItemFav("32-99999", getString(R.string.FavState_Cancel)));
		mFavsInfo.add(new ItemFav("32-99999", getString(R.string.FavState_Cancel)));
		mFavsInfo.add(new ItemFav("32-99999", getString(R.string.FavState_Cancel)));
		mFavsInfo.add(new ItemFav("32-99999", getString(R.string.FavState_Cancel)));
		mFavsInfo.add(new ItemFav("32-99999", getString(R.string.FavState_Cancel)));
		mFavAdapter.addAllItems(mFavsInfo);
		//End for test

        mBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pushFragment();
            }
        });

		return view;
	}
	//---------------------------------
	//METHODS
	//---------------------------------

    private void pushFragment()
    {
        FragmentManager manager = mActivity.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(android.R.id.tabcontent, new DeliverInfoFragment(true));
        ft.addToBackStack(null);
        ft.commit();
    }

	//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}

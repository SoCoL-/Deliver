package ru.deliver.deliverApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import ru.deliver.deliverApp.Adapters.TextPicAdapter;

/**
 * Created by Evgenij on 21.08.13.
 *
 * Список отделений
 */
public class OfficesFragment extends Fragment
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

	//---------------------------------
	//VARIABLES
	//---------------------------------

    private TextPicAdapter mAdapter;

	//---------------------------------
	//SUPER
	//---------------------------------

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.offices_fragment, container, false);

        ListView mList = (ListView)view.findViewById(R.id.Offices_List);
        mAdapter = new TextPicAdapter();
        mList.setAdapter(mAdapter);

        mAdapter.clear();
        mAdapter.addItem("Moscow");
        mAdapter.addItem("Krasnoyarsk");
        mAdapter.addItem("Other");

		return view;
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

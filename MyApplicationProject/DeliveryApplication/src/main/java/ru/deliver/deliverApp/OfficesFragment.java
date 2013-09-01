package ru.deliver.deliverApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.deliver.deliverApp.Adapters.ExpandAdapter;
import ru.deliver.deliverApp.Adapters.TextPicAdapter;
import ru.deliver.deliverApp.Network.AnswerServer;
import ru.deliver.deliverApp.Network.NetManager;
import ru.deliver.deliverApp.Utils.Offices;
import ru.deliver.deliverApp.Utils.OfficesAdd;

/**
 * Created by Evgenij on 21.08.13.
 *
 * Список отделений
 */
public class OfficesFragment extends Fragment implements AnswerServer
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

	//---------------------------------
	//VARIABLES
	//---------------------------------

    private ExpandAdapter mAdapter;
    private NetManager mNetManager;


	//---------------------------------
	//SUPER
	//---------------------------------

	public OfficesFragment()
	{
		super();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.offices_fragment, container, false);

        ExpandableListView mList = (ExpandableListView)view.findViewById(R.id.Offices_List);
        mAdapter = new ExpandAdapter(getActivity());
        mList.setAdapter(mAdapter);

        mNetManager = new NetManager(getActivity());
        mNetManager.setInterface(this);

		return view;
	}

    @Override
    public void ResponceOK(String TAG, final ArrayList<String> params)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                setAdapters();
            }
        });
    }

    @Override
    public void ResponceError(String TAG, final String text)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setAdapters();
    }

    //---------------------------------
	//METHODS
	//---------------------------------

    private void setAdapters()
    {
        mAdapter.clear();

        ArrayList<Offices> mOffices = ((Main)getActivity()).mOffices;
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<ArrayList<OfficesAdd>> infos = new ArrayList<ArrayList<OfficesAdd>>();
        for(Offices o : mOffices)
        {
            names.add(o.getCity());
            ArrayList<OfficesAdd> info = new ArrayList<OfficesAdd>();
            for(OfficesAdd oa : o.getContacts())
            {
                info.add(oa);
            }
            infos.add(info);
        }

        mAdapter.addItems(infos, names);
    }

	//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}

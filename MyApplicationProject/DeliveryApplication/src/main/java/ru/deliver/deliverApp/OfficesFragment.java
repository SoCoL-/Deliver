package ru.deliver.deliverApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.deliver.deliverApp.Adapters.TextPicAdapter;
import ru.deliver.deliverApp.Network.AnswerServer;
import ru.deliver.deliverApp.Network.NetManager;

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

    private TextPicAdapter mAdapter;
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

        ListView mList = (ListView)view.findViewById(R.id.Offices_List);
        mAdapter = new TextPicAdapter();
        mList.setAdapter(mAdapter);

        mNetManager = new NetManager(getActivity());
        mNetManager.setInterface(this);
        mNetManager.sendOffices();

        mAdapter.clear();
        mAdapter.addItem("Moscow");
        mAdapter.addItem("Krasnoyarsk");
        mAdapter.addItem("Other");

		return view;
	}

    @Override
    public void ResponceOK(String TAG, final ArrayList<String> params)
    {

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

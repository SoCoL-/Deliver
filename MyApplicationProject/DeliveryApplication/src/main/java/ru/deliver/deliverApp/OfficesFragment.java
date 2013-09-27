package ru.deliver.deliverApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.deliver.deliverApp.Adapters.TextPicAdapter;
import ru.deliver.deliverApp.Network.AnswerServer;
import ru.deliver.deliverApp.Network.NetManager;
import ru.deliver.deliverApp.Setup.Logs;
import ru.deliver.deliverApp.Setup.Settings;
import ru.deliver.deliverApp.Utils.Offices;

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

    private ListView mList;

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

        Logs.i("Create OfficesFragment Activity");

		View view = inflater.inflate(R.layout.offices_fragment, container, false);
        mList = (ListView)view.findViewById(R.id.Offices_List);

        NetManager mNetManager = new NetManager(getActivity());
        mNetManager.setInterface(this);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Bundle b = new Bundle();
                b.putInt("position", i);
                b.putString("title", ((Main)getActivity()).mOffices.get(i).getCity());
                pushFragment(b);
            }
        });

        setAdapters();

		return view;
	}

    @Override
    public void ResponceOK(final String TAG, final ArrayList<String> params)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if(TAG.equals(Settings.REQ_TAG_OFFI))
                {
                    Logs.i("Get answer from NetManager. Update Adapter");
                    setAdapters();
                }
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
    }

    //---------------------------------
	//METHODS
	//---------------------------------

    public void setAdapters()
    {
        ArrayList<Offices> mOffices = ((Main)getActivity()).mOffices;

        ArrayList<String> names = new ArrayList<String>();
        for(Offices o : mOffices)
        {
            names.add(o.getCity());
        }
        TextPicAdapter mAdapter = new TextPicAdapter();
        mAdapter.addAllItems(names);
        mList.setAdapter(mAdapter);
    }

    private void pushFragment(Bundle b)
    {
        ((Main)getActivity()).goToInfo(b, Main.FRAGMENT_OFFICES_INFO);
    }

    //---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}

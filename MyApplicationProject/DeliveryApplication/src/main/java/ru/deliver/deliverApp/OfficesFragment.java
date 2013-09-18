package ru.deliver.deliverApp;

import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.deliver.deliverApp.Adapters.OfficesInfoAdapter;
import ru.deliver.deliverApp.Network.AnswerServer;
import ru.deliver.deliverApp.Network.NetManager;
import ru.deliver.deliverApp.Setup.Logs;
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

    //private ExpandAdapter mAdapter;
    private ListView mList;
    private TextView mTitle;

    private ArrayAdapter<String> mFirstLayerAdapter;
    private NetManager mNetManager;

    private boolean isSecondLayer;


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

        //ExpandableListView mList = (ExpandableListView)view.findViewById(R.id.Offices_List);
        mList = (ListView)view.findViewById(R.id.Offices_List);
        mTitle = (TextView)view.findViewById(R.id.Offices_Title);
        //mAdapter = new ExpandAdapter(getActivity());
        //mList.setAdapter(mAdapter);

        mNetManager = new NetManager(getActivity());
        mNetManager.setInterface(this);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(!isSecondLayer)
                {
                    isSecondLayer = true;
                    Logs.i("onItemClick: isSecondLayer = " + isSecondLayer);
                    mTitle.setText(((Main)getActivity()).mOffices.get(i).getCity());
                    OfficesInfoAdapter mSecondAdapter = new OfficesInfoAdapter();
                    ArrayList<OfficesAdd> mOfficesInfo = ((Main)getActivity()).mOffices.get(i).getContacts();
                    mSecondAdapter.addItems(mOfficesInfo);
                    mList.setAdapter(mSecondAdapter);
                }
            }
        });

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
        isSecondLayer = false;
        Logs.i("onResume: isSecondLayer = " + isSecondLayer);
        setAdapters();
    }

    //---------------------------------
	//METHODS
	//---------------------------------

    private void setAdapters()
    {
        //mAdapter.clear();
        ArrayList<Offices> mOffices = ((Main)getActivity()).mOffices;

        if(!isSecondLayer)
        {
            ArrayList<String> names = new ArrayList<String>();
            for(Offices o : mOffices)
            {
                names.add(o.getCity());
            }

            mFirstLayerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, names);
            mList.setAdapter(mFirstLayerAdapter);
        }
    }

    public boolean onBackPress()
    {
        Logs.i("isSecondLayer = " + isSecondLayer);
        if(!mTitle.getText().equals(R.string.Offices_Name))
        {
            this.isSecondLayer = false;
            mTitle.setText(R.string.Offices_Name);
            ArrayList<Offices> mOffices = ((Main)getActivity()).mOffices;
            ArrayList<String> names = new ArrayList<String>();
            for(Offices o : mOffices)
            {
                names.add(o.getCity());
            }

            mFirstLayerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, names);
            mList.setAdapter(mFirstLayerAdapter);
            Logs.i("return true");
            return true;
        }

        Logs.i("return false");
        return false;
    }

//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}

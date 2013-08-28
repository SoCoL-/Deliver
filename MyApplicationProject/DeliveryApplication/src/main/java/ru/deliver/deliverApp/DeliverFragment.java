package ru.deliver.deliverApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import ru.deliver.deliverApp.Adapters.FavAdapterDeliver;
import ru.deliver.deliverApp.Network.AnswerServer;
import ru.deliver.deliverApp.Network.NetManager;
import ru.deliver.deliverApp.Utils.EditWithDrawable;

/**
 * Created by Evgenij on 20.08.13.
 *
 * Отображение первого таба
 */
public class DeliverFragment extends Fragment implements AnswerServer
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

	//---------------------------------
	//VARIABLES
	//---------------------------------

    private ListView mListFavs;
    public FavAdapterDeliver mFavAdapter;
    private NetManager mNetManager;

	//---------------------------------
	//SUPER
	//---------------------------------

	public DeliverFragment()
	{
		super();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.deliver_fragment, container, false);

		final EditWithDrawable mEdit	= (EditWithDrawable)view.findViewById(R.id.Deliver_number);
        Button mBtn 					= (Button)view.findViewById(R.id.Deliver_find);
		mListFavs 				        = (ListView)view.findViewById(R.id.Deliver_List);
		mFavAdapter 	                = new FavAdapterDeliver();

		mListFavs.setAdapter(mFavAdapter);

        mNetManager = new NetManager();
        mNetManager.setInterface(this);

		mListFavs.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
                Bundle b = new Bundle();
                b.putBoolean("IsNew", false);
                b.putInt("Position", i);

				pushFragment(b);
				//TODO + выборка избранного из БД
			}
		});

        mBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				if(mEdit.getText().length() <= 0)
					Toast.makeText(getActivity(), R.string.Error_NumberDeliver, Toast.LENGTH_SHORT).show();
				else
                {
                    mEdit.setText("");
                    Bundle b = new Bundle();
                    b.putBoolean("IsNew", true);
					pushFragment(b);
                }
				//TODO + Запрос на поиск на серваке данного заказа
			}
		});

		return view;
	}

    @Override
    public void ResponceOK(String TAG)
    {

    }

    @Override
    public void ResponceError(String TAG)
    {

    }

    //---------------------------------
	//METHODS
	//---------------------------------

    private void pushFragment(Bundle b)
    {
		((Main)getActivity()).goToInfo(b);
    }

    @Override
    public void onResume()
    {
        if(((Main)getActivity()).mFavourites.size() > 0)
        {
            mListFavs.setVisibility(View.VISIBLE);
            mFavAdapter.addAllItems(((Main)getActivity()).mFavourites);
        }
        else
            mListFavs.setVisibility(View.GONE);

        super.onResume();
    }

//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}

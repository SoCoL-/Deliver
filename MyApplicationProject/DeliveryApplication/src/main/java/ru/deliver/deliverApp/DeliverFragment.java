package ru.deliver.deliverApp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.deliver.deliverApp.Adapters.FavAdapterDeliver;
import ru.deliver.deliverApp.DateBase.DBHelper;
import ru.deliver.deliverApp.Network.AnswerServer;
import ru.deliver.deliverApp.Network.NetManager;
import ru.deliver.deliverApp.Setup.Logs;
import ru.deliver.deliverApp.Setup.Settings;
import ru.deliver.deliverApp.Utils.EditWithDrawable;
import ru.deliver.deliverApp.Utils.Favourite;
import ru.deliver.deliverApp.Utils.InfoFavouriteItem;

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
    private EditWithDrawable mEdit;
    private Button mUpd;

    public FavAdapterDeliver mFavAdapter;
    private NetManager mNetManager;
    private ProgressDialog mPD;
    private boolean isUpdate;
    private String bufNumber;

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

		mEdit           = (EditWithDrawable)view.findViewById(R.id.Deliver_number);
        Button mBtn     = (Button)view.findViewById(R.id.Deliver_find);
        mUpd            = (Button)view.findViewById(R.id.Deliver_update);
		mListFavs       = (ListView)view.findViewById(R.id.Deliver_List);
		mFavAdapter     = new FavAdapterDeliver();

		mListFavs.setAdapter(mFavAdapter);
        registerForContextMenu(mListFavs);

        mNetManager = new NetManager(getActivity());
        mNetManager.setInterface(this);

        mPD = new ProgressDialog(getActivity());
        mPD.setMessage(getString(R.string.Info_Loading));

		mListFavs.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
                Bundle b = new Bundle();
                b.putBoolean("IsNew", false);
                b.putInt("Position", i);

				pushFragment(b);
			}
		});

        mUpd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPD.show();
                isUpdate = true;
                updateFavs();
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
                    mPD.show();
                    isUpdate = false;
                    ArrayList<String> items = new ArrayList<String>(1);
                    items.add(mEdit.getText().toString());
                    bufNumber = mEdit.getText().toString();
                    mNetManager.sendDeparture(items);

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);
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
                mPD.dismiss();
                if(!isUpdate)
                {
                    Logs.i("Go to FavInfo");
                    mEdit.setText("");
                    Bundle b = new Bundle();
                    b.putBoolean("IsNew", true);
                    pushFragment(b);
                }
                else
                {
                    Logs.i("Update All Favs");
                    mFavAdapter.clear();
                    mFavAdapter.addAllItems(((Main)getActivity()).mFavourites);

                    ArrayList<Favourite> mFavs = new ArrayList<Favourite>();

                    //добавим в избранное
                    DBHelper helper = new DBHelper(getActivity(), null);
                    helper.openDB();

                    //Сохраним все в БД
                    for(Favourite f : mFavs)
                    {
                        helper.addFav(f);
                        for(InfoFavouriteItem ifi : f.getFavItems())
                            helper.addFavInfo(f.getNumber(), ifi);
                    }
                    helper.closeDB();
                }
            }
        });
    }

    @Override
    public void ResponceError(final String TAG, final String text)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if(TAG.equals(Settings.REQ_TAG_DEPA))
                {
                    mPD.dismiss();
                    Toast.makeText(getActivity(), text + " " + bufNumber, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.Deliver_List)
        {
            menu.setHeaderTitle(R.string.Menu_Title);
            menu.add(0, 0, 0, R.string.Menu_Delete);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        Logs.i("ContextInfoSelected");
        final AdapterView.AdapterContextMenuInfo adapInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Favourite selected = ((Main)getActivity()).mFavourites.get((int) adapInfo.id);

        Logs.i("Selected = " + selected.getNumber() + ", " + selected.getFrom());
        Logs.i("id = " + adapInfo.id);

        switch (item.getItemId())
        {
            case 0:
                DBHelper helper = new DBHelper(getActivity(), null);
                helper.openDB();
                helper.deleteFav(selected.getNumber());
                helper.closeDB();
                ((Main)getActivity()).mFavourites.remove((int) adapInfo.id);
                Logs.i("mFavourites.size() = " + ((Main)getActivity()).mFavourites.size());
                mFavAdapter.clear();
                mFavAdapter.addAllItems(((Main)getActivity()).mFavourites);
                return true;
        }
        return false;
    }

    //---------------------------------
	//METHODS
	//---------------------------------

    /**
     * Загрузка всех избранных из БД
     * */
    private void initFav()
    {
        Logs.i("Load favs");
        DBSaver saver = new DBSaver();
        saver.execute();
    }

    /**
     * Добавляем в стек фрагмент второго уровня
     * */
    private void pushFragment(Bundle b)
    {
		((Main)getActivity()).goToInfo(b);
    }

    @Override
    public void onResume()
    {
        initFav();
        Logs.i("DeliveryFragment: onResume");

        super.onResume();
    }

    /**
     * Обновление всех избранных в списке пользователя
     * */
    private void updateFavs()
    {
        ArrayList<Favourite> mFavs = ((Main)getActivity()).mFavourites;
        ArrayList<String> mFavNumbers = new ArrayList<String>();
        for(Favourite f : mFavs)
        {
            mFavNumbers.add(f.getNumber());
        }

        mNetManager.sendDeparture(mFavNumbers);
    }

    //---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------

    class DBSaver extends AsyncTask<Void, Void, Void>
    {
        DBHelper helper;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            helper = new DBHelper(getActivity(), null);

            helper.openDB();
            Logs.i("Открыли БД");
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Logs.i("Читаем БД");
            ((Main)getActivity()).mFavourites = helper.findAllFavourites();
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            helper.closeDB();
            Logs.i("Close DB");
            Logs.i("Change UI in Fragment1");
            mFavAdapter.clear();

            if(((Main) getActivity()).mFavourites != null)
                    mFavAdapter.addAllItems(((Main) getActivity()).mFavourites);

            if(((Main)getActivity()).mFavourites.size() > 0)
            {
                Logs.i("DeliveryFragment: mFavourites.size() > 0");
                mListFavs.setVisibility(View.VISIBLE);
                mUpd.setVisibility(View.VISIBLE);
            }
            else
            {
                Logs.i("DeliveryFragment: mFavourites.size() <= 0");
                mListFavs.setVisibility(View.GONE);
                mUpd.setVisibility(View.GONE);
            }
        }
    }
}

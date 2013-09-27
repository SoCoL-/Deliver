package ru.deliver.deliverApp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

    private LinearLayout mContent;
    private EditWithDrawable mEdit;
    private Button mUpd;

    private NetManager mNetManager;
    private ProgressDialog mPD;
    private boolean isUpdate;
    private String bufNumber;
    private int mGlobalPosition;

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
        mContent        = (LinearLayout)view.findViewById(R.id.Deliver_List);

        registerForContextMenu(mContent);

        mNetManager = new NetManager(getActivity());
        mNetManager.setInterface(this);

        mPD = new ProgressDialog(getActivity());
        mPD.setMessage(getString(R.string.Info_Loading));

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

        mBtn.setOnClickListener(new View.OnClickListener()
        {
			@Override
			public void onClick(View v)
			{
				if(mEdit.getText() != null && mEdit.getText().length() <= 0)
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
                    fillContent();

                    ArrayList<Favourite> mFavs = new ArrayList<Favourite>();
                    mFavs.addAll(((Main)getActivity()).mFavourites);

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
        final Favourite selected = ((Main)getActivity()).mFavourites.get(mGlobalPosition);

        Logs.i("Selected = " + selected.getNumber() + ", " + selected.getFrom());
        Logs.i("id = " + mGlobalPosition);

        switch (item.getItemId())
        {
            case 0:
                DBHelper helper = new DBHelper(getActivity(), null);
                helper.openDB();
                helper.deleteFav(selected.getNumber());
                helper.closeDB();
                ((Main)getActivity()).mFavourites.remove(mGlobalPosition);
                Logs.i("mFavourites.size() = " + ((Main)getActivity()).mFavourites.size());
                mContent.removeAllViews();
                fillContent();
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
    public void initFav()
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
		((Main)getActivity()).goToInfo(b, Main.FRAGMENT_DELIVERY_INFO);
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

    private void fillContent()
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ArrayList<Favourite> mFavs = ((Main)getActivity()).mFavourites;

        int i = 0;
        for(Favourite f : mFavs)
        {
            final int position = i;
            final LinearLayout mContentItem = (LinearLayout)inflater.inflate(R.layout.fav_item, null);
            if(mContentItem == null)
                return;

            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getActivity().getResources().getDisplayMetrics()));
            llp.leftMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getActivity().getResources().getDisplayMetrics());
            llp.rightMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getActivity().getResources().getDisplayMetrics());
            mContentItem.setLayoutParams(llp);

            mContentItem.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    mGlobalPosition = position;
                    return false;
                }
            });

            mContentItem.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bundle b = new Bundle();
                    b.putBoolean("IsNew", false);
                    b.putInt("Position", position);

                    pushFragment(b);
                }
            });

            if(i == 0)
                (mContentItem.findViewById(R.id.FavItem_Devider)).setVisibility(View.GONE);
            ((TextView)mContentItem.findViewById(R.id.FavItem_Number)).setText(f.getNumber());
            ((TextView)mContentItem.findViewById(R.id.FavItem_State)).setText(f.getFavItems().get(f.getFavItems().size() - 1).getDescription());

            mContent.addView(mContentItem);
            i++;
        }
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
            Logs.i("Open DB");
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Logs.i("Read DB");
            ((Main)getActivity()).mFavourites = helper.findAllFavourites();
            Logs.i("mFavourites.size() = " + ((Main)getActivity()).mFavourites.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            helper.closeDB();
            Logs.i("Close DB");
            Logs.i("Change UI in Fragment1");
            Logs.i("mFavourites.size() = " + ((Main)getActivity()).mFavourites.size());

            mContent.removeAllViews();
            fillContent();

            if(((Main)getActivity()).mFavourites.size() > 0)
            {
                Logs.i("DeliveryFragment: mFavourites.size() > 0");
                mContent.setVisibility(View.VISIBLE);
                mUpd.setVisibility(View.VISIBLE);
            }
            else
            {
                Logs.i("DeliveryFragment: mFavourites.size() <= 0");
                mContent.setVisibility(View.GONE);
                mUpd.setVisibility(View.GONE);
            }
        }
    }
}

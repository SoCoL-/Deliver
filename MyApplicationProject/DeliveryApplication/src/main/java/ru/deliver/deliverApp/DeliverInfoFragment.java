package ru.deliver.deliverApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.deliver.deliverApp.DateBase.DBHelper;
import ru.deliver.deliverApp.Utils.Favourite;
import ru.deliver.deliverApp.Utils.InfoFavouriteItem;

/**
 * Created by Evgenij on 21.08.13.
 *
 * Информация об отправлении
 */
public class DeliverInfoFragment extends Fragment
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

	//---------------------------------
	//VARIABLES
	//---------------------------------

	private boolean isNew;
    private int mPosition;
	private LinearLayout mContent;

    private Favourite f;

	//---------------------------------
	//SUPER
	//---------------------------------

	public DeliverInfoFragment()
	{
		super();
	}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (container == null)
            return null;

        View view = inflater.inflate(R.layout.deliver_info_fragment, container, false);
		Button mToFav = (Button)view.findViewById(R.id.DelivInfo_ToFav);
		mContent = (LinearLayout)view.findViewById(R.id.DelivInfo_Content);
        TextView mDeparture = (TextView)view.findViewById(R.id.DelivInfo_departure);
        TextView mFrom = (TextView)view.findViewById(R.id.DelivInfo_where);
        TextView mTo = (TextView)view.findViewById(R.id.DelivInfo_to);

        isNew = getArguments().getBoolean("IsNew");

		if(isNew)
        {
			mToFav.setVisibility(View.VISIBLE);
            //Получаем из временного хранилища пришедшую информацию
            f = ((Main)getActivity()).mBufDeparture;
            mFrom.setText(f.getFrom());
            mTo.setText(f.getTo());
            mDeparture.setText(f.getNumber());

            boolean isFirst = true;
            for(InfoFavouriteItem ifi : f.getFavItems())
            {
                addContent(ifi.getDate() + " " + ifi.getTime(), ifi.getDescription(), inflater, isFirst);
                if(isFirst)
                    isFirst = false;
            }

            //проверка на существование такой записи
            ArrayList<Favourite> mFavs = ((Main)getActivity()).mFavourites;
            for(Favourite fav : mFavs)
            {
                if(fav.getNumber().equals(f.getNumber()))
                {
                    mToFav.setVisibility(View.GONE);
                    break;
                }
            }
        }
		else
        {
            mPosition = getArguments().getInt("Position");
			mToFav.setVisibility(View.GONE);
            //Получаем данные из списка

            Favourite fav = ((Main)getActivity()).mFavourites.get(mPosition);
            mFrom.setText(fav.getFrom());
            mTo.setText(fav.getTo());
            mDeparture.setText(fav.getNumber());

            boolean isFirst = true;
            for(InfoFavouriteItem ifi : fav.getFavItems())
            {
                addContent(ifi.getDate() + " " + ifi.getTime(), ifi.getDescription(), inflater, isFirst);
                if(isFirst)
                    isFirst = false;
            }
        }

        mToFav.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //добавим в избранное
                DBHelper helper = new DBHelper(getActivity(), null);
                helper.openDB();

                helper.addFav(f);
                for(InfoFavouriteItem ifi : f.getFavItems())
                    helper.addFavInfo(f.getNumber(), ifi);

                helper.closeDB();

                ((Main)getActivity()).mFavourites.add(f);
                Toast.makeText(getActivity(), R.string.Info_FavAdd, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

	//---------------------------------
	//METHODS
	//---------------------------------

	private void addContent(String time, String State, LayoutInflater inflater, boolean isTop)
	{
		//Logs.i("addContent Start!!!!");
		LinearLayout mContentInfo = (LinearLayout)inflater.inflate(R.layout.deliv_info_content, null);
		//Logs.e("Inflate mContentItem");

		if(mContentInfo == null)
			return;

		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getActivity().getResources().getDisplayMetrics()));
		if(isTop)
			llp.topMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getActivity().getResources().getDisplayMetrics());
		llp.leftMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getActivity().getResources().getDisplayMetrics());
		llp.rightMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getActivity().getResources().getDisplayMetrics());
		mContentInfo.setLayoutParams(llp);

		//Logs.e("Find textView");
		if(isTop)
			(mContentInfo.findViewById(R.id.DelivContent_Devider)).setVisibility(View.GONE);
		((TextView)mContentInfo.findViewById(R.id.DelivContent_Time)).setText(time);
		((TextView)mContentInfo.findViewById(R.id.DelivContent_Info)).setText(State);

		//Logs.e("Add view to mContent");
		mContent.addView(mContentInfo);
		//Logs.i("addContent Stop!!!!");
	}

	//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}

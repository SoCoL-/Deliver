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

import ru.deliver.deliverApp.Setup.Logs;

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
	private LinearLayout mContent;

	//---------------------------------
	//SUPER
	//---------------------------------

	public DeliverInfoFragment()
	{
		super();
	}

	public DeliverInfoFragment(boolean isNew)
	{
		super();
		this.isNew = isNew;
	}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (container == null)
            return null;

        View view = inflater.inflate(R.layout.deliver_info_fragment, container, false);
		Button mToFav = (Button)view.findViewById(R.id.DelivInfo_ToFav);
		mContent = (LinearLayout)view.findViewById(R.id.DelivInfo_Content);

		if(isNew)
			mToFav.setVisibility(View.VISIBLE);
		else
			mToFav.setVisibility(View.GONE);

		addContent("11.06.2013 11:33", "Send", inflater, true);
		addContent("14.06.2013 21:33", "Customs", inflater, false);
		addContent("21.07.2013 11:53", "Deliver", inflater, false);
		addContent("21.07.2013 11:53", "Deliver", inflater, false);
		addContent("21.07.2013 11:53", "Deliver", inflater, false);
		addContent("21.07.2013 11:53", "Deliver", inflater, false);
		addContent("21.07.2013 11:53", "Deliver", inflater, false);

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

		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getActivity().getResources().getDisplayMetrics()));
		if(isTop)
			llp.topMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getActivity().getResources().getDisplayMetrics());
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

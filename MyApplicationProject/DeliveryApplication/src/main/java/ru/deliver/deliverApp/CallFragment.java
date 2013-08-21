package ru.deliver.deliverApp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import ru.deliver.deliverApp.Setup.Logs;

/**
 * Created by Evgenij on 21.08.13.
 *
 * Заказ доставки
 */
public class CallFragment extends Fragment
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

	//---------------------------------
	//VARIABLES
	//---------------------------------

	private Context mContext;
	private String mDate = "";

	//---------------------------------
	//SUPER
	//---------------------------------

	public CallFragment(Context c)
	{
		this.mContext = c;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.call_fragment, container, false);

		final TextView mCallDate 	= (TextView)view.findViewById(R.id.Call_Date);
		final TextView mCallTime 	= (TextView)view.findViewById(R.id.Call_Time);
		final CheckBox mCheck		= (CheckBox)view.findViewById(R.id.Call_Check);
		final EditText mEditWidth	= (EditText)view.findViewById(R.id.Call_Width);
		final EditText mEditHeight	= (EditText)view.findViewById(R.id.Call_Height);
		final EditText mEditLength	= (EditText)view.findViewById(R.id.Call_Length);

		mCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b)
			{
				if(b)
				{
					mCheck.setText(R.string.Calculator_CheckLetter);
					mEditHeight.setVisibility(View.GONE);
					mEditWidth.setVisibility(View.GONE);
					mEditLength.setVisibility(View.GONE);
				}
				else
				{
					mCheck.setText(R.string.Calculator_CheckParcel);
					mEditHeight.setVisibility(View.VISIBLE);
					mEditWidth.setVisibility(View.VISIBLE);
					mEditLength.setVisibility(View.VISIBLE);
				}
			}
		});

		mCallDate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Logs.i("mCallDate onClick!!!");
				Calendar c = Calendar.getInstance();
				DatePickerDialog dpd = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener()
				{
					@Override
					public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth)
					{
						mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
						Logs.i("setDate = " + mDate);
						mCallDate.setText(mDate);
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				dpd.show();

			}
		});

		mCallTime.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Logs.i("mCallTime onClick!!!");
				Calendar c = Calendar.getInstance();
				TimePickerDialog tpd = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener()
				{
					@Override
					public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute)
					{
						String mTime = hourOfDay + ":" + minute;
						Logs.i("setTime = " + mTime);
						mCallTime.setText(getString(R.string.Call_TimePicker) + " \t " + mTime);
					}
				}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
				tpd.show();
			}
		});

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

package ru.deliver.deliverApp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import ru.deliver.deliverApp.Network.AnswerServer;
import ru.deliver.deliverApp.Network.NetManager;
import ru.deliver.deliverApp.Setup.Logs;

/**
 * Created by Evgenij on 21.08.13.
 *
 * Заказ доставки
 */
public class CallFragment extends Fragment implements AnswerServer
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

	//---------------------------------
	//VARIABLES
	//---------------------------------

	private RadioGroup mCheck;
	private EditText mEditWidth;
	private EditText mEditHeight;
	private EditText mEditLength;
	private EditText mCompanyName;
	private EditText mAddress;
	private EditText mPerson;
	private EditText mPhone;
	private EditText mEMail;
	private EditText mComment;
	private AutoCompleteTextView mFrom;
	private AutoCompleteTextView mTo;
	private TextView mCallDate;
	private TextView mCallTime;
    private Spinner mWeight;
    private Button mBtnDate, mBtnTime;

	private String mDate = "", mTime;
    private String mType;

    NetManager mNetManager;

	//---------------------------------
	//SUPER
	//---------------------------------

	public CallFragment()
	{
		super();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.call_fragment, container, false);

		mCallDate 		= (TextView)view.findViewById(R.id.Call_DateText);
		mCallTime 		= (TextView)view.findViewById(R.id.Call_TimeText);

		mCheck			= (RadioGroup)view.findViewById(R.id.Call_RGroup);

		mEditWidth		= (EditText)view.findViewById(R.id.Call_Width);
		mEditHeight		= (EditText)view.findViewById(R.id.Call_Height);
		mEditLength		= (EditText)view.findViewById(R.id.Call_Length);
		mCompanyName	= (EditText)view.findViewById(R.id.Call_NameCompany);
		mAddress		= (EditText)view.findViewById(R.id.Call_Adress);
		mPerson			= (EditText)view.findViewById(R.id.Call_Person);
		mPhone			= (EditText)view.findViewById(R.id.Call_Phone);
		mEMail			= (EditText)view.findViewById(R.id.Call_Email);
		mComment		= (EditText)view.findViewById(R.id.Call_Comment);

		mFrom 			= (AutoCompleteTextView)view.findViewById(R.id.Call_Auto1);
		mTo 			= (AutoCompleteTextView)view.findViewById(R.id.Call_Auto2);
		mWeight	        = (Spinner)view.findViewById(R.id.Call_Spin1);

		Button mSend	= (Button)view.findViewById(R.id.Call_send);
		mBtnDate    	= (Button)view.findViewById(R.id.Call_Date);
		mBtnTime    	= (Button)view.findViewById(R.id.Call_Time);

        mNetManager = new NetManager(getActivity());
        mNetManager.setInterface(this);

		String[] towns = getActivity().getResources().getStringArray(R.array.towns);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, towns);
		mFrom.setAdapter(adapter1);
		mTo.setAdapter(adapter1);
		String[] weights = getActivity().getResources().getStringArray(R.array.weights);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, weights);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mWeight.setAdapter(adapter2);

        mFrom.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                mFrom.showDropDown();
                return false;
            }
        });

        mTo.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                mTo.showDropDown();
                return false;
            }
        });

        //Default values
        mCheck.check(R.id.Call_Radio1);
        mEditHeight.setVisibility(View.GONE);
        mEditWidth.setVisibility(View.GONE);
        mEditLength.setVisibility(View.GONE);
        mType = "1";
        //end

        mCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (i)
                {
                    case R.id.Call_Radio1:
                        mEditHeight.setVisibility(View.GONE);
                        mEditWidth.setVisibility(View.GONE);
                        mEditLength.setVisibility(View.GONE);
                        mType = "1";
                        break;
                    case R.id.Call_Radio2:
                        mEditHeight.setVisibility(View.VISIBLE);
                        mEditWidth.setVisibility(View.VISIBLE);
                        mEditLength.setVisibility(View.VISIBLE);
                        mType = "2";
                        break;
                }
            }
        });

		mBtnDate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Logs.i("mCallDate onClick!!!");
				Calendar c = Calendar.getInstance();
				DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
				{
					@Override
					public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth)
					{
                        String month = "" + monthOfYear;
                        if(month.length() == 1)
                            month = "0" + month;

						mDate = dayOfMonth + "." + month + "." + year;
						Logs.i("setDate = " + mDate);
						//mCallDate.setText(getString(R.string.Call_DatePicker) + " \t " + mDate);
                        mBtnDate.setText(getActivity().getString(R.string.Call_DatePicker) + " " + mDate);
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				dpd.show();
			}
		});

		mBtnTime.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Logs.i("mCallTime onClick!!!");
				Calendar c = Calendar.getInstance();
				TimePickerDialog tpd = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener()
				{
					@Override
					public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute)
					{
						String m = "" + minute;
						if(m.length() == 1)
							m = "0" + m;

						mTime = hourOfDay + ":" + m;

						Logs.i("setTime = " + mTime);

						//mCallTime.setText(getString(R.string.Call_TimePicker) + " \t " + mTime);
                        mBtnTime.setText(getActivity().getString(R.string.Call_TimePicker) + " " + mTime);
					}
				}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
				tpd.show();
			}
		});

		mSend.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(!isFillAllFields())
                {
					Toast.makeText(getActivity(), R.string.Error_FillFields, Toast.LENGTH_SHORT).show();
                    return;
                }

                mNetManager.sendCall(mDate, mTime, mFrom.getText().toString(), mTo.getText().toString(), mType, mCompanyName.getText().toString(), mAddress.getText().toString(), mPerson.getText().toString(), mPhone.getText().toString(), mEMail.getText().toString(), mComment.getText().toString(), mEditWidth.getText().toString(), mEditHeight.getText().toString(), mEditLength.getText().toString(), (String)mWeight.getSelectedItem());
			}
		});

		return view;
	}
	//---------------------------------
	//METHODS
	//---------------------------------

	/**
	 * Заполнены ли все поля на форме окна
	 * @return true - если заполнены, false - если нет
	 * */
	private boolean isFillAllFields()
	{
		boolean rez = true;

		if(mFrom.getText().length() <= 0 || mTo.getText().length() <= 0)//Проверка полей откуда и куда
			rez = false;

		if(mDate.length() <= 0 || mTime.length() <= 0)//Проверка даты и времени
			rez = false;

		//Проверка основныъх данных о заказчике
		if(mCompanyName.getText().length() <= 0 || mAddress.getText().length() <= 0 || mPerson.getText().length() <= 0 ||
				mPhone.getText().length() <= 0 || mEMail.getText().length() <= 0 || mComment.getText().length() <= 0)
			rez = false;

		if(mType.equals("2")) //Если посылка, то проверим габариты
		{
			if(mEditWidth.getText().length() <= 0 || mEditHeight.getText().length() <= 0 || mEditLength.getText().length() <= 0)
				rez = false;
		}

		return rez;
	}

    private void clearFields()
    {
        mFrom.setText("");
        mTo.setText("");
        mCallDate.setText("");
        mCallTime.setText("");
        mComment.setText("");
        mCompanyName.setText("");
        mAddress.setText("");
        mPerson.setText("");
        mPhone.setText("");
        mEditHeight.setText("");
        mEditLength.setText("");
        mEditWidth.setText("");
        mEMail.setText("");
        mWeight.setSelection(0);
        mBtnDate.setText(R.string.Call_DatePicker);
        mBtnTime.setText(R.string.Call_TimePicker);
    }

    @Override
    public void ResponceOK(String TAG, final ArrayList<String> params)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                clearFields();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("");
                dialog.setMessage(params.get(0));
                dialog.setPositiveButton(R.string.Btn_OK, null);
                dialog.show();
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

    //---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}

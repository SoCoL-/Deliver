package ru.deliver.deliverApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.deliver.deliverApp.Network.AnswerServer;
import ru.deliver.deliverApp.Network.NetManager;
import ru.deliver.deliverApp.Setup.Logs;
import ru.deliver.deliverApp.Setup.Settings;

/**
 * Created by 1 on 20.08.13.
 *
 * Окно расчета стоимости заказа
 */
public final class CalculatorFragment extends Fragment implements AnswerServer
{
    //-------------------------------
    //CONSTANTS
    //-------------------------------

    //-------------------------------
    //VARIABLES
    //-------------------------------

	private CheckBox mCheck;
	private EditText mEditWidth;
	private EditText mEditHeight;
	private EditText mEditLength;
	private AutoCompleteTextView mFrom;
	private AutoCompleteTextView mTo;
    private TextView mSummaText;

    private NetManager mNetManager;

    //-------------------------------
    //CONSTRUCTORS
    //-------------------------------

	public CalculatorFragment()
	{
		super();
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.calculator_fragment, container, false);

		mEditWidth 			= (EditText)view.findViewById(R.id.Calc_Width);
		mEditHeight 		= (EditText)view.findViewById(R.id.Calc_Height);
		mEditLength 		= (EditText)view.findViewById(R.id.Calc_Length);
		mCheck				= (CheckBox)view.findViewById(R.id.Calc_Check);
		mFrom 				= (AutoCompleteTextView)view.findViewById(R.id.Calc_Auto1);
		mTo 				= (AutoCompleteTextView)view.findViewById(R.id.Calc_Auto2);
        mSummaText          = (TextView)view.findViewById(R.id.Calc_Summa);
		final Spinner mWeight 	= (Spinner)view.findViewById(R.id.Calc_Spin1);
		Button mCalculate	= (Button)view.findViewById(R.id.Calc_calculate);

		String[] towns = getActivity().getResources().getStringArray(R.array.towns);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, towns);
		String[] weights = getActivity().getResources().getStringArray(R.array.weights);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, weights);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mFrom.setAdapter(adapter1);
		mTo.setAdapter(adapter1);
		mWeight.setAdapter(adapter2);

        mNetManager = new NetManager(getActivity());
        mNetManager.setInterface(this);

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

		mCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b)
			{
				if(b)//Письмо
				{
					mCheck.setText(R.string.Calculator_CheckLetter);
					mEditHeight.setVisibility(View.GONE);
					mEditWidth.setVisibility(View.GONE);
					mEditLength.setVisibility(View.GONE);
				}
				else//Посылка
				{
					mCheck.setText(R.string.Calculator_CheckParcel);
					mEditHeight.setVisibility(View.VISIBLE);
					mEditWidth.setVisibility(View.VISIBLE);
					mEditLength.setVisibility(View.VISIBLE);
				}
			}
		});

		mCalculate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(!isFillAllFields())
					Toast.makeText(getActivity(), R.string.Error_FillFields, Toast.LENGTH_SHORT).show();

                String type;
                if(mCheck.isChecked())
                    type = "1";
                else
                    type = "2";

                mNetManager.sendCalcReq(mFrom.getText().toString(), mTo.getText().toString(), (String)mWeight.getSelectedItem(), type, mEditHeight.getText().toString(), mEditWidth.getText().toString(), mEditLength.getText().toString());
			}
		});

        return view;
    }
    //-------------------------------
    //SUPER METHODS
    //-------------------------------

    //-------------------------------
    //METHODS
    //-------------------------------

	/**
	 * Заполнены ли все поля на форме окна
	 * @return true - если заполнены, false - если нет
	 * */
	private boolean isFillAllFields()
	{
		boolean rez = true;
		Boolean isMail = mCheck.isChecked();

		if(mFrom.getText().length() <= 0 || mTo.getText().length() <= 0)
			rez = false;

		if(!isMail) // Если посылка, то проверим габариты
		{
			if(mEditWidth.getText().length() <= 0 || mEditHeight.getText().length() <= 0 || mEditLength.getText().length() <= 0)
				rez = false;
		}

		return rez;
	}

    @Override
    public void ResponceOK(String TAG, final ArrayList<String> params)
    {
        if(!TAG.equals(Settings.REQ_TAG_CALC))
            return;

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mSummaText.setVisibility(View.VISIBLE);
                mSummaText.setText(params.get(0) + getString(R.string.Info_Rubles));
            }
        });
    }

    @Override
    public void ResponceError(String TAG, final String text)
    {
        Logs.e("Error in send; text = " + text);
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mSummaText.setVisibility(View.GONE);
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //-------------------------------
    //GETTERS/SETTERS
    //-------------------------------

    //-------------------------------
    //INNER CLASSES
    //-------------------------------
}

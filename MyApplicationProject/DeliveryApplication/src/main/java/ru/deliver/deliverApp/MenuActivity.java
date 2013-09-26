package ru.deliver.deliverApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by Evgenij on 25.09.13.
 *
 * Окно показывающее меню с кнопками = закладкам
 */
public class MenuActivity extends Activity implements View.OnClickListener
{
    //-----------------------------
    //Constants
    //-----------------------------

    //-----------------------------
    //Variables
    //-----------------------------

    //-----------------------------
    //Ctors
    //-----------------------------

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu_activity);

        Button b1 = (Button)findViewById(R.id.MenuAct_Btn1);
        Button b2 = (Button)findViewById(R.id.MenuAct_Btn2);
        Button b3 = (Button)findViewById(R.id.MenuAct_Btn3);
        Button b4 = (Button)findViewById(R.id.MenuAct_Btn4);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        String TAB = "";
        switch(view.getId())
        {
            case R.id.MenuAct_Btn1:
                TAB = "tab1";
                break;
            case R.id.MenuAct_Btn2:
                TAB = "tab2";
                break;
            case R.id.MenuAct_Btn3:
                TAB = "tab3";
                break;
            case R.id.MenuAct_Btn4:
                TAB = "tab4";
                break;
        }

        Intent i = new Intent();
        i.setClass(this, Main.class);
        i.putExtra("TAB", TAB);
        startActivity(i);
        finish();
    }
    //-----------------------------
    //Methods
    //-----------------------------

    //-----------------------------
    //Getters/Setters
    //-----------------------------

    //-----------------------------
    //Inner Classes
    //-----------------------------
}

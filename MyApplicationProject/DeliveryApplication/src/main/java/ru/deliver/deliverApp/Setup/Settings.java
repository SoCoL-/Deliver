package ru.deliver.deliverApp.Setup;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Evgenij on 20.08.13.
 *
 * Класс настроек приложения
 */
public final class Settings
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

    //Переменная включает логи
	public static final boolean IS_DEBUG = false;

    //Названия методов
    public static final String REQ_TAG_CALC = "CALC";
    public static final String REQ_TAG_CALL = "DELIVERY";
    public static final String REQ_TAG_DEPA = "DEPARTURE";
    public static final String REQ_TAG_OFFI = "CONTACT";

    //Названия ключей
    public static final String FIELD_D1 = "d1";
    public static final String FIELD_D2 = "d2";
    public static final String FIELD_D3 = "d3";
    public static final String FIELD_FROM = "from";
    public static final String FIELD_TO = "to";
    public static final String FIELD_WEIGHT = "weight";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_COMMENT = "comment";
    public static final String FIELD_DATA = "date";
    public static final String FIELD_TIME = "time";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_EMAIL = "mail";
    public static final String FIELD_PERSON = "person";
    public static final String FIELD_NUMBER = "num";

    //Идентификаторы ошибок запросов
    public static final String CITY_ERROR = "CITY_ERROR";
    public static final String WEIGHT_ERROR = "WEIGHT_ERROR";
    public static final String DATA_ERROR = "DATA_ERROR";
    public static final String DEPARTURE_ERROR = "ERROR_DEPARTURE";

    //Текст сообщений
    public static final String INFO_RUBLES = " руб.";

	//---------------------------------
	//VARIABLES
	//---------------------------------

	//---------------------------------
	//SUPER
	//---------------------------------

	//---------------------------------
	//METHODS
	//---------------------------------

    /**
     * Метод получает строку из потока данных
     * @param inputStream - поток данных
     * @return Строка из потока, либо null
     * */
    static synchronized public String convertStreamToString(final InputStream inputStream) throws IOException
    {
        if (inputStream != null)
        {
            StringBuilder sb = new StringBuilder();
            String line;
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line).append("\n");
                }
            }
            finally
            {
                inputStream.close();
            }
            return sb.toString();
        }
        else
        {
            return "";
        }
    }

    public static boolean isInternet(FragmentActivity a)
    {
        try
        {
            /*ConnectivityManager nInfo = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
            nInfo.getActiveNetworkInfo().isConnectedOrConnecting();*/

            ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting())
            {
                Logs.i("Internet is ON");
                return true;
            }
            else
            {
                Logs.i("Internet is OFF");
                return false;
            }
        }
        catch (Exception e)
        {
            Logs.e("Error: Internet is OFF");
            return false;
        }
    }

	//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------
}

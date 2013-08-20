package ru.deliver.deliverApp.Setup;

import android.util.Log;

/**
 * Created by Evgenij on 20.08.13.
 *
 * Класс выводит логи в консоль
 */
public final class Logs
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

	private static final String CURR_TAG = "DeliveryApplication";

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
	 * Сообщение об ощибке в лог
	 * @param TAG - Строка поиска в консоли
	 * @param Message - Сообщение ошибки
	 * */
	public static void e(String TAG, String Message)
	{
		Log.e(TAG, Message);
	}

	/**
	 * Сообщение об ошибке в лог с дефолтным тэгом
	 * @param Message - Сообщение ошибки
	 * */
	public static void e(String Message)
	{
		Log.e(CURR_TAG, Message);
	}

	/**
	 * Информационное сообщение в лог
	 * @param TAG - Строка поиска в консоли
	 * @param Message - Сообщение
	 * */
	public static void i(String TAG, String Message)
	{
		Log.i(TAG, Message);
	}

	/**
	 * Информационное сообщение в лог с дефлотным тэгом
	 * @param Message - Сообщение
	 * */
	public static void i(String Message)
	{
		Log.i(CURR_TAG, Message);
	}

	//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}

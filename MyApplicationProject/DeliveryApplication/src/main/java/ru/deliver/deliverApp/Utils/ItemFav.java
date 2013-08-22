package ru.deliver.deliverApp.Utils;

/**
 * Created by Evgenij on 22.08.13.
 *
 * Элемент списка избранного
 */
public class ItemFav
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

	//---------------------------------
	//VARIABLES
	//---------------------------------

	private String mNumber;
	private String mState;

	//---------------------------------
	//SUPER
	//---------------------------------

	public ItemFav(final String number, final String state)
	{
		this.mNumber = number;
		this.mState = state;
	}

	//---------------------------------
	//METHODS
	//---------------------------------

	//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	public String getNumber()
	{
		return mNumber;
	}

	public void setNumber(String mNumber)
	{
		this.mNumber = mNumber;
	}

	public String getState()
	{
		return mState;
	}

	public void setState(String mState)
	{
		this.mState = mState;
	}

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}

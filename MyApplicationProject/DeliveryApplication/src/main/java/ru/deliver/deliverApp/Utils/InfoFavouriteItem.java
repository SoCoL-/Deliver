package ru.deliver.deliverApp.Utils;

/**
 * Created by Evgenij on 27.08.13.
 *
 * Элемент хранения табличной части избранного
 */
public class InfoFavouriteItem
{
    private String mDate;
    private String mTime;
    private String mDescription;

    public InfoFavouriteItem()
    {

    }

    public InfoFavouriteItem(String date, String time, String text)
    {
        this.mDate = date;
        this.mTime = time;
        this.mDescription = text;
    }


    public String getDate()
    {
        return mDate;
    }

    public void setDate(String mDate)
    {
        this.mDate = mDate;
    }

    public String getTime()
    {
        return mTime;
    }

    public void setTime(String mTime)
    {
        this.mTime = mTime;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String mDescription)
    {
        this.mDescription = mDescription;
    }
}

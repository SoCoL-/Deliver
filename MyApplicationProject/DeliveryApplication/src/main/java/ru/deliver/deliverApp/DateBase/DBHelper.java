package ru.deliver.deliverApp.DateBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ru.deliver.deliverApp.Setup.Logs;
import ru.deliver.deliverApp.Utils.Favourite;
import ru.deliver.deliverApp.Utils.InfoFavouriteItem;

/**
 * Created by Evgenij on 27.08.13.
 *
 * Класс реализующий работу с БД
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FavouritesDB";

    private static final String TABLE_FAVOURITE = "Favourite_Table";
    private static final String COLUMN_NUMBER   = "number";
    private static final String COLUMN_FROM     = "from";
    private static final String COLUMN_TO       = "to";

    private static final String TABLE_FAVOURITE_INFOITEM    = "FavouriteInfoItem_Table";
    private static final String COLUMN_DATE_TIME            = "date_time";
    private static final String COLUMN_DESCRIPTION          = "description";

    private SQLiteDatabase mDB;

    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String CREATE_FAVOURITE_TABLE = "CREATE TABLE "
                + TABLE_FAVOURITE   +
                "("
                + COLUMN_NUMBER     + " INTEGER PRIMARY KEY,"
                + COLUMN_FROM       + " TEXT,"
                + COLUMN_TO         + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(CREATE_FAVOURITE_TABLE);

        String CREATE_FAV_INFOITEM_TABLE = "CREATE TABLE "
                + TABLE_FAVOURITE_INFOITEM  +
                "("
                + COLUMN_DATE_TIME          + " TEXT,"
                + COLUMN_DESCRIPTION        + " TEXT,"
                + "PRIMARY KEY (" + COLUMN_DATE_TIME + ") ,"
                + "FOREIGN KEY (" + COLUMN_NUMBER + " )"
                + "REFERENCES " + TABLE_FAVOURITE + " (" + COLUMN_NUMBER + " )" +
                ")";
        sqLiteDatabase.execSQL(CREATE_FAV_INFOITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE_INFOITEM);
        onCreate(sqLiteDatabase);
    }

    public void openDB()
    {
        mDB = this.getWritableDatabase();
    }

    public void closeDB()
    {
        if(mDB != null)
            mDB.close();
    }

    public void addFav(Favourite f)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER, f.getNumber());
        values.put(COLUMN_FROM, f.getFrom());
        values.put(COLUMN_TO, f.getTo());

        mDB.insert(TABLE_FAVOURITE, null, values);
    }

    public void addFavInfo(InfoFavouriteItem ifi)
    {
        ContentValues values = new ContentValues();
        String date_time = ifi.getDate() + " " + ifi.getTime();
        values.put(COLUMN_DATE_TIME, date_time);
        values.put(COLUMN_DESCRIPTION, ifi.getDescription());

        mDB.insert(TABLE_FAVOURITE_INFOITEM, null, values);
    }

    public ArrayList<Favourite> findAllFavourites()
    {
        ArrayList<Favourite> rez = new ArrayList<Favourite>();

        if(mDB == null)
            return null;

        Cursor c = mDB.rawQuery("select * from Favourite_Table", null);
        if(c == null)
            return null;

        if(c.moveToFirst())
        {
            do
            {
                Favourite f = new Favourite();
                f.setNumber(Integer.parseInt(c.getString(0)));
                f.setFrom(c.getString(1));
                f.setTo(c.getString(2));
                rez.add(f);
            }
            while (c.moveToNext());
            c.close();
        }

        return rez;
    }

    public InfoFavouriteItem findFavInfo(int fav_number)
    {
        InfoFavouriteItem rez = new InfoFavouriteItem();

        if(mDB == null)
            return null;

        Cursor c = mDB.rawQuery("select * from FavouriteInfoItem_Table where number like '" + fav_number + "' ", null);
        if(c == null)
            return null;

        if(c.moveToFirst())
        {
            String date_time = c.getString(0);
            String[] times = date_time.split(" ");
            rez.setDate(times[0]);
            rez.setTime(times[1]);
            rez.setDescription(c.getString(1));

            c.close();
        }

        return rez;
    }

    public void deleteFav(int fav_number)
    {

    }
}

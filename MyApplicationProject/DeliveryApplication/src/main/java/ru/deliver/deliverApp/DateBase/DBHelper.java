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
    private static final String COLUMN_FROM     = "_from";
    private static final String COLUMN_TO       = "_to";

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
                + COLUMN_NUMBER     + " TEXT,"
                + COLUMN_FROM       + " TEXT,"
                + COLUMN_TO         + " TEXT,"
                + "PRIMARY KEY (" + COLUMN_NUMBER + ")" +
                ")";
        sqLiteDatabase.execSQL(CREATE_FAVOURITE_TABLE);

        String CREATE_FAV_INFOITEM_TABLE = "CREATE TABLE "
                + TABLE_FAVOURITE_INFOITEM  +
                "("
                + COLUMN_NUMBER             + " TEXT,"
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
        Logs.i("Add new FAV");
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER, f.getNumber());
        values.put(COLUMN_FROM, f.getFrom());
        values.put(COLUMN_TO, f.getTo());

        mDB.insert(TABLE_FAVOURITE, null, values);
    }

    public void addFavInfo(String fav_number, InfoFavouriteItem ifi)
    {
        Logs.i("Add new FAVINFO");
        ContentValues values = new ContentValues();
        String date_time = ifi.getDate() + " " + ifi.getTime();
        values.put(COLUMN_NUMBER, fav_number);
        values.put(COLUMN_DATE_TIME, date_time);
        values.put(COLUMN_DESCRIPTION, ifi.getDescription());

        mDB.insert(TABLE_FAVOURITE_INFOITEM, null, values);
    }

    public ArrayList<Favourite> findAllFavourites()
    {
        ArrayList<Favourite> rez = new ArrayList<Favourite>();

        if(mDB == null)
            return null;

        Logs.i("Find all FAVS");
        Cursor c = mDB.rawQuery("select * from Favourite_Table", null);
        if(c == null)
            return null;

        Logs.i("cursor.getColumnCount() = " + c.getColumnCount());
        Logs.i("cursor.getCount() = " + c.getCount());

        if(c.moveToFirst())
        {
            do
            {
                Favourite f = new Favourite();
                f.setNumber(c.getString(0));
                f.setFrom(c.getString(1));
                f.setTo(c.getString(2));
                ArrayList<InfoFavouriteItem> info;
                info = findFavInfo(f.getNumber());
                f.setFavItems(info);
                rez.add(f);
            }
            while (c.moveToNext());
            c.close();
        }

        Logs.i("After adding: rez.length() = " + rez.size());

        return rez;
    }

    public ArrayList<InfoFavouriteItem> findFavInfo(String fav_number)
    {
        if(mDB == null)
            return null;

        Cursor c = mDB.rawQuery("select * from FavouriteInfoItem_Table where number like '" + fav_number + "' ", null);
        if(c == null)
            return null;

        ArrayList<InfoFavouriteItem> rez = new ArrayList<InfoFavouriteItem>();

        if(c.moveToFirst())
        {
            do
            {
                InfoFavouriteItem ifi = new InfoFavouriteItem();
                String date_time = c.getString(1);
                String[] times = date_time.split(" ");
                ifi.setDate(times[0]);
                ifi.setTime(times[1]);
                ifi.setDescription(c.getString(2));
                rez.add(ifi);
            }
            while (c.moveToNext());
            c.close();
        }

        return rez;
    }

    public boolean deleteFav(String fav_number)
    {
        if(mDB == null)
            return false;

        Logs.i("Delete favs fav_number = " + fav_number);

        ArrayList<InfoFavouriteItem> buf = findFavInfo(fav_number);
        Logs.i("buf.size() = " + buf.size());

        /*for(InfoFavouriteItem ifi : buf)
        {
            //String datetime = ifi.getDate() + " " + ifi.getTime();
            int delCount = mDB.delete(TABLE_FAVOURITE_INFOITEM, COLUMN_DESCRIPTION + "='" + ifi.getDescription()+"'", null);
            Logs.i("Delete favs infos = " + delCount + " by description = " + ifi.getDescription());
        }*/


        int delCount = mDB.delete(TABLE_FAVOURITE_INFOITEM, COLUMN_NUMBER + "='" + fav_number+"'", null);
        Logs.i("Delete favs infos = " + delCount);
        int delFavCount = mDB.delete(TABLE_FAVOURITE, COLUMN_NUMBER + "='" + fav_number+"'", null);
        Logs.i("Delete favs = " + delFavCount);

        return true;
    }

    /*public boolean updateFav(int fav_number, InfoFavouriteItem ifi)
    {
        if(mDB == null)
            return false;

        ContentValues values = new ContentValues();
        String date_time = ifi.getDate() + " " + ifi.getTime();
        values.put(COLUMN_DATE_TIME, date_time);
        values.put(COLUMN_DESCRIPTION, ifi.getDescription());

        int updCount = mDB.update(TABLE_FAVOURITE_INFOITEM, values, , null);

        return true;
    }*/
}

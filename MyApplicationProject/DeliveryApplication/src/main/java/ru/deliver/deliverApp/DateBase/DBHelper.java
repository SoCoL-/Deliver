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
import ru.deliver.deliverApp.Utils.Offices;
import ru.deliver.deliverApp.Utils.OfficesAdd;

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

    private static final String TABLE_CONTACTS          = "Contacts_Table";
    private static final String COLUMN_CITY             = "_city";

    private static final String TABLE_CONTACTS_INFO     = "ContactsInfo_Table";
    private static final String COLUMN_NAME             = "_name";
    private static final String COLUMN_PHONE            = "_phone";
    private static final String COLUMN_ADDRESS          = "_address";
    private static final String COLUMN_EMAIL            = "_email";
    private static final String COLUMN_FAX              = "_fax";
    private static final String COLUMN_TIME              = "_time";


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

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS +
                "("
                + COLUMN_CITY + " TEXT,"
                + "PRIMARY KEY (" + COLUMN_CITY + ")" +
                ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);

        String CREATE_CONTACTS_INFO_TABLE = "CREATE TABLE " + TABLE_CONTACTS_INFO +
                "("
                + COLUMN_CITY + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_FAX + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + "PRIMARY KEY (" + COLUMN_ADDRESS + ") ,"
                + "FOREIGN KEY (" + COLUMN_CITY + " )"
                + "REFERENCES " + TABLE_CONTACTS + " (" + COLUMN_CITY + " )" +
                ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE_INFOITEM);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS_INFO);
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

    /**
     * Для перезаписи филиалов базу надо очистить
     * */
    public void deleteContacts()
    {
        mDB.delete(TABLE_CONTACTS, null, null);
        mDB.delete(TABLE_CONTACTS_INFO, null, null);
    }

    /**
     * Добавляем избранное в БД
     * */
    public void addFav(Favourite f)
    {
        Logs.i("Add new FAV");
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER, f.getNumber());
        values.put(COLUMN_FROM, f.getFrom());
        values.put(COLUMN_TO, f.getTo());

        mDB.insert(TABLE_FAVOURITE, null, values);
    }

    /**
     * Добавляем информацию избранного в БД
     * */
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

    /**
     * Добавляем филиал в БД
     * */
    public void addContact(Offices o)
    {
        Logs.i("Add new OFFICES");
        ContentValues values = new ContentValues();
        values.put(COLUMN_CITY, o.getCity());

        mDB.insert(TABLE_CONTACTS, null, values);
    }

    /**
     * Добавляем информацию филиала в БД
     * */
    public void addContactInfo(String city, OfficesAdd oa)
    {
        Logs.i("Add new OFFICES_ADD");
        ContentValues values = new ContentValues();
        values.put(COLUMN_CITY, city);
        values.put(COLUMN_NAME, oa.getName());
        values.put(COLUMN_ADDRESS, oa.getAddress());
        values.put(COLUMN_PHONE, oa.getPhone());
        values.put(COLUMN_EMAIL, oa.getEMail());
        values.put(COLUMN_FAX, oa.getFax());
        values.put(COLUMN_TIME, oa.getTime());

        mDB.insert(TABLE_CONTACTS_INFO, null, values);
    }

    /**
     * Поиск всех избранных в БД
     * */
    public ArrayList<Favourite> findAllFavourites()
    {
        ArrayList<Favourite> rez = new ArrayList<Favourite>();

        if(mDB == null)
            return rez;

        Logs.i("Find all FAVS");
        Cursor c = mDB.rawQuery("select * from Favourite_Table", null);
        if(c == null)
            return rez;

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

    /**
     * Поиск всех филиалов в БД, если отсутствует интернет
     * */
    public ArrayList<Offices> findAllContacts()
    {
        ArrayList<Offices> contacts = new ArrayList<Offices>();
        if(mDB == null)
            return contacts;

        Logs.i("Find all Contacts");
        Cursor c = mDB.rawQuery("select * from Contacts_Table", null);
        if(c == null)
            return contacts;

        if(c.moveToFirst())
        {
            do
            {
                Offices o = new Offices();
                o.setCity(c.getString(0));
                o.setContacts(findAllContactInfo(c.getString(0)));
                contacts.add(o);
            }
            while (c.moveToNext());
            c.close();
        }

        Logs.i("After find Contacts: contacts.length() = " + contacts.size());

        return contacts;
    }

    /**
     * Поиск информации избранного в БД
     * */
    public ArrayList<InfoFavouriteItem> findFavInfo(String fav_number)
    {
        ArrayList<InfoFavouriteItem> rez = new ArrayList<InfoFavouriteItem>();

        if(mDB == null)
            return rez;

        Cursor c = mDB.rawQuery("select * from FavouriteInfoItem_Table where number like '" + fav_number + "' ", null);
        if(c == null)
            return rez;

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

    /**
     * Поиск информации о филиале
     * */
    public ArrayList<OfficesAdd> findAllContactInfo(String city)
    {
        ArrayList<OfficesAdd> contactInfo = new ArrayList<OfficesAdd>();

        if(mDB == null)
            return contactInfo;

        Logs.i("city = " + city);
        Cursor c = mDB.rawQuery("select * from ContactsInfo_Table where _city like '" + city + "' ", null);
        if(c == null)
            return contactInfo;

        if(c.moveToFirst())
        {
            do
            {
                OfficesAdd oa = new OfficesAdd();
                oa.setName(c.getString(1));
                oa.setAddress(c.getString(2));
                oa.setPhone(c.getString(3));
                oa.setEMail(c.getString(4));
                oa.setFax(c.getString(5));
                oa.setTime(c.getString(6));
                contactInfo.add(oa);
            }
            while (c.moveToNext());
            c.close();
        }

        Logs.i("After find ContactsInfo: contactInfo.length() = " + contactInfo.size());

        return contactInfo;
    }

    /**
     * Удаление избранного из БД
     * */
    public boolean deleteFav(String fav_number)
    {
        if(mDB == null)
            return false;

        Logs.i("Delete favs fav_number = " + fav_number);

        ArrayList<InfoFavouriteItem> buf = findFavInfo(fav_number);
        Logs.i("buf.size() = " + buf.size());

        int delCount = mDB.delete(TABLE_FAVOURITE_INFOITEM, COLUMN_NUMBER + "='" + fav_number+"'", null);
        Logs.i("Delete favs infos = " + delCount);
        int delFavCount = mDB.delete(TABLE_FAVOURITE, COLUMN_NUMBER + "='" + fav_number+"'", null);
        Logs.i("Delete favs = " + delFavCount);

        return true;
    }
}

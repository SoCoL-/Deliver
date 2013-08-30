package ru.deliver.deliverApp.Network;

import java.io.InputStream;

/**
 * Created by Evgenij on 28.08.13.
 *
 * Ответ от сервака
 */
public class ResponceTask
{
    public String mResponceTag;
    public int mResponceCode;
    public InputStream mJSON;
    public String mErrorText;

    public ResponceTask(int code, String tag, InputStream object, String error)
    {
        this.mResponceCode = code;
        this.mResponceTag = tag;
        this.mJSON = object;
        this.mErrorText = error;
    }
}

package ru.deliver.deliverApp.Network;

import org.json.JSONObject;

/**
 * Created by Evgenij on 28.08.13.
 *
 * Ответ от сервака
 */
public class ResponceTask
{
    public String mResponceTag;
    public int mResponceCode;
    public JSONObject mJSON;

    public ResponceTask(int code, String tag, JSONObject object)
    {
        this.mResponceCode = code;
        this.mResponceTag = tag;
        this.mJSON = object;
    }
}

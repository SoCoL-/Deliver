package ru.deliver.deliverApp.Network;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Evgenij on 28.08.13.
 *
 * Структура запроса
 */
public final class RequestTask
{
    public String mRquestTag;
    public JSONArray mJSON;

    public RequestTask(String tag, JSONArray object)
    {
        this.mJSON = object;
        this.mRquestTag = tag;
    }
}

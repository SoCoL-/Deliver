package ru.deliver.deliverApp.Network;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.deliver.deliverApp.Setup.Logs;

/**
 * Created by Evgenij on 28.08.13.
 *
 * Асинхронный запрос
 */
public final class Request extends AsyncTask<String, Integer, ResponceTask>
{
    private static final String ADDRESS = "http://exmail.ws/mobile/mac.php";

    private static final int HTTP_OK = 200;
    private static final String TYPE = "TYPE";
    private static final String DATA = "DATA";

    private RequestTask mReqTask;
    private ResponceTask mRespTask;

    private IProgress progress;

    public Request(RequestTask task)
    {
        Logs.i("Create Request");
        this.mReqTask = task;
    }

    public void setInterface(IProgress p)
    {
        Logs.i("Set Interface");
        this.progress = p;

        if(progress != null)
            if(mRespTask != null)
                progress.GetResponce(mRespTask);
    }

    @Override
    protected ResponceTask doInBackground(String... strings)
    {
        Logs.i("Start doInBackground");
        HttpPost mPost = new HttpPost(ADDRESS);

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(httpParameters, 60000);
        HttpConnectionParams.setConnectionTimeout(httpParameters, 60000);

        DefaultHttpClient client = new DefaultHttpClient(httpParameters);

        if(mReqTask == null)
        {
            Logs.i("mReqTask == null");
        }

        try
        {
            List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
            nvps.add(new BasicNameValuePair(TYPE, mReqTask.mRquestTag));
            nvps.add(new BasicNameValuePair(DATA, mReqTask.mJSON.toString()));
            UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
            mPost.setEntity(p_entity);

            Logs.i("request = " + nvps.toString());

            long time = System.currentTimeMillis();
            Logs.i("Начало запроса: " + System.currentTimeMillis());
            HttpResponse response = client.execute(mPost);
            Logs.i(response.getStatusLine().toString());
            if(response.getStatusLine().getStatusCode() != HTTP_OK)
            {
                //TODO Создать JSONObject с информацией об ошибке со стороны сервака
                Logs.e("Вернули не с 200, а с "+response.getStatusLine().getStatusCode());
                return new ResponceTask(response.getStatusLine().getStatusCode(), mReqTask.mRquestTag, null);
            }
            HttpEntity responseEntity = response.getEntity();
            InputStream is = responseEntity.getContent();
            Long mLength = responseEntity.getContentLength();
            Logs.i("Конец запроса: "+(System.currentTimeMillis()-time));

            Logs.i("Обработка ответа: " + System.currentTimeMillis());
            time = System.currentTimeMillis();
            String rez = new String(readBytesFromStream(is, mLength));
            Logs.i("Конец обработки: "+(System.currentTimeMillis()-time));
            time = System.currentTimeMillis();
            Logs.i("Начало создания JSON "+time);
            JSONObject json = new JSONObject(rez);
            Logs.i("Конец создания JSON "+(System.currentTimeMillis()-time));

            return new ResponceTask(HTTP_OK, mReqTask.mRquestTag, json);
        }
        catch(Exception e)
        {
            //TODO Сделать JSON оповещающий о превышении ожидания ответа от сервака
            Logs.e(e.toString());
            return new ResponceTask(-1, mReqTask.mRquestTag, null);
        }
    }

    @Override
    protected void onPostExecute(ResponceTask responce)
    {
        mRespTask = responce;

        if(progress != null && mRespTask != null)
        {
            progress.GetResponce(mRespTask);
        }

        progress = null;
    }

    private byte[] readBytesFromStream(InputStream is, long length) throws IOException
    {
        int bytesRead, offset = 0;
        if (length > Integer.MAX_VALUE)
        {
            Logs.e("Слишком большой объём");
            return null;
        }
        final int BLOCK_SIZE = 78*10240;
        byte[] data = new byte[20480];

        do
        {
            bytesRead = is.read(data, offset, data.length - offset);
            offset+=bytesRead;
            if(offset == data.length)
            {
                //ext array
                byte[] mas = new byte[data.length + BLOCK_SIZE];
                for(int i =0 ;i< data.length; i++)
                {
                    mas[i] = data[i];
                }
                data = null;
                data = mas;
            }
        }
        while(bytesRead > 0);
        return data;
    }
}

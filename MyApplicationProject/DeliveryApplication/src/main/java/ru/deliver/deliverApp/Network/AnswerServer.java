package ru.deliver.deliverApp.Network;

import java.util.ArrayList;

/**
 * Created by Evgenij on 28.08.13.
 *
 * Ответ окну от сервера
 */
public interface AnswerServer
{
    void ResponceOK(String TAG, ArrayList<String> params);
    void ResponceError(String TAG, String text);
}

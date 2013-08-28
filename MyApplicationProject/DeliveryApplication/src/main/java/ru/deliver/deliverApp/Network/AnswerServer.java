package ru.deliver.deliverApp.Network;

/**
 * Created by Evgenij on 28.08.13.
 *
 * Ответ окну от сервера
 */
public interface AnswerServer
{
    void ResponceOK(String TAG);
    void ResponceError(String TAG);
}

package ru.deliver.deliverApp.Network;

/**
 * Created by Evgenij on 28.08.13.
 *
 * Интерфейс оповещения пользователя об ответе сервака
 */
public interface IProgress
{
    /**
     * Информируем на ответ от сервака
     * */
    void GetResponce(ResponceTask task);
}

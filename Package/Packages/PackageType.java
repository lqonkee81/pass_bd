/*
 * Типы, на котоорые разделяются пакеты
 *
 * REGISTRATION - регистрация пользователя
 * AUTHORIZATION - авторизация пользователя на сервере
 * ADD_AUTHORIZE_DATA - добавление данных для авторизации на каких-либо сайтах
 * DELETE_AUTHORIZE_DATA - удаление данных для авторизации на каких-либо сайтах
 * SERVICE - сервисные пакеты для неявного общения между сервером и клиентомы
 *
 * */


public enum PackageType {
    REGISTRATION,
    AUTHORIZATION,
    ADD_AUTHORIZE_DATA,
    DELETE_AUTHORIZE_DATA,
    SERVICE_ERROR,
    SERVICE_ACCEPT,
    ENCRYPTION
}
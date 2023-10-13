# **Прототип онлайн-магазина**

### Требования
 - Java 8
 - PostgreSQL 15
 - Docker

### Инструкция по запуску
- Восстановить дамп БД (file: *shop_db_dump.sql*)
  - psql -U *username* -a -f *FilePath*
- Для запуска в среде разработки запустите файл *OnlineShopApplication*
- Для запуска из Docker выполните команды:
  - docker-compose build
  - docker-compose up

Доступ к админ-аккаунту: 
- Логин: serzhik2002@gmail.com
- Пароль: adminadmin
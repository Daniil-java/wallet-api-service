## 0.0.2 (06.12.2024)
[et-2]
* Реализация эндпоинта POST /api/v1/wallet для операций пополнения и снятия средств. 
* Реализация эндпоинта GET /api/v1/wallets/{walletId} для получения баланса кошелька. 
* Добавление глобальной обработки ошибок (@ControllerAdvice) для стандартных ошибок. 
* Добавление Swagger/OpenAPI для документирования API.

## 0.0.1 (06.12.2024)
[et-1]
* Настройка application.yaml
* Настройка PostgreSQL и создание Docker Compose файла для базы данных.
* Разработка Liquibase-скриптов для миграции базы данных.
* Создание соответствующих сущнотей
* Реализация схемы базы данных (таблицы для кошельков и транзакций).
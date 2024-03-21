# ChargingNetwork
Инструкция по запуску приложения:
 - Скачать исходный код проекта [архивом](https://github.com/NikushinIvan/ChargingNetwork/archive/refs/heads/main.zip)
или загрузить Гит проект, запустив в командной строке `git clone https://github.com/NikushinIvan/ChargingNetwork.git`
 - Для запуска базы данных потребуется установленный Docker image postgres 15.6. Установить образ неоходимо командой `docker pull postgres:15.6`
 - Запустить контейнер с базой данных командой `docker run --name ChargingNetwork -p18000:5432 -e POSTGRES_USER=charging_network_app -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=ChargingNetwork -d postgres:15.6`
 - Запустить Spring Boot приложение из IDE
 - UI будет доступен по адресу [http://localhost:8080](http://localhost:8080)
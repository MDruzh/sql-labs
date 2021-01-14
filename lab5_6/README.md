# lab 5

1. Створив ресурсну групу на Azure.
![alt text](screenshots/1.png "Опис")
2. Створив Event Hub та його entity.
![alt text](screenshots/2.png "Опис")
3. Додав та налаштував відповідний Shared Access Policy.
![alt text](screenshots/3.png "Опис")
4. Створив новий інстанс Azure Cache for Redis.
![alt text](screenshots/4.png "Опис")
5. Запустив код. Принцип його роботи наступний: спочатку відбувається запис даних в консоль, коли відбувся запис половини усіх об'єктів, відбувається запис безпосередньо в Event Hub. Для запису даних скористався аплікацією Postman: метод GET на localhos:8080/url.
![alt text](screenshots/5.png "Опис")
![alt text](screenshots/6.png "Опис")
6. Для перевірки перейшов в Event Hub та подивися на запис даних. Для цього використав Process data для інстанса Event Hub.
![alt text](screenshots/7.png "Опис")
7. А також перевірив запис даних у редісі.
![alt text](screenshots/8.png "Опис")



# MuseumBot

## Задачи: 
1) Реализовать базовый функционал бота: 
- реализовать функции /help и /start 
- посмотреть список ближайших мероприятий: /viewUpcomingEvents
- записаться на мероприятие: /singUpForEvent <EventId>
- отменить запись: /cancel <EventId>
- посмотреть на записанные мероприятия: /viewMyEvents

Примеры:
/start:
```
User: /start
Bot: Здравствуйте, я бот, который поможет вам отслеживать предстоящие культурные мероприятия.
Доступны следующие команды:
	/help - Справка
	/viewUpcomigEvents - Посмотреть предстоящие мероприятия
	/singUpForEvent <EventId> - Зарегистрироваться на мероприятие по его идентификационному номеру. Этот номер можно узнать из функции /viewUpcomigEvents.
	/viewMyEvents - Посмотреть список мероприятий, на которые вы записаны.
	/cancel <EventId> - Отменить запись на мероприятие.
```

/help: Почти тот же текст что и в start, только без первого предложения.

/viewUpcomingEvents
```
User: /viewUpcomingEvents
Bot: 
1) Импрессионизм в России. Живопись из собрания Государственного Русского музея
EventId: 11
Описание: «Русский импрессионизм» — явление спорное, по мнению академической науки, и, безусловно, эффектное, по мнению зрителей.
Нежные переливы солнечного света в весенней листве, легкий бриз на побережье Черного моря,
сочные ароматы цветов и фруктов — это те ощущения, которые передают через краски такие мэтры русского искусства,
как К. Коровин, И. Левитан, И. Репин и прочие, в чьих работах угадываются черты импрессионизма.
Место проведения: Музей изобразительных искусств (пл. Революции, 1) (MuseumId: 112)
Дата: 30.10.2023
Время начала:  17:40
Длительность: 1 час

2) Экспозиция Центра истории камнерезного дела
EventId: 12
Описание: нет описания
Место проведения: Центр истории камнерезного дела (ул. Пушкина, 5) (MuseumId: 111)
Дата: 31.10.2023
Время начала:  17:00
Длительность: 1 час
```

/singUpForEvent <EventId>
```
User: /singUpForEvent 12
Bot: Вы успешно записались на мероприятие “Экспозиция Центра истории камнерезного дела”
```

/viewMyEvents
```
User: /viewMyEvents
Bot: 
Экспозиция Центра истории камнерезного дела
EventId: 12
Описание: нет описания
Место проведения: Центр истории камнерезного дела (ул. Пушкина, 5) (MuseumId: 111)
Дата: 31.10.2023
Время начала:  17:00
Длительность: 1 час
```

/cancel <EventId>
```
User: /cancel 12
Bot: Запись на мероприятие отклонена успешно.
```
 

2) Реализовать функционал, связанный с музеем и его рейтингом: 

- посмотреть информацию об экспонате (предполагается, что возможно это только во время выставки?): /viewExhibit <ExhibitName>
- оценить выставку/оставить отзыв: /leaveReview
- посмотреть информацию о конкретном музее: /viewMuseum <MuseumId>
- посмотреть отзывы/рейтинг музея: /viewMuseumRank <MuseumId>


Примеры:
/viewExhibit <ExhibitName>
```
User: /viewExhibit 117
Bot: Большой Шигирский идол — самая древняя монументальная скульптура из дерева в мире.
Ее создали из лиственницы более 11 тысяч лет назад. Идола обнаружили в 1890 году при добыче золота на Шигирском торфянике недалеко от Екатеринбурга.

Еще в древности идол распался на части.
В конце XIX века из произвольно соединенных кусков музейные работники сделали фигуру высотой почти в три метра — с руками и скрещенными ногами.
В 1914 году скульптуру собрали заново, на этот раз уже с неиспользованными раньше фрагментами — так идол достиг пятиметровой высоты.
Фигура расписана геометрическими узорами и лицами, всего на идоле можно увидеть восемь персонажей.
```

/leaveReview
```
User: /leaveReview
Bot: Выберите мероприятие:
| Импрессионизм в России… | Экспозиция Центра истории… |
U: *Нажимает на первую кнопку*
B: Как мы оцениваете данное мероприятие от 0 до 10, где 0 - это не понравилось совсем; 10 - очень понравилось?
U: 8,5
B: Напишите, пожалуйста, небольшой отзыв. Что вам понравилось/запомнилось больше всего.
U: *Пишет отзыв*
```

/viewMuseum <MuseumId>
```
User: /viewMuseum 112
Bot: Музей изобразительных искусств
MuseumId: 112
Адрес: пл. Революции, 1
Описание: Челябинский государственный музей изобразительных искусств — единственный в Челябинской области художественный музей,
последовательно представляющий развитие регионального, русского, западноевропейского, восточного искусства.
Открыт 6 июня 1940 года в здании Александро-Невской церкви на Алом поле, в то время имевшей статус картинной галереи.

Коллекция музея насчитывает более 16 000 произведений, включающих живопись, графику, скульптуру, декоративно-прикладное искусство.
```

/viewMuseumRank <MuseumId>
```
User: /viewMuseumRank 112
Bot: Средняя оценка от пользователей - 8,5. Ниже несколько последних отзывов.
| Оценка: 10; Мероприятие: Импрессионизм в России… |
| Оценка: 8,5; Мероприятие: Импрессионизм в России… |
U: *Нажимает на первый отзыв*
B: 
Мероприятие: Импрессионизм в России. Живопись из собрания Государственного Русского музея
Оценка: 8,5
Дата проведения: 30.10.2023
Отзыв: Очень понравилось. Только не успел пару картин посмотреть(
```

3) Дополнить функционал:
- бот присылает напоминание о мероприятии
- бот с какой-то периодичностью присылает информацию о рандомном экспонате (данную функцию можно будет отключить) (имеет смысл, чтобы экспонат был с ближайшего мероприятия, тогда это будет типа завлечение пользователя на мероприятие)
- реализовать викторины (после выставки бот предлагает пользователю пройти викторину по выставке)
- пользователь может посмотреть свои настройки уведомлений /viewSettings
- пользователь может изменить свои настройки: /changeSettings

/viewSettings
/changeSettings
Напоминание
Случайный экспонат
Викторины

![image](https://github.com/Elenaz441/MuseumBot/assets/102030455/57f9f75d-2416-4a12-99be-a2d484e99189)

Структура бд 

```
Итоговый текст для \start и \help: 
	/help - Справка
	/viewUpcomigEvents - Посмотреть предстоящие мероприятия
	/singUpForEvent <EventId> - Зарегистрироваться на мероприятие по его идентификационному номеру. Этот номер можно узнать из функции /viewUpcomigEvents.
	/viewMyEvents - Посмотреть список мероприятий, на которые вы записаны.
	/cancel <EventId> - Отменить запись на мероприятие.
	/viewExhibit <ExhibitName> - Посмотреть информацию о экспонате. Данная функция доступна тогда, когда вы находитесь на каком-либо мероприятии. Название экспоната вы можете узнать в музее.
	/leaveReview - Оставить отзыв. При вызове этой функции вам будет выдан список мероприятий, на которые вы ещё не оставили отзыв.
	/viewMuseum <MuseumId> - Посмотреть информацию о музее.
	/viewMuseumRank <MuseumId> - Посмотреть рейтинг музея и прочитать отзывы о мероприятиях, которые проходили в этом музее.
	/viewSettings - Посмотреть настройки уведомлений.
	/changeSettings - Изменить настройки уведомлений.
Также я могу присылать вам напоминание за день до мероприятия, информацию о случайном экспонате и предлагать пройти викторины после мероприятия. Вы можете настроить все эти функции с помощью команды /changeSettings.
```

package ru.urfu.museumbot.service;

import org.springframework.stereotype.Service;
import ru.urfu.museumbot.models.Event;

import java.util.*;
@Service
public class EventController {
    private Long ID = 0L;
    private final List<Event> listEvents = new ArrayList<>();

    public EventController() {
    }
    {
        this.listEvents.add(new Event(++ID, "Постоянная экспозиция музея радио им. А.С.Попова",
                "Вы окажетесь музейной «лаборатории» и наглядно узнаете принципы действия вибратора Герца, " +
                        "трансформатора Tesla, электрофорной машины, телеграфного аппарата Морзе, " +
                        "приемника Александра Попова образца 1895 года и не менее знаковых изобретений " +
                        "его предшественников.",
                new GregorianCalendar(2023, Calendar.NOVEMBER, 1, 11, 0).getTime(),
                120,
                "ул. Розы Люксембург, 9/11"));
        this.listEvents.add(new Event(++ID, "Монетный двор и всё, что в нём",
                "Мы обсудим историю появления денег, поиграем в древний рынок и " +
                        "изучим основные законы изготовления монет. Узнаем, какие деньги делали " +
                        "в нашем городе и как долго работал екатеринбургский Монетный двор.\n" +
                        "А еще вместе с Лисенком изготовим собственную денежку и вспомним азартные игры наших бабушек и дедушек.",
                new GregorianCalendar(2023, Calendar.NOVEMBER, 4, 13, 0).getTime(),
                120,
                "ул. Карла Либкнехта, 26"));
    }

    public List<Event> getListEvents() {
        return listEvents;
    }
}

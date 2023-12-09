package ru.urfu.museumbot.commands;

/**
 * класс контекста для выполнения {@link Command}
 */
public class CommandArgs {

    /**
     * Идентификатор чата, которому предназначено сообщение
     */
    private Long chatId;

    /**
     * данные, которые отправляются при нажатии на кнопку
     * содержат информацию, разделённую пробелом над каким объектом
     * и какую операцию на данный момент выполняем
     */
    private String callbackData = "";

    /**
     * ответ пользователя
     */
    private String userInput = "";


    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }

}

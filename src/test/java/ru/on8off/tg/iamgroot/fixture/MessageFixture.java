package ru.on8off.tg.iamgroot.fixture;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.List;

public class MessageFixture {

    public static Message privateMessage(){
        return createMessage("test", 1L, "private", null, "FirstName", "LastName", null);
    }

    public static Message groupMessage(String botNameMention){
        return createMessage("test", -1L, "group", "Test Group", null, null, botNameMention);
    }
    public static Message command(String command){
        var msg = new Message();
        msg.setDate(getDate());
        msg.setText(command);
        var chat = new Chat();
        chat.setId(1L);
        chat.setType("group");
        chat.setTitle("Group");
        msg.setChat(chat);
        var entity = new MessageEntity();
        entity.setType("bot_command");
        entity.setOffset(0);
        entity.setLength(command.length());
        msg.setEntities(List.of(entity));
        return msg;
    }

    public static Message createMessage(String messageText, Long chatId, String chatType, String chatTitle, String firstName, String lastName, String botNameMention){
        var msg = new Message();
        msg.setDate(getDate());
        if(botNameMention != null) {
            msg.setText("@" + botNameMention + " " +messageText);
        } else {
            msg.setText(messageText);
        }
        var chat = new Chat();
        chat.setId(chatId);
        chat.setType(chatType);
        chat.setTitle(chatTitle);
        chat.setFirstName(firstName);
        chat.setLastName(lastName);
        msg.setChat(chat);
        if(botNameMention != null) {
            msg.setText("@" + botNameMention);
            var entity = new MessageEntity();
            entity.setType("mention");
            entity.setText("@" + botNameMention);
            entity.setOffset(0);
            entity.setLength(msg.getText().length());
            msg.setEntities(List.of(entity));
        }
        return msg;
    }

    private static Integer getDate(){
        var now = System.currentTimeMillis();
        return (int)(now / 1000);
    }
}

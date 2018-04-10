package com.example.stack.welearn.fixtures;

import com.example.stack.welearn.entities.ChatMessage;
import com.example.stack.welearn.entities.ChatUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
 * Created by troy379 on 12.12.16.
 */
public final class MessagesFixtures extends FixturesData {
    private MessagesFixtures() {
        throw new AssertionError();
    }

    public static ChatMessage getImageMessage() {
        return new ChatMessage()
                .setUser(getUser())
                .setImage(new ChatMessage.Image(getRandomImage()))
                .setId(getRandomId());
    }

    public static ChatMessage getVoiceMessage() {
        ChatMessage message=new ChatMessage()
                .setId(getRandomId())
                .setUser(getUser());
        message.setVoice(new ChatMessage.Voice("http://example.com", rnd.nextInt(200) + 30));
        return  message;
    }

    public static ChatMessage getTextMessage() {
        return getTextMessage(getRandomMessage());
    }

    public static ChatMessage getTextMessage(String text) {
            return new ChatMessage()
                    .setId(getRandomId())
                    .setUser(getUser())
                .setText(text).setCreatedAt(new Date());
    }

    public static ArrayList<ChatMessage> getMessages(Date startDate) {
        ArrayList<ChatMessage> messages = new ArrayList<>();
        for (int i = 0; i < 10/*days count*/; i++) {
            int countPerDay = rnd.nextInt(5) + 1;

            for (int j = 0; j < countPerDay; j++) {
                ChatMessage message;
                if (i % 2 == 0 && j % 3 == 0) {
                    message = getImageMessage();
                } else {
                    message = getTextMessage();
                }

                Calendar calendar = Calendar.getInstance();
                if (startDate != null) calendar.setTime(startDate);
                calendar.add(Calendar.DAY_OF_MONTH, -(i * i + 1));
                message.setCreatedAt(calendar.getTime());
                messages.add(message);
            }
        }
        return messages;
    }

    private static ChatUser getUser() {
        boolean even = rnd.nextBoolean();
        return new ChatUser()
                .setId(even?"0":"1")
                .setName(even? names.get(0):names.get(1))
                .setAvatar(even?avatars.get(0):avatars.get(1))
                .setOnline(true);


    }
}

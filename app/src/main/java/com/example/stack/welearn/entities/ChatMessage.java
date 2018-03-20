package com.example.stack.welearn.entities;

import android.speech.tts.Voice;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

/**
 * Created by stack on 2018/1/17.
 */

public class ChatMessage implements IMessage,MessageContentType.Image,MessageContentType{
    private String id;
    private String text;
    private IUser user;
    private Date createdAt;
    private Image image;
    private Voice voice;

    private boolean isTeacher;


    public Voice getVoice() {
        return voice;
    }

    public ChatMessage setVoice(Voice voice) {
        this.voice = voice;
        return this;
    }

    public Image getImage() {
        return image;
    }

    public ChatMessage setImage(Image image) {
        this.image = image;
        return this;
    }

    public ChatMessage setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ChatMessage setId(String id) {
        this.id = id;
        return this;
    }

    public ChatMessage setText(String text) {
        this.text = text;
        return this;
    }

    public ChatMessage setUser(IUser user) {
        this.user = user;
        return this;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getImageUrl() {
//        return image.url;
        return image==null?null:image.url;
    }

    public static class Image{
        private String url;

        public Image(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public Image setUrl(String url) {
            this.url = url;
            return this;
        }
    }

    public static class Voice{
        private String url;
        private int duration;

        public Voice(String url, int duration) {
            this.url = url;
            this.duration = duration;
        }

        public String getUrl() {
            return url;
        }

        public Voice setUrl(String url) {
            this.url = url;
            return this;
        }

        public int getDuration() {
            return duration;
        }

        public Voice setDuration(int duration) {
            this.duration = duration;
            return this;
        }
    }
}

package com.example.stack.welearn.entities;

import cn.jiguang.imui.commons.models.IUser;

/**
 * Created by stack on 2018/1/9.
 */

public class User implements IUser {
    public String id;
    public String displayName;
   public String avatar;
    @Override
    public String getId() {
//        return null;
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getAvatarFilePath() {
        return avatar;
    }

    public static  class DefaultUser implements IUser{
        String id;
        String displayName;
        String avatar;

        public DefaultUser(String id, String displayName, String avatar) {
            this.id = id;
            this.displayName = displayName;
            this.avatar = avatar;
        }

        public DefaultUser() {
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getDisplayName() {
            return null;
        }

        @Override
        public String getAvatarFilePath() {
            return null;
        }
    }
}


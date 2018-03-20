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


}


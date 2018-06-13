package vn.edu.hcmute.ms14110050.chatproject.model.node_users.last_message;

import android.databinding.BaseObservable;

import vn.edu.hcmute.ms14110050.chatproject.common.constant.MESSAGE_TYPE;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public class LastMessagePOJO extends BaseObservable implements Cloneable{

    protected boolean isSender;

    protected long createTime;

    protected String content;

    @MESSAGE_TYPE
    protected int messageType;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}

package vn.edu.hcmute.ms14110050.chatproject.common;

/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 *
 * Cần cải tiến lại sử dụng generic, giảm bớt việc cast
 */

public class SendObject {
    int tag;
    Object value;

    public SendObject() {

    }

    public SendObject(int tag, Object value) {
        this.tag = tag;
        this.value = value;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

package vn.edu.hcmute.ms14110050.chatproject.common.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static vn.edu.hcmute.ms14110050.chatproject.common.constant.MESSAGE_TYPE.IMAGE;
import static vn.edu.hcmute.ms14110050.chatproject.common.constant.MESSAGE_TYPE.SOUND;
import static vn.edu.hcmute.ms14110050.chatproject.common.constant.MESSAGE_TYPE.TEXT;

/**
 * Created by Vo Ngoc Hanh on 5/30/2018.
 */

@IntDef({TEXT, IMAGE, SOUND})
@Retention(RetentionPolicy.SOURCE)
public @interface MESSAGE_TYPE {
    int TEXT = 0;
    int IMAGE = 1;
    int SOUND = 2;
}
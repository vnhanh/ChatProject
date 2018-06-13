package vn.edu.hcmute.ms14110050.chatproject.common;

/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public class IntegerUnique {
    private static int id = 0;

    public static int generate() {
        return ++id;
    }
}

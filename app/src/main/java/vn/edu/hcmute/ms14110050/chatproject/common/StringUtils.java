package vn.edu.hcmute.ms14110050.chatproject.common;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by Vo Ngoc Hanh on 5/20/2018.
 */

public class StringUtils {
    public static boolean isEmpty(String string) {
        return string == null || string.equals("");
    }

    public static String deAccent(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCOMBINING_DIACRITICAL_MARKS}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}

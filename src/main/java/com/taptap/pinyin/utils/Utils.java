package com.taptap.pinyin.utils;

import com.github.houbb.heaven.util.lang.StringUtil;

/**
 * @author kl (http://kailing.pub)
 * @since 2021/2/22
 */
public class Utils {

    private Utils(){}

    public static String trim(String text){
        if(StringUtil.isBlank(text)) return text;
        return text.replace(" ","");
    }
}

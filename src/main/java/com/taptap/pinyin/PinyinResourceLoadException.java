package com.taptap.pinyin;

/**
 * @author kl (http://kailing.pub)
 * @since 2021/2/19
 */
public class PinyinResourceLoadException extends RuntimeException{
    PinyinResourceLoadException(){
        super("字典文件加载异常");
    }
}

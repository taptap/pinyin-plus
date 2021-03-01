package com.taptap.pinyin;

/**
 * 词组
 * @author kl (http://kailing.pub)
 * @since 2021/2/19
 */
public class Word {

    private String simplified;
    private String traditional;
    private String pinyin;
    private String pinyinNoTone;

    public Word() {
    }

    public Word(String simplified, String traditional, String pinyin, String pinyinNoTone) {
        this.simplified = simplified;
        this.traditional = traditional;
        this.pinyin = pinyin;
        this.pinyinNoTone = pinyinNoTone;
    }

    public String getPinyinNoTone() {
        return pinyinNoTone;
    }

    public void setPinyinNoTone(String pinyinNoTone) {
        this.pinyinNoTone = pinyinNoTone;
    }

    public String getSimplified() {
        return simplified;
    }

    public void setSimplified(String simplified) {
        this.simplified = simplified;
    }

    public String getTraditional() {
        return traditional;
    }

    public void setTraditional(String traditional) {
        this.traditional = traditional;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

}

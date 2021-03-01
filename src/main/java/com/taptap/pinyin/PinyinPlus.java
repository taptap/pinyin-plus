package com.taptap.pinyin;

import com.github.houbb.heaven.util.lang.CharUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.segment.support.segment.result.impl.SegmentResultHandlers;
import com.taptap.pinyin.analyzer.WordAnalyzer;
import com.taptap.pinyin.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author kl (http://kailing.pub)
 * @since 2021/2/19
 */
public class PinyinPlus {

    private static final HashMap<String, Word> words = ResourceLoad.loadCedict();
    private static final WordAnalyzer wordAnalyzer = WordAnalyzer.newInstance();

    private PinyinPlus(){}

    /**
     * 汉字转拼音索引模式，词组拼音空格隔开返回
     * @param text 汉字文本
     * @return 拼音
     */
    public static String toIndex(String text){
       return to(text,true);
    }

    /**
     * 汉语转拼音，单子拼音空格隔开返回
     * @param text 汉字文本
     * @return 拼音
     */
    public static String to(String text){
        return to(text,false);
    }

    /**
     * 汉字转拼音
     * @param text 汉字文本Y
     * @param isIndex 是否索引模式
     * @return 拼音
     */
    private static String to(String text,boolean isIndex) {
        if(StringUtil.isBlank(text))return text;
        Word word = words.get(text);
        if (word != null) {
            if(isIndex){
                return Utils.trim(word.getPinyinNoTone());
            }else {
                return word.getPinyinNoTone();
            }
        } else {
            StringJoiner joiner = new StringJoiner(" ");
            List<String> segmentResult = wordAnalyzer.segment(text, SegmentResultHandlers.word());
            for (String segmentStr : segmentResult) {
                word = words.get(segmentStr);
                if(word != null){
                    if(isIndex){
                        joiner.add(Utils.trim(word.getPinyinNoTone()));
                    }else {
                        joiner.add(word.getPinyinNoTone());
                    }
                }else {
                    List<Character> characterList = StringUtil.toCharacterList(segmentStr);
                    for (Character character :characterList){
                        if (CharUtil.isChinese(character)){
                            word = words.get(character.toString());
                            joiner.add(word.getPinyinNoTone());
                        }else {
                            joiner.add(character.toString());
                        }
                    }
                }
            }
            return joiner.toString();
        }
    }

}

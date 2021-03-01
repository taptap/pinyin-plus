package com.taptap.pinyin;

import com.github.houbb.segment.support.segment.result.impl.SegmentResultHandlers;
import com.taptap.pinyin.analyzer.WordAnalyzer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


/**
 * @author kl (http://kailing.pub)
 * @since 2021/2/22
 */
class PinyinPlusTest {

    @Test
    void testToPinYin() {
        String pinyin = PinyinPlus.to("率土之滨");
        System.err.println(pinyin);
        Assertions.assertEquals("shuai tu zhi bin", pinyin);
    }

    @Test
    void testToPinYin2() {
        String pinyin = PinyinPlus.toIndex("写的射雕英雄传");
        System.err.println(pinyin);
        Assertions.assertEquals("xie de shediaoyingxiongzhuan", pinyin);
    }

    @Test
    void testWordAnalyzer() {
        List<String> words = WordAnalyzer.newInstance().segment("我喜欢看武侠小说，特别是金庸先生写的射雕英雄传", SegmentResultHandlers.word());
        System.err.println(words);
        Assertions.assertEquals("[我, 喜欢, 看, 武侠小说, ，, 特别, 是, 金庸, 先生, 写, 的, 射雕英雄传]",words.toString());
    }


}
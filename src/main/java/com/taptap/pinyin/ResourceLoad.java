package com.taptap.pinyin;


import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.StringUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author kl (http://kailing.pub)
 * @since 2021/2/19
 */
public class ResourceLoad {

    private static final String CEDICT_FILE = "cedict_ts.u8";
    private static final String EXT_CEDICT_FILE = "cedict_ts_extend.u8";
    private static final String CUSTOM_CEDICT_FILE = "custom_cedict_ts.u8";
    private static final String DUO_YIN_ZI = "duoyinzi.u8";
    private static final char SKIP_ANNOTATION = '#';
    private static final Map<String, String> YIN_BIAO = new HashMap<>();
    private static final HashMap<String, Word> words = new HashMap<>();

    private ResourceLoad() {
    }

    static {
        YIN_BIAO.put("ā", "a-1");
        YIN_BIAO.put("á", "a-2");
        YIN_BIAO.put("ǎ", "a-3");
        YIN_BIAO.put("à", "a-4");
        YIN_BIAO.put("ō", "o-1");
        YIN_BIAO.put("ó", "o-2");
        YIN_BIAO.put("ǒ", "o-3");
        YIN_BIAO.put("ò", "o-4");
        YIN_BIAO.put("ē", "e-1");
        YIN_BIAO.put("é", "e-2");

        YIN_BIAO.put("ě", "e-3");
        YIN_BIAO.put("è", "e-4");
        YIN_BIAO.put("ń", "n-2");
        YIN_BIAO.put("ň", "n-3");
        YIN_BIAO.put("", "n-4");
        YIN_BIAO.put("ī", "i-1");
        YIN_BIAO.put("í", "i-2");
        YIN_BIAO.put("ǐ", "i-3");
        YIN_BIAO.put("ì", "i-4");
        YIN_BIAO.put("ū", "u-1");

        YIN_BIAO.put("ú", "u-2");
        YIN_BIAO.put("ǔ", "u-3");
        YIN_BIAO.put("ù", "u-4");
        YIN_BIAO.put("ǖ", "ü-1");
        YIN_BIAO.put("ǘ", "ü-2");
        YIN_BIAO.put("ǚ", "ü-3");
        YIN_BIAO.put("ǜ", "ü-4");
        YIN_BIAO.put("", "m-2");
    }

    public static synchronized HashMap<String, Word> loadCedict() {
        if (words.isEmpty()) {
            InputStream cedictStream = Objects.requireNonNull(ResourceLoad.class.getClassLoader().getResourceAsStream(CEDICT_FILE));
            InputStream cedictExtStream = Objects.requireNonNull(ResourceLoad.class.getClassLoader().getResourceAsStream(EXT_CEDICT_FILE));
            InputStream duoyinzi = Objects.requireNonNull(ResourceLoad.class.getClassLoader().getResourceAsStream(DUO_YIN_ZI));
            words.putAll(loadCedict(cedictStream));
            words.putAll(loadCedict(cedictExtStream));
            words.putAll(loadCedict(duoyinzi));
            words.putAll(loadCustomCedict());
            return words;
        }
        return words;
    }

    /**
     * 加载自定义的 cedict 资源
     *
     * @return 词组
     */
    private static HashMap<String, Word> loadCustomCedict() {
        InputStream customCedictStream = ResourceLoad.class.getClassLoader().getResourceAsStream(CUSTOM_CEDICT_FILE);
        if (customCedictStream != null) {
            return loadCedict(customCedictStream);
        }
        return new HashMap<>();
    }

    private static HashMap<String, Word> loadCedict(InputStream inputStream) {

        HashMap<String, Word> keyMap = new HashMap<>();
        try {
            List<String> lines = FileUtil.readAllLines(inputStream);
            for (String line : lines) {
                if (line.charAt(0) == SKIP_ANNOTATION) {
                    continue;
                }
                String[] str = line.split(" /");
                String[] rem = str[0].split("\\[");
                String pinyin = rem[1].replaceAll("[\\[\\]]", "").toLowerCase();
                String pinyinNoTones = pinyin.replaceAll("[\\[\\]:12345]", "").toLowerCase();

                String[] chineseArr = rem[0].split(" ");
                String traditional = chineseArr[0];
                String simplified = chineseArr[1];
                Word word = new Word(simplified, traditional, pinyin, pinyinNoTones);

                if (StringUtil.LETTERS_UPPER.contains(simplified) || StringUtil.LETTERS_LOWER.contains(simplified)) {
                    continue;
                }

                if (traditional.equals(simplified)) {
                    keyMap.put(simplified, word);

                } else {
                    keyMap.put(simplified, word);
                    keyMap.put(traditional, word);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PinyinResourceLoadException();
        }
        return keyMap;
    }

    /**
     * 移除注音
     *
     * @param pinyin 带注音的拼音
     * @return 没注音的拼音
     */
    private static String removeYinBiao(String pinyin) {
        for (Map.Entry<String, String> entry : YIN_BIAO.entrySet()) {
            if (pinyin.contains(entry.getKey())) {
                String[] yinBiaoValues = entry.getValue().split("-");
                return pinyin.replaceAll(entry.getKey(), yinBiaoValues[0]) + yinBiaoValues[1];
            }
        }
        return pinyin;
    }
}
package com.taptap.pinyin.analyzer;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.heaven.support.instance.impl.Instances;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.segment.data.phrase.api.INormalization;
import com.github.houbb.segment.data.phrase.api.INormalizationResult;
import com.github.houbb.segment.data.phrase.api.ISegmentPhraseData;
import com.github.houbb.segment.data.phrase.api.ISegmentWordEntry;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 分词数据抽象父类实现
 * （1）惰性加载
 *
 * @author binbin.hou
 * @since 0.0.1
 */
@ThreadSafe
public abstract class AbstractSegmentPhraseData implements ISegmentPhraseData {


    /**
     * 默认的词数统计
     * @since 0.0.1
     */
    public static final int DEFAULT_WORD_COUNT = 3;

    /**
     * 默认的词性
     * @since 0.0.1
     */
    public static final String DEFAULT_WORD_TYPE = "un";

    /**
     * 静态异变的 NormalizationResult 结果
     * @since 0.0.1
     */
    private static volatile INormalizationResult normalizationResult;

    /**
     * 获取静态词列表
     * @return 词列表
     * @since 0.0.1
     */
    protected abstract List<ISegmentWordEntry> getStaticVolatileWordEntryList();

    /**
     * 获取行内容
     * @return 字典行内容
     * @since 0.0.1
     */
    protected abstract List<String> readDictLines();

    /**
     * 获取对应的词信息
     *
     * ps: 即使列表为空，依然可以分词。
     * 没有 HMM 之前，就相当于与全部为单个字。
     *
     * @return 词信息列表
     * @since 0.0.1
     */
    private List<ISegmentWordEntry> getWordEntryList() {
        List<ISegmentWordEntry> wordEntries = getStaticVolatileWordEntryList();
        if(CollectionUtil.isNotEmpty(wordEntries)) {
            return wordEntries;
        }

        synchronized (AbstractSegmentPhraseData.class) {
            if(CollectionUtil.isEmpty(wordEntries)) {
                // 设置一次
                initWordEntryList(wordEntries);
            }
        }

        return wordEntries;
    }

    @Override
    public Set<String> getPhraseSet() {
        return getFreqMap().keySet();
    }

    @Override
    public Double getFreq(String word) {
        Map<String, Double> freqMap = getFreqMap();

        Double freq = freqMap.get(word);

        // 如果为空，则默认返回最小的频率即可。
        if(ObjectUtil.isNull(freq)) {
            freq = normalizationResult.minFreq();
        }

        return freq;
    }

    /**
     * 获取频率对应的 Map
     * @return 频率 map
     * @since 0.1.0
     */
    private Map<String, Double> getFreqMap() {
        INormalizationResult normalizationResult = normalization();

        return normalizationResult.freqMap();
    }

    @Override
    public double getMinFreq() {
        INormalizationResult normalizationResult = normalization();

        return normalizationResult.minFreq();
    }

    /**
     * 标准化的结果
     * @return 标准化
     * @since 0.0.1
     */
    private INormalizationResult normalization() {
        if(ObjectUtil.isNotNull(normalizationResult)) {
            return normalizationResult;
        }

        synchronized (AbstractSegmentPhraseData.class) {
            if(ObjectUtil.isNull(normalizationResult)) {
                initNormalizationResult();
            }
        }
        return normalizationResult;
    }

    /**
     * 初始化标准化结果
     * @since 0.0.1
     */
    private void initNormalizationResult() {
        final long startTime = System.currentTimeMillis();

        List<ISegmentWordEntry> wordEntries = getWordEntryList();
        INormalization normalization = Instances.singleton(LogNormalization.class);
        normalizationResult = normalization.normalization(wordEntries);

        //清空原始列表
        wordEntries.clear();

        final long costTime = System.currentTimeMillis()-startTime;
        System.out.println("Normalization init finished, cost time : " + costTime + " ms!");
    }

    @Override
    public boolean contains(String word) {
        INormalizationResult normalizationResult = normalization();
        return normalizationResult.freqMap().containsKey(word);
    }



    /**
     * 构建分词列表
     * （1）跳过空白行
     * （2）允许 2-3 列的内容为空，用默认值填充
     * （3）允许整体文件为空
     *
     * @param segmentWordEntryList 单词明细列表
     * @since 0.0.1
     */
    private void initWordEntryList(final List<ISegmentWordEntry> segmentWordEntryList) {
        final long startTime = System.currentTimeMillis();
        final List<String> allLines = this.readDictLines();

        for(String line : allLines) {
            if(StringUtil.isEmptyTrim(line)) {
                // 跳过空白行
                continue;
            }
            ISegmentWordEntry segmentWordEntry = SegmentWordEntry.newInstance().word(line)
                    .count(DEFAULT_WORD_COUNT)
                    .type(DEFAULT_WORD_TYPE);
            segmentWordEntryList.add(segmentWordEntry);
        }

        final long costTime = System.currentTimeMillis()-startTime;
        System.out.println("[Segment] dict init word-list finish, cost time : " + costTime + " ms!");
    }

}

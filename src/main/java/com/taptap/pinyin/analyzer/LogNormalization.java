package com.taptap.pinyin.analyzer;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.segment.data.phrase.api.INormalization;
import com.github.houbb.segment.data.phrase.api.ISegmentWordEntry;

import java.util.List;
import java.util.Map;

/**
 * 标准化接口
 *
 * <pre>
 *  x* = log10(x) / log10(max)
 * </pre>
 * @author binbin.hou
 * @since 0.0.1
 */
@ThreadSafe
public class LogNormalization implements INormalization {

    /**
     * 标准化
     * @return 标准化的结果
     * @since 0.0.1
     */
    @Override
    public NormalizationResult normalization(final List<ISegmentWordEntry> wordEntries) {
        // fail-fast
        NormalizationResult result = new NormalizationResult();
        if(CollectionUtil.isEmpty(wordEntries)) {
            return result;
        }

        // 默认值
        long minCount = Long.MAX_VALUE;
        long maxCount = Long.MIN_VALUE;
        double minFreq = Double.MAX_VALUE;
        double maxFreq = Double.MIN_VALUE;

        // 遍历计算
        for(ISegmentWordEntry segmentWordEntry : wordEntries) {
            long count = segmentWordEntry.count();
            if(minCount > count) {
                minCount = count;
            }
            if(maxCount < count) {
                maxCount = count;
            }
        }

        // 计算频率
        Map<String, Double> freqMap = Guavas.newHashMap(wordEntries.size());
        for(ISegmentWordEntry segmentWordEntry : wordEntries) {
            // 标准化 [0,1]
            double freq = Math.log(segmentWordEntry.count()*1.0)/Math.log(maxCount*1.0);
            if(minFreq > freq) {
                minFreq = freq;
            }
            if(maxFreq < freq) {
                maxFreq = freq;
            }

            freqMap.put(segmentWordEntry.word(), freq);
        }

        return result.minCount(minCount)
                .maxCount(maxCount)
                .minFreq(minFreq)
                .maxFreq(maxFreq)
                .freqMap(freqMap);
    }

}

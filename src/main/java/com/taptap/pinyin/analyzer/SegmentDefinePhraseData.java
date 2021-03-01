package com.taptap.pinyin.analyzer;

import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.segment.data.phrase.api.ISegmentWordEntry;
import com.taptap.pinyin.ResourceLoad;

import java.util.List;

public class SegmentDefinePhraseData extends AbstractSegmentPhraseData {

    /**
     * 默认的内置行
     *
     * @since 0.0.1
     */
    private static volatile List<ISegmentWordEntry> segmentWordEntryList = Guavas.newArrayList();

    @Override
    protected List<ISegmentWordEntry> getStaticVolatileWordEntryList() {
        return segmentWordEntryList;
    }

    @Override
    protected List<String> readDictLines() {
        return CollectionUtil.arrayToList(ResourceLoad.loadCedict().keySet().toArray(new String[]{}));
    }

}
package com.taptap.pinyin.analyzer;

import com.github.houbb.segment.api.ISegment;
import com.github.houbb.segment.api.ISegmentContext;
import com.github.houbb.segment.api.ISegmentResult;
import com.github.houbb.segment.bs.ISegmentBs;
import com.github.houbb.segment.support.format.impl.SegmentFormats;
import com.github.houbb.segment.support.segment.impl.SegmentContext;
import com.github.houbb.segment.support.segment.impl.Segments;
import com.github.houbb.segment.support.segment.mode.impl.SegmentModes;
import com.github.houbb.segment.support.segment.result.ISegmentResultHandler;
import com.github.houbb.segment.support.segment.result.impl.SegmentResultHandlers;
import com.github.houbb.segment.support.tagging.pos.tag.impl.SegmentPosTaggings;

import java.util.List;

/**
 * 分词引导类
 *
 * @author binbin.hou
 * @since 0.0.1
 */
public final class WordAnalyzer implements ISegmentBs {

    /**
     * 分词实现
     * @since 0.0.1
     */
    private final ISegment segment = Segments.defaults();

    private final ISegmentContext context = buildContext();

    /**
     * 引导类
     * @since 0.0.1
     */
    private WordAnalyzer(){}

    /**
     * 创建一个新的实例
     * @return this
     * @since 0.0.1
     */
    public static WordAnalyzer newInstance() {
        return new WordAnalyzer();
    }


    /**
     * 直接执行分词
     * @param string 字符串
     * @return 结果
     * @since 0.0.1
     */
    @Override
    public List<ISegmentResult> segment(final String string) {
        return segment(string, SegmentResultHandlers.common());
    }

    /**
     * 分词处理
     * @param string 原始字符串
     * @param handler 处理类
     * @param <R> 泛型
     * @return 处理后的结果
     * @since 0.0.4
     */
    public <R> R segment(final String string, final ISegmentResultHandler<R> handler) {
        List<ISegmentResult> segmentResults = segment.segment(string, context);
        return handler.handler(segmentResults);
    }

    /**
     * 构建上下文
     * @return 上下文
     * @since 0.0.3
     */
    private static ISegmentContext buildContext() {
        return SegmentContext.newInstance()
                .data(new SegmentDefinePhraseData())
                .mode(SegmentModes.dict())
                .format(SegmentFormats.chineseSimple())
                .posTagging(SegmentPosTaggings.none());
    }
}

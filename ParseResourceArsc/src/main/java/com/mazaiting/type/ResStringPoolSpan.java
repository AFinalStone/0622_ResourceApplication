package com.mazaiting.type;

/**
 * ResStringPool_span在前描述字符串的样式
 * 
 * struct ResStringPool_span
{
    enum {
        END = 0xFFFFFFFF
    };

    // This is the name of the span -- that is, the name of the XML
    // tag that defined it.  The special value END (0xFFFFFFFF) indicates
    // the end of an array of spans.
    ResStringPool_ref name;

    // The range of characters in the string that this span applies to.
    uint32_t firstChar, lastChar;
};
 * 
 * @author mazaiting
 */
public class ResStringPoolSpan {
	/**
	 * 结束标识
	 */
	public final static int END = 0xFFFFFFFF;
	/**
	 * Ref引用
	 */
	public ResStringPoolRef name;
	/**
	 * 第一个字符
	 */
	public int firstChar;
	/**
	 * 最后一个字符
	 */
	public int lastChar;
	
	@Override
	public String toString(){
		return "name:"+name.toString()+",firstChar:"+firstChar+",lastChar:"+lastChar;
	}


}
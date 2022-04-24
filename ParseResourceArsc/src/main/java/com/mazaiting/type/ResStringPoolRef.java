package com.mazaiting.type;

/**
 * ResStringPool_ref在后固定值为0XFFFFFFFF作为占位符
 * struct ResStringPool_ref
{
    // Index into the string pool table (uint32_t-offset from the indices
    // immediately after ResStringPool_header) at which to find the location
    // of the string data in the pool.
    uint32_t index;
};
 * @author mazaiting
 */
public class ResStringPoolRef {
	/**
	 * 标识
	 */
	public int index;
	/**
	 * String Pool Ref所占字节大小
	 * @return
	 */
	public int getSize() {
		return 4;
	}
	
	@Override
	public String toString() {
		return "index: " + index;
	}
}

package com.mazaiting.type;

import com.mazaiting.Util;

/**
 * Resource.arsc文件格式是由一系列的chunk构成，每一个chunk均包含ResChunk_header
 * 
 * struct ResChunk_header
{
    // Type identifier for this chunk.  The meaning of this value depends
    // on the containing chunk.
    uint16_t type;

    // Size of the chunk header (in bytes).  Adding this value to
    // the address of the chunk allows you to find its associated data
    // (if any).
    uint16_t headerSize;

    // Total size of this chunk (in bytes).  This is the chunkSize plus
    // the size of any data associated with the chunk.  Adding this value
    // to the chunk allows you to completely skip its contents (including
    // any child chunks).  If this value is the same as chunkSize, there is
    // no data associated with the chunk.
    uint32_t size;
};
 * 
 * @author mazaiting
 */
public class ResChunkHeader {
	/**
	 * 当前这个chunk的类型
	 */
	public short type;
	/**
	 * 当前chunk的头部大小
	 */
	public short headerSize;
	/**
	 * 当前chunk的大小
	 */
	public int size;
	
	/**
	 * 获取Chunk Header所占字节数
	 * @return
	 */
	public int getHeaderSize() {
		return 2 + 2 + 4;
	}
	
	@Override
	public String toString(){
		return "type: " + Util.bytesToHexString(Util.int2Byte(type)) + ",headerSize: " + headerSize + ",size: " + size;
	}
	
}

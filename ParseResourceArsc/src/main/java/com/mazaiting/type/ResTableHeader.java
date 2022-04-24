package com.mazaiting.type;

/**
 * Resources.arsc文件的第一个结构是资源索引表头部
 * 描述Resources.arsc文件的大小和资源包数量
 * 
 * struct ResTable_header
{
    struct ResChunk_header header;

    // The number of ResTable_package structures.
    uint32_t packageCount;
};
 * 
 * @author mazaiting
 */
public class ResTableHeader {
	/**
	 * 标准的Chunk头部信息格式
	 */
	public ResChunkHeader header;
	/**
	 * 被编译的资源包个数
	 * Android 中一个apk可能包含多个资源包，默认情况下都只有一个就是应用的包名所在的资源包
	 */
	public int packageCount;
	
	public ResTableHeader() {
		header = new ResChunkHeader();
	}
	
	/**
	 * 获取当前Table Header所占字节数
	 * @return
	 */
	public int getHeaderSize() {
		return header.getHeaderSize() + 4;
	}
	
	@Override
	public String toString(){
		return "header:" + header.toString() + "\n" + "packageCount:"+packageCount;
	}
}

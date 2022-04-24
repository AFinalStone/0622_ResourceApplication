package com.mazaiting.type;

/**
 * 类型资源项数据块用来描述资源项的具体信息，可以知道每一个资源项的名称、值和配置等信息。
 * 类型资源项数据同样是按照类型和配置来组织的，即一个具有n个配置的类型一共对应有n个类型
 * 资源项数据块。
 * 
 * struct ResTable_type
{
    struct ResChunk_header header;

    enum {
        NO_ENTRY = 0xFFFFFFFF
    };
    
    // The type identifier this chunk is holding.  Type IDs start
    // at 1 (corresponding to the value of the type bits in a
    // resource identifier).  0 is invalid.
    uint8_t id;
    
    // Must be 0.
    uint8_t res0;
    // Must be 0.
    uint16_t res1;
    
    // Number of uint32_t entry indices that follow.
    uint32_t entryCount;

    // Offset from header where ResTable_entry data starts.
    uint32_t entriesStart;
    
    // Configuration this collection of entries is designed for.
    ResTable_config config;
};
 * 
 * @author mazaiting
 */
public class ResTableType {
	/**
	 * NO_ENTRY常量
	 */
	public final static int NO_ENTRY = 0xFFFFFFFF;
	/**
	 * Chunk的头部信息结构
	 */
	public ResChunkHeader header;
	/**
	 * 标识资源的Type ID
	 */
	public byte id;
	/**
	 * 保留，始终为0
	 */
	public byte res0;
	/**
	 * 保留，始终为0
	 */
	public short res1;
	/**
	 * 本类型资源项个数，指名称相同的资源项的个数
	 */
	public int entryCount;
	/**
	 * 资源项数组块相对头部的偏移值
	 */
	public int entriesStart;
	/**
	 * 指向一个ResTable_config，用来描述配置信息，地区，语言，分辨率等
	 */
	public ResTableConfig resConfig;
	
	public ResTableType() {
		header = new ResChunkHeader();
		resConfig = new ResTableConfig();
	}
	
	/**
	 * 获取当前资源类型所占的字节数
	 * @return
	 */
	public int getSize() {
		return header.getHeaderSize() + 1 + 1 + 2 + 4 + 4;
	}
	
	@Override
	public String toString(){
		return "header: " + header.toString() + ",id: " + id + ",res0: " + res0 + ",res1: " + res1 + 
				",entryCount: " + entryCount + ",entriesStart: " + entriesStart;
	}
 
}
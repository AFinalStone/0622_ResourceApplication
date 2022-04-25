package com.mazaiting.type;

/**
 * 类型规范数据块用来描述资源项的配置差异性。通过这个差异性，我们可以知道每个资源项的配置状况。
 * 知道了一个资源项的配置状况之后，Android资源管理框架在检测到设备的配置信息发生变化之后，就
 * 可以知道是否需要重新加载该资源项。类型规范数据块是按照类型来组织的，即每一种类型都对应有一个
 * 类型规范数据块。
 * 
 * struct ResTable_typeSpec
{
    struct ResChunk_header header;

    // The type identifier this chunk is holding.  Type IDs start
    // at 1 (corresponding to the value of the type bits in a
    // resource identifier).  0 is invalid.
    uint8_t id;
    
    // Must be 0.
    uint8_t res0;
    // Must be 0.
    uint16_t res1;
    
    // Number of uint32_t entry configuration masks that follow.
    uint32_t entryCount;

    enum {
        // Additional flag indicating an entry is public.
        SPEC_PUBLIC = 0x40000000
    };
};
 * 
 * @author mazaiting
 */
public class ResTableTypeSpec {
	/**
	 * SPEC公共常量
	 */
	public final static int SPEC_PUBLIC = 0x40000000;
	/**
	 * Chunk的头部信息结构
	 */
	public Res00ChunkHeader header;
	/**
	 * 标识资源的Type ID,Type ID是指资源的类型ID。资源的类型有animator、anim、color、drawable、layout、menu、raw、string和xml等等若干种，每一种都会被赋予一个ID。
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
	 * 本类型资源项个数，即名称相同的资源项的个数
	 */
	public int entryCount;
	
	public ResTableTypeSpec() {
		header = new Res00ChunkHeader();
	}
	
	@Override
	public String toString(){
		return "header: " + header.toString() + ",id: " + id + ",res0: " + res0 + 
				",res1: " + res1 + ",entryCount: " + entryCount;
	}

}
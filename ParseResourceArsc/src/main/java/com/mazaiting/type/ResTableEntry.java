package com.mazaiting.type;

import com.mazaiting.ParseResourceUtil;

/**
 * 用来描述一个资源项的具体信息
 * struct ResTable_entry
{
    // Number of bytes in this structure.
    uint16_t size;

    enum {
        // If set, this is a complex entry, holding a set of name/value
        // mappings.  It is followed by an array of ResTable_map structures.
        FLAG_COMPLEX = 0x0001,
        // If set, this resource has been declared public, so libraries
        // are allowed to reference it.
        FLAG_PUBLIC = 0x0002
    };
    uint16_t flags;
    
    // Reference into ResTable_package::keyStrings identifying this entry.
    struct ResStringPool_ref key;
};
 * @author mazaiting
 *
 */
public class ResTableEntry {
	/**
	 * 复杂标识
	 */
	public final static int FLAG_COMPLEX = 0x0001;
	/**
	 * 公共标识
	 */
	public final static int FLAG_PUBLIC = 0x0002;
	/**
	 * 大小
	 */
	public short size;
	/**
	 * 标志位
	 */
	public short flags;
	
	public ResStringPoolRef key;
	
	public ResTableEntry() {
		key = new ResStringPoolRef();
	}
	
	/**
	 * 当前实体所占字节数
	 * @return
	 */
	public int getSize() {
		return 2 + 2 + key.getSize();
	}
	
	@Override
	public String toString(){
		return "size: " + size + ",flags: " + flags + ",key: " + key.toString() + 
				",str: " + ParseResourceUtil.getKeyString(key.index);
	}

}
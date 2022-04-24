package com.mazaiting.type;

/**
 * Package数据块记录编译包的元数据
 * 
 * struct ResTable_package
{
    struct ResChunk_header header;

    // If this is a base package, its ID.  Package IDs start
    // at 1 (corresponding to the value of the package bits in a
    // resource identifier).  0 means this is not a base package.
    uint32_t id;

    // Actual name of this package, \0-terminated.
    uint16_t name[128];

    // Offset to a ResStringPool_header defining the resource
    // type symbol table.  If zero, this package is inheriting from
    // another base package (overriding specific values in it).
    uint32_t typeStrings;

    // Last index into typeStrings that is for public use by others.
    uint32_t lastPublicType;

    // Offset to a ResStringPool_header defining the resource
    // key symbol table.  If zero, this package is inheriting from
    // another base package (overriding specific values in it).
    uint32_t keyStrings;

    // Last index into keyStrings that is for public use by others.
    uint32_t lastPublicKey;

    uint32_t typeIdOffset;
};
 * 
 * Package数据块的整体结构
 * String Pool
 * Type String Pool
 * Key String Pool
 * Type Specification
 * Type Info
 * 
 * @author mazaiting
 */
public class ResTablePackage {
	/**
	 * Chunk的头部信息数据结构
	 */
	public ResChunkHeader header;
	/**
	 * 包的ID，等于package id,一般用户包的值Package Id为0X7F，系统资源包Pacage Id为0X01
	 * 这个值会在构建public.xml中的id值时用到
	 */
	public int id;
	/**
	 * 包名
	 */
	public char[] name = new char[128];
	/**
	 * 类型字符串资源池相对头部的偏移
	 */
	public int typeStrings;
	/**
	 * 最后一个到处的public类型字符串在类型字符串资源池中的索引，目前这个值设置为类型字符串资源池的元素格尔书
	 */
	public int lastPublicType;
	/**
	 * 资源项名称字符串相对头部的偏移
	 */
	public int keyStrings;
	/**
	 * 最后一个导出的Public资源项名称字符串在资源项名称字符串资源池中的索引，目前这个值设置为资源项名称字符串资源池的元素个数
	 */
	public int lastPublicKey;
	
	public ResTablePackage() {
		header = new ResChunkHeader();
	}
	
	@Override
	public String toString(){
		return "header: " + header.toString() + "\n" + ",id= " + id + ",name: " + name.toString() + 
				",typeStrings:" + typeStrings + ",lastPublicType: " + lastPublicType + ",keyStrings: " + keyStrings 
				+ ",lastPublicKey: " + lastPublicKey;
	}
}
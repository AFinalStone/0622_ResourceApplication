package com.wjdiankong.parseresource.type;

public class ResStringPoolSpan {
	
	public final static int END = 0xFFFFFFFF;
	
	public ResStringPoolRef name;
	public int firstChar;
	public int lastChar;
	
	@Override
	public String toString(){
		return "name:"+name.toString()+",firstChar:"+firstChar+",lastChar:"+lastChar;
	}

}

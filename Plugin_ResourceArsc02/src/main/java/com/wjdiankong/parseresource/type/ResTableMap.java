package com.wjdiankong.parseresource.type;

public class ResTableMap {
	
	public ResTableRef name;
	public ResValue value;
	
	public ResTableMap(){
		name = new ResTableRef();
		value = new ResValue();
	}
	
	public int getSize(){
		return name.getSize() + value.getSize();
	}
	
	@Override
	public String toString(){
		return name.toString()+",value:"+value.toString();
	}

}

package com.wjdiankong.parseresource.type;

public class ResTableConfig {
	
	//uiMode
	public final static int MASK_UI_MODE_TYPE = 0;
	public final static int UI_MODE_TYPE_ANY = 0x00;
	public final static int UI_MODE_TYPE_NORMAL =  0x01;
	public final static int UI_MODE_TYPE_DESK = 0x02;
	public final static int UI_MODE_TYPE_CAR = 0x03;
	public final static int UI_MODE_TYPE_TELEVISION = 0x04;
	public final static int UI_MODE_TYPE_APPLIANCE = 0x05;
	public final static int UI_MODE_TYPE_WATCH = 0x06;
	public final static int MASK_UI_MODE_NIGHT = 0;
	public final static int SHIFT_UI_MODE_NIGHT = 0;
	public final static int UI_MODE_NIGHT_ANY = 0x00;
	public final static int UI_MODE_NIGHT_NO = 0x01;
	public final static int UI_MODE_NIGHT_YES = 0x02;
	
	//screenLayout
	public final static int MASK_SCREENSIZE = 0;
	public final static int SCREENSIZE_ANY = 0x00;
	public final static int SCREENSIZE_SMALL = 0x01;
	public final static int SCREENSIZE_NORMAL = 0x02;
	public final static int SCREENSIZE_LARGE = 0x03;
	public final static int SCREENSIZE_XLARGE = 0x04;
	public final static int MASK_SCREENLONG = 0;
	public final static int SHIFT_SCREENLONG = 0;
	public final static int SCREENLONG_ANY = 0x00;
	public final static int SCREENLONG_NO = 0x01;
	public final static int SCREENLONG_YES = 0x02;
	public final static int MASK_LAYOUTDIR = 0;
	public final static int SHIFT_LAYOUTDIR = 0;
	public final static int LAYOUTDIR_ANY = 0x00;
	public final static int LAYOUTDIR_LTR =  0x01;
	public final static int LAYOUTDIR_RTL = 0x02;
	
	/**
	 * uint32_t size;
	 */
	public int size;

    
    public short mcc;
    public short mnc;
    
    public int imsi;
    

    public byte[] language = new byte[2];
    public byte[] country = new byte[2];
    
    public int locale;

    public byte orientation;
    public byte touchscreen;
    public short density;
    
    public int screenType;
    

    public byte keyboard;
    public byte navigation;
    public byte inputFlags;
    public byte inputPad0;
    
    public int input;

    public short screenWidth;
    public short screenHeight;
    
    public int screenSize;

    public short sdVersion;
    public short minorVersion;
    
    public int version;

    public byte screenLayout;
    public byte uiMode;
    public short smallestScreenWidthDp;
    
    public int screenConfig;
    

    public short screenWidthDp;
    public short screenHeightDp;
    
    public int screenSizeDp;
    
    /**
     *  char localeScript[4];
    	char localeVariant[8];
     */
    public byte[] localeScript = new byte[4];
    public byte[] localeVariant = new byte[8];
    
    public int getSize(){
    	return 48;
    }
    
    @Override
    public String toString(){
    	return "size:"+size+",mcc=" + mcc+",locale:"+locale+",screenType:"+screenType+",input:"+
    			input+",screenSize:"+screenSize+",version:"+version+",sdkVersion:"+sdVersion+",minVersion:"+minorVersion
    			+",screenConfig:"+screenConfig+ ",screenLayout:"+screenLayout + ",uiMode:"+uiMode + 
    			",smallestScreenWidthDp:"+smallestScreenWidthDp + ",screenSizeDp:"+screenSizeDp;
    }
    

}

package sensors;

import edu.wpi.first.wpilibj.I2C;

public class LED {

	/* **********************************************************
	 *      Constants for LCD Panel
	 * ********************************************************/
	// LCD Commands
	public static final int LCD_CLEARDISPLAY = 0x01;
	public static final int LCD_RETURNHOME = 0x02;
	public static final int LCD_ENTRYMODESET = 0x04;
	public static final int LCD_DISPLAYCONTROL = 0x08;
	public static final int LCD_CURSORSHIFT = 0x10;
	public static final int LCD_FUNCTIONSET = 0x20;
	public static final int LCD_SETCGRAMADDR = 0x40;
	public static final int LCD_SETDDRAMADDR = 0x80;

	// Flags for display on/off control
	public static final int LCD_DISPLAYON = 0x04;
	public static final int LCD_DISPLAYOFF = 0x00;
	public static final int LCD_CURSORON = 0x02;
	public static final int LCD_CURSOROFF = 0x00;
	public static final int LCD_BLINKON = 0x01;
	public static final int LCD_BLINKOFF = 0x00;

	// Flags for display entry mode
	// public static final int LCD_ENTRYRIGHT = 0x00;
	public static final int LCD_ENTRYLEFT = 0x02;
	public static final int LCD_ENTRYSHIFTINCREMENT = 0x01;
	public static final int LCD_ENTRYSHIFTDECREMENT = 0x00;

	// Flags for display/cursor shift
	public static final int LCD_DISPLAYMOVE = 0x08;
	public static final int LCD_CURSORMOVE = 0x00;
	public static final int LCD_MOVERIGHT = 0x04;
	public static final int LCD_MOVELEFT = 0x00;

	// flags for function set
	public static final int LCD_8BITMODE = 0x10;
	public static final int LCD_4BITMODE = 0x00;
	public static final int LCD_2LINE = 0x08;	//for 2 or 4 lines actually
	public static final int LCD_1LINE = 0x00;
	public static final int LCD_5x10DOTS = 0x04;	//seldom used!!
	public static final int LCD_5x8DOTS = 0x00;	

	// flags for backlight control
	public static final int LCD_BACKLIGHT = 0x08;
	public static final int LCD_NOBACKLIGHT = 0x00;

	//bitmasks for register control
	public static final int En = 0b00000100; // Enable bit
	public static final int Rw = 0b00000010; // Read/Write bit
	public static final int Rs = 0b00000001; // Register select bit
	
	I2C lcdDisplay;
	
	public LED() {
		initLCD();
	}
	
	void initLCD() {
		lcdDisplay = new I2C(I2C.Port.kOnboard, 0x27);

		LCDwriteCMD(0x03);
		LCDwriteCMD(0x03); 
		LCDwriteCMD(0x03); 
		LCDwriteCMD(0x02);
		//4 bit mode??? -- yes. Always. It's the default way of doing this for LCD displays
		LCDwriteCMD( LCD_FUNCTIONSET | LCD_2LINE | LCD_5x8DOTS | LCD_4BITMODE );
		LCDwriteCMD( LCD_DISPLAYCONTROL | LCD_DISPLAYON );    
		LCDwriteCMD( LCD_CLEARDISPLAY );
		LCDwriteCMD( LCD_ENTRYMODESET | LCD_ENTRYLEFT );
		zsleep(10);
	}

	//write a sleep method to get rid of the try-catch stuff
	public void zsleep(int t) {
		try { Thread.sleep(t);
		} catch(InterruptedException e) {}
	}

	//This is for writing commands, 4 bits at a time 
	public void LCDwriteCMD (int data) {
		LCD_rawWrite(data & 0xF0);
		LCD_rawWrite((data <<4 ) & 0xF0);
	}

	//This is for writing a character, 4 bits at a time
	public void LCDwriteChar ( int data) {
		LCD_rawWrite( Rs |  (data & 0xF0));
		LCD_rawWrite( Rs | ((data <<4 ) & 0xF0));
	}

	public void LCD_rawWrite( int data) {
		lcdDisplay.write(0, data | LCD_BACKLIGHT );
		strobe(data);
	}

	public void strobe(int data){
		//    	Syntax: lcdDisplay.write(reg,data);    	
		lcdDisplay.write(0, data | En | LCD_BACKLIGHT );
		zsleep(1);
		lcdDisplay.write(0, (data & ~En) | LCD_BACKLIGHT );
		zsleep(1);
	}

	//This is the "public" method. The one that is actually used by other code to write to the display.
	public void LCDwriteString(String s, int line) {
		switch (line) {
		case 1: LCDwriteCMD(0x80); break;
		case 2: LCDwriteCMD(0xC0); break;
		case 3: LCDwriteCMD(0x94); break;
		case 4: LCDwriteCMD(0xD4); break;
		default: return;	//invalid line number does nothing.
		}

		//limit to 20 chars/line so we don't have to worry about overflow messing up the display
		if (s.length() > 20) s = s.substring(0, 20); 

		for (int i=0; i<s.length(); i++){
			LCDwriteChar(s.charAt(i));
		}
	}
}

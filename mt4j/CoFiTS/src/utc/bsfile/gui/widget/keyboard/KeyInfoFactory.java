package utc.bsfile.gui.widget.keyboard;

import java.util.ArrayList;

import org.mt4j.util.math.Vector3D;

public class KeyInfoFactory {
	private static KeyInfoFactory instance;
	
	public static KeyInfoFactory getInstance() {
		if (instance == null)
			instance = new KeyInfoFactory();
		return instance;
	}
	/**
	 * The Class KeyInfo.
	 * @author  C.Ruff
	 */
	public class KeyInfo {

		/** The keyfont unicode. */
		public String keyfontUnicode;

		/** The char unicode to write. */
		public String charUnicodeToWrite;

		/** The char unicode to write shifted. */
		public String charUnicodeToWriteShifted;

		/**
		 * The position.
		 * @uml.property  name="position"
		 * @uml.associationEnd  
		 */
		public Vector3D position;

		/** The visibility info. */
		public int visibilityInfo;

		/** The Constant NORMAL_KEY. */
		public static final int NORMAL_KEY = 0;

		/** The Constant KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED. */
		public static final int KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED = 1;

		/** The Constant KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED. */
		public static final int KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED = 2;

		/**
		 * Instantiates a new key info.
		 * 
		 * @param keyfontUnicode
		 *            the keyfont unicode
		 * @param charUnicodeToWrite
		 *            the char unicode to write
		 * @param charUnicodeToWriteShifted
		 *            the char unicode to write shifted
		 * @param position
		 *            the position
		 * @param visibilityInfo
		 *            the visibility info
		 */
		public KeyInfo(String keyfontUnicode, String charUnicodeToWrite,
				String charUnicodeToWriteShifted, Vector3D position,
				int visibilityInfo) {
			super();
			this.keyfontUnicode = keyfontUnicode;
			this.charUnicodeToWrite = charUnicodeToWrite;
			this.charUnicodeToWriteShifted = charUnicodeToWriteShifted;
			this.position = position;
			this.visibilityInfo = visibilityInfo;
		}

	}

	public KeyInfo[] getGermanLayout() {
		ArrayList<KeyInfo> keyInfos = new ArrayList<KeyInfo>();

		float lineY = 35;
		// float advanceMent =
		// keyFont.getFontCharacterByUnicode("A").getHorizontalDist()-10;
		float advanceMent = 42;
		float startX = 60;
		// keyInfos.add(new KeyInfo("^", "^", "^", new Vector3D(startX,lineY),
		// KeyInfo.NORMAL_KEY)); //ESC key

		keyInfos.add(new KeyInfo("1", "1", "1", 
				     new Vector3D(startX + 1	* advanceMent, lineY),
				             KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("!", "!", "!", new Vector3D(startX + 1
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("2", "2", "2", new Vector3D(startX + 2
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("\"", "\"", "\"", new Vector3D(startX + 2
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("3", "3", "3", new Vector3D(startX + 3
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));

		keyInfos.add(new KeyInfo("4", "4", "4", new Vector3D(startX + 4
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("�", "$", "$", new Vector3D(startX + 4
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("5", "5", "5", new Vector3D(startX + 5
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("%", "%", "%", new Vector3D(startX + 5
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("6", "6", "6", new Vector3D(startX + 6
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("&", "&", "&", new Vector3D(startX + 6
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("7", "7", "7", new Vector3D(startX + 7
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("/", "/", "/", new Vector3D(startX + 7
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("8", "8", "8", new Vector3D(startX + 8
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("(", "(", "(", new Vector3D(startX + 8
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("9", "9", "9", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo(")", ")", ")", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("0", "0", "0", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("=", "=", "=", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("\\", "\\", "\\", new Vector3D(startX + 11
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("?", "?", "?", new Vector3D(startX + 11
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		// ////////////////
		lineY = 77;
		startX = 80; // | + 27

		keyInfos.add(new KeyInfo("Q", "q", "Q", new Vector3D(startX + 1
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("W", "w", "W", new Vector3D(startX + 2
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("E", "e", "E", new Vector3D(startX + 3
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("R", "r", "R", new Vector3D(startX + 4
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("T", "t", "T", new Vector3D(startX + 5
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("Z", "z", "Z", new Vector3D(startX + 6
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("U", "u", "U", new Vector3D(startX + 7
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("I", "i", "I", new Vector3D(startX + 8
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("O", "o", "O", new Vector3D(startX + 9
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("P", "p", "P", new Vector3D(startX + 10
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		// �
		keyInfos.add(new KeyInfo("111", "�", "�", new Vector3D(startX + 11
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));

		keyInfos.add(new KeyInfo("+", "+", "+", new Vector3D(startX + 12
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("*", "*", "*", new Vector3D(startX + 12
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		lineY = 119;
		startX = 136; // +58
		keyInfos.add(new KeyInfo("A", "a", "A", new Vector3D(startX, lineY),
				KeyInfo.NORMAL_KEY)); //
		keyInfos.add(new KeyInfo("S", "s", "S", new Vector3D(startX + 1
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("D", "d", "D", new Vector3D(startX + 2
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("F", "f", "F", new Vector3D(startX + 3
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("G", "g", "G", new Vector3D(startX + 4
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("H", "h", "H", new Vector3D(startX + 5
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("J", "j", "J", new Vector3D(startX + 6
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("K", "k", "K", new Vector3D(startX + 7
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("L", "l", "L", new Vector3D(startX + 8
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		// �
		keyInfos.add(new KeyInfo("1111", "�", "�", new Vector3D(startX + 9
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		// �
		keyInfos.add(new KeyInfo("11", "�", "�", new Vector3D(startX + 10
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));

		// //////////////////
		lineY = 161;
		startX = 70; // -60
		keyInfos.add(new KeyInfo("<", "<", "<", new Vector3D(startX + 1
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo(">", ">", ">", new Vector3D(startX + 1
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("Y", "y", "Y", new Vector3D(startX + 2
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("X", "x", "X", new Vector3D(startX + 3
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("C", "c", "C", new Vector3D(startX + 4
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("V", "v", "V", new Vector3D(startX + 5
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("B", "b", "B", new Vector3D(startX + 6
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("N", "n", "N", new Vector3D(startX + 7
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("M", "m", "M", new Vector3D(startX + 8
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));

		keyInfos.add(new KeyInfo(",", ",", ",", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo(";", ";", ";", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo(".", ".", ".", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo(":", ":", ":", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("-", "-", "-", new Vector3D(startX + 11
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));

		keyInfos.add(new KeyInfo("#", "#", "#", new Vector3D(startX + 12
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("'", "'", "'", new Vector3D(startX + 12
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		// /////////
		// Special keys
		keyInfos.add(new KeyInfo("z", "back", "back", new Vector3D(580, 35),
				KeyInfo.NORMAL_KEY));// Backspace
		keyInfos.add(new KeyInfo("v", "\t", "\t", new Vector3D(62, 77),
				KeyInfo.NORMAL_KEY)); // Tab
		keyInfos.add(new KeyInfo("j", "shift", "shift", new Vector3D(78, 120),
				KeyInfo.NORMAL_KEY)); // Shift
		keyInfos.add(new KeyInfo("f", "\n", "\n", new Vector3D(615, 105),
				KeyInfo.NORMAL_KEY)); // Enter

		return keyInfos.toArray(new KeyInfo[keyInfos.size()]);
	}

	public KeyInfo[] getFrenchLayout(float width, float height) {
		ArrayList<KeyInfo> keyInfos = new ArrayList<KeyInfo>();

		float lineY = 35 * (height/245);
		// float advanceMent =
		// keyFont.getFontCharacterByUnicode("A").getHorizontalDist()-10;
		float advanceMent = 42 * (width/700);
		float startX = 60 * (width/700);
		// keyInfos.add(new KeyInfo("^", "^", "^", new Vector3D(startX,lineY),
		// KeyInfo.NORMAL_KEY)); //ESC key

		keyInfos.add(new KeyInfo("1", "1", "1", new Vector3D(startX + 1
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("!", "!", "!", new Vector3D(startX + 1
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("2", "2", "2", new Vector3D(startX + 2
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("\"", "\"", "\"", new Vector3D(startX + 2
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("3", "3", "3", new Vector3D(startX + 3
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("#", "#", "#", new Vector3D(startX + 3
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("4", "4", "4", new Vector3D(startX + 4
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("$", "$", "$", new Vector3D(startX + 4
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("5", "5", "5", new Vector3D(startX + 5
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("%", "%", "%", new Vector3D(startX + 5
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("6", "6", "6", new Vector3D(startX + 6
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("&", "&", "&", new Vector3D(startX + 6
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("7", "7", "7", new Vector3D(startX + 7
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("/", "/", "/", new Vector3D(startX + 7
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("8", "8", "8", new Vector3D(startX + 8
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("(", "(", "(", new Vector3D(startX + 8
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("9", "9", "9", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo(")", ")", ")", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("0", "0", "0", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("=", "=", "=", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("\\", "\\", "\\", new Vector3D(startX + 11
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("?", "?", "?", new Vector3D(startX + 11
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		// ////////////////
		lineY = 77 * (height/245);
		startX = 80  * (width/700); // | + 27

		keyInfos.add(new KeyInfo("A", "a", "A", new Vector3D(startX + 1
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("Z", "z", "Z", new Vector3D(startX + 2
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("E", "e", "E", new Vector3D(startX + 3
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("R", "r", "R", new Vector3D(startX + 4
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("T", "t", "T", new Vector3D(startX + 5
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("Y", "y", "Y", new Vector3D(startX + 6
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("U", "u", "U", new Vector3D(startX + 7
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("I", "i", "I", new Vector3D(startX + 8
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("O", "o", "O", new Vector3D(startX + 9
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("P", "p", "P", new Vector3D(startX + 10
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		// �
		keyInfos.add(new KeyInfo("-", "-", "-", new Vector3D(startX + 11
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));

		keyInfos.add(new KeyInfo("+", "+", "+", new Vector3D(startX + 12
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("*", "*", "*", new Vector3D(startX + 12
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		lineY = 119  * (height/245);;
		startX = 136  * (width/700); // +58
		keyInfos.add(new KeyInfo("Q", "q", "Q", new Vector3D(startX, lineY),
				KeyInfo.NORMAL_KEY)); //
		keyInfos.add(new KeyInfo("S", "s", "S", new Vector3D(startX + 1
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("D", "d", "D", new Vector3D(startX + 2
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("F", "f", "F", new Vector3D(startX + 3
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("G", "g", "G", new Vector3D(startX + 4
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("H", "h", "H", new Vector3D(startX + 5
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("J", "j", "J", new Vector3D(startX + 6
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("K", "k", "K", new Vector3D(startX + 7
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("L", "l", "L", new Vector3D(startX + 8
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("M", "m", "M", new Vector3D(startX + 9
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		// $
		keyInfos.add(new KeyInfo("$", "$", "$", new Vector3D(startX + 10
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		// keyInfos.add(new KeyInfo("$", "$", "$", new
		// Vector3D(startX+10*advanceMent,lineY),
		// KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		// keyInfos.add(new KeyInfo("\u20AC", "�", "\u20AC", new
		// Vector3D(startX+10*advanceMent,lineY),
		// KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		// //////////////////
		lineY = 161 * (height/245);
		startX = 70  * (width/700); // -60
		keyInfos.add(new KeyInfo("<", "<", "<", new Vector3D(startX + 1
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo(">", ">", ">", new Vector3D(startX + 1
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("W", "w", "W", new Vector3D(startX + 2
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("X", "x", "X", new Vector3D(startX + 3
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("C", "c", "C", new Vector3D(startX + 4
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("V", "v", "V", new Vector3D(startX + 5
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("B", "b", "B", new Vector3D(startX + 6
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("N", "n", "N", new Vector3D(startX + 7
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		// keyInfos.add(new KeyInfo("M", "m", "M", new
		// Vector3D(startX+8*advanceMent,lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo(",", ",", ",", new Vector3D(startX + 8
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("?", "?", "?", new Vector3D(startX + 8
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		// keyInfos.add(new KeyInfo(",", ",", ",", new
		// Vector3D(startX+9*advanceMent,lineY),KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		// keyInfos.add(new KeyInfo(";", ";", ";", new
		// Vector3D(startX+9*advanceMent,lineY),KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));
		keyInfos.add(new KeyInfo(";", ";", ";", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo(".", ".", ".", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		// keyInfos.add(new KeyInfo(".", ".", ".", new
		// Vector3D(startX+10*advanceMent,lineY),
		// KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		// keyInfos.add(new KeyInfo(":", ":", ":", new
		// Vector3D(startX+10*advanceMent,lineY),
		// KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));
		keyInfos.add(new KeyInfo(":", ":", ":", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("/", "/", "/", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		// keyInfos.add(new KeyInfo("-", "-", "-", new
		// Vector3D(startX+11*advanceMent,lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("!", "!", "!", new Vector3D(startX + 11
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		// keyInfos.add(new KeyInfo("[", "[", "[", new
		// Vector3D(startX+11*advanceMent,lineY),
		// KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("'", "'", "'", new Vector3D(startX + 12
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		// keyInfos.add(new KeyInfo("]", "]", "]", new
		// Vector3D(startX+12*advanceMent,lineY),
		// KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		// /////////
		// Special keys
		keyInfos.add(new KeyInfo("z", "back", "back", new Vector3D(580 * (width/700), 35 * (height/245)),
				KeyInfo.NORMAL_KEY));// Backspace
		keyInfos.add(new KeyInfo("v", "\t", "\t", new Vector3D(62 * (width/700), 77* (height/245)),
				KeyInfo.NORMAL_KEY)); // Tab
		keyInfos.add(new KeyInfo("j", "shift", "shift", new Vector3D(78 * (width/700), 120* (height/245)),
				KeyInfo.NORMAL_KEY)); // Shift
		keyInfos.add(new KeyInfo("f", "\n", "\n", new Vector3D(615 * (width/700), 105 * (height/245)),
				KeyInfo.NORMAL_KEY)); // Enter

		return keyInfos.toArray(new KeyInfo[keyInfos.size()]);
	}

	// FIXME no "@" available in key font?

	public KeyInfo[] getUSLayout() {
		ArrayList<KeyInfo> keyInfos = new ArrayList<KeyInfo>();

		float lineY = 35;
		// float advanceMent =
		// keyFont.getFontCharacterByUnicode("A").getHorizontalDist()-10;
		float advanceMent = 42;
		float startX = 60;
		// keyInfos.add(new KeyInfo("^", "^", "^", new Vector3D(startX,lineY),
		// KeyInfo.NORMAL_KEY)); //ESC key

		keyInfos.add(new KeyInfo("1", "1", "1", new Vector3D(startX + 1
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("!", "!", "!", new Vector3D(startX + 1
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("2", "2", "2", new Vector3D(startX + 2
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		// FIXME should be "@" here

		keyInfos.add(new KeyInfo("3", "3", "3", new Vector3D(startX + 3
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("#", "#", "#", new Vector3D(startX + 3
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("4", "4", "4", new Vector3D(startX + 4
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("$", "$", "$", new Vector3D(startX + 4
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("5", "5", "5", new Vector3D(startX + 5
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("%", "%", "%", new Vector3D(startX + 5
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("6", "6", "6", new Vector3D(startX + 6
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		// FIXME "^" missing

		keyInfos.add(new KeyInfo("7", "7", "7", new Vector3D(startX + 7
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("&", "&", "&", new Vector3D(startX + 7
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("8", "8", "8", new Vector3D(startX + 8
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("*", "*", "*", new Vector3D(startX + 8
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("9", "9", "9", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("(", "(", "(", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("0", "0", "0", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo(")", ")", ")", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("=", "=", "=", new Vector3D(startX + 11
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("+", "+", "+", new Vector3D(startX + 11
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		// ////////////////
		lineY = 77;
		startX = 80; // | + 27

		keyInfos.add(new KeyInfo("Q", "q", "Q", new Vector3D(startX + 1
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("W", "w", "W", new Vector3D(startX + 2
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("E", "e", "E", new Vector3D(startX + 3
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("R", "r", "R", new Vector3D(startX + 4
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("T", "t", "T", new Vector3D(startX + 5
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("Y", "y", "Y", new Vector3D(startX + 6
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));

		keyInfos.add(new KeyInfo("U", "u", "U", new Vector3D(startX + 7
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("I", "i", "I", new Vector3D(startX + 8
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("O", "o", "O", new Vector3D(startX + 9
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("P", "p", "P", new Vector3D(startX + 10
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		// �
		// keyInfos.add(new KeyInfo("111", "�", "�", new
		// Vector3D(startX+11*advanceMent,lineY), KeyInfo.NORMAL_KEY));

		// FIXME woanders hin
		keyInfos.add(new KeyInfo("\\", "\\", "\\", new Vector3D(startX + 11
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));

		keyInfos.add(new KeyInfo("-", "-", "-", new Vector3D(startX + 12
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));

		lineY = 119;
		startX = 136; // +58
		keyInfos.add(new KeyInfo("A", "a", "A", new Vector3D(startX, lineY),
				KeyInfo.NORMAL_KEY)); //
		keyInfos.add(new KeyInfo("S", "s", "S", new Vector3D(startX + 1
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("D", "d", "D", new Vector3D(startX + 2
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("F", "f", "F", new Vector3D(startX + 3
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("G", "g", "G", new Vector3D(startX + 4
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("H", "h", "H", new Vector3D(startX + 5
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("J", "j", "J", new Vector3D(startX + 6
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("K", "k", "K", new Vector3D(startX + 7
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("L", "l", "L", new Vector3D(startX + 8
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo(";", ";", ";", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo(":", ":", ":", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo("'", "'", "'", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("\"", "\"", "\"", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));
		// //////////////////
		lineY = 161;
		startX = 70; // -60

		keyInfos.add(new KeyInfo("Z", "z", "Z", new Vector3D(startX + 2
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("X", "x", "X", new Vector3D(startX + 3
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("C", "c", "C", new Vector3D(startX + 4
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("V", "v", "V", new Vector3D(startX + 5
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("B", "b", "B", new Vector3D(startX + 6
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("N", "n", "N", new Vector3D(startX + 7
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));
		keyInfos.add(new KeyInfo("M", "m", "M", new Vector3D(startX + 8
				* advanceMent, lineY), KeyInfo.NORMAL_KEY));

		keyInfos.add(new KeyInfo(",", ",", ",", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("<", "<", "<", new Vector3D(startX + 9
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		keyInfos.add(new KeyInfo(".", ".", ".", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo(">", ">", ">", new Vector3D(startX + 10
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		// FIXME wohin
		keyInfos.add(new KeyInfo("/", "/", "/", new Vector3D(startX + 11
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("?", "?", "?", new Vector3D(startX + 11
				* advanceMent, lineY),
				KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED));

		// /////////
		// Special keys
		keyInfos.add(new KeyInfo("z", "back", "back", new Vector3D(580, 35),
				KeyInfo.NORMAL_KEY));// Backspace
		keyInfos.add(new KeyInfo("v", "\t", "\t", new Vector3D(62, 77),
				KeyInfo.NORMAL_KEY)); // Tab
		keyInfos.add(new KeyInfo("j", "shift", "shift", new Vector3D(78, 120),
				KeyInfo.NORMAL_KEY)); // Shift
		keyInfos.add(new KeyInfo("f", "\n", "\n", new Vector3D(615, 105),
				KeyInfo.NORMAL_KEY)); // Enter

		return keyInfos.toArray(new KeyInfo[keyInfos.size()]);
	}
	
}

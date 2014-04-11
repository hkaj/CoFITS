package mcb.mcbandroid.utils;

import java.util.ArrayList;

/**
 * Utility class to handle tag filtering
 */
public class Tagger {
	
	/**
	 * Convert a string to a list of tag
	 * @param str		The string to convert
	 * @return the list of tag
	 */
	static public ArrayList<String> toTags(String str) {
		ArrayList<String> tags = new ArrayList<String>();
		String split[] = str.split(" ");
		
		for ( String s : split ) {
			tags.add( s );
		}
		
		return tags;
	}
	
	/**
	 * Convert a list of tag to a string
	 * @param tags		The list of tag
	 * @return a string representation
	 */
	static public String toString(ArrayList<String> tags) {
		String str = "";
		for ( String s : tags ) {
			if ( str.isEmpty() == false ) 
				str += " ";
			str += s;
		}
		return str;
	}
	
	/**
	 * Compare the common part of two string
	 * 
	 * For example 'hello' and 'hel' are considered equals
	 * 
	 * @param a					first word 
	 * @param b					second word
	 * @param ignoreCase		if true, 'HELLO' and 'hel' are considered equal
	 * @return the result of the comparison
	 */
	static public boolean compare(String a, String b, boolean ignoreCase) {
		String c = b.substring(0, Math.min(a.length(), b.length()));
		
		if ( ignoreCase )
			return a.equalsIgnoreCase(c);
		else
			return a.equals(c);
	}

	/**
	 * Is the string specified matched by the tags ?
	 * @param tags				List of tag to match
	 * @param str				String to compare to
	 * @param oneIsEnough		If set to true, one matching tag is enough for this method to return true
	 * @param ignoreCase		If set to true, ignore case when comparing tags to the string
	 * @return result of the matching
	 */
	static public boolean match(ArrayList<String> tags, String str, boolean oneIsEnough, boolean ignoreCase) {
		ArrayList<String> stags = toTags(str);
		
		for ( String s : stags ) {
			boolean found = false;
			for ( String ss : tags ) {
				if ( compare(s,ss,ignoreCase) ) {
					found = true;
					break;
				}
			}
			
			if ( oneIsEnough == false && found == false ) {
				return false;
			}
			else if ( oneIsEnough && found == true) {
				return true;
			}
		}
		
		return oneIsEnough == false;
	}
	
	/**
	 * Is the string specified matched by the tags ? (ignoreCase=true)
	 * 
	 * @param tags				List of tag to match
	 * @param str				String to compare to
	 * @param oneIsEnough		If set to true, one matching tag is enough for this method to return true
	 * @return result of the matching
	 */
	static public boolean match(ArrayList<String> tags, String str, boolean oneIsEnough) {
		return match(tags,str,oneIsEnough,true);
	}
	
	/**
	 * Is the string specified matched by the tags ? (ignoreCase=true, oneIsEnough=true)
	 * 
	 * @param tags				List of tag to match
	 * @param str				String to compare to
	 * @return result of the matching
	 */
	static public boolean match(ArrayList<String> tags, String str) {
		return match(tags,str,true,true);
	}
}

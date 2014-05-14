package shake_n_bacon;

import providedCode.*;

/**
 * @author <name>
 * @UWNetID <uw net id>
 * @studentID <id number>
 * @email <email address>
 * 
 *        TODO: REPLACE this comment with your own as appropriate.
 * 
 *        1. This comparator is used by the provided code for both data-counters
 *        and sorting. Because of how the output must be sorted in the case of
 *        ties, your implementation should return a negative number when the
 *        first argument to compare comes first alphabetically.
 * 
 *        2. Do NOT use any String comparison provided in Java's standard
 *        library; the only String methods you should use are length and charAt.
 * 
 *        3. You can use ASCII character codes to easily compare characters
 *        http://www.asciitable.com/
 * 
 *        4. When you are unsure about the ordering, you can try
 *        str1.compareTo(str2) to see what happens. Your
 *        stringComparator.compare(str1, str2) should behave the same way as
 *        str1.compareTo(str2). They don't have to return the same value, but
 *        their return values should have the same sign (+,- or 0).
 */
public class StringComparator implements Comparator<String> {

	/**
	 * TODO Replace this comment with your own as appropriate.
	 */
	@Override
	public int compare(String s1, String s2) {
		// i is used for iterating through strings
		// compare is the result of a comparison between characters at i
		int i = 0;
		int compare = 0;
		
		// Check the lengths first
		int initialCompare = s1.length() - s2.length();
		
		// If the lengths are the same, compare characters
		if (initialCompare == 0) {
			
			// Only compare while there are no differences
			while (compare == 0 && i < s1.length()) {
				char letterOne = s1.charAt(i);
				char letterTwo = s2.charAt(i);
				if (letterOne != letterTwo) {
					// Will return negative numbers if letter one comes first in
					// the alphabet.
					compare = letterOne - letterTwo;
				}
				i++;
			}
		} else {
			// Return negative numbers if first string is shorter
			// Return positive numbers if first string is longer
			compare = initialCompare;
		}
		return compare;
	}
}

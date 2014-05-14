package shake_n_bacon;

import providedCode.Hasher;

/**
 * @author Ryan McDaniel & Alyanna Castillo
 * @UWNetID rynmcd & castia
 * @studentID 1026006 & 09629247
 * @email rynmcd@uw.edu & castia@uw.edu
 */
public class StringHasher implements Hasher {

	/**
	 * Creates a hash for a string.  Uses a custom algorithm that 
	 * shifts each character (in ASCII) based on its placement in the string
	 * and XORs its ASCII value with a running "sum" that is the hash.
	 */
	@Override
	public int hash(String str) {
		int myCurrHash = 0;
		for (int i = 0; i < str.length(); i++) {
			if (i == 0) {
				// Hashes of single character strings will be just the first character
				// as an ASCII integer
				myCurrHash = str.charAt(i);
			} else {
				// Everything after the first character will be shifted to the left based
				// on its position in the string, and XOR'd (essentially adding) with the
				// current Hash value.
				myCurrHash ^= (str.charAt(i) << i);
			}
		}
		return myCurrHash;
	}
}

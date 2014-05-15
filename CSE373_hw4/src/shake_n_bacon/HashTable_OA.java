package shake_n_bacon;

import providedCode.*;

/**
 * @author <name>
 * @UWNetID <uw net id>
 * @studentID <id number>
 * @email <email address>
 * 
 *       A hash table with open addressing.  We used quadratic probing
 */
public class HashTable_OA extends DataCounter {
	private DataCount[] myData;
	Hasher myHash;
	Comparator<String> myComp;
	
	// Current number of unique words
	private int size;
	
	// Number of times resized.
	private int primeCount;
	
	public HashTable_OA(Comparator<String> c, Hasher h) {
		// Create an array of DataCounts, set size to 0
		myData = new DataCount[17];
		size = 0;
		
		// Save the Hasher and Comparator. Set the prime number
		// to use to the first (17).
		primeCount = 0;
		myHash = h;
		myComp = c;
	}

	// Adds a value to the hash table and increments the number
	// of occurrences of the word or number of unique words respectively.
	public void incCount(String data) {
		// Calculate the load factor to figure out rehashing
		float loadFactor = (float)getSize() / myData.length;
		
		// 0.7 is a fairly good value
		if (loadFactor > 0.7) {
			// rehash the table with a new prime number
			primeCount++;
			rehash(primeCount);
		}
		// Find the string's hash and place it in the table
		int index = myHash.hash(data) % myData.length;
		
		// Probe for the correct spot quadratically
		probe(data, index, 0);
	}
	
	// Recreates the hash table twice as large and places old values again.
	// Supports up to 200000 words
	private void rehash(int prime) {
		
		// Set of prime numbers used.
		int[] myPrimes = {17, 37, 79, 163, 331, 673, 1361, 2729, 5471, 10957, 21929, 43867, 87739, 175481, 350963};
		// new array to use
		DataCount[] newData = new DataCount[myPrimes[prime]];
		SimpleIterator myIterator = getIterator();
		
		// While there is stuff to iterate over.
		while (myIterator.hasNext()) {
			// The data that is being placed again
			DataCount myRehashData = myIterator.next();
			
			String data = myRehashData.data;
			
			// The new index to go to
			int index = myHash.hash(data) % newData.length;
			
			// If nothing exists, place it there
			if (newData[index] == null) {
				newData[index] = myRehashData;
				
			// Probe quadratically with a new array size
			} else {
				int i = 1;
				while (newData[(index + i*i) % newData.length] != null) {
					i++;
				}
				newData[(index + i*i) % newData.length] = myRehashData;
			}
		}
		
		// Swap arrays
		myData = newData;
		
	}
	
	// Probe used by incCount.  Will increment the size if something is
	// being placed for the first time.
	private void probe(String data, int index, int probeNum) {
		// Find correct index
		int newIndex = indexProbe(index, probeNum);
		
		// Something is being placed for the first time
		if (myData[newIndex] == null) {
			myData[newIndex] = new DataCount(data, 1);
			size++;
		// Compare if what is already there is the same as what is being placed
		} else if (myComp.compare(myData[newIndex].data, data) == 0){
			myData[newIndex].count++;
			
		// Find a new place to put it if the space is occupied
		} else {
			probe(data, index, probeNum+1);
		}
	}

	// Calculates a quadratic probe based on what probe number it is
	private int indexProbe(int index, int probeNum) {
		return (index + probeNum*probeNum) % myData.length;
	}
	
	// Returns the number of unique strings in the hash table
	public int getSize() {
		return size;
	}

	// Returns the number of times a string has been put into the hash table
	// Will return 0 if the string does not occur
	public int getCount(String data) {
		// TODO Auto-generated method stub
		int index = myHash.hash(data) % myData.length;
		int offset = 0;
		int newIndex = index;
		
		DataCount curData = myData[newIndex];
		
		// If the hash is not used
		if (curData == null) {
			return 0;
		// If the values at the hashed area are different, probe for different spots
		} else if (myComp.compare(curData.data, data) != 0) {
			while(curData != null && myComp.compare(curData.data, data) != 0) {
				offset++;
				newIndex = indexProbe(index, offset);
				// If we have probed and found a match
				if (newIndex < myData.length) {
					curData = myData[newIndex];
				// We never found a match
				} else {
					curData = null;
				}
			}
		}
		
		// If a match was not found return 0
		if (newIndex >= myData.length || curData == null) {
			return 0;
		// Return the correct count
		} else {
			return myData[newIndex].count;
		}
	}

	// Returns an iterator to traverse every value of the hash table
	public SimpleIterator getIterator() {
		return new MyOAIterator();
	}
	
	// Allows iteration through a open-addressed hash table
	private class MyOAIterator implements SimpleIterator {
		// The current saved index
		int curIndex;
		
		// Initialize index to 0
		public MyOAIterator() {
			curIndex = 0;
		}
		
		public boolean hasNext() { 
			return curIndex < myData.length;
		}
		
		public DataCount next() {
			if (!hasNext()) {
				throw new java.util.NoSuchElementException();
			}
			
			DataCount curData = myData[curIndex];
			// Zero case - increment until not null
			while (curData == null && hasNext()) {
				curIndex++;
				
				// Find first not null value to start iterator
				curData = myData[curIndex];
			}
			
			// Current data is saved in curData, increment to "next"
			curIndex++;
			
			// Make sure that the next piece of data is not null too
			while (hasNext() && myData[curIndex] == null) {
				curIndex++;
			}
				
			return curData;
		}
		
	}
	
}
package shake_n_bacon;

import providedCode.*;

/**
 * @author <name>
 * @UWNetID <uw net id>
 * @studentID <id number>
 * @email <email address>
 * 
 *        TODO: Replace this comment with your own as appropriate.
 * 
 *        1. You may implement HashTable with open addressing discussed in
 *        class; You can choose one of those three: linear probing, quadratic
 *        probing or double hashing. The only restriction is that it should not
 *        restrict the size of the input domain (i.e., it must accept any key)
 *        or the number of inputs (i.e., it must grow as necessary).
 * 
 *        2. Your HashTable should rehash as appropriate (use load factor as
 *        shown in the class).
 * 
 *        3. To use your HashTable for WordCount, you will need to be able to
 *        hash strings. Implement your own hashing strategy using charAt and
 *        length. Do NOT use Java's hashCode method.
 * 
 *        4. HashTable should be able to grow at least up to 200,000. We are not
 *        going to test input size over 200,000 so you can stop resizing there
 *        (of course, you can make it grow even larger but it is not necessary).
 * 
 *        5. We suggest you to hard code the prime numbers. You can use this
 *        list: http://primes.utm.edu/lists/small/100000.txt NOTE: Make sure you
 *        only hard code the prime numbers that are going to be used. Do NOT
 *        copy the whole list!
 * 
 *        TODO: Develop appropriate tests for your HashTable.
 */
public class HashTable_OA extends DataCounter {
	private DataCount[] myData;
	Hasher myHash;
	Comparator<String> myComp;
	private int size;
	private int primeCount;
	
	public HashTable_OA(Comparator<String> c, Hasher h) {
		// Create an array of DataCounts, set size to 0
		myData = new DataCount[17];
		size = 0;
		primeCount = 0;
		myHash = h;
		myComp = c;
	}

	@Override
	public void incCount(String data) {

		float loadFactor = (float)getSize() / myData.length;
		if (loadFactor > 0.7) {
			primeCount++;
			rehash(primeCount);
		}
		int index = myHash.hash(data) % myData.length;
		
		probe(data, index, 0);
	}
	
	private void rehash(int prime) {
		int[] myPrimes = {17, 37, 79, 163, 331, 673, 1361, 2729, 5471, 10957, 21929, 43867, 87739, 175481, 350963};
		DataCount[] newData = new DataCount[myPrimes[prime]];
		SimpleIterator myIterator = getIterator();
		
		while (myIterator.hasNext()) {
			DataCount myRehashData = myIterator.next();
			String data = myRehashData.data;
			int index = myHash.hash(data) % newData.length;
			if (newData[index] == null) {
				newData[index] = myRehashData;
			} else {
				int i = 1;
				while (newData[(index + i*i) % newData.length] != null) {
					i++;
				}
				newData[(index + i*i) % newData.length] = myRehashData;
			}
		}
		
		myData = newData;
		
	}
	
	private void probe(String data, int index, int probeNum) {
		int offset = probeNum * probeNum;
		int newIndex = (index + offset) % myData.length;
		
		if (myData[newIndex] == null) {
			myData[newIndex] = new DataCount(data, 1);
			size++;
		} else if (myComp.compare(myData[newIndex].data, data) == 0){
			myData[newIndex].count++;
		} else {
			probe(data, index, probeNum+1);
		}
	}

	private int indexProbe(int index, int probeNum) {
		return (index + probeNum*probeNum) % myData.length;
	}
	
	@Override
	public int getSize() {
		return size;
	}

	@Override
	public int getCount(String data) {
		// TODO Auto-generated method stub
		int index = myHash.hash(data) % myData.length;
		int offset = 0;
		int newIndex = index;
		while (myComp.compare(myData[newIndex].data, data) != 0) {
			offset++;
			newIndex = indexProbe(index, offset);
		}
		return myData[newIndex].count;
	}

	@Override
	public SimpleIterator getIterator() {
		return new MyOAIterator();
	}
	
	private class MyOAIterator implements SimpleIterator {
		int curIndex;
		
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
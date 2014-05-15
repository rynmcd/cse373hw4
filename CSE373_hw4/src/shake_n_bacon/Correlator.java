package shake_n_bacon;

import java.io.IOException;

import providedCode.*;

/**
 * @author <name>
 * @UWNetID <uw net id>
 * @studentID <id number>
 * @email <email address>
 * 
 *        Calculates the relationship between the frequencies of the words that
 *        are similar in two text files.
 */
public class Correlator {

	public static void main(String[] args) {
		// 3 arguments
		if (args.length != 3) {
			usage();
		}
		double variance = 0.0;
		
		// Compare the options
		String firstArg = args[0].toLowerCase();
		DataCounter counterOne = null;
		DataCounter counterTwo = null;
		// Separate chaining
		if (firstArg.equals("-s")) {
			counterOne = new HashTable_SC(new StringComparator(),
					new StringHasher());
			counterTwo = new HashTable_SC(new StringComparator(),
					new StringHasher());
		// Open addressing
		} else if (firstArg.equals("-o")) {
			counterOne = new HashTable_OA(new StringComparator(),
					new StringHasher());
			counterTwo = new HashTable_OA(new StringComparator(),
					new StringHasher());
		// error
		} else {
			usage();
		}
		
		// Count the words
		int total1 = countWords(args[1], counterOne);
		int total2 = countWords(args[2], counterTwo);
		
		// compare sizes for efficiency
		int sizeCompare = counterOne.getSize() - counterTwo.getSize();
		
		// The value in the first slot is iterated over, so less iterations is better
		if (sizeCompare >= 0) {
			variance = calcVariance(counterTwo, counterOne, total2, total1);
		} else {
			variance = calcVariance(counterOne, counterTwo, total1, total2);
		}

		
		// IMPORTANT: Do not change printing format. Just print the double.
		System.out.println(variance);
	}
	
	// Finds the variance between two hash sets.  t1 and t2 are the size of the documents read in
	private static double calcVariance(DataCounter counterOne, DataCounter counterTwo, int t1, int t2) {
		double variance = 0.0;
		SimpleIterator myIterator = counterOne.getIterator();
		
		// While there is still stuff to iterate over
		while (myIterator.hasNext()) {
			// Find the word and the number of times it occurs
			DataCount myWord = myIterator.next();
			
			// Calculate the frequency of the word
			double frequencyOne = ((double)myWord.count) / t1;
			int countTwo = counterTwo.getCount(myWord.data);
			double frequencyTwo = ((double)countTwo) / t2;
			
			// Only if it is within bounds should we calculate the variance
			if (inBounds(frequencyOne) && inBounds(frequencyTwo)) {
				variance += (frequencyOne - frequencyTwo)*(frequencyOne - frequencyTwo);
			}
		}
		return variance;
	}
	
	// Only values below 1% and above 0.01% should be checked
	private static boolean inBounds(double myFreq) {
		return 0.01 >= myFreq && myFreq >= 0.0001 && myFreq != 0.0;
	}
	
	// Prints out info about how to use in the event of an error
	private static void usage() {
		System.err
				.println("Usage: [-s | -o] <filename of doc 1> <filename of doc 2>");
		System.err.println("-s for hashtable with separate chaining");
		System.err.println("-o for hashtable with open addressing");
		System.exit(1);
	}
	
	// Counts the words in a file and returns total number of words counted
	private static int countWords(String file, DataCounter counter) {
		int count = 0;
		
		try {
			FileWordReader reader = new FileWordReader(file);
			String word = reader.nextWord();
			while (word != null) {
				counter.incCount(word);
				word = reader.nextWord();
				count++;
			}
		} catch (IOException e) {
			System.err.println("Error processing " + file + " " + e);
			System.exit(1);
		}
		
		return count;
	}
}

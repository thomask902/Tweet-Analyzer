
/**
 * Thomas Kleinknecht
 * MSCI 240: Assignment 3
 * November 23, 2021
 * This program reads from two csv files, one containing tweets written about Donald Trump and the other Joe Biden, each including info about the tweet and its author.
 * From these files, it reads in a list of the authors with how many tweets they have posted about these candidates. It then outputs to the console it's operational speed
 * along with the top 20 users with the most tweets amoung the lists.
 * Input: The two files, "hashtag_donaldtrump.csv" and "hashtag_joebiden.csv".
 * Output: The speed which it took to read in each file, along with the speed it took to count all of the tweets, and the top 20 users with the most tweets.
 */

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Program {
	public static final String TRUMP_TWEETS_FILENAME = "hashtag_donaldtrump.csv";
	public static final String BIDEN_TWEETS_FILENAME = "hashtag_joebiden.csv";

	/**
	 * This constant will allow you to either read the data into local memory
	 * (_your_ code will be faster, but won't work in Codio for the entire dataset)
	 * or to use persistent storage (_your_ code will be slower, but uses a smaller
	 * amount of memory and is possible to complete entirely in Codio).
	 */
	// TODO: adjust this constant as necessary
	public static final boolean READ_INTO_MEMORY = false;

	/**
	 * This constant will allow you to display the header information for the data
	 * files (i.e., what columns have what names). Set this to false before handing
	 * in the assignment.
	 */
	// TODO: adjust this constant as necessary
	public static final boolean SHOW_HEADERS = false;

	/**
	 * When testing, you may want to choose a smaller portion of the dataset. This
	 * number lets you limit it to only the first MAX_ENTRIES. Setting this over 3.5
	 * million will get all the data. IMPORTANT NOTE: this will ONLY have an effect
	 * when READ_INTO_MEMORAY is true
	 */
	// TODO: adjust this constant as necessary
	// private static final int MAX_TWEETS = 50000;
	public static final int MAX_TWEETS = Integer.MAX_VALUE;

	/**
	 * This constant represents the folder that contains the data. You should not
	 * have to adjust this.
	 */
	public static final String DATA_DIRECTORY = "data";

	/**
	 * This static field manages timing in your code. You can and should reuse it to
	 * time your code.
	 */
	public static Stopwatch stopwatch = new Stopwatch();

	/**
	 * The main method of your program. The first half of this is written for you
	 * (don't adjust this!). Where it says "Add your code here..." you should put
	 * your main program (remember to use methods where appropriate).
	 * 
	 * @param args the arguments to this main program provided on the command line
	 *             (none)
	 * @throws IOException when the data files cannot be read properly
	 */
	public static void main(String[] args) throws IOException {
		/*
		 * We have already setup all the data to make the rest of your assignment easier
		 * for you.
		 * 
		 * All of the data is available in an Iterable object. While you may not know
		 * what an Iterable object is, you've already used these many times before!
		 * ArrayList, LinkedList, the keySet for a HashMap, etc. are all Iterable
		 * objects. To use them, you just need to use a for each loop.
		 * 
		 * IMPORTANT NOTE: the big difference between what you might be used to and the
		 * Iterable is that YOU CAN ONLY ITERATE OVER IT ONE TIME. Once you've done
		 * that, the Iterable object will be "exhausted". Therefore, make sure in that
		 * first pass that you store the information you need in the appropriate Map
		 * object (as per the assignment instructions).
		 */
		Path dir = Paths.get(DATA_DIRECTORY);

		Iterable<CSVRecord> trumpTweets = readData(dir.resolve(TRUMP_TWEETS_FILENAME), "Trump tweets", READ_INTO_MEMORY,
				false);

		Iterable<CSVRecord> bidenTweets = readData(dir.resolve(BIDEN_TWEETS_FILENAME), "Biden tweets", READ_INTO_MEMORY,
				SHOW_HEADERS);

		Iterable<CSVRecord> allTweets = () -> new IteratorChain<>(trumpTweets.iterator(), bidenTweets.iterator());

		/*
		 ************************ IMPORTANT *********************
		 * 
		 * NOTE: You may NOT change anything about these lists! You should only read
		 * stats from index 0 to index list.size() - 1 Do NOT sort the list or change
		 * them in any way.
		 * 
		 ********************************************************/

		// TODO: you can comment/uncomment to test each map
		// implementation.
//        findTopUsingArrayList(allTweets, 20);
//        findTopUsingTreeMap(allTweets, 20);
		findTopUsingHashMap(allTweets, 20);
	}

	/**
	 * TODO: Add your code for unsorted array list here. You will do best to create
	 * many methods to organize your work and experiments.
	 * 
	 * @param allTweets the Iterable object that lets you iterate over all of the
	 *                  data
	 * @param n         the number of users to report the top N for
	 */
	public static void findTopUsingArrayList(Iterable<CSVRecord> allTweets, int n) {

		// declaring ArrayList to store TweetCount variables, and tweetCount value to
		// store the total number of tweets counted
		ArrayList<TweetCount> topTweets = new ArrayList<>();
		int tweetCount = 0;

		// going through each record in allTweets and adding the users and counts to
		// ArrayList as necessary
		for (CSVRecord record : allTweets) {
			tweetCount++;
			if (record.isSet(8)) {
				boolean contains = false;

				// traversing ArrayList to check if the user is already in list, if so
				// increments their count
				for (TweetCount tweet : topTweets) {
					if (tweet.getScreenName().equals(record.get(8))) {
						contains = true;
						tweet.increment();
						break;
					}
				}

				// if the user is not already in list, adds user with a count of 1
				if (!contains) {
					TweetCount temp = new TweetCount(record.get(8), 1);
					topTweets.add(temp);
				}
			}
		}

		// adding TweetCount Objects to priorityQueue from ArrayList to allow it to
		// sort, and passing in comparator which will prioritize maximum value to make
		// printing easy
		PriorityQueue<TweetCount> top = new PriorityQueue<>(Collections.reverseOrder());
		for (TweetCount tweet : topTweets) {
			top.add(tweet);
		}
		System.out.println("To count " + tweetCount + " tweets with an ArrayList took " + stopwatch.getElapsedSeconds()
				+ " seconds.");

		// printing the top twenty users
		printTopOrdered(top, n);
	}

	/**
	 * TODO: Add your code for tree maps here. You will do best to create many
	 * methods to organize your work and experiments.
	 * 
	 * @param allTweets the Iterable object that lets you iterate over all of the
	 *                  data
	 * @param n         the number of users to report the top N for
	 */
	public static void findTopUsingTreeMap(Iterable<CSVRecord> allTweets, int n) {

		// declaring Treemap to store TweetCount variables and update their count
		TreeMap<String, TweetCount> topTweets = new TreeMap<>();

		// calling recordToMap to enter all records into map appropriately
		int tweetCount = recordToMap(topTweets, allTweets);

		// going through TreeMap and adding to priorityQueue, which prioritizes greatest
		// TweetCount
		PriorityQueue<TweetCount> top = mapToQueue(topTweets);
		System.out.println("To count " + tweetCount + " tweets with a TreeMap took " + stopwatch.getElapsedSeconds()
				+ " seconds.");

		// printing the top twenty users
		printTopOrdered(top, n);
	}

	/**
	 * TODO: Add your code for hash maps here. You will do best to create many
	 * methods to organize your work and experiments.
	 * 
	 * @param allTweets the Iterable object that lets you iterate over all of the
	 *                  data
	 * @param n         the number of users to report the top N for
	 */

	public static void findTopUsingHashMap(Iterable<CSVRecord> allTweets, int n) {

		// declaring Hashmap to store TweetCount variables and their count
		HashMap<String, TweetCount> topTweets = new HashMap<>();

		// calling recordToMap to enter all records into map appropriately, and tally
		// the tweets
		int tweetCount = recordToMap(topTweets, allTweets);

		// going through HashMap and adding to priorityQueue, which prioritizes greatest
		// TweetCount
		PriorityQueue<TweetCount> top = mapToQueue(topTweets);
		System.out.println("To count " + tweetCount + " tweets with a HashMap took " + stopwatch.getElapsedSeconds()
				+ " seconds.");

		// printing the top twenty users
		printTopOrdered(top, n);
	}

	/**
	 * YOU SHOULD NOT CHANGE THIS METHOD.
	 * 
	 * This method reads the data into an Iterable object.
	 * 
	 * @param path           the path of the file to read from
	 * @param description    the description to use when reporting the type of data
	 * @param readIntoMemory true if the data should be read into memory (it takes a
	 *                       lot of memory!), false if the Iterable object should
	 *                       just go through the file.
	 * @param printHeader    true if this method should print the header information
	 *                       (i.e., which column has what name).
	 * @return an Iterable object with all of the data in CSVRecord objects
	 * @throws IOException if the file could not be read
	 */
	public static Iterable<CSVRecord> readData(Path path, String description, boolean readIntoMemory,
			boolean printHeader) throws IOException {
		stopwatch.reset();
		stopwatch.start();

		CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new FileReader(path.toFile()));
		Map<String, Integer> headerMap = parser.getHeaderMap();
		Iterable<CSVRecord> iterable = () -> parser.iterator();

		if (readIntoMemory) {
			List<CSVRecord> list2 = StreamSupport.stream(iterable.spliterator(), false).limit(MAX_TWEETS)
					.collect(Collectors.toList());

			System.out.printf("Finished reading %,d %s in %f seconds.\n", list2.size(), description,
					stopwatch.getElapsedSeconds());

			iterable = list2;
		}

		if (printHeader) {
			System.out.println("Data available:");
			for (String key : headerMap.keySet()) {
				int value = headerMap.get(key);
				System.out.printf("\t%d = %s\n", value, key);
			}
		}

		return iterable;
	}

	// method takes a map input and Iterable variable and goes through every record
	// in the iterable adding it to the map appropriately, and returning the number
	// of tweets
	public static int recordToMap(Map<String, TweetCount> topTweets, Iterable<CSVRecord> allTweets) {
		// going through each record in allTweets and adding the users and counts to Map
		// as necessary
		int count = 0;
		for (CSVRecord record : allTweets) {
			count++;
			// checks if there is a user at this record, if so, adds to Map with name as key
			// and TweetCount object as value
			if (record.isSet(8)) {
				String user = record.get(8);

				// checking if a key has already been mapped to this tweet, if so, increments
				// the tweet count, and maps this name to the tweet
				if (topTweets.containsKey(user)) {
					topTweets.get(user).increment();
					topTweets.put(user, topTweets.get(user));
				} else {
					TweetCount temp = new TweetCount(user, 1);
					topTweets.put(user, temp);
				}

			}
		}
		return count;
	}

	// method takes map as input and adds TweetCount objects to priorityQueue
	public static PriorityQueue<TweetCount> mapToQueue(Map<String, TweetCount> topTweets) {
		PriorityQueue<TweetCount> top = new PriorityQueue<>(Collections.reverseOrder());
		for (String name : topTweets.keySet()) {
			TweetCount tweet = topTweets.get(name);
			top.add(tweet);
		}
		return top;
	}

	// prints top "n" values of already ordered priority queue
	public static void printTopOrdered(PriorityQueue<TweetCount> top, int n) {
		for (int i = 0; i < n; i++) {
			TweetCount printing = top.poll();
			System.out.println(printing.getScreenName() + " had " + printing.getCount() + " tweets");
		}
	}
}

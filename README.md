# Tweet-Analyzer

The purpose of this file is to give the reader an overview of the 3 different methods (ArrayList, TreeMap, HashMap) that were used to determine the top 20 tweeters. It will specifically focus on the 3 methods that were created, the performance of each map, and the selection of the method to determine the top 20 users. This report also explains issues which were encountered during the implementation of the 3 methods.

An in-depth explanation of the ArrayList, TreeMap, and HashMap includes the worst-case growth rate for each implementation, how the data was collected and obtained, steps used in collecting the data, and the consistency of the top 20 users that were printed in each method implementation. This also includes how the worst-case growth rate was selected for the method and an explanation of the major logic with the correct growth-rate. Specific data tables and charts were created to give the reader a better understanding of the speed, which will allow for a better analytical approach. The code for these 3 methods will be submitted through Codio/Github with the TweetCount class which includes the comparable execution.

**ArrayList Implementation**

The worst case growth rate of a method is determined by its most time inefficient lines or calls. In the case of this implementation, this is the for each loop which traverses the allTweets iterable, going through each record across the two files. This is not because of the for each loop itself however, but due to the fact that inside this for each loop, there is another for each loop, which traverses the ArrayList which is being created for each record, searching to see if the user is already in the list. With our variable “n” being the number of tweets read in, the worst case for this inner for each loop is that we have to go through every value in the ArrayList, for the last value in the outer for each loop of the records. This would give this entire double loop a worst case growth rate of O(n^2). All other parts of the code, such as adding it to a priorityQueue, have a maximum worst case growth rate of O(n). This is reinforced in the collected data below, as we can see the quadratic trendline fits the data quite well!
Using the Stopwatch class, and stopping the timer after reading in and sorting all tweets, just before printing the top 20, I ensured that only the method itself was being timed. I then ran the findTopUsingArrayList method 3 times at 5 different input values for the number of tweets to be read. This allows for an average run time to be determined to ensure that outlying data points are not skewing the data. The table and subsequent graph are shown below:

The resultant top 20 users were the same as the image in the HashMap implementation section.

**TreeMap Implementation**

As discussed in the previous implementation, we are looking for the parts of the method which are the least efficient. For the TreeMap implementation, this is the recordToMap method, and the mapToQueue method, with both of them having a worst case growth rate of O(nlogn). Other TreeMap specific methods used in this implementation, such as containsKey, get, and put, along with add and poll for the PriorityQueue, we know from the java documentation all have a guaranteed worst case growth rate of O(log n), which makes the overall worst case O(nlogn). This is due to the for each loops in both the recordToMap method, which takes the tweets from the iterable object and adds them to the map, and the mapToQueue method, which takes these map values and adds them to a priority queue. This is because in the worst case, these O(logn) methods are being run n times. I was unable to do a linearithmic trendline in google sheets, but you can see that the data mimics this path as it starts with a steeper slope and slowly reduces the slope.
The data collection process is explained in the ArrayList implementation section, but the data collected for the TreeMap implementation is below:

The resultant top 20 users were the same as the image in the HashMap implementation section.

**HashMap Implementation**

Due to the fact that the implementation of the HashMap solution is nearly identical to the TreeMap implementation, and the containsKey, get, and put methods for HashMap are also O(logn), this results in the same overall worst case order of growth of O(nlogn). The same recordToMap and mapToQueue methods are called, which, as explained in the TreeMap implementation section, have a worst case of O(nlogn) due to their for each loops and O(logn) method calls. Just as in the data for TreeMap, we can see that for HashMap, the data takes a linearithmic form, with the slope being steeper at first but slowly lessening as n increases.
The data collection process is explained in the ArrayList implementation section, but the data collected for the HashMap implementation is below:


The resultant top 20 users as found by this implementation were the same for all three implementations.



**Performance and Thoughts on Optimal Solution**

When looking at the data, or even without seeing runtimes and just looking at the ease of implementation in the code itself, it is clear that the ArrayList is not the most optimal solution for this problem. Because the ArrayList class, as stated in the java documentation, requires a worst case of O(n) time to find the index of an object, or find if it is in the list, it is no faster to use these methods than to use a “for each” loop as I did in my implementation. This is what separates this implementation from the TreeMap and Hashmap, as they also contain “for each” loops, but their methods used within to access and add objects or pairs runs at a worst case of O(logn). When looking at the TreeMap and HashMap implementations, although their runtimes are quite similar, and their worst case order of growth is the same, the average run time for HashMap is consistently slightly faster than the TreeMap, making it a better option. This is due to the fact that the TreeMap is sorting the keys, as it is implemented with a binary search tree, but in our case, the keys, which are the names of the users, do not need to be sorted, and this feature provides us with no value and just results in the TreeMap implementation running slightly slower. 

**Method to Determine Top 20 Users**

This decision was made based on a few different criteria, ease of implementation and integration into the three data structures which were being used, ease of printing in the correct order, and run time efficiency. I decided to use a priority queue, which is implemented with a priority heap, as it had good overall efficiency, giving (O(logn)) to add values to the queue, and to remove the value which I wanted to print using the poll method (also O(logn)). It also was simple to integrate with each of my data structures, as I didn’t have to manipulate the data at all. Third of all, by adding a comparable iteration to my TweetCount object class, and passing a comparator into the PriorityQueue (Collections.reverseOrder()), which took the comparable I had implemented and sorted by prioritizing the max TweetCount object (which truly meant the max count value), it was easy to remove the first 20 priority objects and print them. One of the reasons PriorityQueue gave such better efficiency over other methods, is the fact that it does not sort all of the data, but only gives the singular priority object from the queue, which worked fine for me and made it a great choice.

Language used-Java

PROGRAM DESCRIPTION
The program is divided into three classes:
search.java
Edge.java
Node.java

An object of Node.java represents a node that is pushed into the queue(for task 1,3,4) and or stack(for task 2). Each node stores information about the node's name, cost to reach that node and the ancestor list of that node.

An object of Edge.java represents a cell in the adjacency matrix which in turn represents an edge of the tree. Each object stores information such as source & destination of the edge,cost, reliability and if the edge is present .

search.java contains following functions:
main()-extracts the arguments, creates the adjacency matrix and perform as per the option
bfs()-performs bfs/task1
dfs()-performs dfs/task2
ucs()-performs ucs/task3
ucsmodified()-performs task 4
createNodeDictionary()-converts the name of the vertices into numbers whic can be used as index to the adjacency matrix
getKeyInDictionary()- gets the key from nodeDictionary(given 0, it will fetch "UCSB")
createFile()- creates the log file and path file and write data onto it

sortqueue() and findMin()- i have implemented in order to sort the queue, i have used the pseudo code from STACK OVERFLOW and implemented that pseudo code. Here is the reference
Reference : http://stackoverflow.com/questions/5134127/sorting-a-queue-using-same-queue

ANALYSIS OF SIMILARITIES/DIFFERENCES BETWEEN TASK 3 AND 4
In both the task 3 and task 4 the open queue is sorted after the top of the queue has been expanded.
The only difference is that in task 3 the cost of a node is equal to the cost of all the edges leading to that node(also called as ancestors list of that node), but in task 4 we also add the reliability factor to the cost which is equal to number of edges having reliability as  zero divided by 2 in the edges leading to that node. 


HOW TO COMPILE/EXECUTE
The Program needs java version 1.6 or higher on aludra.
For compiling(we have to compile all the java files(Node.java,Edge.java,search.java))
javac *.java
For execution
java search -t <task> -s <start_node> -g <goal_node> -i <input_file> -op <output_path> -ol <output_log>
where task can be 1,2,3,4


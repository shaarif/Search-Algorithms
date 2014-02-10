import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;
public class search{
	static String[][] edges=new String[1000][4];
	static Map<String, Integer> nodeDictionary = new TreeMap<String,Integer>();
	static Queue<Node> ucsq=new LinkedList<Node>();
	static int numberOfEdges=0;
	static int numberOfNodes=0;
	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
        /* Process the input*/
		int task=Integer.parseInt(args[1]);
		String startNode=args[3];
		String goalNode=args[5];
		String inputPath=args[7];
		String outputPath=args[9];
		String outputPathLog=args[11];
		/* Process the input file*/
		processInputFile(inputPath);
		/*Creating the Dictionary*/
		createNodeDictionary();
		/*Creating Adjacency Matrix*/
		int node1,node2,rel;
		float cost;
		Edge[][] adjMatrix=new Edge[numberOfNodes][numberOfNodes];
		for(int a=0;a<numberOfEdges;a++)
		{
			node1=nodeDictionary.get(edges[a][0]);
			node2=nodeDictionary.get(edges[a][1]); 
			cost=Float.parseFloat(edges[a][2]);
			rel=Integer.parseInt(edges[a][3]);
			adjMatrix[node1][node2]=new Edge(node1,node2,cost,rel,true);
			adjMatrix[node2][node1]=new Edge(node2,node1,cost,rel,true);
		}
		//Fill the rest of the matrix
		for(int i=0;i<numberOfNodes;i++)
		{
			for(int j=0;j<numberOfNodes;j++)
			{
				if(adjMatrix[i][j]==null)
					adjMatrix[i][j]=new Edge(0,0,0,0,false);
			}
	    }
		/*Pick a Task*/
		switch (task)
		{
		case 1:
			bfs(adjMatrix,startNode,goalNode,outputPath,outputPathLog);
			break;
		case 2:
			dfs(adjMatrix,startNode,goalNode,outputPath,outputPathLog);
			break;
		case 3:
			ucs(adjMatrix,startNode,goalNode,outputPath,outputPathLog);
			break;
		case 4:
			ucsmodified(adjMatrix,startNode,goalNode,outputPath,outputPathLog);
			break;
		}
	}
	/* Modified Uniform Cost Search */
	public static void ucsmodified(Edge[][] mat,String startNode, String goalNode, String outputPath, String outputPathLog) throws FileNotFoundException, UnsupportedEncodingException
	{
		Queue<String> ucstraversal=new LinkedList<String>();
		Queue<String> ucspath=new LinkedList<String>();
		ArrayList<String> aList=new ArrayList<String>();
		ArrayList<String> optList=new ArrayList<String>();
		int rowindex=0;
		Node srcNode=new Node(startNode,0,aList);
		Node removed=new Node(null,0,aList);
		String dest;
		ucsq.add(srcNode);
		float totalCost=0;
		float optCost=9999;
		float relcount=0;
		int flag=0;
		while(ucsq.isEmpty()==false)
		{   
			srcNode=ucsq.peek();
			rowindex=nodeDictionary.get(srcNode.name);
			for(int j=0;j<numberOfNodes;j++)
			{   
				if(mat[rowindex][j].getPresence()==true)
				{
				    dest=getKeyInDictionary(j);
				    //check if srcNode==GoalNode
				    if(srcNode.name.equalsIgnoreCase(goalNode))
				    {
				    	if(srcNode.cost<optCost)
				    	{   optList=new ArrayList<String>();
				    		optList.addAll(srcNode.ancestorList);
				    		optList.add(srcNode.name);
				    		optCost=srcNode.cost;
				    	}
				    }
				    else
				    {    
				    	if(!srcNode.ancestorList.contains(dest))
				    	{//storing ancestor list of destNode in aList
				    	aList.addAll(srcNode.ancestorList);
				    	//if(!aList.contains(srcNode.name))
				        aList.add(srcNode.name);
				        //increasing the cost
				    	totalCost=srcNode.cost+mat[rowindex][j].cost;
				    	if(mat[rowindex][j].reliability==0)
				    		relcount++;
				    	totalCost=totalCost+(relcount/2);
				    	relcount=0;
				        Node destNode=new Node(dest,totalCost,aList);
				        //check if ucsq already have that destNode then remove the one with more cost
				        for(Node n: ucsq)
				        {       
				        		if(n.name.equalsIgnoreCase(destNode.name))
				        		{    
				        			 if(n.cost>destNode.cost)
				        			  {
				        			  n.cost=destNode.cost;
				        			  n.ancestorList.clear();
				        			  n.ancestorList.addAll(destNode.ancestorList);
				        
				        			  }
				        			 flag=1;
				        		}
				        }	
				        if(flag==0)
				    	{ 
				    	  if(!ucstraversal.contains(destNode.name))
				    		  ucsq.add(destNode);
				    	}
				    	aList.clear();
				    	flag=0;
				    	}
				    }
			     }
		     }
			ucstraversal.add(srcNode.name);
			removed=ucsq.remove();
			if(removed.name.equalsIgnoreCase(goalNode))
			{
				break;	
			}
			ucsq=sortqueue(ucsq);
		}
		java.util.Iterator<String> itr = optList.iterator();
		while(itr.hasNext()){
			ucspath.add(itr.next());
		}
		createFile(outputPath,ucspath);
		createFile(outputPathLog,ucstraversal);
	}
	/* Uniform Cost  Search*/
	public static void ucs(Edge[][] mat,String startNode, String goalNode, String outputPath, String outputPathLog) throws FileNotFoundException, UnsupportedEncodingException
	{
		Queue<String> ucstraversal=new LinkedList<String>();
		Queue<String> ucspath=new LinkedList<String>();
		ArrayList<String> aList=new ArrayList<String>();
		ArrayList<String> optList=new ArrayList<String>();
		int rowindex=0;
		Node srcNode=new Node(startNode,0,aList);
		Node removed=new Node(null,0,aList);
		String dest;
		ucsq.add(srcNode);
		float totalCost=0;
		float optCost=9999;
		int flag=0;
		while(ucsq.isEmpty()==false)
		{   
			srcNode=ucsq.peek();
			rowindex=nodeDictionary.get(srcNode.name);
			for(int j=0;j<numberOfNodes;j++)
			{   
				if(mat[rowindex][j].getPresence()==true)
				{
				    dest=getKeyInDictionary(j);
				    //check if srcNode==GoalNode
				    if(srcNode.name.equalsIgnoreCase(goalNode))
				    {
				    	if(srcNode.cost<optCost)
				    	{   optList=new ArrayList<String>();
				    		optList.addAll(srcNode.ancestorList);
				    		optList.add(srcNode.name);
				    		optCost=srcNode.cost;
				    	}
				    }
				    else
				    {    
				    	if(!srcNode.ancestorList.contains(dest))
				    	{//storing ancestor list of destNode in aList
				    	aList.addAll(srcNode.ancestorList);
				    	//if(!aList.contains(srcNode.name))
				        aList.add(srcNode.name);
				        //increasing the cost
				    	totalCost=srcNode.cost+mat[rowindex][j].cost;
				        Node destNode=new Node(dest,totalCost,aList);
				        //check if ucsq already have that destNode then remove the one with more cost
				        for(Node n: ucsq)
				        {       
				        		if(n.name.equalsIgnoreCase(destNode.name))
				        		{    
				        			 if(n.cost>destNode.cost)
				        			  {
				        			  n.cost=destNode.cost;
				        			  n.ancestorList.clear();
				        			  n.ancestorList.addAll(destNode.ancestorList);
				        
				        			  }
				        			 flag=1;
				        		}
				        }	
				        if(flag==0)
				    	{ 
				    	  if(!ucstraversal.contains(destNode.name))
				    		  ucsq.add(destNode);
				    	}
				    	aList.clear();
				    	flag=0;
				    	}
				    }
			     }
		     }
			ucstraversal.add(srcNode.name);
			removed=ucsq.remove();
			if(removed.name.equalsIgnoreCase(goalNode))
			{
				break;
			}
			ucsq=sortqueue(ucsq);
		}
		/*Printing the optimal path & optimal Cost*/
		java.util.Iterator<String> itr = optList.iterator();
		while(itr.hasNext()){
			ucspath.add(itr.next());
		}
		/*Copying the traversal in a txt file*/
		createFile(outputPath,ucspath);
		createFile(outputPathLog,ucstraversal);
	}
	/* Depth First Search*/
	public static void dfs(Edge[][] mat,String startNode, String goalNode, String outputPath, String outputPathLog) throws FileNotFoundException, UnsupportedEncodingException
	{
		Stack<Node> dfsq=new Stack<Node>();
		Queue<String> dfstraversal=new LinkedList<String>();
		Queue<String> dfspath=new LinkedList<String>();
		ArrayList<String> aList=new ArrayList<String>();
		ArrayList<String> optList=new ArrayList<String>();
		int rowindex=0;
		float totalCost=0;
		float optCost=Float.MAX_VALUE;
		String dest;
		Node srcNode=new Node(startNode,0,aList);
		dfsq.add(srcNode);
		while(dfsq.isEmpty()==false)
		{
			srcNode=dfsq.peek();
			dfstraversal.add(srcNode.name);
			dfsq.pop();
			rowindex=nodeDictionary.get(srcNode.name);
			for(int j=0;j<numberOfNodes;j++)
			{   
				if(mat[rowindex][j].getPresence()==true)
				{
					dest=getKeyInDictionary(j);
				    //check if srcNode==GoalNode
				    if(srcNode.name.equalsIgnoreCase(goalNode))
				    {
				    	if(srcNode.cost<optCost)
				    	{   optList=new ArrayList<String>();
				    		optList.addAll(srcNode.ancestorList);
				    		optList.add(srcNode.name);
				    		optCost=srcNode.cost;
				    	}
				    }
				    else
				    {    
				    	if(!srcNode.ancestorList.contains(dest))
				    	{//storing ancestor list of destNode in aList
				    	aList.addAll(srcNode.ancestorList);
				    	//if(!aList.contains(srcNode.name))
				        aList.add(srcNode.name);
				        //increasing the cost
				    	totalCost=srcNode.cost+mat[rowindex][j].cost;
				        Node destNode=new Node(dest,totalCost,aList);
				    	dfsq.push(destNode);
				    	aList.clear();
				    	}
				    }
				}
			}
		}
		/*Printing the optimal path & optimal Cost*/
		java.util.Iterator<String> itr = optList.iterator();
		while(itr.hasNext()){
			dfspath.add(itr.next());
		}
		createFile(outputPath,dfspath);
		createFile(outputPathLog,dfstraversal);
	}
	/* Breadth First Search*/
	public static void bfs(Edge[][] mat,String startNode, String goalNode, String outputPath, String outputPathLog) throws FileNotFoundException, UnsupportedEncodingException
	{
		Queue<Node> bfsq=new LinkedList<Node>();
		Queue<String> bfstraversal=new LinkedList<String>();
		Queue<String> bfspath=new LinkedList<String>();
		ArrayList<String> aList=new ArrayList<String>();
		ArrayList<String> optList=new ArrayList<String>();
		int rowindex=0;
		Node srcNode=new Node(startNode,0,aList);
		String dest;
		bfsq.add(srcNode);
		float totalCost=0;
		float optCost=Float.MAX_VALUE;
		while(bfsq.isEmpty()==false)
		{   
			srcNode=bfsq.peek();
			rowindex=nodeDictionary.get(srcNode.name);
			for(int j=0;j<numberOfNodes;j++)
			{   
				if(mat[rowindex][j].getPresence()==true)
				{
				    dest=getKeyInDictionary(j);
				    //check if srcNode==GoalNode
				    if(srcNode.name.equalsIgnoreCase(goalNode))
				    {
				    	if(srcNode.cost<optCost)
				    	{   optList=new ArrayList<String>();
				    		optList.addAll(srcNode.ancestorList);
				    		optList.add(srcNode.name);
				    		optCost=srcNode.cost;
				    	}
				    }
				    else
				    {    
				    	if(!srcNode.ancestorList.contains(dest))
				    	{//storing ancestor list of destNode in aList
				    	aList.addAll(srcNode.ancestorList);
				    	//if(!aList.contains(srcNode.name))
				        aList.add(srcNode.name);
				        //increasing the cost
				    	totalCost=srcNode.cost+mat[rowindex][j].cost;
				        Node destNode=new Node(dest,totalCost,aList);
				    	bfsq.add(destNode);
				    	aList.clear();
				    	}
				    }
			     }
		     }
			bfstraversal.add(srcNode.name);
			bfsq.remove();
		}
		/*Printing the optimal path & optimal Cost*/
		java.util.Iterator<String> itr = optList.iterator();
		while(itr.hasNext()){
			bfspath.add(itr.next());
		}
		createFile(outputPath,bfspath);
		createFile(outputPathLog,bfstraversal);
		}
	public static String getKeyInDictionary(int value)
	{
		for (Entry<String, Integer> entry : nodeDictionary.entrySet()) {
		     if(entry.getValue()==value)
		    	 return entry.getKey();
		}
		return null;
	}
	/* Creating a hash dictionary of the nodes*/
	public static void createNodeDictionary()
	{
		int count=0,b=0;
		for(int a=0;a<numberOfEdges;a++)
        {
			while(b<2)
			{
			if(nodeDictionary.isEmpty())
			{ nodeDictionary.put(edges[a][b], count);
			  count++;
			}
			else
			{
				if(nodeDictionary.containsKey(edges[a][b]) == false)
				{ nodeDictionary.put(edges[a][b], count);
				  count++;
				}
			}
			b++;
			}
			b=0;
        }
		numberOfNodes=count;
		/*print TreeMap*/
		/*for (Entry<String, Integer> entry : nodeDictionary.entrySet()) {
		     System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
		}*/
		
	}
	/* Process input file*/
	public static void processInputFile(String path)
	{
		BufferedReader br=null;
		int k=0,i=0;
		try{
			br= new BufferedReader( new FileReader(path));
			String line;
	        while ((line = br.readLine()) != null && k<4) {
	        	 String[] tokens = line.split(",");
	        	 for(int j=0;j<tokens.length;j++)
	        	 {
	        		 edges[i][k]=tokens[j];
                     k++;	 
	        	 }
	        	 k=0;
	        	 i++;
	        }
	        
	        numberOfEdges=i;
		}
		catch (Exception e)
		{
			
		}
	}
	/* Write output to the files*/
	public static void createFile(String fileName, Queue<String> q) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		while(q.peek()!=null)
		{
			writer.println(q.peek());
			q.remove();
		}
		writer.close();
	}
	/* Reference : http://stackoverflow.com/questions/5134127/sorting-a-queue-using-same-queue */
	public static Queue<Node> sortqueue(Queue<Node> q)
	{
		int length=q.size();
		float min=0;
		Node curNode,retNode;
		for(int i=0;i<length;i++)
		{
			retNode=findMin(q,length-i,length);
			min=retNode.cost;
			for(int j=1;j<=length;j++)
			{
				curNode=q.remove();
				if(curNode.cost != min)
				q.add(curNode);
			}
			q.add(retNode);
		}
		return q;
	}
	public static Node findMin(Queue<Node> q,int k,int length)
	{
		float min=Float.MAX_VALUE;
		Node dequeuedNode;
		ArrayList<String> aList=new ArrayList<String>();
		Node retNode=new Node(null,0,aList);
		for(int i=1;i<=length;i++)
		{
			dequeuedNode=q.remove();
			if(dequeuedNode.cost<min && i<=k)
			{
				min=dequeuedNode.cost;
				retNode=dequeuedNode;
			}
			q.add(dequeuedNode);
		}
		return retNode;
	}
	/*Reference End*/
}

import java.util.ArrayList;
public class Node {
public String name;
public float cost;
public ArrayList<String> ancestorList=new ArrayList<String>();
public Node(String nodeName, float nodeCost, ArrayList<String> aList )
{

	name=nodeName;
	cost=nodeCost;
	ancestorList.addAll(aList);
}
}

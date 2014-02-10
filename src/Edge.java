public class Edge {
 public float cost=0;
 public int reliability=0;
 public int srcnode=0;
 public int destnode=0;
 public Boolean isPresent=false;
 
 public Edge(int src, int dest, float c,int rel,Boolean isP)
 {
	 cost=c;
	 reliability=rel;
	 srcnode=src;
	 destnode=dest;
	 isPresent=isP;
 }
 public Boolean getPresence()
 {
	 return isPresent;
 }
 public float getCost()
 {
	 return cost;
 }
 public int getReliability()
 {
	 return reliability;
 }

}

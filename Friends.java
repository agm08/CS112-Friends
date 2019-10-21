package friends;

import structures.Queue;
import structures.Stack;

import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		
		Queue <Person> names = new Queue<>();		
		boolean [] visited = new boolean [g.members.length];
		Person start = g.members[g.map.get(p1)];
		Person target = g.members[g.map.get(p2)];
		ArrayList <Person> namecaller = new ArrayList<>();
		boolean targetfound = false;
		
		names.enqueue(start);
		//enqueue names 
		while(!names.isEmpty()) {
			//dequeue names
			Person temp = names.dequeue();
			//add dequeued names to arraylist
			namecaller.add(temp);
			
			if(target.equals(temp)) {
				targetfound = true;
				break;
			}
			
			int nameIndex = g.map.get(temp.name);
			visited[nameIndex] = true;
			Friend personsFriends = g.members[nameIndex].first;
			while(personsFriends != null) {
				if(visited[personsFriends.fnum] == false) {
					names.enqueue(g.members[personsFriends.fnum]);
						if(target.equals(g.members[personsFriends.fnum])) {
							targetfound = true;
							break;
						}
					
				}
				personsFriends = personsFriends.next;
			}
		}
		
		Stack <String> holdnames = new Stack<>();
		
		if(targetfound == true) {
			int minIndex;
			int i = namecaller.size()-1;
			holdnames.push(namecaller.get(i).name);
			while(i != 0) {
				Friend friendsPtr = namecaller.get(i).first; 
				minIndex = Integer.MAX_VALUE;
				while(friendsPtr != null) {
					Person tempPerson = g.members[friendsPtr.fnum];
					for(int j =0; j<namecaller.size(); j++) {
						if(tempPerson == namecaller.get(j) && j<i && j<minIndex){
							minIndex =j;
						}
					}
					friendsPtr = friendsPtr.next;
				}
				i = minIndex;
				holdnames.push(namecaller.get(i).name);
			}
			
			ArrayList <String> correctPath = new ArrayList<>();
			while(!holdnames.isEmpty()) {
				correctPath.add(holdnames.pop());
			}  
			return correctPath;
		}
		
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		
		ArrayList<ArrayList<String>> cliquesList = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		
		
		
		for(int i = 0; i<g.members.length; i++) {
			
			Stack <String> oldStack = new Stack<String>();
			Stack<String> newStack = new Stack<String>();
			ArrayList <String> hold = new ArrayList<String>();
			 Person currentPerson = g.members[i];
			 int index = g.map.get(currentPerson.name);
			 
			 if(currentPerson.school == null) {
				 continue;
			 }
			 
			 if(currentPerson.school.equals(school) && visited[index] == false) {
				 tempBFS(g, visited, school, currentPerson, oldStack);
				 while(!oldStack.isEmpty()) {
					 newStack.push(oldStack.pop());
				 }
			 }
			 while(!newStack.isEmpty()) {
				hold.add(newStack.pop()); 
				
			 }
			 
			 if(!hold.isEmpty()) {
				 cliquesList.add(hold);
			 }
		}
		
		return cliquesList;
	}
		
		private static void tempBFS(Graph g, boolean [] visited, String School, Person p, Stack <String> s) {
			Queue <Person> BfsQueue = new Queue <>();
			BfsQueue.enqueue(p);
			
			while(!BfsQueue.isEmpty()) {
				Person current = BfsQueue.dequeue();
				s.push(current.name);
				
				
				visited[g.map.get(current.name)] = true;
				
				Friend currentFriend = current.first;
				
				while(currentFriend != null) {
					
					String friendSchool = g.members[currentFriend.fnum].school;
					if(friendSchool == null) {
						currentFriend = currentFriend.next;
						continue;
					}
					if(friendSchool.equals(School) && visited[currentFriend.fnum] == false) {
						BfsQueue.enqueue(g.members[currentFriend.fnum]);
						visited[currentFriend.fnum] = true;
					}
					currentFriend = currentFriend.next;
				}
				
			}
		}
		
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
	
	
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		
		
		ArrayList <String> connectors = new ArrayList <>();
		boolean [] visited = new boolean [g.members.length];
		boolean [] backedUp = new boolean [g.members.length];
		int [] dfsnum = new int[g.members.length];
		int [] backnum = new int [g.members.length];
		
		for(int i = 0; i < g.members.length; i++) {
			if(visited[i] == false)
				DFSConnectors(g, i, visited, dfsnum, backnum, i, connectors, backedUp, i);
		}
		
		return connectors;
		
	}

	private static void DFSConnectors(Graph g, int perIndex, boolean[] visited, int[] dfsnum, int backnum[], 
			int prevIndex, ArrayList<String> connectors, boolean[] Backed, int startIndex) {
		
		if(visited[perIndex] == true) {
			return;
		}
		
		visited[perIndex] = true;
		dfsnum[perIndex] = dfsnum[prevIndex] + 1;
		backnum[perIndex] = dfsnum[perIndex];
		
		Friend friPtr = g.members[perIndex].first;
		while(friPtr != null) {
			
			if(visited[friPtr.fnum]) {
				backnum[perIndex] = Math.min(backnum[perIndex], dfsnum[friPtr.fnum]);
			}
			
			else {

				DFSConnectors(g, friPtr.fnum, visited, dfsnum, backnum, perIndex, connectors, Backed, startIndex);
				
				if(!connectors.contains(g.members[perIndex].name) && dfsnum[perIndex] <= backnum[friPtr.fnum] ) {
					
					if(perIndex != startIndex || Backed[perIndex] == true) {
						connectors.add(g.members[perIndex].name);
					}
				}
				if(dfsnum[perIndex] > backnum[friPtr.fnum]){
					backnum[perIndex] = Math.min(backnum[perIndex], backnum[friPtr.fnum]);
				}
				
				Backed[perIndex] = true;
			}
			
			friPtr = friPtr.next;
		}
		
	}
}


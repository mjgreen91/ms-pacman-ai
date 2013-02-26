package pacman.entries.pacman;

import java.util.Timer;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;
	private int sectionScore = 0;
	private int bestJ = 0;
	
	public MOVE getMove(Game game, long timeDue) 
	{
		
		/*
		 * What this needs to do:
		 * Find the neighbouring nodes.
		 * Evaluate them and find the best one.
		 * Find the neighbours for that node.
		 * Rinse repeat until time runs out or best node found
		 */
		
		MOVE lastMove = game.getPacmanLastMoveMade();
		int curNode = game.getPacmanCurrentNodeIndex();
		int bestS = 0;
		int closeG = 0;
		MOVE[] moves = game.getPossibleMoves(curNode);
		boolean ghostAlert = false;

		//int[]ghostDs = getGhostDistances(game, curNode);
		
	//	if(ghostAlert == false){
			System.out.println("Pos moves: " + moves.length);
			for(int i = 0; i < moves.length ;i ++){
				int searchNode = game.getNeighbour(curNode, moves[i]);
				int junc = findNextJunction(game, searchNode, moves[i]);

				System.out.println("i = " + i);
				System.out.println("score: " + sectionScore + " Junction: " + junc);

				if(sectionScore > bestS){
					bestS = sectionScore;
					bestJ = junc;
				}

				else if(sectionScore == 0 && i == moves.length){
					System.out.println("Finding nearest pill");
					bestJ = game.getClosestNodeIndexFromNodeIndex(curNode, game.getActivePillsIndices(), DM.PATH);
				}
				sectionScore = 0;

				System.out.println("Moving to: " + bestJ + " Score: " + bestS);
				System.out.println();
				myMove = game.getNextMoveTowardsTarget(curNode, bestJ, lastMove, DM.PATH);
			}
	//	}
		
/*		//Ghost is too close: RUN AWAY!
		else if(ghostAlert == true){
			System.out.println("Running Away");
			myMove = game.getNextMoveAwayFromTarget(curNode, closeG, DM.PATH);
		}*/
		
		return myMove;
	}

	/**
	 * Keeps searching along a path until it finds a junction or corner.
	 * Every node passed through on route is scored and the final score is stored
	 * for evaluation purposes. 
	 * @param game - A copy of the game state
	 * @param startNode - The node to begin searching from
	 * @param last - The last move made (Gives direction in which to search)
	 * @returns - The index of the next junction or corner.
	 */
	public int findNextJunction(Game game, int startNode, MOVE direc){
		int score = 0;
		int junction = 0;
		int nextNode = startNode;
		boolean atJunction = false;
		int steps = 0;
			while(!atJunction){
				
				steps++;
								
				//Check if node is a junction
				if(game.getPossibleMoves(nextNode).length > 2 && steps != 0){
					System.out.println("At junction: " + nextNode);
					System.out.println(steps);
					score = score + getNodeScore(game, nextNode);
					System.out.println("Score this path: " + score);
					System.out.println();
					atJunction = true;
				}
				//Check if corner
				else if(game.getNeighbour(nextNode, direc) == -1 && steps != 0){
					System.out.println("At Corner: " + nextNode);
					System.out.println(steps);
					score = score + getNodeScore(game, nextNode);
					System.out.println("Score this path: " + score);
					System.out.println();
					atJunction = true;
				}
				//Still in corridor get next node
				else{
					score = score + getNodeScore(game, nextNode);
					nextNode = game.getNeighbour(nextNode, direc);
				}
			}
		
		junction = nextNode;
		sectionScore = score;
		return junction;
}
	/**
	 * Gets the score for the give node. Score is based on 
	 * point value of any pills at that node
	 * @param game - copy of the game state
	 * @param node - The node to be scored.
	 * @returns a score for the node.
	 */
	public int getNodeScore(Game game, int node){
		int nScore = 0;
		int pill = game.getPillIndex(node);
		int pPill = game.getPowerPillIndex(node);
		
		//Is there a normal pill at this node?
		if(pill != -1){
			if(game.isPillStillAvailable(pill)){
				nScore = 10;
			}
		}
		
		//Is there a powerPill at this node?
		else if(pPill != -1){
			if (game.isPowerPillStillAvailable(pPill)){
				nScore = 50;
			}
		}
		
		System.out.println(pill + " " + pPill);
		return nScore;
	}
	
	public int[] getGhostDistances(Game game, int pacNode){
		int[] ghostDists = new int[4];
		GHOST[] ghosts = new GHOST[4];
		ghosts[0] = GHOST.BLINKY;
		ghosts[1] = GHOST.PINKY;
		ghosts[2] = GHOST.INKY;
		ghosts[3] = GHOST.SUE;
		
		for(int i = 0; i < ghosts.length; i++){
			int gNode = game.getGhostCurrentNodeIndex(ghosts[i]);
			ghostDists[i] = game.getShortestPathDistance(pacNode, gNode);	
		}

		
		return ghostDists;
	}
	
} 

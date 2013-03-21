package pacman.entries.pacman;

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
	private int gNode = 0;
	
	@Override
	public MOVE getMove(Game game, long timeDue) 
	{
		
		
		MOVE lastMove = game.getPacmanLastMoveMade();
		int curNode = game.getPacmanCurrentNodeIndex();
		int bestS = 0;
		int bestN = 0;
		MOVE[] moves = game.getPossibleMoves(curNode);
		boolean ghostAlert;
		
		ghostAlert = isGhostTooClose(game, curNode);
		
		if(ghostAlert == false){
		//	System.out.println("Pos moves: " + moves.length);

			//For every possible move
			for(int i = 0; i < moves.length ; i ++){
				int neigh = curNode;
				int score = 0;
				//Get the total score for the next 3 nodes in this direction.
				for(int j=0; j < 3; j++){
					score = score + getNodeScore(game, neigh);
					neigh = game.getNeighbour(neigh, moves[i]);
					if(neigh == -1){
						break;
					}
				}
			//	System.out.println("Searched 5 neighbours");
				if(score > bestS && neigh != -1){
					bestS = score;
					bestN = neigh;					
				}
					
			}

			if(bestS == 0){
		//			System.out.println("Finding nearest pill");
					bestN = game.getClosestNodeIndexFromNodeIndex(curNode, game.getActivePillsIndices(), DM.PATH);
				}
				bestS = 0;

			//	System.out.println("Moving to: " + bestN + " Score: " + bestS);
			//	System.out.println();
				myMove = game.getNextMoveTowardsTarget(curNode, bestN, lastMove, DM.PATH);
			}
		
		//Ghost is too close: RUN AWAY!
		else if(ghostAlert == true){
		//	System.out.println("Running Away");
			myMove = game.getNextMoveAwayFromTarget(curNode, gNode, DM.PATH);
			ghostAlert = isGhostTooClose(game, game.getNeighbour(curNode, myMove));
		}
		
		return myMove;
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
		
		/*//Is there an edible ghost in this node;
		if(nomTime && node == gNode){
			nScore = nScore + game.getGhostCurrentEdibleScore();
		}*/
		
		//Is there a normal pill at this node?
		if(pill != -1){
			if(game.isPillStillAvailable(pill)){
				nScore = 10;
			}
		}
		
		//Is there a powerPill at this node?
		if(pPill != -1){
			if (game.isPowerPillStillAvailable(pPill)){
				nScore = 50;
			}
		}
		
	//	System.out.println(pill + " " + pPill);
		return nScore;
	}
	
	/**
	 * This method find the current positions of each ghosts and finds
	 * the distance between them and Ms. Pac-man.
	 * If a ghost is deemed too close it checks whether that ghost is edible or not.
	 * If edible a flag is raised for the scoring method.
	 * @param game - A copy of the game state.
	 * @param pacNode - Ms. Pac-mans current position
	 * @returns - True if an inedible ghost is within minimum distance
	 *  or false if the ghost is edible.
	 */
	public boolean isGhostTooClose(Game game, int pacNode){
		int[] ghostNs = new int[4];
		GHOST[] ghosts = new GHOST[4];
		ghosts[0] = GHOST.BLINKY;
		ghosts[1] = GHOST.PINKY;
		ghosts[2] = GHOST.INKY;
		ghosts[3] = GHOST.SUE;
		
		//Gets the current nodes of all the ghosts 
		for(int i = 0; i < ghosts.length; i++){
			ghostNs[i] = game.getGhostCurrentNodeIndex(ghosts[i]);
			int dist = game.getShortestPathDistance(pacNode, ghostNs[i]);
			
			//Is a ghost too close.
			if(dist <= 7){
				gNode = ghostNs[i];
				
				//Is said ghost edible or not?
				if(game.isGhostEdible(ghosts[i])){
					return false;
				}
				else if(game.getGhostLairTime(ghosts[i]) > 0){
					return false;
				}
				else{
					return true;
				}
			}
		}
		
		return false;
	}
	
} 

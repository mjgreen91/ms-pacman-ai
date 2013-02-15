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
		int currentNode = game.getPacmanCurrentNodeIndex();
		int currentBest = currentNode;
		int bestScore = game.getScore();
		int posScore = 1000;
		MOVE[] posMoves = game.getPossibleMoves(currentNode, lastMove);
		GHOST[] ghosts = new GHOST[4];
			ghosts[0] = GHOST.BLINKY;
			ghosts[1] = GHOST.PINKY;
			ghosts[2] = GHOST.INKY;
			ghosts[3] = GHOST.SUE;
		boolean ghostAlert = false;
		int projectedScore = bestScore;
		//Evaluates all current possible moves and scores them.
			//Highest scoring becomes next move to make.
			for(int i = 0; i < posMoves.length; i++){
				int temp = game.getNeighbour(currentNode, posMoves[i]);
				int score = 0;
				int pill = game.getPillIndex(temp);
				int pPill = game.getPowerPillIndex(temp);
				
				//Determines how close the ghosts are to the node being evaluated.
				for(int j = 0; j < ghosts.length; j++){
					int gNode = game.getGhostCurrentNodeIndex(ghosts[j]);
					int dist = game.getManhattanDistance(temp, gNode);
					
					if(dist < 5 && game.isGhostEdible(ghosts[j]) == false){
						ghostAlert = true;
						currentBest = gNode;
					}
					else if(dist < 5 && game.isGhostEdible(ghosts[j]) == true){
						score = score + game.getGhostCurrentEdibleScore();
					}
				}
				
				if(pill != -1 && game.isPillStillAvailable(pill)){
					score = score + 10;
				}
				
				//Want clause in here so it doesn't chase powerpills if ghosts are already edible
				if(pPill != -1 && game.isPowerPillStillAvailable(pPill)){
					score = score + 50;
				}
				
				if(ghostAlert == true){
					myMove = game.getNextMoveAwayFromTarget(currentNode, currentBest, DM.MANHATTAN);
				}
				
				else{
					if(score > posScore){
					posScore = score;
					currentBest = temp;
					}
					myMove = game.getNextMoveTowardsTarget(currentNode, currentBest, lastMove, DM.MANHATTAN);
				}
			}
		return myMove;
	}

	
} 

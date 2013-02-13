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
		int posScore = 0;
		MOVE[] posMoves = game.getPossibleMoves(currentNode, lastMove);
		GHOST[] ghosts = new GHOST[4];
			ghosts[0] = GHOST.BLINKY;
			ghosts[1] = GHOST.PINKY;
			ghosts[2] = GHOST.INKY;
			ghosts[3] = GHOST.SUE;

			
			//Evaluates all current possible moves and scores them.
			//Highest scoring becomes next move to make.
			for(int i = 0; i < posMoves.length; i++){
				int temp = game.getNeighbour(currentNode, posMoves[i]);
				int score = 0;
				
				//Determines how close the ghosts are to the node being evaluated.
				for(int j = 0; j < ghosts.length; j++){
					int gNode = game.getGhostCurrentNodeIndex(ghosts[j]);
					int dist = game.getManhattanDistance(temp, gNode);
					
					if(dist < 5 && game.isGhostEdible(ghosts[j]) == false){
						score = score - 1000;
					}
					else if(dist < 5 && game.isGhostEdible(ghosts[j]) == true){
						score = score + game.getGhostCurrentEdibleScore();
					}
				}
				
				if(game.isPillStillAvailable(temp) == true){
					score = score + 10;
				}
				
				//Want clause in here so it doesn't chase powerpills if ghosts are already edible
				if(game.isPowerPillStillAvailable(temp) == true){
					score = score + 50;
				}
				
				if(score > posScore){
					posScore = score;
					currentBest = temp;
				}
			}
			System.out.println(currentBest);
			System.out.println(posScore);
			myMove = game.getNextMoveTowardsTarget(currentNode, currentBest, DM.MANHATTAN);
			currentBest = 0;
			posScore = 0;
			
			
		return myMove;
	}

	
} 
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
		int currentBest = 0;
		int[] neighbours = game.getNeighbouringNodes(currentNode);
		int check = 0;
		
		while(check != 40){			
		
			check ++;
			
			//Find the best node out of those.
			currentBest = evaluateNeighbours(game, neighbours);
			
			//Make the next move to be towards the best found node
			myMove = game.getNextMoveTowardsTarget(currentNode, currentBest, lastMove, DM.PATH);
			
			//get the neighbours of the best node.
			neighbours = game.getNeighbouringNodes(currentBest);
			
		}
		
		return myMove;
	}
	
	public int evaluateNeighbours(Game game, int[] neighbours){
		int bestFound = 0;
		int bestScore = 0;
		GHOST[] ghosts = new GHOST[4];
		ghosts[0] = GHOST.BLINKY;
		ghosts[1] = GHOST.PINKY;
		ghosts[2] = GHOST.INKY;
		ghosts[3] = GHOST.SUE;
		
		for(int i = 0; i < neighbours.length; i++){
			int temp = neighbours[i];
			int tempScore = 0;
			
			for(int j = 0; j < ghosts.length; j++){
				int ghost = game.getGhostCurrentNodeIndex(ghosts[j]);
				
					if(ghost == temp && game.isGhostEdible(ghosts[j]) != true){
						tempScore = tempScore - 1000;
				}
					else if(ghost == temp && game.isGhostEdible(ghosts[j]) == true){
						tempScore = tempScore + game.getGhostCurrentEdibleScore();
					}
			}
			
			if(game.isPillStillAvailable(temp) == true){
				tempScore = tempScore + 10;
			}
			
			if(game.isPowerPillStillAvailable(temp) == true){
				tempScore = tempScore + 50;
			}
			
			if(tempScore > bestScore){
				bestScore = tempScore;
				bestFound = temp;
			}
		}
		System.out.println(bestFound);
		System.out.println(bestScore);
		return bestFound;
	}
	
}
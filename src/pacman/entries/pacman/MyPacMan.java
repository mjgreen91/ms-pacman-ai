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
		int projectedScore = bestScore;
		MOVE[] posMoves = game.getPossibleMoves(currentNode, lastMove);
		GHOST[] ghosts = new GHOST[4];
			ghosts[0] = GHOST.BLINKY;
			ghosts[1] = GHOST.PINKY;
			ghosts[2] = GHOST.INKY;
			ghosts[3] = GHOST.SUE;
		boolean tooClose = false;
		GHOST closestG = null;
		int check = 0;
		
		while(check != 40){			
		//This is in place of a timer just now
			check ++;
			
			//Search through all possible moves.
			for(int i = 0; i < posMoves.length; i++){
				int temp = game.getNeighbour(currentNode, posMoves[i]);;
				int tempScore = 0;
			
			//Find the best move out of those.
				
				//Searches for ghosts in the current node
				for(int j = 0; j < ghosts.length; j++){
					int ghost = game.getGhostCurrentNodeIndex(ghosts[j]);
					
						if(ghost == temp && game.isGhostEdible(ghosts[j]) != true){
							tempScore = tempScore - 1000;
					}
						else if(ghost == temp && game.isGhostEdible(ghosts[j]) == true){
							tempScore = tempScore + game.getGhostCurrentEdibleScore();
						}
						
						else if(game.getDistance(currentNode, ghost, DM.PATH) < 10){
							tooClose = true;
							closestG = ghosts[j];
						}
				}
				//checks if node has a pill in it. 
				if(game.isPillStillAvailable(temp) == true){
					tempScore = tempScore + 10;
				}
				
				//Checks if there is a powerpill in the node.
				//Would like to add clause that it doesn't go for
				//powerpill if ghosts are still edible.
				if(game.isPowerPillStillAvailable(temp) == true){
					tempScore = tempScore + 50;
				}
				
				if(tempScore > projectedScore){
					projectedScore = tempScore;
					currentBest = temp;
				}
				
			}
			
			bestScore = projectedScore;
			System.out.println("Best node: " + currentBest);
			System.out.println("Best score: " + bestScore);
			
			//get the possible moves from the best node.
			posMoves = game.getPossibleMoves(currentBest);
			
		}
		//Make the next move to be towards the best found node
		//Or away from the closest ghost.
		if(tooClose == true){
			myMove = game.getNextMoveAwayFromTarget(currentNode, game.getGhostCurrentNodeIndex(closestG), DM.PATH);
		}
		else{
			myMove = game.getNextMoveTowardsTarget(currentNode, currentBest, lastMove, DM.PATH);
		}
		
		return myMove;
	}

	
} 
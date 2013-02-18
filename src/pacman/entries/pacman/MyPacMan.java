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
		int curNode = game.getPacmanCurrentNodeIndex();
		int curBest = curNode;
		int bestScore = game.getScore();
		MOVE[] posMoves = game.getPossibleMoves(curNode, lastMove);
		GHOST[] ghosts = new GHOST[4];
			ghosts[0] = GHOST.BLINKY;
			ghosts[1] = GHOST.PINKY;
			ghosts[2] = GHOST.INKY;
			ghosts[3] = GHOST.SUE;
		boolean ghostAlert = false;
		boolean atJunction = false;
		
		
		for(int i = 0; i < posMoves.length ;i ++){
			int score = 0;
			int searchNode = game.getNeighbour(curNode, posMoves[i]);
			
			while(atJunction != true){
				int pill = game.getPillIndex(searchNode);
				int pPill = game.getPowerPillIndex(searchNode);
				MOVE[] cornerCheck = game.getPossibleMoves(searchNode);

				//Are we at a junction? 
				//If yes cash up & compare scores
				if(game.isJunction(searchNode) == true){
					if(score > bestScore){
						bestScore = score;
						curBest = searchNode;
					}
					atJunction = true;
				}

				//Are we at a right-angle corner?
				else if(cornerCheck.length != 0){

					if(pill != -1 && game.isPillStillAvailable(pill)){
						score = score + 10;
					}

					else if(pPill != -1 && game.isPowerPillStillAvailable(pPill)){
						score = score + 50;
					}

					searchNode = game.getNeighbour(searchNode, cornerCheck[0]);
				}

				else{
					if(pill != -1 && game.isPillStillAvailable(pill)){
						score = score + 10;
					}

					else if(pPill != -1 && game.isPowerPillStillAvailable(pPill)){
						score = score + 50;
					}

					searchNode = game.getNeighbour(searchNode, lastMove);
				}
			}
		myMove = game.getNextMoveTowardsTarget(curNode, curBest, DM.PATH);
		}
		
/*				//Determines how close the ghosts are to the node being evaluated.
				for(int j = 0; j < ghosts.length; j++){
					int gNode = game.getGhostCurrentNodeIndex(ghosts[j]);
					int dist = game.getManhattanDistance(temp, gNode);
					
					if(dist < 5 && game.isGhostEdible(ghosts[j]) == false){
						ghostAlert = true;
						currentBest = gNode;
					} */
		return myMove;
	}

	
} 

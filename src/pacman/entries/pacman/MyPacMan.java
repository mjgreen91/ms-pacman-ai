package pacman.entries.pacman;

import java.lang.reflect.Array;
import java.util.Random;

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
		
		//Simple AI which randomly decides which direction to go every tick.
		//If a ghost gets too close it will try to move away from it.
		MOVE lastMove = game.getPacmanLastMoveMade();
		int currentNode = game.getPacmanCurrentNodeIndex();
		MOVE[] posMoves = game.getPossibleMoves(currentNode, lastMove);
		double closest = 10000;
		int ghostIndex = 5;
		GHOST[] ghosts = new GHOST[4];
		ghosts[0] = GHOST.BLINKY;
		ghosts[1] = GHOST.PINKY;
		ghosts[2] = GHOST.INKY;
		ghosts[3] = GHOST.SUE;
		
		//Find the closest ghost to ms pac-man
		for(int i=0; i<ghosts.length; i++){
			double temp = game.getDistance(currentNode, game.getGhostCurrentNodeIndex(ghosts[i]), DM.PATH);
			
			if(temp < closest){
				closest = temp;
				ghostIndex = i;
				System.out.println(closest);
			}
		}
		
		//If the closest ghost is too close then move away from it.
		//Otherwise pick a random move to make.
		if(closest <= 10 && closest != -1){
			myMove = game.getNextMoveAwayFromTarget(currentNode, ghostIndex, DM.PATH);
		}
		
		else{
			Random rg = new Random();
			int stop = rg.nextInt(posMoves.length);
			myMove = posMoves[stop];
		}
		
		
		return myMove;
	}
	
}
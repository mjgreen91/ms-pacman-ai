package pacman.entries.pacman;

import java.util.Random;

import pacman.controllers.Controller;
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
		//A very simple intial base. The controller determines possible moves
		//and randomly chooses one as it's next move. Going back in the direction
		//it came is a valid move.
		int currentNode = game.getPacmanCurrentNodeIndex();
		
		if(game.isJunction(currentNode)){
			MOVE[] pos = game.getPossibleMoves(currentNode);
			Random r = new Random();
			int stop = r.nextInt(pos.length);
			myMove = pos[stop];
			}
		
		else{
			myMove = game.getPacmanLastMoveMade();
		}
		return myMove;
	}
}
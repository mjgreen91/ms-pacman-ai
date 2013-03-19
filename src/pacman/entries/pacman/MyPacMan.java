package pacman.entries.pacman;

import java.util.ArrayList;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This agent heads towards the nearest pill it can find regardless of whether it is
 * a normal pill or a power pill. If a ghost gets too close to it, it will take evasive
 * actions until it is far enough away to resume hunting the nearest pill. 
 */
public class MyPacMan extends Controller<MOVE>
{
	private boolean nomTime = false;
	private MOVE myMove=MOVE.NEUTRAL;
	private int[] ghostNs = new int[4];
	private int gNode = 0;
	private MOVE[] ghostMoves = new MOVE[4];
	
	public MOVE getMove(Game game, long timeDue) 
	{
		MOVE lastMove = game.getPacmanLastMoveMade();
		int curNode = game.getPacmanCurrentNodeIndex();
		boolean ghostAlert;
		
		ghostAlert = isGhostTooClose(game, curNode);
		
		//Hunt ghosts if they are edible
	/*	if(nomTime == true){
			System.out.println("Chasing");
			myMove = game.getNextMoveTowardsTarget(curNode, gNode, lastMove, DM.PATH);
		} */
				
		//Find nearest pill
		if(ghostAlert == false){
			System.out.println("Moving to nearest pill.");
			int[] pills = game.getActivePillsIndices();
			myMove = game.getNextMoveTowardsTarget(curNode, game.getClosestNodeIndexFromNodeIndex(curNode,pills,DM.PATH), DM.PATH);
		}

		
		//Ghost is too close: RUN AWAY!
		else if(ghostAlert == true){
			ArrayList<MOVE> goodMoves = new ArrayList<MOVE>();
			MOVE[] posMoves = game.getPossibleMoves(curNode);
			for(int i=0; i < posMoves.length; i++){
				int neigh = game.getNeighbour(curNode, posMoves[i]);
				for(GHOST ghost: GHOST.values()){
					double d = game.getDistance(neigh, game.getGhostCurrentNodeIndex(ghost), DM.PATH);
					if(d > 7){
						goodMoves.add(posMoves[i]);
					}
				}
			}
			Random r = new Random();
			myMove = goodMoves.get(r.nextInt(goodMoves.size()));
			
		}
		
		return myMove;
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
		GHOST[] ghosts = new GHOST[4];
		ghosts[0] = GHOST.BLINKY;
		ghosts[1] = GHOST.PINKY;
		ghosts[2] = GHOST.INKY;
		ghosts[3] = GHOST.SUE;
		int[] gDists = new int[4];
		//Gets the current nodes of all the ghosts 
		for(int i = 0; i < ghosts.length; i++){
			ghostNs[i] = game.getGhostCurrentNodeIndex(ghosts[i]);
			gDists[i] = game.getShortestPathDistance(pacNode, ghostNs[i]);
			ghostMoves[i] = game.getGhostLastMoveMade(ghosts[i]);
			
			//Is the current ghost edible
			if(game.isGhostEdible(ghosts[i])){
				nomTime = true;
			}
			
			else if(game.wasGhostEaten(ghosts[i]) == false){
				nomTime = false;
			}
			
			//Is a ghost too close.
			if(gDists[i] <= 7){
				gNode = ghostNs[i];
				
				//Is the ghost in the lair?
				if(game.getGhostLairTime(ghosts[i]) > 0){
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

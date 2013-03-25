package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This agent heads towards the nearest pill it can find regardless of whether it is
 * a normal pill or a power pill. If a power pill is picked up it will actively hunt
 * the ghosts. If ghosts start getting too close it will try to find a power pill or
 * try to escape the ghost depending on how close it is. 
 */
public class MyPacMan extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;
	private int PERIMETER = 15;
	private int gNode = 0;
	private boolean nom = false;
	private int[] ghostNs = new int[4];
	
	public MOVE getMove(Game game, long timeDue) 
	{
		MOVE lastMove = game.getPacmanLastMoveMade();
		int curNode = game.getPacmanCurrentNodeIndex();
		boolean ghostAlert;
		
		ghostAlert = isGhostTooClose(game, curNode);
		
		//Hunt mode
		if(nom == true){
			//System.out.println("Chasing");
			myMove = game.getNextMoveTowardsTarget(curNode, gNode, DM.PATH);
			ghostAlert = isGhostTooClose(game, curNode);
		}
				
		//Nearest Pill mode
		if(ghostAlert == false && nom == false){
			//System.out.println("Moving to nearest pill.");
			int[] pills = game.getActivePillsIndices();
			myMove = game.getNextMoveTowardsTarget(curNode, game.getClosestNodeIndexFromNodeIndex(curNode,pills,DM.PATH), DM.PATH);
		}

		
		//Escape mode
		else if(ghostAlert == true){
			
			//Ghost is about to catch us, move away!
			if(game.getDistance(curNode, gNode, DM.PATH) < 10){
				//System.out.println("RUNNING AWAY!!");
				myMove = game.getNextMoveAwayFromTarget(curNode, gNode, DM.PATH);
			}
			//Find a pill and turn the tables!
			else{
				//System.out.println("MOAR POWER!");
				myMove = game.getNextMoveTowardsTarget(curNode, game.getClosestNodeIndexFromNodeIndex(curNode, game.getActivePowerPillsIndices(), DM.PATH), DM.PATH);
			}
		}
		
		return myMove;
	}

	
	
/**
 * This method gets the distance from Ms Pac-Man to
 * each of the ghosts. Each ghost is checked to see first if it is
 * edible and if not then has it crossed over the perimeter boundary. 
 * @param game - A copy of the game state.
 * @param pacNode - Ms Pac-Mans current node.
 * @returns true if a non-edible ghost has broken the perimeter
 * and false otherwise
 */
	public boolean isGhostTooClose(Game game, int pacNode){
		
		boolean run = false;
		int i = 0;
		
		for(GHOST ghost : GHOST.values()){
			//Add the ghosts current position to the array
			ghostNs[i] = game.getGhostCurrentNodeIndex(ghost);
			//Get the distance between Ms Pac-Man and the ghost.
			double dist = game.getDistance(pacNode, ghostNs[i], DM.PATH);
			i++;
			
			//Is the ghost edible?
			if(game.isGhostEdible(ghost)){
				gNode = game.getGhostCurrentNodeIndex(ghost);
				run = false;
				nom = true;
			}
			else if(game.isGhostEdible(ghost) == false){
				nom = false;
				//Has the ghost broken the perimeter?
				if(dist <= PERIMETER && game.getGhostLairTime(ghost) == 0){
					gNode = game.getGhostCurrentNodeIndex(ghost);
					run = true;
				}
			}
		}
		
		return run;
	}
	
} 

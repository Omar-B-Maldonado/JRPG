package main;

import java.util.Stack;

import javax.swing.JFrame;

import states.GameState;

public class GameStateManager 
{
	private Stack<GameState> stateStack;
	
	private GameStatePanel statePanel;
	
	public GameStateManager()
	{
		stateStack = new Stack<>();
		
		statePanel = Game.statePanel;
	}
	
	public void pushState(GameState state)
	{	
		state.init();
		
		stateStack.push(state);
		
		statePanel.setState(state);
	}
	
	public void popState()
	{
		if(!stateStack.isEmpty())
		{	
			stateStack.pop();
			
			//sets the statePanel's state to the one on top of the stack
			if(!stateStack.isEmpty()) statePanel.setState(stateStack.peek());
			
			else statePanel.setState(null);
		}
	}
	
	public void update()
	{
		//calls the top state's update method
		if(!stateStack.isEmpty()) stateStack.peek().update();
	}
	
	public GameState getPreviousState()
	{
		//if there are 2+ states in the stack return the index of the state underneath the top one
		if(stateStack.size() > 1) return(stateStack.get(stateStack.size() - 2));
		
		else return null;
	}
	
}

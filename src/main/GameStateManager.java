package main;

import java.awt.Graphics;
import java.util.Stack;

import javax.swing.JFrame;

public class GameStateManager 
{
	private Stack<GameState> stateStack;
	
	private GameStatePanel statePanel;
	
	public GameStateManager(GameStatePanel statePanel)
	{
		stateStack = new Stack<>();
		
		this.statePanel = statePanel;
	}
	
	//-----------------------------------------------------
	
	public void pushState(GameState state)
	{	
		state.init();
		
		statePanel.setState(state);
		
		stateStack.push(state);
	}
	
	public void popState()
	{
		if(!stateStack.isEmpty())
		{	
			stateStack.pop();
			
			if(!stateStack.isEmpty()) 
			{
				statePanel.setState(stateStack.peek());	//sets the state of the statePanel to the state that is on top of the stack
			}
			else	// if the state stack is empty
			{
				statePanel.setState(null);
			}
			
		}
	}
	//------------------------------------------------------
	
	public void update()
	{
		if(!stateStack.isEmpty())
		{
			stateStack.peek().update();	//calls the top state's update method
		}
	}

	//------------------------------------------------------
	
	public GameState getPreviousState()
	{
		if(stateStack.size() > 1)	//if there are 2 or more states in the stack
		{
			return(stateStack.get(stateStack.size() - 2));
		}
		
		else return null;
	}
	
	//------------------------------------------------------
	
}

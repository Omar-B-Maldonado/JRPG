package main;

import java.io.IOException;
import java.net.URL;
import java.io.File;
import javax.sound.sampled.*;

public class SoundManager 
{
	private Clip clip;
	private Clip musicClip;
	
    public void loadMusic(String musicFileName) 
    {
    	URL musicURL = getClass().getResource("/sounds/" + musicFileName);
    	try 
        {
            AudioInputStream music = AudioSystem.getAudioInputStream(musicURL);
            
            musicClip = AudioSystem.getClip();
            musicClip.open(music);
            
        } catch(Exception e) {e.printStackTrace();}
    }

    public void playMusic() 
    {
        if (!musicClip.isRunning()) musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopMusic() 
    {
        if (musicClip != null && musicClip.isRunning())  musicClip.stop();
    }
    
    public void setSound(String soundFileName)
	{
    	URL soundURL = getClass().getResource("/sounds/" + soundFileName);
    	try 
		{
			AudioInputStream sound = AudioSystem.getAudioInputStream(soundURL);
			clip = AudioSystem.getClip();
			clip.open(sound);
		}
		catch(Exception e) {e.printStackTrace();}			
	}

	public void play() 
	{
		clip.setFramePosition(0);;
		clip.start();
	}
}

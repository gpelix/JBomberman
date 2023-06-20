package app.jbomberman;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Deprecated(forRemoval = true)
public class AudioManager
{
	private static AudioManager instance;

	public static AudioManager getInstance()
	{
		if (instance == null)
			instance = new AudioManager();
		return instance;
	}

	private AudioManager()
	{
	}

	public void play(InputStream in)
	{

		try {
//			InputStream in = new BufferedInputStream(new FileInputStream(filename));
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/

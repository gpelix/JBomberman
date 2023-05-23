
package app.jbomberman;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

@Deprecated(forRemoval = true)
public class SoundPlayerUsingClip
{
    public static synchronized void playSound(final String url)
    {
        new Thread(new Runnable()
        {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run()
            {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            new File(url));
                    clip.open(inputStream);
                    clip.start();
                    clip.loop(4);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static final void main(String[] args) throws InterruptedException
    {
        playSound("/tmp/01 Title Screen.wav");
        playSound("/home/gp/java/jbomberman/src/main/resources/app/jbomberman/sounds/02 Stage Start.wav");
        Thread.sleep(49999);
    }
}

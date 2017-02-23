package scrountch.geom

import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

/**
 * NOT IMPLEMENTED! (this code does not work!)
 * @author bear amade
 */
// Date: 04/04/2016

class Sounds extends Thread {
    //class Sounds extends Thread implements I18NMethodNaming{
    AudioInputStream audioInputStream ;
    Clip clip;
    long frameLength
    boolean markSupported ;
    boolean initialized ;
    int numberRuns= -1;
    int numberFrames ;

    public Sounds(String urlDescription) {
        URL url = new URL(urlDescription)
        audioInputStream = AudioSystem.getAudioInputStream(url);
        clip = AudioSystem.getClip()
        markSupported = audioInputStream.markSupported()
        frameLength =  audioInputStream.getFrameLength()
        //println(frameLength)
        clip.open(audioInputStream)
        this.setDaemon(true)
        this.start()
    }

    public  void run() {
        while(true) {
            synchronized (this) {
                this.notify()
                if(numberFrames <0) {
                    return
                }
                if (numberRuns >= 0) {
                    doPlay(numberRuns, numberFrames)
                }
                this.wait()
            }
        }
    }

    public void play() {
        play(frameLength as int)
    }

    public void play(int frames) {
        play(0,frames)
    }

    public synchronized void play(int number, int frames) {
       numberRuns = number
        numberFrames = frames
        this.notify()
    }

    public synchronized void stopSound() {
        numberFrames = -1
        this.notify()
    }

    public void doPlay(int number, int frames) {
        println 'playing'
        int numFrames = Math.max(frames, frameLength)
        clip.setLoopPoints(0, frames)
        clip.loop(number)

    }

}

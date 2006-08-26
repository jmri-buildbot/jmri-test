package jmri.jmrit;

import java.applet.AudioClip;
import java.net.URL;
import java.net.MalformedURLException;

import javax.sound.sampled.*;

/**
 * Provide simple way to load and play sounds in JMRI.
 * <P>
 * This is placed in the jmri.jmrit package by process of
 * elimination.  It doesn't belong in the base jmri package, as
 * it's not a basic interface.  Nor is it a specific implementation
 * of a basic interface, which would put it in jmri.jmrix.  It seems
 * most like a "tool using JMRI", or perhaps a tool for use with JMRI,
 * so it was placed in jmri.jmrit.
 * <P>
 * S@see jmri.jmrit.sound
 *
 * @author	Bob Jacobsen  Copyright (C) 2004, 2006
 * @version	$Revision: 1.2 $
 */
public class Sound  {

    /*
     * Constructor takes the filename or URL, and
     * causes the sound to be loaded
     */
     public Sound(String filename) {
        loadingSound(filename);
     }

    /**
     * Play the sound once
     */
    public void play() {
        audioClip.play();
    }

    /**
     * Play the sound as a loop
     */
    public void loop() {
        audioClip.loop();
    }

    /**
     * Stop playing as a loop
     */
    public void stop() {
        audioClip.stop();
    }


    /**
     * Load the requested sound resource
     */
    void loadingSound(String filename) {
        try {
            // create a base URL for the sound file location
            URL url = new URL(jmri.util.FileUtil.getUrl(new java.io.File(filename)));

            // create a loader and start asynchronous sound loading
            audioClip = new sun.applet.AppletAudioClip(url);

        } catch (MalformedURLException e) {
            log.error("Error creating sound address: "+e.getMessage());
        }
    }

    /**
     * Play a sound from a buffer
     *
     */
    public static void playSoundBuffer(byte[] wavData) {

        // get characteristics from buffer
        float sampleRate = 11200.0f;
        int sampleSizeInBits = 8;
        int channels = 1;
        boolean signed = (sampleSizeInBits > 8 ? true : false);
        boolean bigEndian = true;
        
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        SourceDataLine line;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format); // format is an AudioFormat object
        if (!AudioSystem.isLineSupported(info)) {
            // Handle the error.
            log.error("line not supported: "+info);
            return;
        }
        // Obtain and open the line.
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
        } catch (LineUnavailableException ex) {
            // Handle the error.
            log.error("error opening line: "+ex);
            return;
        }    
        line.start();
        // write(byte[] b, int off, int len) 
        line.write(wavData,0,wavData.length);

    }


    /**
     * The actual sound, stored as an AudioClip
     */
    public AudioClip audioClip = null;

    public class WavBuffer {
        public WavBuffer(byte[] content) {
            buffer = content;
            
            // find fmt chunk and set offset
            int index = 12; // skip RIFF header
            while (index<buffer.length) {
                // new chunk
                if (buffer[index]==0x66 && 
                    buffer[index+1]==0x6D &&
                    buffer[index+2]==0x74 &&
                    buffer[index+3]==0x20) {
                        // found it
                        fmtOffset = index;
                        return;
                } else {
                    // skip
                    index = index + 8
                            +buffer[index+4]
                            +buffer[index+5]*256
                            +buffer[index+6]*256*256
                            +buffer[index+7]*256*256*256;
                    System.out.println("index now "+index);
                }
            }
            log.error("Didn't find fmt chunk");
            
        }
        
        int fmtOffset;
        
        byte[] buffer;
        
        float getSampleRate() {
            return 11200.0f;
        }
        int getSampleSizeInBits() {
            return 8;
        }
        int getChannels() {
            return 1;
        }
        boolean getBigEndian() {return false;}
        boolean getSigned() { return (getSampleSizeInBits()>8); }
    }

    static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(Sound.class.getName());
}

package managerMicro;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import javafx.scene.shape.Line;
import managerMicro.ThreadMicro;

public class ThreadMicro extends Thread {

	// record duration, in milliseconds
	static final long RECORD_TIME = 800; // 0.4s

	private Mixer mixer;
	private int ival;
	private String name;
	private File wavFile;
	private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
	private static TargetDataLine line;
	private Info[] infoTemp;
	private Info infocat;
	

	/**
	 * Defines an audio format
	 */
	AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 8;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		return format;
	}



	public ThreadMicro(Mixer m, int i) {
		System.out.println("\n\nInit de l'enregistrement n°"+i);
		this.mixer = m;
		this.ival = i;
		this.name = Integer.toString(i);
		this.wavFile = new File("C:/Users/Styblinski/Documents/Polytech4/iCreate/test" + i + ".wav");
		this.infoTemp = mixer.getTargetLineInfo();
		this.infocat = infoTemp[0];
		System.out.println(infocat);
		
	}
	
	
	public void start() {
		try {
			
			if (!AudioSystem.isLineSupported(infocat)) {
				System.out.println("Line not supported");
				System.exit(0);
			}
			
			//Timer
			Thread stopper = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(RECORD_TIME);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					ThreadMicro.finish();
				}
			});
			
			stopper.start();
			
			// Initialisation de la dataLine
			AudioFormat format = getAudioFormat();
			//DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			line = (TargetDataLine) AudioSystem.getLine(infocat);
			System.out.println("Le mixer actuel est "+ mixer);
			line.open(format);
			line.start(); // start capturing
			System.out.println("Micro "+ ival + " start capturing...");

			AudioInputStream ais = new AudioInputStream(line);

			System.out.println("Micro "+ ival + " start recording...");

			// start recording
			AudioSystem.write(ais, fileType, wavFile);
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		System.out.println("Enregistrement n°"+ival+" terminé");
	}

	static void finish() {
		line.stop();
		line.close();
		System.out.println("Finished");
	}


	public Mixer getMixer() {
		return this.mixer;
	}

}

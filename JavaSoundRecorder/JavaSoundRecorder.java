
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
import test3.implementation;

/**
 * A sample program is to demonstrate how to record sound in Java author:
 * www.codejava.net
 */
public class JavaSoundRecorder {
	
	
	int[] amplitudes;

	/**
	 * Captures the sound and record into a WAV file
	 */
	void start() {

		// Détermine parmi tous les périphériques, ceux d'entrées microphone
		List<Integer> mixerNeed = new ArrayList<Integer>();
		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		System.out.println("Nombre de devices : " + mixerInfo.length);
		for (int i = 0; i < mixerInfo.length; i++) {
			if (mixerInfo[i].getDescription().toLowerCase().contains("Capture".toLowerCase())) {
				mixerNeed.add(i);
			}
		}

		// Création d'un tableau du nombre de micros disponibles
		Mixer[] mixers = new Mixer[mixerNeed.size()];
		for (int j = 0; j < mixerNeed.size(); j++) {
			Mixer mixeri = AudioSystem.getMixer(mixerInfo[mixerNeed.get(j)]);
			mixers[j] = mixeri;
			int k = j + 1;
			System.out.println("le mixer" + k + " est : " + mixers[j]);
			System.out.println("associé au micro : " + mixerInfo[mixerNeed.get(j)].getName());
		}

		ThreadMicro[] threads = new ThreadMicro[mixers.length];
		// Création d'un tableau de thread
		for (int i = 0; i < mixers.length; i++) {
			threads[i] = new ThreadMicro(mixers[i], i);
		}
		
		for (int i = 0; i < mixers.length; i++) {
			threads[i].start();
		}
		
		
		int amplitudesMicro[] = new int[3];
		//Récupération des amplitudes
		for (int i = 0; i < mixers.length; i++) {
			amplitudesMicro[i]=(new implementation(i).main());
			System.out.println(amplitudesMicro[i]);
		}
		amplitudes=amplitudesMicro;
	}
	
	public int[] getAmplitudes() {
		return this.amplitudes;
	}
	

	public static void main(String[] args) {

		final JavaSoundRecorder recorder = new JavaSoundRecorder();

		// creates a new thread that waits for a specified
		// of time before stopping
		Thread stopper = new Thread(new Runnable() {
			public void run() {
				System.out.println("Appel du thread qui lance les threads");
				//recorder.finish();
			}
		});

		stopper.start();

		// start recording
		recorder.start();
	}
}
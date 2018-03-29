
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

/**
 * A sample program is to demonstrate how to record sound in Java author:
 * www.codejava.net
 */
public class JavaSoundRecorder {
	// record duration, in milliseconds
	static final long RECORD_TIME = 2000; // 5s

	// path of the wav file
	// File wavFile = new File("D:/Projets/iCreate/test.wav");
	File wavFile = new File("C:/Users/Styblinski/Documents/Polytech4/iCreate/test.wav");

	// format of audio file
	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

	// the line from which audio data is captured
	TargetDataLine line;

	/**
	 * Defines an audio format
	 */
	AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 8;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		return format;
	}

	/**
	 * Captures the sound and record into a WAV file
	 */
	void start() {

		try {
			Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
			System.out.println("Nombre de devices : " + mixerInfo.length);
			for (int i = 0; i < mixerInfo.length; i++) {
				if (mixerInfo[i].getDescription().toLowerCase().contains("Capture".toLowerCase())){
					System.out.println(mixerInfo[i].getVersion());
					System.out.println(mixerInfo[i].getName());
					System.out.println(mixerInfo[i].getDescription());
					System.out.println(mixerInfo[i].getVendor());
					System.out.println("\n");
				}
			}
			// pour chaque mixer on va regarder ses caracteristiques
			/*for (Mixer.Info a : mixerInfo) {

				System.out.println("\n\n" + a + " Description 666 :" + a.getDescription());

				System.out.println("--------------------------------");

				Mixer mix = AudioSystem.getMixer(a);

				// Visualisation dex controles support�s par chaque Mixer
				for (Control c : mix.getControls()) {
					System.out.println("Controls supported by Mixer:");
					System.out.println("\t" + c);
				}

				// Visualisation des SourceLine disponibles pour ce Mixer
				for (Line.Info i : mix.getSourceLineInfo()) {
					System.out.println("\n" + i);

					// Visualisation dex controles support�s par cette Line
					System.out.println("Controls supported by Source Line:");
					for (Control c : AudioSystem.getLine(i).getControls()) {
						System.out.println("\t" + c);
					}
				}
				// Visualisation des TargetLine disponibles pour ce Mixer
				for (Line.Info i : mix.getTargetLineInfo()) {
					System.out.println("\n" + i);

					// Visualisation dex controles support�s par cette Line
					System.out.println("Controls supported by Target Line:");
					for (Control c : AudioSystem.getLine(i).getControls()) {
						System.out.println("\t" + c);
					}
				}
			}*/

			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			System.out.println(info);

			// checks if system supports the data line
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line not supported");
				System.exit(0);
			}

			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start(); // start capturing

			System.out.println("Start capturing...");

			AudioInputStream ais = new AudioInputStream(line);

			System.out.println("Start recording...");

			// start recording
			AudioSystem.write(ais, fileType, wavFile);
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Closes the target data line to finish capturing and recording
	 */
	void finish() {
		line.stop();
		line.close();
		System.out.println("Finished");
	}

	/**
	 * Entry to run the program
	 */
	public static void main(String[] args) {
		final JavaSoundRecorder recorder = new JavaSoundRecorder();

		// creates a new thread that waits for a specified
		// of time before stopping
		Thread stopper = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(RECORD_TIME);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				recorder.finish();
			}
		});

		stopper.start();

		// start recording
		recorder.start();
	}
}
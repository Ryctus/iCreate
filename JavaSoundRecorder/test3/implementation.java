package test3;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.*;

public class implementation{
	public static void main(String[] args) {
		
		byte[] tab = fileToByteArray("C:/Users/Styblinski/Documents/Polytech4/iCreate/test.wav");
		System.out.println("la taille du tableau de Byte est "+ tab.length);
		int[] tab2 = new int[tab.length/8];
		System.out.println("la taille du tableau d'Int est "+ tab2.length);
	    for (int l = 0; l < tab.length/8; l++) {
	    	byte[] tabAux= {tab[4*l],tab[4*l+1],tab[4*l+2],tab[4*l+3],tab[4*l+4],tab[4*l+5],tab[4*l+6],tab[4*l+7]};
	    	tab2[l]=byteArrayToInt(tabAux);
	    }
		int max =0;
		
		for (int i=0;i<tab2.length;i++){
			if (max<tab2[i]){
				max = tab2[i];
				
			}
		}
		max-=2139190000;
		System.out.println("le pic est à : "+max);
		
		}
	
	public static int byteArrayToInt(byte[] b){
		   final ByteBuffer bb = ByteBuffer.wrap(b);
		   bb.order(ByteOrder.LITTLE_ENDIAN);
		   return bb.getInt();
	}
	
	
	public static byte[] fileToByteArray(String name){
	    Path path = Paths.get(name);
	    try {
	        return Files.readAllBytes(path);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
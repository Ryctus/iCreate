import processing.net.*;

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

String HTTP_GET_REQUEST = "GET /";
String HTTP_HEADER = "HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\r\n";

Server s;
Client c;
String input;

int compteurQuestion = 0;
int compteurReponse = 0;

int nombreQuestion = 3;
int nombreReponse = 2;

String currentQuestion = "q" + compteurQuestion;
String currentReponse = currentQuestion + "r" + compteurReponse;

int amplitude = 0;
int seuilSouffle = 3000000;
int seuilToquer = 1000000;
int seuilFrotter = 2000000;

JavaSoundRecorder jsr = new JavaSoundRecorder();
implementation amp = new implementation();


void setup() 
{
  s = new Server(this, 8080); // start server on http-alt
}

void draw() 
{
  //Receive data from client
    c = s.available();
    if (c != null) {
      input = c.readString();
      input = input.substring(0, input.indexOf("\n")); // Only up to the newline
      
      if (input.indexOf(HTTP_GET_REQUEST) == 0) // starts with ...
      {
          //s.write(HTTP_HEADER);  // answer that we're ok with the request and are gonna send html
          
          println("debut capture son");
          jsr.captureSound();
          amplitude = amp.amplitude();
          
          if(amplitude < seuilToquer){ // Question suivante
            s.write(currentQuestion);
            compteurQuestion = (compteurQuestion +1)%nombreQuestion;
            currentQuestion = "q"+compteurQuestion;
            compteurReponse = 0;
            currentReponse = currentQuestion + "r" + compteurReponse;
          } 
          
          else if (amplitude >= seuilToquer && amplitude < seuilFrotter) {
            s.write(currentReponse);
            println("Question envoyé : " + currentReponse);
            compteurReponse++;
            if(compteurReponse%nombreReponse==0){
              compteurQuestion++;
              currentQuestion = "q"+compteurQuestion%nombreQuestion;
              currentReponse = currentQuestion + "r0";
            }
            else{
              currentReponse = currentQuestion + "r" + compteurReponse%nombreReponse;
            }
          }
          
          else if (amplitude >= seuilFrotter && amplitude < seuilSouffle){ //Je frotter = Envoyer une réponse
            s.write("delair");
            compteurQuestion = 0;
            compteurReponse = 0;
            currentQuestion = "q"+compteurQuestion;
            currentReponse = currentQuestion + "r" + 0;
          } 
          
          else { // Je souffle = Répond à la question
            s.write("delaiq");
            compteurQuestion = 0;
            compteurReponse = 0;
            currentQuestion = "q"+compteurQuestion;
            currentReponse = currentQuestion + "r" + 0;
          }
          
          // some html
      
          // close connection to client, otherwise it's gonna wait forever
          
          
          c.stop();
 
      }
    }
}

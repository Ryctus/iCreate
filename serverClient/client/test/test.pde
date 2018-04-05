import processing.net.*;
import ddf.minim.*;


String HTTP_HEADER = "HTTP/1.0 200 OK\nContent-Type: text/html\n\n";
String data="q0";

Minim minim;
AudioPlayer player;

Client c;

PImage image;

boolean draw = true;


int nbQuestion = 0;
int nbReponse = 0;
String nameReponse = "reponse/r0";
String nameQuestion = "question/q0";
JavaSoundRecorder jsr = new JavaSoundRecorder();

void setup() {
    minim = new Minim(this);
    c = new Client(this, "127.0.0.1", 8080);
    c.write("GET / HTTP/1.0\n");  // Use the HTTP "GET" command to ask for a webpage
    size(1920,1080);
    image = loadImage(data+".jpg");
    image(image,0,0);
    println("data + jpg " + data+".jpg");
    draw = true;
}

void refresh(){
  if (c.available() > 0) {    // If there's incoming data from the client...
    data = c.readString();   // ...then grab it and print it 
    println(data);
  }
}

void lecture(){
    println(data);
    player = minim.loadFile(data+".mp3");
    player.play();
    int length = player.length(), position = 0;
    while(position <  length){
    position +=1;
      delay(1);
    }
    player.pause();
    data = "tuto";
    draw = true;
    redraw();
    setup(); 
}

void draw() {
  if (c.available() > 0) {
    if(draw){
      refresh();
      size(1920,1080);
      image = loadImage(data+".jpg");
      image(image,0,0);
      draw = false;
      return;
    }
  }
  if(!draw){
    if(data.equals("delair")){ // enregistre une réponse
      data = "valide";
      jsr.captureSound(nameReponse);
      nbReponse++;
      nameReponse = "reponse/r" + nbReponse;
      draw = true;
      redraw();
      setup();
      return;
    }
    else if (data.equals("delaiq")) { //enregistre une question
      data = "valide";
      jsr.captureSound(nameQuestion);
      nbQuestion++;
      nameQuestion = "question/q" + nbQuestion;
      draw = true;
      redraw();
      setup();
      return;
    }
    else if(data.equals("valide")){
      setup();
      return;
    }
    else {
      lecture();
    }
  }
}

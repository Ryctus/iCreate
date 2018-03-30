import processing.net.*;
import ddf.minim.*;

String HTTP_HEADER = "HTTP/1.0 200 OK\nContent-Type: text/html\n\n";
String data;

Minim minim;
AudioPlayer player;

Client c;

void setup() {
    minim = new Minim(this);
    c = new Client(this, "10.6.78.105", 8080);
    c.write("GET / HTTP/1.0\n");  // Use the HTTP "GET" command to ask for a webpage
}

void draw() {
  while(true){
    if (c.available() > 0) {    // If there's incoming data from the client...
      data = c.readString();   // ...then grab it and print it 
      println(data);
      player = minim.loadFile(data+".mp3");
      player.play();
      int length = player.length(), position = 0;
      while(position <  length){
        position +=1;
        delay(1);
      }
      player.pause();
      
      setup();
      delay(1);
      
    }
  }
}

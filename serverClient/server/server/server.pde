import processing.net.*;

String HTTP_GET_REQUEST = "GET /";
String HTTP_HEADER = "HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\r\n";

Server s;
Client c;
String input;

int compteurQuestion = 0;
int compteurReponse = 0;

int nombreQuestion = 2;
int nombreReponse = 2;

String currentQuestion = "q" + compteurQuestion;
String currentReponse = currentQuestion + "r" + compteurReponse;

String lastSend = null;


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
        s.write(HTTP_HEADER);  // answer that we're ok with the request and are gonna send html
    
        if(lastSend == null){
          s.write(currentQuestion);
          lastSend = currentQuestion;
        }
        else {
          s.write(currentReponse);
          compteurReponse++;
          if(compteurReponse%nombreReponse==0){
            lastSend = null;
            compteurQuestion++;
            currentQuestion = "q"+compteurQuestion%nombreQuestion;
            currentReponse = currentQuestion + "r0";
          }
          else{
            currentReponse = currentQuestion + "r" + compteurReponse%nombreReponse;
          }
          
        }
        // some html
    
        // close connection to client, otherwise it's gonna wait forever
        
        
        c.stop();
 
      }
    }
}

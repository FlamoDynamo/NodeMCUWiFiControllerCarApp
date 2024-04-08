#define ENA   14          // Enable/speed motors Right        GPIO14(D5)
#define ENB   12          // Enable/speed motors Left         GPIO12(D6)
#define IN_1  15          // L298N in1 motors Right           GPIO15(D8)
#define IN_2  13          // L298N in2 motors Right           GPIO13(D7)
#define IN_3  2           // L298N in3 motors Left            GPIO2(D4)
#define IN_4  0           // L298N in4 motors Left            GPIO0(D3)

#include <ESP8266WiFi.h>
#include <WiFiClient.h> 
#include <ESP8266WebServer.h>

String command;             //String to store app command state.
int speedCar = 255;         // 400 - 1023.

const char* ssid = "NodeMCU Car";
ESP8266WebServer server(80);

void setup() {
 
 pinMode(ENA, OUTPUT);
 pinMode(ENB, OUTPUT);  
 pinMode(IN_1, OUTPUT);
 pinMode(IN_2, OUTPUT);
 pinMode(IN_3, OUTPUT);
 pinMode(IN_4, OUTPUT); 
  
  Serial.begin(115200);
  
  // Connecting WiFi
  WiFi.mode(WIFI_AP);
  WiFi.softAP(ssid);

  IPAddress myIP = WiFi.softAPIP();
  Serial.print("AP IP address: ");
  Serial.println(myIP);
 
 // Starting WEB-server 
  server.on("/", HTTP_handleRoot);
  server.onNotFound(HTTP_handleRoot);
  server.begin();    
}

void goAhead(){ 
  digitalWrite(IN_1, LOW);
  digitalWrite(IN_2, HIGH);
  analogWrite(ENA, speedCar);

  digitalWrite(IN_3, LOW);
  digitalWrite(IN_4, HIGH);
  analogWrite(ENB, speedCar);
}

void goBack(){ 
  digitalWrite(IN_1, HIGH);
  digitalWrite(IN_2, LOW);
  analogWrite(ENA, speedCar);

  digitalWrite(IN_3, HIGH);
  digitalWrite(IN_4, LOW);
  analogWrite(ENB, speedCar);
}

void goRight(){ 
  digitalWrite(IN_1, HIGH);
  digitalWrite(IN_2, LOW);
  analogWrite(ENA, speedCar);

  digitalWrite(IN_3, LOW);
  digitalWrite(IN_4, HIGH);
  analogWrite(ENB, speedCar);
}

void goLeft(){
  digitalWrite(IN_1, LOW);
  digitalWrite(IN_2, HIGH);
  analogWrite(ENA, speedCar);

  digitalWrite(IN_3, HIGH);
  digitalWrite(IN_4, LOW);
  analogWrite(ENB, speedCar);
}

void stopRobot(){  
  digitalWrite(IN_1, LOW);
  digitalWrite(IN_2, LOW);
  analogWrite(ENA, speedCar);

  digitalWrite(IN_3, LOW);
  digitalWrite(IN_4, LOW);
  analogWrite(ENB, speedCar);
}

void loop() {
  server.handleClient();
      
  // Nhận dữ liệu từ ứng dụng Android
  if (server.hasArg("speed")) {
    int newSpeed = server.arg("speed").toInt();
    if (newSpeed >= 0 && newSpeed <= 255) {
      speedCar = newSpeed;
    } else {
      // Nếu tốc độ nằm ngoài phạm vi hợp lệ, giữ nguyên giá trị cũ
      Serial.println("Invalid speed value: " + server.arg("speed"));
    }
  }
  
  // Nhận dữ liệu từ joystick
  if (server.hasArg("x") && server.hasArg("y")) {
    float xPercent = server.arg("x").toFloat();
    float yPercent = server.arg("y").toFloat();

    // Xử lý dữ liệu từ joystick để điều khiển xe
    if (xPercent > 0 && yPercent > 0) {
      goAhead();
    } else if (xPercent < 0 && yPercent > 0) {
      goLeft();
    } else if (xPercent > 0 && yPercent < 0) {
      goRight();
    } else if (xPercent < 0 && yPercent < 0) {
      goBack();
    } else {
      stopRobot();
    }
  }
}

void HTTP_handleRoot(void) {
  if (server.hasArg("speed") || server.hasArg("x") || server.hasArg("y")) {
    Serial.println(server.arg("speed") + ", " + server.arg("x") + ", " + server.arg("y"));
  }
  server.send(200, "text/html", "");
  delay(1);
}

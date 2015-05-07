# havissIoT

This project is just for fun and for learning more about IoT. 
This repository will in the beginning be used just for the purpose of having my code available on all my computers - most code wont work

Plan:
- Connect different devices (on different platforms) together using the internet and MQTT.
- Create a server application to analyze the data coming from the devices and perform actions based on these data
- Store the most valuable data in a database - MongoDB
- Access the data from other devices and/or a web page.

Planned clients:
- Arduino with WiFi shield
- Raspberry PI - https://github.com/HaavardM/havissIoT-RPI
- Intel Galileo running Windows
- Computer

MQTT:
- http://mqtt.org/
- I use the Eclipse Paho libraries - https://eclipse.org/paho/
- I use the mosquitto server/broker - http://mosquitto.org/

MongoDB:
- http://www.mongodb.org/
- I use the standard mongodb driver for java

Other information:
- Im not planning to create my own MQTT broker. The "server" side of the project is just an application for analyzing the data. 
- I have very limited programming experience. So please dont be to harsh when my code is bad.. :)
- The server-side application will be written in java
- The clients will be written in the language most appropiate for the platform (C/C++, java, etc)
- Maven will be used for java applications



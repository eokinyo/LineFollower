import lejos.ev3.*; // Importing necessary EV3 classes

import lejos.hardware.Sound; // Importing Sound class for audio feedback
import lejos.hardware.lcd.LCD; // Importing LCD class for displaying messages
import lejos.hardware.port.Port; // Importing Port class for specifying sensor port
import lejos.hardware.port.SensorPort; // Importing SensorPort class for sensor ports
import lejos.hardware.sensor.EV3UltrasonicSensor; // Importing EV3UltrasonicSensor class for ultrasonic sensor
import lejos.robotics.SampleProvider; // Importing SampleProvider class for sensor data retrieval

public class ObstacleDetector extends Thread { // Declaring ObstacleDetector class extending Thread

    private DataExchange DEObj; // Declaring instance variable for DataExchange
    private EV3UltrasonicSensor usSensor; // Declaring instance variable for ultrasonic sensor
    private SampleProvider distanceProvider; // Declaring instance variable for sensor data provider

    private final int securityDistance = 25; // Declaring and initializing security distance threshold

    public ObstacleDetector(DataExchange DE) { // Constructor initializing sensors
        DEObj = DE; // Assigning DataExchange object to instance variable
        Port port = SensorPort.S1; // Specifying the sensor port
        usSensor = new EV3UltrasonicSensor(port); // Initializing ultrasonic sensor on specified port
        distanceProvider = usSensor.getDistanceMode(); // Obtaining distance measurement provider
    }

    public void run() { // Run method controlling obstacle detection behavior
        float[] sample = new float[distanceProvider.sampleSize()]; // Array to store sensor readings
        while (true) { // Infinite loop for continuous operation
            distanceProvider.fetchSample(sample, 0); // Fetching distance sample data

            float distance = sample[0] * 100; // Converting distance to centimeters

            if (distance > securityDistance) { // Checking if distance is greater than security threshold
                DEObj.setCMD(1); // Sending command indicating safe distance
            } else { // If distance is less than or equal to security threshold
                DEObj.setCMD(0); // Sending command indicating obstacle proximity
                // Displaying "Object found!" message on LCD
                LCD.drawString("Object found!", 0, 1);
                LCD.refresh(); // Refreshing LCD display
                Sound.twoBeeps(); // Producing audio alert
                Sound.twoBeeps(); // Producing audio alert
            }
        }
    }
}

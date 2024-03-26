import lejos.ev3.*; // Importing necessary EV3 classes
import lejos.hardware.lcd.LCD; // Importing LCD class
import lejos.hardware.motor.Motor; // Importing Motor class
import lejos.hardware.port.SensorPort; // Importing SensorPort class
import lejos.hardware.sensor.EV3ColorSensor; // Importing EV3ColorSensor class
import lejos.hardware.sensor.EV3UltrasonicSensor; // Importing EV3UltrasonicSensor class
import lejos.robotics.SampleProvider; // Importing SampleProvider class

public class LineFollower extends Thread { // Declaring LineFollower class extending Thread
    private DataExchange DEObj; // Declaring instance variable for DataExchange
    private EV3ColorSensor ss; // Declaring instance variable for color sensor
    private EV3UltrasonicSensor us; // Declaring instance variable for ultrasonic sensor
    private final double colorPattern = 0.27; // Declaring and initializing color threshold

    public LineFollower(DataExchange DE) { // Constructor initializing sensors
        DEObj = DE;
        ss = new EV3ColorSensor(SensorPort.S3); // Initializing color sensor on port S3
        us = new EV3UltrasonicSensor(SensorPort.S2); // Initializing ultrasonic sensor on port S2
        //ss.setFloodlight(true); // Turning on floodlight for color sensor
    }

    public void run() { // Run method to control robot behavior
    	
    	final SampleProvider sp = ss.getRedMode();
        while (true) { // Infinite loop for continuous operation
            if (DEObj.getCMD() == 1) { // Checking if command is received
                float[] sample = new float[sp.sampleSize()]; // Array to store sensor readings
                sp.fetchSample(sample, 0); // Fetching color sensor sample data

                if (sample[0] < colorPattern) { // Checking if color sensor reading is below threshold
                    // Setting motor speeds and making the robot move forward
                    Motor.B.setSpeed(130);
                    Motor.A.setSpeed(260);
                    Motor.B.forward();
                    Motor.A.forward();
                } else { // If color sensor reading is above threshold
                    // Setting motor speeds and making the robot move forward
                    Motor.B.setSpeed(260);
                    Motor.A.setSpeed(130);
                    Motor.B.forward();
                    Motor.A.forward();
                }
                // Displaying color sensor reading on LCD
                LCD.drawString("Color Value: " + sample[0], 0, 0);
                LCD.refresh(); // Refreshing LCD display
            } else { // If command is not received
                // Stopping the motors
                Motor.B.stop();
                Motor.A.stop();
            }
        }
    }
}

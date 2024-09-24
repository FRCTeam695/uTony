// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.DoubleArrayTopic;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.StringTopic;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDSubsystem extends SubsystemBase {
  /** Creates a new LEDSubsystem. */
  AddressableLED LED;
  AddressableLEDBuffer LED_Buffer;
  Servo m_Servo;
  DoubleSupplier axis;

  NetworkTableInstance inst = NetworkTableInstance.getDefault();
  NetworkTable table = inst.getTable("LEDs");
  DoubleArrayTopic colorTopic = table.getDoubleArrayTopic("Color");

  final DoubleArrayPublisher colorPub = colorTopic.publish();

  public LEDSubsystem(DoubleSupplier axis) {
    LED = new AddressableLED(1);
    LED_Buffer = new AddressableLEDBuffer(5);
    LED.setLength(LED_Buffer.getLength());
    this.axis = axis;
    m_Servo = new Servo(0);
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void LEDInit(int r, int g, int b){
    for (int i = 0; i < LED_Buffer.getLength(); i++) {
      // Sets the specified LED to the RGB values for red
      LED_Buffer.setRGB(i, r, g, b);
      
  }
  colorPub.set(new double[] {r,g,b});
  LED.setData(LED_Buffer);
  LED.start();
}
  public void LEDExecute(int r, int g, int b){
    for (int i = 0; i < LED_Buffer.getLength(); i++) {
      // Sets the specified LED to the RGB values for red
      LED_Buffer.setRGB(i, r, g, b);
      
  }
  colorPub.set(new double[] {r,g,b});
  LED.setData(LED_Buffer);
  LED.start();
}
  public void LEDEnd(int r, int g, int b){
    for (int i = 0; i < LED_Buffer.getLength(); i++) {
      // Sets the specified LED to the RGB values for red
      LED_Buffer.setRGB(i, r, g, b);
      
  }
  colorPub.set(new double[] {r,g,b});
  LED.setData(LED_Buffer);
  LED.start();
}


  
  public final Command setColor(int r, int g, int b){ 
    // return new FunctionalCommand(()-> {LEDInit(r,g,b); System.out.println("Yes");} , 
    // ()-> {LEDExecute(r,g,b);},
    // interrupted -> {LEDEnd(r, g, b);} ,
    //  () -> true, 
    //  this);
    return new InstantCommand(() -> {LEDInit(r,g,b);}, this);
  }

  public void turnOnSomeLEDs(double doub,int r,int g,int b){
    int numLED = (int)(Math.abs((doub)*5)); //0-5
    for(int i = 0; i < numLED && i < LED_Buffer.getLength(); i++){
      LED_Buffer.setRGB(i, r, g, b);

    } 
    for(int i = numLED; i < LED_Buffer.getLength();i++){
      LED_Buffer.setRGB(i, 0, 0, 0);
    }
    colorPub.set(new double[] {r,g,b});
    LED.setData(LED_Buffer);
    LED.start();
  }

  public final Command setServo(DoubleSupplier speed){
    return new FunctionalCommand(()-> {},
     () -> {
      m_Servo.set((speed.getAsDouble()+1)/2);
      if(speed.getAsDouble() < 0){
        turnOnSomeLEDs(speed.getAsDouble(),0,(int)((speed.getAsDouble())*-255),0);
      } else{
        turnOnSomeLEDs(speed.getAsDouble(), (int)((speed.getAsDouble())*255),0,0);
      }
    }, 
     interrupted -> {}, 
     ()->false, 
     this);
  }
  


      // if(axis.getAsDouble() > .5){ 
      //   for (int i = 0; i < LED_Buffer.getLength(); i++) {
      // // Sets the specified LED to the RGB values for red
      //     LED_Buffer.setRGB(i,0,(int)(axis.getAsDouble()+1)/2*255,0);
      //   }
      // } else{
      //   for (int i = 0; i < LED_Buffer.getLength(); i++) {
      // // Sets the specified LED to the RGB values for red
      //     LED_Buffer.setRGB(i,(int)(axis.getAsDouble()+1)/2*255,0,0);
      //   }
      // }
      
    
  }
  


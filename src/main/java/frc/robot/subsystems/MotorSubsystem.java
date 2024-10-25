// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import javax.swing.GroupLayout.ParallelGroup;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import static edu.wpi.first.wpilibj2.command.Commands.*;

public class MotorSubsystem extends SubsystemBase {
  /** Creates a new CansparkMax. */
  CANSparkFlex motor1 = new CANSparkFlex(56, MotorType.kBrushless);
  RelativeEncoder encoder1 = motor1.getEncoder();
  DigitalInput limitSwitch = new DigitalInput(0);
  PIDController positionPID = new PIDController(.01,0, 0);
  double kP = 0.0002;
  double kI = 2 * Math.pow(10, -6 );
  double kD = .001;
  SparkPIDController m_motorVelocityPID = motor1.getPIDController();
  public MotorSubsystem() {
    motor1.setIdleMode(IdleMode.kBrake);  
    m_motorVelocityPID.setP(kP);
    m_motorVelocityPID.setI(kI);
    m_motorVelocityPID.setD(kD);
    SmartDashboard.putNumber("kP", kP);
    SmartDashboard.putNumber("kI", kI);
    SmartDashboard.putNumber("kD", kD);
    m_motorVelocityPID.setIZone(0);
  }

  public Command runMotorVelocityPIDTEST(DoubleSupplier velocity){
    return new FunctionalCommand(
      () -> {
        SmartDashboard.putNumber("kP", kP);
        SmartDashboard.putNumber("kI", kI);
        SmartDashboard.putNumber("kD", kD);
      }, 
      () -> {

        m_motorVelocityPID.setReference(velocity.getAsDouble()*400, CANSparkBase.ControlType.kVelocity);
      SmartDashboard.putNumber("setpoint",velocity.getAsDouble()*400);
    
    }, 
      interupted -> {m_motorVelocityPID.setReference(0, CANSparkBase.ControlType.kVelocity);}, 
      ()-> false, 
      this);
  }
  public Command runMotorVelocity(DoubleSupplier velocity){
    return new FunctionalCommand(
      () -> {m_motorVelocityPID.setReference(velocity.getAsDouble()*400, CANSparkBase.ControlType.kVelocity);
       SmartDashboard.putNumber("setpoint",velocity.getAsDouble()*400);}, 
      () -> {m_motorVelocityPID.setReference(velocity.getAsDouble()*400, CANSparkBase.ControlType.kVelocity);
      SmartDashboard.putNumber("setpoint",velocity.getAsDouble()*400);}, 
      interupted -> {m_motorVelocityPID.setReference(0, CANSparkBase.ControlType.kVelocity);}, 
      ()-> false, 
      this);
  }

  public Command runMotorPosition(DoubleSupplier position){
    return new FunctionalCommand(
      () -> {
        m_motorVelocityPID.setReference(positionPID.calculate(encoder1.getPosition(), position.getAsDouble()),
         CANSparkBase.ControlType.kVelocity);}, 
      () -> {
        m_motorVelocityPID.setReference(positionPID.calculate(encoder1.getPosition(), position.getAsDouble()),
         CANSparkBase.ControlType.kVelocity);}, 
      interupted -> {m_motorVelocityPID.setReference(0,
         CANSparkBase.ControlType.kVelocity);}, 
      () -> false, 
      this);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("speed",encoder1.getVelocity()); 
    // if(Math.abs(kP - SmartDashboard.getNumber("kP", -1)) < .00000000001){
    //   SmartDashboard.putNumber("kP", kP);
    // }else{
    // kP = SmartDashboard.getNumber("kP",-1);
    // }
    // if(Math.abs(kD - SmartDashboard.getNumber("kD", -1)) < .00000000001){
    //   SmartDashboard.putNumber("kD", kD);
    // }else{
    // kD = SmartDashboard.getNumber("kD",-1);
    // }
    // if(Math.abs(kI - SmartDashboard.getNumber("kI", -1)) < .00000000001){
    //   SmartDashboard.putNumber("kI", kI);
    // }else{
    // kI = SmartDashboard.getNumber("kI",-1);
    // }
    // SmartDashboard.putNumber("basdfasdf", kD);
  
        //  System.out.print(("")+ kP + kI + kD);
        
        
  }
  public Command runMotorUntilLimitSwitch(DoubleSupplier speed){
    double timeOut = 10;
    FunctionalCommand cmd = new FunctionalCommand(
      () -> {motor1.set(speed.getAsDouble()/3);}, 
      () -> {motor1.set(speed.getAsDouble()/3);}, 
      interupted -> {motor1.set(0);}, 
      () -> !limitSwitch.get(), 
      this);
    return race(cmd, new WaitCommand(timeOut));
  }
}

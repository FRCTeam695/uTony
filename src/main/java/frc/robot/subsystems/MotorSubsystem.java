// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import javax.swing.GroupLayout.ParallelGroup;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

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
  public MotorSubsystem() {
    motor1.setIdleMode(IdleMode.kBrake);
  }

  public Command runMotor(DoubleSupplier speed){
    return new FunctionalCommand(
      () -> {motor1.set(speed.getAsDouble()/3);}, 
      () -> {motor1.set(speed.getAsDouble()/3);}, 
      interupted -> {motor1.set(0);}, 
      () -> false, 
      this);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("random", Math.random());
    SmartDashboard.putNumber("random2", Math.random());
    SmartDashboard.putNumber("random3", Math.random());
    SmartDashboard.putBoolean("switch", limitSwitch.get());
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

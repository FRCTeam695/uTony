// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MotorSubsystem extends SubsystemBase {
  /** Creates a new CansparkMax. */
  CANSparkFlex motor1 = new CANSparkFlex(56, MotorType.kBrushless);
  RelativeEncoder encoder1 = motor1.getEncoder();
  public MotorSubsystem() {}

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
  }
}

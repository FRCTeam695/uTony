// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import org.opencv.dnn.Model;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class DriveTrainSubsystem extends SubsystemBase {

  CANSparkMax motor_L1 = new CANSparkMax(12, MotorType.kBrushless);
  CANSparkMax motor_L2 = new CANSparkMax(10, MotorType.kBrushless);
  CANSparkMax motor_R1 = new CANSparkMax(13, MotorType.kBrushless);
  CANSparkMax motor_R2 = new CANSparkMax(11, MotorType.kBrushless);
     
  RelativeEncoder encoder_L1 = motor_L1.getEncoder();
  RelativeEncoder encoder_L2 = motor_L2.getEncoder();
  RelativeEncoder encoder_R1 = motor_R1.getEncoder();
  RelativeEncoder encoder_R2 = motor_R2.getEncoder();

  DifferentialDrive driveTrain;
  /** Creates a new Drivetrain. */
  public DriveTrainSubsystem() {
    motor_L1.setInverted(true);
    motor_L2.follow(motor_L1);
    motor_R2.follow(motor_R1);
    driveTrain = new DifferentialDrive(motor_L1, motor_R1);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public Command driveTank(DoubleSupplier left, DoubleSupplier right){
    return run(
      ()->{
        driveTrain.tankDrive(left.getAsDouble(), right.getAsDouble());
      }
    );
  }
  public Command driveArcade(DoubleSupplier speed, DoubleSupplier turn){
    return run(
      ()->{
        driveTrain.arcadeDrive(speed.getAsDouble(), turn.getAsDouble());  
      }
    );
  }
  public Command driveCurvature(DoubleSupplier speed, DoubleSupplier turn,JoystickButton turnInPlace){
    return run(
      ()->{
        driveTrain.curvatureDrive(speed.getAsDouble(), turn.getAsDouble(), turnInPlace.getAsBoolean());  
      }
    );
  }
}

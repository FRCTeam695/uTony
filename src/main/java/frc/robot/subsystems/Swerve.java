// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Swerve extends SubsystemBase {
  /** Creates a new Swerve. */
  //motors
  final TalonFX m_lin = new TalonFX(0, "rio");
  final TalonFX m_rot = new TalonFX(0, "rio");
  final DutyCycleOut m_linReq = new DutyCycleOut(0.0);
  final DutyCycleOut m_rotReq = new DutyCycleOut(0.0);

  //pid controllers
  final PIDController m_lin_PID = new PIDController(0, 0, 0); //speed (m/s)
  final PIDController m_rot_PID = new PIDController(0, 0, 0); //rotation (rad)

  //constant
  final Double MAX_SPEED_OF_MOTOR = 1.0;//REPLACE WITH m/s

  //controller inputs
  DoubleSupplier FWD_ctr;
  DoubleSupplier STR_ctr;
  DoubleSupplier ROT_ctr;

  //motor vector values
  double runAngle;//radians
  double runSpeed;//speed (m/s)

  public Swerve(DoubleSupplier FWD, DoubleSupplier STR, DoubleSupplier ROT) {
    this.FWD_ctr = FWD;
    this.STR_ctr = STR;
    this.ROT_ctr = ROT;
    runAngle = 0;
    runSpeed = 0;
    m_rot_PID.enableContinuousInput(-Math.PI, Math.PI);
  }
  public void resetClass(){
    //to implement
  }

  private void calculateMotorDrive(){
    //init power vector
    double m_STR = 0;
    double m_FWD = 0;

    //FWD and STR contribution
    m_STR = STR_ctr.getAsDouble();
    m_FWD = FWD_ctr.getAsDouble();

    //ROT Contribution
    double ROT_STR = ROT_ctr.getAsDouble() * Math.cos(135*Math.PI*180);
    double ROT_FWD = ROT_ctr.getAsDouble() * Math.sin(135*Math.PI*180);

    m_STR += ROT_STR;
    m_FWD += ROT_FWD;

    //normalize vector if greater than max
    double magnitude = Math.sqrt(m_FWD*m_FWD + m_STR*m_STR);
    if(magnitude > 1){
      m_STR /= magnitude;
      m_FWD /= magnitude;
    }

    //Multiply by max speed
    m_STR *= MAX_SPEED_OF_MOTOR;
    m_FWD *= MAX_SPEED_OF_MOTOR;

    //convert to angle and magnitude
    runAngle = 0; //CHANGE *******
    runSpeed = Math.sqrt(m_FWD*m_FWD + m_STR*m_STR);
  }

  public Command runSwerve(){
    return new FunctionalCommand(
      () -> {resetClass();},
      () -> {
        //main motor vloop
        calculateMotorDrive();
        m_rot_PID.setSetpoint(runAngle);
        m_lin_PID.setSetpoint(runSpeed);
      }, 
      interrupted -> {},
      () -> true,
      this
    );
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

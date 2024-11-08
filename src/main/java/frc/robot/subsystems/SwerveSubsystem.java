// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveSubsystem extends SubsystemBase {
  /** Creates a new Swerve. */
  //motors
  final TalonFX m_lin = new TalonFX(13, "rio");
  final TalonFX m_rot = new TalonFX(12, "rio");
  final DutyCycleOut m_linReq = new DutyCycleOut(0.0);
  final DutyCycleOut m_rotReq = new DutyCycleOut(0.0);
  final CANcoder m_rotEncoder = new CANcoder(11);

  //pid controllers
  final PIDController m_lin_PID = new PIDController(.001, 0, 0); //speed (m/s)
  final PIDController m_rot_PID = new PIDController(.3, 0, 0); //rotation (rad)

  //constant
  final Double MAX_SPEED_OF_MOTOR = 1.0;//REPLACE WITH m/s
  
  //controller inputs
  DoubleSupplier FWD_ctr;
  DoubleSupplier STR_ctr;
  DoubleSupplier ROT_ctr;

  //deadbands
  final double FWD_DEADBAND = .1;
  final double STR_DEADBAND = .1;
  final double ROT_DEADBAND = .15;

  //motor vector values
  double runAngle;//radians 0 is straight forward, positive is
  double runSpeed;//speed (m/s)
  boolean flipped = false; 

  public SwerveSubsystem(DoubleSupplier FWD, DoubleSupplier STR, DoubleSupplier ROT) {
    this.FWD_ctr = FWD;
    this.STR_ctr = STR;
    this.ROT_ctr = ROT;
    runAngle = 0;
    runSpeed = 0;
    m_lin.setNeutralMode(NeutralModeValue.Brake);
    m_rot.setNeutralMode(NeutralModeValue.Brake);

    m_rot_PID.enableContinuousInput(-Math.PI, Math.PI);
  }
  
  private void resetClass(){
    //to implement
      m_rot_PID.setSetpoint(0);
      m_lin_PID.setSetpoint(0);
  }

  private void runPIDMotors(){
    m_rot_PID.setSetpoint(runAngle);
    // m_lin_PID.setSetpoint(runSpeed);
    m_lin.setControl(m_linReq.withOutput(runSpeed/MAX_SPEED_OF_MOTOR));
    m_rot.setControl(m_rotReq.withOutput(m_rot_PID.calculate((m_rotEncoder.getAbsolutePosition().getValueAsDouble())*2*Math.PI)));
  }

  private double deadbandInput(DoubleSupplier doub, double deadband){
    if(Math.abs(doub.getAsDouble()) < deadband ){
      return 0;
    } else{
      return (doub.getAsDouble()-deadband)/(1-deadband);
    }
  }

  private void calculateMotorSwerveDrive(){
    //init vector
    double m_STR = 0;
    double m_FWD = 0;
    double m_ROT = 0;

    //get input values with deadband 
    //RETURNS 0 WHEN WITHIN DEADBAND
    m_STR = STR_ctr.getAsDouble();
    m_FWD = FWD_ctr.getAsDouble();
    double dead = .1;
    //deadband
    if(Math.sqrt(m_STR * m_STR + m_FWD * m_FWD) < dead){
      m_STR = 0;
      m_FWD = 0;
    } else{
      m_STR = (m_STR-dead)/(1-dead);
      m_FWD = (m_FWD-dead)/(1-dead);
    }
    m_ROT = deadbandInput(ROT_ctr, ROT_DEADBAND);

    //if all zero, keep angle the same but set speed to zero 
    if(m_STR == 0 && m_FWD == 0 && m_ROT == 0){// IF ALL WITHIN DEADBAND SINCE
       //DEADBAND ALREADY SET THEM TO 0
      runSpeed = 0;
      return;
    }

    //ROT Contribution
    double ROT_STR = ROT_ctr.getAsDouble() * Math.cos(45*Math.PI/180);
    double ROT_FWD = ROT_ctr.getAsDouble() * Math.sin(45*Math.PI/180);
    SmartDashboard.putNumber("ROT_STR", ROT_STR);
    SmartDashboard.putNumber("ROT_FWD", ROT_FWD);
    m_STR += ROT_STR;
    m_FWD += ROT_FWD;

    //normalize vector if greater than max
    double magnitude = Math.sqrt(m_FWD*m_FWD + m_STR*m_STR);
    
    // m_STR /= magnitude;
    // m_FWD /= magnitude;
    // magnitude = 1;
    

    //Multiply by max speed
    m_STR *= MAX_SPEED_OF_MOTOR;
    m_FWD *= MAX_SPEED_OF_MOTOR;
    SmartDashboard.putNumber("STR", m_STR);
    SmartDashboard.putNumber("FWD", m_FWD);
    
    //convert to angle and magnitude

    runAngle = Math.atan2(m_STR, m_FWD);
    runSpeed = magnitude;
  }

  public Command runSwerve(){
    return new FunctionalCommand(
      () -> {resetClass();},
      () -> {
        // //main motor loop 
         calculateMotorSwerveDrive();//calculate angle and speed
         runPIDMotors();//send to motors
      }, 
      interrupted -> {
        resetClass();
      },
      () -> false,
      this
    );
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Speed", runSpeed);
    SmartDashboard.putNumber("Rotation", runAngle*180/Math.PI);
  }
}

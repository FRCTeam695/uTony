// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.LEDCommand;
// import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.LEDSubsystem;
import frc.robot.subsystems.MotorSubsystem;

import java.sql.Driver;
import java.util.function.DoubleSupplier;

import org.opencv.core.MatOfRotatedRect;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller.Axis;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...



  // private final ExampleCommand cmd = new ExampleCommand(m_exampleSubsystem);
  private final Joystick m_Joystick = new Joystick(0);

  private final JoystickButton greenButton = new JoystickButton(m_Joystick, 1);
  private final JoystickButton redButton = new JoystickButton(m_Joystick, 2);
  private final JoystickButton blueButton = new JoystickButton(m_Joystick, 3);
  private final JoystickButton yellowButton = new JoystickButton(m_Joystick, 4);
  private final JoystickButton leftBumper = new JoystickButton(m_Joystick, 5);
  private final JoystickButton rightBumper = new JoystickButton(m_Joystick, 6);

  private final DoubleSupplier xAxisL = () -> m_Joystick.getRawAxis(0);
  private final DoubleSupplier yAxisL = () -> m_Joystick.getRawAxis(1);

  private final DoubleSupplier xAxisR = () -> m_Joystick.getRawAxis(4);
  private final DoubleSupplier yAxisR = () -> m_Joystick.getRawAxis(5);

  // private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final LEDSubsystem m_LedSubsystem = new LEDSubsystem(xAxisL);
  private final MotorSubsystem m_MotorSubsystem = new MotorSubsystem();
  // private final DriveTrainSubsystem m_DriveTrainSubsystem = new DriveTrainSubsystem();


  // Replace with CommandPS4Controller or CommandJoystick if needed
  // private final CommandXboxController m_driverController =
  //     new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    // new Trigger(m_exampleSubsystem::exampleCondition)
    //     .onTrue(new ExampleCommand(m_exampleSubsystem));

    greenButton.onTrue(m_LedSubsystem.setColor( 0, 255, 0));
    redButton.onTrue(m_LedSubsystem.setColor( 255, 0, 0));
    yellowButton.onTrue(m_MotorSubsystem.runMotorVelocityPIDTEST(xAxisL));
    blueButton.onTrue(m_LedSubsystem.setColor( 0, 0, 255));
    // leftBumper.whileTrue(m_MotorSubsystem.runMotor(xAxisL));



    // m_MotorSubsystem.setDefaultCommand(m_MotorSubsystem.runMotor(xAxisL));


    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    // m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());


    //tank+arcadedrive
    // m_DriveTrainSubsystem.setDefaultCommand(m_DriveTrainSubsystem.driveTank(yAxisL,yAxisR));
    // m_DriveTrainSubsystem.setDefaultCommand(m_DriveTrainSubsystem.driveArcade(yAxisL, xAxisR));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *  
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
     //return Autos.exampleAuto(m_exampleSubsystem);
     return null;
  }

}

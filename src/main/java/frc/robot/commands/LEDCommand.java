// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LEDSubsystem;

public class LEDCommand extends Command {
  /** Creates a new LEDCommand. */
  LEDSubsystem m_LedSubsystem;
  int r=0;
  int g=0;
  int b=0;

  public LEDCommand(LEDSubsystem m_LedSubsystem,int r,int g,int b) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.r = r;
    this.g = g;
    this.b = b;
    this.m_LedSubsystem = m_LedSubsystem;
    addRequirements(this.m_LedSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_LedSubsystem.setColor(r, g, b);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

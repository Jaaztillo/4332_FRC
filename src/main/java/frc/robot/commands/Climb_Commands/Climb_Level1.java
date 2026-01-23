// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climb_Commands;

import frc.robot.Constants.ClimbConstants;
import frc.robot.subsystems.Climb_Subsystem;

import edu.wpi.first.wpilibj2.command.Command;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Climb_Level1 extends Command {
  private Climb_Subsystem Climb_Subsystem;

  /** Creates a new Climb_First_Level_Command. */
  public Climb_Level1(Climb_Subsystem Climb_Subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.Climb_Subsystem = Climb_Subsystem;

    addRequirements(this.Climb_Subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
    Climb_Subsystem.climb_level_1();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Climb_Subsystem.atSetPoint() || 
           Math.abs(Climb_Subsystem.get_inches() - ClimbConstants.First_Rung_Distance) < 0.75;
  }
}

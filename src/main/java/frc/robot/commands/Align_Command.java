// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.LimelightHelpers;
import frc.robot.subsystems.Limelight_Subsystem;
import frc.robot.subsystems.TankDrive_Subsystem;

import frc.robot.Constants.LimelightConstants;

import edu.wpi.first.wpilibj2.command.Command;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Align_Command extends Command {
  private Limelight_Subsystem limelight_Subsystem;
  private TankDrive_Subsystem tankDrive_Subsystem;

  /** Creates a new Align_Command. */
  public Align_Command(Limelight_Subsystem limelight_Subsystem, TankDrive_Subsystem tankDrive_Subsystem) 
  {
    // Use addRequirements() here to declare subsystem dependencies.
    this.limelight_Subsystem = limelight_Subsystem;
    this.tankDrive_Subsystem = tankDrive_Subsystem;

    addRequirements(this.limelight_Subsystem, this.tankDrive_Subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    double leftOutput = limelight_Subsystem.get_left_output();
    double rightOutput = limelight_Subsystem.get_right_output();

    if (Math.abs(leftOutput) < 0.05) leftOutput = 0;
    if (Math.abs(rightOutput) < 0.05) rightOutput = 0;

    tankDrive_Subsystem.tank_drive(leftOutput, rightOutput);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    tankDrive_Subsystem.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
      if (!limelight_Subsystem.has_AprilTag_Left() && !limelight_Subsystem.has_AprilTag_Right()) {
          return true; // stop if tag lost
      }

      double z = LimelightHelpers.getTargetPose3d_RobotSpace("limelight").getZ();
      double tx = LimelightHelpers.getTX(LimelightConstants.name);

      return Math.abs(z - LimelightConstants.DESIRED_DISTANCE) < 0.05 && Math.abs(tx) < 2.0;
  }
}

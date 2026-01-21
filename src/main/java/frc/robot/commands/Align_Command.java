// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.LimelightHelpers;
import frc.robot.subsystems.Limelight_Subsystem;
import frc.robot.subsystems.TankDrive_Subsystem;

import frc.robot.Constants.LimelightConstants;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Align_Command extends Command {
  private Limelight_Subsystem limelight_Subsystem;
  private TankDrive_Subsystem tankDrive_Subsystem;
  private final CommandXboxController controller;

  private boolean rumbled = false;

  /** Creates a new Align_Command. */
  public Align_Command(Limelight_Subsystem limelight_Subsystem, TankDrive_Subsystem tankDrive_Subsystem, CommandXboxController controller) 
  {
    // Use addRequirements() here to declare subsystem dependencies.
    this.limelight_Subsystem = limelight_Subsystem;
    this.tankDrive_Subsystem = tankDrive_Subsystem;
    this.controller = controller;

    addRequirements(this.tankDrive_Subsystem);
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

    // Check alignment
    boolean aligned =
        limelight_Subsystem.hasValidTag()
        && Math.abs(
            LimelightHelpers.getTX(LimelightConstants.name)
           ) < LimelightConstants.TX_TOLERANCE
        && Math.abs(
            limelight_Subsystem.getFilteredDistanceZ()
            - LimelightConstants.DESIRED_DISTANCE
           ) < LimelightConstants.Z_TOLERANCE;

    // Rumble ONCE when aligned
    if (aligned && !rumbled) {
      controller.setRumble(RumbleType.kBothRumble, 1.0);
      rumbled = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    tankDrive_Subsystem.stop();

    // Stop rumble
    controller.setRumble(RumbleType.kBothRumble, 0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return rumbled;
  }
}

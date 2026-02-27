// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.DifferentialDriveCommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.math.MathUtil;
// Controller
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

// Subsystems
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.TankDriveSubsystem;

public class AlignDriveCommand extends Command {
  /** Limelight Subsystem which can be used to get the distance and angle from a valid april tag */
  private LimelightSubsystem limelight;

  /** Tankdrive Subsystem which can be used to control the drive train */
  private TankDriveSubsystem tankDrive;

  /** Xbox Controller which can be used to bind keybinds and give feedback to driver */
  private final CommandXboxController controller;

  /** Robot turn power */
  private final Double kTurn = 0.5;

  /**
   * AlignDriveCommand Constructor
   * @param tankDrive A subsystem to turn the robot to the target angle
   * @param limelight A subsystem used to get the target angle
   * @param controller A Xbox Controller to rumble to the driver when the robot is aligned
   */

  public AlignDriveCommand(TankDriveSubsystem tankDrive, LimelightSubsystem limelight, CommandXboxController controller) 
  {
    this.tankDrive = tankDrive;
    this.limelight = limelight;
    this.controller = controller;
    
    addRequirements();
  }

  /**
   * Turn the Robot to the Hub for Scoring Fuel | Repeats every 20m/s
   */
  @Override
  public void execute () {
    // Checks for a shooting tag
    if (!limelight.has_AprilTag_Shoot()) return;

    // Get angle to turn to
    double tx = limelight.getTX();

    // Set turn power
    double turn = tx * kTurn;

    // Clamp Power from Minimum to Maximum in Tank Drive
    turn = MathUtil.clamp(turn, -1.0, 1.0);
    
    // Apply Deadzone
    if (Math.abs(turn) < 0.05) {
      turn = 0;
    } else if (Math.abs(turn) < 0.2) {
      // Minimum Turn Power
      turn = Math.signum(turn) * 0.2;
    }

    // Set the motors left and right powers
    double left  = turn;
    double right = -turn;

    // Turn until the desired angle is reached
    if (Math.abs(tx) < 0.5) {
      // Zero the alignment
      tankDrive.reset();

      // Rumble Controller
      controller.setRumble(RumbleType.kBothRumble, 1.0);
      return;
    }

    // Sets values to arcade drive
    tankDrive.set(left, right);
  }

  /** Stops the drive train and the controller rumble */
  @Override
  public void end(boolean interrupted) 
  {
    // Reset the values added to arcade drive
    tankDrive.reset();

    // Stop rumbling controller
    controller.setRumble(RumbleType.kBothRumble, 0.0);
  }

  /** Will only stop aligning when driver lets go of the keybind */
  @Override
  public boolean isFinished() { return false; }
}

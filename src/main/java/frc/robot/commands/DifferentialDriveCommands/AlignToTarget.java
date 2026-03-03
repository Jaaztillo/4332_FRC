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
import frc.robot.subsystems.DriveSubsystem;

public class AlignToTarget extends Command {
  /** Robot turn power */
  private final Double kTurn = 0.5;

  /** Limelight Subsystem which can be used to get the distance and angle from a valid april tag */
  private LimelightSubsystem limelight;

  /** Tankdrive Subsystem which can be used to control the drive train */
  private DriveSubsystem tankDrive;

  /** Xbox Controller which can be used to bind keybinds and give feedback to driver */
  private final CommandXboxController controller;

  /**
   * AlignDriveCommand Constructor
   * @param tankDrive A subsystem to turn the robot to the target angle
   * @param limelight A subsystem used to get the target angle
   * @param controller A Xbox Controller to rumble to the driver when the robot is aligned
   */
  public AlignToTarget(DriveSubsystem tankDrive, LimelightSubsystem limelight, CommandXboxController controller) 
  {
    this.tankDrive = tankDrive;
    this.limelight = limelight;
    this.controller = controller;
    
    addRequirements();
  }

  /**
   * AlignDriveCommand Constructor for autonomous
   * @param tankDrive A subsystem to turn the robot to the target angle
   * @param limelight A subsystem used to get the target angle
   */
  public AlignToTarget(DriveSubsystem tankDrive, LimelightSubsystem limelight) 
  {
    this.tankDrive = tankDrive;
    this.limelight = limelight;
    this.controller = null;
    
    addRequirements();
  }

  /**
   * Turn the Robot to the Hub for Scoring Fuel | Repeats every 20m/s
   */
  @Override
  public void execute () {
    // Angle
    double Theta = limelight.lookAtTarget();
    
    // Set turn power
    double turn = Theta * kTurn;

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

    // Stop Turning Once Desired Angle is Reached
    if (Math.abs(Theta) < 0.5) {
      tankDrive.resetAlignment();
      tankDrive.setAligned(true);

      // Rumble Controller
      if (controller != null) controller.setRumble(RumbleType.kBothRumble, 1.0);
      return;
    }

    // Keep Turning Once Desired Angle is Reached
    tankDrive.setAlignment(left, right);
    tankDrive.setAligned(false);

    // Stop Rumble controller
    if (controller != null) controller.setRumble(RumbleType.kBothRumble, 0.0);
  }

  /** Stops the drive train and the controller rumble */
  @Override
  public void end (boolean interrupted) {
    // Reset the values added to arcade drive
    tankDrive.resetAlignment();
    tankDrive.setAligned(false);

    // Stop targeting with limelight
    limelight.stopTargeting();

    // Stop rumbling controller
    if (controller == null) controller.setRumble(RumbleType.kBothRumble, 0.0);
  }

  /** Will only stop aligning when driver lets go of the keybind */
  @Override
  public boolean isFinished() { return false; }
}
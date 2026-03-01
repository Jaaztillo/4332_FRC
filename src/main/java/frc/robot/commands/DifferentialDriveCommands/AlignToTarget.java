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
import frc.robot.subsystems.PigeonSubsystem;
import frc.robot.subsystems.DriveSubsystem;

public class AlignToTarget extends Command {
  /** Offset from apriltag to hub */
  private final Double sideOffsetFoward = 0.53;
  private final Double sideOffsetLeft = 0.36;

  private final Double frontOffsetFoward = 0.6;

  private final Double hubUpOffset = 0.69;

  /** Offset from apriltag to outpost */
  private final Double outpostOffsetFoward = 4.60;

  /** Robot turn power */
  private final Double kTurn = 0.5;

  /** Limelight Subsystem which can be used to get the distance and angle from a valid april tag */
  private LimelightSubsystem limelight;

  /** Tankdrive Subsystem which can be used to control the drive train */
  private DriveSubsystem tankDrive;

  /** Pigeon Subsystem to detect heading of robot */
  private PigeonSubsystem pigeon;

  /** Xbox Controller which can be used to bind keybinds and give feedback to driver */
  private final CommandXboxController controller;

  /**
   * AlignDriveCommand Constructor
   * @param tankDrive A subsystem to turn the robot to the target angle
   * @param limelight A subsystem used to get the target angle
   * @param controller A Xbox Controller to rumble to the driver when the robot is aligned
   */
  public AlignToTarget(DriveSubsystem tankDrive, LimelightSubsystem limelight, PigeonSubsystem pigeon, CommandXboxController controller) 
  {
    this.tankDrive = tankDrive;
    this.limelight = limelight;
    this.pigeon = pigeon;
    this.controller = controller;
    
    addRequirements();
  }

  /**
   * AlignDriveCommand Constructor for autonomous
   * @param tankDrive A subsystem to turn the robot to the target angle
   * @param limelight A subsystem used to get the target angle
   */
  public AlignToTarget(DriveSubsystem tankDrive, LimelightSubsystem limelight, PigeonSubsystem pigeon) 
  {
    this.tankDrive = tankDrive;
    this.limelight = limelight;
    this.pigeon = pigeon;
    this.controller = null;
    
    addRequirements();
  }

  /**
   * Turn the Robot to the Hub for Scoring Fuel | Repeats every 20m/s
   */
  @Override
  public void execute () {
    // Checks for a shooting tag
    if (!limelight.hasAprilTagShoot() && !limelight.hasAprilTagShootLeft() && !limelight.hasAprilTagShootRight()) return;

    // Get the relative offset foward position
    double offsetFoward =
      limelight.hasAprilTagOutpost() ? outpostOffsetFoward
    :  limelight.hasAprilTagShoot() ? frontOffsetFoward
    : limelight.hasAprilTagShootLeft() ? -sideOffsetFoward
    : limelight.hasAprilTagShootRight() ? sideOffsetFoward
    : 0;

    // Get the relative offset left position
    double offsetLeft =
      limelight.hasAprilTagShootLeft() ? -sideOffsetLeft
    : limelight.hasAprilTagShootRight() ? sideOffsetLeft
    : 0;

    double offsetUp =
      limelight.hasAprilTagShoot() || limelight.hasAprilTagShootLeft() || limelight.hasAprilTagShootRight() ?
      hubUpOffset : 0;
    
    // Position 3D (foward, left, up)
    limelight.setTarget(offsetFoward, offsetLeft, offsetUp);
    
    // Get angle to turn to
    double motorAngle = limelight.getPositionAngle();

    // Get Angle to turn to / heading error
    double headingError = pigeon.getHeadingError(motorAngle);

    // Set turn power
    double turn = headingError * kTurn;

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

    limelight.setHeadingError(headingError);

    // Stop Turning Once Desired Angle is Reached
    if (Math.abs(headingError) < 0.5) {
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

    // Reset the limelights current target
    limelight.resetTarget();
    limelight.setHeadingError(0.0);

    // Stop rumbling controller
    if (controller == null) controller.setRumble(RumbleType.kBothRumble, 0.0);
  }

  /** Will only stop aligning when driver lets go of the keybind */
  @Override
  public boolean isFinished() { return false; }
}

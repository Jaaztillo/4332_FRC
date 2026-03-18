package frc.robot.commands.AlignmentCommands;

import edu.wpi.first.math.MathUtil;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.DriveSubsystem;

public class AlignToTarget extends Command {
  /** Xbox Controller which can be used to bind keybinds and give feedback to driver */
  private final CommandXboxController controller;

  /** Tankdrive Subsystem which can be used to control the drive train */
  private DriveSubsystem drive;

  /** Robot turn power */
  private final Double kTurn = 0.5;

  public AlignToTarget(DriveSubsystem drive, CommandXboxController controller) {
    this.drive = drive;
    this.controller = controller;
  }

  /**
   * Align
   */
  @Override
  public void execute() {
    // Target Pose
    Pose3d target = drive.getGridTarget();
    
    // Angle from robot pose to target pose
    double angle = drive.angleToTarget(target);

    // Turn Power
    double turn = MathUtil.clamp(angle * kTurn, -1.0, 1.0);

    // Apply Friction Deadzone
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
    if (Math.abs(angle) < 0.5) {
      // Rumble Controller
      if (controller != null) controller.setRumble(RumbleType.kBothRumble, 1.0);
      return;
    }

    // Turn to target
    drive.tankDrive(left, right);
  }

  /**
   * Stop rumbling controller once the command ends
   */
  @Override
  public void end(boolean interrupted) {
    if (controller == null) controller.setRumble(RumbleType.kBothRumble, 0.0);
  }

  // Stops when driver stops holding button down
  @Override
  public boolean isFinished() {
    return false;
  }
}

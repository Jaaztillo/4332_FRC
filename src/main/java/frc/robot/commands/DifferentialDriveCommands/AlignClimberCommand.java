package frc.robot.commands.DifferentialDriveCommands;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import edu.wpi.first.math.MathUtil;

// Subsystems
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.TankDriveSubsystem;

public class AlignClimberCommand extends Command {
  /** Turn Power Coefficients */
  double kTurn = 0.5;

  /** Limelight Subsystem which can be used to get the distance and angle from a valid april tag */
  private LimelightSubsystem limelight;

  /** Tankdrive Subsystem which can be used to control the drive train */
  private TankDriveSubsystem tankDrive;

  /** Controller to give feedback to driver when the climber is aligned */
  private CommandXboxController controller;

  /** String to decide which pole to align to */
  private String side;
  
  /**
   * AlignClimberCommand
   * @param tankDrive A subsystem to turn the robot to the target angle
   * @param limelight A subsystem used to get the target angle
   * @param controller A Xbox Controller to rumble to the driver when the robot is aligned
   * @param side The side to decide which pole the robot will align to
   */
  public AlignClimberCommand(TankDriveSubsystem tankDrive, LimelightSubsystem limelight, CommandXboxController controller, String side) {
    this.tankDrive = tankDrive;
    this.limelight = limelight;
    this.controller = controller;
    this.side = side;

    addRequirements();
  }

  /**
   * Turn the Robot to it's respective Pole for Climbing | Repeats every 20m/s
   */
  @Override
  public void execute() {
    if (!limelight.has_AprilTag_Climb()) return;

    // Get angle to turn to
    double tx = limelight.getTX();

    // Get distance from Robot to April Tag
    double distance = limelight.getFilteredDistanceZ();

    // Get the offset position
    double dx = side.equals("left") ? -16.3 : 16.3;

    // Get offset angle to look towards pole
    double offsetAngle = Math.toDegrees(
        Math.atan2(dx, distance)
      );

    // Get offset Tx Angle (Look to pole, not tag)
    double poleTx = tx + offsetAngle;

    // Set turn power
    double turn = poleTx * kTurn;

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
    if (!(Math.abs(tx) < 0.5)) {
      tankDrive.set(left, right);
    }

    // Reset Align if reached desired angle
    tankDrive.reset();
    
    // Rumble controller based on side aligned on
    if (side.equals("left")) controller.setRumble(RumbleType.kLeftRumble, 1.0);
    if (side.equals("right")) controller.setRumble(RumbleType.kRightRumble, 1.0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Reset the values added to arcade drive
    tankDrive.reset();

    // Rumble controller based on side aligned on
    if (side.equals("left")) controller.setRumble(RumbleType.kLeftRumble, 0.0);
    if (side.equals("right")) controller.setRumble(RumbleType.kRightRumble, 0.0);
  }

  /** Command only ends when driver let's go of button binding */
  @Override
  public boolean isFinished() { return false; }
}

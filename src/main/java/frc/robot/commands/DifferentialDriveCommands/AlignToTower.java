package frc.robot.commands.DifferentialDriveCommands;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import edu.wpi.first.math.MathUtil;
// Subsystems
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.PigeonSubsystem;
import frc.robot.subsystems.DriveSubsystem;

public class AlignToTower extends Command {
  /** Pole Offset from apriltag */
  private double poleOffset = 0.409575;

  /** Turn Power Coefficients */
  private double kTurn = 0.5;

  /** Limelight Subsystem which can be used to get the distance and angle from a valid april tag */
  private LimelightSubsystem limelight;

  /** Tankdrive Subsystem which can be used to control the drive train */
  private DriveSubsystem tankDrive;

  /** Pigeon Subsystem to detect heading of robot */
  private PigeonSubsystem pigeon;

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
  public AlignToTower(DriveSubsystem tankDrive, LimelightSubsystem limelight, PigeonSubsystem pigeon, CommandXboxController controller, String side) {
    this.tankDrive = tankDrive;
    this.limelight = limelight;
    this.controller = controller;
    this.pigeon = pigeon;
    this.side = side;

    addRequirements();
  }

  /**
   * Turn the Robot to it's respective Pole for Climbing | Repeats every 20m/s
   */
  @Override
  public void execute() {
    if (!limelight.hasAprilTagClimb()) return;

    // Get the offset position
    double offsetX = side.equals("left") ? -poleOffset : poleOffset;

     // Position 3D (foward, left, up)
    limelight.setTarget(0.0, offsetX, 0.0);

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
      controller.setRumble(RumbleType.kBothRumble, 1.0);
      return;
    }

    // Keep Turning Once Desired Angle is Reached
    tankDrive.setAlignment(left, right);
    tankDrive.setAligned(false);

    // Stop Rumble controller
    controller.setRumble(RumbleType.kBothRumble, 0.0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Reset the values added to arcade drive
    tankDrive.resetAlignment();
    tankDrive.setAligned(false);

    // Reset the limelights current target
    limelight.resetTarget();
    limelight.setHeadingError(0.0);

    // Stop Rumble controller
    controller.setRumble(RumbleType.kBothRumble, 0.0);
  }

  /** Command only ends when driver let's go of button binding */
  @Override
  public boolean isFinished() { return false; }
}

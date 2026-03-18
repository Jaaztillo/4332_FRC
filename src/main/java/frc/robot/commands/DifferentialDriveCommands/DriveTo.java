package frc.robot.commands.DifferentialDriveCommands;

import edu.wpi.first.wpilibj2.command.Command;

// Subsystems
import frc.robot.subsystems.DriveSubsystem;

public class DriveTo extends Command {
  /** Tank Drive Subsystem which controls the drive train */
  private DriveSubsystem drive;

  /** fowardPercent -> LeftJoystick | rotationPercent -> RightJoystick*/
  private Double fowardPercent;
  private Double rotationPercent;

  /**
   * Tank Drive Constructor
   * @param drive the subsystem that controls the drive train
   * @param fowardPercent the value from the left joystick to control the drive
   * @param rotationPercent the value from the right joystick to control the twist
   */
  public DriveTo(DriveSubsystem drive, Double fowardPercent, Double rotationPercent) {
    this.drive = drive;

    this.fowardPercent = fowardPercent;
    this.rotationPercent = rotationPercent;

    addRequirements(drive);
  }

  /**
   * Get the left and right joystick values and apply the values to arcadeDrive with a deadband
   */
  @Override
  public void execute() 
  {
    // Arcade drive
    drive.arcadeDrive(fowardPercent, rotationPercent);
  }
  
  /** Drive train stops when command ends */
  @Override
  public void end(boolean interrupted) { drive.stop(); }

  /** command only stops when driver lets go of the keybind */
  @Override
  public boolean isFinished() { return false; }
}
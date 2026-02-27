package frc.robot.commands.DifferentialDriveCommands;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.DoubleSupplier;
import edu.wpi.first.math.MathUtil;

// Subsystems
import frc.robot.subsystems.TankDriveSubsystem;

public class TankDriveCommand extends Command {
  /** Tank Drive Subsystem which controls the drive train */
  private TankDriveSubsystem tankDrive;

  /** fowardPercent -> LeftJoystick | rotationPercent -> RightJoystick*/
  private DoubleSupplier fowardPercent;
  private DoubleSupplier rotationPercent;

  /**
   * Tank Drive Constructor
   * @param tankDrive the subsystem that controls the drive train
   * @param fowardPercent the value from the left joystick to control the drive
   * @param rotationPercent the value from the right joystick to control the twist
   */
  public TankDriveCommand(TankDriveSubsystem tankDrive, DoubleSupplier fowardPercent, DoubleSupplier rotationPercent) {
    this.tankDrive = tankDrive;

    this.fowardPercent = fowardPercent;
    this.rotationPercent = rotationPercent;

    addRequirements(tankDrive);
  }

  /**
   * Get the left and right joystick values and apply the values to arcadeDrive with a deadband
   */
  @Override
  public void execute() 
  {
    // Deadband so that the robot won't nudge
    double fowardDeadband = MathUtil.applyDeadband(fowardPercent.getAsDouble(), 0.04);
    double rotationDeadband = MathUtil.applyDeadband(rotationPercent.getAsDouble(), 0.04);

    // Arcade drive
    tankDrive.drive(fowardDeadband, rotationDeadband);
  }
  
  /** Drive train stops when command ends */
  @Override
  public void end(boolean interrupted) { tankDrive.stop(); }

  /** command only stops when driver lets go of the keybind */
  @Override
  public boolean isFinished() { return false; }
}

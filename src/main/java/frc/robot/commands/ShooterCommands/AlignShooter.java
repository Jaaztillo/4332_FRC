package frc.robot.commands.ShooterCommands;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj2.command.Command;

// Subsystems
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AlignShooter extends Command {
  /** The Shooter subsystem which controls the velocity and angle of the shooter mechanism */
  private ShooterSubsystem shooter;

  /** The Drive subsystem which let's us get data for distance control */
  private DriveSubsystem drive;

  /**
   * 
   * @param shooter the subsystem which will make sure the RPM and Angle match to shoot towards the desired location
   * @param drive the subsystem used to measure the distance
   */
  public AlignShooter(ShooterSubsystem shooter, DriveSubsystem drive) {
    this.shooter = shooter;
    this.drive = drive;
  }

  /**
   * Sets RPM and Angle of the shooter every 20ms
   */
  @Override
  public void execute() {
    Pose3d target = drive.getGridTarget();

    if (target == null) return;

    Double distance = drive.getDistance(target);
    
    shooter.setRPM(distance);
  }

  /**
   * Returns the RPM and Angle to it's original state on end
   */
  @Override
  public void end(boolean interrupted) {}

  /**
   * Command only ends once driver lets go of the button
   */
  @Override
  public boolean isFinished() { return false; }
}
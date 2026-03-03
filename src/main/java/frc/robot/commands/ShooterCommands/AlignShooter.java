package frc.robot.commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;

// Subsystems
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AlignShooter extends Command {
  /** The Shooter subsystem which controls the velocity and angle of the shooter mechanism */
  private ShooterSubsystem shooter;

  /** The limelight subsystem which let's us get data for distance control */
  private LimelightSubsystem limelight;

  /**
   * 
   * @param shooter the subsystem which will make sure the RPM and Angle match to shoot towards the desired location
   * @param limelight the subsystem used to measure the distance
   */
  public AlignShooter(ShooterSubsystem shooter, LimelightSubsystem limelight) {
    this.shooter = shooter;
    this.limelight = limelight;
  }

  /**
   * Sets RPM and Angle of the shooter every 20ms
   */
  @Override
  public void execute() {
    Double distance = limelight.getDistance(limelight.getCurrentTarget());
    
    shooter.setAngle(distance);
    shooter.setRPM(distance);
  }

  /**
   * Returns the RPM and Angle to it's original state on end
   */
  @Override
  public void end(boolean interrupted) {
    shooter.setAngle(4);
    shooter.setRPM(4);
  }

  /**
   * Command only ends once driver lets go of the shooter keybind
   */
  @Override
  public boolean isFinished() { return false; }
}
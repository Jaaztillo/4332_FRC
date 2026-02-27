package frc.robot.commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;

// Subsystems
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AlignShooterCommand extends Command {
  /** The Shooter subsystem which controls the velocity and angle of the shooter mechanism */
  private ShooterSubsystem shooter;

  /** The limelight subsystem which let's us get data for distance control */
  private LimelightSubsystem limelight;

  public AlignShooterCommand(ShooterSubsystem shooter, LimelightSubsystem limelight) {
    this.shooter = shooter;
    this.limelight = limelight;
  }

  /**
   * Sets RPM and Angle of the shooter every 20ms
   */
  @Override
  public void execute() {
    if (!limelight.has_AprilTag_Shoot()) return;
    
    Double distance = limelight.getFilteredDistanceZ();
    
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

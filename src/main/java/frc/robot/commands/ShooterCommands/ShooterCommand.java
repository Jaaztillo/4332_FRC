package frc.robot.commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;

// Subsystems
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends Command {
  /** The Shooter subsystem which controls when the shooter mechanism will shoot and stop */
  private ShooterSubsystem shooter;
  
  public ShooterCommand(ShooterSubsystem shooter) {
    this.shooter = shooter;

    addRequirements();
  }

  /**
   * Run Shooter Motor
   */
  @Override
  public void initialize () { 
    shooter.shoot(); 
  }

  /**
   * Stop the shooter once the command ends
   */
  @Override
  public void end(boolean interrupted) { shooter.stop(); }

  /**
   * Command only ends once driver lets go of the shooter keybind
   */
  @Override
  public boolean isFinished() { return false; }
}

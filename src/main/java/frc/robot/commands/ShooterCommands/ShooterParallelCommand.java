package frc.robot.commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

// Subsystems
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.RollerSubsystem;

/**
 * Parallel command for the shooter
 */
public class ShooterParallelCommand extends ParallelCommandGroup {
  public ShooterParallelCommand(ShooterSubsystem shooter, RollerSubsystem roller) {
    addCommands(
      // Start Aligning and running Shooter
      new ShooterCommand(shooter),
      
      // Wait 1.0s -> Start feeding balls to shooter
      new RollerSequenceCommand(roller)
    );
  }
}

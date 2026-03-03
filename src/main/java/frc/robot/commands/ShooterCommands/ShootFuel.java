package frc.robot.commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
// Subsystems
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.RollerSubsystem;

/**
 * Parallel command for the shooter and roller sequence
 */
public class ShootFuel extends ParallelCommandGroup {
  public ShootFuel(ShooterSubsystem shooter, RollerSubsystem roller) {
    addCommands(
      // Run the shooter
      new StartEndCommand(
        shooter::shoot,
        shooter::stop,
        shooter
      ),
      
      // Wait 1.0s -> Start feeding balls to shooter
      new IndexFuel(roller)
    );
  }
}
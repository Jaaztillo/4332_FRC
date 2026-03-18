package frc.robot.commands.ShooterCommands;

//import edu.wpi.first.wpilibj2.command.ConditionalCommand;
//import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

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
      
      // Roller sequential sequence
      new SequentialCommandGroup(
        // Wait until it reaches the point but with a timeout of 4 seconds
        new WaitUntilCommand(shooter::atRPM).withTimeout(4.0),

        // Remove This later after testing
        new StartEndCommand(
            roller::run,
            roller::stop,
            roller
          )
      )
    );
  }
}
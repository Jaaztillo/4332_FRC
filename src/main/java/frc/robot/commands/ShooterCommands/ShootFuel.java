package frc.robot.commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
// Subsystems
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.RollerSubsystem;

/**
 * Parallel command for the shooter and roller sequence
 */
public class ShootFuel extends ParallelCommandGroup {
  public ShootFuel(ShooterSubsystem shooter, RollerSubsystem roller, LimelightSubsystem limelight) {
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
        new WaitUntilCommand(shooter::atSetpoint).withTimeout(4.0),
        
        // Feed balls to shooter only if there is a currentTarget
        new ConditionalCommand(
          new StartEndCommand(
            roller::run,
            roller::stop,
            roller
          ),
          new InstantCommand(),
          () -> limelight.getCurrentTarget() != null
        )
      )
    );
  }
}
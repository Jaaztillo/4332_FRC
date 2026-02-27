package frc.robot.commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

// Subsystems
import frc.robot.subsystems.RollerSubsystem;

/**
 * Sequential Command for the roller to run after 1 second
 */
public class RollerSequenceCommand extends SequentialCommandGroup {
  public RollerSequenceCommand(RollerSubsystem roller) {
    addCommands(
      // Wait 1 second
      new WaitCommand(1.0),

      // Run Roller
      new RollerCommand(roller)
    );

    // Add requirements for the entire group
    addRequirements(roller);
  }
}

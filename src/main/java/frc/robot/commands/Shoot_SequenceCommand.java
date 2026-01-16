package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter_Subsystem;
import frc.robot.subsystems.Roller_Subsystem;

// This command replaces your original Shooter_Command for this specific sequence
public class Shoot_SequenceCommand extends SequentialCommandGroup {

  /**
   * Creates a new ShootSequence command group.
   * Runs the shooter, waits 2 seconds, then runs the roller.
   */
  public Shoot_SequenceCommand(Shooter_Subsystem shooter, Roller_Subsystem roller) {
    // Add commands to run sequentially
    addCommands(
        // Command 1: Run the shooter (keeps running until the command group finishes)
        new Shooter_Command(shooter), 

        // Command 2: Wait for 2 seconds
        new WaitCommand(2.0),

        // Command 3: Run the roller (keeps running until the command group finishes)
        new Roller_Command(roller)
    );

    // Add requirements for the entire group
    addRequirements(shooter, roller);
  }
}

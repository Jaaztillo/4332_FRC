package frc.robot.commands.ClimbCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

// Subsystems
import frc.robot.subsystems.ClimbSubsystem;

/**
 * Sequential Climb Command To Climb to Level 3 Via Multiple Climb Commands
 */

public class SequentialClimb extends SequentialCommandGroup {
  public SequentialClimb(ClimbSubsystem climber) {
    addCommands(
      // Climber -> First Rung
      new Climb(climber, 1),
      new WaitCommand(1),

      // Robot Climbs to First Rung
      new Climb(climber),
      new WaitCommand(1),

      //========================================\\

      // Climber -> Second Rung
      new Climb(climber, 2),
      new WaitCommand(1),

      // Robot Climbs to Secong Rung
      new Climb(climber),
      new WaitCommand(1),

      //========================================\\
      
      // Climber -> Third Rung
      new Climb(climber, 3),
      new WaitCommand(1),

      // Robot Climbs to Third Rung
      new Climb(climber)
    );

    addRequirements(climber);
  }
}

package frc.robot.commands.ClimbCommands;

import edu.wpi.first.wpilibj2.command.Command;

// Subsystems
import frc.robot.subsystems.ClimbSubsystem;

public class ClimbDown extends Command {
  /** Climber Subsystem that controls the climber motors and the position it's at for tower climbing */
  private ClimbSubsystem climber;

  /**
   * ClimbDown Constructor
   * @param climber the subsystem to bring the robot down from the first rung after autonomous
   */
  public ClimbDown(ClimbSubsystem climber) {
    this.climber = climber;

    addRequirements(this.climber);
  }

  /** Climb down from rung 1 */
  @Override
  public void initialize() { climber.climbDown(); }

  /** Command is an Instant Command */
  @Override
  public boolean isFinished() { return true; }
}

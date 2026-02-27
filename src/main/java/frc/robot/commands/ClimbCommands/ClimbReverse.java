/**
 * THIS COMMAND IS JUST FOR TESTING FOR CLIMBER FACTORING AND WILL BE REMOVED LATER
 */

package frc.robot.commands.ClimbCommands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.ClimbSubsystem;

public class ClimbReverse extends Command {
  private ClimbSubsystem climber;

  public ClimbReverse(ClimbSubsystem climber) {
    this.climber = climber;

    addRequirements(this.climber);
  }

  @Override
  public void execute() { climber.climbReverse(); }

  @Override
  public void end(boolean interrupted) { climber.stop(); }

  @Override
  public boolean isFinished() { return false; }
}

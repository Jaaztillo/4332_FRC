/**
 * THIS COMMAND IS JUST FOR TESTING FOR CLIMBER FACTORING AND WILL BE REMOVED LATER
 */

package frc.robot.commands.ClimbCommands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.ClimbSubsystem;

public class ClimbFoward extends Command {
  private ClimbSubsystem climber;

  public ClimbFoward(ClimbSubsystem climber) {
    this.climber = climber;

    addRequirements(this.climber);
  }

  @Override
  public void execute() { climber.climbFoward(); }

  @Override
  public void end(boolean interrupted) { climber.stop(); }

  @Override
  public boolean isFinished() { return false; }
}

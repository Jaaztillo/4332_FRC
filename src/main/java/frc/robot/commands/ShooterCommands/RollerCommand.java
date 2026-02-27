package frc.robot.commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;

// Subsystems
import frc.robot.subsystems.RollerSubsystem;

public class RollerCommand extends Command {
  /** The Roller Subsystem which is resposible for feeding the balls to the shooter */
  private RollerSubsystem Roller_Subsystem;
  
  public RollerCommand(RollerSubsystem Roller_Subsystem) 
  {
    this.Roller_Subsystem = Roller_Subsystem;

    addRequirements(this.Roller_Subsystem);
  }

  /** Run The Roller Motor */
  @Override
  public void initialize() { Roller_Subsystem.run(); }

  /** Stop the Roller Motor */
  @Override
  public void end(boolean interrupted) { Roller_Subsystem.stop(); }

  /** Roller Command only stops when driver lets go of roller keybind */
  @Override
  public boolean isFinished() { return false; }
}

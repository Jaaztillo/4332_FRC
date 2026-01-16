/*
  * MASTER PROGRAMMERS WORK
  */
  
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter_Subsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Shooter_Command extends Command {
  private Shooter_Subsystem shooter_subsystem;

  /** Creates a new Shooter_Command. */
  public Shooter_Command(Shooter_Subsystem shooter_subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter_subsystem = shooter_subsystem;

    addRequirements(this.shooter_subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    shooter_subsystem.shoot();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    shooter_subsystem.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

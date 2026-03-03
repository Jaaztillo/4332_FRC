package frc.robot.commands.ClimbCommands;

import edu.wpi.first.wpilibj2.command.Command;

// Subsystems
import frc.robot.subsystems.ClimberSubsystem;

/**
   * Uses overloaded constructors to dictate if the climber will climb to a level or retract itself
   */

public class Climb extends Command {
  /** Climber Subsystem that controls the climber motors and the position it's at for tower climbing */
  private ClimberSubsystem climber;

  /** Controls which level the robot will climb to */
  private int level;
  
  /**
   * Climb Constructor (climber, level)
   * @param climber the subsystem to move the climber up to a rung
   */
  public Climb (ClimberSubsystem climber, int level) {
    this.climber = climber;
    this.level = level;
  }

  /**
   * Climb Constructor (climber)
   * @param climber the subsystem to move the climber up to a rung
   */
  public Climb (ClimberSubsystem climber) {
    this.climber = climber;
    this.level = 0;
  }

  /** Climb to Respective Level */
  @Override
  public void initialize () { climber.climb(level); }

  /** Climber stops once the climber reaches it's respective point */
  @Override
  public void end (boolean interrupted) { climber.stop(); }

  /** Command Ends when climber reaches it's respective point */
  @Override
  public boolean isFinished () { return climber.atSetPoint(); }
}
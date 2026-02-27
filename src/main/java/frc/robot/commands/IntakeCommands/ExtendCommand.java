package frc.robot.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.ExtensionSubsystem;
import frc.robot.subsystems.ClimbSubsystem;

public class ExtendCommand extends Command {
  // Extension Subsystem to make the intake extend
  private ExtensionSubsystem extension;

  // Climber Subsystem to get if the climber is extended to retract it
  private ClimbSubsystem climber;

  /**
   * 
   * @param extension a subsystem that will let the intake extend
   * @param climber a subsystem to make sure the climber isn't extended while trying to extend the intake
   */
  public ExtendCommand(ExtensionSubsystem extension, ClimbSubsystem climber) {
    this.extension = extension;
    this.climber = climber;

    addRequirements(extension, climber);
  }

  /** Extend or Retract the extension based on the intakes state */
  @Override
  public void initialize() {
    // Don't Extend if the climber is extended
    if (climber.isExtended()) return;

    // Extend if not extended and is retracted
    if (!extension.isExtended() && extension.isRetracted()) extension.extend();

    // Retract if extended and not retraced
    if (extension.isExtended() && !extension.isRetracted()) extension.retract();
  }

  /** Stop extending or retracting */
  @Override
  public void end(boolean interrupted) { extension.stop(); }

  /** Command finishes when a limit for extending or retracting is hit */
  @Override
  public boolean isFinished() { return extension.isExtended() || extension.isRetracted(); }
}

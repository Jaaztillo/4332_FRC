package frc.robot.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.ExtensionSubsystem;

public class ExtendIntake extends Command {
  // Extension Subsystem to make the intake extend
  private ExtensionSubsystem extension;

  /**
   * ExtendIntake Command Constructor
   * @param extension a subsystem that will let the intake extend
   */
  public ExtendIntake(ExtensionSubsystem extension) {
    this.extension = extension;

    addRequirements(extension);
  }

  /** Extend or Retract the extension based on the intakes state */
  @Override
  public void initialize() {
    // Extend if not extended and is retracted
    if (!extension.isExtended() && extension.isRetracted()) { 
      extension.extend(); 
      return; 
    }

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
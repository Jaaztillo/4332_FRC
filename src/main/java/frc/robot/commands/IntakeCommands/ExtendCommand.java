package frc.robot.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ExtensionSubsystem;

public class ExtendCommand extends Command {
  /** Extension Subsystem which can be used to control the extension */
  private ExtensionSubsystem extension;

  /** Creates a new ExtendCommand. */
  public ExtendCommand(ExtensionSubsystem extension) {
    this.extension = extension;

    addRequirements(extension);
  }
  
  /**
   * Check the state of the extension to extend or retract
   */
  @Override
  public void initialize() {
    if (extension.isExtended()) {
      extension.retract();
      return;
    }

    extension.extend();
  }
  // Stop running command once the extension is at it's setpoint
  @Override
  public boolean isFinished() {
    return extension.atSetPoint();
  }
}

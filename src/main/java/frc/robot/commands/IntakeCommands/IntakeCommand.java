package frc.robot.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeCommand extends Command {
  /** Intake Subsystem which can be used to control the intake */
  private IntakeSubsystem intake;

  /** Creates a new IntakeCommand. */
  public IntakeCommand(IntakeSubsystem intake) {
    this.intake = intake;

    addRequirements(intake);
  }

  /**
   * Run the intake on start
   */
  @Override
  public void initialize() {
    intake.run();
  }

  /**
   * Stop the intake on end
   */
  @Override
  public void end(boolean interrupted) {
    intake.stop();
  }

  // Intake runs while driver holds button down
  @Override
  public boolean isFinished() {
    return false;
  }
}

package frc.robot.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;

// Subsystems
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeFuel extends Command {
  /** Intake Subsystem which is resposible for intaking the fuel */
  private IntakeSubsystem intake;

  /**
   * IntakeFuel Command Constructor
   * @param intake subsystem to intake fuel
   */
  public IntakeFuel(IntakeSubsystem intake) 
  {
    this.intake = intake;

    addRequirements(this.intake);
  }

  /** Runs the intake */
  @Override
  public void initialize() { intake.run(); }

  /** Stop the intake */
  @Override
  public void end(boolean interrupted) { intake.stop(); }

  /** command only stops when driver lets go of the keybind */
  @Override
  public boolean isFinished() { return false; }
}

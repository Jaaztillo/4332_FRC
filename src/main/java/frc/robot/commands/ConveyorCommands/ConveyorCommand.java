package frc.robot.commands.ConveyorCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ConveyorSubsystem;

public class ConveyorCommand extends Command {
  /** Conveyor Subsystem which can be used to control the conveyor */
  private ConveyorSubsystem conveyor;

  public ConveyorCommand(ConveyorSubsystem conveyor) {
    this.conveyor = conveyor;

    addRequirements(conveyor);
  }

  /**
   * Run the conveyor on start
   */
  @Override
  public void initialize() {
    conveyor.run();
  }

  /**
   * Stop the conveyor on end
   */
  @Override
  public void end(boolean interrupted) {
    conveyor.stop();
  }

  // Conveyor runs while driver holds button down
  @Override
  public boolean isFinished() {
    return false;
  }
}

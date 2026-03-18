package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

// Conveyor constants
import frc.robot.Constants.ConveyerConstants;

public class ConveyorSubsystem extends SubsystemBase {
  // Conveyor Talon SRX Motor
  private final WPI_TalonSRX Conveyor = new WPI_TalonSRX(ConveyerConstants.Conveyer_ID);

  /**
   * Configure the Talon SRX Conveyor Motor
   */
  public ConveyorSubsystem() 
  {
    Conveyor.configFactoryDefault();
  }

  /**
   * Run the Conveyor Motor
   */
  public void run ()
  {
    Conveyor.set(-0.8);
  }

  /**
   * Stop the Conveyor Motor
   */
  public void stop ()
  {
    Conveyor.set(0.0);
  }
}
package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

// Intake constants
import frc.robot.Constants.IntakeConstants;

public class IntakeSubsystem extends SubsystemBase {
  // Intake Talon SRX Motor
  private final WPI_TalonSRX Intake = new WPI_TalonSRX(IntakeConstants.Intake_ID);

  /**
   * Configure the Talon SRX Intake Motor
   */
  public IntakeSubsystem() 
  {
    Intake.configFactoryDefault();
  }

  /**
   * Run the Intake Motor
   */
  public void run ()
  {
    Intake.set(1.0);
  }

  /**
   * Stop the Intake Motor
   */
  public void stop ()
  {
    Intake.set(0.0);
  }
}
package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

// Digital Input
import edu.wpi.first.wpilibj.DigitalInput;

// Intake constants
import frc.robot.Constants.ExtendConstants;

public class ExtensionSubsystem extends SubsystemBase {
  // Extension Talon SRX Motor
  private final WPI_TalonSRX Extender = new WPI_TalonSRX(ExtendConstants.extend_ID);

  // Extension Limit Switch
  private final DigitalInput extendedLimit = new DigitalInput(ExtendConstants.extend_Limit_Port);
  private final DigitalInput retractedLimit = new DigitalInput(ExtendConstants.retract_Limit_Port);

  /**
   * Configure the Talon SRX Extension Motor
   */
  public ExtensionSubsystem() {
    Extender.configFactoryDefault();
  }

  /**
   * Extend the Intake
   */
  public void extend ()
  {
    if (isExtended()) { stop(); return; }
    
    Extender.set(1.0);
  }

  /**
   * Retract the Intake
   */
  public void retract ()
  {
    if (isRetracted()) { stop(); return; }
    
    Extender.set(-1.0);
  }

  /**
   * Stop the Extender Motor
   */
  public void stop ()
  {
    Extender.set(0.0);
  }

  public Boolean isExtended () {
    return extendedLimit.get();
  }

  public Boolean isRetracted () {
    return retractedLimit.get();
  }
}

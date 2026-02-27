package frc.robot.subsystems;

// TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Roller Constants
import frc.robot.Constants.RollerConstants;

public class RollerSubsystem extends SubsystemBase {
  // Roller Motor
  private final WPI_TalonSRX TalonSRX = new WPI_TalonSRX(RollerConstants.roller_ID);

  /**
   * Configure the Talon SRX Roller
   */
  public RollerSubsystem() {
    TalonSRX.configFactoryDefault();
  }

  /**
   * Sets the speed of the roller motors.
   */
  public void run() {
    TalonSRX.set(1);
  }

  /**
   * Stops the roller motors by setting their speed to 0.
   */
  public void stop() {
    TalonSRX.stopMotor();
  }
}
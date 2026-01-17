package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
// Import the necessary WPI_TalonSRX class from the phoenix library
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
// Import the Constants class that presumably contains RollerConstants
import frc.robot.Constants.RollerConstants;

/**
 * A subsystem that manages two roller motors controlled by TalonSRX motor
 * controllers.
 */
public class Roller_Subsystem extends SubsystemBase {
  // Define WPI_TalonSRX motor controllers as private final members
  private final WPI_TalonSRX TalonSRX_Left = new WPI_TalonSRX(RollerConstants.roller_left_ID);
  private final WPI_TalonSRX TalonSRX_Right = new WPI_TalonSRX(RollerConstants.roller_right_ID);

  /** Creates a new Roller_Subsystem. */
  public Roller_Subsystem() {
    // Configure the left motor controller
    TalonSRX_Left.setNeutralMode(NeutralMode.Brake); // Motors stop immediately when no power is applied
    TalonSRX_Left.neutralOutput(); // Ensure the motor is not running initially
    TalonSRX_Left.setSensorPhase(false); // Adjust sensor phase if necessary

    // Configure limit switches connected via the Feedback Connector
    TalonSRX_Left.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
    TalonSRX_Left.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
    
    // Set nominal (minimum) output to zero
    TalonSRX_Left.configNominalOutputForward(0.0, 0);
    TalonSRX_Left.configNominalOutputReverse(0.0, 0);

    // Configure the right motor controller to follow the left one and be inverted
    TalonSRX_Right.setInverted(true); // Invert the right motor

    // Configure the right motor to follow the left motor's commands
    TalonSRX_Right.follow(TalonSRX_Left);
    TalonSRX_Right.setNeutralMode(NeutralMode.Brake); // Set the right motor to brake mode as well
  }

  /**
   * Sets the speed of the roller motors.
   */
  public void runRoller() {
    TalonSRX_Left.set(1);
  }

  /**
   * Stops the roller motors by setting their speed to 0.
   */
  public void stopRoller() {
    TalonSRX_Left.set(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run.
    // Use this for tasks that need to run continuously, such as updating
    // SmartDashboard data or checking sensor values.
  }
}
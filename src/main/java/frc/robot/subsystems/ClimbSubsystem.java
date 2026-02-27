package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Smart Dashboard
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// Spark Imports (SparkMax, SparkMaxConfig, MotorType, PersistMode, ResetMode)
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;

// Climb Constants
import frc.robot.Constants.ClimbConstants;

public class ClimbSubsystem extends SubsystemBase {
  // Primary Motor
  private final SparkMax climb_primary = new SparkMax(ClimbConstants.Climb_Left_ID, MotorType.kBrushless);
  
  // Follower Motor
  private final SparkMax climb_follower = new SparkMax(ClimbConstants.Climb_Right_ID, MotorType.kBrushless);

  // Encoder
  private final RelativeEncoder encoder = climb_primary.getEncoder();

  // Climber Controller
  private final SparkClosedLoopController climb_controller = climb_primary.getClosedLoopController();

  // Primary Configuration
  private final SparkMaxConfig primary_config  = new SparkMaxConfig();

  // Follower Configuration
  private final SparkMaxConfig follower_config  = new SparkMaxConfig();

  // Climber extended State
  private boolean extended = false;
  
  /** Creates a new Climb_Subsystem. */
  @SuppressWarnings({"removal"})
  public ClimbSubsystem () {
    // Primary Motor Configurations
    primary_config
      .smartCurrentLimit(80)
      .voltageCompensation(12)
      .idleMode(IdleMode.kBrake);

    // Limit Configurations
    primary_config.softLimit
      .forwardSoftLimitEnabled(true)
      .forwardSoftLimit(35.0) // Max height in inches
      .reverseSoftLimitEnabled(true)
      .reverseSoftLimit(0.0); // Min height in inches
      
    // Motor Encoder Configurations (Inches instead of rotations)
    primary_config.encoder
        .positionConversionFactor(ClimbConstants.PositionFactor)
        .velocityConversionFactor(ClimbConstants.PositionFactor / 60.0);

    // PID Coefficients
    primary_config.closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .p(ClimbConstants.P)
        .i(ClimbConstants.I)
        .d(ClimbConstants.D)
        .outputRange(-1, 1);

    // Follow Motor Configurations
    follower_config
        .smartCurrentLimit(80)
        .voltageCompensation(12)
        .idleMode(IdleMode.kBrake)
        .follow(climb_primary, true);
    
    // Primary and Follower Configuration
    climb_primary.configure(primary_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    climb_follower.configure(follower_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // Set Encoder Position to Initial Position
    encoder.setPosition(0);
  }

  /**
   * Set climber to specific position
   * @param inches position to climb to
   */
  private void setClimbPosition(double inches) { climb_controller.setSetpoint(inches, ControlType.kPosition, ClosedLoopSlot.kSlot0); }

  /**
   * Climb to respective rung based on the parameter given
   * @param level rung to climb to
   */
  public void climb (int level) { 
    switch (level) {
      case 1:
        setClimbPosition(ClimbConstants.First_Rung_Distance);
        break;
      case 2:
        setClimbPosition(ClimbConstants.Second_Rung_Distance);
        break;
      case 3:
        setClimbPosition(ClimbConstants.Third_Rung_Distance);
        break;
      default:
        setClimbPosition(ClimbConstants.Retract_Distance);
        break;
    }
  }

  /**
   * Climb down from Rung 1 | Pneumatics
   */
  public void climbDown () { 
    System.out.println("Climbed Down!!"); 
  }

  /**
   * Climb up | Testing
   */
  public void climbFoward () { climb_primary.set(1.0); }

  /**
   * Climb down | Testing
   */
  public void climbReverse () { climb_primary.set(-1.0); }

  /**
   * Stop Climbing
   */
  public void stop () { climb_primary.set(0); }

  /**
   * Checks if the climber is at target point
   * @return {@code true} if climber is at setpoint, {@code false} otherwise.
   */
  public boolean atSetPoint() {
    double error = Math.abs(climb_controller.getSetpoint() - encoder.getPosition());

    return error < 0.25;
  }

  /**
   * Extend the climber
   */
  public void extend () {
    extended = true;
  }

  /**
   * Retract the climber
   */
  public void retract () {
    extended = false;
  }

  /**
   * Checks if the climber is extended
   * @return the climbers extension state
   */
  public boolean isExtended () { return extended; }

  /** See where the target is to change the constants to climb to level 3 */
  @Override
  public void periodic() {
    SmartDashboard.putNumber("Climbed", climb_controller.getSetpoint());
  }
}

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ExtendConstants;

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

public class ExtensionSubsystem extends SubsystemBase {
  // extension motor
  private final SparkMax extendMotor = new SparkMax(ExtendConstants.Extend_ID, MotorType.kBrushless);

  // extension encoder
  private final RelativeEncoder encoder = extendMotor.getEncoder();

  // extension controller
  private final SparkClosedLoopController extendController = extendMotor.getClosedLoopController();

  // extension Configuration
  private final SparkMaxConfig extendConfig  = new SparkMaxConfig();

  // extended State
  private boolean extended = false;

  /** Creates a new ExtensionSubsystem. */
  @SuppressWarnings({"removal"})
  public ExtensionSubsystem() {
    // Extend Motor Configurations
    extendConfig
      .smartCurrentLimit(80)
      .voltageCompensation(12)
      .idleMode(IdleMode.kBrake);

    // Limit Configurations
    extendConfig.softLimit
      .forwardSoftLimitEnabled(true)
      .forwardSoftLimit(35.0)
      .reverseSoftLimitEnabled(true)
      .reverseSoftLimit(0.0);
      
    // Motor Encoder Configurations (Inches instead of rotations)
    extendConfig.encoder
        .positionConversionFactor(ExtendConstants.PositionFactor)
        .velocityConversionFactor(ExtendConstants.PositionFactor / 60.0);

    // PID Coefficients
    extendConfig.closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .p(ExtendConstants.P)
        .i(ExtendConstants.I)
        .d(ExtendConstants.D)
        .outputRange(-1, 1);
    
    // Primary Configuration
    extendMotor.configure(extendConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  /**
   * Extend the intake
   */
  public void extend () {
    setExtendPosition(ExtendConstants.Extend_Position);
    extended = true;
  }

  /**
   * Retract the intake
   */
  public void retract () {
    setExtendPosition(ExtendConstants.Retract_Position);
    extended = false;
  }

  /**
   * Checks if the climber is at target point
   * @return {@code true} if climber is at setpoint, {@code false} otherwise.
   */
  public boolean atSetPoint() {
    double error = Math.abs(extendController.getSetpoint() - encoder.getPosition());

    return error < 0.25;
  }

  /**
   * Checks if the intake is extended
   * @return the intakes extension state
   */
  public boolean isExtended () { return extended; }

  /**
   * Set intake to specific position
   * @param inches position to climb to
   */
  private void setExtendPosition(double inches) { extendController.setSetpoint(inches, ControlType.kPosition, ClosedLoopSlot.kSlot0); }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

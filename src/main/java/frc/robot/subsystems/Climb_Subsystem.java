// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimbConstants;

// spark imports (SparkMax, SparkMaxConfig, MotorType, PersistMode, ResetMode)
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

public class Climb_Subsystem extends SubsystemBase {
  private final SparkMax climb_primary = new SparkMax(ClimbConstants.climb_left_ID, MotorType.kBrushless);
  private final SparkMax climb_follower = new SparkMax(ClimbConstants.climb_right_ID, MotorType.kBrushless);

  private final RelativeEncoder primary_encoder = climb_primary.getEncoder();
  private final SparkClosedLoopController climb_controller = climb_primary.getClosedLoopController();

  private final SparkMaxConfig primary_config  = new SparkMaxConfig();
  private final SparkMaxConfig follower_config  = new SparkMaxConfig();

  /** Creates a new Climb_Subsystem. */
  @SuppressWarnings({"removal"})
  public Climb_Subsystem() 
  {
    // Basic shooter configuration
    primary_config
      .smartCurrentLimit(60)
      .voltageCompensation(12)
      .idleMode(IdleMode.kBrake);

    // Encoder Conversion (Inches instead of rotations)
    primary_config.encoder
        .positionConversionFactor(ClimbConstants.kPositionFactor)
        .velocityConversionFactor(ClimbConstants.kPositionFactor / 60.0);

    // Soft Limits (Crucial safety to prevent over-extension)
    /*
     * primary_config.softLimit
        .forwardSoftLimitEnabled(true)
        .forwardSoftLimit(20.0) // Max height in inches
        .reverseSoftLimitEnabled(true)
        .reverseSoftLimit(0.0); // Min height in inches
     */

    primary_config.softLimit
        .forwardSoftLimit(25.0) // Give it more breathing room
        .reverseSoftLimit(-2.0); // Allow it to go slightly below 0
      
    // PID Coefficients
    primary_config.closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .p(ClimbConstants.kP)
        .i(ClimbConstants.kI)
        .d(ClimbConstants.kD)
        .allowedClosedLoopError(0.5, ClosedLoopSlot.kSlot0)
        .outputRange(-1, 1);

    // Configure follower
    follower_config
        .idleMode(IdleMode.kBrake)
        .follow(climb_primary, true);

    primary_config.signals.primaryEncoderPositionPeriodMs(20);
    primary_config.signals.primaryEncoderVelocityPeriodMs(20);
    
    climb_primary.configure(primary_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    climb_follower.configure(follower_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    primary_encoder.setPosition(0);
  }

  /*
   * LEARN HOW CLIMB WORKS TO PROGRAM 
   * Manually pull the climber up exactly 10 inches.
   * Print the value of get_primary_tick().
   * Divide 10 by that number. That is your Position Conversion Factor.
   * Put convertion factor in constants and then uncomment the conversion factor configuration
   */

  /** CLIMB TO LEVEL 1 */
  public void climb_level_1 ()
  {
    setClimbPosition(ClimbConstants.First_Rung_Distance);
  }

  public void climb_level_2 ()
  {
    setClimbPosition(ClimbConstants.Second_Rung_Distance);
  }

  public void climb_level_3 ()
  {
    setClimbPosition(ClimbConstants.Third_Rung_Distance);
  }

  public void climb_down_level_1 ()
  {
    setClimbPosition(ClimbConstants.First_Rung_Distance + 5);
  }

  public void climb_self ()
  {
    setClimbPosition(ClimbConstants.Robot_Back_Distance);
  }

  /** UTILS */
  public void setClimbPosition(double inches) {
    climb_controller.setSetpoint(inches, ControlType.kPosition, ClosedLoopSlot.kSlot0);
  }

  public boolean atSetPoint ()
  {
    return climb_controller.isAtSetpoint();
  }
  
  public double get_inches ()
  {
    return primary_encoder.getPosition();
  }

  // For testing
  public void resetClimbEncoder() {
    primary_encoder.setPosition(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Climb Inches", get_inches());
  }
}

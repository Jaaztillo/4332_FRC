/*
  * MASTER PROGRAMMERS WORK
  */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.ClosedLoopSlot;


public class Shooter_Subsystem extends SubsystemBase {
  // Motor and Controller objects
  private final SparkMax shooter_motor = new SparkMax(ShooterConstants.shooter_ID, MotorType.kBrushless);
  private final SparkClosedLoopController shooter_pid = shooter_motor.getClosedLoopController();
  private final RelativeEncoder shooter_encoder = shooter_motor.getEncoder();

  private final SparkMaxConfig shooter_config = new SparkMaxConfig();

  @SuppressWarnings({"removal"})
  public Shooter_Subsystem() {
    // Basic motor settings
    shooter_config.smartCurrentLimit(60);
    shooter_config.voltageCompensation(12);

    // PID Coefficients for velocity control (RPM)
    shooter_config.closedLoop
        .p(ShooterConstants.kP)
        .i(ShooterConstants.kI)
        .d(ShooterConstants.kD)
        .velocityFF(ShooterConstants.kFF) 
        .outputRange(-1, 1);
    
    // Apply configuration
    shooter_motor.configure(shooter_config, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters);
  }

  /** Simple full-power shoot */
  public void shoot() {
    shooter_motor.set(1.0);
  }

  /** Stop the motor */
  public void stop() {
    shooter_motor.set(0);
  }

  /** 
   * Sets the shooter to a specific RPM using the SparkMax PID controller
   * @param rpm Target velocity in Rotations Per Minute
   */
  @SuppressWarnings({"removal"})
  public void set_rpm(double rpm) {
    shooter_pid.setReference(rpm, SparkBase.ControlType.kVelocity, ClosedLoopSlot.kSlot0);
  }

  /** Useful for debugging in Shuffleboard/SmartDashboard */
  public double get_actual_velocity() {
    return shooter_encoder.getVelocity();
  }

  @Override
  public void periodic() {
    // Optional: Log RPM to Dashboard here
  }
}
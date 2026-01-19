// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.ClimbConstants;

// spark imports (SparkMax, SparkMaxConfig, MotorType, PersistMode, ResetMode)
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkBase.PersistMode;

public class Climb_Subsystem extends SubsystemBase {
  private final SparkMax climb_primary = new SparkMax(ClimbConstants.climb_left_ID, MotorType.kBrushless);
  private final SparkMax climb_follower = new SparkMax(ClimbConstants.climb_right_ID, MotorType.kBrushless);

  private final RelativeEncoder primary_encoder = climb_primary.getEncoder();

  private final SparkMaxConfig climb_config = new SparkMaxConfig();

  /** Creates a new Climb_Subsystem. */
  @SuppressWarnings({ "removal", "deprecation" })
  public Climb_Subsystem() 
  {
    // Basic shooter configuration
    climb_config.smartCurrentLimit(60);
    climb_config.voltageCompensation(12);

    /*
    * climb_config.encoder
    *     .positionConversionFactor(ClimbConstants.kInchesPerRevolution)
    *     .velocityConversionFactor(ClimbConstants.kInchesPerRevolution / 60.0); // For Inches/Sec
    */

    // setting leftFollower to follow leftLeader
    climb_config.follow(climb_primary);
    climb_follower.configure(climb_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    climb_config.disableFollowerMode();

    climb_follower.setInverted(true);
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

  }

  public void climb_level_2 ()
  {

  }

  public void climb_level_3 ()
  {

  }

  public void climb_down_level_1 ()
  {

  }

  /** UTILS */
  public double get_inches ()
  {
    return primary_encoder.getPosition();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

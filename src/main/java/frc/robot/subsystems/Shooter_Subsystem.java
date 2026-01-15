// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.ShooterConstants;

// spark imports (SparkMax, SparkMaxConfig, MotorType, PersistMode, ResetMode)
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Shooter_Subsystem extends SubsystemBase {
  private final SparkMax shooter_SparkMax = new SparkMax(ShooterConstants.shooter_ID, MotorType.kBrushless);

  private final SparkMaxConfig shooter_config = new SparkMaxConfig();

  /** Creates a new Shooter_Subsystem. */
  @SuppressWarnings("removal")
  public Shooter_Subsystem() 
  {
    // Basic shooter configuration
    shooter_config.smartCurrentLimit(60);
    shooter_config.voltageCompensation(12);

    shooter_SparkMax.configure(shooter_config, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters);
  }

  /** Shoot Method */
  public void shoot ()
  {
    shooter_SparkMax.set(1);
  }

  /** Stop Method */
  public void stop ()
  {
    shooter_SparkMax.set(0);
  }

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
  }
}

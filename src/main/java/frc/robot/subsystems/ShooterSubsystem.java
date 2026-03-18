package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Math Utility
import edu.wpi.first.math.MathUtil;

// Interpolating Hashmap
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

// SmartDashboard
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.DoublePublisher;

// Spark Max
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.ClosedLoopSlot;

// Constants
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.DashboardIds;

public class ShooterSubsystem extends SubsystemBase {
  // RPM Map
  private final InterpolatingDoubleTreeMap rpmMap = new InterpolatingDoubleTreeMap();

  // Primary Shooter Motor
  private final SparkMax primary = new SparkMax(ShooterConstants.Primary_Shooter_ID, MotorType.kBrushless);

  // Follower Motor
  private final SparkMax follower = new SparkMax(ShooterConstants.Follower_Shooter_ID, MotorType.kBrushless);

  // Shooter PID
  private final SparkClosedLoopController encoder = primary.getClosedLoopController();
  
  // Shooter configuration
  private final SparkMaxConfig primaryConfig = new SparkMaxConfig();
  private final SparkMaxConfig followerConfig = new SparkMaxConfig();

  // Shooting Flag
  private boolean shooting = false;

  // Current rpm
  private Double rpm = 0.0;

  // Dashboard data publishers
  private final DoublePublisher rpmPublisher;

  /**
   * Configure the Shooter Subsystem
   */
  @SuppressWarnings({"removal"})
  public ShooterSubsystem() {
    // Configure current and voltage limit
    primaryConfig
      .smartCurrentLimit(40)
      .voltageCompensation(12);

    // Configure PID values
    primaryConfig.closedLoop
        .p(ShooterConstants.P)
        .i(ShooterConstants.I)
        .d(ShooterConstants.D)
        .velocityFF(ShooterConstants.FF) 
        .outputRange(-1, 1);

    // Configure Follower and invert + follow the primary motor
    followerConfig
      .smartCurrentLimit(40)
      .voltageCompensation(12)
      .idleMode(IdleMode.kCoast)
      .follow(primary, true);

    // Configure PID values
    followerConfig.closedLoop
        .p(ShooterConstants.P)
        .i(ShooterConstants.I)
        .d(ShooterConstants.D)
        .velocityFF(ShooterConstants.FF) 
        .outputRange(-1, 1);
    
    primary.configure(primaryConfig, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters);
    follower.configure(followerConfig, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters);

    

    // Setup RPM Interpolation Map
    setRpmMap();

    // Initialize publishers on SmartDashboard table
    rpmPublisher = NetworkTableInstance.getDefault()
        .getTable("SmartDashboard")
        .getDoubleTopic(DashboardIds.Set_Rpm)
        .publish();

    // Set initial values
    rpmPublisher.set(0.0);
  }

  /**
   * Runs the motor
   */
  public void shoot() 
  {
    shooting = true; // set to true later
  }

  /**
   * Stops the motor
   */
  public void stop() {
    shooting = false;
    primary.stopMotor();
  }

  /** 
   * Sets the shooter to a specific RPM using the SparkMax PID controller
   * @param distance Target distance to determine RPM
   */
  public void setRPM(double distance) {
    distance = MathUtil.clamp(distance, 1.0, 9.0);

    rpm = rpmMap.get(distance);
  }

  /**
   * get true if the motor has reached it's current RPM or false if the motor has not reached it's current RPM
   * @return if the motor has reached it's current RPM
   */
  public boolean atRPM() {
    double velocity = primary.getEncoder().getVelocity();
    double error = Math.abs(velocity - this.rpm);
    
    return error < ShooterConstants.Rpm_Tolerance;
  }

  /** Set up the RPM Map */
  private void setRpmMap () {
    rpmMap.put(1.0, 1225.0);
    rpmMap.put(2.0, 1500.0);
    rpmMap.put(3.0, 1725.0);
    rpmMap.put(4.0, 1850.0);
    rpmMap.put(5.0, 3000.0);
    rpmMap.put(6.0, 3500.0);
    rpmMap.put(7.0, 4250.0);
    rpmMap.put(8.0, 5000.0);
    rpmMap.put(9.0, 6000.0);
  }

  /**
   * Periodically get the client input for RPM
   */
  @Override
  public void periodic() {
    // Read values from Elastic widget (via NetworkTables)
    double rpmInput = NetworkTableInstance.getDefault()
        .getTable("SmartDashboard")
        .getEntry(DashboardIds.Set_Rpm)
        .getDouble(this.rpm);

    // Update values if input changed
    if (rpmInput != this.rpm) this.rpm = rpmInput;

    // Set Motor RPM
    if (!shooting) return;

    encoder.setSetpoint(this.rpm, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
  }
}
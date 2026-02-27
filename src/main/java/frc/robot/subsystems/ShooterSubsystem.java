package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.MathUtil;
// Interpolating Hashmap
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

// Smart Dashboard
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// Spark Max
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.ClosedLoopSlot;

// Servo
import edu.wpi.first.wpilibj.Servo;
import frc.robot.Constants.HoodConstants;
// Shooter Constants
import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {
  // RPM Map
  private final InterpolatingDoubleTreeMap rpmMap = new InterpolatingDoubleTreeMap();

  // Angle Map
  private final InterpolatingDoubleTreeMap angleMap = new InterpolatingDoubleTreeMap();

  // Servo Motor
  private final Servo servo = new Servo(HoodConstants.hood_ID);

  // Shooter Motor
  private final SparkMax shooter_motor = new SparkMax(ShooterConstants.shooter_ID, MotorType.kBrushless);

  // Shooter PID
  private final SparkClosedLoopController shooterPid = shooter_motor.getClosedLoopController();

  // Shooter configuration
  private final SparkMaxConfig shooter_config = new SparkMaxConfig();

  // Current angle
  private Double angle = 0.0;

  // Current rpm
  private Double rpm = 0.0;

  // Shooting Flag
  private boolean shooting = false;

  /**
   * Configure the Shooter Subsystem
   */
  @SuppressWarnings({"removal"})
  public ShooterSubsystem() {
    // Configure current and voltage limit
    shooter_config.smartCurrentLimit(40);
    shooter_config.voltageCompensation(12);

    // Configure PID values
    shooter_config.closedLoop
        .p(ShooterConstants.P)
        .i(ShooterConstants.I)
        .d(ShooterConstants.D)
        .velocityFF(ShooterConstants.FF) 
        .outputRange(-1, 1);
    
    shooter_motor.configure(shooter_config, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters);

    // Setup Interpolation Maps
    setRpmMap();
    setAngleMap();
  }

  /**
   * Runs the motor
   */
  public void shoot() 
  {
    shooting = true;
  }

  /**
   * Stops the motor
   */
  public void stop() {
    shooting = false;
    shooter_motor.stopMotor();
  }

  /**
   * Set angle of the shooter based on the distance
   * @param angle angle to set shooter at based on distance
   */
  public void setAngle (double distance) { 
    distance = MathUtil.clamp(distance, 1.0, 6.0);
    
    this.angle = angleMap.get(distance); 
  }

  /**
   * Testing to get the perfect angle based on the distance
   * @param angle angle to set shooter at
   */
  public void setAngleSimple (double angle) { this.angle = angle; }

  /*
   * For testing to find perfect rpm based on distance
   */
  public void setRpmSimple (double rpm) { this.rpm = rpm; }

  /** 
   * Sets the shooter to a specific RPM using the SparkMax PID controller
   * @param rpm Target velocity in Rotations Per Minute
   */
  public void setRPM(double distance) {
    distance = MathUtil.clamp(distance, 1.0, 6.0);

    rpm = rpmMap.get(distance);
  }

  /**
   * Periodically get the client input for RPM and Angle (For testing)
   */
  @Override
  public void periodic() {
    double rpm = SmartDashboard.getNumber("RPM Input", 800.0);
    setRpmSimple(rpm);

    double angle = SmartDashboard.getNumber("Servo Angle: ", 45.0);
    setAngleSimple(angle);

    // Set Servo Angle
    servo.setAngle(this.angle);

    // Set Motor RPM
    if (!shooting) return;

    shooterPid.setSetpoint(this.rpm, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
  }

  /** Set up the RPM Map */
  private void setRpmMap () {
    rpmMap.put(1.0, 1200.0);
    rpmMap.put(2.0, 1200.0);
    rpmMap.put(3.0, 1400.0);
    rpmMap.put(4.0, 1600.0);
    rpmMap.put(5.0, 1800.0);
    rpmMap.put(6.0, 2000.0);
  }

  /** Set up the Angle Map */
  private void setAngleMap () {
    angleMap.put(1.0, 65.0);
    angleMap.put(2.0, 50.0);
    angleMap.put(3.0, 45.0);
    angleMap.put(4.0, 50.0);
    angleMap.put(5.0, 55.0);
    angleMap.put(6.0, 60.0);
  }
}
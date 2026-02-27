package frc.robot.subsystems;

// Differential Drive
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

// Smart Dashboard
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.TankDriveConstants;

// TalonSRX
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class TankDriveSubsystem extends SubsystemBase 
{
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // Leaders
  private final WPI_TalonSRX left_leader = new WPI_TalonSRX(TankDriveConstants.left_leader_ID);
  private final WPI_TalonSRX right_leader = new WPI_TalonSRX(TankDriveConstants.right_leader_ID);

  // Followers
  private final WPI_TalonSRX left_follower = new WPI_TalonSRX(TankDriveConstants.left_follower_ID);
  private final WPI_TalonSRX right_follower = new WPI_TalonSRX(TankDriveConstants.right_follower_ID);

  // Differential Drive Train
  private final DifferentialDrive drive = new DifferentialDrive(left_leader, right_leader);

  // Powers that can be added to the drive
  private double right = 0;
  private double left = 0;
  
  /**
   * Configure the tank drive motors
   */
  public TankDriveSubsystem () 
  {
    m_chooser.setDefaultOption("Default Auto", TankDriveConstants.kDefault_Auto);
    m_chooser.addOption("My Auto", TankDriveConstants.kCustom_Auto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // Configure Followers
    left_follower.follow(left_leader);
    right_follower.follow(right_leader);

    // Basic Talon SRX Config
    left_leader.configContinuousCurrentLimit(40);
    left_leader.enableCurrentLimit(true);
    left_leader.setNeutralMode(NeutralMode.Brake);

    // Inverse the Follows
    right_leader.setInverted(true);
    right_follower.setInverted(true);
  }
  
  /**
   * Drive the drive train in arcade drive mode but also adds a left and right power to align while driving
   * @param Y Left  Joystick (-Y)
   * @param X Right Joystick (X)
   */
  public void drive(double Y, double X) {
      // Lower down speed if aligning to climber
      if (left > 0 || right > 0) { Y /= 2; X /= 2; }

      // Calculate base motor outputs
      double baseLeft = -Y + X;
      double baseRight = -Y - X;
      
      // Add extra powers to align while driving
      double finalLeft = baseLeft + left;
      double finalRight = baseRight + right;

      // Clamp outputs between -1 and 1
      finalLeft = Math.max(-1, Math.min(finalLeft, 1));
      finalRight = Math.max(-1, Math.min(finalRight, 1));

      // Set to drive motors
      drive.tankDrive(finalLeft, finalRight);
  }

  /**
   * Sets the left and right motor power to add to the motors while driving
   * @param left the left motor power
   * @param right the right motor power
   */
  public void set (double left, double right) {
    this.left = left;
    this.right = right;
  }

  /**
   * Resets the left and right motor power to add to the motors while driving
   */
  public void reset () {
    this.left = 0.0;
    this.right = 0.0;
  }

  /**
   * Stop the drive train
   */
  public void stop ()
  {
    left_leader.stopMotor();
    right_leader.stopMotor();
  }

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
  }
}

package frc.robot.subsystems;

// Differential Drive
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

// Smart Dashboard
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.TankDriveConstants;

// TalonSRX
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class TankDriveSubsystem extends SubsystemBase 
{
  // Leaders
  private final WPI_TalonSRX leftLeader = new WPI_TalonSRX(TankDriveConstants.Left_Leader_ID);
  private final WPI_TalonSRX rightLeader = new WPI_TalonSRX(TankDriveConstants.Right_Leader_ID);

  // Followers
  private final WPI_TalonSRX leftFollower = new WPI_TalonSRX(TankDriveConstants.Left_Follower_ID);
  private final WPI_TalonSRX rightFollower = new WPI_TalonSRX(TankDriveConstants.Right_Follower_ID);

  // Differential Drive Train
  private final DifferentialDrive drive = new DifferentialDrive(leftLeader, rightLeader);

  // Powers that can be added to the drive
  private double right = 0;
  private double left = 0;
  
  /**
   * Configure the tank drive motors
   */
  public TankDriveSubsystem () 
  {
    // Configure Followers
    leftFollower.follow(leftLeader);
    rightFollower.follow(rightLeader);

    // Basic Talon SRX Config
    leftLeader.configContinuousCurrentLimit(40);
    leftLeader.enableCurrentLimit(true);
    leftLeader.setNeutralMode(NeutralMode.Brake);

    // Inverse the Follows
    rightLeader.setInverted(true);
    rightFollower.setInverted(true);
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
    leftLeader.stopMotor();
    rightLeader.stopMotor();
  }

  @Override
  public void periodic()
  {
    SmartDashboard.putBoolean("Aligning Drive", Math.abs(left) > 0 && Math.abs(right) > 0);
  }
}

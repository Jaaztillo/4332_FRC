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

public class DriveSubsystem extends SubsystemBase 
{
  // Leaders
  private final WPI_TalonSRX leftLeader = new WPI_TalonSRX(TankDriveConstants.Left_Leader_ID);
  private final WPI_TalonSRX rightLeader = new WPI_TalonSRX(TankDriveConstants.Right_Leader_ID);

  // Followers
  private final WPI_TalonSRX leftFollower = new WPI_TalonSRX(TankDriveConstants.Left_Follower_ID);
  private final WPI_TalonSRX rightFollower = new WPI_TalonSRX(TankDriveConstants.Right_Follower_ID);

  // Differential Drive Train
  private final DifferentialDrive drive = new DifferentialDrive(leftLeader, rightLeader);

  // Boolean to state weather robot is aligned or not
  private boolean aligned = false;

  // Powers that can be added to the drive
  private double right = 0;
  private double left = 0;
  
  /**
   * Configure the tank drive motors and the robot's odometry and pose
   * @param pigeon the subsystem responsible for getting the robots heading
   */
  public DriveSubsystem () {
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
    // Deadband to detect if driver is actually driving
    boolean driverDriving = Math.abs(Y) > 0 || Math.abs(X) > 0;

    double finalLeft;
    double finalRight;

    /** 
     * Drive the robot plus the alignment needed and if only if the driver is driving else then auto align the robot
     */
    if (driverDriving) {
        // Slow down if aligning
        if (left != 0 || right != 0) {
            Y /= 2.0;
            X /= 2.0;
        }

        // Arcade drive math
        double baseLeft = -Y + X;
        double baseRight = -Y - X;

        // Add alignment offsets
        finalLeft = baseLeft + left;
        finalRight = baseRight + right;
      } else {
        finalLeft = left;
        finalRight = right;
    }

    // Clamp motor outputs
    finalLeft = Math.max(-1.0, Math.min(finalLeft, 1.0));
    finalRight = Math.max(-1.0, Math.min(finalRight, 1.0));

    // Drive motors
    drive.tankDrive(finalLeft, finalRight);
  }

  /**
   * Drive the drive train | Arcade Mode
   * @param foward the speed the motors will be at to drive
   * @param turn the speed the motors will be at to turn
   */
  public void arcadeDrive (double foward, double turn) { drive.arcadeDrive(foward, turn); }

  /**
   * Drive the drive train | Tank Mode
   * @param left the speed the left motor will be at
   * @param right the speed the right motor will be at
   */
  public void set (double left, double right) { drive.tankDrive(left, right); }

  /**
   * Stop the drive train
   */
  public void stop () { leftLeader.stopMotor(); rightLeader.stopMotor(); }

  /**
   * Sets the left and right motor power to add to the motors while driving
   * @param left the left motor power
   * @param right the right motor power
   */
  public void setAlignment (double left, double right) { this.left = left; this.right = right; }

  /**
   * Resets the left and right motor power to add to the motors while driving
   */
  public void resetAlignment () { this.left = 0.0; this.right = 0.0; }

  public void setAligned (boolean aligned) { this.aligned = aligned; }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Aligned", aligned);
  }
}

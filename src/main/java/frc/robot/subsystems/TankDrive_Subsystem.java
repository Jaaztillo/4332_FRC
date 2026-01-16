/*
  * MASTER PROGRAMMERS WORK
  */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.TankDriveConstants;

// TalonSRX
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class TankDrive_Subsystem extends SubsystemBase 
{
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // Define Leaders
  private final WPI_TalonSRX left_leader = new WPI_TalonSRX(TankDriveConstants.left_leader_ID);
  private final WPI_TalonSRX right_leader = new WPI_TalonSRX(TankDriveConstants.right_leader_ID);

  // Define Followers
  private final WPI_TalonSRX left_follower = new WPI_TalonSRX(TankDriveConstants.left_follower_ID);
  private final WPI_TalonSRX right_follower = new WPI_TalonSRX(TankDriveConstants.right_follower_ID);

  private final DifferentialDrive drive = new DifferentialDrive(left_leader, right_leader);

  /** Creates a new TankDrive_Subsystem. */
  public TankDrive_Subsystem () 
  {
    m_chooser.setDefaultOption("Default Auto", TankDriveConstants.kDefault_Auto);
    m_chooser.addOption("My Auto", TankDriveConstants.kCustom_Auto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // Configure Followers
    left_follower.follow(left_leader);
    right_follower.follow(right_leader);

    // Basic Talon SRX Config (Optional)
    left_leader.configContinuousCurrentLimit(40); // Standard FRC limit
    left_leader.enableCurrentLimit(true);
    left_leader.setNeutralMode(NeutralMode.Brake);

    // INVERSE
    left_follower.setInverted(true);
    right_follower.setInverted(true);
  }

  public void drive (double Y, double X)
  {
    drive.arcadeDrive(Y, X);
  }

  public void stop ()
  {
    left_leader.stopMotor();
    right_leader.stopMotor();
  }

  public double get_percent_out_left ()
  {
    return left_leader.get();
  }

  public double get_percent_out_right ()
  {
    return right_leader.get();
  }

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
  }
}

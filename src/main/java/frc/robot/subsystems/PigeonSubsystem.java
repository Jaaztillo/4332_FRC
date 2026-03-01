package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Pigeon 2.0
import com.ctre.phoenix6.hardware.Pigeon2;

// Math Utility
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
// Pigeon Constants
import frc.robot.Constants.PigeonConstants;

public class PigeonSubsystem extends SubsystemBase {
  // Gyroscope
  private Pigeon2 gyroscope = new Pigeon2(PigeonConstants.Pigeon_ID);

  // Pigeon Subsystem constructor
  public PigeonSubsystem() { gyroscope.reset(); }

  /**
   * get the robots heading
   * @return heading
   */
  public Double getHeading()
  {
    return gyroscope.getYaw().getValueAsDouble();
  }

  /**
   * get the robots wrapped heading
   * @return wrapped heading (-180, 180)
   */
  public double getHeadingWrapped () 
  {
      return MathUtil.angleModulus(getHeading());
  }

  /**
   * returns the angle from the robot to the target angle
   * 
   * @param targetHeading angle for robot to look at
   * @return angle from robot to target angle
   */
  public double getHeadingError (double targetHeading) {
    return MathUtil.angleModulus(targetHeading - getHeading());
  }

  public Rotation2d getRotation2d () {
    return Rotation2d.fromDegrees(getHeading());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

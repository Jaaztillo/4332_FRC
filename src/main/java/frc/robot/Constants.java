package frc.robot;

import java.util.HashMap;

// Position and Rotation
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

/* 
 * CAN Ports: 
 *  1, 2, 3, 4 (TANK)
 *  5 (Pigeon 2.0)
 *  6 (Intake)
 *  7 (Conveyor)
 *  8 (Roller)
 *  9 (Extension)
 *  10, 11 (Shooter)
 */

public final class Constants 
{
  public static class AutonomousNames {
    public static final String Test_Auton = "Test";
  }

  public static class DashboardIds {
    // AUTONOMOUS CHOICES
    public static final String Autonomous_Choices = "Autonomous Choices";

    // PLAYING FIELD TO VIEW ROBOTS POSE ON THE FIELD
    public static final String Game_Field = "Game Field";
    public static final Field2d Field = new Field2d();

    // ROBOT POSE
    public static final String Pose = "Pose (x, y, yaw)";

    // APRILTAG HEADING ERROR | APRILTAG DISTANCE
    public static final String Heading_Error = "Heading Error";
    public static final String Distance = "Distance";

    // SET RPM AND ANGLE
    public static final String Set_Rpm = "Set Rpm";
  }

  public static class OperatorConstants {
    // CONTROLLER PORT ID
    public static final int kDriverControllerPort = 0;
  }

  public static class TankDriveConstants {
    // DRIVE MOTOR IDS
    public static final int Left_Leader_ID    = 1;
    public static final int Left_Follower_ID  = 2;

    public static final int Right_Leader_ID   = 3;
    public static final int Right_Follower_ID = 4;

    // Track Width | Meters (MEASURE)
    public static final double Track_Width = 0.6;

    // Wheel Diamater | Meters (MEASURE)
    public static final double Wheel_Diameter = 0.1524;

    // Gear Ratio (ASK)
    public static final double Gear_Ratio = 1.0;
  }

  public static class PigeonConstants {
    // PIGEON ID
    public static final int Pigeon_ID = 5;
  }

  public static class IntakeConstants {
    // INTAKE ID
    public static final int Intake_ID = 6;
  }

  public static class ConveyerConstants {
    // CONVEYOR ID
    public static final int Conveyer_ID = 7;
  }

  public static class RollerConstants {
    // ROLLER ID
    public static final int Roller_ID = 8;
  }

  public static class ExtendConstants {
    // EXTENDER ID
    public static final int Extend_ID = 9;

    // PID VALUES
    public static final double P = 0.1;
    public static final double I = 0.0;
    public static final double D = 0.0;

    // POSITION FACTOR
    public static final double PositionFactor = 0.2;

    // EXTEND POSITION
    public static final double Extend_Position = 2.0;

    // EXTEND POSITION
    public static final double Retract_Position = 0;
  }

  public static class ShooterConstants {
    // PID VALUES
    public static final double P  = 0.0001;
    public static final double I  = 0;
    public static final double D  = 0;
    public static final double FF = 0.0002;

    // SHOOTER ID
    public static final int Primary_Shooter_ID = 10;
    public static final int Follower_Shooter_ID = 11;

    // RPM TOLERANCE
    public static final int Rpm_Tolerance = 10;
  }
  
  public static class LimelightConstants {
    // LIMELIGHT NAME
    public static final String Limelight = "limelight";

    // REAR LIMELIGHT NAME
    public static final String Rear_Limelight = "rear limelight";
  }

  public static class GamePoseConstants {
    // BLUE HUB RELATIVE TO FIELD
    public static final Pose3d Blue_Hub = new Pose3d(
      Units.inchesToMeters(181.29), 
      Units.inchesToMeters(158.32), 
      Units.inchesToMeters(72.00),
      new Rotation3d(0.0, 0.0, Math.toRadians(180)));

    // BLUE OUTPOST RELATIVE TO FIELD
    public static final Pose3d Blue_Outpost = new Pose3d(
      Units.inchesToMeters(20.54), 
      Units.inchesToMeters(25.62),
      Units.inchesToMeters(21.75),
       new Rotation3d(0.0, 0.0, Math.toRadians(0)));
    
    // BLUE ALLIANCE AREA RELATIVE TO FIELD
    public static final Pose3d Blue_Alliance_Area = new Pose3d(
      Units.inchesToMeters(90.00), 
      Units.inchesToMeters(242.09), 
      Units.inchesToMeters(35.00), 
      new Rotation3d(0.0, 0.0, Math.toRadians(0)));

    // RED HUB RELATIVE TO FIELD
    public static final Pose3d Red_Hub = new Pose3d(
      Units.inchesToMeters(468.83),
      Units.inchesToMeters(158.32), 
      Units.inchesToMeters(72.00),
      new Rotation3d(0.0, 0.0, Math.toRadians(0)));

    // RED OUTPOST RELATIVE TO FIELD
    public static final Pose3d Red_Outpost = new Pose3d(
      Units.inchesToMeters(629.58), 
      Units.inchesToMeters(291.02), 
      Units.inchesToMeters(21.75), 
      new Rotation3d(0.0, 0.0, Math.toRadians(180)));
    
    // RED ALLIANCE AREA RELATIVE TO FIELD
    public static final Pose3d Red_Alliance_Area = new Pose3d(
      Units.inchesToMeters(560), 
      Units.inchesToMeters(74.55), 
      Units.inchesToMeters(35.00), 
      new Rotation3d(0.0, 0.0, Math.toRadians(180)));
    
    // APRIL TAG FIELD HASHMAP (ID, Pose3d)
    public static final HashMap<Integer, Pose3d> tagFieldMap = new HashMap<>(); static {
      tagFieldMap.put(1, new Pose3d(Units.inchesToMeters(467.08), Units.inchesToMeters(291.79), Units.inchesToMeters(35.00), new Rotation3d(0.0, 0.0, Math.toRadians(180))));
      tagFieldMap.put(2, new Pose3d(Units.inchesToMeters(468.56), Units.inchesToMeters(182.08), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(90))));
      tagFieldMap.put(3, new Pose3d(Units.inchesToMeters(444.80), Units.inchesToMeters(172.32), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(180))));
      tagFieldMap.put(4, new Pose3d(Units.inchesToMeters(444.80), Units.inchesToMeters(158.32), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(180))));
      tagFieldMap.put(5, new Pose3d(Units.inchesToMeters(468.56), Units.inchesToMeters(134.56), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(270))));
      tagFieldMap.put(6, new Pose3d(Units.inchesToMeters(467.08), Units.inchesToMeters(24.85), Units.inchesToMeters(35.00), new Rotation3d(0.0, 0.0, Math.toRadians(180))));
      tagFieldMap.put(7, new Pose3d(Units.inchesToMeters(470.03), Units.inchesToMeters(24.85), Units.inchesToMeters(35.00), new Rotation3d(0.0, 0.0, Math.toRadians(0))));
      tagFieldMap.put(8, new Pose3d(Units.inchesToMeters(482.56), Units.inchesToMeters(134.56), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(270))));
      tagFieldMap.put(9, new Pose3d(Units.inchesToMeters(492.33), Units.inchesToMeters(144.32), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(0))));
      tagFieldMap.put(10, new Pose3d(Units.inchesToMeters(492.33), Units.inchesToMeters(158.32), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(0))));
      tagFieldMap.put(11, new Pose3d(Units.inchesToMeters(482.56), Units.inchesToMeters(182.08), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(90))));
      tagFieldMap.put(12, new Pose3d(Units.inchesToMeters(470.03), Units.inchesToMeters(291.79), Units.inchesToMeters(35.00), new Rotation3d(0.0, 0.0, Math.toRadians(0))));
      tagFieldMap.put(13, new Pose3d(Units.inchesToMeters(649.58), Units.inchesToMeters(291.02), Units.inchesToMeters(21.75), new Rotation3d(0.0, 0.0, Math.toRadians(180))));
      tagFieldMap.put(14, new Pose3d(Units.inchesToMeters(649.58), Units.inchesToMeters(274.02), Units.inchesToMeters(21.75), new Rotation3d(0.0, 0.0, Math.toRadians(180))));
      tagFieldMap.put(15, new Pose3d(Units.inchesToMeters(649.57), Units.inchesToMeters(169.78), Units.inchesToMeters(21.75), new Rotation3d(0.0, 0.0, Math.toRadians(180))));
      tagFieldMap.put(16, new Pose3d(Units.inchesToMeters(649.57), Units.inchesToMeters(152.78), Units.inchesToMeters(21.75), new Rotation3d(0.0, 0.0, Math.toRadians(180))));
      tagFieldMap.put(17, new Pose3d(Units.inchesToMeters(183.03), Units.inchesToMeters(24.85), Units.inchesToMeters(35.00), new Rotation3d(0.0, 0.0, Math.toRadians(0))));
      tagFieldMap.put(18, new Pose3d(Units.inchesToMeters(181.56), Units.inchesToMeters(134.56), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(270))));
      tagFieldMap.put(19, new Pose3d(Units.inchesToMeters(205.32), Units.inchesToMeters(144.32), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(0))));
      tagFieldMap.put(20, new Pose3d(Units.inchesToMeters(205.32), Units.inchesToMeters(158.32), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(0))));
      tagFieldMap.put(21, new Pose3d(Units.inchesToMeters(181.56), Units.inchesToMeters(182.08), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(90))));
      tagFieldMap.put(22, new Pose3d(Units.inchesToMeters(183.03), Units.inchesToMeters(291.79), Units.inchesToMeters(35.00), new Rotation3d(0.0, 0.0, Math.toRadians(0))));
      tagFieldMap.put(23, new Pose3d(Units.inchesToMeters(180.08), Units.inchesToMeters(291.79), Units.inchesToMeters(35.00), new Rotation3d(0.0, 0.0, Math.toRadians(180))));
      tagFieldMap.put(24, new Pose3d(Units.inchesToMeters(167.56), Units.inchesToMeters(182.08), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(90))));
      tagFieldMap.put(25, new Pose3d(Units.inchesToMeters(157.79), Units.inchesToMeters(172.32), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(180))));
      tagFieldMap.put(26, new Pose3d(Units.inchesToMeters(157.79), Units.inchesToMeters(158.32), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(180))));
      tagFieldMap.put(27, new Pose3d(Units.inchesToMeters(167.56), Units.inchesToMeters(134.56), Units.inchesToMeters(44.25), new Rotation3d(0.0, 0.0, Math.toRadians(270))));
      tagFieldMap.put(28, new Pose3d(Units.inchesToMeters(180.08), Units.inchesToMeters(24.85), Units.inchesToMeters(35.00), new Rotation3d(0.0, 0.0, Math.toRadians(180))));
      tagFieldMap.put(29, new Pose3d(Units.inchesToMeters(0.54), Units.inchesToMeters(25.62), Units.inchesToMeters(21.75), new Rotation3d(0.0, 0.0, Math.toRadians(0))));
      tagFieldMap.put(30, new Pose3d(Units.inchesToMeters(0.54), Units.inchesToMeters(42.62), Units.inchesToMeters(21.75), new Rotation3d(0.0, 0.0, Math.toRadians(0))));
      tagFieldMap.put(31, new Pose3d(Units.inchesToMeters(0.55), Units.inchesToMeters(146.86), Units.inchesToMeters(21.75), new Rotation3d(0.0, 0.0, Math.toRadians(0))));
      tagFieldMap.put(32, new Pose3d(Units.inchesToMeters(0.55), Units.inchesToMeters(163.86), Units.inchesToMeters(21.75), new Rotation3d(0.0, 0.0, Math.toRadians(0))));
    }
  }
}
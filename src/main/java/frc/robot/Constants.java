package frc.robot;

// Java utils (Hashmap, Arrays)
import java.util.*;

// Mediam Filter
import edu.wpi.first.math.filter.MedianFilter;

/* 
 * Ports: 
 *  1, 2, 3, 4 (TANK)
 *  5 (Pigeon 2.0)
 *  6 (Intake)
 *  7 (Shooter)
 *  8 (Roller)
 *  9 (Hood)
 *  10, 11 (Climb)
 *  12 (Extend)
 *  13 (Extend Limit Switch)
 *  14 (Retract Limit Switch)
 */

public final class Constants 
{

  public static class OperatorConstants 
  {
    // CONTROLLER PORT ID
    public static final int kDriverControllerPort = 0;
  }

  public static class TankDriveConstants 
  {
    // DRIVE MOTOR IDS
    public static final int Left_Leader_ID    = 1;
    public static final int Left_Follower_ID  = 2;

    public static final int Right_Leader_ID   = 3;
    public static final int Right_Follower_ID = 4;
  }

  public static class PigeonConstants
  {
    // PIGEON ID
    public static final int Pigeon_ID = 5;
  }

  public static class IntakeConstants
  {
    // INTAKE ID
    public static final int Intake_ID = 6;
  }

  public static class ShooterConstants
  {
    // PID VALUES
    public static final double P  = 0.0001;
    public static final double I  = 0;
    public static final double D  = 0;
    public static final double FF = 0.0002;

    // SHOOTER ID
    public static final int Shooter_ID = 7;
  }

  public static class RollerConstants
  {
    // ROLLER ID
    public static final int Roller_ID = 8;
  }

  public static class HoodConstants
  {
    // Distance Factor
    public static final double Factor = 1;

    // HOOD ID
    public static final int Hood_ID = 9;
  }

  public static class ClimbConstants
  {
    // PID VALUES
    public static final double P = 0.1;
    public static final double I = 0.0;
    public static final double D = 0.0;

    // POSITION FACTOR
    public static final double PositionFactor = 0.2;

    // RUNG DISTANCES
    public static final double First_Rung_Distance  = 24;
    public static final double Second_Rung_Distance = 16;
    public static final double Third_Rung_Distance  = 16;
    public static final double Retract_Distance     = 0;

    // CLIMB MOTOR IDS
    public static final int Climb_Left_ID   = 11;
    public static final int Climb_Right_ID  = 10;
  }

  public static class ExtendConstants {
    // EXTENDER ID
    public static final int Extend_ID = 12;

    public static final int Extend_Limit_Port = 13;
    public static final int Retract_Limit_Port = 14;
  }
  
  public static class LimelightConstants
  {
    // LIMELIGHT NAME
    public static final String Name = "limelight";

    // FILTER FOR LOWER LATENCY ON DISTANCE CALCULATIONS
    public static final MedianFilter Distance_Filter = new MedianFilter(3);

    // APRILTAG SHOOTER IDS
    public static final Set<Integer> Apriltag_Shoot = new HashSet<>(Arrays.asList(
      5, 8, 9, 10, 11, 2, 21, 24, 25, 26, 27, 18
    ));

    // APRILTAG CLIMBER IDS
    public static final Set<Integer> Apriltag_Climb = new HashSet<>(Arrays.asList(
      10, 26
    ));
  }
}

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
    // DRIVE SETTINGS
    public static final String kDefault_Auto = "Default";
    public static final String kCustom_Auto = "My Auto";
    public String m_autoSelected;

    // STATE (USED TO TELL IF ROBOT IS INVERSED OR NOT)
    public static final int state = 0;
  
    // DRIVE MOTOR IDS
    public static final int left_leader_ID    = 1;
    public static final int left_follower_ID  = 2;

    public static final int right_leader_ID   = 3;
    public static final int right_follower_ID = 4;
  }

  public static class PigeonConstants
  {
    // PIGEON ID
    public static final int Pigeon_ID = 5;
  }

  public static class IntakeConstants
  {
    // INTAKE ID
    public static final int intake_ID = 6;
  }

  public static class ShooterConstants
  {
    // PID VALUES
    public static final double P  = 0.0001;
    public static final double I  = 0;
    public static final double D  = 0;
    public static final double FF = 0.0002;

    // SHOOTER ID
    public static final int shooter_ID = 7;
  }

  public static class RollerConstants
  {
    // ROLLER ID
    public static final int roller_ID = 8;
  }

  public static class HoodConstants
  {
    // Distance Factor
    public static final double Factor = 1;

    // HOOD ID
    public static final int hood_ID = 9;
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
    public static final int climb_left_ID   = 11;
    public static final int climb_right_ID  = 10;
  }

  public static class ExtendConstants {
    // EXTENDER ID
    public static final int extend_ID = 12;

    public static final int extend_Limit_Port = 13;
    public static final int retract_Limit_Port = 14;
  }
  
  public static class LimelightConstants
  {
    // LIMELIGHT NAME
    public static final String name = "limelight";

    // TIMEOUT CONSTANT (SAFTEY)
    public static final double ID_TIMEOUT = 0.25;

    // FILTER FOR LOWER LATENCY ON DISTANCE CALCULATIONS
    public static final MedianFilter distanceFilter = new MedianFilter(3);

    // APRILTAG SHOOTER IDS
    public static final Set<Integer> APRILTAG_SHOOT = new HashSet<>(Arrays.asList(
      5, 8, 9, 10, 11, 2, 21, 24, 25, 26, 27, 18
    ));

    // APRILTAG CLIMBER IDS
    public static final Set<Integer> APRILTAG_CLIMB = new HashSet<>(Arrays.asList(
      10, 26
    ));
  }

  public static class Climb_Align_Constants
  {
    public static final double KTURNP     = 0.03;         // turning strength
    public static final double KYAWP      = 0.05;         // turning strength
    public static final double KDRIVEP    = 0.8;          // forward/backward strength
    public static final double MAX_OUTPUT = 0.6;          // max motor output

    public static final double DESIRED_DISTANCE_01 = 0.5; // meters from target
    public static final double DESIRED_DISTANCE_02 = 2.0; // meters from target

    public static final double X_OFFSET = 0.682874;   // OFFSET FROM CENTER HUB APRIL TAG TO TOWER CENTER
    
    public static final double TX_TOLERANCE = 1.0;        // TOLERANCE FOR X AXIS
    public static final double Z_TOLERANCE  = 0.01;       // TOLAREANCE FOR DISTANCE

    public static final double FINAL_YAW = 0.0;        // Where the robot will look in the end
  }
}

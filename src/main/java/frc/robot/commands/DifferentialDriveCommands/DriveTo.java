package frc.robot.commands.DifferentialDriveCommands;


import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.PathPoint;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightSubsystem;

public class DriveTo extends Command {
  private final double Position_Tolarance = 0.2;
  private static final double kP_Drive = 1.2;
  private static final double kP_Turn = 2.5;
  private final double Max_Speed = 0.6;
  private final double Max_Turn = 0.6;

  private DriveSubsystem tankDrive;
  private LimelightSubsystem limelight;

  private List<PathPoint> points;
  private int currentPointIndex;

  private String pathName;

  private PathPlannerPath path;

  private boolean pathReversed;

  public DriveTo(DriveSubsystem tankDrive, LimelightSubsystem limelight, String pathName) {
    this.tankDrive = tankDrive;
    this.limelight = limelight;

    this.pathName = pathName;

    currentPointIndex = 0;
  }

  // Initialize the path and points based on the path chosen
  @Override
  public void initialize() {
    try {
      path = PathPlannerPath.fromPathFile(pathName);
      points = path.getAllPathPoints();
      currentPointIndex = 0;

      pathReversed = path.isReversed();
    } catch (IOException | FileVersionException | ParseException e) {
      System.out.println("Path unavailable!");
      path = null;
      points = null;
    }
  }

  @Override
  public void execute() {
      if (path == null || points == null) return;

      Pose3d currentPose = limelight.getFieldRelativePose();

      if (currentPose == null) {
          tankDrive.stop();
          return;
      }

      if (currentPointIndex >= points.size()) {
          tankDrive.stop();
          return;
      }

      // Current target point
      Translation2d target = points.get(currentPointIndex).position;

      // Distance to target
      double dx = target.getX() - currentPose.getX();
      double dy = target.getY() - currentPose.getY();
      
      double distance = Math.hypot(dx, dy);

      // Convert target point to Pose3d for Limelight turnAngle
      Pose3d targetPose = new Pose3d(target.getX(), target.getY(), 0.0, currentPose.getRotation());

      // Proportional Driving
      double forward = kP_Drive * distance;

      forward = Math.min(forward, Max_Speed);

      // Proportional Turning
      double turn = kP_Turn * limelight.turnAngle(targetPose);

      turn = Math.max(-Max_Turn, Math.min(turn, Max_Turn));

      // Reverse the robot drive and turn if the path is reversed
      if (pathReversed) {
        forward = -forward;
        turn = -turn;
      }

      // Drive the chassis
      tankDrive.arcadeDrive(forward, turn);

      System.out.printf("Point %d | dist=%.2f m | tol=%.2f m%n",
        currentPointIndex, distance, Position_Tolarance
      );
      
      // Go to the next point once we're close enough to the point
      if (distance < Position_Tolarance && Math.abs(turn) < 0.15) {
          currentPointIndex++;
      }
  }

  // Stop driving at the end of the path
  @Override
  public void end(boolean interrupted) { tankDrive.stop(); }

  // Command ends when all points have been traveled
 @Override
  public boolean isFinished() {
    return points != null && currentPointIndex >= points.size();
  }
}
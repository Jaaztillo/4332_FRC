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
import frc.robot.subsystems.PigeonSubsystem;

public class DriveTo extends Command {
  private final double Position_Tolarance = 0.05;
  private final double Max_Speed = 0.6;
  private final double Max_Turn = 0.6;

  private DriveSubsystem tankDrive;
  private LimelightSubsystem limelight;
  private PigeonSubsystem pigeon;

  private List<PathPoint> points;
  private int currentPointIndex;

  private String pathName;

  private PathPlannerPath path;

  public DriveTo(DriveSubsystem tankDrive, LimelightSubsystem limelight, PigeonSubsystem pigeon, String pathName) {
    this.tankDrive = tankDrive;
    this.limelight = limelight;
    this.pigeon = pigeon;

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
    } catch (IOException | FileVersionException | ParseException e) {
      System.out.println("Path unavailable!");
      path = null;
      points = null;
    }
  }

  @Override
  public void execute() {
    // Ensure there is a path and points
    if (path == null || points == null) return;

    // Get Robot Pose relative to the Field
    Pose3d currentPose = limelight.getFieldRelativePose();

    // Ensure there is a currentPose for the robot
    if (currentPose == null) {
      tankDrive.stop();
      return;
    }

    // End Once there is no more points left
    if (currentPointIndex >= points.size()) {
      tankDrive.stop();
      return;
    }

    // Current target point
    Translation2d target = points.get(currentPointIndex).position;

    double dx = target.getX() - currentPose.getX();
    double dy = target.getY() - currentPose.getY();

    double distance = Math.hypot(dx, dy);

    // Field-relative angle
    double angleToTarget = Math.toDegrees(Math.atan2(dy, dx));

    // Robot-relative heading error
    double turn = pigeon.getHeadingError(angleToTarget);

    // Simple proportional control
    double forward = Math.min(distance, Max_Speed);
    turn = Math.max(-Max_Turn, Math.min(turn, Max_Turn));

    tankDrive.arcadeDrive(forward, turn);

    // Move to next path point
    if (distance < Position_Tolarance) {
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

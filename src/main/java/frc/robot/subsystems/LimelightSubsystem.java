package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.geometry.Rotation3d;

import edu.wpi.first.networktables.NetworkTableInstance;

import frc.robot.Constants.LimelightConstants;

/**
 * LimelightSubsystem
 * 
 * Handles multiple Limelight cameras to estimate the robot's position on the field
 * using fiducial/MegaTag vision targets. Chooses the best camera reading and
 * sends it to the DriveSubsystem for vision-aided odometry.
 */
public class LimelightSubsystem extends SubsystemBase {
    private Pose2d bestPose = null;
    private String bestCam = null;

    /**
     * Constructor for LimelightSubsystem
     */
    public LimelightSubsystem() {}

    /**
     * Get the latest best vision pose from the Limelight cameras
     * 
     * @return robot-relative Pose2d or null if no target detected
     */
    public Pose2d getBestVisionPose() {
        return bestPose;
    }

    /**
     * Get the latency of the best camera currently used for vision
     * 
     * @return latency in seconds, or 0 if no camera is active
     */
    public double getBestCameraLatency() {
        if (bestCam == null) return 0.0;
        return getCameraLatency(bestCam);
    }

    /**
     * Get the camera's processing latency reported by the Limelight
     * 
     * @param cam the Limelight camera name (front or rear)
     * @return latency in seconds
     */
    private double getCameraLatency(String cam) {
        double latencyMs = NetworkTableInstance.getDefault()
            .getTable(cam)
            .getEntry("tl") // Limelight tag latency
            .getDouble(0.0);
        return latencyMs / 1000.0;
    }

    /**
     * Estimate a confidence metric for the fiducial detection.
     * Uses Limelight's reported pose ambiguity and target validity.
     * Gives a slight boost to Limelight 3.
     * 
     * @param cam the Limelight camera name
     * @return a confidence value between 0.0 (worst) and 1.0 (best)
     */
    private double getTargetConfidence(String cam) {
      var table = NetworkTableInstance.getDefault().getTable(cam);
      
      // Check if a valid target exists
      double tv = table.getEntry("tv").getDouble(0.0);
      if (tv < 1.0) return 0.0; // no target, confidence = 0
      
      // Limelight reports pose ambiguity (0 = perfect, higher = worse)
      double poseAmbiguity = table.getEntry("ta").getDouble(0.0);
      
      // Base confidence inversely proportional to ambiguity
      double confidence = 1.0 / (poseAmbiguity + 1.0); 
      
      // Boost confidence slightly if using Limelight 3 (better camera)
      if (cam.equals(LimelightConstants.Limelight)) {
          confidence *= 1.1; // 10% bonus
      }
      
      // Clamp to 0-1
      confidence = Math.min(1.0, Math.max(0.0, confidence));
      
      return confidence;
    }

    /**
     * Converts a camera-relative Pose2d into a robot-relative Pose2d
     * using the camera-to-robot transform
     * 
     * @param camPose the pose relative to the camera
     * @param cam the Limelight camera name
     * @return robot-relative Pose2d
     */
    private Pose2d cameraPoseToRobotPose(Pose2d camPose, String cam) {
      Transform3d t = getCameraToRobotTransform(cam);

      // Only consider yaw rotation (Z axis) for 2D Pose2d
      Rotation2d camToRobot2d = new Rotation2d(t.getRotation().getZ());

      Rotation2d robotRot = camPose.getRotation().plus(camToRobot2d);
      double robotX = camPose.getX() - t.getX();
      double robotY = camPose.getY() - t.getY();

      return new Pose2d(robotX, robotY, robotRot);
    }

    /**
     * Returns the transformation from camera coordinates to robot coordinates
     * 
     * @param cam the Limelight camera name
     * @return Transform3d representing camera-to-robot offset
     */
    private Transform3d getCameraToRobotTransform(String cam) {
      if (cam.equals(LimelightConstants.Limelight)) {
          // Front Camera Mounted Pose
          return new Transform3d(
              new Translation3d(0.2, 0.0, 0.5),
              new Rotation3d(0, 0, 0)
          );
      } else {
          // Reat Camera Mounted Pose
          return new Transform3d(
              new Translation3d(-0.3, 0.0, 0.4),
              new Rotation3d(Math.toRadians(-15), 0, Math.toRadians(45))
          );
      }
    }

    /**
     * Returns the ID of the fiducial/MegaTag currently detected by a Limelight
     * 
     * @param cam the Limelight camera name
     * @return fiducial ID if valid, -1 if none detected
     */
    private int getFiducialID(String cam) {
      int id = (int) NetworkTableInstance.getDefault()
              .getTable(cam)
              .getEntry("tid")
              .getDouble(-1.0);
      return id >= 0 ? id : -1;
    }

    /**
     * Checks both front and rear Limelight cameras for fiducial targets,
     * selects the reading with the best confidence, and sends the
     * resulting Pose2d and timestamp to the DriveSubsystem.
     */
    @Override
    public void periodic() {
      String[] cams = {LimelightConstants.Limelight, LimelightConstants.Rear_Limelight};
        
      bestCam = null;
      double bestConfidence = Double.MAX_VALUE;

      for (String cam : cams) {
          if (getFiducialID(cam) != -1) {
              // Get camera-relative tx, ty (degrees)
              double tx = NetworkTableInstance.getDefault().getTable(cam).getEntry("tx").getDouble(0.0);
              double ty = NetworkTableInstance.getDefault().getTable(cam).getEntry("ty").getDouble(0.0);

              // Rough estimate of camera pose on field
              Pose2d camPose = new Pose2d(tx, ty, new Rotation2d(Math.toRadians(tx)));
              Pose2d robotPose = cameraPoseToRobotPose(camPose, cam);

              double confidence = getTargetConfidence(cam);

              // Check confidence to detect the best pose and cam
              if (confidence < bestConfidence) {
                  bestConfidence = confidence;
                  bestPose = robotPose;
                  bestCam = cam;
              }
          }
      }
  }
}
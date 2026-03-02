package frc.robot.commands.AutonomousCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// Subsystems
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.DriveSubsystem;

// Commands
import frc.robot.commands.DifferentialDriveCommands.AlignToTarget;
import frc.robot.commands.ShooterCommands.AlignShooter;

public class AlignToHub extends SequentialCommandGroup {
  public AlignToHub(DriveSubsystem tankDrive, ShooterSubsystem shooter, LimelightSubsystem limelight) {
    addCommands(
      new AlignToTarget(tankDrive, limelight),
      new AlignShooter(shooter, limelight)
    );
  }
}

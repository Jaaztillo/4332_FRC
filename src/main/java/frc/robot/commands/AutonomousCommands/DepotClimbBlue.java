package frc.robot.commands.AutonomousCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberSubsystem;

// Subsystems
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.RollerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.commands.ClimbCommands.Climb;
// Commands
import frc.robot.commands.DifferentialDriveCommands.DriveTo;
import frc.robot.commands.ShooterCommands.ShootFuel;

public class DepotClimbBlue extends SequentialCommandGroup {
  public DepotClimbBlue( DriveSubsystem tankDrive, LimelightSubsystem limelight, 
      ShooterSubsystem shooter, RollerSubsystem roller, ClimberSubsystem climber) {
    
    addCommands(
      new DriveTo(tankDrive, limelight, "collectDepotBlue"),
      new DriveTo(tankDrive, limelight, "backupDepotBlue"),
      new DriveTo(tankDrive, limelight, "shootBlue"),
      new AlignToHub(tankDrive, shooter, limelight),
      new ShootFuel(shooter, roller, limelight).withTimeout(3.0),
      new DriveTo(tankDrive, limelight, "toHubBlue"),
      new DriveTo(tankDrive, limelight, "toClimberBlue"),
      new Climb(climber, 1)
    );
  }
}
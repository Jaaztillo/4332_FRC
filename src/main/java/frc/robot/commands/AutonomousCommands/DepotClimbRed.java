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

public class DepotClimbRed extends SequentialCommandGroup {
  public DepotClimbRed( DriveSubsystem tankDrive, LimelightSubsystem limelight, 
      ShooterSubsystem shooter, RollerSubsystem roller, ClimberSubsystem climber) {
    
    addCommands(
      new DriveTo(tankDrive, limelight, "collectDepotRed"),
      new DriveTo(tankDrive, limelight, "backupDepotRed"),
      new DriveTo(tankDrive, limelight, "shootRed"),
      new AlignToHub(tankDrive, shooter, limelight),
      new ShootFuel(shooter, roller).withTimeout(3.0),
      new DriveTo(tankDrive, limelight, "toHubRed"),
      new DriveTo(tankDrive, limelight, "toClimberRed"),
      new Climb(climber, 1)
    );
  }
}
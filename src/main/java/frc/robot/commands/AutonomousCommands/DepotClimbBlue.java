package frc.robot.commands.AutonomousCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClimberSubsystem;
// Subsystems
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.PigeonSubsystem;
import frc.robot.subsystems.RollerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.commands.ClimbCommands.Climb;
// Commands
import frc.robot.commands.DifferentialDriveCommands.DriveTo;
import frc.robot.commands.ShooterCommands.ShootFuel;

public class DepotClimbBlue extends SequentialCommandGroup {
  public DepotClimbBlue( DriveSubsystem tankDrive, LimelightSubsystem limelight, PigeonSubsystem pigeon, 
      ShooterSubsystem shooter, RollerSubsystem roller, ClimberSubsystem climber) {
    
    addCommands(
      new DriveTo(tankDrive, limelight, pigeon, "collectDepotBlue"),
      new DriveTo(tankDrive, limelight, pigeon, "backupDepotBlue"),
      new DriveTo(tankDrive, limelight, pigeon, "shootBlue"),
      new AlignToHub(tankDrive, shooter, limelight, pigeon),
      new ShootFuel(shooter, roller).withTimeout(3.0),
      new DriveTo(tankDrive, limelight, pigeon, "toHubBlue"),
      new DriveTo(tankDrive, limelight, pigeon, "toClimberBlue"),
      new Climb(climber, 1)
    );
  }
}

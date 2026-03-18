package frc.robot;

// Commands
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

// Xbox Controller
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

// Constants
import frc.robot.Constants.*;
import frc.robot.commands.ConveyorCommands.ConveyorCommand;
import frc.robot.commands.DifferentialDriveCommands.DriveTo;
import frc.robot.commands.DifferentialDriveCommands.TankDrive;
import frc.robot.commands.IntakeCommands.ExtendCommand;
import frc.robot.commands.IntakeCommands.IntakeCommand;
import frc.robot.commands.ShooterCommands.ShootFuel;
import frc.robot.subsystems.ConveyorSubsystem;

// Subsystems
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ExtensionSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.RollerSubsystem;

import frc.robot.subsystems.IntakeSubsystem;
/**
 * Robot Container containing subsystems and commands
 */
import frc.robot.subsystems.LimelightSubsystem;
public class RobotContainer {
  // Subsystems
  private ShooterSubsystem shooter = new ShooterSubsystem();
  private DriveSubsystem drive = new DriveSubsystem();
  private RollerSubsystem roller = new RollerSubsystem();
  private IntakeSubsystem intake = new IntakeSubsystem();
  private ExtensionSubsystem extend = new ExtensionSubsystem();
  private ConveyorSubsystem conveyor = new ConveyorSubsystem();
  private LimelightSubsystem limelight = new LimelightSubsystem();

  // Xbox controller]
  private final CommandXboxController controller01 = new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** 
   * The container for the robot. Contains subsystems, OI devices, and commands. 
  */
  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings () {

    /*********************************************************************************
     *                      //====== KEYBINDS ======\\
     * 
     *                        LEFT JOYSTICK  | DRIVE
     *                        RIGHT JOYSTICK | TURN
     * 
     *                        LEFT BUMPER    | EXTEND / RETRACT INTAKE                       
     *                        LEFT TRIGGER   | INTAKE
     *                        
     *                        RIGHT TRIGGER  | SHOOT TO TARGET
     * 
     **********************************************************************************/

    // What if aldo doesn't align with anything what if when he pressed the shoot button depending on the robots pose on the field
    // We shoot either to our alliance side, outpost or hub

    // == TANKDRIVE BINDING == \\
    drive.setDefaultCommand(
        new TankDrive(drive,
          () -> controller01.getLeftY(), () -> controller01.getRightX())
    );

    // == LIMELIGHT DEFAULT COMMAND == \\
    limelight.setDefaultCommand(
      new RunCommand(() -> {
        Pose2d pose = limelight.getBestVisionPose();
        double latency = limelight.getBestCameraLatency();
        if (pose != null) {
            drive.addVisionMeasurement(pose, Timer.getFPGATimestamp() - latency);
        }
      }, limelight)
    );
    
    // ==== INTAKE BINDING ==== \\

    controller01.leftTrigger().whileTrue(new ParallelCommandGroup(
      new IntakeCommand(intake),
      new ConveyorCommand(conveyor)
    ));

    controller01.leftBumper().onTrue(new ExtendCommand(extend));

    // ==== SHOOTER BINDING ==== \\

    // On right trigger run sequence command align to target then shoot
    controller01.rightTrigger().whileTrue(new ShootFuel(shooter, roller));
  }

  /**
   * gets the autonomous command with the name given to it
   * @param autoName the name of the autonomous you want to perform
   * @return the autnomous command with the name given
   */
  public Command AutonomousCommand (String autoName) {
    switch (autoName) {
      case AutonomousNames.Test_Auton:
        return new DriveTo(drive, 1.0, 0.0).withTimeout(1.0);
      default:
        return null;
    }
  }
}
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.DifferentialDrive__Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.TankDrive_Subsystem;
import frc.robot.subsystems.Pigeon_Subsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TankDrive_Inverse_Command extends Command {
  private TankDrive_Subsystem TankDrive_Subsystem;
  private Pigeon_Subsystem Pigeon_Subsystem;

  private boolean Rotated = false;

  /** Creates a new TankDrive_Inverse_Command. */
  public TankDrive_Inverse_Command(TankDrive_Subsystem TankDrive_Subsystem, Pigeon_Subsystem Pigeon_Subsystem) {
    this.TankDrive_Subsystem = TankDrive_Subsystem;
    this.Pigeon_Subsystem = Pigeon_Subsystem;

    addRequirements(TankDrive_Subsystem, Pigeon_Subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    // INVERT CONTROLS
    TankDrive_Subsystem.toggleInverted();

    // ROTATE ROBOT 180 DEGREES
    double current_yaw = Pigeon_Subsystem.getHeading();
    double target_yaw = current_yaw + 180.0;

    if (Math.abs(target_yaw - current_yaw) > 2.0) {
        TankDrive_Subsystem.set(1, -1); 
        Rotated = false;
    } else {
        TankDrive_Subsystem.stop();
        Rotated = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Rotated;
  }
}

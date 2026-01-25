/*
  * MASTER PROGRAMMERS WORK
  */
  
package frc.robot.commands.DifferentialDrive__Commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.TankDrive_Subsystem;

public class TankDrive_Command extends Command {
  private TankDrive_Subsystem tank_drive;

  private DoubleSupplier left_percent;
  private DoubleSupplier right_percent;

  /** Creates a new TankDrive_Command. */
  public TankDrive_Command(TankDrive_Subsystem tank_drive, DoubleSupplier left_percent, DoubleSupplier right_percent) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.tank_drive = tank_drive;

    this.left_percent = left_percent;
    this.right_percent = right_percent;

    addRequirements(this.tank_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    // Deadband it so that the robot won't nudge even if the driver let's go of the joystick
    double foward_percent = tank_drive.applyInversion(left_percent.getAsDouble());
    double rotation_percent = tank_drive.applyInversion(right_percent.getAsDouble());

    double foward_percent_deadband = MathUtil.applyDeadband(foward_percent, 0.07);
    double rotation_percent_deadband = MathUtil.applyDeadband(rotation_percent, 0.07);

    tank_drive.drive(foward_percent_deadband, rotation_percent_deadband);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    tank_drive.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

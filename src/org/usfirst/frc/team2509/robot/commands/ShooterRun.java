package org.usfirst.frc.team2509.robot.commands;

import org.usfirst.frc.team2509.robot.Robot;
import org.usfirst.frc.team2509.robot.RobotMap;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

/**
 * @author Nate
 */
public class ShooterRun extends Command {
	private double TARGETSPEED = 4000;
	private final CANTalon MOTOR = Robot.shooter.getShoot();
	private final Talon KICKER = Robot.shooter.getKicker();
	private final Talon GATE = RobotMap.GATE;
    public ShooterRun() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	MOTOR.set(TARGETSPEED);
    	System.out.println("SHOOTER STARTING");
    	Timer.delay(0.5);
		System.out.println("GATE OPENING");
		GATE.set(0.3);
    	Timer.delay(0.125);
    	GATE.set(0);
    	System.out.println("AUGER STARTING");
    	KICKER.set(1);
    }
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//MOTOR.set(20000);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
	protected void end() {

    	MOTOR.changeControlMode(TalonControlMode.PercentVbus);
    	MOTOR.set(0);
    	MOTOR.changeControlMode(TalonControlMode.Speed);
    	KICKER.set(0.0);
    	GATE.set(-0.3);
    	Timer.delay(0.125);
    	GATE.set(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}

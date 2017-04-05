package org.usfirst.frc.team2509.robot.commands;

import org.usfirst.frc.team2509.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Command;
/**
 * 
 * @author Alex & Mason
 * This took Alex and Mason 3hrs to do, and it would have taken Nate 5 minutes. This should tell you out
 */



public class ClimbUp extends Command {
	private CANTalon motor = RobotMap.CLIMB_MOTOR;
	private CANTalon altmotor = RobotMap.CLIMB_ALT;
    public ClimbUp() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	motor.set(1.0);
    	altmotor.set(1.0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	motor.set(0);
    	altmotor.set(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}

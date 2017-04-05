/**
 * @author Mason
 */
package org.usfirst.frc.team2509.robot.commands;

import org.usfirst.frc.team2509.robot.Robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Command;

/**
 *@author Nate
 */
public class SweeperReverse extends Command {
	private final CANTalon MOTOR = Robot.sweeper.MOTOR;
    public SweeperReverse() {
    	requires(Robot.sweeper);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	MOTOR.set(-0.5);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	MOTOR.set(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}

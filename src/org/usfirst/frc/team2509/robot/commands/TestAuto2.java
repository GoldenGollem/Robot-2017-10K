package org.usfirst.frc.team2509.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TestAuto2 extends Command {

    public TestAuto2() {
    }

    // Called just before this Command runs the first time 
    protected void initialize() {
    	
    }
    protected void execute() {
    }
    

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(Timer.getMatchTime()>0&&Timer.getMatchTime()<15){
        	return false;
        }else{
        	return true;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
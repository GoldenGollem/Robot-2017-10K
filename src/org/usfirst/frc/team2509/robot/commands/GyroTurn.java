package org.usfirst.frc.team2509.robot.commands;

import org.usfirst.frc.team2509.robot.Robot;
import org.usfirst.frc.team2509.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GyroTurn extends Command {
	private Joystick stick = Robot.oi.getOpStick();
	private RobotDrive drive = RobotMap.DRIVETRAIN;
    private ADXRS450_Gyro gyro = RobotMap.DT_GYRO;
	public GyroTurn() {
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//POV gyro stuff, eg put POV to 0(360) it turns to 0(centers)
    	switch(stick.getPOV()){
    	case 0:
    		if(gyro.getAngle()<360||gyro.getAngle()>=180){
    			drive.mecanumDrive_Cartesian(0, 0, 0.5, gyro.getAngle());
    		}else if(gyro.getAngle()<180||gyro.getAngle()>0){
    			drive.mecanumDrive_Cartesian(0, 0, -0.5, gyro.getAngle());
    		}
    		while((gyro.getAngle()<365&&gyro.getAngle()>355)||(gyro.getAngle()<5&&gyro.getAngle()>-5)){
    			drive.mecanumDrive_Cartesian(0, 0, 0, gyro.getAngle());
    		}
    		break;
    	case 90:
    		if(gyro.getAngle()<90||gyro.getAngle()>270) 
    			drive.mecanumDrive_Cartesian(0, 0, 0.5, gyro.getAngle());
    		else if(gyro.getAngle()<270&&gyro.getAngle()>90) 
    			drive.mecanumDrive_Cartesian(0, 0, -0.5, gyro.getAngle());
    		while(gyro.getAngle()<95&&gyro.getAngle()>85) 
    			drive.mecanumDrive_Cartesian(0, 0, 0, gyro.getAngle());
    		
    	}
    	
    	if(stick.getPOV()==180.0){
    		while(gyro.getAngle()<180&&gyro.getAngle()>0){
    			drive.mecanumDrive_Cartesian(0, 0, 0.5, gyro.getAngle());
    		}
    		if(gyro.getAngle()<180&&gyro.getAngle()>160){
    			drive.mecanumDrive_Cartesian(0, 0, 0, gyro.getAngle());
    		}
    	
    	}else if(stick.getPOV()==270.0){
    		while(gyro.getAngle()<270&&gyro.getAngle()>90);
    			drive.mecanumDrive_Cartesian(0, 0, 0.5, gyro.getAngle());
    		}
			if(gyro.getAngle()<270&&gyro.getAngle()>250){
				drive.mecanumDrive_Cartesian(0, 0, 0.5, gyro.getAngle());
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

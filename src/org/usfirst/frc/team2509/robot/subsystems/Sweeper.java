package org.usfirst.frc.team2509.robot.subsystems;

import org.usfirst.frc.team2509.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.CANTalon;

/**
 *
 */
public class Sweeper extends Subsystem {
	public final CANTalon MOTOR = RobotMap.SWEEP_MOTOR;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public CANTalon getMotor(){
    	return MOTOR;
    }
}


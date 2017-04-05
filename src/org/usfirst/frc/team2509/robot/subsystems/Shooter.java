package org.usfirst.frc.team2509.robot.subsystems;

import org.usfirst.frc.team2509.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.CANTalon;

/**
 *
 */
public class Shooter extends Subsystem {
	public final CANTalon SHOOT = RobotMap.SHOOT_MOTOR;
	public final Talon KICKER = RobotMap.SHOOT_KICKER;
	public final Encoder ENCODER = RobotMap.SHOOT_ENCODER;
	public final Talon GATE = RobotMap.GATE;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public CANTalon getShoot(){
    	return SHOOT;
    }
    public Talon getKicker(){
    	return KICKER;
    }
    public Encoder getEncoder(){
    	return ENCODER;
    }
    public Talon getGate(){
    	return GATE;
    }
    
}


package org.usfirst.frc.team2509.robot;

import org.usfirst.frc.team2509.robot.commands.AutonomousCommand;
import org.usfirst.frc.team2509.robot.commands.Blue1;
import org.usfirst.frc.team2509.robot.commands.Blue2;
import org.usfirst.frc.team2509.robot.commands.Blue3;
import org.usfirst.frc.team2509.robot.commands.BlueBoilerShoot;
import org.usfirst.frc.team2509.robot.commands.BlueCenterShoot;
import org.usfirst.frc.team2509.robot.commands.ClimbUp;
import org.usfirst.frc.team2509.robot.commands.FilterTarget;
import org.usfirst.frc.team2509.robot.commands.OpDrive;
import org.usfirst.frc.team2509.robot.commands.Red1;
import org.usfirst.frc.team2509.robot.commands.Red2;
import org.usfirst.frc.team2509.robot.commands.Red3;
import org.usfirst.frc.team2509.robot.commands.RedBoilerShoot;
import org.usfirst.frc.team2509.robot.commands.RedCenterShoot;
import org.usfirst.frc.team2509.robot.commands.ShooterAim;
import org.usfirst.frc.team2509.robot.commands.ShooterRun;
import org.usfirst.frc.team2509.robot.commands.SweeperForward;
import org.usfirst.frc.team2509.robot.commands.SweeperReverse;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Joystick OPSTICK;
    public Joystick COOPSTICK;
    public JoystickButton CLIMB;
    public JoystickButton DROP_GEAR;
    public JoystickButton FORWARD_SWEEP;
    public JoystickButton REVERSE_SWEEP;
    public JoystickButton SHOOT;
    public JoystickButton SHOOT_AIM;
    public String defaultAuto = "Default";
	public String BLUE1 = "Blue 1";
	public String BLUE2 = "Blue 2";
	public String BLUE3 = "Blue 3";
	public String BLUECENTER = "Blue Center";
	public String BLUESHOOT = "Blue Shoot";
	public String RED1 = "Red 1";
	public String RED2  = "Red 2";
	public String RED3 = "Red 3";
	public String REDCENTER = "Red 2S";
	public String REDSHOOT = "Red 3S";
	public String autoSelected;
	public SendableChooser<String> chooser = new SendableChooser<>();
	private Command autoCommand;

	public OI() {
        OPSTICK = new Joystick(0);
        COOPSTICK = new Joystick(1);
        
        // SmartDashboard Buttons
        SmartDashboard.putData("Climb Up", new ClimbUp());
        SmartDashboard.putData("OpDrive", new OpDrive());
        SmartDashboard.putData("Shooter Start", new ShooterRun());
        SmartDashboard.putData("ReverseSweep", new SweeperReverse());
        SmartDashboard.putData("Forward Sweep", new SweeperForward());
        SmartDashboard.putData("Filter", new FilterTarget());
        //SmartDashboard.putData("Drop Gear", new DropGear());
        SmartDashboard.putData("Aim Shooter", new ShooterAim());
        
        // Joystick Buttons
        CLIMB = new JoystickButton(COOPSTICK, 1);
        CLIMB.whileHeld(new ClimbUp());
        SHOOT = new JoystickButton(OPSTICK, 1);
        SHOOT.whileHeld(new ShooterRun());
        SHOOT_AIM = new JoystickButton(OPSTICK,3);
        SHOOT_AIM.whileHeld(new ShooterAim());
        FORWARD_SWEEP = new JoystickButton(COOPSTICK,3);
        FORWARD_SWEEP.whileHeld(new SweeperForward());
        REVERSE_SWEEP = new JoystickButton(COOPSTICK, 5);
        REVERSE_SWEEP.whileHeld(new SweeperReverse());
        
        //Autonomous Chooser
        chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("Blue 1", BLUE1);
		chooser.addObject("Blue 1-Shoot", BLUECENTER);
		chooser.addObject("Blue 2", BLUE2);
		chooser.addObject("Blue 2-Shoot", BLUESHOOT);
		chooser.addObject("Blue 3", BLUE3);
		chooser.addObject("Red 1", RED1);
		chooser.addObject("Red 2", RED2);
		chooser.addObject("Red 2-Shoot", REDCENTER);
		chooser.addObject("Red 3", RED3);
		chooser.addObject("Red 3-Shoot", REDSHOOT);
		SmartDashboard.putData("Auto choices", chooser);
		
	}
    public Joystick getOpStick() {
        return OPSTICK;
    }
    public Joystick getCoopStick(){
    	return COOPSTICK;
    }
    public double getScaledX(){
		return (OPSTICK.getX()*((OPSTICK.getRawAxis(3)+3)*0.25));
	}
	public double getScaledY(){
		return (OPSTICK.getY()*((OPSTICK.getRawAxis(3)+3)*0.25));
	}
	public double getScaledZ(){
		return (OPSTICK.getZ()*((OPSTICK.getRawAxis(3)+3)*0.25));
	}
	public Command getAutonomous(String autoChoice){
		switch(autoChoice){
    	case "Blue 1":
   		 	autoCommand = new Blue1();
   		 	break;
    	case "Blue 1S":
    		autoCommand = new BlueBoilerShoot();
    		break;
    	case "Blue 2":
    		autoCommand = new Blue2();
   			break;
    	case "Blue 2S":
    		autoCommand = new BlueCenterShoot();
    		break;
    	case "Blue 3":
    		autoCommand = new Blue3();
    		break;
    	case "Red 1":
   		 	autoCommand = new Red1();
   		 	break;
    	case "Red 2":
    		autoCommand = new Red2();
    		break;
    	case "Red 2S":
    		autoCommand = new RedCenterShoot();
    		break;
    	case "Red 3":
    		autoCommand = new Red3();
    		break;
    	case "Red 3S":
    		autoCommand = new RedBoilerShoot();
    		break;
    	case "Default":
		default:
			autoCommand = new AutonomousCommand();
			break;
    	}
		return autoCommand;
	}
}


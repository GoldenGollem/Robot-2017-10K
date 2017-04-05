package org.usfirst.frc.team2509.robot;

import org.usfirst.frc.team2509.robot.commands.Blue1;
import org.usfirst.frc.team2509.robot.commands.Blue2;
import org.usfirst.frc.team2509.robot.commands.Blue3;
import org.usfirst.frc.team2509.robot.commands.GyroTurn;
import org.usfirst.frc.team2509.robot.commands.OpDrive;
import org.usfirst.frc.team2509.robot.commands.Red1;
import org.usfirst.frc.team2509.robot.commands.Red2;
import org.usfirst.frc.team2509.robot.commands.Red3;
import org.usfirst.frc.team2509.robot.commands.SweeperForward;
import org.usfirst.frc.team2509.robot.subsystems.Climb;
import org.usfirst.frc.team2509.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2509.robot.subsystems.Shooter;
import org.usfirst.frc.team2509.robot.subsystems.Sweeper;
import org.usfirst.frc.team2509.robot.subsystems.Vision;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    public Command 
    	autonomousCommand,
    	climbUp,
    	dropGear,
    	filterGearTarget,
    	sweepForward,
    	gyroTurn,
    	opDrive,
    	blue1,
    	blue2,
    	blue3,
    	red1,
    	red2,
    	red3,
    	shoot;
    public Joystick 
    	OpStick,
    	CoopStick;
    public static OI oi;
    public static Climb climb;
    public static DriveTrain driveTrain;
    public static Shooter shooter;
    public static Sweeper sweeper;
    public static Vision vision;
    String autoCommand,
    	BLUE1,BLUE2,BLUE3,BLUECENTER,BLUESHOOT,
    	RED1,RED2,RED3,REDCENTER,REDSHOOT;
    public static boolean isTeleop;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	RobotMap.init();
        climb = new Climb();
        driveTrain = new DriveTrain();
        shooter = new Shooter();
        sweeper = new Sweeper();
        vision = new Vision();
        vision.initDefaultCommand();
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();
        // instantiate the command used for the autonomous perio
        gyroTurn = new GyroTurn();
        opDrive = new OpDrive();
        red1 = new Red1();
        red2 = new Red2();
        red3 = new Red3();
        blue1 = new Blue1();
        blue2 = new Blue2();
        blue3 = new Blue3();
        sweepForward = new SweeperForward();
        new Thread(()->{
			while(true){
				SmartDashboard.putBoolean("Switch", RobotMap.GEAR_SWITCH.get());
				SmartDashboard.putNumber("GYRO ANGLE" , RobotMap.DT_GYRO.getAngle());
				SmartDashboard.putNumber("Encoder", (RobotMap.SHOOT_MOTOR.getSpeed()));
			}
		}).start();
        System.out.println("Robot Ready");
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    public void autonomousInit() {
    	RobotMap.DT_GYRO.reset();
    	SmartDashboard.putBoolean("Switch", RobotMap.GEAR_SWITCH.get());
    	autoCommand = oi.chooser.getSelected();
    	autonomousCommand = oi.getAutonomous(autoCommand);
        // schedule the autonomous command (example)
    	if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        
		
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.isCanceled();
        if(isEnabled()&&isOperatorControl()) opDrive.start();
        isTeleop = true;
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	Scheduler.getInstance().run();
        SmartDashboard.putNumber("GYRO ANGLE" , RobotMap.DT_GYRO.getAngle());
    	SmartDashboard.putBoolean("Switch", RobotMap.GEAR_SWITCH.get());
        
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
        //System.out.println(RobotMap.DT_GYRO.getAngle());
    }
}

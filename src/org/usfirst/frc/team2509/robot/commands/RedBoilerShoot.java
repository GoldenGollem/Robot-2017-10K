package org.usfirst.frc.team2509.robot.commands;

import java.util.ArrayList;
import java.util.Iterator;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team2509.robot.Robot;
import org.usfirst.frc.team2509.robot.RobotMap;

import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RedBoilerShoot extends Command {
	public Command Aim = new ShooterAim();
	private ArrayList<MatOfPoint>
		contours = new ArrayList<MatOfPoint>();
	private final CvSink
		GEARSINK = CameraServer.getInstance().getVideo("GEAR"),
		SHOOTERSINK = CameraServer.getInstance().getVideo("SHOOTER");
	private double
	TARGETSPEED= 3800,
	DISTANCE;
	private final CvSource 
		OUTPUT_STREAM = CameraServer.getInstance().putVideo("ALT-Cam", 640, 480);
	private final Mat
		BINARY = new Mat(),
		CLUSTERS = new Mat(),
		HEIRARCHY = new Mat(),
		HSV = new Mat(),
		SOURCE = new Mat(),
		THRESH = new Mat();
	protected final Scalar 
	//COLOR VALUES
		BLACK = new Scalar(0,0,0),
		BLUE = new Scalar(255, 0, 0),
		GREEN = new Scalar(0, 255, 0),
		RED = new Scalar(0, 0, 255),
		YELLOW = new Scalar(0, 255, 255),
		//Thresholds values
		LOWER_BOUNDS = new Scalar(180,190,40),
		UPPER_BOUNDS = new Scalar(200,210,60);
	private final RobotDrive DT = RobotMap.DRIVETRAIN;
	private final ADXRS450_Gyro GYRO = RobotMap.DT_GYRO;
	private final DigitalInput SWITCH = RobotMap.GEAR_SWITCH;
	private Rect GEARTARGET, SHOOTTARGET;
	private Thread boilerV = new Thread(()->{
		while(true){
			contours.clear();
			RobotMap.SHOOT_CAM.setBrightness(0);
			RobotMap.SHOOT_CAM.setResolution(160,120);
			SHOOTERSINK.grabFrame(SOURCE);
			Imgproc.cvtColor(SOURCE, HSV, Imgproc.COLOR_BGR2RGB);
			Imgproc.threshold(HSV, BINARY, 180, 190, Imgproc.THRESH_BINARY_INV);	
			Imgproc.cvtColor(BINARY, THRESH, Imgproc.COLOR_HSV2BGR);
			Imgproc.cvtColor(THRESH, CLUSTERS, Imgproc.COLOR_BGR2GRAY);
			Mat GRAY = CLUSTERS;
			Imgproc.Canny(GRAY, HEIRARCHY, 2, 4);
			Imgproc.findContours(HEIRARCHY, contours, new Mat(),Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
	        for(MatOfPoint mop :contours){
				Rect rec = Imgproc.boundingRect(mop);
				Imgproc.rectangle(SOURCE, rec.br(), rec.tl(), RED);
			}
			for(Iterator<MatOfPoint> iterator = contours.iterator();iterator.hasNext();){
				MatOfPoint matOfPoint = (MatOfPoint) iterator.next();
				Rect rec = Imgproc.boundingRect(matOfPoint);
				//float aspect = (float)rec.width/(float)rec.height;
				if( rec.height < 8||rec.height>25){
					iterator.remove();
					continue;
				}
				SHOOTTARGET = rec;
				SmartDashboard.putNumber("S_Contours", contours.size());
				SmartDashboard.putNumber("S_X", rec.x);
				SmartDashboard.putNumber("S_Width", rec.width);
				SmartDashboard.putNumber("S_Height", rec.height);
			}			
			OUTPUT_STREAM.putFrame(SOURCE);
		}
	});
	private Thread gearV = new Thread(()->{
    	while(true){
    		contours.clear();
    		RobotMap.GEAR_CAM.setBrightness(0);
    		GEARSINK.grabFrame(SOURCE);
    		//	CVSINK.grabFrameNoTimeout(SOURCE);
    		Imgproc.cvtColor(SOURCE, HSV, Imgproc.COLOR_BGR2RGB);
    		Imgproc.threshold(HSV, BINARY, 180, 190, Imgproc.THRESH_BINARY_INV);	
    		Imgproc.cvtColor(BINARY, THRESH, Imgproc.COLOR_HSV2BGR);
    		Imgproc.cvtColor(THRESH, CLUSTERS, Imgproc.COLOR_BGR2GRAY);
    		Mat GRAY = CLUSTERS;
    			//Core.inRange(THRESH	, LOWER_BOUNDS, UPPER_BOUNDS, CLUSTERS);	
    		Imgproc.Canny(GRAY, HEIRARCHY, 2, 4);
    		Imgproc.findContours(HEIRARCHY, contours, new Mat(),Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
    	    for(MatOfPoint mop :contours){
    			Rect rec = Imgproc.boundingRect(mop);
    			Imgproc.rectangle(SOURCE, rec.br(), rec.tl(), RED);
    		}
    		for(Iterator<MatOfPoint> iterator = contours.iterator();iterator.hasNext();){
    			MatOfPoint matOfPoint = (MatOfPoint) iterator.next();
    			Rect rec = Imgproc.boundingRect(matOfPoint);
    			if( rec.height < 25 || rec.width < 10){
    				iterator.remove();
    				continue;
    			}
    			GEARTARGET = rec;
    			SmartDashboard.putNumber("G_Contours", contours.size());
    			SmartDashboard.putNumber("G_X", rec.x);
    			SmartDashboard.putNumber("G_Width", rec.width);
    		}			
    		if(contours.size()==3){
    			Rect rec = Imgproc.boundingRect(contours.get(0));
    			Point center = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0);
    			Point centerw = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0 - 20);
    			Imgproc.putText(SOURCE, ""+(Point)rec.br(), center, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
    			Imgproc.putText(SOURCE, ""+(Point)rec.tl(), centerw, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
    		}
    		OUTPUT_STREAM.putFrame(SOURCE);
    	}
    });
	public RedBoilerShoot() {
    	requires(Robot.driveTrain);
    }
	// Called just before this Command runs the first time 
	protected void initialize() {
		//Gear Vision Thread
	    gearV.start();
	    
	    //Drive Forward @ 75% for 1.15 Seconds
	    DT.mecanumDrive_Cartesian(0, 0.75, 0, 0);
    	Timer.delay(1.15);
    	DT.drive(0, 0);
    	//Rotate the Robot untill GYRO reads -42 degrees
    	while(GYRO.getAngle()>(-45)) DT.mecanumDrive_Cartesian(0, 0, -0.4, 0);
    	if(GYRO.getAngle()<(-45)) DT.drive(0, 0);
    	//Drive Forward @ 40% for 0.2 Seconds
    	DT.mecanumDrive_Cartesian(0, 0.5, 0, 0);
    	Timer.delay(.25);
    	DT.drive(0, 0);
    	//Pause For 0.9 Seconds
    	Timer.delay(0.9);//This May Be Able To Be Shortened
    	//If Robot see Target Continue
    	while(GEARTARGET == null)Timer.delay(0.02);
    	if(GEARTARGET != null&&(Timer.getMatchTime()>0&&Timer.getMatchTime()<14.9)){
			System.out.println("FOUND TARGET");
			//While Target is less than 55 and in AutoTime
			while(/*SWITCH.get()==false&&*/GEARTARGET.width<37&&(Timer.getMatchTime()>0&&Timer.getMatchTime()<14.5)){
	    		  //SmartDashboard.putBoolean("Switch", RobotMap.GEAR_SWITCH.get());
	    	    	//If Target is Left of Goal move left
	    			if(GEARTARGET.x<52){
	    				System.out.println("TO THE LEFT");
	    				DT.mecanumDrive_Cartesian(0.35, 0, 0, 0);
	    				Timer.delay(0.04);
	    				DT.mecanumDrive_Cartesian(0, 0, 0, 0);
	    			}
	    			//If Target is Right of Goal Move right
	    			else if(GEARTARGET.x>62){
	    				System.out.println("TO THE RIGHT");
	    				DT.mecanumDrive_Cartesian(-0.35,0, 0, 0);
	    				Timer.delay(0.04);
	    				DT.mecanumDrive_Cartesian(0, 0, 0, 0);
	    			}
	    			//If Target is In Goal move Forward
	    			else if(GEARTARGET.x>=52&&GEARTARGET.x<=62){
	    				System.out.println("FORWARD");
	    				DT.mecanumDrive_Cartesian(0, 0.3, 0, 0);	
	    				Timer.delay(0.25);
	    				DT.mecanumDrive_Cartesian(0, 0.0, 0, 0);
	    				Timer.delay(0.25);
	    			}
	    		}
				
	    		DT.mecanumDrive_Cartesian(0, 0.5, 0, 0);
	    		Timer.delay(0.4);
	    		DT.drive(0, 0);
    	/*	while(SWITCH.get()==false&&(Timer.getMatchTime()>0&&Timer.getMatchTime()<14.5)){
    			Timer.delay(0.05);
    			System.out.println("WAITING");
    		}*/

	    		Timer.delay(0.75);
	    		gearV.stop();
	    		Timer.delay(0.75);
    		boilerV.start();//Shooter Vision Thread
    		DT.mecanumDrive_Cartesian(0, -0.75, 0, 0);
    	    Timer.delay(0.75);
    	    DT.mecanumDrive_Cartesian(0, 0, 0, 0);
    	    
    	    System.out.println("SHOOTER STARTING");
    	    while(GYRO.getAngle()<(-37)) DT.mecanumDrive_Cartesian(0, 0, 0.4, 0);
        	if(GYRO.getAngle()>(-37)) DT.drive(0, 0);
    	    RobotMap.SHOOT_MOTOR.set(TARGETSPEED);
    	    Timer.delay(0.5);
//    	    while(SHOOTTARGET == null&&(Timer.getMatchTime()>0&&Timer.getMatchTime()<15))Timer.delay(0.02);
//    	    if(SHOOTTARGET != null&&(Timer.getMatchTime()>0&&Timer.getMatchTime()<15)){
//        		System.out.println("AIMING NOW");
//        		while(SHOOTTARGET.x<=90||SHOOTTARGET.x>=100&&(Timer.getMatchTime()>0&&Timer.getMatchTime()<15)){
//        			if(SHOOTTARGET.x>98&&(Timer.getMatchTime()>0&&Timer.getMatchTime()<15)){
//        				System.out.println("To The Right");
//        				DT.mecanumDrive_Cartesian(0, 0, 0.25, 0);
//        				System.out.println("To The Right");
//        				Timer.delay(0.1);
//        				DT.drive(0, 0);
//        			}else if(SHOOTTARGET.x<92&&(Timer.getMatchTime()>0&&Timer.getMatchTime()<15)){
//        				System.out.println("To The Left");
//        				DT.mecanumDrive_Cartesian(0, 0, -0.25, 0);
//        				System.out.println("To The Left");
//        				Timer.delay(0.1);
//        				DT.drive(0, 0);
//        			}else{
//        				DT.drive(0, 0);
//        				System.out.println("Now Kick");
//        			}
//        		}
        		 if((Timer.getMatchTime()>0&&Timer.getMatchTime()<15)){
         	    	System.out.println(Timer.getMatchTime());
             		RobotMap.GATE.set(0.6);
             		System.out.println("GATE OPENING");
             	    Timer.delay(0.125);
             	    RobotMap.GATE.set(0);
             	    RobotMap.SHOOT_MOTOR.set(TARGETSPEED);
             	    System.out.println("AUGER STARTING");
             	    RobotMap.SHOOT_KICKER.set(1);
         	    }else{
         	    	end();
         	    }
        		 
    	   
    		
		}else{
			System.out.println("NO TARGET");
		}
	}
    	
	protected void execute() {
    	if(Robot.isTeleop) end();
    }
	    

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {

        return !(Timer.getMatchTime()>0&&Timer.getMatchTime()<15);
//        if(Timer.getMatchTime()>0&&Timer.getMatchTime()<15){
//        	return false;
//        }else{
//        	return true;
//        }
    }

	// Called once after isFinished returns true
    protected void end() {
    	DT.drive(0, 0);
    	RobotMap.GATE.set(-0.6);
        Timer.delay(0.125);
        RobotMap.GATE.set(0);
    	RobotMap.SHOOT_KICKER.set(0);
    	RobotMap.SHOOT_MOTOR.changeControlMode(TalonControlMode.PercentVbus);
    	RobotMap.SHOOT_MOTOR.set(0);
    	RobotMap.SHOOT_MOTOR.changeControlMode(TalonControlMode.Speed);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }   
}
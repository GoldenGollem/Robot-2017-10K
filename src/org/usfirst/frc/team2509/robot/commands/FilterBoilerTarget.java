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

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class FilterBoilerTarget extends Command {
	private ArrayList<MatOfPoint>
		contours = new ArrayList<MatOfPoint>();
	private final CvSink
		CVSINK = CameraServer.getInstance().getVideo("SHOOTER");
	private final CvSource 
		OUTPUT_STREAM = CameraServer.getInstance().putVideo("ALT-SHOOTER", 320, 240);
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

    // Called just before this Command runs the first time
    protected void initialize() {
    	new Thread(()->{
    		while(true){
    			contours.clear();
    			RobotMap.SHOOT_CAM.setBrightness(0);
    			CVSINK.grabFrame(SOURCE);
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
    				Robot.vision.BOILER_TARGET = rec;
    				SmartDashboard.putInt("Contours", contours.size());
    				SmartDashboard.putInt("X", rec.x);
    				SmartDashboard.putInt("Width", rec.width);
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
    	}).start();
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

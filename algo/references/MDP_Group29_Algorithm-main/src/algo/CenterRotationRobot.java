package algo;

import java.lang.Math.*;

public class CenterRotationRobot {

    private Vector3 frontLeftWheel, frontRightWheel;

    private Vector3 centerOffset = new Vector3(3, 4, 0);

    private Vector3 backLeftWheel, backRightWheel;

    private Vector3 robotPosOrientation;

    public CenterRotationRobot(double robotCenterX, double robotCenterY, double robotAngle){
        robotPosOrientation = new Vector3(robotCenterX, robotCenterY, robotAngle);
        frontLeftWheel = new Vector3(robotCenterX - centerOffset.x, robotCenterY + centerOffset.y, 0f);
        frontRightWheel = new Vector3(robotCenterX + centerOffset.x, robotCenterY + centerOffset.y, 0f);
        backLeftWheel = new Vector3(robotCenterX - centerOffset.x, robotCenterY - centerOffset.y, 0f);
        backRightWheel = new Vector3(robotCenterX + centerOffset.x, robotCenterY - centerOffset.y, 0f);
    }

    public Vector3 calculateRotationCenter(double leftWheelRotation, double rightWheelRotation, boolean isLeft){
        // TODO: centerRotation needs to take in consideration of rotated robotcar where the centerOfRotation
        // TODO: does not just take the back wheels's y as 0 reference
        Vector3 centerRotation = robotPosOrientation;
        double distFromWheel = 0f;
        if(isLeft){
            distFromWheel =  (calculateDistanceBetweenTwoPoints(frontLeftWheel, backLeftWheel))/Math.tan((frontLeftWheel.rotation * Math.PI/180));
            centerRotation = new Vector3(backLeftWheel.x - distFromWheel, backLeftWheel.y, 0);
        }else{
            distFromWheel =  (calculateDistanceBetweenTwoPoints(frontRightWheel, backRightWheel))/Math.tan((frontRightWheel.rotation * Math.PI/180));
            centerRotation = new Vector3(backRightWheel.x - distFromWheel, backRightWheel.y, 0);
        }

        return centerRotation;
    }

    public Vector3 calculateNewTurningRobotPos(Vector3 centerRotation, boolean isLeft, float turningRadiusRate){

        Vector3 newPos = new Vector3(0,0,0);

        // TODO: RobotPosOrientation is supposedly the center of the robot turning against the center point axis
        // TODO: Haven't take in consideration to initial robot turning at diagonal angles
        if(isLeft){
            newPos.x = Math.sqrt(Math.pow(centerRotation.x - robotPosOrientation.x, 2)) * Math.cos(turningRadiusRate);
            newPos.y = Math.sqrt(Math.pow(centerRotation.y - robotPosOrientation.y, 2)) * Math.sin(turningRadiusRate);
            newPos.rotation -= turningRadiusRate;
        }else{
            newPos.x = Math.sqrt(Math.pow(centerRotation.x - robotPosOrientation.x, 2)) * Math.cos(-turningRadiusRate);
            newPos.y = Math.sqrt(Math.pow(centerRotation.y - robotPosOrientation.y, 2)) * Math.sin(-turningRadiusRate);
            newPos.rotation += turningRadiusRate;
        }

        return newPos;
    }

    private double calculateDistanceBetweenTwoPoints(Vector3 pointOne, Vector3 pointTwo){
        return Math.sqrt(Math.pow(pointOne.x - pointTwo.x, 2) + Math.pow(pointOne.y - pointTwo.y, 2));
    }

    public class Vector3{
        public double x, y, rotation;

        public Vector3(double x, double y, double rotation){
            this.x = x;
            this.y = y;
            this.rotation = rotation;
        }
    }
}



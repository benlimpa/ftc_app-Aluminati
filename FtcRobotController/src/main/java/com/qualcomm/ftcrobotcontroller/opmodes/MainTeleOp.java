package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by BenL on 10/7/15.
 */
public class MainTeleOp extends OpMode
{

    DcMotorController.DeviceMode    devMode;

    DcMotorController               motorControl1;
    DcMotorController               motorControl2;
    DcMotorController               motorControl3;
    DcMotorController               motorControl4;

    //ServoController                 servoControl;
    DcMotor     leftDrive;
    DcMotor     rightDrive;

    DcMotor     bottomSpool;
    DcMotor     topSpool;

    DcMotor     leftRangle;
    DcMotor     rightRangle;

    boolean     manualMode;
    //int     loops;

    @Override
    public void init()
    {
        manualMode = false;
        //leftDrive   = hardwareMap.dcMotor.get("leftDrive");
        //rightDrive  = hardwareMap.dcMotor.get("rightDrive");

        topSpool = hardwareMap.dcMotor.get("topSpool");
        bottomSpool = hardwareMap.dcMotor.get("bottomSpool");

        leftRangle = hardwareMap.dcMotor.get("leftRangle");
        rightRangle = hardwareMap.dcMotor.get("rightRangle");
        rightRangle.setDirection(DcMotor.Direction.REVERSE);

        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");
        rightDrive.setDirection(DcMotor.Direction.REVERSE);


        motorControl1 = hardwareMap.dcMotorController.get("MotorControl1");
        motorControl2 = hardwareMap.dcMotorController.get("MotorControl2");
        motorControl3 = hardwareMap.dcMotorController.get("MotorControl3");
        motorControl4 = hardwareMap.dcMotorController.get("MotorControl4");

        //leftDrive.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        //rightDrive.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        //rightDrive.setDirection(DcMotor.Direction.REVERSE);

        motorControl1.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        motorControl2.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        motorControl3.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        motorControl4.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
    }

    @Override
    public void loop()
    {
        //if (devMode == DcMotorController.DeviceMode.WRITE_ONLY)
        //{
            //double[] drivePower = getDrivePower(-scaleInput(gamepad1.left_stick_x), -scaleInput(gamepad1.left_stick_y));

            //telemetry.addData("Left Drive: ", drivePower[0]);
            //telemetry.addData("Right Drive: ", drivePower[1]);
        if (!manualMode) {
            if (gamepad2.a) {
                rightRangle.setPower(-0.4);
                leftRangle.setPower(-0.4);
            } else if (gamepad2.b) {
                rightRangle.setPower(0.4);
                leftRangle.setPower(0.4);
            } else {
                rightRangle.setPower(0);
                leftRangle.setPower(0);
            }
            topSpool.setPower(scaleInput(gamepad2.right_stick_y));
            bottomSpool.setPower(scaleInput(gamepad2.left_stick_y)*2/5);
        } else {
            rightRangle.setPower(scaleInput(gamepad2.left_stick_y));
            leftRangle.setPower(scaleInput(gamepad2.right_stick_y));
        }


        double[] drivePower = getDrivePower(-scaleInput(gamepad1.left_stick_x), -scaleInput(gamepad1.left_stick_y));
        leftDrive.setPower(drivePower[0]);
        rightDrive.setPower(drivePower[1]);


        if (gamepad2.dpad_down) {
            manualMode = true;
        } else if (gamepad2.dpad_up) {
            manualMode = false;
        }
        telemetry.addData("Mode: ", manualMode);
/*
            if (gamepad1.x) {
                leftRangle.setPower(-0.4);
            } else if (gamepad1.y) {
                leftRangle.setPower(0.4);
            } else {
                leftRangle.setPower(0);
            }
            */

        //}

        /*if (devMode == DcMotorController.DeviceMode.READ_ONLY)
        {
            telemetry.addData("Left Drive Power: ", leftDrive.getPower());
            telemetry.addData("Left Drive Power: ", leftDrive.getPower());
        }
        */
/*
        if ((loops % 17) == 0)
        {
            motorControl.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);

        }
        */
        //devMode = motorControl.getMotorControllerDeviceMode();
        //loops++;
    }

    @Override
    public void stop()
    {
        // nothing to stop
    }

    private double[] getDrivePower(double x, double y) // Takes x and y value of joystick, returns drivePower[leftDrive, rightDrive]
    {
        double[] drivePower = new double[2];

        /*
         *  The Following algorithm is Kendra's Joystick to Tank Drive Conversion (http://home.kendra.com/mauser/Joystick.html)
         */

        double rightPlusLeft     = (100 - Math.abs(x * 100)) * y + (y * 100);
        double rightMinusLeft    = (100 - Math.abs(y * 100)) * x + (x * 100);

        drivePower[0] = (rightPlusLeft-rightMinusLeft)/200; // left drive power
        drivePower[1] = (rightPlusLeft+rightMinusLeft)/200; // right drive power

        // Make sure that the power is not out of bounds
        for (int index = 0; index <= 1; index++)
        {
            if (Math.abs(drivePower[index]) > 1.0)
            {
                drivePower[index] = 1f;
            }
        }

        return drivePower;
    }

    // Input scale from NXTTeleOp for more accuracy in lower ranges
    double scaleInput(double dVal)
    {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}

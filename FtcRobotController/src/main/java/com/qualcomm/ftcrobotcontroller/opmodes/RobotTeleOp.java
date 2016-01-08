package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by BenL on 1/6/16.
 */
public class RobotTeleOp extends RobotTelemetry
{
    private boolean manualRangle;
    private boolean manualPimp;
    private boolean pimpWheelAdjust;
    private boolean boxSide;

    public RobotTeleOp ()
    {
        super(false); // Do not use drive encoders
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

    @Override
    public void init()
    {
        manualRangle = false;
        boxSide = RIGHT;
        manualPimp = false;
        pimpWheelAdjust = false;
    }
    @Override
    public void loop()
    {
        // Change Modes:
        if (gamepad2.dpad_down)
            manualRangle = true;
        else if (gamepad2.dpad_up)
            manualRangle = false;

        if (gamepad2.dpad_right)
            boxSide = RIGHT;
        else if (gamepad2.dpad_left)
            boxSide = LEFT;

        if (gamepad1.x)
            manualPimp = false;
        else if (gamepad1.y)
            manualPimp = true;

        if (gamepad1.dpad_right)
            pimpWheelAdjust = true;
        else if (gamepad1.dpad_left)
            pimpWheelAdjust = false;

        if (manualRangle)
        {
            setMotorPower("rightRangle", -scaleInput(gamepad1.right_stick_y));
            setMotorPower("leftRangle", -scaleInput(gamepad1.left_stick_y));
        }
        else
        {

        }

        // Telemetry
        addTelemetry(7, "Manual Rangle Control: " + manualRangle);

        if (boxSide == RIGHT)
            addTelemetry(8, "Servo Box Side: RIGHT");
        else
            addTelemetry(8, "Servo Box Side: LEFT");

        updateTelemetry();
    }
}

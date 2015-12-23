package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.LightSensor;

/**
 * Created by BenL on 12/22/15.
 */
public class SensorTest extends OpMode
{

    ColorSensor sensor;

    @Override
    public void init()
    {
        sensor = hardwareMap.colorSensor.get("colorSensor");
        sensor.enableLed(true);
    }

    @Override
    public void loop()
    {
        telemetry.addData("Color Sensor: ", sensor.toString());
        telemetry.addData("Red: ", sensor.red());
        telemetry.addData("Green: ", sensor.green());
        telemetry.addData("Blue:  ", sensor.blue());
        telemetry.addData("Alpha: ", sensor.alpha());
    }

    @Override
    public void stop()
    {
        // nothing to stop
    }
}

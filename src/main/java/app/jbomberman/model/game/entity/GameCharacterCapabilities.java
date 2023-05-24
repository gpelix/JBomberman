package app.jbomberman.model.game.entity;

import app.jbomberman.util.RandomNumberGenerator;

public class GameCharacterCapabilities
{
    private boolean goesThroughWallsFlag;

    private double speed;

    public GameCharacterCapabilities(boolean goesThroughWalls, double speed)
    {
        this.speed = speed;
        this.goesThroughWallsFlag = goesThroughWalls;
    }

    public double getSpeed()
    {
        return speed;
    }

    public double getSpeedInstance()
    {
        return speed + RandomNumberGenerator.getInstance().nextDouble() * speed / 4;

    }

    public boolean goesThroughWalls()
    {
        return goesThroughWallsFlag;
    }

    @Override
    public String toString()
    {
        return "GameCharacterCapabilities(speed=%f, goesThroughWalls=%b)"
            .formatted(speed, goesThroughWallsFlag);
    }


    // Override if needed. Needed by the moveManager
    public boolean takesPowerUps()
    {
        return false;
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
    }

    public void setGoesThroughWalls(boolean goesThroughWallsFlag)
    {
        this.goesThroughWallsFlag = goesThroughWallsFlag;
    }
}


package app.jbomberman.model.game.entity.gamecharacter;

public class BomberManCapabilities extends GameCharacterCapabilities
{
    private int bombPower;
    private int availableBombs;
    private boolean remoteControl;
    private boolean goesThroughBombs;
    private boolean goesThroughFire;
    private boolean isInvincible;
    private int score;
    private int numberOfLives;

    public static final BomberManCapabilities defaultCapabilities =
        new BomberManCapabilities(
            1.5,
            2,
            1,
            false,
            false,
            false,
            false,
            false,
            0,
            3
        );

    public BomberManCapabilities(double speed,
                                                             int bombPower, int availableBombs,
                                                             boolean remoteControl,
                                                             boolean goesThroughWalls,
                                                             boolean goesThroughBombs,
                                                             boolean goesThroughFire,
                                                             boolean isInvincible,
                                                             int score,
                                                             int numberOfLives)
    {
        super(goesThroughWalls, speed);

        this.bombPower = bombPower;
        this.availableBombs = availableBombs;
        this.remoteControl = remoteControl;
        this.goesThroughBombs = goesThroughBombs;
        this.goesThroughFire = goesThroughFire;
        this.isInvincible = isInvincible;
        this.score = score;
        this.numberOfLives = numberOfLives;
    }

    public int getBombPower()
    {
        return bombPower;
    }

    public int getAvailableBombs()
    {
        return availableBombs;
    }

    public boolean hasRemoveControl()
    {
        return remoteControl;
    }

    public boolean goesThroughFire()
    {
        return goesThroughFire;
    }

    public boolean goesThroughBombs()
    {
        return goesThroughBombs;
    }

    public boolean isInvincible()
    {
        return isInvincible;
    }

    public void unsetIsInvincible()
    {
        isInvincible = false;
    }


    public void addDelta(BomberManCapabilities capabilities)
    {
        setSpeed(getSpeed() + capabilities.getSpeed());
        bombPower += capabilities.getBombPower();
        availableBombs += capabilities.availableBombs;
        remoteControl |= capabilities.remoteControl;
        setGoesThroughWalls(goesThroughWalls() || capabilities.goesThroughWalls());
        goesThroughBombs |= capabilities.goesThroughBombs;
        goesThroughFire |= capabilities.goesThroughFire;
        isInvincible |= capabilities.isInvincible;
        score += capabilities.score;
        numberOfLives += capabilities.numberOfLives;
    }

    @Override
    public boolean takesPowerUps()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return ("BomberManCapabilities(%s, bombPower=%d, availableBombs=%d, " +
            "remoteControl=%b, goesThroughBombs=%b, goesThroughFire=%b, " +
            "isInvincible=%b)")
            .formatted(super.toString(), bombPower, availableBombs, remoteControl,
                goesThroughBombs, goesThroughFire, isInvincible);
    }

    public int getNumberOfLives()
    {
        return numberOfLives;
    }


    public int getPointsEarned()
    {
        return score;
    }

    public void removeLife()
    {
        numberOfLives--;
    }

    public void addLife()
    {
        numberOfLives++;
    }

    public void addPoints(int points)
    {
        score += points;
    }
}

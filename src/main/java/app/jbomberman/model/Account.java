package app.jbomberman.model;

import app.jbomberman.model.game.entity.BomberManCapabilities;

public class Account
{
    private final String playerName;
    private int level;
    private BomberManCapabilities capabilities;

    public Account(String name, int level,
                   BomberManCapabilities hisCapabilities)
    {
        this.playerName = name;
        this.level = level;
        this.capabilities = hisCapabilities;
    }

    public BomberManCapabilities getCapabilities()
    {
        return capabilities;
    }

    public int getLevel()
    {
        return level;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setCapabilities(BomberManCapabilities capabilities)
    {
        this.capabilities = capabilities;
    }

    public void incrementLevel()
    {
        level++;
    }

    public void setLevel(int i)
    {
        level = i;
    }
}

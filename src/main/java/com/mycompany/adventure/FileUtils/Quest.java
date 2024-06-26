package com.mycompany.adventure.FileUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

/**
 *
 * @author bogna
 */
public class Quest 
{
    private String questName;
    private String NPC;
    private String description;
    private String location;
    private ArrayList<String> reward;
    private String questType;
    private String startText;
    private String endText;
    public Sprite card;
    private String coordX;
    private String coordY;
    private ArrayList<String> find;
    public int priority;
    public BitmapFont cardDrawer = new BitmapFont(Gdx.files.internal("Fonts/Retron2.fnt"), Gdx.files.internal("Fonts/Retron2.png"), false);


    public Quest() {}

    public ArrayList<String> getFind() 
    {
        return find;
    }

    public void setFind(ArrayList<String> find)
    {
        this.find = find;
    }

    public String getCoordX()
    {
        return coordX;
    }

    public void setCoordX(String coordX)
    {
        this.coordX = coordX;
    }

    public String getCoordY()
    {
        return coordY;
    }

    public void setCoordY(String coordY) 
    {
        this.coordY = coordY;
    }

    public String getEndText()
    {
        return endText;
    }

    public void setEndText(String endText) 
    {
        this.endText = endText;
    }

    public String getStartText()
    {
        return startText;
    }

    public void setStartText(String startText) 
    {
        this.startText = startText;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public int getPriority() 
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public BitmapFont getCardDrawer() 
    {
        return cardDrawer;
    }

    public void setCardDrawer(BitmapFont cardDrawer) 
    {
        this.cardDrawer = cardDrawer;
    }

    public Sprite getCard() 
    {
        return card;
    }

    public void renderCard(SpriteBatch batch)
    {
        card.draw(batch);

        GlyphLayout layout = new GlyphLayout();
        layout.setText(cardDrawer, questName);
        cardDrawer.setColor(Color.GOLD);
        cardDrawer.draw(batch, questName, (card.getX() +
                card.getWidth() / 2) - layout.width / 2, card.getY() + 42);
    }

    public void renderText(SpriteBatch batch) 
    {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(cardDrawer, questName);
        cardDrawer.setColor(Color.GOLD);
        cardDrawer.draw(batch, questName, (card.getX() +
                card.getWidth() / 2) - layout.width / 2, card.getY() + 42);
    }

    public void setCard(Sprite card) 
    {
        this.card = card;
    }

    public String getQuestType() 
    {
        return questType;
    }

    public void setQuestType(String questType)
    {
        this.questType = questType;
    }

    public String getQuestName() 
    {
        return questName;
    }

    public void setQuestName(String questName) 
    {
        this.questName = questName;
    }

    public String getNPC() 
    {
        return NPC;
    }

    public void setNPC(String NPC)
    {
        this.NPC = NPC;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public ArrayList<String> getReward()
    {
        return reward;
    }

    public void setReward(ArrayList<String> reward)
    {
        this.reward = reward;
    }


}

    

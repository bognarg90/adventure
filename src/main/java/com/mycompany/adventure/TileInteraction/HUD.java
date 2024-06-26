package com.mycompany.adventure.TileInteraction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mycompany.adventure.Effects.ScreenText;
import com.mycompany.adventure.Entities.Good.NPC;
import com.mycompany.adventure.Entities.Player;
import com.mycompany.adventure.Levels.World;
import com.mycompany.adventure.TileInteraction.Objects.Signpost;

//Head-up display létrehozása a játékos tevékenységének/állapotának követéséhez
public class HUD 
{
    public NPC drawNewQuest = null;
    public Texture heart, threeQheart, halfHeart, QuarterHeart, empty, coin;
    private Sprite pause, newQuest;
    
    private ScreenText coins = new ScreenText();
    
    public SpriteBatch hud;
    private float elapsed = 0f;
    
    private Player player;
    
//HUD elemek fájlainak beolvasása, méretezése és elhelyezése
    public HUD(Player player)
    {
        this.player = player;
        
        hud = new SpriteBatch();
        heart = new Texture(Gdx.files.internal("UI/HUD/Hearts/heart.png"));
        threeQheart = new Texture(Gdx.files.internal("UI/HUD/Hearts/3-4-heart.png"));
        halfHeart = new Texture(Gdx.files.internal("UI/HUD/Hearts/half-heart.png"));
        QuarterHeart = new Texture(Gdx.files.internal("UI/HUD/Hearts/quarter-heart.png"));
        empty = new Texture(Gdx.files.internal("UI/HUD/Hearts/background.png"));
        coin = new Texture(Gdx.files.internal("UI/HUD/Coins/coin.png"));

        pause = new Sprite(new Texture(Gdx.files.internal("UI/pause.png")));
        newQuest = new Sprite(new Texture(Gdx.files.internal("UI/HUD/NPC/newQuest.png")));

        pause.setPosition(950, 770);
        newQuest.setPosition(1000, 700);

        pause.setSize(25, 25);

        coins.setPosition(new Vector2(25, 753));
        coins.setColor(Color.YELLOW);
        coins.setSize(1f);
    }
    
//Jelző megjelenítése
    public void drawSign(Signpost activeSign) 
    {
        hud.begin();
        activeSign.drawText(hud);
        hud.end();
    }
    
//NPC pábeszédek megjelenítése
    public void drawNPCDialog(NPC npc, String text) 
    {
        hud.begin();
        npc.drawText(text, hud);
        hud.end();
    }

    public void resetTimer() 
    {
        elapsed = 0f;
    }

//Életerősáv megjelenítése, változása
    public void render() 
    {

        hud.begin();
        if(!World.detector.player.inventory_.renderOverlay) 
        {
            for(int x = 0; x < player.hearts; x++) 
            {
                if(player.health == player.hearts) 
                {
                    hud.draw(heart, 20 * x + 5, 770);
                } 
                else 
                {
                    if(x > player.health - 1) 
                    {
                        hud.draw(empty, 20 * x + 5, 770);
                    } 
                    else 
                    {
                        hud.draw(heart, 20 * x + 5, 770);
                    }
                }
            }
            hud.draw(coin, 5, 740);

            coins.setText(player.coins + "");
            coins.renderOnlyIf(hud);
        }

        pause.draw(hud);

//Küldetés megjelenítése
        if(drawNewQuest != null) 
        {
            newQuest.draw(hud);
            if(newQuest.getX() > 785) 
            {
                newQuest.setX(newQuest.getX() - 3f);
            } 
            else 
            {
                drawNewQuest.addQuestToPlayer();
                elapsed += Gdx.graphics.getDeltaTime();
                if(elapsed > 4) 
                {
                    elapsed = 0f;
                    newQuest.setPosition(1000, 700);
                    drawNewQuest = null;
                }
            }
        }

        hud.end();
    }

//Játék megállítása
    public boolean pausePressed() 
    {

        if(Gdx.input.isTouched()) 
        {
            if (pause.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) {
                World.levelMusic.pause();
                return true;
            }
        }
        return false;
    }
}

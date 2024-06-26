package com.mycompany.adventure.Entities.Good;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mycompany.adventure.Classifier;
import com.mycompany.adventure.Entities.MovementScript;

public class Adventurer extends NPC
{
    Texture walkSheet;
    Texture idleSheet;
    Texture dieSheet;
    
    public Adventurer(String name, int x, int y, MovementScript script)
    {
        super(name, Classifier.Kalandor, x, y, 0.3f, script);

        walkSheet = new Texture(Gdx.files.internal("Entities/NPCAdventurer/adventurer_walk.png"));
        idleSheet = new Texture(Gdx.files.internal("Entities/NPCAdventurer/adventurer_idle.png"));
        //dieSheet = new Texture(Gdx.files.internal("Entities/NPCBlacksmith/blacksmith_die.png"));
        
        constructAnimation();
    }
    
    private void constructAnimation() 
    {
        TextureRegion[][] walkTMP = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / 1,
                walkSheet.getHeight() / 1);

        TextureRegion[] walkFrames = new TextureRegion[1 * 1];
        int index = 0;
        for (int i = 0; i < 1; i++) 
        {
            for (int j = 0; j < 1; j++) 
            {
                walkFrames[index++] = walkTMP[i][j];
            }
        }

        super.walkAnim = new Animation<TextureRegion>(0.1f, walkFrames);

        TextureRegion[][] idleTMP = TextureRegion.split(idleSheet,
                idleSheet.getWidth() / 1,
                idleSheet.getHeight() / 1);

        TextureRegion[] idleFrames = new TextureRegion[1 * 1];
        int index3 = 0;
        for (int i = 0; i < 1; i++) 
        {
            for (int j = 0; j < 1; j++) 
            {
                idleFrames[index3++] = idleTMP[i][j];
            }
        }

        super.idleAnim = new Animation<TextureRegion>(0.1f, idleFrames);

        /*TextureRegion[][] dieTMP = TextureRegion.split(dieSheet,
                dieSheet.getWidth() / 15,
                dieSheet.getHeight() / 1);

        TextureRegion[] dieFrames = new TextureRegion[15 * 1];
        int index4 = 0;
        for (int i = 0; i < 1; i++) 
        {
            for (int j = 0; j < 15; j++) 
            {
                dieFrames[index4++] = dieTMP[i][j];
            }
        }

        super.dieAnim = new Animation<TextureRegion>(0.1f, dieFrames);*/

    }
}

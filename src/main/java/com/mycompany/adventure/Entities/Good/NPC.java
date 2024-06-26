package com.mycompany.adventure.Entities.Good;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mycompany.adventure.Classifier;
import com.mycompany.adventure.Effects.ScreenText;
import com.mycompany.adventure.Entities.MovementScript;
import com.mycompany.adventure.FileUtils.DialogueManager;
import com.mycompany.adventure.FileUtils.QuestManager;
import com.mycompany.adventure.Levels.World;
import com.mycompany.adventure.TileInteraction.Objects.Item;
import java.util.ArrayList;

public class NPC 
{
    private MovementScript script;
    private String name;
    private Vector2 position;
    private String horizDirection;
    private ArrayList<String> sequence;
    private int currentSequenceItem;
    private String type;

    public Animation<TextureRegion> walkAnim;
    public Animation<TextureRegion> idleAnim;
    public Animation<TextureRegion> dieAnim;

    public int animState = 0;
    public boolean stop = false;
    public float stateTime = 0f;
    public float elapsedTime = 0f;
    public TextureRegion currentFrame;
    private float speed;

    private FileHandle file;
    public QuestManager quest;
    public boolean questFinished = false;
    public DialogueManager dialog;
    private Sprite dialogBox = new Sprite(new Texture(Gdx.files.internal("UI/HUD/NPC/dialogueBox.png")));
    public String questType;
    private ScreenText textDrawer;
    private BitmapFont nameDrawer = new BitmapFont(Gdx.files.internal("Fonts/turok2.fnt"), Gdx.files.internal("Fonts/turok2.png"), false);
    public ArrayList<String> reward = new ArrayList<String>();

    public NPC(String name, String type, int x, int y, float speed, MovementScript script) {
        this.script = script;
        this.name = name;
        this.type = type;
        this.speed = speed;
        this.position = new Vector2(x, y);
        this.horizDirection = "right";
        this.sequence = script.getSequence();
        currentSequenceItem = 0;

        textDrawer = new ScreenText("Fonts/Retron2.fnt", "Fonts/Retron2.png");
        textDrawer.setColor(Color.TAN);

        quest = null;
        file = null;
        questType = null;

        dialogBox.setCenter(500, 110);

        if (Classifier.marketplaceNPCs.contains(type))
        {
            dialog = new DialogueManager(this, Gdx.files.internal("Quests/Conversation/marketplace.json"));
        }
        else 
        {
            dialog = new DialogueManager(this, Gdx.files.internal("Quests/Conversation/marketplace.json"));
        }
    }

    public void drawText(String text, SpriteBatch batch)
    {
        stop = true;
        animState = 0;
        dialogBox.draw(batch);
        textDrawer.setText(text);

        GlyphLayout layout = new GlyphLayout();
        layout.setText(textDrawer.drawer, text);
        textDrawer.setPosition(new Vector2((float) (Gdx.graphics.getWidth() / 2) - layout.width / 2, 130));
        
        
        layout.setText(nameDrawer, name + " a " + type);
        nameDrawer.setColor(Color.BLACK);
        nameDrawer.getData().setScale(0.9f);
        nameDrawer.draw(batch, name + " a " + type, (float) (Gdx.graphics.getWidth() / 2) - layout.width / 2, 170);

        textDrawer.renderOnlyIf(batch);
    }

    public void addQuestToPlayer() 
    {
        if(!World.quests.contains(quest.questData)) 
        {
            World.addQuest(quest.questData);
        }
    }

    public NPC setQuest(FileHandle file)
    {
        this.file = file;
        quest = new QuestManager(this, file);
        questType = quest.questData.getQuestType();
        reward.addAll(quest.questData.getReward());

        if(reward.get(0).equals(Classifier.Weapon)) 
        {}
        return this;
    }

    public String getQuestText() 
    {
        return quest.questData.getStartText();
    }

    public void render(SpriteBatch batch) 
    {
        stateTime += Gdx.graphics.getDeltaTime();

        switch(animState) 
        {
            case 0:
                currentFrame = idleAnim.getKeyFrame(stateTime, true);
                break;
            case 1:
                currentFrame = walkAnim.getKeyFrame(stateTime, true);
                break;
            case 2:
                currentFrame = dieAnim.getKeyFrame(stateTime, true);
                break;
        }

        boolean flip = (horizDirection.equals("left"));
        batch.draw(currentFrame, flip ? position.x + currentFrame.getRegionWidth() : position.x, position.y, flip ? -currentFrame.getRegionWidth() : currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

        if(!stop) move();
    }

    public Rectangle getRect() 
    {
        return new Rectangle(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    private void move()
    {
        elapsedTime += Gdx.graphics.getDeltaTime();

        String current = sequence.get(currentSequenceItem);

        if(current.equals("Up") ||
                current.equals("Down") ||
                current.equals("Left") ||
                current.equals("Right")) {
            if(elapsedTime <= script.getIntervalTime()) 
            {
                animState = 1;
                if(current.equals("Up")) 
                {
                    if(!World.detector.NPCcollisionAt(this, Math.round(position.x), Math.round(position.y + 5)).equals("obstacle")) 
                    {
                        position.x += speed;
                    }
                }
                else if(current.equals("Down")) 
                {
                    if(!World.detector.NPCcollisionAt(this, Math.round(position.x), Math.round(position.y - 5)).equals("obstacle"))
                    {
                        position.y -= speed;
                    }
                } 
                else if(current.equals("Left")) 
                {
                    if(!World.detector.NPCcollisionAt(this, Math.round(position.x - 5), Math.round(position.y)).equals("obstacle")) 
                    {
                        horizDirection = "left";
                        position.x -= speed;
                    }
                }
                else if(current.equals("Right")) 
                {
                    if(!World.detector.NPCcollisionAt(this, Math.round(position.x + 5), Math.round(position.y)).equals("obstacle"))
                    {
                        horizDirection = "right";
                        position.x += speed;
                    }
                }
            } 
            else 
            {
                elapsedTime = 0f;
                if(currentSequenceItem + 1 == sequence.size()) 
                {
                    currentSequenceItem = 0;
                } 
                else
                {
                    currentSequenceItem += 1;
                }
            }
        }
        else if(current.equals("Stop")) 
        {
            if (elapsedTime <= script.getStopTime()) 
            {
                animState = 0;
            } 
            else
            {
                elapsedTime = 0f;
                if (currentSequenceItem + 1 == sequence.size()) 
                {
                    currentSequenceItem = 0;
                } else {
                    currentSequenceItem += 1;
                }
            }
        }
    }

    public void giveQuestReward() 
    {
        if(quest.questData.getReward().size() > 1) 
        {
            if(quest.questData.getReward().size() > 4) 
            {
                Item temp_ = new Item(quest.questData.getReward().get(0), "itemSprites/" + quest.questData.getReward().get(1), Classifier.Weapon, Integer.parseInt(quest.questData.getReward().get(2)),
                        quest.questData.getReward().get(3), Integer.parseInt(quest.questData.getReward().get(4)));
                temp_.sprite.setSize(16, 16);
                World.detector.player.inventory_.addItem(temp_);
            }
            else 
            {
                Item temp_ = new Item(quest.questData.getReward().get(0), "itemSprites/" + quest.questData.getReward().get(1), Classifier.Food, 1,
                        quest.questData.getReward().get(2));
                temp_.sprite.setSize(16, 16);
                World.itemToAdd.add(temp_);
            }
        } 
        else
        {
            World.detector.player.coins += Integer.parseInt(quest.questData.getReward().get(0));
        }

    }
}

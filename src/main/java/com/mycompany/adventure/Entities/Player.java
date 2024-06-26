package com.mycompany.adventure.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mycompany.adventure.Classifier;
import com.mycompany.adventure.Effects.Sound;
import com.mycompany.adventure.Levels.World;
import com.mycompany.adventure.TileInteraction.Inventory;
import com.mycompany.adventure.TileInteraction.Objects.Item;

public class Player 
{
    public TextureRegion currentFrame;

    private float speed = 0.7f;
    private boolean runTemp = false;

    public Vector2 position;
    public Inventory inventory_;
    public int WIDTH;
    public int HEIGHT;

    private boolean flip;
    public boolean invincible = false;

    public Item swordClone;
    public Item spearClone;

    private Sprite slash_left, slash_right;
    public boolean renderSlash = false;
    private float slashTime = 0;
    private float stateTime = 0f;
    public int cooldown = 0;

    private Sound effect_slash;
    public Sound effect_eat;

    public int hearts = 10;
    public int health = 10;

    public int coins = 0;

    private Rectangle box;
    public String horiDirection = "right";
    public String vertDirection = "up";

    private Texture walkSheet;
    private Animation<TextureRegion> walkAnim, idleAnim;
    private Texture idleSheet;
    private int animState = 0;
    
    //A játékos létrehozása
    public Player(int x, int y) 
    {

        position = new Vector2();
        position.x = x;
        position.y = y;

        inventory_ = new Inventory(this);
        inventory_.addItem(new Item("Alma", "itemSprites/tile002.png", Classifier.Food, 1, "Visszaállít 2 életpontot"), 10);
        inventory_.addItem(new Item("Szeder", "itemSprites/tile000.png", Classifier.Food, 1, "Visszaállít 2 életpontot"), 20);

        slash_left = new Sprite(new Texture(Gdx.files.internal("Character/slash_left2.png")));
        slash_right = new Sprite(new Texture(Gdx.files.internal("Character/slash_right2.png")));
        effect_slash = new Sound("sword_slash2", false);
        effect_eat = new Sound("eat_food", false);

        HEIGHT = 22;
        WIDTH = 15;

        box = new Rectangle();
        box.x = position.x;
        box.y = position.y;
        box.height = HEIGHT;
        box.width = WIDTH;

        walkSheet = new Texture(Gdx.files.internal("Character/knight_run_3.png"));
        idleSheet = new Texture(Gdx.files.internal("Character/knight_idle_3.png"));

        TextureRegion[][] walkTMP = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / 1,
                walkSheet.getHeight() / 4);

        TextureRegion[] walkFrames = new TextureRegion[1 * 4];
        int index = 0;
        for (int i = 0; i < 4; i++) 
        {
            for (int j = 0; j < 1; j++) 
            {
                walkFrames[index++] = walkTMP[i][j];
            }
        }
        walkAnim = new Animation<TextureRegion>(0.1f, walkFrames);

        TextureRegion[][] idleTMP = TextureRegion.split(idleSheet,
                idleSheet.getWidth() / 1,
                idleSheet.getHeight() / 3);

        TextureRegion[] idleFrames = new TextureRegion[1 * 3];
        int index2 = 0;
        for (int i = 0; i < 3; i++) 
        {
            for (int j = 0; j < 1; j++) 
            {
                idleFrames[index2++] = idleTMP[i][j];
            }
        }
        idleAnim = new Animation<TextureRegion>(0.4f, idleFrames);
    }

    public void setStop()
    {
        animState = 0;
    }

    
    public void update(boolean disableMovement) 
    {

        runTemp = false;
        if(!disableMovement)
        {
            keyboardMove();
            controllerMove();
        }
        processCollision();
        processWeaponry();

        box.x = position.x;
        box.y = position.y;
    }

    private void processCollision() 
    {
        if(World.detector.hasCollided().equals("room_entrance")) 
        {
            Gdx.app.log("Player", "Room Entrance");
        }

    }
    //Fegyver létrehozása
    private void processWeaponry() 
    {
        if(inventory_.inventory.size() != 0)
        {
            if (inventory_.slotSelected - 1 < inventory_.inventory.size()) 
            {
                Item item = inventory_.inventory.get(inventory_.slotSelected - 1).stackedItem;
                if(item.name.toLowerCase().contains("kard") || item.name.toLowerCase().contains("balta") || item.name.toLowerCase().contains("szigony") || item.name.toLowerCase().contains("penge") ||
                           item.name.toLowerCase().contains("bot") || item.name.toLowerCase().contains("rapier") || item.name.toLowerCase().contains("szurony") || item.name.toLowerCase().contains("pajzs")) 
                {
                    swordClone = new Item(item.name, item.spritePath, item.type, item.durability, item.description);
                    swordClone.sprite.setSize(16, 16);
                    spearClone = null;
                } 
                else if(item.name.toLowerCase().contains("szurony") || item.name.toLowerCase().contains("szigony") || item.name.toLowerCase().contains("bot")) 
                {
                    spearClone = new Item(item.name, item.spritePath, item.type, item.durability, item.description);
                    spearClone.sprite.setSize(16, 16);
                    swordClone = null;
                } 
                else 
                {
                    swordClone = null;
                    spearClone = null;
                }
            }
            else
            {
                swordClone = null;
                spearClone = null;
            }
        }
        else 
        {
            swordClone = null;
            spearClone = null;
        }
        if(swordClone != null)
        {
            if(horiDirection.equals("left"))
            {
                swordClone.sprite.setCenter(Math.round(position.x + 2), Math.round(position.y + (HEIGHT / 2) - 3));
                swordClone.sprite.rotate(-20);
            } else if(horiDirection.equals("right")) 
            {
                swordClone.sprite.setCenter(Math.round(position.x + WIDTH - 4), Math.round(position.y + (HEIGHT / 2) + 5 - 3));
                swordClone.sprite.rotate(20);
            }
        } 
        else if(spearClone != null) 
        {
            if(horiDirection.equals("left"))
            {
                spearClone.sprite.setCenter(Math.round(position.x - 4), Math.round(position.y + (HEIGHT / 2) - 3));
                spearClone.sprite.rotate(60);
            } 
            else if(horiDirection.equals("right")) 
            {
                spearClone.sprite.setCenter(Math.round(position.x + WIDTH - 4), Math.round(position.y - 3));
                spearClone.sprite.rotate(-60);
            }
        }
    }

    public void attack() 
    {
        effect_slash.play();
        renderSlash = true;
        slashTime = 0f;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera)
    {
        slashTime += Gdx.graphics.getDeltaTime();
        stateTime += Gdx.graphics.getDeltaTime();

        die();

        switch(animState) 
        {
            case 0:
                currentFrame = idleAnim.getKeyFrame(stateTime, true);
                break;
            case 1:
                currentFrame = walkAnim.getKeyFrame(stateTime, true);
                break;
        }

        WIDTH = currentFrame.getRegionWidth();
        HEIGHT = currentFrame.getRegionHeight();

        flip = (horiDirection.equals("left"));
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(currentFrame, flip ? position.x + WIDTH : position.x, position.y, flip ? -WIDTH : WIDTH, HEIGHT);

        renderClones(batch);

        if(renderSlash && slashTime > 0.2f) 
        {
            cooldown = 0;
            renderSlash = false;
        }

        batch.end();

        //inventory_.render();
    }

    private void renderClones(SpriteBatch batch)
    {
        if(swordClone != null) 
        {
            if(!flip) 
            {
                swordClone.sprite.setFlip(true, false);
            }
            if(renderSlash) 
            {
                if(flip) 
                {
                    slash_left.setPosition(position.x - 14, position.y - 2);
                    swordClone.sprite.rotate90(false);
                    swordClone.sprite.setY(swordClone.sprite.getY() - 10);
                    slash_left.draw(batch);
                    swordClone.render(batch);
                    if(cooldown == 0) 
                    {
                        cooldown = 1;
                    }
                } 
                else 
                {
                    slash_right.setPosition(position.x + 14, position.y - 2);
                    swordClone.sprite.rotate90(true);
                    swordClone.sprite.setY(swordClone.sprite.getY() - 10);
                    slash_right.draw(batch);
                    swordClone.render(batch);
                    if(cooldown == 0) {
                        cooldown = 1;
                    }
                }
            }
            swordClone.render(batch);
        }
        if(spearClone != null) 
        {
            if (!flip) 
            {
                spearClone.sprite.setFlip(true, false);
            }
            if (renderSlash)
            {
                if (flip)
                {
                    spearClone.sprite.setPosition(spearClone.sprite.getX() - 17, spearClone.sprite.getY());
                    spearClone.render(batch);
                    if (cooldown == 0) 
                    {
                        cooldown = 1;
                    }
                } 
                else
                {
                    spearClone.sprite.setPosition(spearClone.sprite.getX() + 17, spearClone.sprite.getY());
                    spearClone.render(batch);
                    if (cooldown == 0) {
                        cooldown = 1;
                    }
                }
            }
            spearClone.render(batch);
        }
    }

    public Rectangle getRectangle()
    {
        return box;
    }

    public Rectangle getMovedRect(int x, int y)
    {
        Rectangle box2 = new Rectangle();
        box2.width = box.width;
        box2.height = box.height;
        box2.x = x;
        box2.y = y;

        return box2;
    }

    public Rectangle getInflatedRect() 
    {
        Rectangle box2 = new Rectangle();
        box2.width = box.width + 4;
        box2.height = box.height + 4;
        box2.setCenter(box.x + 2, box.y + 2);
        return box2;
    }

// controlleres mozgás
    public void controllerMove()
    {
        if(World.movingRight)
        {
            if(!World.detector.collisionAt(Math.round(position.x + 3), Math.round(position.y)).equals("obstacle"))
            {
                animState = 1;
                position.x += speed;
                runTemp = true;
            }
        }
        if(World.movingLeft) 
        {
            if(!World.detector.collisionAt(Math.round(position.x - 3), Math.round(position.y)).equals("obstacle"))
            {
                animState = 1;
                position.x -= speed;
                runTemp = true;
            }
        }
        if(World.movingUp) 
        {
            if(!World.detector.collisionAt(Math.round(position.x), Math.round(position.y + 3)).equals("obstacle"))
            {
                animState = 1;
                position.y += speed;
                runTemp = true;
            }
        }
        if(World.movingDown) 
        {
            if(!World.detector.collisionAt(Math.round(position.x), Math.round(position.y - 3)).equals("obstacle"))
            {
                animState = 1;
                position.y -= speed;
                runTemp = true;
            }
        }
        if(!runTemp) animState = 0;
    }

// Mozgás irányítása
    public void keyboardMove()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            if(!World.detector.collisionAt(Math.round(position.x - 3), Math.round(position.y)).equals("obstacle")) 
            {
                position.x -= speed;
                horiDirection = "left";
                animState = 1;
                runTemp = true;
            }

        }

        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            if(!World.detector.collisionAt(Math.round(position.x + 3), Math.round(position.y)).equals("obstacle"))
            {
                position.x += speed;
                horiDirection = "right";
                animState = 1;
                runTemp = true;
            }

        }

        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            if(!World.detector.collisionAt(Math.round(position.x), Math.round(position.y - 3)).equals("obstacle"))
            {
                position.y -= speed;
                vertDirection = "down";
                animState = 1;
                runTemp = true;
            }

        }

        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            if(!World.detector.collisionAt(Math.round(position.x), Math.round(position.y + 3)).equals("obstacle")) 
            {
                position.y += speed;
                vertDirection = "up";
                animState = 1;
                runTemp = true;
            }

        }
        if(!runTemp) animState = 0;
    }

    public void die() 
    {
        if(health <= 0) 
        {
            Gdx.app.log("Player", "died");
            World.playerDie();
        }
    }
}

package com.mycompany.adventure.Levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mycompany.adventure.Classifier;
import com.mycompany.adventure.Controller.ControllerSetup;
import com.mycompany.adventure.Controller.FollowCam;
import com.mycompany.adventure.Debugger;
import com.mycompany.adventure.Effects.ScreenText;
import com.mycompany.adventure.Effects.Sound;
import com.mycompany.adventure.Entities.Evil.Enemy;
import com.mycompany.adventure.Entities.Evil.Skeleton;
import com.mycompany.adventure.Entities.Evil.Slime;
import com.mycompany.adventure.Entities.Good.Adventurer;
import com.mycompany.adventure.Entities.Good.Blacksmith;
import com.mycompany.adventure.Entities.Good.FoodVendor;
import com.mycompany.adventure.Entities.Good.NPC;
import com.mycompany.adventure.Entities.MovementScript;
import com.mycompany.adventure.Entities.Player;
import com.mycompany.adventure.FileUtils.DialogueManager;
import com.mycompany.adventure.FileUtils.GameManager;
import com.mycompany.adventure.FileUtils.Quest;
import com.mycompany.adventure.TileInteraction.CollisionDetector;
import com.mycompany.adventure.TileInteraction.HUD;
import com.mycompany.adventure.TileInteraction.Objects.Chest.Chest;
import com.mycompany.adventure.TileInteraction.Objects.Chest.StorageChest;
import com.mycompany.adventure.TileInteraction.Objects.Chest.TreasureChest;
import com.mycompany.adventure.TileInteraction.Objects.Item;
import com.mycompany.adventure.TileInteraction.Objects.ItemStack;
import com.mycompany.adventure.TileInteraction.Objects.Signpost;
import java.util.ArrayList;
import java.util.Random;

public class World implements Screen {

    public static boolean questAdded = false;
    public static StorageChest chestInventoryDisp = null;
    private TiledMap map;
    private AssetManager manager;
    private SpriteBatch batch;
    private SpriteBatch pauseBatch;
    private SpriteBatch overwriteBatch;
    //private SpriteBatch itemBatch;

    public static ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();
    public static ArrayList<ItemStack> itemStackToRemove = new ArrayList<ItemStack>();
    public static ArrayList<Item> itemToAdd = new ArrayList<Item>();
    public static ArrayList<Item> onFloorToRemove = new ArrayList<Item>();
    public static ArrayList<String> removedEnemies = new ArrayList<String>();
    public static ArrayList<Signpost> signposts = new ArrayList<Signpost>();
    public static ArrayList<TreasureChest> treasureChests = new ArrayList<TreasureChest>();
    public static ArrayList<StorageChest> storageChests = new ArrayList<StorageChest>();
    public static ArrayList<Integer> openedChests = new ArrayList<Integer>();
    public static ArrayList<Quest> quests = new ArrayList<Quest>();
    public static ArrayList<String> questsCompleted = new ArrayList<String>();
    public static ArrayList<Quest> questsToRemove = new ArrayList<Quest>();
    public static ArrayList<NPC> NPCs;

    public static CollisionDetector detector;
    private HUD hud;
    public static FollowCam cam;
    private Player player;
    private Debugger debugger;
    public GameManager gameManager;
    public static Sound levelMusic;
    public static ScreenText drawManager;

    public static ArrayList<Enemy> enemies;
    public static ArrayList<Item> onFloor;

    public static int questY = 450;
    private boolean pauseMenuActive = false;
    private boolean overwriteMenuActive = false;
    public static Signpost signActive = null;
    private boolean renderWords = false;

    public static Game game;

    public static boolean autoPickup = true;
    public static boolean disableMovement = false;
    public static boolean encryptSaveFiles = false;

    public static boolean movingRight, movingLeft, movingUp, movingDown;
    private Sprite options, save_quit, back, go, overwrite;

    public static String focus;
    private Random rand;

    public static int storageID = 100;

    private OrthogonalTiledMapRenderer renderer;
    private int[] layersToRender = new int[] {0, 1, 2, 3, 5, 6};
    private int[] layersToRenderAfter = new int[] {4};
    private boolean npcTextActive;
    private String npcText;
    private NPC npcActive;

    public static final String RED_BOLD = "\033[1;31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public World(Game game, boolean loadData) 
    {

        rand = new Random();

        removedEnemies.clear();
        quests.clear();
        storageChests.clear();
        treasureChests.clear();
        signposts.clear();
        questY = 450;

        this.game = game;
        batch = new SpriteBatch();
        pauseBatch = new SpriteBatch();
        overwriteBatch = new SpriteBatch();
        //player = new Player(580, 1300);
        player = new Player(400, 475);

        onFloor = new ArrayList<Item>();
        enemies = new ArrayList<Enemy>() 
        {{
            add(new Slime("Slime_1", Classifier.Orange_Slime, 1093, 1087, new MovementScript("counterclockwise_1x2")));
            add(new Slime("Slime_2", Classifier.Green_Slime, 1138, 926, new MovementScript("leftRight_2x2")));
            add(new Slime("Slime_3", Classifier.Orange_Slime, 1256, 779, new MovementScript("counterclockwise_1x2")));
            add(new Skeleton("Skeleton_1", Classifier.Skeleton, 1115, 1215, new MovementScript("leftRight_2x2")));
            add(new Skeleton("Skeleton_2", Classifier.Skeleton, 1075, 1100, new MovementScript("stationary")));

        }};

        NPCs = new ArrayList<NPC>() 
        {{
            add(new Blacksmith("Lajcsi", 440, 480, new MovementScript("leftRight_2x2")));
            add(new FoodVendor("Karcsi", 570, 480, new MovementScript("stationary")).setQuest(Gdx.files.internal("Quests/BerryBlast.json")));
            add(new Adventurer("Marci", 600, 480, new MovementScript("leftRight_2x2")).setQuest(Gdx.files.internal("Quests/FindTheSword.json")));
        }};

        options = new Sprite(new Texture(Gdx.files.internal("UI/pause-options.png")));
        save_quit = new Sprite(new Texture(Gdx.files.internal("UI/save-quit.png")));
        back = new Sprite(new Texture(Gdx.files.internal("UI/back.png")));
        go = new Sprite(new Texture(Gdx.files.internal("UI/go.png")));
        overwrite = new Sprite(new Texture(Gdx.files.internal("UI/overwrite.png")));

        focus = "nothing";

        options.setCenter(500, 600);
        save_quit.setCenter(500, 400);
        back.setCenter(500, 200);
        go.setCenter(700, 400);
        overwrite.setCenter(500, 600);

        createItems();
        createSignposts();

        gameManager = new GameManager(player);
        if(loadData)
        {
            if(gameManager.loadData()) 
            {
                applyChanges();
                gameManager.saveData();
                
            } 
            else 
            {
                game.setScreen(new TitleScreen(game, true));
            }
        } 
        else 
        {
            if(gameManager.createNewSave(false)) 
            {
                overwriteMenuActive = true;
            } 
            else
            {
                gameManager.saveData();
            }
        }

        manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("level/overworld_forest_2.tmx", TiledMap.class);
        manager.finishLoading();
        map = manager.get("level/overworld_forest_2.tmx", TiledMap.class);

        renderer = new OrthogonalTiledMapRenderer(map);

        detector = new CollisionDetector(player, map);
        drawManager = new ScreenText();
        ControllerSetup controller = new ControllerSetup(player);
        hud = new HUD(player);
        cam = new FollowCam(player);
        debugger = new Debugger(player);

        createChests();

        loadEnemies();
        loadMusic();
        if(gameManager.data.isMusicOn() || !loadData) 
        {
            levelMusic.play();
        }

    }

    public static void addQuest(Quest questData) {
        Sprite temp = new Sprite(new Texture(Gdx.files.internal("UI/HUD/Inventory/questCard.png")));
        temp.setCenter(320, questY);
        questY -= 65;
        questData.setCard(temp);
        quests.add(questData);

        if(questData.getQuestType().equals("elveszett")) 
        {
            if(questData.getReward().size() > 4)
            {
                Item temp_ = new Item(questData.getReward().get(0), "itemSprites/" + questData.getReward().get(1), Classifier.Weapon, Integer.parseInt(questData.getReward().get(2)),
                        questData.getReward().get(3), Integer.parseInt(questData.getReward().get(4)));
                temp_.loadCoords(Integer.parseInt(questData.getCoordX()), Integer.parseInt(questData.getCoordY()));
                temp_.sprite.setSize(16, 16);
                World.onFloor.add(temp_);
            } 
            else 
            {

            }
        }

    }

    public void removeQuest(Quest questdata) 
    {
        for(Quest quest : quests) 
        {
            if(quest.getQuestName().equals(questdata.getQuestName())) 
            {
                questsToRemove.add(quest);
                questY += 65;
            }
        }
    }

    private void createChests() 
    {
        ArrayList<MapObject> chests_ = detector.getChests();
        for(MapObject chest : chests_) {
            MapProperties props = chest.getProperties();
            int x = Math.round(((RectangleMapObject) chest).getRectangle().getX());
            int y = Math.round(((RectangleMapObject) chest).getRectangle().getY());
            String itemName = (String) props.get("tileName");
            String itemID = "itemSprites/" + props.get("tileID");
            String itemType = (String) props.get("Classifier");
            int itemDurability = (Integer) props.get("Durability");
            String itemDescription = (String) props.get("Description");
            int chestID = (Integer) props.get("ChestID");

            try
            {
                int itemDamage = (Integer) props.get("Damage");
                treasureChests.add(new TreasureChest(chestID, x, y, new Item(itemName, itemID, itemType, itemDurability, itemDescription, itemDamage)));
            }
            catch (NullPointerException e)
            {
                treasureChests.add(new TreasureChest(chestID, x, y, new Item(itemName, itemID, itemType, itemDurability, itemDescription)));
            }
        }
        for(Chest chest2 : treasureChests) 
        {
            if(openedChests.contains(chest2.id)) 
            {
                chest2.chestState = 1;
            }
        }

        if(storageChests.size() == 0) 
        {
            storageChests.add(new StorageChest(player.position.x, player.position.y));
        }
    }

    public static void playerDie() 
    {
        game.setScreen(new GameOver(game));
    }

    private void createSignposts() 
    {

        signposts.add(new Signpost(521, 477, ">>>"));
        signposts.add(new Signpost(1152, 1272, "Gate"));
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(37/255f, 32/255f, 31/255f, 1);
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );


        focus = "nothing";
        questAdded = false;

        if(hud.pausePressed()) 
        {
            pauseMenuActive = true;
        }

        if(pauseMenuActive) 
        {
            drawPauseMenu();
        } 
        else if(overwriteMenuActive)
        {
            drawOverwriteMenu();
        } else
        {

            debugger.printDebug();

            renderer.setView(cam.camera);
            renderer.render(layersToRender);

            batch.begin();
            for(Item item : onFloor) 
            {
                item.render(batch);
            }

            for(Signpost sign : signposts)
            {
                sign.render(batch);
                if(sign.sprite.getBoundingRectangle().overlaps(player.getRectangle())) 
                {
                    focus = "sign";
                    renderWords = true;
                    if(signActive == null) {
                        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) 
                        {
                            signActive = sign;
                            renderWords = true;
                            disableMovement = true;
                        }
                    }
                    else
                    {
                        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) 
                        {
                            signActive = null;
                            renderWords = false;
                            disableMovement = false;
                        }
                    }
                } 
                else
                {
                    if(!renderWords) 
                    {
                        signActive = null;
                    }
                }
            }

            for(Enemy enemy : enemies) 
            {
                enemy.render(batch);
                if(player.swordClone != null)
                {
                    if(!enemy.hit && player.cooldown == 1 && detector.EnemycollisionWith(player.swordClone, enemy)) 
                    {
                        enemy.hit = true;
                        Gdx.app.log("Player", "Hit Enemy: " + enemy.getName());
                        if(enemy.animState != 3) enemy.takeDamage(player.inventory_.itemSelected.damage, player.horiDirection);
                        for(ItemStack item : player.inventory_.inventory) 
                        {
                            if(player.swordClone.name.equals(item.stackedItem.name))
                            {
                                item.stackedItem.durability -= enemy.hardness;
                            }
                        }
                    }
                    if(player.cooldown == 0)
                    {
                        enemy.hit = false;
                    }
                } 
                else if(player.spearClone != null) 
                {
                    if (!enemy.hit && player.cooldown == 1 && detector.EnemycollisionWith(player.spearClone, enemy)) 
                    {
                        enemy.hit = true;
                        Gdx.app.log("Player", "Hit Enemy: " + enemy.getName());
                        if(enemy.animState != 3) enemy.takeDamage(player.inventory_.itemSelected.damage, player.horiDirection);
                        for(ItemStack item : player.inventory_.inventory)
                        {
                            if (player.spearClone.name.equals(item.stackedItem.name))
                            {
                                item.stackedItem.durability -= enemy.hardness;
                            }
                        }
                    }
                    if (player.cooldown == 0) 
                    {
                        enemy.hit = false;
                    }
                }
            }

            for(NPC npc : NPCs) // Minden NPC-re vonatkozik
            { 
                npc.render(batch);
                if(npc.getRect().overlaps(player.getRectangle()))  // Ha a játékos keresztezi az NPC-t
                {
                    focus = "NPC";
                    if (Gdx.input.isKeyJustPressed(Input.Keys.F)) // Ha a játékos megnyomja az "f" betűt
                    { 
                        if (!npcTextActive) // Ha a szöveg inaktív
                        { 
                            npcTextActive = true;
                            npcActive = npc;
                            disableMovement = true;
                            player.setStop();
                            if(npc.questType != null)  // Ha az NPC-nél van felvehető küldetés
                            {
                                if(!World.quests.contains(npc.quest.questData)) // Ha a küldetés még nincs átvéve
                                { 
                                    npcText = npc.getQuestText();
                                } 
                                else 
                                {
                                    if(!npc.questFinished)  // Ha a küldetés nincs befejezve
                                    {
                                        if(npc.quest.questData.getQuestType().equals("elveszett"))  // Ha a küldetéshez szükséges tárgyak megvannak
                                        {
                                            for (ItemStack item_ : player.inventory_.inventory) 
                                            {
                                                if (item_.stackedItem.description.equals(npc.quest.questData.getReward().get(3))) // Ha a tárgyak leírása megegyezik
                                                { 
                                                    npcText = npc.quest.questData.getEndText();
                                                    npc.questFinished = true;
                                                    questsCompleted.add(npc.quest.questData.getQuestName());
                                                    removeQuest(npc.quest.questData);
                                                }
                                                else
                                                {
                                                    npcText = npc.dialog.dialogueData.getDialogue()[rand.nextInt(npc.dialog.dialogueData.getDialogue().length - 1)];
                                                }
                                            }
                                        } 
                                        else if(npc.quest.questData.getQuestType().equals("collection")) 
                                        { 
                                            for (ItemStack item_ : player.inventory_.inventory) 
                                            {
                                                if(item_.stackedItem.name.equals(npc.quest.questData.getFind().get(0)) &&
                                                        item_.count >= Integer.parseInt(npc.quest.questData.getFind().get(1))) // Ha egyezik a név és megvan a mennyiség
                                                { 
                                                    npcText = npc.quest.questData.getEndText();
                                                    if(item_.count == Integer.parseInt(npc.quest.questData.getFind().get(1))) //  Ha pont ugyanannyi
                                                    {
                                                        itemStackToRemove.add(item_);
                                                    } 
                                                    else
                                                    {
                                                        item_.count -= Integer.parseInt(npc.quest.questData.getFind().get(1));
                                                    }
                                                    npc.giveQuestReward();
                                                    npc.questFinished = true;
                                                    questsCompleted.add(npc.quest.questData.getQuestName());
                                                    removeQuest(npc.quest.questData);
                                                }
                                                else
                                                {
                                                    npcText = npc.dialog.dialogueData.getDialogue()[rand.nextInt(npc.dialog.dialogueData.getDialogue().length - 1)];
                                                }
                                            }
                                        } 
                                        else 
                                        {
                                            npcText = npc.dialog.dialogueData.getDialogue()[rand.nextInt(npc.dialog.dialogueData.getDialogue().length - 1)];
                                        }
                                    } 
                                    else 
                                    {
                                        npcText = npc.dialog.dialogueData.getDialogue()[rand.nextInt(npc.dialog.dialogueData.getDialogue().length - 1)];
                                    }
                                }
                            } 
                            else 
                            {
                                npcText = npc.dialog.dialogueData.getDialogue()[rand.nextInt(npc.dialog.dialogueData.getDialogue().length - 1)];
                            }
                        } 
                        else 
                        {
                            if(npc.questType != null) 
                            {
                                if(!World.quests.contains(npc.quest.questData)) 
                                {
                                    if(!npc.questFinished) hud.drawNewQuest = npc;
                                }
                            }
                            npcTextActive = false;
                            npcText = null;
                            disableMovement = false;
                            npc.stop = false;
                            npcActive = null;
                        }

                    }
                }
            }

            for(TreasureChest chest : treasureChests)
            {
                chest.render(batch);
                if(chest.getRect().overlaps(player.getRectangle())) 
                {
                    focus = "chest";
                    if(Gdx.input.isKeyJustPressed(Input.Keys.F))
                    {
                        if(chest.chestState != 1) 
                        {
                            chest.displayContents();
                            openedChests.add(chest.id);
                        }
                        chest.chestState = 1;
                    }
                }
            }

            for(StorageChest chest : storageChests) 
            {
                chest.render(batch);
                if(chest.getRect().overlaps(player.getRectangle()))
                {
                    focus = "chest";
                    if(Gdx.input.isKeyJustPressed(Input.Keys.F)) 
                    {
                        if(chestInventoryDisp != null) 
                        {
                            chestInventoryDisp.chestState = 0;
                            player.inventory_.renderChestOverlay = false;
                            player.inventory_.renderOverlay = false;
                            chestInventoryDisp = null;
                            disableMovement = false;
                            player.inventory_.refreshInventory();
                        }
                        else
                        {
                            chest.chestState = 1;
                            player.inventory_.renderChestOverlay = true;
                            player.inventory_.renderOverlay = true;
                            chestInventoryDisp = chest;
                        }

                    }
                }
            }

            batch.end();

            if(disableMovement) 
            {
                player.update(true);
            } 
            else
            {
                player.update(false);
            }
            cam.update();

            player.render(batch, cam.camera);

            //debugger.renderDebugBoxes(enemies, onFloor);
            renderer.render(layersToRenderAfter);

            if(chestInventoryDisp != null) 
            {
                chestInventoryDisp.displayInventory(player.inventory_.overlay);
                disableMovement = true;
            }

            hud.render();
            if(signActive != null) 
            {
                hud.drawSign(signActive);
            }
            player.inventory_.render();

            if(npcTextActive) 
            {
                hud.drawNPCDialog(npcActive, npcText);
                if(npcActive.questType != null){}
            }

            for(Enemy enemy : enemiesToRemove) 
            {
                enemies.remove(enemy);
                if(!removedEnemies.contains(enemy.getName())) 
                {
                    removedEnemies.add(enemy.getName());
                }
            }

            for(Item item : onFloorToRemove) 
            {
                onFloor.remove(item);
            }

            for(ItemStack item : itemStackToRemove) 
            {
                player.inventory_.inventory.remove(item);
                player.inventory_.refreshInventory();
            }

            for(Quest quest : questsToRemove)
            {
                quests.remove(quest);
            }

            for(Item item : itemToAdd)
            {
                player.inventory_.addItem(item);
            }

            enemiesToRemove.clear();
            onFloorToRemove.clear();
            itemStackToRemove.clear();
            questsToRemove.clear();
            itemToAdd.clear();
        }

    }


    private void setSave() 
    {
        gameManager.data.setPlayerPosition(player.position);
        gameManager.data.setInventory(player.inventory_.inventory);
        gameManager.data.setOnFloor(onFloor);
        gameManager.data.setHealth(player.health);
        gameManager.data.setHearts(player.hearts);
        gameManager.data.setSlotSelected(player.inventory_.slotSelected);
        gameManager.data.setEnemiesDead(removedEnemies);
        gameManager.data.setCoins(player.coins);
        gameManager.data.setOpenedChests(openedChests);
        gameManager.data.setStorageChests(storageChests);
        gameManager.data.setQuestsCompleted(questsCompleted);
        gameManager.saveData();
    }

    private void applyChanges() 
    {
        player.position = gameManager.data.getPlayerPosition();
        player.health = gameManager.data.getHealth();
        player.hearts = gameManager.data.getHearts();
        player.coins = gameManager.data.getCoins();
        player.inventory_.loadInventory(gameManager);
        player.inventory_.slotSelected = gameManager.data.getSlotSelected();

        openedChests = gameManager.data.getOpenedChests();
        removedEnemies = gameManager.data.getEnemiesDead();
        onFloor = gameManager.data.getOnFloor();
        storageChests = gameManager.data.getStorageChests();
        try 
        {
            if(onFloor.size() != 0) 
            {
                for(Item item : onFloor)
                {
                    item.sprite.setSize(16, 16);
                }
            }
        } 
        catch (NullPointerException e) 
        {
            Gdx.app.log(RED_BOLD + "FILE ERROR", "CORRUPT LOAD FILE (maybe you forgot to save?)" + ANSI_RESET);
            System.exit(1);

        }


    }

    private void drawOverwriteMenu()
    {
        back.setCenter(300, 400);
        overwriteBatch.begin();
        go.draw(overwriteBatch);
        back.draw(overwriteBatch);
        overwrite.draw(overwriteBatch);
        overwriteBatch.end();

        if(Gdx.input.isTouched()) 
        {
            if (go.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) 
            {
                gameManager.createNewSave(true);
                gameManager.saveData();
                overwriteMenuActive = false;
            } 
            else if (back.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY()))
            {
                game.setScreen(new TitleScreen(game, false));
            }
        }
    }

    private void drawPauseMenu()
    {
        back.setCenter(500, 200);
        pauseBatch.begin();
        options.draw(pauseBatch);
        save_quit.draw(pauseBatch);
        back.draw(pauseBatch);
        pauseBatch.end();

        if(Gdx.input.isTouched()) 
        {
            if (options.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) 
            {
                setSave();
                game.setScreen(new Options(game, true, gameManager));
            }
            else if (save_quit.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) 
            {
                setSave();
                levelMusic.stop();
                game.setScreen(new TitleScreen(game, false));
            } 
            else if (back.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) 
            {
                levelMusic.play();
                pauseMenuActive = false;
            }
        }
    }

    private void createItems() 
    {
        // Name Path Type Durability Description Damage
        Item sword = new Item("Az utazó kardja", "itemSprites/tile072.png", Classifier.Weapon, 50, "Hétköznapi kovácsolt kard", 2);
        sword.loadCoords(772, 1373);
        sword.sprite.setSize(16, 16);

        Item spear = new Item("Posszeidon szigonya", "itemSprites/tile118.png", Classifier.Weapon, 50, "Csak ne kerüljön rossz kezekbe", 10);
        spear.loadCoords(772, 1380);
        spear.sprite.setSize(16, 16);

        onFloor.add(sword);
        onFloor.add(spear);
        Gdx.app.log("World", "OnFloor Sprites Loaded");
    }

    private void loadEnemies()
    {
        enemiesToRemove.clear();
        for(Enemy enemy : enemies) 
        {
            for(String enemyName : removedEnemies)
            {
                if(enemy.getName().equals(enemyName))
                {
                    enemiesToRemove.add(enemy);
                }
            }
        }
        Gdx.app.log("World", "Enemies Loaded");
    }

    private void loadMusic()
    {
        levelMusic = new Sound("worldMusic", true);
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() 
    {
        manager.dispose();
        batch.dispose();
    }

}
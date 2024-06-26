package com.mycompany.adventure.FileUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.mycompany.adventure.Entities.Good.NPC;
import com.mycompany.adventure.Levels.World;

public class DialogueManager 
{
    public Dialogue dialogueData;
    private FileHandle data;
    private Json json;
    private NPC npc;

    public DialogueManager(NPC npc, FileHandle data)
    {
        this.json = new Json();
        this.data = data;
        this.npc = npc;

        parseRequirements();
    }

    private void parseRequirements() 
    {
        try 
        {
            dialogueData = json.fromJson(Dialogue.class, data.readString());
        } 
        catch(Exception e) 
        {
            Gdx.app.log("Dialogue", "Dialogue Parsing Failed");
            e.printStackTrace();
        }
    }
}

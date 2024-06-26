package com.mycompany.adventure.TileInteraction.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

//Statusbar a tárgyak tartósságának követésére
public class ItemDurabilityBar 
{
    //ShapeRenderer változó alakzat létrehozásához
    private ShapeRenderer durRenderer;
    
    public ItemDurabilityBar()
    {
        durRenderer = new ShapeRenderer();
    }
    
    public ShapeRenderer getDurRenderer()
    {
        return durRenderer;
    }
    
    //render metódus az alakzat megrajzolásához (Item osztály "durability" változó hívása)
    public void render(Item item)
    {
        //Színek renderelése állapothoz mérten
        float durabilityFraction = (float) item.durability / item.baseDur;
        if(durabilityFraction >= 0.35 && durabilityFraction <= 0.6) 
        {
            durRenderer.setColor(Color.YELLOW);
        } 
        else if(durabilityFraction > 0.6) 
        {
            durRenderer.setColor(Color.GREEN);
        } 
        else if(durabilityFraction < 0.35) 
        {
            durRenderer.setColor(Color.RED);
        }
        durRenderer.rect(item.sprite.getX() + 5, item.sprite.getY() - 5, durabilityFraction * 25, 2);
    }
}

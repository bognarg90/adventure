package com.mycompany.adventure.Effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mycompany.adventure.Levels.World;


// Játékhangok / Zene megállítása
public class Sound 
{
    private Music music;
    
    public Sound(String track,boolean looping)
    {
        music = Gdx.audio.newMusic(Gdx.files.internal("Music/" + track + ".mp3"));
        if(looping){music.setLooping(true);}
    }
    
    public Sound(String track)
    {
        music = Gdx.audio.newMusic(Gdx.files.internal(track));
    }
    
    public void play() 
    {
        music.play();
    }

    public void pause() 
    {
        music.pause();
    }

    public void stop() 
    {
        music.stop();
    }
}

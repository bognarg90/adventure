package com.mycompany.adventure.Levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mycompany.adventure.Effects.Sound;
import com.mycompany.adventure.TileInteraction.GifDecoder;

public class GameOver implements Screen
{
    private Game game;

    private Sprite title, load, quit;
    private SpriteBatch screen;
    private TextureRegion currentFrame;

    private float stateTime = 0f;

    private Animation<TextureRegion> gdec;
    

    private Sound music;

    public GameOver(Game game) 
    {

        Gdx.app.log("Title", "Set game over screen");

        
        this.game = game;
        gdec = GifDecoder.loadGIFAnimation(Animation.PlayMode.NORMAL, Gdx.files.internal("UI/gameOver.gif").read());
        screen = new SpriteBatch();

        title = new Sprite(new Texture(Gdx.files.internal("UI/title.png")));
        
        this.load = new Sprite(new Texture(Gdx.files.internal("UI/load-game.png")));
        this.quit = new Sprite(new Texture(Gdx.files.internal("UI/quit.png")));

        title.setCenter(500, 450);
        load.setCenter(500, 390);
        quit.setCenter(500, 325);
    }

    @Override
    public void show() { }

    @Override
    public void render(float f) {}

    @Override
    public void resize(int i, int i1) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}

package com.mycompany.adventure.Levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class About implements Screen
{
    private Game game;
    private SpriteBatch screen;
    
    private Sprite info, created, back;
    
    public About(Game game)
    {
        this.game = game;
        screen = new SpriteBatch();
        
        created = new Sprite(new Texture(Gdx.files.internal("UI/createdBy.png")));
        info = new Sprite(new Texture(Gdx.files.internal("UI/information.png")));
        back = new Sprite(new Texture(Gdx.files.internal("UI/back.png")));
        
        created.setCenter(500, 600);
        info.setCenter(500, 400);
        back.setCenter(100, 50);
    }
    
    @Override
    public void show(){}

    @Override
    public void render(float delta) 
    {
        Gdx.gl.glClearColor(37/255f, 32/255f, 31/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        screen.begin();
        created.draw(screen);
        info.draw(screen);
        back.draw(screen);
        screen.end();
        
        backPressed();
    }
    
    private void backPressed()
    {
        if(Gdx.input.isTouched())
        {
            if(back.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY()))
            {
                game.setScreen(new TitleScreen(game, false));
            }
        }
    }

    @Override
    public void resize(int i, int i1) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() { }

    @Override
    public void dispose() {}
    
}

package com.mycompany.adventure.Levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mycompany.adventure.Effects.Sound;

public class TitleScreen implements Screen
{
    private Game game;
    
    private Sprite title, title_01, play, play_01, help, help_01, about,  about_1, titleIcon, loadGame, loadGame_1, notFound;
    private SpriteBatch screen;
    
    //Animációk
    int rotation = 1;
    boolean fileNotFound;
    private Sound music;
    private boolean renderTransition = false;
    
    ParticleEffect pe;
    
    public TitleScreen(Game game, boolean fileNotFound)
    {
        Gdx.app.log("Title", "Set title screen");
        music = new Sound("Music/title_music.mp3");
        
        this.game = game;
        screen = new SpriteBatch();
        this.fileNotFound = fileNotFound;
        
        title = new Sprite(new Texture(Gdx.files.internal("UI/title.png")));
        title_01 = new Sprite(new Texture(Gdx.files.internal("UI/title-1.png")));
        play = new Sprite(new Texture(Gdx.files.internal("UI/play.png")));
        play_01 = new Sprite(new Texture(Gdx.files.internal("UI/play-1.png")));
        help = new Sprite(new Texture(Gdx.files.internal("UI/howToPlay.png")));
        help_01 = new Sprite(new Texture(Gdx.files.internal("UI/howToPlay-1.png")));
        about = new Sprite(new Texture(Gdx.files.internal("UI/about.png")));
        about_1 = new Sprite(new Texture(Gdx.files.internal("UI/about-1.png")));
        loadGame = new Sprite(new Texture(Gdx.files.internal("UI/load-game.png")));
        loadGame_1 = new Sprite(new Texture(Gdx.files.internal("UI/load-game_1.png")));
        notFound = new Sprite(new Texture(Gdx.files.internal("UI/not-found.png")));

        titleIcon = new Sprite(new Texture(Gdx.files.internal("UI/titleIcon.png")));
    
        title.setCenter(500, 700);
        title_01.setCenter(500, 700);
        play.setCenter(500, 200);
        play_01.setCenter(500, 200);
        help.setCenter(750, 200);
        help_01.setCenter(750, 200);
        about.setCenter(250, 200);
        about_1.setCenter(250, 200);
        loadGame.setCenter(500, 100);
        loadGame_1.setCenter(500, 100);
        notFound.setCenter(500, 60);

        titleIcon.setCenter(500,450);
        
        //music.play();
        
        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("UI/HUD/everblade_fire3"),Gdx.files.internal("UI/HUD/"));
        pe.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        pe.start();
    }
    
    @Override
    public void show()
    {
        
    }

    @Override
    public void render(float delta) 
    {
        pe.update(Gdx.graphics.getDeltaTime());

        if(rotation != 360) 
        {
            rotation += 1;
        } 
        else 
        {
            rotation = 0;
        }

        Gdx.gl.glClearColor(37/255f, 32/255f, 31/255f, 1);
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        screen.begin();

        pe.draw(screen);

        if (title.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) 
        {
            title_01.draw(screen);
        } 
        else 
        {
            title.draw(screen);
        }
        if (play.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) 
        {
            play_01.draw(screen);
        } 
        else 
        {
            play.draw(screen);
        }
        if (about.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) 
        {
            about_1.draw(screen);
        } 
        else 
        {
            about.draw(screen);
        }
        if (help.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) 
        {
            help_01.draw(screen);
        } 
        else 
        {
            help.draw(screen);
        }
        if (loadGame.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) 
        {
            loadGame_1.draw(screen);
        } 
        else 
        {
            loadGame.draw(screen);
        }
        
        if(fileNotFound) 
        {
            notFound.draw(screen);
        }
        titleIcon.setRotation(135);
        titleIcon.draw(screen);
        screen.end();

        if (pe.isComplete()) pe.reset();

        buttonPressed();

    }
    
    private void buttonPressed()
    {
        if(Gdx.input.isTouched()) 
        {
            if(play.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY()))
            {
                music.stop();
                game.setScreen(new World(game, false));
            } 
            else if(help.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY()))
            {
                music.stop();
                game.setScreen(new HelpScreen(game));
            } 
            else if(about.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) 
            {
                music.stop();
                game.setScreen(new About(game));
            } 
            else if(loadGame.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) 
            {
                music.stop();
                game.setScreen(new World(game, true));
            }
        }
    }

    @Override
    public void resize(int width, int height){}

    @Override
    public void pause(){}

    @Override
    public void resume(){}

    @Override
    public void hide(){}

    @Override
    public void dispose(){}
    
}

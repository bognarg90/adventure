package com.mycompany.adventure.Levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HelpScreen implements Screen
{
    private Game game;
    private SpriteBatch screen;
    
    private Sprite back;
    private BitmapFont title = new BitmapFont(Gdx.files.internal("Fonts/Retron2.fnt"), Gdx.files.internal("Fonts/Retron2.png"), false);
    private BitmapFont plot = new BitmapFont(Gdx.files.internal("Fonts/Retron2.fnt"), Gdx.files.internal("Fonts/Retron2.png"), false);
    private BitmapFont plot2 = new BitmapFont(Gdx.files.internal("Fonts/Retron2.fnt"), Gdx.files.internal("Fonts/Retron2.png"), false);

    private BitmapFont move = new BitmapFont(Gdx.files.internal("Fonts/Retron2.fnt"), Gdx.files.internal("Fonts/Retron2.png"), false);
    private BitmapFont QDesc = new BitmapFont(Gdx.files.internal("Fonts/Retron2.fnt"), Gdx.files.internal("Fonts/Retron2.png"), false);
    private BitmapFont EDesc = new BitmapFont(Gdx.files.internal("Fonts/Retron2.fnt"), Gdx.files.internal("Fonts/Retron2.png"), false);
    private BitmapFont FDesc = new BitmapFont(Gdx.files.internal("Fonts/Retron2.fnt"), Gdx.files.internal("Fonts/Retron2.png"), false); 
    
    private ParticleEffect pe1, pe2;
    
    public HelpScreen(Game game)
    {
        this.game = game;
        screen = new SpriteBatch();
        
        back = new Sprite(new Texture(Gdx.files.internal("UI/back.png")));
        back.setCenter(100,50);
        
        pe1 = new ParticleEffect();
        pe2 = new ParticleEffect();
        
        pe1.load(Gdx.files.internal("UI/HUD/everblade_fire3"),Gdx.files.internal("UI/HUD/"));
        pe2.load(Gdx.files.internal("UI/HUD/everblade_fire3"),Gdx.files.internal("UI/HUD/"));
        
        pe1.getEmitters().first().setPosition(Gdx.graphics.getWidth()/8, 500);
        pe1.start();

        pe2.getEmitters().first().setPosition((Gdx.graphics.getWidth() / 8) * 7, 500);
        pe2.start();
    }
    @Override
    public void show(){}

    @Override
    public void render(float delta) 
    {
        Gdx.gl.glClearColor(37/255f, 32/255f, 31/255f, 1);
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        
        pe1.update(Gdx.graphics.getDeltaTime());
        pe2.update(Gdx.graphics.getDeltaTime());
        
        screen.begin();
        
        pe1.draw(screen);
        pe2.draw(screen);
        
        back.draw(screen);
        
        GlyphLayout layout = new GlyphLayout();
        layout.setText(title, "Útmutató");
        title.setColor(Color.FIREBRICK);
        title.getData().setScale(1.5f);
        title.draw(screen, "Útmutató", (Gdx.graphics.getWidth() / 2) - layout.width / 2, 780);
        
        layout.setText(plot, "Gipsz Jakab, a mágus megfertözte teremtményeivel a világot");
        plot.setColor(Color.CORAL);
        plot.getData().setScale(0.5f);
        plot.draw(screen, "Gipsz Jakab, a mágus megfertözte teremtményeivel a világot", (Gdx.graphics.getWidth() / 2) - layout.width / 2, 630);
        
        layout.setText(plot2, "Találd meg a Gipszvágo nevű ösi fegyvert, hogy legyőzhesd öt!");
        plot2.setColor(Color.CORAL);
        plot2.getData().setScale(0.5f);
        plot2.draw(screen, "Találd meg a Gipszvágo nevű ösi fegyvert, hogy legyőzhesd öt!", (Gdx.graphics.getWidth() / 2) - layout.width / 2, 600);

        layout.setText(move, "Mozgasd a játékost a 'W A S D' gombokkal");
        move.setColor(Color.TAN);
        move.getData().setScale(0.5f);
        move.draw(screen, "Mozgasd a játékost a 'W A S D' gombokkal", (Gdx.graphics.getWidth() / 2) - layout.width / 2, 500);

        layout.setText(QDesc, "Tárgy felvételéhez, csak sétálj rá. Eldobás: 'Q'");
        QDesc.setColor(Color.TAN);
        QDesc.getData().setScale(0.5f);
        QDesc.draw(screen, "Tárgy felvételéhez, csak sétálj rá. Eldobás: 'Q'", (Gdx.graphics.getWidth() / 2) - layout.width / 2, 470);

        layout.setText(EDesc, "Nyisd meg a tárhelyet a 'E'-vel. Itt találod a küldetéseket és a leírást.");
        EDesc.setColor(Color.TAN);
        EDesc.getData().setScale(0.4f);
        EDesc.draw(screen, "Nyisd meg a tárhelyet a 'E'-vel. Itt találod a küldetéseket és a leírást.", (Gdx.graphics.getWidth() / 2) - layout.width / 2, 440);

        layout.setText(FDesc, "Tárgy használata: 'F' (evés, támadás, nyitás, dialógusok)");
        FDesc.setColor(Color.TAN);
        FDesc.getData().setScale(0.5f);
        FDesc.draw(screen, "Tárgy használata: 'F' (evés, támadás, nyitás, dialógusok)", (Gdx.graphics.getWidth() / 2) - layout.width / 2, 410);

        screen.end();

        if (pe1.isComplete()) pe1.reset();
        if (pe2.isComplete()) pe2.reset();

        buttonPressed();
    }
    
    public void buttonPressed()
    {
        if(Gdx.input.isTouched()) 
        {
            if(back.getBoundingRectangle().contains(Gdx.input.getX(), 800 - Gdx.input.getY())) 
            {
                Gdx.app.log("Help", "Returning to Title Screen");
                game.setScreen(new TitleScreen(game, false));
            }
        }
    }

    @Override
    public void resize(int i, int i1){}
    @Override
    public void pause(){}
    @Override
    public void resume(){}
    @Override
    public void hide(){}
    @Override
    public void dispose(){}
}

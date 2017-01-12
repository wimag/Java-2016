package torrentGUI.scnene;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import torrentGUI.GuiClient;

import java.io.IOException;

public class GUI extends Game {
    private Screen mainScreen;

    public final GuiClient client;


    public GUI(GuiClient guiClient) {
        this.client = guiClient;
    }

    @Override
    public void create() {
        mainScreen = new MainScreen(this);
        setScreen(mainScreen);
    }

    @Override
    public void dispose() {
        mainScreen.dispose();
        try {
            client.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }


}
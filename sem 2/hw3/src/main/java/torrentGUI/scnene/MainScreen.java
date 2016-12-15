package torrentGUI.scnene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import torrentGUI.scnene.files.FileDownloader;
import torrentGUI.scnene.files.FileUploader;
import torrentGUI.scnene.paths.PathGenerator;
import torrentGUI.scnene.trackers.FileMetaInf;
import torrentGUI.scnene.trackers.ListTracker;
import torrentGUI.scnene.trackers.LocalFilesTracker;
import torrentGUI.scnene.trackers.RemoteFilesTracker;

/**
 * Created by Mark on 14.12.2016.
 */
public class MainScreen implements Screen {
    private ScrollPane scrollPane;
    private List<String> list;
    Skin skin;
    SpriteBatch batcher;
    float gameWidth, gameHeight;
    TextureAtlas atlas;
    private Stage stage;
    private GUI mainGUI;
    private TextButton pick, state;
    private float sinceLastUpdate = 0;
    private ListTracker tracker;
    private Label filename, filesize, progress;
    private PathGenerator pathGenerator;
    ShapeRenderer shapeRenderer;

    private FileMetaInf selectedFileMeta = null;

    public MainScreen(GUI mainGUI){
        this.mainGUI = mainGUI;
        tracker = new LocalFilesTracker(this, mainGUI.client.storage);
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        gameWidth = Gdx.graphics.getWidth();
        gameHeight = Gdx.graphics.getHeight();
        atlas = new TextureAtlas(Gdx.files.internal("ui/atlas.pack"));
        skin = new Skin(Gdx.files.internal("ui/list_skin.json"), atlas);

        list = new List<>(skin);

        batcher = new SpriteBatch();
        scrollPane = new ScrollPane(list);
        scrollPane.setBounds(0, 0, gameWidth / 3f, gameHeight * 13 / 15 - 75);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setPosition(gameWidth / 15, gameHeight / 15 + 75);
        scrollPane.setTransform(true);

        stage = new Stage(new StretchViewport(gameWidth, gameHeight));
        stage.addActor(scrollPane);
        Gdx.input.setInputProcessor(stage);

        pick = new TextButton("UPLOAD", skin);
        pick.setPosition(gameWidth/15, 20);
        pick.setWidth(gameWidth/3f);

        state = new TextButton("REMOTE", skin);
        state.setPosition(gameWidth/15, 20 + pick.getHeight());
        state.setWidth(gameWidth/3f);

        Label filenameCaption = new Label("File name:", skin);
        filenameCaption.setPosition(gameWidth / 2, gameHeight - 50);
        filenameCaption.setFontScale(0.65f);

        filename = new Label("", skin);
        filename.setWrap(true);
        filename.setPosition(gameWidth / 2, gameHeight - 80);
        filename.setWidth(gameWidth / 4);
        filename.setFontScale(0.65f);

        progress = new Label("", skin);
        progress.setPosition(gameWidth * 2 / 3, 50);


        Label fileSizeCaption = new Label("File size:", skin);
        fileSizeCaption.setPosition(gameWidth * 3 / 4 + 20, gameHeight - 50);
        fileSizeCaption.setFontScale(0.65f);

        filesize = new Label("", skin);
        filesize.setPosition(gameWidth * 3 / 4 + 20, gameHeight - 80);
        filesize.setFontScale(0.65f);
        pick.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ListTracker tmp = tracker;
                if(tmp instanceof LocalFilesTracker){
                    new Thread(new FileUploader(mainGUI.client)).start();
                } else {
                    new Thread(new FileDownloader(mainGUI.client, tracker.getMetaFor(list.getSelectedIndex()))).start();
                }

            }
        });

        state.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchMode();
                state.setChecked(!state.isChecked());
            }
        });

        list.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tracker.onPick(list.getSelectedIndex());
            }
        });

        stage.addActor(pick);
        stage.addActor(state);
        stage.addActor(filename);
        stage.addActor(filesize);
        stage.addActor(filenameCaption);
        stage.addActor(fileSizeCaption);
        stage.addActor(progress);
        updateTimed(0);
    }

    /**
     * Update info about displayed file
     * @param meta
     */
    public synchronized void showMetaInf(FileMetaInf meta){
        if(meta == null){
            return;
        }
        filename.setText(meta.formatName());
        filesize.setText(meta.formatSize());
        selectedFileMeta = meta;
        pathGenerator = new PathGenerator(meta.id);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updateTimed(delta);
        stage.act(delta);
        stage.draw();
        drawProgress();
    }

    /**
     * update list every 0.25 seconds
     * @param delta
     */
    private void updateTimed(float delta){
        sinceLastUpdate += delta;
        if(sinceLastUpdate >= 0.1f){
            synchronized (this){
                if(sinceLastUpdate >= tracker.getTimeout()){
                    list.setItems(tracker.getOptions());
                    sinceLastUpdate = 0;
                }
            }
            FileMetaInf currentMeta;
            synchronized (this){
                currentMeta = selectedFileMeta;
            }
            if (currentMeta != null && mainGUI.client.storage.hasFile(currentMeta.id)){
                showMetaInf(tracker.getMetaFor(list.getSelectedIndex()));
                double ratio = mainGUI.client.storage.getFile(currentMeta.id).getDownloadedRatio();
                int percent = (int)(ratio * 100);
                progress.setText(percent + "%");
            }
        }
    }

    private void drawProgress(){
        if(pathGenerator == null){
            return;
        }
        FileMetaInf meta = selectedFileMeta;
        if(meta == null || !meta.downloaded){
            return;
        }

        int k = 500;
        Vector2 a = new Vector2();
        Vector2 b = new Vector2();
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);

        for(int i = 0; i < k-1; ++i)
        {
            pathGenerator.valueAt(a, ((float)i)/((float)k-1));
            pathGenerator.valueAt(b, ((float)(i+1))/((float)k-1));
            a.scl(gameWidth / 2f - 30, gameHeight * 0.4f);
            a.add(gameWidth / 2f + 10, 60);
            b.scl(gameWidth / 2f - 30, gameHeight * 0.4f);
            b.add(gameWidth / 2f + 10, 60);
            shapeRenderer.line(a, b);
        }
        float progress = (float) mainGUI.client.storage.getFile(meta.id).getDownloadedRatio();
        shapeRenderer.end();
        shapeRenderer.begin(ShapeType.Filled);
        pathGenerator.valueAt(a, 0f);
        a.scl(gameWidth / 2f - 30, gameHeight * 0.4f);
        a.add(gameWidth / 2f + 10, 60);
        shapeRenderer.circle(a.x, a.y, 7f);
        pathGenerator.valueAt(a, 1f);
        a.scl(gameWidth / 2f - 30, gameHeight * 0.4f);
        a.add(gameWidth / 2f + 10, 60);
        shapeRenderer.circle(a.x, a.y, 7f);
        shapeRenderer.setColor(0, 1, 0, 1);
        pathGenerator.valueAt(a, progress);
        a.scl(gameWidth / 2f - 30, gameHeight * 0.4f);
        a.add(gameWidth / 2f + 10, 60);
        shapeRenderer.circle(a.x, a.y, 5f);
        shapeRenderer.end();
    }

    /**
     * Switch between browsing local files and remote
     */
    private synchronized void switchMode(){
        if (tracker instanceof LocalFilesTracker){
            tracker = new RemoteFilesTracker(this, mainGUI.client);
            state.setText("LOCAL");
            pick.setText("DOWNLOAD");
        } else{
            tracker = new LocalFilesTracker(this, mainGUI.client.storage);
            state.setText("REMOTE");
            pick.setText("UPLOAD");
        }
        sinceLastUpdate = Float.MAX_VALUE;
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

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        atlas.dispose();
    }
}

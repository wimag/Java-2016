package torrentGUI.scnene.paths;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Mark on 14.12.2016.
 */
public class PathGenerator {
    private final Vector2[] dataset;
    private final CatmullRomSpline<Vector2> trajectory;

    public PathGenerator(int id){
        this(id, 4);
    }

    public PathGenerator(int id, int N){
        Random random = new Random(id);
        dataset = new Vector2[N + 4];
        dataset[0] = new Vector2(0, 1);
        dataset[1] = new Vector2(0.1f, 0.9f);
        dataset[N+2] = new Vector2(0.95f, 0.05f);
        dataset[N+3] = new Vector2(1, 0);
        for (int i = 2; i <= N + 1; i++) {
            dataset[i] = new Vector2(random.nextFloat(), random.nextFloat());
        }
        trajectory = new CatmullRomSpline<>(dataset, false);
    }

    public void valueAt(Vector2 out, float t){
        trajectory.valueAt(out, t);
    }
}

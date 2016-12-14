package torrent;

import org.junit.*;
import org.junit.rules.TemporaryFolder;
import torrent.client.SimpleClient;
import torrent.common.storage.ServerFile;
import torrent.server.TrackerServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Mark on 13.12.2016.
 */
public class MainTest {
    @Rule
    public final TemporaryFolder tmpFolder = new TemporaryFolder();
    private final int MAX_CLIENTS = 5;
    private final SimpleClient clients[] = new SimpleClient[MAX_CLIENTS];

    @BeforeClass
    public static void setup() throws IOException {
        TrackerServer.runServer(false);
    }

    @AfterClass
    public static void teardown() throws IOException {
        TrackerServer.stopServer();
        TestUtils.clearStorages();

    }

    @Before
    public void initClients() throws IOException, ClassNotFoundException {
        for(int i = 0; i < MAX_CLIENTS; i++){
            clients[i] = SimpleClient.nextInstance();
            clients[i].start();
        }
    }

    @After
    public void tearDownClients() throws IOException, InterruptedException {
        for(int i = 0; i < MAX_CLIENTS; i++){
            clients[i].stop();
        }
    }

    @Test
    public void testOneUpload() throws IOException {
        File folder = tmpFolder.newFolder("upload");
        File testFile = new File(folder, "1");
        int fileSize = 12345;
        TestUtils.createFile(testFile, fileSize);
        int id = clients[0].clientClient.upload(testFile.getAbsolutePath());
        for(int i = 0; i < MAX_CLIENTS; i++){
            List<ServerFile> files = clients[i].clientClient.listFiles();
            assertEquals(files.size(), 1);
            assertEquals(files.get(0).name, testFile.getAbsolutePath());
            assertEquals(files.get(0).size, fileSize);
            assertEquals(files.get(0).id, id);
        }
    }

    @Test
    public void testMultipleUpload() throws IOException {
        File folder = tmpFolder.newFolder("list");
        File testFile = new File(folder, "1");
        int fileSize = 12345;
        TestUtils.createFile(testFile, fileSize);
        for(int i = 0; i < MAX_CLIENTS; i++){
            clients[i].clientClient.upload(testFile.getAbsolutePath());
        }
        List<List<ServerFile>> results = new ArrayList<>();
        for(int i = 0; i < MAX_CLIENTS; i++){
            results.add(clients[i].clientClient.listFiles());
        }
        for(int i = 1; i < MAX_CLIENTS; i++){
            assertEquals(results.get(i).size(), results.get(i-1).size());
            for(int j = 0; j < results.get(i).size(); j++){
                TestUtils.assertEqualMetainf(results.get(i-1).get(j), results.get(i).get(j));
            }
        }
    }

    @Test
    public void testDownload() throws IOException, InterruptedException {
        testDownload(50000001);
        testDownload(12345);
    }


    private void testDownload(int fileSize) throws IOException, InterruptedException {
        File folder = tmpFolder.newFolder("download" + fileSize);
        File testFileSmall = new File(folder, "1");
        File testFileSmallCopy = new File(folder, "2");
        TestUtils.createFile(testFileSmall, fileSize);
        int id = clients[0].clientClient.upload(testFileSmall.getAbsolutePath());
        Thread.sleep(clients[0].server.HEARTBEAT_INTERVAL);

        clients[1].clientClient.downloadFile(id, testFileSmallCopy.getAbsolutePath(), fileSize);
        while (clients[1].storage.getFile(id).isDownloaded()){
            Thread.sleep(500);
        }
        Thread.sleep(clients[0].server.HEARTBEAT_INTERVAL);
        for(int i = 2; i < MAX_CLIENTS; i++){
            List<InetSocketAddress> peers = clients[i].clientClient.getPeers(id);
            assertEquals(peers.size(), 2);
        }
        File copies[] = new File[MAX_CLIENTS];
        copies[0] = testFileSmall;
        copies[1] =  testFileSmallCopy;
        for(int i = 2; i < MAX_CLIENTS; i++){
            copies[i] = new File(folder, Integer.toString(i+1));
            clients[i].clientClient.downloadFile(id, copies[i].getAbsolutePath(), fileSize);

        }
        for(int i = 1; i < MAX_CLIENTS; i++){
            while (clients[i].storage.getFile(id).isDownloaded()){
                Thread.sleep(500);
            }
            TestUtils.assertEqualsFileHashes(copies[0], copies[i]);
        }
    }



}
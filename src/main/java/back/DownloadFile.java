package back;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DownloadFile {
    private List<String> mList;
    private int countThread;
    private String rootDir;
    private boolean pause = false;
    private AtomicLong downloadSize = new AtomicLong(0);
    private long fullSizeFiles;

    public DownloadFile(List<String> mList, int countThread, String rootDir) {
        this.mList = mList;
        this.countThread = countThread;
        this.rootDir = rootDir;
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList(
                "https://images.wallpaperscraft.ru/image/pirs_more_vecher_123104_3840x2400.jpg",
                "https://images.wallpaperscraft.ru/image/pirs_more_vecher_123104_3840x2400.jpg",
                "https://images.wallpaperscraft.ru/image/pirs_more_vecher_123104_3840x2400.jpg",
                "https://images.wallpaperscraft.ru/image/pirs_more_vecher_123104_3840x2400.jpg",
                "https://images.wallpaperscraft.ru/image/pirs_more_vecher_123104_3840x2400.jpg",
                "https://images.wallpaperscraft.ru/image/pirs_more_vecher_123104_3840x2400.jpg",
                "https://images.wallpaperscraft.ru/image/pirs_more_vecher_123104_3840x2400.jpg",
                "https://images.wallpaperscraft.ru/image/pirs_more_vecher_123104_3840x2400.jpg",
                "https://images.wallpaperscraft.ru/image/pirs_more_vecher_123104_3840x2400.jpg",
                "https://images.wallpaperscraft.ru/image/pirs_more_vecher_123104_3840x2400.jpg",
                "https://images.wallpaperscraft.ru/image/pirs_more_vecher_123104_3840x2400.jpg",
                "https://images.wallpaperscraft.ru/image/pirs_more_vecher_123104_3840x2400.jpg"
        );
        int count = 2;
        String path = "C:\\Users\\Ivan\\IdeaProjects\\downloadManager\\pictures\\";


        DownloadFile df = new DownloadFile(list, count, path);
        df.startDownload();

        while(df.getPercent() != 100.0) {
            System.out.println("percent download = " + df.getPercent() + " %");
        }
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void startDownload() {
        Thread fullSizeThread = new Thread(() -> {
            try {
                fullSizeFiles = getFullDownloadSize(mList);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
        fullSizeThread.start();
        File file = new File(rootDir);
        file.mkdirs();
        String fileName = "file";
        AtomicInteger i = new AtomicInteger();
        ExecutorService execut = Executors.newFixedThreadPool(countThread);
        mList.parallelStream().forEach(s -> execut.submit(() -> {
            try {
                downloadFile(fileName + i.addAndGet(1), s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        execut.shutdown();

    }

    private void downloadFile(final String filename, final String url) throws IOException {
        System.out.println("Start download thread: \n" + Thread.currentThread().getName() + " \n" +
                url +
                " \n in path " + filename);

        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream());
            fout = new FileOutputStream(rootDir + File.separator + filename);

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                if (pause) {
                    while (pause) {
                        System.out.println("sleep is up");
                        Thread.currentThread().sleep(10);
                    }
                }
                fout.write(data, 0, count);
                downloadSize.getAndAdd(count);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
                System.out.println("input stream close");
            }
            if (fout != null) {
                fout.close();
                System.out.println("fileOutput stream close");
            }
        }
    }


    private static long getFileSize(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

    private static long getFullDownloadSize(List<String> urlLis) throws MalformedURLException {
        long fullSize = 0;
        for (String url : urlLis) {
            fullSize += getFileSize(new URL(url));
        }
        return fullSize;
    }

    public double getPercent(){
        if(fullSizeFiles !=0 && downloadSize != null) {
            return (downloadSize.get() * 100) / fullSizeFiles;
        }
        return 0;
    }
}

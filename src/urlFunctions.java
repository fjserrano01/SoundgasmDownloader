import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class urlFunctions {
    public static void openUrl(String asmrLink){
        if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
            try{
                Desktop.getDesktop().browse(new URI(asmrLink));
            }catch (URISyntaxException | IOException e){
                System.out.println("Didnt open website " + asmrLink);
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean activeURL(String url){
        try {
            URI link = new URI(url);
            HttpURLConnection huc = (HttpURLConnection) link.toURL().openConnection();
            huc.setRequestMethod("HEAD");
            int responseCode = huc.getResponseCode();
            return responseCode != HttpURLConnection.HTTP_NOT_FOUND;
        }catch (IOException | URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, String> bookmarkList(){
        HashMap<String, String> bookmarks = new HashMap<>();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("./dist/BookmarkReader.exe");
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String ln;
            while((ln = reader.readLine()) != null){
                if(ln.contains("soundgasm.net") && activeURL(ln)){
                    String [] audioName = ln.split("/");
                    String name = audioName[audioName.length -1];
                    if(bookmarks.containsKey(name)){
                        continue;
                    }
                    bookmarks.put(name, ln);
                }
            }

            process.destroy();
            reader.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return bookmarks;
    }

    //Get link of m4a file from site html
    public static String getUrlLink(String audioLink){
        String download = "";
        try {
            URI link = new URI(audioLink);
            InputStreamReader reader = new InputStreamReader(link.toURL().openStream());
            BufferedReader in = new BufferedReader(reader);

            String inputLine;
            while((inputLine = in.readLine()) != null){
                if(inputLine.contains("m4a:")){
                    download = inputLine.split("\"")[1];
                }
            }
            in.close();

        }catch (IOException | URISyntaxException e){
            throw new RuntimeException(e);
        }
        return download;
    }

    //Download m4a file
    public static void downloadAudio(String url, String fileName, String filePath){

        try (BufferedInputStream in = new BufferedInputStream(new URI(url).toURL().openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filePath+fileName+".m4a")
        ){
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while((bytesRead = in.read(dataBuffer, 0, 1024)) != -1){
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }

        } catch (IOException | URISyntaxException e){
            System.out.println("file not downloaded" + fileName);
            throw new RuntimeException(e);
        }
    }
}

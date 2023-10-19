import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HashMap<String, String> asmrAudios;

//        Scanner reader = new Scanner(System.in);
//        System.out.println("Individual link or all bookmarks: ");
//        String ans = reader.nextLine();

        //Gets list of soundgasm links from Bookmarks
        asmrAudios = bookmarkList();

//       ToDo Print if link is dead: openUrl(asmrAudios);
        //Get download file path
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter file output location:");
        String filePath = reader.nextLine();//C:/Users/Elizabeth Serrano/Documents/audios/

        //Parse html to get m4a link and then download with link
        System.out.println("Audio link list:");
        for(Map.Entry<String, String> bookmark : asmrAudios.entrySet()){
            String link = getUrlLink(bookmark.getValue());
            downloadAudio(link, bookmark.getKey(), filePath);
        }

    }


    public static void openUrl(String asmrLink){
        if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
            try{
                System.out.println(asmrLink);

                Desktop.getDesktop().browse(new URI(asmrLink));
            }catch (URISyntaxException | IOException e){
                System.out.println("Didnt open website " + asmrLink);
                throw new RuntimeException(e);
            }
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
                if(ln.contains("soundgasm.net")){
                    String [] audioName = ln.split("/");
                    String name = audioName[audioName.length -1];
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
            BufferedReader in = new BufferedReader( new InputStreamReader(link.toURL().openStream()));

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
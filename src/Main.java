// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.awt.Desktop;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> asmrAudios = new ArrayList<>();
        //ToDo Add bookmark reader
        Scanner reader = new Scanner(System.in);
        System.out.println("Audio test file: ");
        String testFile = reader.nextLine();
        asmrAudios.add(testFile);

//        openUrl(asmrAudios);

        //ToDo Print if link is dead

        //Parse html to get m4a link
        String url = getUrlLink(asmrAudios.get(0));
        System.out.println(url);

        //Download m4a file
        String [] audioName = asmrAudios.get(0).split("/");
        String fileName = audioName[audioName.length-1];
        System.out.println(fileName);
        downloadAudio(url, fileName);

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
    public static void downloadAudio(String url, String fileName){
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter file output location:");
        String filePath = reader.nextLine();//C:/Users/Elizabeth Serrano/Documents/audios/
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filePath+fileName+".m4a")
        ){
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while((bytesRead = in.read(dataBuffer, 0, 1024)) != -1){
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e){
            System.out.println("file not downloaded" + fileName);
            throw new RuntimeException(e);
        }
    }
}
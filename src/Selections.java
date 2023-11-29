import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Selections {
    public static void displayBookmarks(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Print bookmark list? y or n");
        String input = reader.nextLine();

        if(input.equalsIgnoreCase("y")){
            HashMap<String, String> asmrAudios = urlFunctions.bookmarkList();
            System.out.println(asmrAudios.keySet());
        }
    }

    public static void selectDownloadOption(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Download options: \n(1) Download all\n(2) Download one\n(3) quit");
        String input = reader.nextLine();
        switch (input){
            case "1": {
                HashMap<String, String> asmrAudios = urlFunctions.bookmarkList();

                //Get download file path
                System.out.println("Enter file output location:");
                String filePath = reader.nextLine();//C:/Users/Elizabeth Serrano/Documents/audios/

                //Parse html to get m4a link and then download with link
                System.out.println("Audio link list:");
                for(Map.Entry<String, String> bookmark : asmrAudios.entrySet()){
                    System.out.println(bookmark.getValue());
                    String link = urlFunctions.getUrlLink(bookmark.getValue());
                    urlFunctions.downloadAudio(link, bookmark.getKey(), filePath);
                }
            }
            case "2": {
                System.out.println("Enter bookmark to download: ");
                String link = reader.nextLine();
                System.out.println("Enter file output location:");
                String filepath = reader.nextLine();

                urlFunctions.downloadSingleBookmark(link, filepath);

            }
            case "3" : {
                break;
            }

        }
    }
}

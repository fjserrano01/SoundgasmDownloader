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
        asmrAudios = urlFunctions.bookmarkList();

        //Get download file path
        Scanner reader = new Scanner(System.in);
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
}
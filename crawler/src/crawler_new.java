import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.net.URL;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class crawler {
    static String[] SENTENCE;
    public static void main(String args[]) throws IOException {
        int counter = 1;
        // open file
        File urls = new File(args[0]);
        if (urls.exists()) {
            // read file line by line , put lines into PriorityQueue
            Scanner reader = new Scanner(urls);
            Queue<String> urlToVisit = new ArrayDeque<>();
            while (reader.hasNext()) {
                urlToVisit.add(reader.nextLine());
            }
            // connect url and parse doc into text and new urls.
            while (!urlToVisit.isEmpty()) {
                final String url_s = urlToVisit.poll();
                System.out.println(url_s);
                URL url_u = new URL(url_s);
                //String path = url_u.getFile().substring(0, url_u.getFile().lastIndexOf('/'));
                String base = url_u.getProtocol() + "://" + url_u.getHost();

                String html = Jsoup.connect(url_s)
                        .header("connection", "Keep-Alive")
                        .get().html();
                Document doc = Jsoup.parse(html);
                Elements links = doc.getElementsByTag("a");
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    if (!linkHref.startsWith("http")) {
                        linkHref = base + linkHref;
                    }
                    urlToVisit.add(linkHref);
                }
                System.out.println(counter + doc.text());
                ArrayList<String> sentences_array=new ArrayList<String>();
                String article =doc.text();
                article = article.replaceAll("\\[.*?\\]","");
                article = article.replaceAll("\\(.*?\\) ?", "");
                SENTENCE = article.split("(?<=[.!?^;])\\s*");
                for (int i=0;i<SENTENCE.length;i++)
                {
                    SENTENCE[i] = SENTENCE[i].replaceAll("[^a-zA-Z0-9,.\\s ]","");
                    if(SENTENCE[i].length()>50)
                        sentences_array.add(SENTENCE[i]);
                }
                for(int i=0;i<sentences_array.size();i++){
                    System.out.println("Sentence " + (i+1) +" " + sentences_array.get(i).length()+": " + sentences_array.get(i));
                }
                counter++;
            }
        } else {
            System.out.println("The file doesn't exist.");
        }
    }
}


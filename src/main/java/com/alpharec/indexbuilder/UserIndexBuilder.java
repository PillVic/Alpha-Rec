package com.alpharec.indexbuilder;

import com.alpharec.JavaConfig;
import com.alpharec.data.Resource;
import com.alpharec.item.MovieItem;
import com.alpharec.item.UserItem;
import com.alpharec.pojo.Rating;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

import static com.alpharec.indexbuilder.MovieIndexBuilder.buildMovieItem;
import static com.alpharec.util.Flag.UserField.*;
import static com.alpharec.util.ObjectAnalyzer.*;

/** 建立和user相关的倒排索引
 * @author pillvic
* */
public class UserIndexBuilder implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(UserIndexBuilder.class);
    public static final String USER_INDEX_PATH = "Data/Index/UserIndex";
    public static final int TOP_NUM = 100;

    @Override
    public void run() {
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        Resource r = context.getBean("resource", Resource.class);

        try {
            IndexWriter writer = r.getIndexWriter(USER_INDEX_PATH);
            List<Document> documents = new ArrayList<>();
            int maxUserId = r.dbReader.getMaxUserId();
            int minUserId = r.dbReader.getMinUserId();
            for (int i = minUserId; i <= maxUserId; i++) {
                List<Rating> ratingList = r.dbReader.getMovieRatingsByUserId(minUserId);
                UserItem ui = userItemBuilder(i, ratingList);

                if (ui != null) {
                    Document doc = getDoc(ui);
                    logger.info("[INFO]:id:{}, doc:{}", i, doc);
                    documents.add(doc);
                }
            }
            writer.addDocuments(documents);
        } catch (Exception e) {
            logger.error("User Index build", e);
        }
    }

    public UserItem userItemBuilder(int userId, List<Rating> ratingList) {
        UserItem userItem = new UserItem(userId);
        if (isNullOrEmptyList(ratingList)) {
            return null;
        }
        Map<String, Double> preferTagMap = new HashMap<>(ratingList.size());
        Map<String, Double> preferGenresMap = new HashMap<>(ratingList.size());

        for (var rating : ratingList) {
            int movieId = rating.getMovieId();
            MovieItem movieItem = buildMovieItem(movieId);
            List<String> genres = movieItem.getGenreList();
            if (!isNullOrEmptyList(genres)) {
                genres.forEach(t -> {
                    if (!preferGenresMap.containsKey(t)) {
                        preferGenresMap.put(t, 0.0);
                    }
                    double totalRate = preferGenresMap.get(t);
                    preferGenresMap.put(t, totalRate + rating.getRating());
                });
            }
            Set<String> tags = movieItem.getTags();
            if (!isNullOrEmptySet(tags)) {
                tags.forEach(t -> {
                    if (!preferTagMap.containsKey(t)) {
                        preferTagMap.put(t, 0.0);
                    }
                    double totalRate = preferTagMap.get(t);
                    preferTagMap.put(t, totalRate + rating.getRating());
                });
            }
        }
        userItem.setPreferTag(getTopN(TOP_NUM, preferTagMap.keySet().stream().toList(),
                Comparator.comparingDouble((preferTagMap::get))
        ));
        userItem.setPreferGenres(getTopN(TOP_NUM, preferGenresMap.keySet().stream().toList(),
                Comparator.comparingDouble(preferGenresMap::get)));
        return userItem;
    }

    public Document getDoc(UserItem userItem) {
        Document doc = new Document();
        doc.add(new IntPoint(USER_ID + "", userItem.userId));
        if (!isNullOrEmptyList(userItem.preferGenres)) {
            userItem.preferGenres.forEach(
                    t -> doc.add(new TextField(PREFER_GENRES + "", t, Field.Store.YES)));
        }
        if (!isNullOrEmptyList(userItem.preferTags)) {
            userItem.preferTags.forEach(
                    t -> doc.add(new TextField(PREFER_TAGS + "", t, Field.Store.YES)));

        }
        logger.info("[INFO]:userId:{}, doc:{}", userItem.userId, doc);
        return doc;
    }

    public static void main(String[] args) {
        UserIndexBuilder userIndexBuilder = new UserIndexBuilder();
        userIndexBuilder.run();
    }
}

package io.github.biezhi.geekbb.service;

import io.github.biezhi.geekbb.enums.FavoriteType;
import io.github.biezhi.geekbb.enums.RelateType;
import io.github.biezhi.geekbb.model.db.Relation;
import io.github.biezhi.geekbb.utils.ArrayUtils;
import io.github.biezhi.geekbb.utils.GeekDevUtils;
import com.blade.ioc.annotation.Bean;
import lombok.extern.slf4j.Slf4j;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.biezhi.geekbb.constants.GeekDevConst.*;
import static io.github.biezhi.anima.Anima.delete;
import static io.github.biezhi.anima.Anima.select;

/**
 * 关系服务
 * <p>
 * 粉丝、关注、收藏、点赞
 *
 * @author biezhi
 * @date 2017/8/2
 */
@Bean
@Slf4j
public class RelationService {

    private DB db = DBMaker.fileDB(RELATE_DB_NAME).fileMmapEnable().make();

    public RelationService() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            db.close();
            log.info("closed mapdb");
        }));
    }

    /**
     * 关注一个用户
     *
     * @param uid    被关注的用户
     * @param fansId 粉丝, 我
     */
    public void follow(Long uid, Long fansId) {
        // uid的粉丝+1
        Map<Long, long[]> map = this.getMap(RelateType.FOLLOW);

        this.mapAdd(map, uid, fansId);

        // fans follow uid
        long count = select().from(Relation.class).where(Relation::getRelateType, RelateType.FOLLOW.name())
                .and(Relation::getUid, fansId).and(Relation::getEventId, uid.toString()).count();
        if (count == 0) {
            Relation relation = new Relation();
            relation.setUid(fansId);
            relation.setEventId(uid.toString());
            relation.setRelateType(RelateType.FOLLOW.name());
            relation.setCreated(LocalDateTime.now());
            relation.save();
        }

        // fansId的关注列表+1
        Map<Long, long[]> mapFollowing = db.treeMap(RELATE_FOLLOWING)
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();

        this.mapAdd(mapFollowing, fansId, uid);
    }

    /**
     * 取消关注一个用户
     *
     * @param uid    关注的用户uid
     * @param fansId 粉丝, 我
     */
    public void unfollow(Long uid, Long fansId) {
        // uid的粉丝-1
        Map<Long, long[]> map = this.getMap(RelateType.FOLLOW);

        this.mapRemove(map, uid, fansId);

        delete().from(Relation.class).where(Relation::getRelateType, RelateType.FOLLOW.name())
                .and(Relation::getUid, fansId).and(Relation::getEventId, uid.toString()).execute();

        // fansId的关注列表-1
        Map<Long, long[]> mapFollowing = db.treeMap(RELATE_FOLLOWING)
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();

        this.mapRemove(map, fansId, uid);

    }

    /**
     * 收藏一个帖子
     * <p>
     * KEY: uid
     * VALUE: tid list
     *
     * @param uid
     * @param tid
     */
    public void collectTopic(Long uid, String tid) {
        Map<Long, long[]> map = this.getMap(RelateType.COLLECT);

        Long tidLong = GeekDevUtils.decodeTid(tid);
        this.mapAdd(map, uid, tidLong);

        long count = select().from(Relation.class).where(Relation::getRelateType, RelateType.COLLECT.name())
                .and(Relation::getUid, uid).and(Relation::getEventId, tid).count();
        if (count == 0) {
            Relation relation = new Relation();
            relation.setUid(uid);
            relation.setEventId(tid);
            relation.setRelateType(RelateType.COLLECT.name());
            relation.setCreated(LocalDateTime.now());
            relation.save();
        }
    }

    /**
     * 取消收藏帖子
     *
     * @param uid
     * @param tid
     */
    public void unCollectTopic(Long uid, String tid) {
        Map<Long, long[]> map = this.getMap(RelateType.COLLECT);

        long tidLong = GeekDevUtils.decodeTid(tid);

        this.mapRemove(map, uid, tidLong);

        delete().from(Relation.class).where(Relation::getRelateType, RelateType.COLLECT.name())
                .and(Relation::getUid, uid).and(Relation::getEventId, tid).execute();
    }

    /**
     * 给帖子点赞
     * <p>
     * KEY: uid
     * VALUE: tid list
     *
     * @param uid
     * @param tid
     */
    public void loveTopic(Long uid, String tid) {
        Map<Long, long[]> map = this.getMap(RelateType.LOVE);

        Long tidLong = GeekDevUtils.decodeTid(tid);

            this.mapAdd(map, uid, tidLong);

        long count = select().from(Relation.class).where(Relation::getRelateType, RelateType.LOVE.name())
                .and(Relation::getUid, uid).and(Relation::getEventId, tid).count();
        if (count == 0) {
            Relation relation = new Relation();
            relation.setUid(uid);
            relation.setEventId(tid);
            relation.setRelateType(RelateType.LOVE.name());
            relation.setCreated(LocalDateTime.now());
            relation.save();
        }
    }

    /**
     * 取消帖子点赞
     *
     * @param uid
     * @param tid
     */
    public void unLoveTopic(Long uid, String tid) {
        Map<Long, long[]> map     = this.getMap(RelateType.LOVE);
        long              tidLong = GeekDevUtils.decodeTid(tid);
        this.mapRemove(map, uid, tidLong);

        delete().from(Relation.class).where(Relation::getRelateType, RelateType.LOVE.name())
                .and(Relation::getUid, uid).and(Relation::getEventId, tid).execute();
    }

    private List<Long> getIds(long[] follwers) {
        if (null == follwers || follwers.length == 0) {
            return null;
        }
        List<Long> followers = new ArrayList<>(follwers.length);
        for (long follwer : follwers) {
            followers.add(follwer);
        }
        return followers;
    }

    private void mapAdd(Map<Long, long[]> map, Long key, Long value) {
        if (map.containsKey(key)) {
            long count = map.values().stream().flatMapToLong(Arrays::stream).filter(value::equals).count();
            if (count == 0) {
                map.put(key, ArrayUtils.append(Objects.requireNonNull(map.get(key)), value));
            }
        } else {
            map.put(key, new long[]{value});
        }
    }

    private void mapRemove(Map<Long, long[]> map, Long key, Long value) {
        if (map.containsKey(key)) {
            long count = map.values().stream().flatMapToLong(Arrays::stream).filter(value::equals).count();
            if (count == 1) {
                map.put(key, ArrayUtils.remove(Objects.requireNonNull(map.get(key)), value));
            }
        } else {
            map.put(key, new long[]{value});
        }
    }

    public boolean isFavorite(String tid, Long uid, FavoriteType favoriteType) {
        Long tidLong = GeekDevUtils.decodeTid(tid);
        if (favoriteType.equals(FavoriteType.LOVE)) {
            Map<Long, long[]> map = this.getMap(RelateType.LOVE);
            if (!map.containsKey(uid)) {
                return false;
            }
            return Arrays.stream(Objects.requireNonNull(map.get(uid))).anyMatch(tidLong::equals);
        } else if (favoriteType.equals(FavoriteType.COLLECT)) {

            Map<Long, long[]> map = this.getMap(RelateType.COLLECT);
            if (!map.containsKey(uid)) {
                return false;
            }
            return Arrays.stream(Objects.requireNonNull(map.get(uid))).anyMatch(tidLong::equals);
        }
        return true;
    }

    private void cleanDB(RelateType relateType) {
        if (RelateType.LOVE.equals(relateType)) {
            this.getMap(RelateType.LOVE).clear();
        }
        if (RelateType.COLLECT.equals(relateType)) {
            this.getMap(RelateType.COLLECT).clear();
        }
        if (RelateType.FOLLOW.equals(relateType)) {
            this.getMap(RelateType.FOLLOW).clear();

            Map<Long, long[]> mapFollowing = db.treeMap(RELATE_FOLLOWING)
                    .keySerializer(Serializer.LONG)
                    .valueSerializer(Serializer.LONG_ARRAY)
                    .counterEnable()
                    .createOrOpen();
            mapFollowing.clear();
        }
    }

    private Map<Long, long[]> getMap(RelateType relateType) {
        String dbName = "";
        switch (relateType) {
            case LOVE:
                dbName = RELATE_TOPIC_LOVES;
                break;
            case COLLECT:
                dbName = RELATE_TOPIC_COLLECTS;
                break;
            case FOLLOW:
                dbName = RELATE_FOLLOWER;
                break;
            default:
                break;
        }
        Map<Long, long[]> map = db.treeMap(dbName)
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();

        return map;
    }

    public void sync(String to) {
        if ("db".equals(to)) {

            delete().from(Relation.class).execute();

            Map<Long, long[]> lovesMap = this.getMap(RelateType.LOVE);

            Set<Long> uidList = lovesMap.keySet();
            for (Long uid : uidList) {
                long[] tids = lovesMap.get(uid);
                for (long tid : tids) {
                    String   tidString = GeekDevUtils.genTid(tid);
                    Relation relation  = new Relation();
                    relation.setRelateType(RelateType.LOVE.name());
                    relation.setUid(uid);
                    relation.setEventId(tidString);
                    relation.setCreated(LocalDateTime.now());
                    relation.save();
                }
            }

            Map<Long, long[]> collectMap = this.getMap(RelateType.COLLECT);

            uidList = collectMap.keySet();
            for (Long uid : uidList) {
                long[] tidList = collectMap.get(uid);
                for (long tid : tidList) {
                    String   tidString = GeekDevUtils.genTid(tid);
                    Relation relation  = new Relation();
                    relation.setRelateType(RelateType.COLLECT.name());
                    relation.setUid(uid);
                    relation.setEventId(tidString);
                    relation.setCreated(LocalDateTime.now());
                    relation.save();
                }
            }

            // uid的粉丝+1
            Map<Long, long[]> followMap = this.getMap(RelateType.FOLLOW);

            uidList = followMap.keySet();
            for (Long uid : uidList) {
                long[] fansIds = followMap.get(uid);
                for (long fansId : fansIds) {
                    Relation relation = new Relation();
                    relation.setRelateType(RelateType.FOLLOW.name());
                    relation.setUid(uid);
                    relation.setEventId(fansId + "");
                    relation.setCreated(LocalDateTime.now());
                    relation.save();
                }
            }
        } else {
            List<Relation> relations = select().from(Relation.class).all();
            List<Relation> loves     = relations.stream().filter(r -> RelateType.LOVE.name().equals(r.getRelateType())).collect(Collectors.toList());
            List<Relation> collects  = relations.stream().filter(r -> RelateType.COLLECT.name().equals(r.getRelateType())).collect(Collectors.toList());
            List<Relation> follows   = relations.stream().filter(r -> RelateType.FOLLOW.name().equals(r.getRelateType())).collect(Collectors.toList());
            this.cleanDB(RelateType.LOVE);
            this.cleanDB(RelateType.COLLECT);
            this.cleanDB(RelateType.FOLLOW);

            for (Relation love : loves) {
                this.loveTopic(love.getUid(), love.getEventId());
            }
            for (Relation collect : collects) {
                this.collectTopic(collect.getUid(), collect.getEventId());
            }
            for (Relation follow : follows) {
                this.follow(follow.getUid(), Long.valueOf(follow.getEventId()));
            }
        }
    }
}
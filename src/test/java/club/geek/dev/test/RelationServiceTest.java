package club.geek.dev.test;

import club.geek.dev.enums.FavoriteType;
import club.geek.dev.service.RelationService;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author biezhi
 * @date 2018/4/8
 */
public class RelationServiceTest {

    private RelationService relationService = new RelationService();

    @Test
    public void testLoveTopic() {
        relationService.loveTopic(1001L, "hello");
        boolean isLove = relationService.isFavorite("hello", 1001L, FavoriteType.LOVE);
        Assert.assertEquals(true, isLove);
    }

}

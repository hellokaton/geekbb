package club.geek.dev.validator;

import club.geek.dev.enums.ErrorCode;
import club.geek.dev.model.db.Topic;
import com.blade.validator.SimpleValidation;
import com.blade.validator.Validators;

import static club.geek.dev.utils.GeekDevUtils.cleanHtml;
import static com.blade.validator.Validators.length;

/**
 * GeekDev Validator
 *
 * @author biezhi
 * @date 2018/4/22
 */
public class GeekDevValidator {

    public static void saveTopic(Topic topic) {
        Validators.notNull(ErrorCode.PARAMETER_IS_MISS.getMsg())
                .test(topic).throwIfInvalid("topic");

        Validators.notEmpty(ErrorCode.PARAMETER_IS_MISS.getMsg()).and(length(5, 50))
                .test(topic.getTitle()).throwIfInvalid("标题");

        Validators.notEmpty(ErrorCode.PARAMETER_IS_MISS.getMsg()).and(length(5, 50_000))
                .test(topic.getContent()).throwIfInvalid("内容");

        SimpleValidation.from(o -> cleanHtml(o.toString()).trim().length() == 0, ErrorCode.XSS_TIP.getMsg())
                .test(topic.getTitle()).throwIfInvalid();

        SimpleValidation.from(o -> cleanHtml(o.toString()).trim().length() == 0, ErrorCode.XSS_TIP.getMsg())
                .test(topic.getContent()).throwIfInvalid();

    }

    public static void editTopic(Topic topic) {
        saveTopic(topic);

        Validators.notNull(ErrorCode.PARAMETER_IS_MISS.getMsg())
                .test(topic.getTid()).throwIfInvalid("帖子ID");
    }

}

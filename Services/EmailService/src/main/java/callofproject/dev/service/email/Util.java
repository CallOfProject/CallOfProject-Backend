package callofproject.dev.service.email;

import java.util.Random;
import java.util.stream.IntStream;

public final class Util
{
    private Util()
    {

    }

    private static final int CODE_LENGTH = 6;
    public static final String TITLE_FORMAT = "%s - [Call-Of-Project]";
    public static String generateVerificationCode(Random random)
    {
        return IntStream.rangeClosed(1, CODE_LENGTH).map(i -> random.nextInt(1, 10))
                .mapToObj(String::valueOf)
                .reduce("", String::concat);
    }
}

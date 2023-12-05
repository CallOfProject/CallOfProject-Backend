package callofproject.dev.data.common.util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;

public final class VerificationCodeGenerator
{
    private static final Random m_random = new SecureRandom();

    private VerificationCodeGenerator()
    {

    }

    public static String generateVerificationCode(int length)
    {
        return IntStream.rangeClosed(1, length).map(i -> m_random.nextInt(1, 10))
                .mapToObj(String::valueOf)
                .reduce("", String::concat);
    }
}

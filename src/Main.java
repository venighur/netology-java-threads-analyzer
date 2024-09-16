import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static final int BLOCKING_QUEUE_CAPACITY = 100;
    public static final int TEXT_LENGTH = 100_000;
    public static final int TEXTS_COUNT = 10_000;

    public static BlockingQueue<String> textsA = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
    public static BlockingQueue<String> textsB = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
    public static BlockingQueue<String> textsC = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);

    public static int maxCountA = 0;
    public static int maxCountB = 0;
    public static int maxCountC = 0;

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < TEXTS_COUNT; i++) {
                try {
                    String text = generateText("abc", TEXT_LENGTH);
                    textsA.put(text);
                    textsB.put(text);
                    textsC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < TEXTS_COUNT; i++) {
                try {
                    String text = textsA.take();
                    int curCount = getCharCount(text, 'a');
                    if (curCount > maxCountA) {
                        maxCountA = curCount;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимально количество символов 'a': " + maxCountA);
        }).start();

        new Thread(() -> {
            for (int i = 0; i < TEXTS_COUNT; i++) {
                try {
                    String text = textsB.take();
                    int curCount = getCharCount(text, 'b');
                    if (curCount > maxCountB) {
                        maxCountB = curCount;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимально количество символов 'b': " + maxCountB);
        }).start();

        new Thread(() -> {
            for (int i = 0; i < TEXTS_COUNT; i++) {
                try {
                    String text = textsC.take();
                    int curCount = getCharCount(text, 'c');
                    if (curCount > maxCountC) {
                        maxCountC = curCount;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимально количество символов 'c': " + maxCountC);
        }).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int getCharCount(String text, char ch) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ch) count++;
        }
        return count;
    }
}
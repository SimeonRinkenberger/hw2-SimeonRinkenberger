import org.junit.*;     // JUnit tools

import java.util.*;     // Collections
import java.io.*;       // File access

/**
 * @Author Simeon Rinkenberger
 * Date: 3-18-22
 *
 * Created to test the methods in HangmanManager.java
 */
public class HangmanManagerTest {

    /* Loads the words in fileName and returns the set of all words in that file*/
    private Set<String> getDictionary(String fileName) {
        try {
            Scanner fileScanner = new Scanner(new File(fileName));
            Set<String> dictionary = new HashSet<>();
            while(fileScanner.hasNext()) {
                dictionary.add(fileScanner.next());
            }
            return dictionary;
        } catch(FileNotFoundException e) {
            Assert.fail("Something went wrong.");      //Something went wrong
        }
        /* Should never be reached. */
        return new HashSet<>();
    }

    /**
     * Constructor tests
     */
    // tests that constructors can be created successfully
    @Test
    public void constructorSuccessTest() {
        Set<String> d = getDictionary("dictionary.txt");
        try {
            new HangmanManager(d, 5, 10);
            new HangmanManager(d, 1, 10);
            new HangmanManager(d, 5, 0);
        } catch (RuntimeException e) {
            Assert.fail("The hangman manager constructor could not be run correctly");
        }
    }
    // tests that constructors throw exceptions when passed illegal values
    @Test
    public void constructorFailTest() {
        Set<String> d = getDictionary("dictionary.txt");
        try {
            new HangmanManager(d, 0, 10);
            new HangmanManager(d, 1, -1);
            new HangmanManager(null, 5, 0);
            Assert.fail("The hangman manager constructor did not throw any exceptions");
        } catch (RuntimeException e) {
            // should not get here
        }
    }

    /**
     * words() tests
     */
    // checks that the value returned is truly a copy
    @Test
    public void wordsCopyTest() {
        Set<String> d = getDictionary("dictionary.txt");
        HangmanManager h = new HangmanManager(d, 5, 10);
        Assert.assertNotSame("dictionaries must be the same", h.words(), d);
        d.add("test");
        Assert.assertNotSame("dictionaries must be the same", h.words(), d);
    }

    /**
     * guessesLeft() tests
     */
    // checks that the guesses remaining updates correctly
    @Test
    public void guessesLeftTest() {
        Set<String> d = getDictionary("dictionary.txt");
        HangmanManager h = new HangmanManager(d, 5, 10);
        Assert.assertEquals("Guesses should not have changed yet", h.guessesLeft(), 10);
        h.record('a');
        h.record('b');
        Assert.assertEquals("Guesses should be lower", h.guessesLeft(), 8);
    }

    /**
     * guesses() tests
     */
    // checks that the guesses returned are properly updated
    @Test
    public void guessesTest() {
        Set<String> d = getDictionary("dictionary.txt");
        HangmanManager h = new HangmanManager(d, 5, 10);
        h.record('a');
        h.record('b');
        h.record('c');
        Set<Character> set1 = new TreeSet<>();
        set1.add('a');
        set1.add('b');
        set1.add('c');
        Set<Character> set2 = h.guesses();
        Assert.assertEquals("guesses was not updated", set1, set2);
    }

    /**
     * pattern() tests
     */
    // checks that the pattern gets created and updated correctly
    @Test
    public void patternTest() {
        Set<String> d = getDictionary("dictionary.txt");
        HangmanManager h = new HangmanManager(d, 5, 10);
        Assert.assertEquals("pattern should be blank", h.pattern(), "- - - - -");
        h.record('a');
        Assert.assertEquals("pattern should not change with a wrong guess", h.pattern(), "- - - - -");
        h.record('e');
        h.record('i');
        h.record('o');
        h.record('u');
        Assert.assertEquals("pattern should change with a correct guess", h.pattern(), "- u - - -");
    }

    /**
     * record() tests
     */
    // checks if an exception is thrown if a letter is guessed twice or there are no guesses left
    @Test
    public void recordExceptionTest() {
        Set<String> d = getDictionary("dictionary.txt");
        HangmanManager h = new HangmanManager(d, 5, 2);
        h.record('a');
        try {
            h.record('a');
            Assert.fail("a was already guessed");
        } catch (RuntimeException e) {
            // should get here
        }
        h.record('b');
        try {
            h.record('c');
            Assert.fail("should not allow another guess");
        } catch (RuntimeException e) {
            // should get here
        }
    }

    // checks if the dictionary get properly updated
    @Test
    public void recordDictionaryTest() {
        Set<String> d = getDictionary("dictionary2.txt");
        HangmanManager h = new HangmanManager(d, 4, 10);
        Set<String> s = getDictionary("dictionary2.txt");
        s.remove("ally");
        h.record('y');
        Assert.assertEquals("considered words should match", s, h.words());
        s.remove("good");
        h.record('g');
        Assert.assertEquals("considered words should match", s, h.words());
    }

    // checks if the number of occurrences gets updated correctly
    @Test
    public void recordNumOccurrencesTest() {
        Set<String> d = getDictionary("dictionary2.txt");
        HangmanManager h = new HangmanManager(d, 4, 10);
        h.record('a');
        h.record('o');
        int num = h.record('e');
        Assert.assertEquals("there should be one e", 1, num);
        h.record('b');
        num = h.record('l');
        Assert.assertEquals("there should be one e", 1, num);
    }
}

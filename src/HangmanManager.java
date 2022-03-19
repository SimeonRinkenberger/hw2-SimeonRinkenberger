import java.util.*;

/**
 * @Author Simeon Rinkenberger
 * Created 3-18-22
 *
 * Contains the necessary methods to run HangmanMain.java
 */
public class HangmanManager {
    private Set<String> consideredWords;
    private int guessesLeft;
    private SortedSet<Character> guessed;
    String pattern;

    // Constructor that accepts a dictionary of words, length of words to use, and max number of guess
    // Initializes the words to be considered, number of guesses left, words guessed, and the word pattern
    public HangmanManager(Collection<String> dictionary, int length, int max) {
        if (length < 1) throw new IllegalArgumentException("length must be greater than 1");
        else if (max < 0) throw new IllegalArgumentException("max guesses must be greater than 0");

        consideredWords = new TreeSet<>();
        for(String word:dictionary) {
            if (word.length() == length) {
                consideredWords.add(word);
            }
        }

        guessesLeft = max;
        guessed = new TreeSet<>();

        pattern = "-";
        for (int i = 1; i < length; i++) {
            pattern += " -";
        }
    }

    // returns the current set of words being considered by the hangman manager
    public Set<String> words() {
        return new TreeSet<>(consideredWords);
    }

    // returns the number of guess the player has left
    public int guessesLeft() {
        return guessesLeft;
    }

    // returns the current set of letters that have been guessed by the user
    public SortedSet<Character> guesses() {
        return new TreeSet<Character>(guessed);
    }

    // returns the current pattern to be displayed based on guesses made
    public String pattern() {
        if (consideredWords.isEmpty()) throw new IllegalArgumentException("words being considered must not be empty");
        return pattern;
    }

    // decides what words to use based on the guess and returns the number of occurrences of the guessed letter
    int record(char guess) {
        if (guessesLeft < 1) throw new IllegalStateException("must have more than one guess left");
        if (consideredWords.isEmpty()) throw new IllegalStateException("set of words being considered cannot be empty");
        if (guessed.contains(guess)) throw new IllegalArgumentException("must be a new guess");

        guessed.add(guess);

        Map<String, Set<String>> answerMap = makeMap(guess);

        String biggestSetKey = findBiggestSetKey(answerMap);
        consideredWords = answerMap.get(biggestSetKey);
        pattern = biggestSetKey;

        int occurrences = findNumOccurrences(guess);
        if (occurrences == 0) guessesLeft --;

        return occurrences;
    }

    // a helper method for record to make the map of words to patterns
    // groups all words being considered to matching patterns
    private Map<String, Set<String>> makeMap(char guess) {
        Map<String, Set<String>> map = new TreeMap<>();
        for (String word:consideredWords) {
            String key = makeKey(word, guess);
            Set<String> value = new TreeSet<>();
            if (map.containsKey(key)) {
                value = map.get(key);
            }
            value.add(word);
            map.put(key, value);
        }
        return map;
    }

    // a helper method for makeMap that creates a key for a word by replacing the - with the guess if necessary
    // returns the key created
    private String makeKey(String word, char guess) {
        String key = pattern;
        for (int i = 0; i < word.length(); i++ ) {
            if (word.charAt(i) == guess) {
                key = key.substring(0, i * 2) + guess + key.substring(i * 2 + 1);
            }
        }
        return key;
    }

    // A helper method that finds the key that corresponds to the largest value set in a map and returns it
    private String findBiggestSetKey(Map<String, Set<String>> map) {
        String biggestSetKey = map.keySet().iterator().next();
        for (String key: map.keySet()) {
            if (map.get(key).size() > map.get(biggestSetKey).size()) {
                biggestSetKey = key;
            }
        }
        return biggestSetKey;
    }

    // finds the number of occurrences of the guess in the current pattern
    private int findNumOccurrences(char guess) {
        int occurrences = 0;
        for (int i = 0; i < pattern.length(); i ++) {
            if (pattern.charAt(i) == guess) occurrences++;
        }
        return occurrences;
    }
}

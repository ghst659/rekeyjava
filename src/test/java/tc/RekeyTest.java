package tc;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Unit tests for the Rekey
 */
public class RekeyTest {
    private static class Generator implements Supplier<String> {
        private int index = 0;
        public String get() {
            String result = String.format("%d", this.index++);
            return result;
        }
        public void reset() {
            this.index = 0;
        }
    }
    @Test
    public void testRekey() {
        String[][] cases = {
            {"foo", "0"},
            {"bar", "1"},
            {"baz", "2"},
            {"mumble", "3"}
        };
        Supplier<String> gen = new Generator();
        Rekey<String> sut = new Rekey<>(gen);
        for (String[] pair: cases) {
            String newVal = sut.newKey(pair[0]);
            Assert.assertEquals(pair[1], newVal);
        }
        for (String[] pair: cases) {
            String secondVal = sut.newKey(pair[0]);
            Assert.assertEquals(pair[1], secondVal);
            String oldVal = sut.oldKey(pair[1]);
            Assert.assertEquals(pair[0], oldVal);
        }
        Set<String> olds = sut.oldSet();
        Assert.assertEquals(cases.length, olds.size());
        Set<String> news = sut.newSet();
        Assert.assertEquals(cases.length, news.size());
        for (String[] pair: cases) {
            olds.remove(pair[0]);
            news.remove(pair[1]);
        }
        Assert.assertTrue(olds.isEmpty());
        Assert.assertTrue(news.isEmpty());
    }
}

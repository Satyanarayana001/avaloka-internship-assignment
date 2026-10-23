public class Main {
    public static void main(String[] args) {
        testLruCache();
        testEventScheduler();
        System.out.println("All tests passed.");
    }

    private static void testLruCache() {
        LRUCache cache = new LRUCache(2);

        cache.put(1, 10);
        cache.put(2, 20);
        assertEquals(10, cache.get(1), "get existing key");
        cache.put(3, 30); // Evicts key 2 because key 1 was just used.
        assertEquals(-1, cache.get(2), "least-recently-used eviction");
        assertEquals(30, cache.get(3), "get newly inserted key");

        cache.put(1, 15); // Update existing key.
        assertEquals(15, cache.get(1), "update existing key");

        LRUCache zeroCapacity = new LRUCache(0);
        zeroCapacity.put(7, 70);
        assertEquals(-1, zeroCapacity.get(7), "zero-capacity cache");
    }

    private static void testEventScheduler() {
        int[][] adjacentEvents = {
            {9, 10},
            {10, 11},
            {11, 12}
        };
        assertEquals(true, EventScheduler.canAttendAll(adjacentEvents),
            "adjacent events do not overlap");
        assertEquals(1, EventScheduler.minRoomsRequired(adjacentEvents),
            "adjacent events need one room");

        int[][] overlappingEvents = {
            {9, 11},
            {9, 10},
            {10, 12}
        };
        assertEquals(false, EventScheduler.canAttendAll(overlappingEvents),
            "overlapping events");
        assertEquals(2, EventScheduler.minRoomsRequired(overlappingEvents),
            "minimum rooms for overlapping events");

        int[][] noEvents = {};
        assertEquals(true, EventScheduler.canAttendAll(noEvents),
            "empty schedule can attend all");
        assertEquals(0, EventScheduler.minRoomsRequired(noEvents),
            "empty schedule needs no rooms");
    }

    private static void assertEquals(int expected, int actual, String testName) {
        if (expected != actual) {
            throw new AssertionError(
                testName + ": expected " + expected + ", but got " + actual
            );
        }
    }

    private static void assertEquals(boolean expected, boolean actual, String testName) {
        if (expected != actual) {
            throw new AssertionError(
                testName + ": expected " + expected + ", but got " + actual
            );
        }
    }
}

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Scheduling helpers for intervals represented as {start, end}.
 * An event ending at time t does not overlap an event starting at time t.
 */
public final class EventScheduler {
    private EventScheduler() {
        // Utility class.
    }

    /**
     * Returns true if no two events overlap; otherwise returns false.
     *
     * Time: O(n log n), due to sorting.
     * Auxiliary space: O(n), because the input is copied before sorting.
     */
    public static boolean canAttendAll(int[][] events) {
        validateEvents(events);
        int[][] sorted = copyEvents(events);
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[0], b[0]));

        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i][0] < sorted[i - 1][1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the minimum number of rooms required for all events.
     *
     * A min-heap stores the end times of events currently occupying rooms.
     * Before scheduling an event, all rooms whose events ended at or before
     * its start time are released.
     *
     * Time: O(n log n).
     * Auxiliary space: O(n).
     */
    public static int minRoomsRequired(int[][] events) {
        validateEvents(events);
        if (events.length == 0) {
            return 0;
        }

        int[][] sorted = copyEvents(events);
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[0], b[0]));

        PriorityQueue<Integer> endingTimes = new PriorityQueue<>();
        int maxRooms = 0;

        for (int[] event : sorted) {
            int start = event[0];
            int end = event[1];

            while (!endingTimes.isEmpty() && endingTimes.peek() <= start) {
                endingTimes.poll();
            }

            endingTimes.offer(end);
            maxRooms = Math.max(maxRooms, endingTimes.size());
        }

        return maxRooms;
    }

    private static int[][] copyEvents(int[][] events) {
        int[][] copy = new int[events.length][];
        for (int i = 0; i < events.length; i++) {
            copy[i] = events[i].clone();
        }
        return copy;
    }

    private static void validateEvents(int[][] events) {
        if (events == null) {
            throw new IllegalArgumentException("Events cannot be null.");
        }

        for (int i = 0; i < events.length; i++) {
            if (events[i] == null || events[i].length != 2) {
                throw new IllegalArgumentException(
                    "Each event must contain exactly a start and end time (index " + i + ")."
                );
            }
            if (events[i][0] > events[i][1]) {
                throw new IllegalArgumentException(
                    "An event's start time cannot be after its end time (index " + i + ")."
                );
            }
        }
    }
}

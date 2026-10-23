# Avaloka Internship Assignment — Java

Solutions for the two data-structures and algorithms problems in the Avaloka AI / Multi-Agent Systems Intern take-home assignment.

## Files

- `LRUCache.java` — LRU cache with `get` and `put` operations.
- `EventScheduler.java` — checks whether all events can be attended and calculates the minimum number of rooms required.
- `Main.java` — lightweight executable tests for the main cases and edge cases.

## Requirements

- Java 8 or newer.

## Compile and run

Run these commands from the repository directory:

```bash
javac LRUCache.java EventScheduler.java Main.java
java Main
```

Expected output:

```text
All tests passed.
```

## Problem 1: LRU Cache

### Approach

A `HashMap<Integer, Node>` maps each key directly to its node. A doubly linked list tracks recency: the node immediately after the dummy head is the most recently used, and the node immediately before the dummy tail is the least recently used.

- `get(key)`: look up the node in the map. If found, move it to the front and return its value; otherwise return `-1`.
- `put(key, value)`: update and move an existing node to the front, or insert a new node at the front. If the cache exceeds capacity, remove the node just before the dummy tail and delete its key from the map.
- A cache with capacity `0` stores no entries.

### Complexity

- `get`: average **O(1)** time.
- `put`: average **O(1)** time.
- Space: **O(C)**, where `C` is the cache capacity.

The hash map provides average O(1) lookup. The doubly linked list supports O(1) removal and insertion when a node reference is known.

## Problem 2: Event Scheduler

Events are represented as `{start, end}` integer pairs. The implementation does not modify the caller's input.

### `canAttendAll`

Copy and sort events by start time. If any event starts strictly before the previous event ends, there is an overlap and the method returns `false`. If no such pair exists, it returns `true`. Equality is allowed, so an event ending at time `t` does not overlap an event starting at `t`.

- Time: **O(n log n)** for sorting.
- Auxiliary space: **O(n)** for the copied event array.

### `minRoomsRequired`

Sort events by start time and use a min-heap of end times for events occupying rooms. Before assigning the next event, remove every end time less than or equal to its start time, because those rooms are free. Add the current event's end time and track the maximum heap size reached.

- Time: **O(n log n)**.
- Auxiliary space: **O(n)**.

## Final discussion and analysis

### 1. Time and space complexity

For the LRU cache, both operations take average O(1) time and the cache uses O(C) space. For both scheduler methods, sorting makes the time complexity O(n log n), with O(n) auxiliary space.

### 2. Trade-offs: HashMap + doubly linked list

The combination supports average O(1) key lookup and O(1) recency updates or eviction. The trade-offs are extra memory for map entries and linked-list pointers, plus more implementation complexity than a simple list. A plain list would require O(n) search to find a key or least-recently-used entry.

### 3. Assigning actual room names or numbers

The current method calculates the minimum count. To also assign room IDs, sort events by start time and maintain two min-heaps: one containing `(endTime, roomNumber)` for occupied rooms, and one containing available room numbers. Before each event, release all occupied rooms whose end time is less than or equal to the event's start. Reuse the smallest available room number if one exists; otherwise allocate the next room number. Record the chosen room number against the event. This produces an explicit event-to-room assignment while reusing rooms whenever possible.

### 4. Thread safety

The implementation is not thread-safe by itself. Concurrent `get` and `put` calls could interleave changes to the map and linked list and corrupt their shared state. One straightforward solution is to synchronize both public methods on the cache instance. Another is to use a `ReentrantLock` around each complete operation, ensuring the map and list are updated atomically. The trade-off is lock contention under concurrent access; a more advanced concurrent design would need to preserve the same recency guarantees.

## Notes

The implementation assumes each event has a start time less than or equal to its end time. Invalid inputs are rejected with `IllegalArgumentException`.

/*
    146. LRU Cache
    
    Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.

Implement the LRUCache class:

LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
int get(int key) Return the value of the key if the key exists, otherwise return -1.
void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.
The functions get and put must each run in O(1) average time complexity.

 

Example 1:

Input
["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
[[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
Output
[null, null, null, 1, null, -1, null, -1, 3, 4]

Explanation
LRUCache lRUCache = new LRUCache(2);
lRUCache.put(1, 1); // cache is {1=1}
lRUCache.put(2, 2); // cache is {1=1, 2=2}
lRUCache.get(1);    // return 1
lRUCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
lRUCache.get(2);    // returns -1 (not found)
lRUCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
lRUCache.get(1);    // return -1 (not found)
lRUCache.get(3);    // return 3
lRUCache.get(4);    // return 4
 

Constraints:

1 <= capacity <= 3000
0 <= key <= 104
0 <= value <= 105
At most 2 * 105 calls will be made to get and put.
*/


class Node {
    int key;
    int value;
    Node prev = null;
    Node next = null;

    Node(int key, int value) {
        this.key = key;
        this.value = value;
    }
}

class DoubleLinkedList {
    Node head;
    Node tail;

    public DoubleLinkedList(){
        head = null;
        tail = null;
    }

    void remove(Node node) {
        if(node.prev == null) {
            tail = node.next;
        } else {
            node.prev.next = node.next;
        }

        if(node.next == null) {
            head = node.prev;
        } else {
            node.next.prev = node.prev;
        }

        node.prev = node.next = null;
    }

    void addTohead(Node node) {
        if(head == null) {
            head = tail = node;
            return;
        }
        head.next = node;
        node.prev = head;
        head = node;
    }

    Node removeFromTail() {
        Node prevTail = tail;
        if(tail == null) {
            return prevTail;
        }
        tail = tail.next;
        if(tail == null) {
            head = tail = null;
        }
         else {
             tail.prev = null;
         }
        return prevTail;
    }
}

class LRUCache {
    public int capacity;
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private final DoubleLinkedList lruList = new DoubleLinkedList();
    public LRUCache(int capacity) {
        this.capacity = capacity;
    }
    
    public int get(int key) {
        Node node = nodeMap.get(key);
        if(node == null) {
            return -1;
        }
        moveToMRU(node);
        return node.value;
    }
    
    public void put(int key, int value) {
        Node node = nodeMap.get(key);
        if(node != null) {
            node.value = value;
            nodeMap.put(key, node);
            moveToMRU(node);
            return;
        }

        node = new Node(key, value);
        if(nodeMap.size() == capacity) {
            evict();
        }
        nodeMap.put(key,node);
        addTohead(node);
    }

    void moveToMRU(Node node){
        lruList.remove(node);
        lruList.addTohead(node);
    }

    void evict(){
        Node node = lruList.removeFromTail();
        if(node != null) {
            nodeMap.remove(node.key);
        }
    }

    void addTohead(Node node) {
        lruList.addTohead(node);
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 
 Explaination - https://www.romaglushko.com/blog/design-lru-cache/
 */

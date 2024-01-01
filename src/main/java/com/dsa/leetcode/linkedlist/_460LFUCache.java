package com.dsa.leetcode.linkedlist;

import java.util.HashMap;
import java.util.Map;

public class _460LFUCache {

    public static void main(String[] args) {

        LFUCache lfuCache = new LFUCache(2);
        lfuCache.put(4, 16);
        lfuCache.put(3, 9);
        lfuCache.put(2, 4);

        lfuCache.get(2);
        lfuCache.get(2);
        lfuCache.get(2);

        lfuCache.get(3);
        lfuCache.put(6, 36);

        lfuCache.get(3);
        lfuCache.get(3);


    }

    static class LFUCache {


        final int capacity;
        int curSize;
        int minFrequency;
        Map<Integer, DLLNode> cache;
        Map<Integer, DoubleLinkedList> frequencyMap;

        /*.*/
        /*
         * @param capacity: total capacity of LFU Cache
         * @param curSize: current size of LFU cache
         * @param minFrequency: frequency of the last linked list (the minimum frequency of entire LFU cache)
         * @param cache: a hash map that has key to Node mapping, which used for storing all nodes by their keys
         * @param frequencyMap: a hash map that has key to linked list mapping, which used for storing all
         * double linked list by their frequencies
         * */


        public LFUCache(int capacity) {//TODO
            this.capacity = capacity;
            this.curSize = 0;
            this.minFrequency = 0;

            this.cache = new HashMap<>();
            this.frequencyMap = new HashMap<>();
        }

        public int get(int key) {//TODO
            DLLNode curNode = cache.get(key);
            if (curNode == null) {
                return -1;
            }
            updateNode(curNode);
            return curNode.val;
        }

        /**
         * add new node into LFU cache, as well as double linked list
         * condition 1: if LFU cache has input key, update node value and node position in list
         * condition 2: if LFU cache does NOT have input key
         * - sub condition 1: if LFU cache does NOT have enough space, remove the Least Recent Used node
         * in minimum frequency list, then add new node
         * - sub condition 2: if LFU cache has enough space, add new node directly
         **/
        public void put(int key, int value) {//TODO
            // corner case: check cache capacity initialization
            if (capacity == 0) {
                return;
            }

            if (cache.containsKey(key)) {//UPDATION
                DLLNode curNode = cache.get(key);
                curNode.val = value;
                updateNode(curNode);
            } else {//ADDITION
                curSize++;
                if (curSize > capacity) {
                    // get minimum frequency list
                    DoubleLinkedList minFreqList = frequencyMap.get(minFrequency);
                    cache.remove(minFreqList.tail.prev.key);
                    minFreqList.removeNode(minFreqList.tail.prev);
                    curSize--;
                }
                // reset min frequency to 1 because of adding a new node
                minFrequency = 1;
                DLLNode newNode = new DLLNode(key, value);

                // get the list with frequency 1, and then add new node into the list, as well as into LFU cache
                DoubleLinkedList curList = frequencyMap.getOrDefault(1, new DoubleLinkedList());
                curList.addNode(newNode);
                frequencyMap.put(1, curList);
                cache.put(key, newNode);
            }
        }

        void updateNode(DLLNode curNode) {
            int curFreq = curNode.frequency;
            DoubleLinkedList curList = frequencyMap.get(curFreq);
            curList.removeNode(curNode);

            // if current list the last list which has the lowest frequency and the current node is the only node in that list,
            // we need to empty the entire list and then increase min frequency value by 1
            if (curFreq == minFrequency && curList.listSize == 0) {//MAIL LOGIC
                minFrequency++;//because the current node in this case will be shifted to the DLL corresponding to the curFreq+1 and that will be the MIN freq
            }

            curNode.frequency++;
            // add current node to another list has current frequency + 1,
            // if we do not have the list with this frequency, initialize it
            DoubleLinkedList newList = frequencyMap.getOrDefault(curNode.frequency, new DoubleLinkedList());
            newList.addNode(curNode);
            frequencyMap.put(curNode.frequency, newList);
        }

        /*
         * @param listSize: current size of double linked list
         * @param head: head node of double linked list
         * @param tail: tail node of double linked list
         * */
        class DoubleLinkedList {
            int listSize;
            DLLNode head;
            DLLNode tail;

            public DoubleLinkedList() {
                this.listSize = 0;
                this.head = new DLLNode(0, 0);
                this.tail = new DLLNode(0, 0);
                head.next = tail;//by default, we are having 2 nodes in dll
                tail.prev = head;
            }

            /**
             * add new node into head of list and increase list size by 1
             **/
            public void addNode(DLLNode curNode) {
                DLLNode nextNode = head.next;
                curNode.next = nextNode;
                curNode.prev = head;
                head.next = curNode;
                nextNode.prev = curNode;
                listSize++;
            }

            /**
             * remove input node and decrease list size by 1
             **/
            public void removeNode(DLLNode curNode) {
                DLLNode prevNode = curNode.prev;
                DLLNode nextNode = curNode.next;
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
                listSize--;
            }

        }

        class DLLNode {
            int key;
            int val;
            int frequency;
            DLLNode prev;
            DLLNode next;

            public DLLNode(int key, int val) {
                this.key = key;
                this.val = val;
                this.frequency = 1;
            }
        }

    }


}

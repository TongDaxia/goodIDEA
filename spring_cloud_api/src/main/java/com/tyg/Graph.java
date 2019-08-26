package com.tyg;

import java.util.LinkedList;
import java.util.Queue;


/**
 * 无向图的样子就是一个数组上挂了一个链表
 * v代表的时顶点的个数
 * 创建了v个链表
 */
public class Graph { // 无向图
    private int v; // 顶点的个数
    private LinkedList<Integer> adj[]; // 邻接表

    public Graph(int v) {
        this.v = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; ++i) {
            adj[i] = new LinkedList<>();
        }
    }

    /**
     * 添加一条边，就是两个订点的位置
     * 比如 3，4：需要在编号为3的链表中添加一个 元素 4；并且在编号4的链表中添加一个3 元素
     *
     * @param s
     * @param t
     */
    public void addEdge(int s, int t) { // 无向图一条边存两次
        adj[s].add(t);
        adj[t].add(s);
    }

    /**
     * 广度优先搜索算法的实现方法
     * 广度优先搜索算法是地毯式搜索，采用迭代循环出每一个顶点的下一节点是否符合要求并继续遍历
     * 直到找到目标对象
     *
     * @param s 第一个目标，出发点
     * @param t 第二个目标，终点
     *          需要查询出经过的最短路径
     */
    public void bfs(int s, int t) {
        if (s == t) return;
        boolean[] visited = new boolean[v];//记录第i个顶点是否被访问过
        visited[s] = true;
        //用来存储已经被访问、但相连的顶点还没有被访问的顶点
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        int[] prev = new int[v];//用来记录搜索路径
        for (int i = 0; i < v; ++i) {
            prev[i] = -1;
        }
        while (queue.size() != 0) {
            int w = queue.poll();//从队列首位置取元素并删除，指的是w位置的链表需要进行遍历
            //取目标的每一个连接对象，如果满足，没有下个节没有被访问，并且
            for (int i = 0; i < adj[w].size(); ++i) {
                int q = adj[w].get(i);
                if (!visited[q]) {
                    prev[q] = w;
                    if (q == t) {
                        print(prev, s, t);
                        return;
                    }
                    visited[q] = true;
                    queue.add(q);
                }
            }
        }
    }

    private void print(int[] prev, int s, int t) { // 递归打印s->t的路径
        if (prev[t] != -1 && t != s) {
            print(prev, s, prev[t]);
        }
        System.out.print(t + " ");
    }


    boolean found = false; // 全局变量或者类成员变量

    public void dfs(int s, int t) {//深度优先算法
        found = false;
        boolean[] visited = new boolean[v];
        int[] prev = new int[v];
        for (int i = 0; i < v; ++i) {
            prev[i] = -1;
        }

        recurDfs(s, t, visited, prev);
        print(prev, s, t);


    }

    /**
     * 深度优先搜索算法
     * 关键就是 遍历当前节点的链表的值，如果这个值没有被访问，则记录列表中的本节点对应的上一个节点；迭代
     *
     * @param w 当前访问节点，初始值是第一个节点，后续是链表中每一个值
     * @param t 目标值
     * @param visited 是否已经被访问的列表，存储了每一个节点的状态
     * @param prev 记录每一个节点（列表位置）对应的上一节点
     */
    private void recurDfs(int w, int t, boolean[] visited, int[] prev) {
        if (found == true) return;
        visited[w] = true;
        if (w == t) {
            found = true;
            return;
        }
        for (int i = 0; i < adj[w].size(); ++i) {
            int q = adj[w].get(i);
            if (!visited[q]) {
                prev[q] = w;
                recurDfs(q, t, visited, prev);
            }
        }
    }

    public static void main(String[] args) {


        Graph graph = new Graph(8);

        graph.addEdge(0, 1);
        graph.addEdge(0, 3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 4);
        graph.addEdge(2, 5);
        graph.addEdge(3, 4);
        graph.addEdge(5, 4);
        graph.addEdge(6, 4);
        graph.addEdge(5, 7);
        graph.addEdge(6, 7);


        // graph.bfs(0,7);
        graph.dfs(0, 7);

    }


}

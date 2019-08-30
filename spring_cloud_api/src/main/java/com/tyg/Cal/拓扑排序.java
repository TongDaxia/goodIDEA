package com.tyg.Cal;

import com.tyg.leetCode.Array;

import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * 如何确定代码源文件的编译依赖关系
 *
 */
public class 拓扑排序 {


    //数据结构是一个图，相当于一个节点上挂着一个链表
    static class Graph {
        private int v; // 顶点的个数
        private LinkedList<Integer> adj[]; // 邻接表
        public Graph(int v) {
            this.v = v;
            adj = new LinkedList[v];
            for (int i=0; i<v; ++i) {
                adj[i] = new LinkedList<>();
            }
        }
        public void addEdge(int s, int t) { // s先于t，边s->t
            adj[s].add(t);
        }

            public String toString(){
            String toString = "";
                for (int i = 0; i < v; i++) {
                    toString +=  "节点"+i+"链表为： "+adj[i].toString()+";   ";
                }
            return toString ;

        }

         //Kahn算法
         public void topoSortByKahn() {
             int[] inDegree = new int[v]; // 统计每个顶点的入度
             for (int i = 0; i < v; ++i) {
                 for (int j = 0; j < adj[i].size(); ++j) {
                     int w = adj[i].get(j); // i->w
                     inDegree[w]++;
                 }
             }
             LinkedList<Integer> queue = new LinkedList<>();
             for (int i = 0; i < v; ++i) {
                 if (inDegree[i] == 0) queue.add(i);
             }
             while (!queue.isEmpty()) {
                 int i = queue.remove();
                 System.out.print("->" + i);
                 for (int j = 0; j < adj[i].size(); ++j) {
                     int k = adj[i].get(j);
                     inDegree[k]--;
                     if (inDegree[k] == 0) queue.add(k);
                 }
             }
         }

        //DFS算法,深度优先遍历
        public void topoSortByDFS() {
            // 先构建逆邻接表，边s->t表示，s依赖于t，t先于s
            LinkedList<Integer> inverseAdj[] = new LinkedList[v];
            for (int i = 0; i < v; ++i) { // 申请空间
                inverseAdj[i] = new LinkedList<>();
            }
            for (int i = 0; i < v; ++i) { // 通过邻接表生成逆邻接表
                for (int j = 0; j < adj[i].size(); ++j) {
                    int w = adj[i].get(j); // i->w
                    inverseAdj[w].add(i); // w->i
                }
            }
            boolean[] visited = new boolean[v];
            for (int i = 0; i < v; ++i) { // 深度优先遍历图
                if (visited[i] == false) {
                    visited[i] = true;
                    dfs(i, inverseAdj, visited);
                }
            }
        }
        private void dfs(
                int vertex, LinkedList<Integer> inverseAdj[], boolean[] visited) {
            for (int i = 0; i < inverseAdj[vertex].size(); ++i) {
                int w = inverseAdj[vertex].get(i);
                if (visited[w] == true) continue;
                visited[w] = true;
                dfs(w, inverseAdj, visited);
            } // 先把vertex这个顶点可达的所有顶点都打印出来之后，再打印它自己
            System.out.print("->" + vertex);
        }



    }

    public static void main(String[] args) {

        Graph graph = new Graph(7);
        graph.addEdge(4,1);
        graph.addEdge(4,3);
        graph.addEdge(2,6);
        graph.addEdge(0,1);
        graph.addEdge(5,3);
        graph.topoSortByKahn();
        System.out.println(graph.toString());

    }




}

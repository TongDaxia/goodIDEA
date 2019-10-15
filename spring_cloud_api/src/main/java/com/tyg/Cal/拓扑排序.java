package com.tyg.Cal;

import com.tyg.leetCode.Array;

import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * 如何确定代码源文件的编译依赖关系
 *
 */
@SuppressWarnings("all")
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

        /**
         *
         * 如果某个顶点入度为0， 也就表示，没有任何顶点必须先于这个顶点执行，那么这
         * 个顶点就可以执行了。
         * 找出一个入度为0的顶点，将其输出到拓扑排序的结果序列中（对应代码中就是把它打印出来），
         * 并且把这个顶点从图中删除（也就是把这个顶点可达的顶点的入度都减1）。
         * 我们循环执行上面的过程，直到所有的顶点都被输出。最后输出的序列，就是满足局部依赖关系的拓扑排序。
         *
         */
         public void topoSortByKahn() {
             int[] inDegree = new int[v]; // 统计每个顶点的入度
             for (int i = 0; i < v; ++i) {
                 for (int j = 0; j < adj[i].size(); ++j) {
                     int w = adj[i].get(j); // i->w   比如 3 ： 4 6 就在 4 ， 6 处 +1
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
                     if (inDegree[k] == 0)
                     {
                         //System.out.println("--------");
                         queue.add(k);
                     }else {
                         //System.out.println("===跳过，下一个开始的===");
                     }
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
                    dfs(i, inverseAdj, visited);//目前是第i个节点
                }
            }
        }

        /**
         *
         * @param vertex 目前访问的节点编号
         * @param inverseAdj 逆邻接表
         * @param visited 深度优先搜索的记号表
         */
        private void dfs(
                int vertex, LinkedList<Integer>[] inverseAdj, boolean[] visited) {
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

        LinkedList<Integer>[]  list = new LinkedList[12];
        Integer[] in = new Integer[23];


        Graph graph = new Graph(7);
        graph.addEdge(4,1);
        graph.addEdge(4,3);
        graph.addEdge(6,4);
        graph.addEdge(2,6);
        graph.addEdge(0,1);
        graph.addEdge(5,3);
        graph.addEdge(5,4);
        graph.addEdge(3,2);//有环形结构,kahn就会打印失败；深度优先会打印一个错的，后添加的关系覆盖了前面不满足的，eg: ->0->5->3->2->6->4->1
        //graph.topoSortByKahn();//->0->2->5->6->4->1->3
        System.out.println();
        graph.topoSortByDFS();// ->0->5->2->6->4->1->3



    }




}

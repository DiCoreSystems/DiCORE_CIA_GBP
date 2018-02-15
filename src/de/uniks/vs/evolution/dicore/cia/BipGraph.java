package de.uniks.vs.evolution.dicore.cia;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by alex on 16/8/9.
 */
public class BipGraph {



    // -------- BipGraph ---------------------------------

    int INF = Integer.MAX_VALUE;
    int NIL = 0;

    int[] pairU;
    int[] pairV;
    int[] dist;
    ArrayList<Integer>[] adj;

    int m;
    int n;

    public void BipGraph(int m, int n)
    {
        this.m = m;
        this.n = n;
        adj = new ArrayList[m+1];

        for (int i = 0; i < adj.length; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    public void addEdge(int u, int v)
    {
        adj[u].add(v); // Add u to v’s list.
        adj[v].add(u); // Add u to v’s list.
    }

    public int hopcroftKarp()
    {
        pairU = new int[m + 1];

        // pairV[v] stores pair of v in matching. If v
        // doesn't have any pair, then pairU[v] is NIL
        pairV = new int[n + 1];

        // dist[u] stores distance of left side vertices
        // dist[u] is one more than dist[u'] if u is next
        // to u'in augmenting path
        dist = new int[m + 1];

        // Initialize NIL as pair of all vertices
        for (int u=0; u<m; u++)
            pairU[u] = NIL;
        for (int v=0; v<n; v++)
            pairV[v] = NIL;

        // Initialize result
        int result = 0;

        // Keep updating the result while there is an
        // augmenting path.
        while (bfs())
        {
            // Find a free vertex
            for (int u=1; u<=m; u++)

                // If current vertex is free and there is
                // an augmenting path from current vertex
                if (pairU[u] == NIL && dfs(u))
                    result++;
        }

        return result;
    }



    // Returns true if there is an augmenting path, else returns
// false
    boolean bfs()
    {
        Queue<Integer> Q = new LinkedList<>(); //an integer queue

        // First layer of vertices (set distance as 0)
        for (int u=1; u<=m; u++)
        {
            // If this is a free vertex, add it to queue
            if (pairU[u]==NIL)
            {
                // u is not matched
                dist[u] = 0;
                Q.add(u);
            }

            // Else set distance as infinite so that this vertex
            // is considered next time
            else dist[u] = INF;
        }

        // Initialize distance to NIL as infinite
        dist[NIL] = INF;

        // Q is going to contain vertices of left side only.
        while (!Q.isEmpty())
        {
            // Dequeue a vertex
//            int u = Q.front();
            int u = Q.element();
            Q.remove();

            // If this node is not NIL and can provide a shorter path to NIL
            if (dist[u] < dist[NIL])
            {
                // Get all adjacent vertices of the dequeued vertex u

                for (int i:adj[u])
                {
                    int v = i;

                    // If pair of v is not considered so far
                    // (v, pairV[V]) is not yet explored edge.
                    if (dist[pairV[v]] == INF)
                    {
                        // Consider the pair and add it to queue
                        dist[pairV[v]] = dist[u] + 1;
                        Q.add(pairV[v]);
                    }
                }
            }
        }

        // If we could come back to NIL using alternating path of distinct
        // vertices then there is an augmenting path
        return (dist[NIL] != INF);
    }

    // Returns true if there is an augmenting path beginning with free vertex u
    boolean dfs(int u)
    {
        if (u != NIL)
        {
            for (int i:adj[u])
            {
                // Adjacent to u
                int v = i;

                // Follow the distances set by BFS
                if (dist[pairV[v]] == dist[u]+1)
                {
                    // If dfs for pair of v also returns
                    // true
                    if (dfs(pairV[v]) == true)
                    {
                        pairV[v] = u;
                        pairU[u] = v;
                        return true;
                    }
                }
            }

            // If there is no augmenting path beginning with u.
            dist[u] = INF;
            return false;
        }
        return true;
    }

    @Test
    public void testBipGraph() {
        BipGraph algorithm = new BipGraph();
        algorithm.BipGraph(4, 4);
        algorithm.addEdge(1, 2);
        algorithm.addEdge(1, 3);
        algorithm.addEdge(2, 1);
        algorithm.addEdge(3, 2);
        algorithm.addEdge(4, 2);
        algorithm.addEdge(4, 4);

        System.out.println( "Size of maximum matching is " + algorithm.hopcroftKarp());
    }

    // -------- END BipGraph ---------------------------------
}

package org.nschmidt.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class McNode {
    
    public static McNode create() {
        return new McNode();
    }

    /** Die Anzahl, die dieser Knoten durch den Eltern-Knoten aufgerufen wurde. */ 
    double N = 0;
    
    /** Der Wert dieses Knotens */ 
    double W = 0;
    
    /** Der durchschnittliche Wert dieses Knotens */ 
    double Q = 0;
    
    /** Die vorherige Wahrscheinlichkeit diesen Knoten zu wählen */
    double P = 0;
    
    /** Eine obere Grenze aus P und N, die steigt, wenn der Knoten wenig gewählt wurde */
    double U = u();
    
    /*
                                    sqrt(sum(N(Geschwisterknoten)))
     U(Knoten) = c_puct * P(Knoten) -------------------------------
                                         1 + N(Knoten)
                 c_puct := 1
     */
            
    /** Der Zustand des Spiels */
    Board state;
    
    McNode parent = null;
    final List<McNode> childs = new ArrayList<>();
    
    private McNode() {
        state = Board.create();
    }
    
    private McNode(Board board) {
        state = board;
    }


    private double u() {
        if (parent == null) return 0;
        U = P * Math.sqrt(parent.childs.stream().mapToDouble(node -> node.N).sum()) / (1.0 + N);
        return U;
    }
    
    public void simulate() {
        if (state.isGameOver()) {
            if (state.wins('X')) backup(1); 
            if (state.isDraw()) backup(0);
            if (state.wins('O')) backup(-1);
            return;
        }
        
        if (childs.isEmpty()) {
            // Wir haben ein vorläufiges Ende erreicht
            addChilds();
            // Sage Wert und Wahrscheinlichkeiten voraus
            predict();
        }
        
        // Wähle Knoten mit Maximum von P+Q ( P = Die vorherige Wahrscheinlichkeit diesen Knoten zu wählen, 
        //                                    Q ein Wert, der steigt, wenn der Knoten wenig gewählt wurde. )
        
        McNode nextNode = childs.get(0);
        double maximum = nextNode.P + nextNode.u();
        for (McNode child : childs) {
            double sumPQ = child.P + child.u();
            if (sumPQ > maximum) {
                nextNode = child;
            }
        }
        
        nextNode.simulate();
    }

    private void predict() {
        // Verwende das Neuronale Netzwerk, um
        // - W den Wert des Knotens und 
        // - P die Wahrscheinlichkeiten für die Kindknoten zu bestimmen 
        PredictionResult prediction = NetworkInstance.predict(this);
        W = prediction.value();
        double[] probabilities = prediction.probabilities();
        int[] moves = state.moves();
        for (int i = 0; i < moves.length; i++) {
            int move = moves[i];
            double probability = probabilities[move];
            childs.get(i).P = probability;
        }
    }

    private void addChilds() {
       childs.addAll(Arrays.stream(state.moves()).boxed()
                           .map(state::applyMove)
                           .map(McNode::new)
                           .map(node -> {node.parent = this; return node;})
                           .toList());
    }
    
    private void backup(double v) {
        N = N + 1; // Dieser Knoten wurde ein weiteres Mal aufgerufen
        W = W + v; // Der Wert steigt um den 
        Q = W / N; // Der durschnittliche Wert dieses Knotens
        if (parent != null) parent.backup(v);
    }
    
    void retrain() {
        NetworkInstance.retrain(this);
    }
    
    void collect(List<McNode> allNodes) {
        if (childs.isEmpty()) return;
        allNodes.addAll(childs);
        for (McNode child : childs) {
            child.collect(allNodes);
        }
    }
    
    int size() {
        if (childs.isEmpty()) return 1;
        int result = 0;
        for (McNode child : childs) {
            result += child.size();
        }
        
        return result;
    }
}

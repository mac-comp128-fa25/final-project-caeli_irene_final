import edu.macalester.graphics.*;
import java.awt.Color;
import java.util.*;

/**
 * Visualizer: displays nodes (card IDs or Card.toString()) in a readable grid.
 * - Can be constructed with a new CanvasWindow(width,height) or with an existing CanvasWindow.
 * - onAddCard / onRemoveCard accept either Card or String (overloads).
 */
public class Visualizer {

    private final CanvasWindow window;
    private final Map<String, VisualNode> nodes = new LinkedHashMap<>();
    private double startX = 50;
    private double startY = 50;
    private double offsetX = 350;
    private double offsetY = 100;
    private int colCount = 0;
    private int rowCount = 0;
    private int maxCols = 4;

    // Constructors
    public Visualizer(int width, int height) {
        this(new CanvasWindow("Visualizer", width, height));
    }

    public Visualizer(CanvasWindow existingWindow) {
        this.window = existingWindow;
    }

    // Small helper node class
    private class VisualNode {
        GraphicsGroup group;
        Rectangle box;
        GraphicsText label;
        Point center;

        VisualNode(String id, Point pos) {
            group = new GraphicsGroup();
            Rectangle box = new Rectangle(0, 0, 180, 80);
            box.setStrokeColor(Color.BLACK);
            box.setStrokeWidth(2);
            box.setFilled(true);
            box.setFillColor(Color.WHITE);

            label = new GraphicsText(id);
            label.setFontSize(14);
            label.setPosition(10, 40);

            group.add(box);
            group.add(label);
        
            group.setPosition(pos);
            center = pos;
        }

        void setColor(Color c) {
            box.setFillColor(c);
        }

        void addToWindow() {
            window.add(group);
        }

        void removeFromWindow() {
            window.remove(group);
        }

        void setPosition(Point p) {
            group.setPosition(p);
            center = p;
        }
    }

    // Allow passing Card directly (SetManager can call with Card)
    public void onAddCard(Card c) {
        if (c == null) return;
        onAddCard(c.toString());
    }

    public void onRemoveCard(Card c) {
        if (c == null) return;
        onRemoveCard(c.toString());
    }

    // String-based API (also supported)
    public void onAddCard(String cardId) {
        if (cardId == null) return;
        // Prevent duplicates: if already present, don't add a second node
        if (nodes.containsKey(cardId)) return;

        Point pos = getNextPosition();
        VisualNode node = new VisualNode(cardId, pos);
        nodes.put(cardId, node);
        node.addToWindow();
        window.pause(120);
    }

    public void onRemoveCard(String cardId) {
        if (cardId == null) return;
        VisualNode node = nodes.remove(cardId);
        if (node != null) {
            node.removeFromWindow();
            // After a removal we re-layout remaining nodes so spacing stays neat
            relayoutAll();
        }
        window.pause(80);
    }

    // Keep track of drawn lines so we can remove them later
    private List<Line> activeLines = new ArrayList<>();

    public void highlightSet(List<String> cardIds) {
        clearLines();

        List<VisualNode> chosen = new ArrayList<>();
        for (String id : cardIds) {
            VisualNode node = nodes.get(id);
            if (node != null) {
                node.setColor(Color.GREEN);
                chosen.add(node);
            }
        }

        // Draw triangle connections between the 3 nodes
        if (chosen.size() == 3) {
            drawConnection(chosen.get(0), chosen.get(1));
            drawConnection(chosen.get(1), chosen.get(2));
            drawConnection(chosen.get(2), chosen.get(0));
        }

        window.pause(600);
    }

    private void drawConnection(VisualNode a, VisualNode b) {
        Line ln = new Line(
                a.center.getX() + 90, a.center.getY() + 40,
                b.center.getX() + 90, b.center.getY() + 40
        );
        ln.setStrokeColor(Color.GREEN);
        ln.setStrokeWidth(3);
        activeLines.add(ln);
        window.add(ln);
    }

    private void clearLines() {
        for (Line ln : activeLines) window.remove(ln);
        activeLines.clear();
    }

    private Point getNextPosition() {
        double x = startX + colCount * offsetX;
        double y = startY + rowCount * offsetY;
        colCount++;
        if (colCount >= maxCols) {
            colCount = 0;
            rowCount++;
        }
        return new Point(x, y);
    }

    /** Recompute positions for every node in insertion order (keeps layout tidy after removals) */
    private void relayoutAll() {
        // clear counters and re-place nodes in insertion order
        colCount = 0;
        rowCount = 0;
        List<String> keys = new ArrayList<>(nodes.keySet());

        // remove all groups from canvas, then add them in new positions
        window.removeAll();
        for (String id : keys) {
            Point pos = getNextPosition();
            VisualNode n = nodes.get(id);
            if (n != null) {
                n.setPosition(pos);
                n.addToWindow();
            }
        }
    }

    /** Optional clear */
    public void clearBoard() {
        for (VisualNode node : nodes.values()) node.removeFromWindow();
        nodes.clear();
        colCount = 0;
        rowCount = 0;
    }

    /** Simple pause helper */
    private void pause(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    /** Example main: use single CanvasWindow (no duplicate windows) */
    public static void main(String[] args) {
        // Create a single canvas, pass it to Visualizer and GameBoard so only one window is created
        CanvasWindow canvas = new CanvasWindow("Set Manager Visualization", 1000, 700);
        Visualizer viz = new Visualizer(canvas);
        GameBoard gb = new GameBoard(canvas);
        SetManager manager = new SetManager(gb); // uses existing constructor
        manager.setVisualizer(viz);

        // Run the ID-visualization routine (shows backtracking)
        List<Integer> ids = manager.generateBoardIDsForViz();

        System.out.println("Generated IDs count: " + ids.size());
    }
}

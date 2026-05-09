import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class FloresAnimadas extends JPanel {

    // ── Configuración (Integrante A agrega más flores; B ajusta velocidad) ──
    static final int NUM_FLORES = 12; // Integrante A cambia esto de 6 a 12
    static final int VELOCIDAD_MS = 30; // Integrante B cambia esto (ms por frame)

    private final ArrayList<Flor> flores = new ArrayList<>();
    private Timer timer;

    // ── Modelo de flor ──────────────────────────────────────────────────────
    static class Flor {
        double x, y, dx, dy;
        int radio;

        Flor(int panelW, int panelH) {
            Random r = new Random();
            radio = 25 + r.nextInt(20);
            x = r.nextInt(panelW);
            y = r.nextInt(panelH);
            dx = (r.nextBoolean() ? 1 : -1) * (1 + r.nextDouble() * 2);
            dy = (r.nextBoolean() ? 1 : -1) * (1 + r.nextDouble() * 2);
        }

        void mover(int w, int h) {
            x += dx;
            y += dy;
            if (x - radio < 0 || x + radio > w)
                dx = -dx;
            if (y - radio < 0 || y + radio > h)
                dy = -dy;
        }

        void dibujar(Graphics2D g) {
            int r = radio;
            // Pétalos
            g.setColor(new Color(255, 215, 0));
            for (int i = 0; i < 8; i++) {
                double ang = Math.toRadians(i * 45);
                int px = (int) (x + Math.cos(ang) * r * 1.6);
                int py = (int) (y + Math.sin(ang) * r * 1.6);
                g.fillOval(px - r / 2, py - r / 2, r, r);
            }
            // Centro
            g.setColor(new Color(139, 90, 43));
            g.fillOval((int) x - r / 2, (int) y - r / 2, r, r);
        }
    }

    // ── Constructor ─────────────────────────────────────────────────────────
    public FloresAnimadas() {
        setBackground(new Color(20, 80, 20));
        setPreferredSize(new Dimension(800, 600));

        // Inicializar flores (después de que el panel tenga tamaño)
        SwingUtilities.invokeLater(() -> {
            int w = getWidth() > 0 ? getWidth() : 800;
            int h = getHeight() > 0 ? getHeight() : 600;
            for (int i = 0; i < NUM_FLORES; i++)
                flores.add(new Flor(w, h));

            timer = new Timer(VELOCIDAD_MS, e -> {
                flores.forEach(f -> f.mover(getWidth(), getHeight()));
                repaint();
            });
            timer.start();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        flores.forEach(f -> f.dibujar(g2));
    }

    // ── Main ────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Flores Animadas 🌼");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new FloresAnimadas());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

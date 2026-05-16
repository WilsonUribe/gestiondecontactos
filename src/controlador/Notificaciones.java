package controlador;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

public class Notificaciones {

    private final JProgressBar barra;
    private final ScheduledExecutorService scheduler;

    public Notificaciones(JProgressBar barra) {
        this.barra = barra;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "NotificacionThread");
            t.setDaemon(true);
            return t;
        });
    }

    public void mostrar(String mensaje, Color color) {
        SwingUtilities.invokeLater(() -> {
            barra.setString(mensaje);
            barra.setForeground(color);
            barra.setValue(100);
        });

        // Limpiar notificación
        scheduler.schedule(() -> {
            SwingUtilities.invokeLater(() -> {
                barra.setString("");
                barra.setValue(0);
                barra.setForeground(UIManager.getColor("ProgressBar.foreground"));
            });
        }, 3, TimeUnit.SECONDS);
    }

    public void mostrarExito(String mensaje) {
        mostrar(mensaje, new Color(0, 153, 51));
    }

    public void mostrarError(String mensaje) {
        mostrar(mensaje, new Color(200, 0, 0));
    }

    public void mostrarInfo(String mensaje) {
        mostrar(mensaje, new Color(0, 102, 204));
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }
}
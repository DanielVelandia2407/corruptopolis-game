package com.corruptopolis.controller;

import com.corruptopolis.model.Game;
import com.corruptopolis.model.Game.BribeResult;
import com.corruptopolis.view.MainFrame;
import javax.swing.SwingUtilities;

/**
 * Controlador principal que gestiona la comunicación entre modelo y vista
 * Implementa el patrón MVC coordinando las interacciones del usuario
 */
public class GameController implements Game.GameObserver {
    private Game gameModel;
    private MainFrame gameView;

    /**
     * Constructor que inicializa el controlador
     */
    public GameController() {
        // Se inicializa cuando se crea un nuevo juego
    }

    /**
     * Inicia un nuevo juego
     */
    public void startNewGame(String playerName) {
        gameModel = new Game(playerName);
        gameModel.addObserver(this);

        SwingUtilities.invokeLater(() -> {
            if (gameView != null) {
                gameView.dispose();
            }
            gameView = new MainFrame(this);
            gameView.setVisible(true);
            updateView();
        });
    }

    /**
     * Maneja el intento de soborno de un contacto
     */
    public void handleBribeAttempt(String contactId) {
        if (gameModel == null) return;

        BribeResult result = gameModel.attemptBribe(contactId);

        SwingUtilities.invokeLater(() -> {
            gameView.showMessage(result.getMessage(), result.isSuccess() ? "Éxito" : "Error");
            updateView();
        });
    }

    /**
     * Maneja la extracción de recursos
     */
    public void handleExtractResources() {
        if (gameModel == null) return;

        gameModel.extractResources();

        SwingUtilities.invokeLater(() -> {
            gameView.showMessage("Recursos extraídos de la red", "Operación Completada");
            updateView();
        });
    }

    /**
     * Maneja operaciones de encubrimiento
     */
    public void handleCoverUpOperation(int investment) {
        if (gameModel == null) return;

        boolean success = gameModel.performCoverUp(investment);
        String message = success ? "Operación de encubrimiento exitosa" : "Fondos insuficientes";

        SwingUtilities.invokeLater(() -> {
            gameView.showMessage(message, success ? "Éxito" : "Error");
            updateView();
        });
    }

    /**
     * Maneja el lavado de dinero
     */
    public void handleMoneyLaundering(int amount) {
        if (gameModel == null) return;

        boolean success = gameModel.getPlayer().launderMoney(amount);
        String message = success ? "Dinero lavado exitosamente" : "Operación fallida";

        SwingUtilities.invokeLater(() -> {
            gameView.showMessage(message, success ? "Éxito" : "Error");
            updateView();
        });
    }

    /**
     * Avanza al siguiente turno
     */
    public void handleNextTurn() {
        if (gameModel == null) return;

        gameModel.nextTurn();

        SwingUtilities.invokeLater(() -> {
            updateView();
            if (!gameModel.isGameActive()) {
                gameView.showGameOver(gameModel.getPlayer().calculateScore());
            }
        });
    }

    /**
     * Maneja la consolidación del poder de un nodo
     */
    public void handleConsolidatePower(String nodeId, int investment) {
        if (gameModel == null) return;

        // Implementar lógica de consolidación
        SwingUtilities.invokeLater(() -> {
            gameView.showMessage("Poder consolidado en " + nodeId, "Operación Completada");
            updateView();
        });
    }

    /**
     * Maneja la neutralización de amenazas
     */
    public void handleNeutralizeThreat(String threatId, int investment) {
        if (gameModel == null) return;

        // Implementar lógica de neutralización
        SwingUtilities.invokeLater(() -> {
            gameView.showMessage("Amenaza neutralizada", "Operación Completada");
            updateView();
        });
    }

    /**
     * Obtiene información detallada de un nodo
     */
    public void requestNodeInfo(String nodeId) {
        if (gameModel == null) return;

        // Buscar el nodo y mostrar información
        SwingUtilities.invokeLater(() -> {
            gameView.showNodeInfo(nodeId);
        });
    }

    /**
     * Pausa o reanuda el juego
     */
    public void toggleGamePause() {
        if (gameModel == null) return;

        // Implementar pausa del juego
        SwingUtilities.invokeLater(() -> {
            updateView();
        });
    }

    /**
     * Guarda el juego actual
     */
    public void saveGame() {
        if (gameModel == null) return;

        // Implementar guardado
        SwingUtilities.invokeLater(() -> {
            gameView.showMessage("Juego guardado", "Guardado");
        });
    }

    /**
     * Carga un juego guardado
     */
    public void loadGame() {
        // Implementar carga
        SwingUtilities.invokeLater(() -> {
            updateView();
        });
    }

    /**
     * Reinicia el juego actual
     */
    public void restartGame() {
        if (gameModel != null) {
            String playerName = gameModel.getPlayer().getName();
            startNewGame(playerName);
        }
    }

    /**
     * Actualiza toda la vista con el estado actual del modelo
     */
    private void updateView() {
        if (gameView != null && gameModel != null) {
            gameView.updateDisplay(gameModel);
        }
    }

    /**
     * Implementación del Observer para reaccionar a cambios en el modelo
     */
    @Override
    public void onGameStateChanged(String eventType) {
        SwingUtilities.invokeLater(() -> {
            updateView();

            switch (eventType) {
                case "game_initialized":
                    gameView.showMessage("¡Bienvenido a Corruptópolis!", "Inicio del Juego");
                    break;
                case "bribe_success":
                    gameView.playSuccessAnimation();
                    break;
                case "bribe_failed":
                    gameView.playFailureAnimation();
                    break;
                case "event_processed":
                    gameView.highlightEventNotification();
                    break;
                case "game_over":
                    gameView.showGameOver(gameModel.getPlayer().calculateScore());
                    break;
                case "turn_advanced":
                    gameView.updateTurnCounter();
                    break;
            }
        });
    }

    /**
     * Maneja el cierre de la aplicación
     */
    public void handleApplicationExit() {
        if (gameModel != null) {
            // Cleanup del modelo si es necesario
        }
        System.exit(0);
    }

    /**
     * Obtiene el modelo del juego (para que la vista pueda acceder a datos de solo lectura)
     */
    public Game getGameModel() {
        return gameModel;
    }

    /**
     * Verifica si hay un juego activo
     */
    public boolean hasActiveGame() {
        return gameModel != null && gameModel.isGameActive();
    }

    /**
     * Obtiene estadísticas del juego actual
     */
    public String getGameStats() {
        if (gameModel == null) return "No hay juego activo";

        return String.format("Turno: %d | %s | %s",
                gameModel.getCurrentTurn(),
                gameModel.getPlayer().toString(),
                gameModel.getCorruptionTree().getTreeStats()
        );
    }
}

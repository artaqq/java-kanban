package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.ManagerSaveException;
import managers.TaskManager;
import tasks.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubTasksHandler extends BaseHttpHandler {

    public SubTasksHandler(TaskManager tm) {
        super(tm);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");

            switch (method) {
                case "GET": {
                    if (pathParts.length == 2) {
                        String response = gson.toJson(tm.getSubtasks());
                        sendSuccess(exchange, response);
                    } else if (pathParts.length == 3) {
                        Long id = Long.parseLong(pathParts[2]);
                        if (tm.getSubTaskById(id) != null) {
                            String response = gson.toJson(tm.getSubTaskById(id));
                            sendSuccess(exchange, response);
                        } else {
                            sendNotFound(exchange);
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                }
                case "POST": {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    SubTask subTask = gson.fromJson(requestBody, SubTask.class);
                    try {
                        if (tm.getSubTaskById(subTask.getId()) != null) {
                            tm.updateSubTask(subTask);
                            sendCreated(exchange, "Подзадача обновлена");
                        } else {
                            tm.createSubTask(subTask);
                            sendCreated(exchange, "Подзадача создана");
                        }
                    } catch (ManagerSaveException e) {
                        if (e.getMessage().contains("пересекается по времени с другой задачей")) {
                            sendHasOverlaps(exchange);
                        } else {
                            sendInternalError(exchange);
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (pathParts.length == 3) {
                        long id = Long.parseLong(pathParts[2]);
                        if (tm.getSubTaskById(id) != null) {
                            tm.deleteSubTask(id);
                            sendSuccess(exchange, "Подзадача удалена");
                        } else {
                            sendNotFound(exchange);
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                }
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }
}

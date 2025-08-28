package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.ManagerSaveException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager tm) {
        super(tm);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");

            switch (method) {
                case GET:
                    if (pathParts.length == 2) {
                        String response = gson.toJson(tm.getTasks());
                        System.out.println(response);
                        sendSuccess(exchange, response);
                    } else if (pathParts.length == 3) {
                        long id = Long.parseLong(pathParts[2]);
                        Optional<Task> task = Optional.ofNullable(tm.getTaskById(id));
                        if (task.isPresent()) {
                            String response = gson.toJson(task);
                            sendSuccess(exchange, response);
                        } else {
                            sendNotFound(exchange);
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case POST: {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(requestBody, Task.class);
                    try {
                        if (tm.getTaskById(task.getId()) != null) {
                            tm.updateTask(task);
                            sendCreated(exchange, "Задача обновлена");
                        } else {
                            tm.createTask(task);
                            sendCreated(exchange, "Задача создана");
                        }
                    } catch (ManagerSaveException e) {
                        if (e.getMessage().contains("пересекается по времени с другой задачей.")) {
                            sendHasOverlaps(exchange);
                        } else {
                            sendInternalError(exchange);
                        }
                    }
                    break;
                }
                case DELETE: {
                    long id = Long.parseLong(pathParts[2]);
                    if (pathParts.length == 3) {
                        if (tm.getTaskById(id) != null) {
                            tm.deleteTask(id);
                            sendSuccess(exchange, "Задача удалена");
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

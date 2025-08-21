package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager tm) {
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
                        String response = gson.toJson(tm.getEpics());
                        sendSuccess(exchange, response);
                    } else if (pathParts.length == 3) {
                        Long id = Long.parseLong(pathParts[2]);
                        if (tm.getEpicById(id) != null) {
                            String response = gson.toJson(tm.getEpicById(id));
                            sendSuccess(exchange, response);
                        } else {
                            sendNotFound(exchange);
                        }
                    } else if (pathParts.length == 4 && pathParts[3].equals("subtasks")) {
                        Long id = Long.parseLong(pathParts[2]);
                        if (tm.getEpicById(id) != null) {
                            String response = gson.toJson(tm.getEpicSubTasks(tm.getEpicById(id)));
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
                    Epic epic = gson.fromJson(requestBody, Epic.class);
                    System.out.println(epic);
                    if (tm.getEpicById(epic.getId()) != null) {
                        tm.updateEpic(epic);
                        sendCreated(exchange, "Эпик обновлен");
                    } else {
                        tm.createEpic(epic);
                        sendCreated(exchange, "Эпик создан");
                    }
                    break;
                }
                case "DELETE": {
                    Long id = Long.parseLong(pathParts[2]);
                    if (tm.getEpicById(id) != null) {
                        tm.deleteEpic(id);
                        sendSuccess(exchange, "Эпик удален");
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                }
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendText(exchange, e.getMessage(), 500);
            //sendInternalError(exchange);
        }
    }
}

package Http;

import Manager.FileBackedTasksManager;

public class HttpTaskManager extends FileBackedTasksManager {
    int port;

//    public HttpTaskManager() {
//    }

    public HttpTaskManager(int port) {
        this.port = port;
    }

    @Override
    protected void save() {

    }

    @Override
    protected void load() {
        super.load();
    }
}

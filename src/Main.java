public class Main {

    public static void main(String [] args) {
        Window window = new Window();
        WatchThread thread = new WatchThread(window.bgPanel);
        thread.start();
    }
}

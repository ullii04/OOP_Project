package university.patterns;

import university.interfaces.Observable;
import university.interfaces.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Design Pattern #2: Observer
 * Notifies subscribed users about university news/events.
 */
public class NewsPublisher implements Observable {
    private final List<Observer> observers = new ArrayList<>();
    private String latestNews;

    public void publishNews(String news) {
        this.latestNews = news;
        Logger.getInstance().log("News published: " + news);
        notifyObservers(news);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event) {
        observers.forEach(o -> o.update(event));
    }

    public String getLatestNews() { return latestNews; }
}

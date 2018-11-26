package assignment.domain.events;

public class Event<TContext> {
    public Event(TContext context) {
        _context = context;
    }
    
    private TContext _context;

    public TContext getContext() {
        return _context;
    }
}
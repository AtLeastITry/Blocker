package assignment.domain.commands;

public class Command<TContext> {
    public Command(TContext context) {
        _context = context;
    }
    
    private TContext _context;

    public TContext getContext() {
        return _context;
    }
}
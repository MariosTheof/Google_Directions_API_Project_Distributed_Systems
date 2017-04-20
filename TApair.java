public class TApair {
	Thread thread;
	masterActionsForWorkers actions;
	TApair(Thread t, masterActionsForWorkers a){
		this.thread = t;
		this.actions = a;
	}
}

public class TApair {
	Thread thread;
	masterActionsForWorkers actionsw;
	masterActionsForReducer actionsr;
	reducerActionsForWorkers actionsrw;
	TApair(Thread t, masterActionsForWorkers a){
		this.thread = t;
		this.actionsw = a;
	}
	TApair(Thread t, masterActionsForReducer a){
		this.thread = t;
		this.actionsr = a;
	}
	TApair(Thread t, reducerActionsForWorkers a){
		this.thread = t;
		this.actionsrw = a;
	}
	
	
}

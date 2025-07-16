package SameGame;


public class Application {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Model model = new Model(10, 16);
		View view = new View();
		Controller controller = new Controller();

		model.addView(view);
		model.addObserver(view);
		controller.addModel(model);
		controller.addView(view);
		view.addModel(model);
		view.addController(controller);

		view.build();
		view.show();
	}
}

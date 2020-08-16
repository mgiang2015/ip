import java.util.Scanner;
import java.util.ArrayList;

public class Duke {
    private ArrayList<Task> tasks;
    
    public Duke() {
        this.tasks = new ArrayList<>();
    }

    /**
     * get_input() is in charge of asking for input from user
     * and return the input as a string to be used for something else.
     * @param sc Scanner object
     * @return a string representing user input
     */
    public static String get_input(Scanner sc) {
        String user_input = sc.nextLine().trim(); // Read user input
        return user_input;
    }

    /**
     * get_output(String) decides what Hal's output should be
     * based on user's input.
     * @param user_input input from user as String
     * @return output by Hal9000
     */
    public String get_output(Task taskInput) {
        return "added: " + taskInput + "\n"; // level 2
    }

    public static String format_response(String output_msg) {
        return 
        "____________________________________________________________\n"+
        output_msg +
        "____________________________________________________________\n";
    }

    public String summarize() {
        String all_tasks = "Here are the tasks in your list:\n";
        for (int i = 0; i < this.tasks.size(); i++) {
            Task t = tasks.get(i);
            String tick = "";
            if (t.isDone()) {
                tick = "✓";
            } else {
                tick = "✗";
            }

            String label = "";
            if (t.getType() == TaskType.ToDo) {
                label = "T";
            } else if (t.getType() == TaskType.Deadline) {
                label = "D";
            } else {
                label = "E";
            }

            all_tasks += String.format("%d.[%s][%s] %s\n", i+1, label, tick, t.toString());
        }

        return all_tasks;
    }

    public String mark_done(int index) {
        if (!this.tasks.get(index).isDone()) {
            this.tasks.get(index).done();
            return String.format("Nice! I've marked this task as done:\n[✓] %s\n", this.tasks.get(index).getName());
        } else {
            return String.format("Task %d already done!\n", index);
        }
    }

    public static TaskType categorize(String[] input_parts) {
        if (input_parts[0].compareTo("todo") == 0) {
            return TaskType.ToDo;
        } else if (input_parts[0].compareTo("deadline") == 0) {
            return TaskType.Deadline;
        } else {
            return TaskType.Event;
        }
    }

    // Can be used to get the details too
    public static String extractTask(String[] input_parts) {
        String task = "";
        for (int i = 1; i < input_parts.length; i++) {
            task += input_parts[i] + " ";
        }
        return task.trim();
    }
    
    public static Task getTask(String input) {
        String[] parts = input.split(" ");
        TaskType type = categorize(parts);

        if (type == TaskType.ToDo) {
            parts = input.split(" ");
            String name = extractTask(parts);
            
            return new ToDo(name);
        } else if (type == TaskType.Deadline) {
            String name = extractTask(input.split("/")[0].split(" "));
            String deadline = extractTask(input.split("/")[1].split(" "));
            return new Deadline(name, deadline); 
        } else {
            String name = extractTask(input.split("/")[0].split(" "));
            String details = extractTask(input.split("/")[1].split(" "));
            
            return new Event(name, details); // need to insert : after by
        }
    }
    
    public void op() {
        boolean end = false;
        Scanner sc = new Scanner(System.in);
        
        while (!end) {
            String input = get_input(sc);
            String[] parts = input.split(" ");
            if (input.compareTo("bye") == 0) {
                end = true;
            } else if (input.compareTo("list") == 0) {
                System.out.println(format_response(this.summarize()));
            } else if (parts[0].compareTo("done") == 0) {
                System.out.println(format_response(this.mark_done(Integer.parseInt(parts[1]) - 1))); 
            } else {
                Task taskInput = getTask(input);

                // add task to list of tasks
                this.tasks.add(taskInput);
                String output_string = format_response(get_output(taskInput));
                System.out.println(output_string);
            }
        }
        System.out.println(format_response("Bye. Hope to see you again soon!\n"));
    }
    
    public static void main(String[] args) {
        String logo =
"   __ _____   __  ___  ___  ___  ___\n" +
"  / // / _ | / / / _ \\/ _ \\/ _ \\/ _ \\\n" +
" / _  / __ |/ /__\\_, / // / // / // /\n" +
"/_//_/_/ |_/____/___/\\___/\\___/\\___/\n";
                                     
        // Intro message
        System.out.println(logo);
        System.out.println(format_response(
            "Hello! I'm Hal9000\nWhat can I do for you?\n"
        ));

        Duke hal9000 = new Duke();

        hal9000.op();

    }
/*
*/
}

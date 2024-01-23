package com.gbetododc.Test;

import com.gbetododc.MSAuthGraph.JsonTodoHW;
import com.gbetododc.MSAuthGraph.MsGraph;
import com.gbetododc.MSAuthGraph.MsGraph.ToDoHW_Task;

public class Test {
    public static void main(String[] args) {
        ToDoHW_Task todohwtask = JsonTodoHW.getToDoHW().getValue().get(0);
        todohwtask.setStatus("completed");

        MsGraph.updateTask(
            todohwtask
        );
    }
}
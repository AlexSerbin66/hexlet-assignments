package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import exercise.model.Task;
import exercise.repository.TaskRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping(path = "")
    public List<Task> index() {
        return taskRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Task show(@PathVariable long id) {

        var task =  taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));

        return task;
    }

    // BEGIN
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Task task) {
        taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Task task) {
        Task task1 = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        task1.setTitle(task.getTitle());
        task1.setDescription(task.getDescription());
    }

    // END

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        taskRepository.deleteById(id);
    }
}

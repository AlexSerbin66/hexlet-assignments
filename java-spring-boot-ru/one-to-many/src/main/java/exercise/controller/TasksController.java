package exercise.controller;

import java.util.List;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.mapper.TaskMapper;
import exercise.model.User;
import exercise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TasksController {

    // BEGIN

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    private TaskDTO show(@PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task with id " + id + " not found"));
        return taskMapper.map(task);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private List<TaskDTO> showAll() {
        return taskRepository.findAll().stream().map(taskMapper::map).toList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    private void save(@RequestBody TaskCreateDTO dto) {
        taskRepository.save(taskMapper.map(dto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    private void delete(@PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task with id " + id + " not found"));
        taskRepository.delete(task);
    }

    @PutMapping("/{id}")
    private void update(@PathVariable Long id, @RequestBody @Valid TaskUpdateDTO dto) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task with id " + id + " not found"));
        taskMapper.update(dto, task);
        if (dto.getAssigneeId() != null) {
            User newAssignee = new User();
            newAssignee.setId(dto.getAssigneeId()); // Просто ставим ID без проверки в БД
            task.setAssignee(newAssignee);
        }

        taskRepository.save(task);

    }
    // END
}

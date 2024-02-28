package exercise.controller;

import java.util.List;

import exercise.dto.*;
import exercise.mapper.TaskMapper;
import exercise.mapper.UserMapper;
import exercise.model.Task;
import exercise.repository.UserRepository;
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
@RequestMapping("/tasks")
public class TasksController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;



    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TaskMapper taskMapper;

    @GetMapping(path = "")
    public List<TaskDTO> index() {
        var tasks = taskRepository.findAll();
        return tasks.stream()
                .map(p -> taskMapper.map(p))
                .toList();
    }

    @GetMapping(path = "/{id}")
    public TaskDTO show(@PathVariable long id) {

        var task =  taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return taskMapper.map(task);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO taskData) {
        var task = taskMapper.map(taskData);
        taskRepository.save(task);
        var author = task.getAssignee();
        author.addTask(task);
        return taskMapper.map(task);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO update(@PathVariable long id, @Valid @RequestBody TaskUpdateDTO taskData) {
        var task =  taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        var assignee = task.getAssignee();
        taskMapper.update(taskData, task);
        taskRepository.save(task);
        if (assignee.getId() != taskData.getAssigneeId()) {
            assignee.removeTask(task);
            var newAssignee =  userRepository.findById(taskData.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
            newAssignee.addTask(task);
            userRepository.save(assignee);
            userRepository.save(newAssignee);
        }
        return taskMapper.map(task);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        var task =  taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        var author = task.getAssignee();
        author.removeTask(task);
        userRepository.save(author);
    }
}

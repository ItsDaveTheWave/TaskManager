package com.darBootCamp.taskManager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.darBootCamp.taskManager.dto.TaskDto;
import com.darBootCamp.taskManager.model.Task;
import com.darBootCamp.taskManager.repo.TaskRepo;

@RestController
@RequestMapping(value = "task")
public class TaskController {

	@Autowired
	private TaskRepo taskRepo;

	@GetMapping
	public List<TaskDto> getTasks(@RequestParam(required = false, value = "state") String state) {
		
		if(state != null) {
			state = state.toLowerCase();
			
			if (state.equals("pending") || state.equals("0")) {
				return Task.listToDto(taskRepo.findByStateEquals(TaskDto.State.PENDING));
			}
			if (state.equals("progress") || state.equals("in_progress") || state.equals("1")) {
				return Task.listToDto(taskRepo.findByStateEquals(TaskDto.State.IN_PROGRESS));
			}
			if (state.equals("finished") || state.equals("2")) {
				return Task.listToDto(taskRepo.findByStateEquals(TaskDto.State.FINISHED));
			}
		}
		
		return Task.listToDto(taskRepo.findAll());
	}
	
	@PostMapping
	public TaskDto createTask(@Valid @RequestBody TaskDto task) {
		return taskRepo.save(task.toEntity()).toDto();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PatchMapping("{id}")
	public ResponseEntity<TaskDto> patchUpdate(@RequestBody Map<String, Object> updates, @PathVariable("id") String id) {
		
		try {
			Long.parseLong(id);
		}
		catch(NumberFormatException e) {
			return new ResponseEntity("Ivalid Id, must be a positive number", HttpStatus.BAD_REQUEST);
		}
		
		Long idLong = Long.parseLong(id);
		if(idLong <= 0) {
			return new ResponseEntity("Ivalid Id, must be a positive number", HttpStatus.BAD_REQUEST);
		}
		
		Optional<Task> taskOpt = taskRepo.findById(idLong);
		
		if(!taskOpt.isPresent()) {
			return new ResponseEntity("The task with id:" + id + " doesn't exist", HttpStatus.NOT_FOUND);
		}
		
		Task task = taskOpt.get();
		
		if(updates.containsKey("description")) {
			
			if(!(updates.get("description") instanceof String)) {
				return new ResponseEntity("Invalid description, must be a string", HttpStatus.BAD_REQUEST);
			}
			String description = (String) updates.get("description");
			if(description.isEmpty()) {
				return new ResponseEntity("Invalid description, can't be empty", HttpStatus.BAD_REQUEST);
			}
			
			task.setDescription(description);
		}
		
		if(updates.containsKey("state")) {
			if(!(updates.get("state") instanceof String)) {
				return new ResponseEntity("Invalid state, must be a string", HttpStatus.BAD_REQUEST);
			}
			
			String state = ((String) updates.get("state")).toLowerCase();
			
			if (state.equals("pending") || state.equals("0")) {
				task.setState(TaskDto.State.PENDING);
			}
			else if (state.equals("progress") || state.equals("in_progress") || state.equals("1")) {
				task.setState(TaskDto.State.IN_PROGRESS);
			}
			else if (state.equals("finished") || state.equals("2")) {
				task.setState(TaskDto.State.FINISHED);
			}
			else {
				return new ResponseEntity("Invalid state", HttpStatus.BAD_REQUEST);
			}
		}
		
		return new ResponseEntity<TaskDto>(taskRepo.save(task).toDto(), HttpStatus.OK);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable("id") String id) {
		
		try {
			Long.parseLong(id);
		}
		catch(NumberFormatException e) {
			return new ResponseEntity<String>("Ivalid Id, must be a positive number", HttpStatus.BAD_REQUEST);
		}
		
		Long idLong = Long.parseLong(id);
		if(idLong <= 0) {
			return new ResponseEntity<String>("Ivalid Id, must be a positive number", HttpStatus.BAD_REQUEST);
		}
		
		Optional<Task> taskOpt = taskRepo.findById(idLong);
		
		if(!taskOpt.isPresent()) {
			return new ResponseEntity<String>("The task with id:" + id + " doesn't exist", HttpStatus.NOT_FOUND);
		}
		
		taskRepo.deleteById(idLong);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return errors;
	}
}
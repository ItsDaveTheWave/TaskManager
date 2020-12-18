package com.darBootCamp.taskManager.dto;

import com.darBootCamp.taskManager.model.Task;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {

	public enum State {
		PENDING,
		IN_PROGRESS,
		FINISHED
	}
	
	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	
	@NotNull
	@NotBlank
	private String description;
	
	@JsonProperty(access = Access.READ_ONLY)
	private State state = State.PENDING;
	
	public TaskDto() {}
	public TaskDto(Long id, String description, State state) {
		this.id = id;
		this.description = description;
		this.state = state;
	}
	
	public TaskDto(String description, State state) {
		this.description = description;
		this.state = state;
	}
	
	public Task toEntity() {
		return new Task(id, description, state);
	}
	
	public static List<Task> listToEntity(List<TaskDto> taskDtoList) {
		List<Task> taskList = new ArrayList<Task>();
		
		for(Task task : taskList) {
			taskDtoList.add(task.toDto());
		}
		
		return taskList;
	}
}
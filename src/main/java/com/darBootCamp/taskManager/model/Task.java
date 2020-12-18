package com.darBootCamp.taskManager.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.darBootCamp.taskManager.dto.TaskDto;
import com.darBootCamp.taskManager.dto.TaskDto.State;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 256, nullable = false)
	private String description;
	
	@Column(nullable = false)
	private State state = State.PENDING;
	
	public Task() {}
	public Task(Long id, String description, State state) {
		this.id = id;
		this.description = description;
		this.state = state;
	}
	public Task(String description, State state) {
		this.description = description;
		this.state = state;
	}
	
	public TaskDto toDto() {
		return new TaskDto(id, description, state);
	}
	
	public static List<TaskDto> listToDto(List<Task> taskList) {
		List<TaskDto> taskDtoList = new ArrayList<TaskDto>();
		
		for(Task task : taskList) {
			taskDtoList.add(task.toDto());
		}
		
		return taskDtoList;
	}
}
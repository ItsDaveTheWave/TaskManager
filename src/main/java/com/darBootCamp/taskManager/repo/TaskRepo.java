package com.darBootCamp.taskManager.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.darBootCamp.taskManager.dto.TaskDto;
import com.darBootCamp.taskManager.model.Task;

public interface TaskRepo extends JpaRepository<Task, Long> {
	
	List<Task> findByStateEquals(TaskDto.State state);
}
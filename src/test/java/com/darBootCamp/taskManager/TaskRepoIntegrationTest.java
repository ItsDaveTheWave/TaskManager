package com.darBootCamp.taskManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.darBootCamp.taskManager.dto.TaskDto;
import com.darBootCamp.taskManager.model.Task;
import com.darBootCamp.taskManager.repo.TaskRepo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;


@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskRepoIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepo taskRepo;

    @Test
    public void whenFindById_thenReturnTask() {
        Task task = new Task("This is a description", TaskDto.State.PENDING);
        entityManager.persist(task);
        entityManager.flush();

        Optional<Task> foundTaskOpt = taskRepo.findById(2L);

        assertThat(foundTaskOpt.isPresent()).isEqualTo(true);
        
        Task foundTask = foundTaskOpt.get();
        
        assertThat(foundTask.getDescription())
          .isEqualTo(task.getDescription());
    }

}
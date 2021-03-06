package com.teamtreehouse.service;

import com.teamtreehouse.dao.InstructionDao;
import com.teamtreehouse.domain.Instruction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InstructionServiceTest {

    @Mock
    private InstructionDao dao;

    @InjectMocks
    private InstructionService service = new InstructionServiceImpl();

    @Test
    public void findAll_ShouldReturnTwo() throws Exception {
        List<Instruction> instructions = Arrays.asList(
                new Instruction(),
                new Instruction()
        );

        when(dao.findAll()).thenReturn(instructions);

        assertEquals("findAll should return two instructions", 2, service.findAll().size());
        verify(dao).findAll();
    }

    @Test
    public void findById_ShouldReturnOne() throws Exception {
        when(dao.findOne(1L)).thenReturn(new Instruction());
        assertThat(service.findById(1L), instanceOf(Instruction.class));
        verify(dao).findOne(1L);
    }

    @Test
    public void saveInstructionTest() throws Exception {
        Instruction instruction = new Instruction();
        instruction.setId(1L);
        instruction.setDescription("test");

        when(dao.findOne(1L)).thenReturn(instruction);
        Instruction result = service.findById(1L);

        assertEquals(1, result.getId().intValue());
        assertEquals("test", result.getDescription());
        verify(dao).findOne(1L);
    }

    @Test
    public void deleteInstructionTest() throws Exception {
        Instruction instruction = new Instruction();

        when(dao.findOne(1L)).thenReturn(instruction);
        service.delete(instruction);
        verify(dao, times(1)).delete(instruction);
    }
}

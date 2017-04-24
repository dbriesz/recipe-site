package com.teamtreehouse.service;

import com.teamtreehouse.dao.InstructionDao;
import com.teamtreehouse.domain.Instruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructionServiceImpl implements InstructionService {
    @Autowired
    private InstructionDao instructionDao;

    @Override
    public List<Instruction> findAll() {
        return (List<Instruction>) instructionDao.findAll();
    }

    @Override
    public Instruction findById(Long id) {
        return instructionDao.findOne(id);
    }

    @Override
    public void save(Instruction instruction) {
        instructionDao.save(instruction);
    }

    @Override
    public void delete(Instruction instruction) {
        instructionDao.delete(instruction);
    }
}

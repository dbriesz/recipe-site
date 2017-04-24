package com.teamtreehouse.service;

import com.teamtreehouse.domain.Instruction;

import java.util.List;

public interface InstructionService {
    List<Instruction> findAll();
    Instruction findById(Long id);
    void save(Instruction instruction);
    void delete(Instruction instruction);
}

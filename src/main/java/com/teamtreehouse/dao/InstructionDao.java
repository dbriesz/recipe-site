package com.teamtreehouse.dao;

import com.teamtreehouse.domain.Instruction;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InstructionDao extends PagingAndSortingRepository<Instruction, Long> {
}

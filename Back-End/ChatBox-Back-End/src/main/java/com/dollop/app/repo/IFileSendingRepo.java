package com.dollop.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.app.data.chat.FileSending;

public interface IFileSendingRepo extends JpaRepository<FileSending, Long>{

}

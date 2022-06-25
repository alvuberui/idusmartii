package org.springframework.idusmartii.board;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.idusmartii.match.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {


	private BoardRepository boardRepo;
	
	@Autowired
	public BoardService(BoardRepository boardRepository) {
		this.boardRepo = boardRepository;
	}
	
	@Transactional
	public int boardCount() {
		return (int) boardRepo.count();
	}

	@Transactional
	public Iterable<Board> findAll(){
		return boardRepo.findAll();
	}

	public Optional<Board> findById(Integer id){
		return boardRepo.findById(id);
	}


	@Transactional
	public void saveBoard(Board newBoard) throws DataAccessException{
		boardRepo.save(newBoard);
	}
    @Transactional
    public void deleteBoard(Integer boardId) throws DataAccessException{
        boardRepo.deleteById(boardId);
    }
}

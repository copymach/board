package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;

import static com.mysema.commons.lang.Assert.assertThat;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")
    public String boardWriteForm() {
        return "boardWrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board , Model model, MultipartFile file) throws Exception {

        boardService.write(board , file);
        System.out.printf("board "+board);

        model.addAttribute("message" , "글 작성이 완료됨");
        model.addAttribute("searchUrl" , "/board/list");

        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model ) {
        model.addAttribute("list", boardService.boardList());
        return "boardlist";
    }

    @GetMapping("/board/view")
    public String boardView(Model model, Integer id) {

        model.addAttribute("board" , boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id , Model model) {
        model.addAttribute("board" , boardService.boardView(id));
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate (@PathVariable("id") Integer id , Board board , MultipartFile file) throws Exception {
        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp , file);

        return "redirect:/board/list";
    }



    @Test
    public void startJPQL() {
        //게시물1을 찾아라.
        String qlString =
                "select * from board bd " +
                        "where bd.id = :id";

        Board findBoard = bd.createQuery(qlString, Board.class)
                .setParameter("id", "1")
                .getSingleResult();

        assertThat(findBoard.getId()).isEqualTo("1");
    }


    @Test
    public void startQuerydsl() {
        //1번 게시물을 찾아라.
        Board findBoard = queryFactory
                .select(id)
                .from(id)
                .where(board.id.eq("1"))//파라미터 바인딩 처리
                .fetchOne();

        assertThat(findBoard.getId()).isEqualTo("1");
    }


}

package com.my.column.controller.rest;

import com.my.column.common.Constants;
import com.my.column.entity.*;
import com.my.column.service.IFilmService;
import com.my.column.service.SearchService;
import com.my.column.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/film")
public class FilmController {

	@Autowired
	private IFilmService filmService;
	@Autowired
	private UserService userService;
	/**
	 * @RequestMapping(value="/showFilm") public String showFilm(Model model) {
	 *                                    List<Film>
	 *                                    fs=filmService.getFilmUnLucene();
	 *                                    model.addAttribute("films",fs); return
	 *                                    "index"; }
	 **/

	@GetMapping({"/search", "/search.html"})
	public String loginPage() {
		return "search";
	}
//	@RequestMapping(value = "/searchFilm", produces = "text/html;charset=UTF-8")
	@RequestMapping(value = "/searchFilm")
	@ResponseBody
	public SearchResult<Film> searchFilm(Model model, String search_str, int start, int limits) {
		SearchResult<Film> fs = SearchService.searchFilm(search_str, start, limits);
		return fs;
	}

	@RequestMapping(value = "/searchActor")
	public @ResponseBody SearchResult<Actor> searchActor(Model model, String search_str, int start, int limits) {
		SearchResult<Actor> as = SearchService.searchActor(search_str, start, limits);
		return as;
	}

	@GetMapping("/updateFilm")
	public String articleDetailPage(HttpServletRequest request) {
		filmService.getFilmLucene();
		return "success";
	}

	@GetMapping("/films")
	public String showFilms(@RequestParam(required = false) String keyword, Model model) {
		List<Film> films;
		if (keyword != null && !keyword.isEmpty()) {
			films = filmService.getFilmsByKeyword(keyword);
		} else {
			films = filmService.buildHot20();
		}
		model.addAttribute("films", films);
		return "films"; // 返回 Thymeleaf 模板名称
	}

	@GetMapping("/film/{filmId}")
	public String filmDetailPage(HttpServletRequest request, @PathVariable("filmId") String filmId,HttpSession httpSession) {
		System.out.println("receive"+filmId);
		//文章信息
		UserEntity loginUser = (UserEntity) httpSession.getAttribute(Constants.USER_SESSION_KEY);


		Film film = filmService.getFilmById(filmId);
		filmService.insertFilmAccessRecord(filmId,loginUser.getUserId(),new Date());

		if (film == null) {
			return "error/error_404";
		}

		request.setAttribute("film", film);
		return "film-detail";
	}
}

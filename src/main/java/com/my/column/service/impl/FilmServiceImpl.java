package com.my.column.service.impl;

import com.my.column.dao.CommentMapper;
import com.my.column.dao.FilmAccessRecordMapper;
import com.my.column.entity.*;
import com.my.column.service.IFilmService;
import com.my.column.util.MovieEventSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.my.column.dao.IActorMapper;
import com.my.column.dao.IFilmMapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class FilmServiceImpl implements IFilmService {
	@Autowired
	private IFilmMapper filmDao;

	@Autowired
	private MovieEventSender movieEventSender;

	@Autowired
	private IActorMapper actorDao;

	@Autowired
	private FilmAccessRecordMapper fileRecoedDAO;

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Override
	public Film getFilmById(String film_id) {
		List<Film> list=filmDao.getFilmById(film_id);
		if(list==null ||list.size()==0)return null;
		return list.get(0);
	}

	@Override
	public List<Film> getFilmsByKeyword(String keyword){
		List<Film> filmList = filmDao.getFilmByKeyWord(keyword);
		return filmList;
	}

	public List<Film> getAllFilms(){
		List<Film> filmList = filmDao.getAllFilms();
		return filmList;
	}
	
	@Override
	public List<Film> getFilmUnLucene() {
		// TODO Auto-generated method stub
		return filmDao.getFilmUnLucene();
	}

	@Override
	public void insertFilmAccessRecord(String film_id, Long user_id, Date access_time){
		movieEventSender.sendMovieViewEvent(film_id,String.valueOf(user_id));
		FilmAccessRecord newRecord = new FilmAccessRecord(film_id,String.valueOf(user_id),access_time);
		fileRecoedDAO.insertFilmAccessRecord(newRecord);
	}
	@Override
	public void updateFilmLucene(String film_id) {
		// TODO Auto-generated method stub
		filmDao.updateFilmLucene(film_id);
	}

	@Cacheable(value = "hot10Film", unless = "#result == null")
	public List<Film> buildHot20(){

		List<Film> filmHot20List= new ArrayList<Film>();
		List<Map<String,Long>> countMapList = fileRecoedDAO.getFilmFrequencyInLast20Hours();
		Map<String, Integer> countMap = new HashMap<>();
		for (Map<String,Long> count:countMapList) {
			Integer value =(int) (long)count.get("frequency");
			countMap.put(String.valueOf(count.get("film_id")),value);
		}

		PriorityQueue<Map.Entry<String, Integer>> minHeap = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

		for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
			minHeap.offer(entry);
			if (minHeap.size() > 10) {
				minHeap.poll();
			}
		}

		List<String> top10FilmIds = new ArrayList<>();
		List<Film> top10Film = new ArrayList<>();
		while (!minHeap.isEmpty()) {
			top10FilmIds.add(minHeap.poll().getKey());
		}

		Collections.reverse(top10FilmIds); // 如果要按照 count 从大到小的顺序，可以反转列表
		System.out.println("Top 10 film ids by count: " + top10FilmIds);

		for(String id:top10FilmIds){
			Film film = filmDao.getFilmById(id).get(0);
			top10Film.add(film);
		}

		redisTemplate.opsForValue().set("hot10FilmMinHeap", minHeap, 1, TimeUnit.HOURS);
		redisTemplate.opsForValue().set("hot10Film", top10Film, 1, TimeUnit.HOURS);
		return top10Film;
	}

	public final static String targetDir="D:\\files\\project\\code\\douban_film";
	public void getFilmLucene() {
		IndexWriterConfig config=new IndexWriterConfig(new IKAnalyzer());
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		IndexWriter writer=null;
		System.out.println("执行任务-----------------");
		try {
			Directory d=FSDirectory.open(Paths.get(targetDir));
			writer=new IndexWriter(d,config);
			List<Film> films=filmDao.getFilmUnLucene();
			System.out.println("index film..."+films.size());
			for(Film film :films) {
				Document doc=new Document();
				doc.add(new TextField("film_id",check(film.getFilm_id()),Field.Store.YES));
				doc.add(new TextField("film_name",check(film.getFilm_name()),Field.Store.YES));
				doc.add(new TextField("director",check(film.getDirector()),Field.Store.YES));
				doc.add(new TextField("screenwriter",check(film.getScreenwriter()),Field.Store.YES));
				doc.add(new TextField("mainactors",check(film.getMainactors()),Field.Store.YES));
				doc.add(new TextField("film_type",check(film.getFilm_type()),Field.Store.YES));
				doc.add(new TextField("area",check(film.getArea()),Field.Store.YES));
				doc.add(new TextField("lang",check(film.getLang()),Field.Store.YES));
				doc.add(new TextField("film_date",check(film.getFilm_date()),Field.Store.YES));
				doc.add(new TextField("film_time",check(film.getFilm_time()),Field.Store.YES));
				doc.add(new TextField("film_alias",check(film.getFilm_alias()),Field.Store.YES));
				doc.add(new TextField("film_summary",check(film.getFilm_summary()),Field.Store.YES));
				doc.add(new TextField("score",check(film.getScore()),Field.Store.YES));
				writer.addDocument(doc);
				filmDao.updateFilmLucene(film.getFilm_id());
			}
			System.out.println("done.");
			System.out.println("index actor...");
			List<Actor> actors=actorDao.getActorUnLucene();
			for(Actor actor:actors) {
				Document doc=new Document();
				doc.add(new TextField("actor_id",check(actor.getActor_id()),Field.Store.YES));
				doc.add(new TextField("main_works",check(actor.getMain_works()),Field.Store.YES));
				doc.add(new TextField("actor_name",check(actor.getActor_name()),Field.Store.YES));
				doc.add(new TextField("gender",check(actor.getGender()),Field.Store.YES));
				doc.add(new TextField("xingzuo",check(actor.getXingzuo()),Field.Store.YES));
				doc.add(new TextField("birthday",check(actor.getBirthday()),Field.Store.YES));
				doc.add(new TextField("birtharea",check(actor.getBirtharea()),Field.Store.YES));
				doc.add(new TextField("occupation",check(actor.getOccupation()),Field.Store.YES));
				doc.add(new TextField("more_name",check(actor.getMore_name()),Field.Store.YES));
				doc.add(new TextField("more_foreign_name",check(actor.getMore_foreign_name()),Field.Store.YES));
				doc.add(new TextField("web_url",check(actor.getWeb_url()),Field.Store.YES));
				doc.add(new TextField("imdb_id",check(actor.getImdb_id()),Field.Store.YES));
				writer.addDocument(doc);
				actorDao.updateActorLucene(actor.getActor_id());
			}
			System.out.println("done.");
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				}catch(IOException e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	String check(String s) {
		return s==null?"":s;
	}

	public MovieUserMatrix generateMatrix(){
		Map<String,Integer> movieMap = new HashMap<>();
		Map<String,Integer> userMap = new HashMap<>();
		List<List<Integer>> matrix = new ArrayList<>();
		List<Comment> fileRecordList = commentMapper.getAllComment();

		MovieUserMatrix res = new MovieUserMatrix(movieMap,userMap,matrix);
		return res;
	}
}

package com.my.column.quartz;

import com.my.column.entity.Actor;
import com.my.column.entity.Film;
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
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Component
public class LuceneTask {
	@Resource
	private IFilmMapper filmDao;
	@Resource
	private IActorMapper actorDao;
	public final static String targetDir="D:\\files\\project\\code\\douban_film";
	public void run() {
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
}

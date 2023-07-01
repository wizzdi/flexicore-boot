package com.wizzdi.flexicore.boot.data.rest.plugin;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.data.rest.app.Book;
import org.pf4j.Extension;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(collectionResourceRel = "books", path = "books")
@Extension
public interface BookRepository extends Plugin,PagingAndSortingRepository<Book, Long> {

	List<Book> findByName( @Param("name") String name);


}

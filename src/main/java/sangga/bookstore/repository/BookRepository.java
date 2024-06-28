package sangga.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sangga.bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{
}

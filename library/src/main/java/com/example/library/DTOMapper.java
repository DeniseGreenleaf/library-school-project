package com.example.library;

import com.example.library.Books.Book;
import com.example.library.User.User;

import java.util.List;
import java.util.stream.Collectors;

public class DTOMapper {

    public static BookWithDetailsDTO toBookDTO(Book book) {
        return new BookWithDetailsDTO(
                book.getBookId(),
                book.getTitle(),
                book.getAuthorName() !=null ? book.getAuthorName() : "Unknown Author",
                book.getAvailableCopies()
        );
    }

    // Manuell mappning för User (utan password)
    public static UserDto toUserDTO(User user) {
        return new UserDto(
                user.getUserId(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail()
        );
    }


    //Stream API för Book-listor
    public static List<BookWithDetailsDTO> toBookDTOList(List<Book> books) {
        return books.stream()
                .map(DTOMapper::toBookDTO)
                .collect(Collectors.toList());
    }

    // Stream API för User-listor
    public static List<UserDto> toUserDTOList(List<User> users) {
        return users.stream()
                .map(DTOMapper::toUserDTO)
                .collect(Collectors.toList());
    }


}

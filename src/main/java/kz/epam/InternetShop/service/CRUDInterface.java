package kz.epam.InternetShop.service;

public interface CRUDInterface<E> {

    E save(E e);

    void delete(E e);
}

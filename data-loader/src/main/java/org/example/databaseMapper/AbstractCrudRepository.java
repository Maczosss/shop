package org.example.databaseMapper;

import com.google.common.base.CaseFormat;
import org.atteo.evo.inflector.English;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.argument.internal.NamedArgumentFinderFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class AbstractCrudRepository<T, ID> implements CrudRepository<T, ID>{

    protected final Jdbi jdbi;

    protected AbstractCrudRepository(Jdbi jdbi){
        this.jdbi = jdbi;
    }

    @SuppressWarnings("unchecked")
    private final Class<T> entityType = (Class<T>)
            ((ParameterizedType) super.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public Optional<T> save(T objectToSave) {
        var insertedRow = new AtomicReference<Optional<T>>(Optional.empty());
        jdbi.useTransaction(transaction -> {
            var sql = "insert into %s %s values %s;".formatted(
                    getTableName(),
                    getColumnNamesForInsert(),
                    getValuesForInsert(objectToSave)
            );
            var insertedRowNumber =
                    jdbi.withHandle(handle -> handle.execute(sql));
            System.out.println(sql);
            System.out.println(insertedRowNumber);
            if(insertedRowNumber>0){
                insertedRow.set(
                        findLast(1).stream().findFirst()
                );
            }
        });
        return insertedRow.get();
    }

    public List<T> saveAll(List<T> objectsToSave){
        var insertedRows = new AtomicReference<List<T>>(List.of());
        jdbi.useTransaction(transaction -> {
            var sql = "insert into %s %s values %s;".formatted(
                    getTableName(),
                    getColumnNamesForInsert(),
                    objectsToSave.stream()
                            .map(this::getValuesForInsert)
                            .collect(Collectors.joining(", "))
            );
            var insertedRowCount = jdbi.withHandle(
                    handle ->
                            handle.execute(sql));
            if(insertedRowCount>0){
                insertedRows.set(findLast(insertedRowCount));
            }
        });
        return insertedRows.get();
    }

    public List<T> findLast(int numberOfRows){
        var sql = "select * from %s limit %d;".formatted(
                getTableName(),
                numberOfRows
        );

        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .mapToBean(entityType)
                .list()
        );
    }

    public List<T> getByFields(List<TempField> fields){
        var sql = "select * from %s where %s;".formatted(
                getTableName(),
                whereSectionForSpecificField(fields)
        );

        var list = jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .mapToBean(entityType)
                .list()
        );
        return list;
    }

    public Optional<T> findById(ID id) {
        var sql = "select * from %s where %s;".formatted(
                getTableName(),
                whereSection(id)
        );
        System.out.println(sql);
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .mapToBean(entityType)
                .findFirst());
    }


    @Override
    public List<T> getAll() {
        var sql = "select * from %s;".formatted(
                getTableName()
        );
        System.out.println(sql);
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .mapToBean(entityType)
                .list());
    }

    private String getTableName(){
        return English.plural(
                CaseFormat.UPPER_CAMEL
                        .to(CaseFormat.LOWER_UNDERSCORE, entityType.getSimpleName()));
    }

    private String getColumnNamesForInsert(){
        var fieldsNames = Arrays
                .stream(entityType.getDeclaredFields())
                .map(Field::getName)
                .filter(field -> !field.equalsIgnoreCase("id"))
                .collect(Collectors.joining(", "));

        return "( %s )".formatted(fieldsNames);
    }

    private String getValuesForInsert(T t){
        var values = Arrays.stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().equalsIgnoreCase("id"))
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        if(field.get(t)==null){
                            return "NULL";
                        }
                        if(List.of(String.class, Enum.class, LocalDate.class).contains(field.getType())){
                            return "'%s'".formatted(field.get(t));
                        }
                        return field.get(t).toString();
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e.getMessage());
                    }
                })
                .collect(Collectors.joining(", "));
        return "( %s )".formatted(values);
    }

    private String getColumnsAndValuesForUpdate(T t){
        var values = Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> {
                    field.setAccessible(true);

                    try{
                        return !field.getName().equalsIgnoreCase("id")
                                && field.get(t) != null;
                    }catch (Exception e){
                        throw new IllegalArgumentException(e.getMessage());
                    }
                })
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        if(List.of(String.class, Enum.class, LocalDate.class)
                                .contains(field.getType())){
                            return "%s = '%s'".formatted(field.getName(), field.get(t));
                        }
                        return field.getName() + " = " + field.get(t).toString();
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e.getMessage());
                    }
                })
                .collect(Collectors.joining(", "));
        return "%s".formatted(values);
    }

    private String whereSection(ID id){
        if(!id.toString().matches("\\d+")){
            throw new IllegalArgumentException("Id is not correct");
        }
        return "id = %s".formatted(id);
    }

    //overdone with new class, but wanted to test record pattern
    private String whereSectionForSpecificField(List<TempField> fields) {
        var result = new StringBuilder();
        for (TempField(String name, String value) : fields) {
            if (name.matches("\\d+")) {
                throw new IllegalArgumentException("field have to be different than id");
            }
            result.append("%s = '%s' and ".formatted(name, value));
        }
        return "( %s )".formatted(result.substring(0, (result.length()-5)));
    }
}

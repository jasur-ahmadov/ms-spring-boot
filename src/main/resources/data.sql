-- noinspection SqlNoDataSourceInspectionForFile

select s.*
from student s;

insert into student(name, surname, age)
values ('Jasur', 'Ahmadov', 24);

drop table student;

truncate table student;

delete
from student
where id = 1;

select s.name,
       sb.book_name,
       sb.student_id
from student s
         inner join student_book sb on s.id = sb.student_id;

select book_name,
       count(*) as count_
from student
group by book_name
having count_ > 2;

select *
from student
where id in (select student_id from student_book where book_name = 'Java');

select lower(name)
from student;

select upper(name)
from student;

select *
from student
where length(name) < 10;
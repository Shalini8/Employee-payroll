#UC2 : Retrieve data and check no of person present in database:
mysql> select * from employeepayroll1;
+----+---------+--------+------------+
| id | name    | salary | start      |
+----+---------+--------+------------+
|  1 | Shalini | 800000 | 2020-03-04 |
|  2 | Meena   |   8000 | 2020-08-06 |
|  3 | Aman    |  60000 | 2020-03-12 |
|  4 | Reena   | 450000 | 2021-05-19 |
|  7 | Shaan   |   4500 | 2021-05-02 |
+----+---------+--------+------------+
#UC3: update salary of Meena:
mysql> select * from employeepayroll1;
+----+---------+---------+------------+
| id | name    | salary  | start      |
+----+---------+---------+------------+
|  1 | Shalini |  800000 | 2020-03-04 |
|  2 | Meena   | 3000000 | 2020-08-06 |
|  3 | Aman    |   60000 | 2020-03-12 |
|  4 | Reena   |  450000 | 2021-05-19 |
|  7 | Shaan   |    4500 | 2021-05-02 |
+----+---------+---------+------------
#UC6: updated gender
mysql> select * from employeepayroll1;
+----+---------+---------+------------+--------+
| id | name    | salary  | start      | gender |
+----+---------+---------+------------+--------+
|  1 | Shalini |  800000 | 2020-03-04 | F      |
|  2 | Meena   | 3000000 | 2020-08-06 | F      |
|  3 | Aman    |   60000 | 2020-03-12 | M      |
|  4 | Reena   |  450000 | 2021-05-19 | F      |
|  7 | Shaan   |    4500 | 2021-05-02 | M      |
+----+---------+---------+------------+--------+
#UC8: Ability to add payrolldetails when new employee is added to the employeepayroll1;

mysql> select * from payrolldetails;
+-------------+-----------+------------+-------------+---------+----------+
| employee_id | basic_pay | deductions | taxable_pay | tax     | net_pay  |
+-------------+-----------+------------+-------------+---------+----------+
|          15 |  50000000 |   10000000 |    40000000 | 4000000 | 46000000 |
+-------------+-----------+------------+-------------+---------+----------+
#Delete CASCADE::
mysql> select * from employeepayroll1;
+----+---------+----------+------------+--------+
| id | name    | salary   | start      | gender |
+----+---------+----------+------------+--------+
|  1 | Shalini |   800000 | 2020-03-04 | F      |
|  2 | Meena   |  3000000 | 2020-08-06 | F      |
|  3 | Aman    |    60000 | 2020-03-12 | M      |
|  4 | Reena   |   450000 | 2021-05-19 | F      |
|  7 | Shaan   |     4500 | 2021-05-02 | M      |
| 15 | Zoya    | 50000000 | 2021-05-23 | F      |
+----+---------+----------+------------+--------+
6 rows in set (0.00 sec)

mysql> DELETE FROM employeepayroll1 where name='Zoya';
Query OK, 1 row affected (0.26 sec)

mysql> select * from payrolldetails;
Empty set (0.03 sec)


--BEGIN USER SERVICE
-- id is universal for all user types!!!
--Table user
create table user(
username varchar(25) NOT NULL UNIQUE,
password varchar(25) NOT NULL UNIQUE,
email varchar(25) NOT NULL UNIQUE,
id int,
usertype varchar(20) NOT NULL, --Based on user type it is understood where user has access
PRIMARY KEY (id)
);

--Table for Services
create table service(
service_name varchar(20),--services can be pages or collections or tables etc.
usertype varchar(20),--user type is a USER GROUP
access_type varchar(10),--access type that is added by sysadmin for evaluations
PRIMARY KEY (service_name)
);

--BEGIN USER GROUP related tables
--each is related to a default user group
--Table customers (registered workshops)
create table customer(
id int,
WorkShop_Name varchar(25) NOT NULL,
AFM int NOT NULL UNIQUE,
WorkShop_Supervisor varchar(25),
telephone int,
FOREIGN KEY (id) REFERENCES user(id)
);

--Table companies (list of companies supported by workshop)
create table company(
workshop_id int NOT NULL,
company int NOT NULL
);

--Table of simple employees
create table employee(
id int,
super_id int,--the employee's supervisor
Name varchar(25) NOT NULL,
FOREIGN KEY (id) REFERENCES user(id)
);

--Table of supervisors
create table supervisor(
id int,
Name varchar(25) NOT NULL,
FOREIGN KEY (id) REFERENCES user(id)
);

--Table of warehouse keepers
create table warehouse_keeper(
id int,
shop varchar(10) NOT NULL,--shop where the warehouse is located
Name varchar(25) NOT NULL,
FOREIGN KEY (id) REFERENCES user(id)
);
--END USER GROUP related tables
--END USER SERVICE

--Table of Spare Parts
create table spare_part(
part_id int,
availability int,
name varchar(25) NOT NULL,
manufacturer varchar(25) NOT NULL,
price decimal(6,2) NOT NULL,
part_type varchar(5) ,
CONSTRAINT chk_part_type CHECK ( type IN ('used','new') ),
PRIMARY KEY (part_id)
);

--Table of supported cars for each spare part
create table supported_car(
part_id int NOT NULL,--spare part
model varchar(25) NOT NULL
);

--Tables for transactions
create table transactions(
trans_id int,
amount decimal(8,2) NOT NULL,
customer_id int NOT NULL,
status varchar(10) NOT NULL,--the phase in which it is
supervisor_id int,--supervisor who authorizes discount(if null there is no discount)
employee_id int,--employee who does a physical transaction (if null it is not physical)
est_date date,
entry_date date NOT NULL,
PRIMARY KEY (trans_id)
);

--parts for each transaction
create table part_trans(
trans_id int,
quantity int,
part_id int,
remaining int,
FOREIGN KEY (trans_id) REFERENCES transactions(trans_id),
FOREIGN KEY (part_id) REFERENCES spare_part(part_id)
);
